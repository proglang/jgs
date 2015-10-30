package main.testclasses;

public class ArrayRef {

	public static void main(String[] args) {
		
	}
	
	public void read() {
		
	}
	
	public void writeWithIndexAsLocal(int i) {
		String[] wr = {"a","b"};
		wr[i] = "c";
	}
	
	public void writeWithFixedIndex() {
		String[] wr = {"a","b"};
		wr[1] = "c";
	}
}
