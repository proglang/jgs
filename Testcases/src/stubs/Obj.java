package stubs;

import security.Definition.Constraints;
import security.Definition.FieldSecurity;

public class Obj {

		@FieldSecurity("low")
		public int lowIField;

		@FieldSecurity("high")
		public int highIField;
		
		@FieldSecurity({"low", "low"})
		public int[] lowLowIField;

		@FieldSecurity({"low", "high"})
		public int[] lowHighIField;
		
		@FieldSecurity({"high", "high"})
		public int[] highHighIField;

		@Constraints("low <= @return")
		public int _lowI(){
			return lowIField;
		}
		
		@Constraints("high <= @return")
		public int _highI() {
			return highIField;
		}
		
		@Constraints({"@0 <= low", "low <= @return"})
		public int low_lowI(int a0){
			return lowIField;
		}
		
		@Constraints({"@0 <= low", "high <= @return"})
		public int low_highI(int a0) {
			return highIField;
		}
		
		@Constraints({"@0 <= high", "low <= @return"})
		public int high_lowI(int a0){
			return lowIField;
		}
		
		@Constraints({"@0 <= high", "high <= @return"})
		public int high_highI(int a0) {
			return highIField;
		}
		
}
