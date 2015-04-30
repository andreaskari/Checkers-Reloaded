import java.util.Comparator;
import java.util.TreeMap;

public class AlphabetComparator implements Comparator<Character> {
	private TreeMap<Character, Integer> alphabetMap;

	public AlphabetComparator(TreeMap<Character, Integer> am) {
		alphabetMap = am;
	}

	public TreeMap<Character, Integer> getMap() {
		return alphabetMap;
	}

	public int compare(Character o1, Character o2) {
		return alphabetMap.get(o1).intValue() - alphabetMap.get(o2).intValue();
	}

	public boolean equals(Object obj) {
		if (obj instanceof AlphabetComparator) {
			return alphabetMap == ((AlphabetComparator) obj).getMap();
		}
		return false;
	}
}