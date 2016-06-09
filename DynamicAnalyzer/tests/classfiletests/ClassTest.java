package classfiletests;

import classfiletests.utils.ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import utils.logging.L1Logger;

import java.util.Arrays;
import java.util.logging.Logger;



@RunWith(Parameterized.class)
public class ClassTest{
    
	private final String name;
	private final boolean hasIllegalFlow;
    

	/**
	 * @param name
	 * @param hasIllegalFlow
	 */
	public ClassTest(String name, boolean hasIllegalFlow) {
    
		this.name = name;
		this.hasIllegalFlow = hasIllegalFlow;
	}
    
	Logger logger = L1Logger.getLogger();
	
	@Parameters(name = "Name: {0}")
	public static Iterable<Object[]> generateParameters() {
		return Arrays.asList(
				new Object[] { "AccessFieldsOfObjectsFail", true}, 
				new Object[] { "AccessFieldsOfObjectsSuccess", false}, 
				new Object[] { "ArithmeticExpressionsFail", true},
				new Object[] { "ArithmeticExpressionsSuccess", false},
				new Object[] { "ArrayRef", true},
				new Object[] { "ExtClasses", true}, 
				new Object[] { "FieldsSuccess", false},
				new Object[] { "FieldWriteFail", true}, 
				new Object[] { "ForLoop", true},
				new Object[] { "IfStmt", true}, 
				new Object[] { "InvokeInternalMethod", true}, 
				new Object[] { "InvokeLibMethod", true},
				new Object[] { "MakeHigh", true},
				new Object[] { "MulArray", true}, 
				new Object[] { "NonStaticMethods", true}, 
				new Object[] { "Simple", true}, 
				new Object[] { "StaticMethods", true}, 
				new Object[] { "SwitchStmt", false},
				new Object[] { "SwitchStmtFail", true},
				new Object[] { "WhileLoop", false},
				new Object[] { "WhileLoopFail", true});
	}

	@Test
	public void test() {
		System.out.println("\n\n\n");
		logger.info("Start of executing main.testclasses." + name + "");

		ClassRunner.testClass(name, hasIllegalFlow);

		logger.info("Finished executing main.testclasses." + name + "");
	}
}
