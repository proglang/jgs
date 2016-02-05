package main.testclasses;

public class IfStmt {

  /**
   * @param args
   */
  public void main(String[] args) {
    // int res = 0;
    multIfs(1);
    multIfs(3);
    multIfs(5);
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
