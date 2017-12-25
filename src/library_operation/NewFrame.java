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
 * �ʿ��½�������JFrame�����࣬ʵ����ActionListener�ӿ�
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
		super("�½��ʿ�");
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
		JLabel nameLabel = new JLabel("�ʿ���:");
		nameLabel.setBounds(10, 10, 95, 20);
		getContentPane().add(nameLabel);

		nameText = new JTextField();
		nameText.setBounds(new Rectangle(120, 10, getContentPane().getWidth() - 130, 20));
		return nameText;
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

	public JButton createOKButton() {
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
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if ("ȷ��".equals(arg0.getActionCommand())) {
			// setVisible(false);
			if (nameText.getText().isEmpty()) // ���ʿ���Ϊ��ʱ��������ʾ��Ϣ
			{
				new MessageFrame("��ʾ", "�����ʿ���!").setVisible(true);
			} else {
				frame.getDicLibs().add(new DicLib(nameText.getText()));
				frame.receiveMessage();
				setVisible(false);
			}
		}

		if ("ȡ��".equals(arg0.getActionCommand())) {
			setVisible(false);
		}
	}
}
