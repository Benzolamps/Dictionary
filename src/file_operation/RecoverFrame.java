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
 * ���ݻ�ԭ������JFrame�����࣬ʵ����ActionListener��Runnable�ӿ�
 * 
 * @author Benzolamps
 *
 */
public class RecoverFrame extends JFrame implements ActionListener, Runnable {
	private static final long serialVersionUID = 4011819573993165113L;
	private JTextField libText; // ��ʾ�ʿ���
	private JLabel progressLabel; // ��ʾ���ݻ�ԭ����
	private JButton cancelButton;
	private JButton okButton;
	private MainFrame frame; // ������
	private String fileStr; // ��ѡ����ļ��ַ���
	private boolean isSave; // ˵�������Ƿ��ǽ��ʿ�д���ļ���������ֵΪtrue
	private StringBuffer progress = new StringBuffer(); // �洢���ݻ�ԭ�����ַ����Ļ�����
	private Timer timer; // ��ʱ��������ʵʱ���±��ݻ�ԭ����

	public RecoverFrame(MainFrame frame, String fileStr, final boolean IS_SAVE) throws IOException {
		// super("���ݻ�ԭ�ʿ�");
		super(IS_SAVE ? "����" : "��ԭ" + "�ʿ�");
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
				// ��Ӧtimer�¼�
				progressLabel.setText((isSave ? "����" : "��ԭ") + "���ȣ�" + progress.toString());
			}
		});
		timer.start(); // ��ʱ����ʼ����
	}

	private JTextField createLibText() throws IOException {
		JLabel l = new JLabel("�ʿ�����");
		l.setBounds(10, 10, 85, 20);
		getContentPane().add(l);

		libText = new JTextField();
		libText.setBounds(100, 10, getContentPane().getWidth() - 110, 20);
		// libText��ʾ�ʿ���
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
		cancelButton = new JButton("ȡ��");
		cancelButton.setMnemonic(KeyEvent.VK_C);
		Point pt = new Point();
		pt.x = 10;
		pt.y = getContentPane().getHeight() - 30;
		cancelButton.setBounds(new Rectangle(pt, new Dimension(80, 20)));
		cancelButton.addActionListener(this);
		return cancelButton;
	}

	private JButton createOKButton() {
		okButton = new JButton("ȷ��");
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
	 * ��дActionListenener��actionPerformed(ActionEvent arg0)������������Ӧtimer�Ͱ�ť�¼�
	 */
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

		if ("ȷ��".equals(arg0.getActionCommand())) {
			// setVisible(false);
			new Thread(this).start(); // ����һ���̣߳���ɱ��ݻ�ԭ����
		}
		if ("ȡ��".equals(arg0.getActionCommand()) || "���".equals(arg0.getActionCommand())) {
			setVisible(false);
		}
	}

	@Override
	/**
	 * ��дRunnable�ӿڵ�run()��������Ϊ���ݻ�ԭ�������߳�
	 */
	public void run() {
		// TODO Auto-generated method stub
		okButton.setEnabled(false);
		cancelButton.setEnabled(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		DicFile f = null;
		DicLib dicLib = null;
		try {
			// ���ļ�������չ������.dcl�������ļ���ĩβ����".dcl"
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

		if (!isSave) // ��ԭ����
		{
			try {
				dicLib = f.readDic(progress); // ��ȡ�ļ������ʿ���Ϣ�洢��dicLib������
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// finally
			for (int i = 0; i < frame.getDicLibs().size(); i++) {
				// ���ʿ��Ѵ��ڣ���ɾ���ôʿ�
				if (frame.getDicLibs().get(i).getName().equals(dicLib.getName())) {
					frame.getDicLibs().remove(i);
					frame.getLibCombo().removeItemAt(i);
					break;
				}
			}
			frame.getDicLibs().add(dicLib); // ����ôʿ�
		} else { // ���ݲ���
			try {
				f.writeDic(frame.getCurrentDicLib(), progress); // ��frame.currentDicLib�Ĵʿ���Ϣд���ļ�
				progressLabel.setText((isSave ? "����" : "��ԭ") + "��ɣ�");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				progressLabel.setText((isSave ? "����" : "��ԭ") + "ʧ�ܣ�");
			}
			// finally
		}
		okButton.setEnabled(true);
		cancelButton.setVisible(false);
		timer.stop(); // ��ʱ��ֹͣ���������ݻ�ԭ����ֹͣʵʱ��ʾ
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		okButton.setText("���");
		frame.receiveMessage(); // ˢ��������
	}
}
