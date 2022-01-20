package comp2402a2;

import java.util.*;

/**
 * An implementation of copyArray FIFO Queue as copyArray singly-linked list.
 * This also includes the stack operations push and pop, which
 * operate on the head of the queue
 * @author morin
 *
 * @param <T> the class of objects stored in the queue
 */
public class SLList<T> extends AbstractList<T> implements Queue<T> {
	class Node {
		T x;
		Node next;
	}

	/**
	 * Front of the queue
	 */
	Node head;

	/**
	 * Tail of the queue
	 */
	Node tail;

	/**
	 * The number of elements in the queue
	 */
	int n;


	public T get(int i) {

		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();

		return getNode(i).x;



	}


	public T set(int i, T x) {

		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();

		Node u = getNode(i);
		T y = u.x; //copying deleted nodes data to return
		u.x = x;
		return y;

	}


	public void add(int i, T x) {

		if (i < 0 || i > n) throw new IndexOutOfBoundsException();

		if(i == n){
			this.add(x);


		}

		else if (i==0 && i != n ){
			this.push(x);

		}

		else{
			Node u = new Node();
			u.x = x;

			Node copy = getNode(i-1);

			u.next= copy.next;

			copy.next = u;
			n++;

		}

	}


	public T remove(int i) {

		if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();

		// if deleting head of list
		if (i == 0)
		{
			return this.pop();
		}


		//to change node before deleting nodes next pointer
		Node prevNode = head;


		for (int j=0; j < i-1; j++)
			prevNode = prevNode.next;

		Node next = prevNode.next.next;

		T y = prevNode.next.x;

		prevNode.next = next;

		if (next == null){
			tail = prevNode;

		}
		--n;
		return y;

	}


	public void reverse() {


		Node pointer = head;
		Node previous = null;
		Node current = null;

		Node newTail = head;

		while (pointer != null) {
			current = pointer;
			pointer = pointer.next;

			// reverse the link
			current.next = previous;
			previous = current;
			head = current;
		}

		tail = newTail;


	}


	public Iterator<T> iterator() {
		class SLIterator implements Iterator<T> {
			protected Node p;

			public SLIterator() {
				p = head;
			}
			public boolean hasNext() {
				return p != null;
			}
			public T next() {
				T x = p.x;
				p = p.next;
				return x;
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
		return new SLIterator();
	}

	public int size() {
		return n;
	}

	public boolean add(T x) {
		Node u = new Node();
		u.x = x;
		if (n == 0) {
			head = u;
		} else {
			tail.next = u;
		}
		tail = u;
		n++;
		return true;
	}

	public boolean offer(T x) {
		return add(x);
	}

	public T peek() {
		if (n == 0) return null;
		return head.x;
	}

	public T element() {
		if (n == 0) throw new NoSuchElementException();
		return head.x;
	}

	public T poll() {
		if (n == 0)
			return null;
		T x = head.x;
		head = head.next;
		if (--n == 0)
			tail = null;
		return x;
	}

	/**
	 * Stack push operation - push x onto the head of the list
	 * @param x the element to push onto the stack
	 * @return x
	 */
	public T push(T x) {
		Node u = new Node();
		u.x = x;
		u.next = head;
		head = u;
		if (n == 0)
			tail = u;
		n++;
		return x;
	}

	protected void deleteNext(Node u) {
		if (u.next == tail)
			tail = u;
		u.next = u.next.next;
	}

	protected void addAfter(Node u, Node v) {
		v.next = u.next;
		u.next = v;
		if (u == tail)
			tail = v;
	}

	protected Node getNode(int i) {
		Node u = head;
		for (int j = 0; j < i; j++)
			u = u.next;
		return u;
	}

	/**
	 * Stack pop operation - pop off the head of the list
	 * @return the element popped off
	 */
	public T remove() {
		if (n == 0)	return null;
		T x = head.x;
		head = head.next;
		if (--n == 0) tail = null;
		return x;
	}

	public T pop() {
		if (n == 0)	return null;
		T x = head.x;
		head = head.next;
		if (--n == 0) tail = null;
		return x;
	}

	public static void display(SLList<String> list){
		for(String s: list){
			System.out.print(s + " ");
		}
		System.out.println();
	}


	public static void main(String[] args) {

		SLList<String> list = new SLList<String>();

		System.out.println("add s at index 0");
		list.add(0,"s");
		display(list);
		System.out.println("Head: " + list.head.x);
		System.out.println("Tail: " + list.tail.x);
		System.out.println("__________________");


		System.out.println("add k at index 0");
		list.add(0,"k");
		display(list);
		System.out.println("Head: " + list.head.x);
		System.out.println("Tail: " + list.tail.x);
		System.out.println("__________________");

		System.out.println("add p at index 2");
		list.add(2,"p");
		display(list);
		System.out.println("Head: " + list.head.x);
		System.out.println("Tail: " + list.tail.x);
		System.out.println("__________________");

		System.out.println("add q at index 1");
		list.add(1,"q");
		display(list);
		System.out.println("Head: " + list.head.x);
		System.out.println("Tail: " + list.tail.x);
		System.out.println("__________________");

		System.out.println("add n at index 0");
		list.add(0,"n");
		display(list);
		System.out.println("Head: " + list.head.x);
		System.out.println("Tail: " + list.tail.x);
		System.out.println("__________________");

		System.out.println("add m at index 5");
		list.add(5,"m");
		display(list);
		System.out.println("Head: " + list.head.x);
		System.out.println("Tail: " + list.tail.x);
		System.out.println("__________________");




		display(list);
		System.out.println("__________________");

		System.out.println("Remove node at index 0");
		list.remove(0);
		display(list);
		System.out.println("Head: " + list.head.x);
		System.out.println("Tail: " + list.tail.x);
		System.out.println("__________________");

		System.out.println("Remove node at index 4");
		list.remove(4);
		display(list);
		System.out.println("Head: " + list.head.x);
		System.out.println("Tail: " + list.tail.x);
		System.out.println("__________________");

		System.out.println("Remove node at index 2");
		list.remove(2);
		display(list);
		System.out.println("Head: " + list.head.x);
		System.out.println("Tail: " + list.tail.x);
		System.out.println("__________________");

		System.out.println("Remove node at index 2");
		list.remove(2);
		display(list);
		System.out.println("Head: " + list.head.x);
		System.out.println("Tail: " + list.tail.x);
		System.out.println("__________________");


		System.out.println("Remove node at index 1");
		list.remove(1);
		display(list);
		System.out.println("Head: " + list.head.x);
		System.out.println("Tail: " + list.tail.x);
		System.out.println("__________________");

		System.out.println("Remove node at index 0");
		list.remove(0);
		display(list);




	}

}
