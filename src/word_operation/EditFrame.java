package word_operation;

import java.awt.event.ActionEvent;

import file_operation.Html;
import main.MainFrame;
import main.MessageFrame;

/**
 * @author Benzolamps
 */
public class EditFrame extends WordFrame {
    private static final long serialVersionUID = -8708045199317233667L;
    private MainFrame frame;

    public EditFrame(MainFrame frame) {
        super("编辑单词 ");
        this.frame = frame;

        libText.setEditable(false);
        libText.setText(frame.getCurrentDicLib().getName());

        originText.setText(frame.getCurrentDicLib().get(frame.getWordList().getSelectedIndex()).getOrigin());

        int index = frame.getWordList().getSelectedIndex();
        Html html = new Html(frame.getCurrentDicLib().get(index).getMeaning());
        meaningText.setText(html.filt().substring(frame.getCurrentDicLib().get(index).getOrigin().length()));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        if ("确定".equals(arg0.getActionCommand())) {
            // setVisible(false);
            if (!originText.getText().isEmpty()) {
                setVisible(false);
                int index = frame.getWordList().getSelectedIndex();
                String mean = new String("<font color=red>");
                mean = mean + originText.getText();
                mean = mean + "</font><br><font color=green>";
                mean = mean + meaningText.getText().replace("\n", "<br/>");
                mean = mean + "</font>";
                frame.getCurrentDicLib().get(index).setOrigin(originText.getText());
                frame.getCurrentDicLib().get(index).setMeaning(mean);
                frame.getWordList().setSelectedIndex(index);
                frame.receiveMessage();
            } else {
                new MessageFrame("提示", "请键入单词原形!");
            }
        }

        if ("取消".equals(arg0.getActionCommand())) {
            setVisible(false);
        }

    }
}
