package dictionary;

/**
 * 单词类
 * 
 * @author Benzolamps
 *
 */
public class Word implements Comparable<Word> { // 实现Comparable接口，方便排序

	private String origin; // 单词
	private String meaning; // 释义

	public Word() {
		origin = "";
		meaning = "";
	}

	public Word(String origin, String meaning) {
		this.origin = origin;
		this.meaning = meaning;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public String toString() {
		return origin;
	}

	@Override
	/**
	 * 重写Comparable的compareTo(Object otherObject)方法，采用单词字符串的按字典顺序排序（忽略大小写）
	 * 
	 * @return 返回origin与other.origin的按字典顺序排序（忽略大小写）结果
	 */
	public int compareTo(Word other) {
		return origin.compareToIgnoreCase(other.origin);
	}
}
