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
 * �ļ����������JFrame�����࣬ʵ����ActionListener�ӿ�
 * 
 * @author Benzolamps
 *
 */
public class FileFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = -5019495375438539969L;
    private MainFrame frame; // ��Ŀ������
    private JFileChooser fc; // �ļ�ѡ����������ѡ��Ҫ������ļ�
    private boolean isSave; // ȷ���ļ��Ƕ�ȡ����д�룬�����д������ֵΪtrue

    public FileFrame(MainFrame frame, boolean isSave) {
        // super("ѡ���ļ�");
        super(isSave ? "���Ϊ" : "��");
        this.frame = frame;
        this.isSave = isSave;
        setLayout(null);
        setLocationRelativeTo(null);
        setSize(480, 400);
        setVisible(true);
        setResizable(false);
        // JLabel messageLabel = new JLabel("��ѡ���ļ���");
        JLabel messageLabel = new JLabel("��ѡ��Ҫ" + (isSave ? "����" : "��ԭ") + "���ļ���");
        messageLabel.setBounds(10, 10, getContentPane().getWidth() - 110, 20);
        getContentPane().add(messageLabel);
        getContentPane().add(createFileChooser());
    }

    private JFileChooser createFileChooser() {
        fc = new JFileChooser(".");
        fc.setAcceptAllFileFilterUsed(false); // ȥ�������ļ���ѡ����
        // ����չ��Ϊ.dcl���ļ����������
        fc.addChoosableFileFilter(new FileNameExtensionFilter("�ʿ��ļ�(*.dcl)", "dcl"));
        fc.setBounds(0, 40, getContentPane().getWidth(), 320);
        // ���Ҫд���ļ���Ĭ���ļ���Ϊ"�ʿ���.dcl"
        if (isSave)
            fc.setSelectedFile(new File(frame.getCurrentDicLib().getName() + ".dcl"));
        fc.setApproveButtonText("ȷ��");
        fc.addActionListener(this);

        return fc;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        if (JFileChooser.APPROVE_SELECTION.equals(arg0.getActionCommand())) { // "ȷ��"��ť�¼�����
            setVisible(false);
            try {
                if (!isSave) {
                    // �����ļ������ʺ�����Ŀʹ�õĴʿ��ļ���������ʾ��Ϣ
                    if (new DicFile(fc.getSelectedFile().getPath(), false).getDicName() == null) {
                        new MessageFrame("��ʾ", "���ǹ淶���Ĵʿ��ļ�!").setVisible(true);
                        return;
                    }
                }
                // �򿪱��ݻ�ԭ�Ի���
                new RecoverFrame(frame, fc.getSelectedFile().getPath(), isSave).setVisible(true);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (JFileChooser.CANCEL_SELECTION.equals(arg0.getActionCommand())) {
            setVisible(false);
        }
    }
}
