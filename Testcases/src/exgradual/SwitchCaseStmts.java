package exgradual;

public class SwitchCaseStmts {

    final int one = 1;
    final int two = 2;
    final int three = 3;

    public static void main(String[] args) {

    }

    public int m1(int i) {
        int add = 1;
        switch (i) {
        case one:
            add += m1(i++);
            break;

        case two:
            add += m1(i++);
            break;

        case three:
            add += m1(i++);
            break;

        default:
            break;
        }
        return add;
    }

}
