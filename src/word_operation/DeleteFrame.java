package word_operation;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import file_operation.Html;
import main.MainFrame;

/**
 * 
 * @author Benzolamps
 *
 */
public class DeleteFrame extends WordFrame {
	private static final long serialVersionUID = 3266384172369142787L;
	private MainFrame frame;

	public DeleteFrame(MainFrame frame) {
		super("删除单词");
		this.frame = frame;

		libText.setEditable(false);
		libText.setText(frame.getCurrentDicLib().getName());

		originText.setEditable(false);
		originText.setText(frame.getCurrentDicLib().get(frame.getWordList().getSelectedIndex()).getOrigin());

		meaningText.setEditable(false);
		int index = frame.getWordList().getSelectedIndex();
		Html html = new Html(frame.getCurrentDicLib().get(index).getMeaning());
		meaningText.setText(html.filt().substring(frame.getCurrentDicLib().get(index).getOrigin().length()));

		add(createMessageLabel());
	}

	private JLabel createMessageLabel() {
		JLabel messageLabel = new JLabel("确定要删除当前单词吗?");
		messageLabel.setBounds(10, 215, getContentPane().getWidth() - 20, 50);

		return messageLabel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if ("确定".equals(arg0.getActionCommand())) {
			setVisible(false);
			frame.getCurrentDicLib().remove(frame.getWordList().getSelectedIndex());
			frame.receiveMessage();
		}

		if ("取消".equals(arg0.getActionCommand())) {
			setVisible(false);
		}
	}

}
