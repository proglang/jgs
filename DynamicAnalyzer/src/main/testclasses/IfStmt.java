package main.testclasses;

public class IfStmt {

  /**
   * @param args
   */
  public static void main(String[] args) {
	IfStmt thisObj = new IfStmt();
    // int res = 0;
    thisObj.multIfs(1);
    thisObj.multIfs(3);
    thisObj.multIfs(5);
  }

  /**
   * @param x
   * @return
   */
  public int multIfs(int x) {
    if (x < 0) {
      x = 0;
    } else if (x < 2) {
      x = 2;
    } else if (x < 4) {
      x = 4;
    } else {
      x = 6;
    }
    return x;
  }
}
