import java.util.AbstractList;

public class ArrayList61B<Element> extends AbstractList<Element> {
	private Element[] array;
	private int numElements;

	public ArrayList61B(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		array = (Element[]) new Object[initialCapacity];
		numElements = 0;
	}

	public ArrayList61B() {
		this(1);
	}

	public Element get(int i) {
		if (i < 0 || i >= numElements) {
			throw new IllegalArgumentException();
		}
		return array[i];
	}

	public boolean add(Element item) {
		if (numElements == array.length) {
			Element[] doubleSizedArray = (Element[]) new Object[array.length * 2];
			for (int i = 0; i < array.length; i++) {
				doubleSizedArray[i] = array[i];
			}
			array = doubleSizedArray;
		}
		array[numElements] = item;
		numElements += 1;
		return true;
	}

	public int size() {
		return numElements;
	}
}