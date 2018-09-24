package file_operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import dictionary.DicLib;
import dictionary.Word;

/**
 * 词库文件类
 * 
 * @author Benzolamps
 *
 */
public class DicFile extends File { // 定义词库文件类是File类的子类
	private static final long serialVersionUID = -3007674007192114857L;

	private BufferedReader raf = null; // 用于从文件中读取词库信息
	private PrintWriter waf = null; // 用于将词库信息写入文件

	public DicFile(String pathName, boolean isWriter) throws IOException {
		// TODO Auto-generated constructor stub
		super(pathName);
	}

	/**
	 * 定义此构造方法的目的是方便将词库文件加入项目，并通过URL转化为URI读取文件
	 */
	public DicFile(URI uri, boolean isWriter) throws IOException {
		// TODO Auto-generated constructor stub
		super(uri);
	}

	/**
	 * 通过从文件中读取第一个文本行来确定该文件是否为适合于项目使用的词库文件，如果不是，返回null；如果是，则返回词库名
	 * 当文件第一个文本行内容为"Dictionary name=词库名"时，才不会返回null
	 * 
	 * @return 如果读取词库文件成功，返回词库名；否则返回null
	 */
	public String getDicName() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(this));
		String s1 = "Dictionary name=";
		String s2 = r.readLine();
		r.close();
		if (!s2.startsWith(s1)) { // 判断s2是否以"Dictionary name="开头
			return null;
		}
		return s2.replace(s1, "");
	}

	/**
	 * 从资源中读取词库信息，并返回一个词库对象
	 * 
	 * @param str 用于表示文件读取进度的字符串缓冲区，方便在后台线程中动态改变读取进度
	 * @return 词库对象
	 */
	public DicLib readDic(StringBuffer str) throws IOException, IndexOutOfBoundsException {
		raf = new BufferedReader(new FileReader(this));
		String s1 = "Dictionary name=";
		String s2 = raf.readLine(); // 读取文件第一行
		if (!s2.startsWith(s1)) {
			return null;
		}

		raf.readLine(); // 再读取一个空行，然后开始读取第一个单词
		DicLib dicLib = new DicLib(s2.replace(s1, ""));
		long l = 0; // 用于组成文件读取进度，表示已读取的文件内容大小
		while (true) {
			String temp = raf.readLine();

			if (temp != null) {
				l += temp.length(); // 读取一行，将该行的大小增加到l

				// 将l与文件大小的比值转化为百分数，作为读取进度，并存储到字符串缓冲区str
				str.replace(0, str.length(), String.format("%.2f%%", l / (double) length() * 100));

				// 通过制表符来分割字符串，用于区分单词与释义，如果该行没有制表符，则会抛出IndexOutOfBoundsException异常
				Word word = null;
				try {
					word = new Word(temp.split("\t")[0], temp.split("\t")[1]);
				} catch (IndexOutOfBoundsException e) { // 当temp.split("\t")数组越界，退出while循环
					break;
				}
				dicLib.add(word);
			} else {
				break;
			}
		}
		dicLib.sort(false);
		return dicLib;
	}

	/**
	 * 将词库信息写入文件
	 * 
	 * @param dicLib 要写入的词库对象
	 * @param str 用于表示文件写入进度的字符串缓冲区，方便在后台线程中动态改变写入进度
	 */
	public void writeDic(DicLib dicLib, StringBuffer str) throws IOException {
		waf = new PrintWriter(new FileWriter(this));
		waf.println("Dictionary name=" + dicLib.getName()); // 将文件第一行写入"Dictionary name=词库名"
		waf.println(); // 写入一个空行
		for (int i = 0; i < dicLib.size(); i++) {
			// 定义一个单词行（单词+制表符+释义）
			String temp = dicLib.get(i).getOrigin() + "\t" + dicLib.get(i).getMeaning();

			waf.println(temp); // 写入一行单词

			// 将数组当前索引与词库总的大小的比值转化为百分数，作为写入进度，并存储到字符串缓冲区str
			str.replace(0, str.length(), String.format("%.2f%%", i / (double) dicLib.size() * 100));
		}
		waf.close();
	}
}
