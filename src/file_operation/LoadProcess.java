package file_operation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import dictionary.DicLib;
import dictionary.Word;

/**
 * ���ع���
 * 
 * @author Benzolamps
 *
 */
public class LoadProcess {
	public static DicLib readDic(String str) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(str)));
		;
		String s1 = "Dictionary name=";
		String s2 = reader.readLine();
		reader.readLine(); // �ٶ�ȡһ�����У�Ȼ��ʼ��ȡ��һ������
		DicLib dicLib = new DicLib(s2.replace(s1, ""));
		while (true) {
			String temp = reader.readLine();

			if (temp != null) {
				// ͨ���Ʊ�����ָ��ַ������������ֵ��������壬�������û���Ʊ��������׳�IndexOutOfBoundsException�쳣
				Word word = null;
				try {
					word = new Word(temp.split("\t")[0], temp.split("\t")[1]);
				} catch (IndexOutOfBoundsException e) { // ��temp.split("\t")����Խ�磬�˳�whileѭ��
					break;
				}
				dicLib.add(word);
			} else
				break;
		}
		dicLib.sort(false);
		return dicLib;
	}
}
