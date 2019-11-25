package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dictionary.DicLib;
import file_operation.FileFrame;
import file_operation.LoadProcess;
import library_operation.DropFrame;
import library_operation.NewFrame;
import word_operation.AddFrame;
import word_operation.DeleteFrame;
import word_operation.EditFrame;

/**
 * 主界面是JFrame的子类，实现了DocumentListener, ListSelectionListener, ActionListener,
 * ItemListener, MouseListener, Runnable接口
 *
 * @author Benzolamps
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MainFrame extends JFrame
        implements DocumentListener, ListSelectionListener, ActionListener, ItemListener, MouseListener, Runnable {
    private static final long serialVersionUID = 776572726716394096L;
    private Vector<DicLib> dicLibs = new Vector<>(); // 存储词库的向量
    private DicLib currentDicLib = new DicLib("默认词库");
    // 指向当前词库
    private JMenuBar menuBar; // 菜单栏
    // 文件、编辑顶层菜单，弹出式菜单的子菜单，移动复制实时更新菜单
    // 弹出式菜单的子菜单结构与编辑菜单完全相同
    private CloneableMenu fileMenu;
    private CloneableMenu editMenu;
    private CloneableMenu forPopupMenu;
    private CloneableMenu[] activeMenu;
    private JPopupMenu listMenu; // 弹出式菜单
    private JComboBox libCombo = new JComboBox(); // 显示所有词库
    // private JTextField showText; // 显示释义
    private JEditorPane showText; // 显示释义
    private JScrollPane showPane; // showText的容器，含有垂直滚动条
    private JTextField wordSeeked; // 查询单词用的文本框
    private JList wordList = new JList(currentDicLib); // 显示当前词库的单词列表
    private JScrollPane wordPane; // wordList的容器，含有垂直滚动条

    public MainFrame() {
        super("电子英汉词典");
        setLayout(null);
        setSize(800, 600);
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setJMenuBar(createMenuBar());
        getContentPane().add(createLibCombo());
        getContentPane().add(createShowPane());
        getContentPane().add(createWordSeeked());
        getContentPane().add(createWordPane());
        getContentPane().add(createPopupMenu());
        createDefaultDicLib();
    }

    public DicLib getCurrentDicLib() {
        return currentDicLib;
    }

    public void setCurrentDicLib(DicLib currentDicLib) {
        this.currentDicLib = currentDicLib;
    }

    public Vector<DicLib> getDicLibs() {
        return dicLibs;
    }

    public void setDicLibs(Vector<DicLib> dicLibs) {
        this.dicLibs = dicLibs;
    }

    public JComboBox getLibCombo() {
        return libCombo;
    }

    public void setLibCombo(JComboBox libCombo) {
        this.libCombo = libCombo;
    }

    public JList getWordList() {
        return wordList;
    }

    public void setWordList(JList wordList) {
        this.wordList = wordList;
    }

    /**
     * 创建默认词库
     */
    private void createDefaultDicLib() {
        // currentDicLib.add(new Word("student", "学生"));
        // currentDicLib.add(new Word("computer", "计算机"));
        dicLibs.add(currentDicLib);
        listMenu.setEnabled(false);
        libCombo.setEnabled(false);
        wordSeeked.setEnabled(false);
        fileMenu.setEnabled(false);
        editMenu.setEnabled(false);
        Thread t = new Thread(this); // 开辟一个线程，用于载入默认词库
        t.start();
    }

    private JMenuBar createMenuBar() {
        menuBar = new JMenuBar();

        fileMenu = new CloneableMenu("文件(F)", this);
        fileMenu.setMnemonic(KeyEvent.VK_F);

        createOneMenuItem("新建词库(N)", KeyEvent.VK_N, fileMenu);
        createOneMenuItem("删除当前词库(D)", KeyEvent.VK_D, fileMenu);
        fileMenu.addSeparator();
        createOneMenuItem("将词库备份到文件(B)", KeyEvent.VK_B, fileMenu);
        createOneMenuItem("从文件中还原词库(R)", KeyEvent.VK_R, fileMenu);
        menuBar.add(fileMenu);

        editMenu = new CloneableMenu("编辑(E)", this);
        editMenu.setMnemonic(KeyEvent.VK_E);
        createOneMenuItem("添加单词(A)", KeyEvent.VK_A, editMenu);
        createOneMenuItem("删除单词(D)", KeyEvent.VK_D, editMenu);
        createOneMenuItem("修改单词(M)", KeyEvent.VK_M, editMenu);
        editMenu.addSeparator();
        activeMenu = new CloneableMenu[2];
        activeMenu[0] = createOneSubMenu("复制单词到词库(C)", KeyEvent.VK_C, editMenu);
        activeMenu[1] = createOneSubMenu("移动单词到词库(M)", KeyEvent.VK_M, editMenu);
        createActiveMenuItem();
        menuBar.add(editMenu);

        return menuBar;
    }

    private JPopupMenu createPopupMenu() {
        listMenu = new JPopupMenu();
        return listMenu;
    }

    /**
     * 简便书写，创建一个菜单项
     *
     * @param label    菜单项标签字符串
     * @param mnemonic 访问键
     * @param menu     被加入的菜单
     * @return
     */
    private JMenuItem createOneMenuItem(String label, int mnemonic, JMenu menu) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.setMnemonic(mnemonic);
        menuItem.addActionListener(this);

        menu.add(menuItem);

        return menuItem;
    }

    /**
     * 简便书写，创建一个子菜单
     *
     * @param label    子菜单标签字符串
     * @param mnemonic 访问键
     * @param menu     被加入的菜单
     * @return
     */
    private CloneableMenu createOneSubMenu(String label, int mnemonic, JMenu menu) {
        CloneableMenu subMenu = new CloneableMenu(label, this);
        if (mnemonic != -1)
            subMenu.setMnemonic(mnemonic);

        menu.add(subMenu);

        return subMenu;
    }

    private JComboBox createLibCombo() {
        libCombo = new JComboBox();
        for (DicLib d : dicLibs) {
            libCombo.addItem(d.getName());
        }
        libCombo.setBounds(1, 0, 250, 20);
        libCombo.addItemListener(this);
        return libCombo;
    }

    private JScrollPane createShowPane() {
        // showText = new JTextArea();
        showText = new JEditorPane();
        showText.setContentType("text/html");
        showText.setEditable(false);

        showPane = new JScrollPane(showText);
        showPane.setBounds(255, 0, getContentPane().getWidth() - 255, getContentPane().getHeight());
        return showPane;
    }

    private JTextField createWordSeeked() {
        wordSeeked = new JTextField();
        wordSeeked.setBounds(1, 27, 250, 20);
        wordSeeked.getDocument().addDocumentListener(this);
        return wordSeeked;
    }

    private JScrollPane createWordPane() {
        wordList = new JList(currentDicLib);
        wordList.addListSelectionListener(this);
        wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wordList.addMouseListener(this);
        wordPane = new JScrollPane(wordList);
        wordPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        wordPane.setBounds(1, 52, 250, getContentPane().getHeight() - 52);

        return wordPane;
    }

    /**
     * 创建复制、移动单词的实时更新菜单项
     */
    public void createActiveMenuItem() {
        for (int i = 0; i < activeMenu.length; i++) {
            activeMenu[i].removeAll();
            for (DicLib d : dicLibs) {
                if (!currentDicLib.equals(d)) {
                    JMenuItem menuTemp = createOneMenuItem(d.getName(), -1, activeMenu[i]);
                    menuTemp.addActionListener(new MyAction(this, menuTemp, i != 0));
                }
            }
            if (dicLibs.size() != 1)
                activeMenu[i].addSeparator();
            JMenuItem menuTemp = createOneMenuItem("新建词库(N)", KeyEvent.VK_N, activeMenu[i]);
            menuTemp.addActionListener(new MyAction(this, menuTemp, i != 0));
        }
    }

    /**
     * 刷新主界面
     */
    public void receiveMessage() {
        wordList.setListData(currentDicLib);

        for (DicLib d : dicLibs) {
            boolean existFlag = false;
            for (int j = 0; j < libCombo.getItemCount(); j++) {
                if (libCombo.getItemAt(j).equals(d.getName())) {
                    existFlag = true;
                    break;
                }
            }
            if (!existFlag)
                libCombo.addItem(d.getName());
        }
        createActiveMenuItem();
    }

    /**
     * 重写ListSelectionListener的valueChanged(ListSelectionEvent
     * arg0)的方法，用于在showText中显示已选中单词的释义
     */
    @Override
    public void valueChanged(ListSelectionEvent arg0) {
        if (wordList.getSelectedIndex() != -1) {

            showText.setText(currentDicLib.get(wordList.getSelectedIndex()).getMeaning());
            showText.setCaretPosition(0); // 将滚动条置于最上方
        }
    }

    /*
     * 响应wordSeeked的事件，将单词定位到查询的单词
     */
    private void seek() {
        int index = currentDicLib.index(wordSeeked.getText());
        if (index != -1) {
            wordList.setSelectedIndex(index);
            wordList.ensureIndexIsVisible(currentDicLib.size() - 1);
            wordList.ensureIndexIsVisible(index); // 滚动滚动条，保证用户可以看到当前单词
        }
    }

    /**
     * 重写DocumentListener的changedUpdate(DocumentEvent arg0)方法，用于将单词定位到查询的单词
     */
    @Override
    public void changedUpdate(DocumentEvent arg0) {
        seek();
    }

    /**
     * 重写DocumentListener的insertUpdate(DocumentEvent arg0)方法，用于将单词定位到查询的单词
     */
    @Override
    public void insertUpdate(DocumentEvent arg0) {
        seek();
    }

    /**
     * 重写DocumentListener的removeUpdate(DocumentEvent arg0)方法，用于将单词定位到查询的单词
     */
    @Override
    public void removeUpdate(DocumentEvent arg0) {
        seek();
    }

    /*
     * 响应按钮，菜单项时间
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        // 添加、删除、修改单词
        if ("添加单词(A)".equals(arg0.getActionCommand())) {
            new AddFrame(this).setVisible(true);
        }

        if ("删除单词(D)".equals(arg0.getActionCommand())) {
            if (wordList.getSelectedIndex() != -1) {
                new DeleteFrame(this).setVisible(true);
            } else { // 没有单词选中
                new MessageFrame("提示", "请先选中要删除的单词!").setVisible(true);
            }
        }

        if ("修改单词(M)".equals(arg0.getActionCommand())) {
            if (wordList.getSelectedIndex() != -1) {
                new EditFrame(this).setVisible(true);
            } else { // 没有单词选中
                new MessageFrame("提示", "请先选中要修改的单词!").setVisible(true);
            }
        }

        if ("新建词库(N)".equals(arg0.getActionCommand())) {
            new NewFrame(this).setVisible(true);
        }

        if ("删除当前词库(D)".equals(arg0.getActionCommand())) {
            new DropFrame(this).setVisible(true);
        }

        if ("从文件中还原词库(R)".equals(arg0.getActionCommand())) {
            new FileFrame(this, false).setVisible(true);
        }

        if ("将词库备份到文件(B)".equals(arg0.getActionCommand())) {
            new FileFrame(this, true).setVisible(true);
        }

    }

    /**
     * 改变当前词库
     */
    @Override
    public void itemStateChanged(ItemEvent arg0) {
        int index = libCombo.getSelectedIndex();
        if (index != -1) {
            currentDicLib = dicLibs.get(index);
        }
        receiveMessage();
    }

    /*
     * 响应单词列表的右击事件
     */
    @Override
    public void mouseClicked(MouseEvent arg0) {
        if (arg0.getButton() == MouseEvent.BUTTON3) {
            int index = wordList.locationToIndex(arg0.getPoint());
            wordList.setSelectedIndex(index);
            forPopupMenu = editMenu.clone();
            listMenu.removeAll();
            listMenu.add(forPopupMenu);
            listMenu.show(arg0.getComponent(), arg0.getX(), arg0.getY());
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    /*
     * 载入默认词库的线程
     */
    @Override
    public void run() {
        try {
            showText.setText("正在载入牛津英汉简明词典．．．");
            dicLibs.add(LoadProcess.readDic("res/e2c.dcl"));
            showText.setText("正在载入牛津汉英简明词典．．．");
            Thread.sleep(1000);
            dicLibs.add(LoadProcess.readDic("res/c2e.dcl"));
            receiveMessage();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        showText.setText("已完成！");
        listMenu.setEnabled(true);
        wordSeeked.setEnabled(true);
        libCombo.setEnabled(true);
        fileMenu.setEnabled(true);
        editMenu.setEnabled(true);
    }

    /*
     * 主方法
     */
    public static void main(String[] args) {
        MainFrame.setDefaultLookAndFeelDecorated(true);
        new MainFrame().setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
