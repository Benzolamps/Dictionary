package dictionary;

/**
 * ������
 * 
 * @author Benzolamps
 *
 */
public class Word implements Comparable<Word> { // ʵ��Comparable�ӿڣ���������

	private String origin; // ����
	private String meaning; // ����

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
	 * ��дComparable��compareTo(Object otherObject)���������õ����ַ����İ��ֵ�˳�����򣨺��Դ�Сд��
	 * 
	 * @return ����origin��other.origin�İ��ֵ�˳�����򣨺��Դ�Сд�����
	 */
	public int compareTo(Word other) {
		return origin.compareToIgnoreCase(other.origin);
	}
}
