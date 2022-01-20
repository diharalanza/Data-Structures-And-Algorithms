package comp2402a2;

import java.util.AbstractList;

/**
 * @author morin
 *
 * @param <T> the type of objects stored in the List
 */
public class BlockedList<T> extends AbstractList<T> {
	/**
	 * keeps track of the class of objects we store
	 */
	Factory<T> f;

	/**
	 * The number of elements stored
	 */
	int n;

	/**
	 * The block size
	 */
	int b;


	T[] copyArray;
	int next;

	/**
	 * Constructor
	 * @param t the type of objects that are stored in this list
	 * @param b the block size
	 */
	public BlockedList(Class<T> t, int b) {
		f = new Factory<T>(t);
		copyArray = f.newArray(1);
		next = 0;
		n = 0;
	}

	public int size() {
		return n;
	}

	public T get(int i) {
		if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		return copyArray[(next+i)%copyArray.length];
	}

	public T set(int i, T x) {
		if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		T y = copyArray[(next +i)% copyArray.length];
		copyArray[(next +i)% copyArray.length] = x;
		return y;
	}

	protected void resize() {
		T[] b = f.newArray(Math.max(2*n,1));
		for (int k = 0; k < n; k++)
			b[k] = copyArray[(next +k) % copyArray.length];
		copyArray = b;
		next = 0;
	}

	public void add(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		if (n+1 > copyArray.length) resize();
		if (i < n/2) {
			next = (next == 0) ? copyArray.length - 1 : next - 1;
			for (int k = 0; k <= i-1; k++)
				copyArray[(next +k)% copyArray.length] = copyArray[(next +k+1)% copyArray.length];
		} else {
			for (int k = n; k > i; k--)
				copyArray[(next +k)% copyArray.length] = copyArray[(next +k-1)% copyArray.length];
		}
		copyArray[(next +i)% copyArray.length] = x;
		n++;
	}

	public T remove(int i) {
		if (i < 0 || i > n - 1)	throw new IndexOutOfBoundsException();
		T x = copyArray[(next +i)% copyArray.length];
		if (i < n/2) {
			for (int k = i; k > 0; k--)
				copyArray[(next +k)% copyArray.length] = copyArray[(next +k-1)% copyArray.length];
			next = (next + 1) % copyArray.length;
		} else {
			for (int k = i; k < n-1; k++)
				copyArray[(next +k)% copyArray.length] = copyArray[(next +k+1)% copyArray.length];
		}
		n--;
		if (3*n < copyArray.length) resize();
		return x;
	}

}
