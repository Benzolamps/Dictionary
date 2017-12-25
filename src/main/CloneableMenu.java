package main;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * �ɿ�¡�Ĳ˵���JMenu�����࣬ʵ����Cloneable�ӿڣ�
 * һ���˵�������ֻ�ܱ�һ�����ѡ�У���Ҫʹ�ýṹһģһ���Ĳ˵����ʿ��ٴ˷���
 * 
 * @author Benzolamps
 *
 */
public class CloneableMenu extends JMenu implements Cloneable {
	private static final long serialVersionUID = -7199663284269505961L;

	MainFrame frame; // ������

	public CloneableMenu(String name, MainFrame frame) {
		super(name);
		this.frame = frame;
	}


	/*
	 * ��дCloneable�ӿڵ�clone()���������˵��е��Ӳ˵�ȫ�����ø���ʱ��ͨ���ݹ�ķ�����ȿ�¡�ò˵�
	 */
	@Override
	public CloneableMenu clone() {
		CloneableMenu cm = new CloneableMenu(getText(), frame); // ��¡�˵���
		for (int i = 0; i < getItemCount(); i++) {
			if (getItem(i) == null) {
				cm.addSeparator(); // ����ָ���
			} else {
				if (getItem(i).getClass().getName().equals("main.CloneableMenu")) { // ���˵�Ϊ����Ķ���ʱ���ݹ���ø÷���
					CloneableMenu temp = ((CloneableMenu) getItem(i)).clone();
					temp.setMnemonic(getItem(i).getMnemonic()); // ��¡���ټ�
					cm.add(temp);
				} else { // ���˵�Ϊ�˵���ʱ��ִ�����²���
					JMenuItem temp = new JMenuItem(getItem(i).getText()); // ��¡�˵���
					temp.setMnemonic(getItem(i).getMnemonic());
					for (ActionListener a : getItem(i).getActionListeners()) // ��¡�¼�������
					{
						temp.addActionListener(a);
					}
					cm.add(temp);
				}
			}
		}
		return cm;
	}
}
