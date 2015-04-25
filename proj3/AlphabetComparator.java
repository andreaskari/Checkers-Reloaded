import java.util.Comparator;
import java.util.HashMap;

public class AlphabetComparator implements Comparator<Character> {
	private HashMap<Character, Integer> alphabetMap;

	public AlphabetComparator(HashMap<Character, Integer> am) {
		alphabetMap = am;
	}

	public HashMap<Character, Integer> getMap() {
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