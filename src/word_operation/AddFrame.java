package word_operation;

import java.awt.event.ActionEvent;

import dictionary.Word;
import main.MainFrame;
import main.MessageFrame;

/**
 * @author Benzolamps
 */
public class AddFrame extends WordFrame {
    private static final long serialVersionUID = -8673060842996414660L;
    MainFrame frame;

    public AddFrame(MainFrame frame) {
        super("添加单词");
        this.frame = frame;

        libText.setEditable(false);
        libText.setText(frame.getCurrentDicLib().getName());
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if ("确定".equals(arg0.getActionCommand())) {
            // setVisible(false);
            if (!originText.getText().isEmpty()) {
                setVisible(false);
                String mean = "<font color=red>";
                mean = mean + originText.getText();
                mean = mean + "</font><br><font color=green>";
                mean = mean + meaningText.getText().replace("\n", "<br/>");
                mean = mean + "</font>";
                frame.getCurrentDicLib().add(new Word(originText.getText(), mean));
                frame.getCurrentDicLib().sort(false);
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
