package dictionary;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * 词库类
 * 
 * @author Benzolamps
 *
 */
public class DicLib extends Vector<Word> { // 词库类是Vector<Word>的子类
	private static final long serialVersionUID = -2809128821012099186L;

	private String name;

	/**
	 * 默认构造方法， 默认词库名为"New Dictionary Library"
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
	 * 获取以bar开头的第一个单词
	 * 
	 * @param bar 单词首字母
	 * @return 单词在词库中的位置，未找到返回-1
	 */
	public int index(String bar) {
		for (int i = 0; i < size(); i++) {
			if (get(i).getOrigin().startsWith(bar)) { // 判断单词是否以bar开头
				return i;
			}
		}
		return -1;
	}

	/**
	 * 对词库进行排序
	 * 
	 * @param isDesc 当isDesc为真时，降序排序；反之升序
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
