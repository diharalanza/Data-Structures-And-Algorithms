package comp2402a4;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.lang.reflect.Array;


/**
* An unfinished implementation of an Countdown tree (for exercises)
* @author morin
*
* @param <T>
*/
public class CountdownTree<T> extends
BinarySearchTree<CountdownTree.Node<T>, T> implements SSet<T> {

	// countdown delay factor
	double d;

	public static class Node<T> extends BSTNode<Node<T>,T> {
		int timer;  // the height of the node
	}

	public CountdownTree(double d) {
		this.d = d;
		sampleNode = new Node<T>();
		c = new DefaultComparator<T>();
	}

	public boolean add(T x) {
		Node<T> u = new Node<T>();
		u.timer = (int)Math.ceil(d);
		u.x = x;
		if (super.add(u)) {
			u = u.parent;
			while (u != nil) {
				u.timer = u.timer - 1;
				if (u.timer <= 0) {
					explode(u);
				}
				u = u.parent;
			}
			// add some code here
			return true;
		}
		return false;
	}

	public void splice(Node<T> u) {
		Node<T> w = u.parent;
		super.splice(u);
		// add some code here (we just removed u from the tree)
		while (w != nil) {
			w.timer = w.timer - 1;
			if (w.timer <= 0)
				explode(w);
			w = w.parent;
		}
	}

	protected void explode(Node<T> u) {
		int i = size(u);
		Node<T> parent = u.parent;
		Node<T>[] array = new Node[i];
		packIntoArray(u, array, 0);
		if (parent == nil) {
			r = buildBalanced(array, 0, i);
			r.parent = nil;
		} else if (parent.right == u) {
			parent.right = buildBalanced(array, 0, i);
			parent.right.parent = parent;
		} else {
			parent.left = buildBalanced(array, 0, i);
			parent.left.parent = parent;
		}

		for(Node<T> node : array){
			node.timer = (int)Math.ceil(d*size(node));
		}

	}


	// Write this code to explode u
		// Make sure to update u.parent and/or r (the tree root) as appropriate



	Node<T> buildBalanced(Node<T>[] u, int i, int j) {
		if (j == 0)
			return nil;
		int m = j / 2;
		u[i + m].left = buildBalanced(u, i, m);
		if (u[i + m].left != nil)
			u[i + m].left.parent = u[i + m];
		u[i + m].right = buildBalanced(u, i + m + 1, j - m - 1);
		if (u[i + m].right != nil)
			u[i + m].right.parent = u[i + m];
		return u[i + m];
	}


	int packIntoArray(Node<T> u, Node<T>[] array, int i) {
		if (u == nil) {
			return i;
		}
		i = packIntoArray(u.left, array, i);
		array[i++] = u;
		return packIntoArray(u.right, array, i);
	}

	// Here is some test code you can use
	public static void main(String[] args) {
		Testum.sortedSetSanityTests(new SortedSSet<Integer>(new CountdownTree<Integer>(1)), 1000);
		Testum.sortedSetSanityTests(new SortedSSet<Integer>(new CountdownTree<Integer>(2.5)), 1000);
		Testum.sortedSetSanityTests(new SortedSSet<Integer>(new CountdownTree<Integer>(0.5)), 1000);

		java.util.List<SortedSet<Integer>> ell = new java.util.ArrayList<SortedSet<Integer>>();
		ell.add(new java.util.TreeSet<Integer>());
		ell.add(new SortedSSet<Integer>(new CountdownTree<Integer>(1)));
		ell.add(new SortedSSet<Integer>(new CountdownTree<Integer>(2.5)));
		ell.add(new SortedSSet<Integer>(new CountdownTree<Integer>(0.5)));
		Testum.sortedSetSpeedTests(ell, 1000000);
	}
}
