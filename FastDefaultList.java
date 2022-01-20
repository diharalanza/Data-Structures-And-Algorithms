package comp2402a3;
import java.lang.reflect.Array;
import java.lang.IllegalStateException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;


/**
 * Implements the List interface as a skiplist so that all the
 * standard operations take O(log n) time
 *
 * TODO: Modify this so that it creates a DefaultList, which is basically
 *       an infinitely long list whose values start out as null
 *
 */
public class FastDefaultList<T> extends AbstractList<T> {
	class Node {
		T x;
		Node[] next;
		int[] length;
		@SuppressWarnings("unchecked")
		public Node(T ix, int h) {
			x = ix;
			next = (Node[])Array.newInstance(Node.class, h+1);
			length = new int[h+1];
		}
		public int height() {
			return next.length - 1;
		}
	}

	/**
	 * This node sits on the left side of the skiplist
	 */
	protected Node sentinel;

	/**
	 * The maximum height of any element
	 */
	int h;

	/**
	 * The number of elements stored in the skiplist
	 */


	/**
	 * A source of random numbers
	 */
	Random rand;

	public FastDefaultList() {
		sentinel = new Node(null, 32);
		h = 0;
		rand = new Random(0);
	}


	/**
	 * Represents a node/index Pair
	 */
	protected class Pair {
		Node u;
		int i;

		Pair(Node u, int i) {
			this.u = u;
			this.i = i;
		}
	}

	/**
	 * Find the node that precedes list index i in the skiplist.
	 *
	 * @param i - the value to search for
	 * @return the predecessor of the node at index i or the final
	 * node if i exceeds size() - 1.
	 */
	protected Pair findPred(int i) {
		// Hint: It's not enough to know u, you also need the value j,
		// maybe change the return type to Pair and return the pair (u,j)
		Node u = sentinel;
		int r = h;
		int j = -1;   // index of the current node in list 0
		while (r >= 0) {
			while (u.next[r] != null && j + u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			r--;
		}
		Pair p = new Pair(u, j);
		return p;
	}

	public T get(int i) {
		// Hint: this is too restrictive any non-negative i is allowed
		// Hint: Are you sure findPred(i).next is the node you're looking for?
		if (i < 0) throw new IndexOutOfBoundsException();

		Pair u = findPred(i);
		Node n = u.u;
		int k = u.i;

		if(n.next[0] != null && n.length[0] + k == i){
			return n.next[0].x;
		}

		return null;

	}

	public T set(int i, T x) {
		// Hint: this is too restrictive any non-negative i is allowed
		// Hint: Are you sure findPred(i).next is the node you're looking for?
		//       If it's not, you'd better add a new node, maybe get everything
		//       else working and come back to this later.
		if (i < 0) throw new IndexOutOfBoundsException();

		Pair n = findPred(i);
		Node q = n.u;
		int o = n.i;
		T y;

		if (q.next[0] != null && q.length[0] + o == i) {
			y = q.next[0].x;
			q.next[0].x = x;
			return y;
		}

		Node v = new Node(x, pickHeight());



		Node u = sentinel;
		int k = v.height();
		int r = h;
		int j = -1; // index of u
		while (r >= 0) {
			while (u.next[r] != null && j+u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}



			if (r <= k) {
				v.next[r] = u.next[r];
				u.next[r] = v;
				v.length[r] = u.length[r] - (i - j);
				u.length[r] = i - j;
			}
			r--;
		}

		return null;
	}

	/**
	 * Insert a new node into the skiplist
	 * @param i the index of the new node
	 * @param w the node to insert
	 * @return the node u that precedes v in the skiplist
	 */
	protected Node add(int i, Node w) {
		Node u = sentinel;
		int k = w.height();
		int r = h;
		int j = -1; // index of u
		while (r >= 0) {
			while (u.next[r] != null && j+u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			u.length[r]++; // accounts for new node in list 0
			if (r <= k) {
				w.next[r] = u.next[r];
				u.next[r] = w;
				w.length[r] = u.length[r] - (i - j);
				u.length[r] = i - j;
			}
			r--;
		}
		return u;
	}



	/**
	 * Simulate repeatedly tossing a coin until it comes up tails.
	 * Note, this code will never generate a height greater than 32
	 * @return the number of coin tosses - 1
	 */
	protected int pickHeight() {
		int z = rand.nextInt();
		int k = 0;
		int m = 1;
		while ((z & m) != 0) {
			k++;
			m <<= 1;
		}
		return k;
	}

	public void add(int i, T x) {
		// Hint: bounds checking again!
		if (i < 0) throw new IndexOutOfBoundsException();
		Node w = new Node(x, pickHeight());
		if (w.height() > h)
			h = w.height();
		add(i, w);
	}

	public T remove(int i) {
		// Hint: bounds checking again!
		if (i < 0) throw new IndexOutOfBoundsException();
		T x = null;
		Node u = sentinel;
		int r = h;
		int j = -1; // index of node u
		while (r >= 0) {
			while (u.next[r] != null && j+u.length[r] < i) {
				j += u.length[r];
				u = u.next[r];
			}
			u.length[r]--;  // for the node we are removing
			if (j + u.length[r] + 1 == i && u.next[r] != null) {
				x = u.next[r].x;
				u.length[r] += u.next[r].length[r];
				u.next[r] = u.next[r].next[r];
				if (u == sentinel && u.next[r] == null)
					h--;
			}
			r--;
		}
		return x;
	}

	public int size() {
		return Integer.MAX_VALUE;
	}

	public String toString() {
		// This is just here to help you a bit with debugging
		StringBuilder sb = new StringBuilder();
		int i = -1;
		Node u = sentinel;
		while (u.next[0] != null) {
			i += u.length[0];
			u = u.next[0];
			sb.append(" " + i + "=>" + u.x);
		}
		return sb.toString();
	}

	public static void main(String[] args) {

// put your test code here if you like
	}
}
