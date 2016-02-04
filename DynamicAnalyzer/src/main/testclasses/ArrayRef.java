package main.testclasses;

public class ArrayRef {

  /**
   * @param args
   */
  public static void main(String[] args) {
    read();
    writeWithIndexAsLocal(1);
    writeWithFixedIndex();
  }
	
  /**
   * 
   */
  public static void read() {
    String [] arr = {"b","a"};
    String res = arr[0];
    System.out.println("Read-Result: " + res);
  }
	
  /**
   * @param i
   */
  public static void writeWithIndexAsLocal(int i) {
    String[] wr = {"a","b"};
    wr[i] = "c";
    System.out.println("Write-Result: " + wr[i]);
  }
	
  /**
   * 
   */
  public static void writeWithFixedIndex() {
    String[] wr = {"a","b"};
    wr[1] = "c";
    System.out.println("Write-Result: " + wr[1]);
  }
}
