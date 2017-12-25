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
 * �ʿ��ļ���
 * 
 * @author Benzolamps
 *
 */
public class DicFile extends File { // ����ʿ��ļ�����File�������
	private static final long serialVersionUID = -3007674007192114857L;

	private BufferedReader raf = null; // ���ڴ��ļ��ж�ȡ�ʿ���Ϣ
	private PrintWriter waf = null; // ���ڽ��ʿ���Ϣд���ļ�

	public DicFile(String pathName, boolean isWriter) throws IOException {
		// TODO Auto-generated constructor stub
		super(pathName);
	}

	/**
	 * ����˹��췽����Ŀ���Ƿ��㽫�ʿ��ļ�������Ŀ����ͨ��URLת��ΪURI��ȡ�ļ�
	 */
	public DicFile(URI uri, boolean isWriter) throws IOException {
		// TODO Auto-generated constructor stub
		super(uri);
	}

	/**
	 * ͨ�����ļ��ж�ȡ��һ���ı�����ȷ�����ļ��Ƿ�Ϊ�ʺ�����Ŀʹ�õĴʿ��ļ���������ǣ�����null������ǣ��򷵻شʿ���
	 * ���ļ���һ���ı�������Ϊ"Dictionary name=�ʿ���"ʱ���Ų��᷵��null
	 * 
	 * @return �����ȡ�ʿ��ļ��ɹ������شʿ��������򷵻�null
	 */
	public String getDicName() throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(this));
		String s1 = "Dictionary name=";
		String s2 = r.readLine();
		r.close();
		if (!s2.startsWith(s1)) { // �ж�s2�Ƿ���"Dictionary name="��ͷ
			return null;
		}
		return s2.replace(s1, "");
	}

	/**
	 * ����Դ�ж�ȡ�ʿ���Ϣ��������һ���ʿ����
	 * 
	 * @param str ���ڱ�ʾ�ļ���ȡ���ȵ��ַ����������������ں�̨�߳��ж�̬�ı��ȡ����
	 * @return �ʿ����
	 */
	public DicLib readDic(StringBuffer str) throws IOException, IndexOutOfBoundsException {
		raf = new BufferedReader(new FileReader(this));
		String s1 = "Dictionary name=";
		String s2 = raf.readLine(); // ��ȡ�ļ���һ��
		if (!s2.startsWith(s1)) {
			return null;
		}

		raf.readLine(); // �ٶ�ȡһ�����У�Ȼ��ʼ��ȡ��һ������
		DicLib dicLib = new DicLib(s2.replace(s1, ""));
		long l = 0; // ��������ļ���ȡ���ȣ���ʾ�Ѷ�ȡ���ļ����ݴ�С
		while (true) {
			String temp = raf.readLine();

			if (temp != null) {
				l += temp.length(); // ��ȡһ�У������еĴ�С���ӵ�l

				// ��l���ļ���С�ı�ֵת��Ϊ�ٷ�������Ϊ��ȡ���ȣ����洢���ַ���������str
				str.replace(0, str.length(), String.format("%.2f%%", l / (double) length() * 100));

				// ͨ���Ʊ�����ָ��ַ������������ֵ��������壬�������û���Ʊ��������׳�IndexOutOfBoundsException�쳣
				Word word = null;
				try {
					word = new Word(temp.split("\t")[0], temp.split("\t")[1]);
				} catch (IndexOutOfBoundsException e) { // ��temp.split("\t")����Խ�磬�˳�whileѭ��
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
	 * ���ʿ���Ϣд���ļ�
	 * 
	 * @param dicLib Ҫд��Ĵʿ����
	 * @param str ���ڱ�ʾ�ļ�д����ȵ��ַ����������������ں�̨�߳��ж�̬�ı�д�����
	 */
	public void writeDic(DicLib dicLib, StringBuffer str) throws IOException {
		waf = new PrintWriter(new FileWriter(this));
		waf.println("Dictionary name=" + dicLib.getName()); // ���ļ���һ��д��"Dictionary name=�ʿ���"
		waf.println(); // д��һ������
		for (int i = 0; i < dicLib.size(); i++) {
			// ����һ�������У�����+�Ʊ��+���壩
			String temp = dicLib.get(i).getOrigin() + "\t" + dicLib.get(i).getMeaning();

			waf.println(temp); // д��һ�е���

			// �����鵱ǰ������ʿ��ܵĴ�С�ı�ֵת��Ϊ�ٷ�������Ϊд����ȣ����洢���ַ���������str
			str.replace(0, str.length(), String.format("%.2f%%", i / (double) dicLib.size() * 100));
		}
		waf.close();
	}
}
