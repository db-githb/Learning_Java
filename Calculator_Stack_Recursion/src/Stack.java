
public class Stack<T> {
	class Node{
		Node next;
		T element;
		
		Node(){
			next = null;
			element = null;
		}
	}
	
	Node top = null;
	private int count;
	
	Stack() {
		top = null;
		count = 0;
	}
	
	T pop() {
		if (top == null) {
			return null;
		}
		Node output = top;
		top = top.next;	
		count--;
		return output.element;
	}
	
	void push(T in) {
		Node input = new Node();
		input.element = in;
		input.next = top;
		top = input;
		count++;
	}
	
	T peek() {
		//return <condition> ? <if true return x> : <else return y>
		return (top == null) ? null : top.element;
	}
	
	int getCount() {
		return count;
	}
}
