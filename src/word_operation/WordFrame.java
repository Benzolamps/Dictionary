package word_operation;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * 由于三个界面类似，故首先设计其父类界面
 *
 * @author Benzolamps
 */
public abstract class WordFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 897483606700043384L;
    JTextField libText;
    JTextField originText;
    JEditorPane meaningText;
    JScrollPane meaningPane;
    JButton cancelButton;
    JButton okButton;

    public WordFrame(String title) {
        super(title);
        setLayout(null);
        setLocationRelativeTo(null);
        setSize(360, 320);
        setVisible(true);
        setResizable(false);
        getContentPane().add(createLibText());
        getContentPane().add(createOriginText());
        getContentPane().add(createMeaningPane());
        getContentPane().add(createCancelButton());
        getContentPane().add(createOKButton());
    }

    private JTextField createLibText() {
        JLabel libLabel = new JLabel("当前词库:");
        libLabel.setBounds(10, 10, 95, 20);
        getContentPane().add(libLabel);

        libText = new JTextField();
        libText.setBounds(new Rectangle(120, 10, getContentPane().getWidth() - 130, 20));
        return libText;
    }

    private JTextField createOriginText() {
        JLabel originLabel = new JLabel("单词:");
        originLabel.setBounds(10, 50, 95, 20);
        add(originLabel);

        originText = new JTextField();
        originText.setBounds(new Rectangle(120, 50, getContentPane().getWidth() - 130, 20));
        return originText;
    }

    private JScrollPane createMeaningPane() {
        JLabel meaningLabel = new JLabel("释义:");
        meaningLabel.setBounds(10, 90, 95, 20);
        add(meaningLabel);

        meaningText = new JEditorPane();

        meaningPane = new JScrollPane(meaningText);
        meaningPane.setBounds(new Rectangle(120, 90, getContentPane().getWidth() - 130, 130));

        return meaningPane;
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
    public abstract void actionPerformed(ActionEvent arg0);

}
