package library_operation;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dictionary.DicLib;
import main.MainFrame;
import main.MessageFrame;

/**
 * 词库新建界面是JFrame的子类，实现了ActionListener接口
 * 
 * @author Benzolamps
 *
 */
public class NewFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = -6558674996415666594L;
	private JTextField nameText;
	private MainFrame frame;
	private JButton cancelButton;
	private JButton okButton;

	public NewFrame(MainFrame frame) {
		super("新建词库");
		this.frame = frame;
		setLayout(null);
		setLocationRelativeTo(null);
		setSize(360, 200);
		setVisible(true);
		setResizable(false);
		getContentPane().add(createNameText());
		getContentPane().add(createCancelButton());
		getContentPane().add(createOKButton());
	}

	public JTextField createNameText() {
		JLabel nameLabel = new JLabel("词库名:");
		nameLabel.setBounds(10, 10, 95, 20);
		getContentPane().add(nameLabel);

		nameText = new JTextField();
		nameText.setBounds(new Rectangle(120, 10, getContentPane().getWidth() - 130, 20));
		return nameText;
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

	public JButton createOKButton() {
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
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if ("确定".equals(arg0.getActionCommand())) {
			// setVisible(false);
			if (nameText.getText().isEmpty()) // 当词库名为空时，发出提示信息
			{
				new MessageFrame("提示", "请键入词库名!").setVisible(true);
			} else {
				frame.getDicLibs().add(new DicLib(nameText.getText()));
				frame.receiveMessage();
				setVisible(false);
			}
		}

		if ("取消".equals(arg0.getActionCommand())) {
			setVisible(false);
		}
	}
}
