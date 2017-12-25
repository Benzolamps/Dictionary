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
 * ��������JFrame�����࣬ʵ����DocumentListener, ListSelectionListener, ActionListener,
 * ItemListener, MouseListener, Runnable�ӿ�
 * 
 * @author Benzolamps
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MainFrame extends JFrame
		implements DocumentListener, ListSelectionListener, ActionListener, ItemListener, MouseListener, Runnable {
	private static final long serialVersionUID = 776572726716394096L;
	private Vector<DicLib> dicLibs = new Vector<DicLib>(); // �洢�ʿ������
	private DicLib currentDicLib = new DicLib("Ĭ�ϴʿ�");; // ָ��ǰ�ʿ�
	private JMenuBar menuBar; // �˵���
	// �ļ����༭����˵�������ʽ�˵����Ӳ˵����ƶ�����ʵʱ���²˵�
	// ����ʽ�˵����Ӳ˵��ṹ��༭�˵���ȫ��ͬ
	private CloneableMenu fileMenu, editMenu, forPopupMenu, activeMenu[];
	private JPopupMenu listMenu; // ����ʽ�˵�
	private JComboBox libCombo = new JComboBox(); // ��ʾ���дʿ�
	// private JTextField showText; // ��ʾ����
	private JEditorPane showText; // ��ʾ����
	private JScrollPane showPane; // showText�����������д�ֱ������
	private JTextField wordSeeked; // ��ѯ�����õ��ı���
	private JList wordList = new JList(currentDicLib); // ��ʾ��ǰ�ʿ�ĵ����б�
	private JScrollPane wordPane; // wordList�����������д�ֱ������

	public MainFrame() throws IOException {
		super("����Ӣ���ʵ�");
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
	 * ����Ĭ�ϴʿ�
	 * 
	 * @throws IOException
	 */
	private void createDefaultDicLib() throws IOException {
		// currentDicLib.add(new Word("student", "ѧ��"));
		// currentDicLib.add(new Word("computer", "�����"));
		dicLibs.add(currentDicLib);
		listMenu.setEnabled(false);
		libCombo.setEnabled(false);
		wordSeeked.setEnabled(false);
		fileMenu.setEnabled(false);
		editMenu.setEnabled(false);
		Thread t = new Thread(this); // ����һ���̣߳���������Ĭ�ϴʿ�
		t.start();
	}

	private JMenuBar createMenuBar() {
		menuBar = new JMenuBar();

		fileMenu = new CloneableMenu("�ļ�(F)", this);
		fileMenu.setMnemonic(KeyEvent.VK_F);

		createOneMenuItem("�½��ʿ�(N)", KeyEvent.VK_N, fileMenu);
		createOneMenuItem("ɾ����ǰ�ʿ�(D)", KeyEvent.VK_D, fileMenu);
		fileMenu.addSeparator();
		createOneMenuItem("���ʿⱸ�ݵ��ļ�(B)", KeyEvent.VK_B, fileMenu);
		createOneMenuItem("���ļ��л�ԭ�ʿ�(R)", KeyEvent.VK_R, fileMenu);
		menuBar.add(fileMenu);

		editMenu = new CloneableMenu("�༭(E)", this);
		editMenu.setMnemonic(KeyEvent.VK_E);
		createOneMenuItem("��ӵ���(A)", KeyEvent.VK_A, editMenu);
		createOneMenuItem("ɾ������(D)", KeyEvent.VK_D, editMenu);
		createOneMenuItem("�޸ĵ���(M)", KeyEvent.VK_M, editMenu);
		editMenu.addSeparator();
		activeMenu = new CloneableMenu[2];
		activeMenu[0] = createOneSubMenu("���Ƶ��ʵ��ʿ�(C)", KeyEvent.VK_C, editMenu);
		activeMenu[1] = createOneSubMenu("�ƶ����ʵ��ʿ�(M)", KeyEvent.VK_M, editMenu);
		createActiveMenuItem();
		menuBar.add(editMenu);

		return menuBar;
	}

	private JPopupMenu createPopupMenu() {
		listMenu = new JPopupMenu();
		return listMenu;
	}

	/**
	 * �����д������һ���˵���
	 * 
	 * @param label �˵����ǩ�ַ���
	 * @param mnemonic ���ʼ�
	 * @param menu ������Ĳ˵�
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
	 * �����д������һ���Ӳ˵�
	 * 
	 * @param label �Ӳ˵���ǩ�ַ���
	 * @param mnemonic ���ʼ�
	 * @param menu ������Ĳ˵�
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
	 * �������ơ��ƶ����ʵ�ʵʱ���²˵���
	 */
	public void createActiveMenuItem() {
		for (int i = 0; i < activeMenu.length; i++) {
			activeMenu[i].removeAll();
			for (DicLib d : dicLibs) {
				if (!currentDicLib.equals(d)) {
					JMenuItem menuTemp = createOneMenuItem(d.getName(), -1, activeMenu[i]);
					menuTemp.addActionListener(new MyAction(this, menuTemp, i == 0 ? false : true));
				}
			}
			if (dicLibs.size() != 1)
				activeMenu[i].addSeparator();
			JMenuItem menuTemp = createOneMenuItem("�½��ʿ�(N)", KeyEvent.VK_N, activeMenu[i]);
			menuTemp.addActionListener(new MyAction(this, menuTemp, i == 0 ? false : true));
		}
	}

	/**
	 * ˢ��������
	 */
	public void receiveMessage() {
		wordList.setListData(currentDicLib);

		for (DicLib d : dicLibs) {
			boolean existFlag = false;
			for (int j = 0; j < libCombo.getItemCount(); j++) {
				if (((String) libCombo.getItemAt(j)).equals(d.getName())) {
					existFlag = true;
					break;
				}
			}
			if (!existFlag)
				libCombo.addItem(d.getName());
		}
		createActiveMenuItem();
	}

	@Override
	/**
	 * ��дListSelectionListener��valueChanged(ListSelectionEvent
	 * arg0)�ķ�����������showText����ʾ��ѡ�е��ʵ�����
	 */
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		if (wordList.getSelectedIndex() != -1) {

			showText.setText(currentDicLib.get(wordList.getSelectedIndex()).getMeaning());
			showText.setCaretPosition(0); // ���������������Ϸ�
		}
	}

	/*
	 * ��ӦwordSeeked���¼��������ʶ�λ����ѯ�ĵ���
	 */
	private void seek() {
		int index = currentDicLib.index(wordSeeked.getText());
		if (index != -1) {
			wordList.setSelectedIndex(index);
			wordList.ensureIndexIsVisible(currentDicLib.size() - 1);
			wordList.ensureIndexIsVisible(index); // ��������������֤�û����Կ�����ǰ����
		}
	}

	@Override
	/*
	 * ��дDocumentListener��changedUpdate(DocumentEvent arg0)���������ڽ����ʶ�λ����ѯ�ĵ���
	 */
	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		seek();
	}

	/*
	 * ��дDocumentListener��insertUpdate(DocumentEvent arg0)���������ڽ����ʶ�λ����ѯ�ĵ���
	 */
	@Override
	public void insertUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		seek();
	}

	/*
	 * ��дDocumentListener��removeUpdate(DocumentEvent arg0)���������ڽ����ʶ�λ����ѯ�ĵ���
	 */
	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		seek();
	}

	/*
	 * ��Ӧ��ť���˵���ʱ��
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		// ��ӡ�ɾ�����޸ĵ���
		if ("��ӵ���(A)".equals(arg0.getActionCommand())) {
			new AddFrame(this).setVisible(true);
		}

		if ("ɾ������(D)".equals(arg0.getActionCommand())) {
			if (wordList.getSelectedIndex() != -1) {
				new DeleteFrame(this).setVisible(true);
			} else { // û�е���ѡ��
				new MessageFrame("��ʾ", "����ѡ��Ҫɾ���ĵ���!").setVisible(true);
			}
		}

		if ("�޸ĵ���(M)".equals(arg0.getActionCommand())) {
			if (wordList.getSelectedIndex() != -1) {
				new EditFrame(this).setVisible(true);
			} else { // û�е���ѡ��
				new MessageFrame("��ʾ", "����ѡ��Ҫ�޸ĵĵ���!").setVisible(true);
			}
		}

		if ("�½��ʿ�(N)".equals(arg0.getActionCommand())) {
			new NewFrame(this).setVisible(true);
		}

		if ("ɾ����ǰ�ʿ�(D)".equals(arg0.getActionCommand())) {
			new DropFrame(this).setVisible(true);
		}

		if ("���ļ��л�ԭ�ʿ�(R)".equals(arg0.getActionCommand())) {
			new FileFrame(this, false).setVisible(true);
		}

		if ("���ʿⱸ�ݵ��ļ�(B)".equals(arg0.getActionCommand())) {
			new FileFrame(this, true).setVisible(true);
		}

	}

	/*
	 * �ı䵱ǰ�ʿ�
	 */
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		int index = libCombo.getSelectedIndex();
		if (index != -1) {
			currentDicLib = dicLibs.get(index);
		}
		receiveMessage();
	}

	/*
	 * ��Ӧ�����б���һ��¼�
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
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
	 * ����Ĭ�ϴʿ���߳�
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			showText.setText("��������ţ��Ӣ�������ʵ䣮����");
			dicLibs.add(LoadProcess.readDic("res/e2c.dcl"));
			showText.setText("��������ţ��Ӣ�����ʵ䣮����");
			Thread.sleep(1000);
			dicLibs.add(LoadProcess.readDic("res/c2e.dcl"));
			receiveMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		showText.setText("����ɣ�");
		listMenu.setEnabled(true);
		wordSeeked.setEnabled(true);
		libCombo.setEnabled(true);
		fileMenu.setEnabled(true);
		editMenu.setEnabled(true);
	}

	/*
	 * ������
	 */
	public static void main(String args[]) throws IOException {
		MainFrame.setDefaultLookAndFeelDecorated(true);
		new MainFrame().setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
