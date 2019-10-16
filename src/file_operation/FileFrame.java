package file_operation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.MainFrame;
import main.MessageFrame;

/**
 * 文件处理界面是JFrame的子类，实现了ActionListener接口
 *
 * @author Benzolamps
 */
public class FileFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = -5019495375438539969L;
    private MainFrame frame; // 项目主界面
    private JFileChooser fc; // 文件选择器，用于选择要处理的文件
    private boolean isSave; // 确定文件是读取还是写入，如果是写入则其值为true

    public FileFrame(MainFrame frame, boolean isSave) {
        // super("选择文件");
        super(isSave ? "另存为" : "打开");
        this.frame = frame;
        this.isSave = isSave;
        setLayout(null);
        setLocationRelativeTo(null);
        setSize(480, 400);
        setVisible(true);
        setResizable(false);
        // JLabel messageLabel = new JLabel("请选择文件：");
        JLabel messageLabel = new JLabel("请选择要" + (isSave ? "备份" : "还原") + "的文件：");
        messageLabel.setBounds(10, 10, getContentPane().getWidth() - 110, 20);
        getContentPane().add(messageLabel);
        getContentPane().add(createFileChooser());
    }

    private JFileChooser createFileChooser() {
        fc = new JFileChooser(".");
        fc.setAcceptAllFileFilterUsed(false); // 去掉所有文件可选属性
        // 将扩展名为.dcl的文件加入过滤器
        fc.addChoosableFileFilter(new FileNameExtensionFilter("词库文件(*.dcl)", "dcl"));
        fc.setBounds(0, 40, getContentPane().getWidth(), 320);
        // 如果要写入文件，默认文件名为"词库名.dcl"
        if (isSave) {
            fc.setSelectedFile(new File(frame.getCurrentDicLib().getName() + ".dcl"));
        }
        fc.setApproveButtonText("确定");
        fc.addActionListener(this);

        return fc;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (JFileChooser.APPROVE_SELECTION.equals(arg0.getActionCommand())) { // "确定"按钮事件监听
            setVisible(false);
            try {
                if (!isSave) {
                    // 当该文件不是适合于项目使用的词库文件，发出提示信息
                    if (new DicFile(fc.getSelectedFile().getPath(), false).getDicName() == null) {
                        new MessageFrame("提示", "不是规范化的词库文件!").setVisible(true);
                        return;
                    }
                }
                // 打开备份还原对话框
                new RecoverFrame(frame, fc.getSelectedFile().getPath(), isSave).setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (JFileChooser.CANCEL_SELECTION.equals(arg0.getActionCommand())) {
            setVisible(false);
        }
    }
}
