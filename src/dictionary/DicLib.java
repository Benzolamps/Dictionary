package dictionary;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * �ʿ���
 * 
 * @author Benzolamps
 *
 */
public class DicLib extends Vector<Word> { // �ʿ�����Vector<Word>������
	private static final long serialVersionUID = -2809128821012099186L;

	private String name;

	/**
	 * Ĭ�Ϲ��췽���� Ĭ�ϴʿ���Ϊ"New Dictionary Library"
	 */
	public DicLib() {
		name = "New Dictionary Library";
	}

	public DicLib(String name) {
		this.name = name;
	}

	public DicLib(Collection<? extends Word> c, String name) {
		super(c);
		this.name = name;
	}

	public DicLib(int initialCapacity, String name) {
		super(initialCapacity);
		this.name = name;
	}

	public DicLib(int initialCapacity, int capacityIncrement, String name) {
		super(initialCapacity, capacityIncrement);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ��bar��ͷ�ĵ�һ������
	 * 
	 * @param bar ��������ĸ
	 * @return �����ڴʿ��е�λ�ã�δ�ҵ�����-1
	 */
	public int index(String bar) {
		for (int i = 0; i < size(); i++) {
			if (get(i).getOrigin().startsWith(bar)) { // �жϵ����Ƿ���bar��ͷ
				return i;
			}
		}
		return -1;
	}

	/**
	 * �Դʿ��������
	 * 
	 * @param isDesc ��isDescΪ��ʱ���������򣻷�֮����
	 */
	public void sort(boolean isDesc) {
		if (isDesc) {
			Comparator<Word> comp = Collections.reverseOrder();
			Collections.sort(this, comp);
		} else {
			Collections.sort(this);
		}

	}

	public String toString() {
		return name;
	}
}
