
public class SwitchCaseStmts {

	public int m1(int i) {
		int add = 1;
		switch (i) {
			case 1:
				add += m1(i++);
				break;

			case 2:
				add += m1(i++);
				break;

			case 3:
				add += m1(i++);
				break;
				
			default:
				break;
		}
		return add;
	}
	
	public String m2(String str) {
		switch (str) {
			case "Hello":
				str += " World";
				break;

			case "Bye":
				str = "See you";
				break;
			default:
				str = "?";
				break;
		}
		return str;
		
	}

}
