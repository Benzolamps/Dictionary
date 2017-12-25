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

import main.MainFrame;
import main.MessageFrame;

/**
 * �ʿ�ɾ��������JFrame�����࣬ʵ����ActionListener�ӿ�
 *  
 * @author Benzolamps
 *
 */
public class DropFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = -7908921611241229898L;
	private JTextField nameText; // ��ʾ�ʿ���
	private MainFrame frame; // ������
	private JButton cancelButton;
	private JButton okButton;

	public DropFrame(MainFrame frame) {
		super("ɾ����ǰ�ʿ�");
		this.frame = frame;
		setLayout(null);
		setLocationRelativeTo(null);
		setSize(360, 200);
		setVisible(true);
		setResizable(false);
		getContentPane().add(createNameText());
		getContentPane().add(createMessageLabel());
		getContentPane().add(createCancelButton());
		getContentPane().add(createOKButton());
	}

	public JTextField createNameText() {
		JLabel nameLabel = new JLabel("�ʿ���:");
		nameLabel.setBounds(10, 10, 95, 20);
		getContentPane().add(nameLabel);

		nameText = new JTextField();
		nameText.setText(frame.getCurrentDicLib().getName());
		nameText.setBounds(new Rectangle(120, 10, getContentPane().getWidth() - 130, 20));
		nameText.setEditable(false);
		return nameText;
	}

	public JLabel createMessageLabel() {
		JLabel messageLabel = new JLabel("ȷ��Ҫɾ����ǰ�ʿ���?");
		messageLabel.setBounds(10, 95, getContentPane().getWidth() - 20, 50);

		return messageLabel;
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
			int index = frame.getLibCombo().getSelectedIndex();
			if (index == 0) // �ʿ��е�Ĭ�ϴʿ��޷�ɾ��
			{
				new MessageFrame("��ʾ", "�޷�ɾ��Ĭ�ϴʿ�!");
			} else // ɾ���ʿ⼰�󶨵���Ͽ��е�ѡ��
			{
				frame.getDicLibs().remove(index);
				frame.getLibCombo().removeItemAt(index);
				frame.setCurrentDicLib(frame.getDicLibs().get(0)); // ���ʿ��е�Ĭ�ϴʿ���Ϊ��ǰ�ʿ�
				frame.receiveMessage(); // ˢ��������
				setVisible(false);
			}
		}

		if ("ȡ��".equals(arg0.getActionCommand())) {
			setVisible(false);
		}
	}
}
