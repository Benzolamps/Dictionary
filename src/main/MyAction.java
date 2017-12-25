package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import dictionary.DicLib;
import dictionary.Word;

/**
 * ���ڴ����쳣����
 * 
 * @author Benzolamps
 *
 */
public class MyAction implements ActionListener {
	JMenuItem mi;
	boolean isMove;
	MainFrame frame;

	public MyAction(MainFrame frame, JMenuItem mi, boolean isMove) {
		this.frame = frame;
		this.mi = mi;
		this.isMove = isMove;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int index = frame.getWordList().getSelectedIndex();
		for (DicLib d : frame.getDicLibs()) {
			if (mi.getText().equals(d.getName())) {
				if (index == -1) {
					new MessageFrame("��ʾ", "��ѡ��Ҫ" + (isMove ? "�ƶ�" : "����") + "�ĵ���!");
				} else {
					Word word = new Word();
					word.setOrigin(new String(frame.getCurrentDicLib().get(index).getOrigin()));
					word.setMeaning(new String(frame.getCurrentDicLib().get(index).getMeaning()));
					d.add(word);
					if (isMove)
						frame.getCurrentDicLib().remove(index);
					new MessageFrame("��ʾ", (isMove ? "�ƶ�" : "����") + "�ɹ�");
					frame.receiveMessage();
				}
			}
		}
	}
}
