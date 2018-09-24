package main;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 用于提示信息
 * 
 * @author Benzolamps
 *
 */
public class MessageFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 6493849943777790374L;
	private JButton okButton;

	public MessageFrame(String name, String text) {
		super(name);
		setLayout(null);
		setSize(360, 200);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		JLabel messageLabel = new JLabel(text);
		messageLabel.setBounds(10, 10, getContentPane().getWidth() - 20, 50);
		getContentPane().add(messageLabel);
		getContentPane().add(createOKButton());
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

			setVisible(false);
		}
	}
}
