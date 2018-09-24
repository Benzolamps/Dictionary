package file_operation;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

import dictionary.DicLib;
import main.MainFrame;

/**
 * 备份还原界面是JFrame的子类，实现了ActionListener和Runnable接口
 * 
 * @author Benzolamps
 *
 */
public class RecoverFrame extends JFrame implements ActionListener, Runnable {
	private static final long serialVersionUID = 4011819573993165113L;
	private JTextField libText; // 显示词库名
	private JLabel progressLabel; // 显示备份还原进度
	private JButton cancelButton;
	private JButton okButton;
	private MainFrame frame; // 主界面
	private String fileStr; // 已选择的文件字符串
	private boolean isSave; // 说明操作是否是将词库写入文件，是则其值为true
	private StringBuffer progress = new StringBuffer(); // 存储备份还原进度字符串的缓冲区
	private Timer timer; // 定时器，用于实时更新备份还原进度

	public RecoverFrame(MainFrame frame, String fileStr, final boolean IS_SAVE) throws IOException {
		// super("备份还原词库");
		super(IS_SAVE ? "备份" : "还原" + "词库");
		this.frame = frame;
		this.fileStr = fileStr;
		isSave = IS_SAVE;
		setLayout(null);
		setLocationRelativeTo(null);
		setSize(360, 200);
		setVisible(true);
		setResizable(false);
		getContentPane().add(createLibText());
		getContentPane().add(createProgressLabel());
		getContentPane().add(createCancelButton());
		getContentPane().add(createOKButton());
		timer = new Timer(1, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 响应timer事件
				progressLabel.setText((isSave ? "备份" : "还原") + "进度：" + progress.toString());
			}
		});
		timer.start(); // 定时器开始工作
	}

	private JTextField createLibText() throws IOException {
		JLabel l = new JLabel("词库名：");
		l.setBounds(10, 10, 85, 20);
		getContentPane().add(l);

		libText = new JTextField();
		libText.setBounds(100, 10, getContentPane().getWidth() - 110, 20);
		// libText显示词库名
		if (!isSave) {
			libText.setText(new DicFile(fileStr, isSave).getDicName());
		} else {
			libText.setText(frame.getCurrentDicLib().getName());
		}
		libText.setEditable(false);

		return libText;
	}

	private JLabel createProgressLabel() {
		progressLabel = new JLabel();
		progressLabel.setBounds(10, 60, getContentPane().getWidth() - 10, 50);

		return progressLabel;
	}

	public JButton createCancelButton() {
		cancelButton = new JButton("取消");
		cancelButton.setMnemonic(KeyEvent.VK_C);
		Point pt = new Point();
		pt.x = 10;
		pt.y = getContentPane().getHeight() - 30;
		cancelButton.setBounds(new Rectangle(pt, new Dimension(80, 20)));
		cancelButton.addActionListener(this);
		return cancelButton;
	}

	private JButton createOKButton() {
		okButton = new JButton("确定");
		okButton.setMnemonic(KeyEvent.VK_O);
		Point pt = new Point();
		pt.x = getContentPane().getWidth() - 90;
		pt.y = getContentPane().getHeight() - 30;
		okButton.setBounds(new Rectangle(pt, new Dimension(80, 20)));
		okButton.addActionListener(this);
		return okButton;
	}

	@Override
	/**
	 * 重写ActionListenener的actionPerformed(ActionEvent arg0)方法，用于响应timer和按钮事件
	 */
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

		if ("确定".equals(arg0.getActionCommand())) {
			// setVisible(false);
			new Thread(this).start(); // 开辟一个线程，完成备份还原操作
		}
		if ("取消".equals(arg0.getActionCommand()) || "完成".equals(arg0.getActionCommand())) {
			setVisible(false);
		}
	}

	@Override
	/**
	 * 重写Runnable接口的run()方法，作为备份还原操作的线程
	 */
	public void run() {
		// TODO Auto-generated method stub
		okButton.setEnabled(false);
		cancelButton.setEnabled(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		DicFile f = null;
		DicLib dicLib = null;
		try {
			// 若文件名的扩展名不是.dcl，将在文件名末尾加上".dcl"
			fileStr = (fileStr.endsWith(".dcl")) ? fileStr : (fileStr + ".dcl");
			f = new DicFile(fileStr, false);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// finally

		if (!isSave) // 还原操作
		{
			try {
				dicLib = f.readDic(progress); // 读取文件，将词库信息存储到dicLib对象中
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// finally
			for (int i = 0; i < frame.getDicLibs().size(); i++) {
				// 若词库已存在，则删除该词库
				if (frame.getDicLibs().get(i).getName().equals(dicLib.getName())) {
					frame.getDicLibs().remove(i);
					frame.getLibCombo().removeItemAt(i);
					break;
				}
			}
			frame.getDicLibs().add(dicLib); // 加入该词库
		} else { // 备份操作
			try {
				f.writeDic(frame.getCurrentDicLib(), progress); // 将frame.currentDicLib的词库信息写入文件
				progressLabel.setText((isSave ? "备份" : "还原") + "完成！");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				progressLabel.setText((isSave ? "备份" : "还原") + "失败！");
			}
			// finally
		}
		okButton.setEnabled(true);
		cancelButton.setVisible(false);
		timer.stop(); // 定时器停止工作，备份还原进度停止实时显示
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		okButton.setText("完成");
		frame.receiveMessage(); // 刷新主界面
	}
}
