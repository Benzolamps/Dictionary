package file_operation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import dictionary.DicLib;
import dictionary.Word;

/**
 * 加载过程
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
		reader.readLine(); // 再读取一个空行，然后开始读取第一个单词
		DicLib dicLib = new DicLib(s2.replace(s1, ""));
		while (true) {
			String temp = reader.readLine();

			if (temp != null) {
				// 通过制表符来分割字符串，用于区分单词与释义，如果该行没有制表符，则会抛出IndexOutOfBoundsException异常
				Word word = null;
				try {
					word = new Word(temp.split("\t")[0], temp.split("\t")[1]);
				} catch (IndexOutOfBoundsException e) { // 当temp.split("\t")数组越界，退出while循环
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
