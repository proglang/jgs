package junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import logging.SootLoggerUtils;
import logging.SootLoggerLevel;
import model.MessageStore;
import model.MessageStore.Message;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class AnalysisFail {
	
	private static class ExpectedError {
		
		private long lineNo;
		private String errorMsg;
		
		public ExpectedError(long lineNo, String errorMsg) {
			this.lineNo = lineNo;
			this.errorMsg = String.format(errorMsg, lineNo);
		}
		
	}
	
	private static final String MAIN = "TaintTrackingFail";
	private static final String ARRAY = "TaintTrackingArray";
	private static final String FIELD = "TaintTrackingField";
	private static final String EXPR = "TaintTrackingExpr";
	private static final String ID_METHODS = "TaintTrackingIdMethods";
	private static final String LOOP = "TaintTrackingLoop";
	private static final String IF_ELSE = "TaintTrackingIfElse";
	private static final String IF_ELSE_2 = "TaintTrackingIfElse2";
	private static final String METHOD = "TaintTrackingMethod";
	private static final String OBJECT = "TaintTrackingObject";
	private static final String STATIC_FIELD = "TaintTrackingStaticField";
	private static final String STATIC_METHOD = "TaintTrackingStaticMethod";
	
	private static MessageStore messageStore;
	public static int overAllCount = 0;

	@BeforeClass
	public static final void init() {
		try {
			messageStore = SootLoggerUtils.restoreSerializedMessageStore();
		} catch (NullPointerException e) {
			fail("Can't fetch the serialized message store.");
		}		
	}
	
	@AfterClass
	public static final void finish() {
		System.out.println("Message Report");
		System.out.println("Security: " + messageStore.getAllMessages(SootLoggerLevel.SECURITY).size());
		System.out.println("Exception: " + messageStore.getAllMessages(SootLoggerLevel.EXCEPTION).size());
		System.out.println("Side-Effect: " + messageStore.getAllMessages(SootLoggerLevel.SIDEEFFECT).size());
		System.out.println("Error: " + messageStore.getAllMessages(SootLoggerLevel.ERROR).size());
		System.out.println("Warning: " + messageStore.getAllMessages(SootLoggerLevel.WARNING).size());
		System.out.println("Security Checker: " + messageStore.getAllMessages(SootLoggerLevel.SECURITYCHECKER).size());
		System.out.println("Information: " + messageStore.getAllMessages(SootLoggerLevel.INFORMATION).size());
	}
	
	private final boolean noMsg(List<Message> messages) {
		return messages.isEmpty();
	}
	
	private final boolean oneMsg(List<Message> messages) {
		return messages.size() == 1;
	}
	
	private final boolean twoMsg(List<Message> messages) {
		return messages.size() == 2;
	}
	
	private final boolean threeMsg(List<Message> messages) {
		return messages.size() == 3;
	}
	
	/**
	 * Main
	 */
		
	@Test
	public final void securityMessagesMain() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(MAIN, SootLoggerLevel.SECURITY)));
	}
	
	@Test
	public final void sideEffectMessagesMain() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(MAIN, SootLoggerLevel.SIDEEFFECT)));
	}
	
	@Test
	public final void exceptionMessagesMain() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(MAIN, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesMain() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(MAIN, SootLoggerLevel.ERROR)));
	}
	
	@Ignore
	public final void warningMessagesMain() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(MAIN, SootLoggerLevel.WARNING)));
	}
	
	/**
	 * Array
	 */
	
	@Test
	public final void securityMessagesArray() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		List<ExpectedError> listThreeError = new ArrayList<ExpectedError>();
		
		listTwoError.add(new ExpectedError(classStart + 8, "Assignment of value with high security level value to low security level array and assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 18, "Assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 19, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 29, "Expected: low return level / Result: high return level (at line %d)"));
		listTwoError.add(new ExpectedError(classStart + 38, "Assignment of value with high security level value to low security level array and assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 48, "Assignment restriction of values with low security level to arrays (at line %d)"));
		listTwoError.add(new ExpectedError(classStart + 58, "Assignment of value with high security level value to low security level array and assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 68, "Assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 69, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 79, "Expected: low return level / Result: high return level (at line %d)"));
		listTwoError.add(new ExpectedError(classStart + 88, "Assignment of value with high security level value to low security level array and assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 98, "Assignment restriction of values with low security level to arrays (at line %d)"));
		listTwoError.add(new ExpectedError(classStart + 108, "Assignment of value with high security level value to low security level array and assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 118, "Assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 119, "Expected: low return level / Result: high return level [length of high level array] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 129, "Expected: low return level / Result: high return level [length of high level array] (at line %d)"));
		listTwoError.add(new ExpectedError(classStart + 138, "Assignment of value with high security level value to low security level array and assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 148, "Assignment restriction of values with low security level to arrays (at line %d)"));
		listThreeError.add(new ExpectedError(classStart + 159, "Assignment of value with high security level value to low security level array, assignment restriction of values with low security level to arrays and stronger index level than the array level (at line %d)"));
		listTwoError.add(new ExpectedError(classStart + 170, "Assignment of value with high security level value to low security level array and assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 181, "Assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 182, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 192, "Assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 193, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 204, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 215, "Expected: low return level / Result: high return level (at line %d)"));
		listThreeError.add(new ExpectedError(classStart + 225, "Assignment of value with high security level value to low security level array, assignment restriction of values with low security level to arrays and stronger index level than the array level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 226, "Expected: low return level / Result: high return level because of high index (at line %d)"));
		listTwoError.add(new ExpectedError(classStart + 236, "Assignment of value with high security level value to low security level array and assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 247, "Assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 258, "Assignment restriction of values with low security level to arrays (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 269, "Stronger index level than the array level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 270, "Expected: low return level / Result: high return level because of high index (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 280, "Stronger index level than the array level (at line %d)"));
		
		
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(ARRAY, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(ARRAY, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		for (ExpectedError expectedError : listThreeError) {
			List<Message> messages = messageStore.getAllMessages(ARRAY, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, threeMsg(messages));
			count += 3;
		}
		
		List<Message> all = messageStore.getAllMessages(ARRAY, SootLoggerLevel.SECURITY);
		assertTrue("Only 46 messages expected.", all.size() == 46 && count == 46);
		
		overAllCount += count;
	}
	
	@Test
	public final void sideEffectMessagesArray() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ARRAY, SootLoggerLevel.SIDEEFFECT)));
	}
	
	@Test
	public final void exceptionMessagesArray() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ARRAY, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesArray() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ARRAY, SootLoggerLevel.ERROR)));
	}
	
	/**
	 * Field
	 */
	
	@Test
	public final void securityMessagesField() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 6, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 14, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 22, "Assignment of high level to field with low level (at line %d)"));

		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(FIELD, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(FIELD, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(FIELD, SootLoggerLevel.SECURITY);
		assertTrue("Only 3 messages expected.", all.size() == 3 && count == 3);
		
		overAllCount += count;
	}
	
	@Test
	public final void sideEffectMessagesField() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(FIELD, SootLoggerLevel.SIDEEFFECT)));
	}
	
	@Test
	public final void exceptionMessagesField() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(FIELD, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesField() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(FIELD, SootLoggerLevel.ERROR)));
	}
	
	/**
	 * Expression
	 */
	
	@Test
	public final void securityMessagesExpr() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 8, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 17, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 26, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 35, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 43, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 52, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 61, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 70, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 79, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 88, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 97, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 106, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 115, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 124, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 133, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 142, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 151, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 160, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 169, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 178, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 187, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 196, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 205, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 214, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 223, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 232, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 241, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 250, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 259, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 268, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 277, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 286, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 295, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 304, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 313, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 322, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 331, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 340, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 349, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 358, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 367, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 376, "Expected: low return level / Result: high return level (at line %d)"));
		
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(EXPR, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(EXPR, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(EXPR, SootLoggerLevel.SECURITY);
		assertTrue("Only 42 messages expected.", all.size() == 42 && count == 42);
		
		overAllCount += count;
	}
	
	@Test
	public final void sideEffectMessagesExpr() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(EXPR, SootLoggerLevel.SIDEEFFECT)));
	}
	
	@Test
	public final void exceptionMessagesExpr() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(EXPR, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesExpr() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(EXPR, SootLoggerLevel.ERROR)));
	}
	
	/**
	 * ID Method
	 */
	
	@Test
	public final void securityMessagesIdMethods() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 7, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 15, "Expected: low or weaker argument level / Result: high argument level (at line %d)"));		

		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(ID_METHODS, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(ID_METHODS, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(ID_METHODS, SootLoggerLevel.SECURITY);
		assertTrue("Only 2 messages expected.", all.size() == 2 && count == 2);
		
		overAllCount += count;
	}
	
	@Test
	public final void sideEffectMessagesIdMethods() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ID_METHODS, SootLoggerLevel.SIDEEFFECT)));
	}
	
	@Test
	public final void exceptionMessagesIdMethods() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ID_METHODS, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesIdMethods() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ID_METHODS, SootLoggerLevel.ERROR)));
	}
	
	/**
	 * Loop
	 */
		
	@Test
	public final void securityMessagesLoop() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 7, "Constant high cause of PC -> invalid call of id function (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 16, "Assignment of value with high security level value to low security level field (at line %d)."));
		listTwoError.add(new ExpectedError(classStart + 25, "Constant high cause of PC -> invalid call of id function & assignment to low (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 34, "Assignment of value with high security level value to low security level field (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 44, "Constant high cause of PC -> invalid call of id function (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 55, "Constant high cause of PC -> invalid call of id function (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 68, "Expected: low return level / Result: high return level (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 77, "Constant high cause of PC -> invalid call of id function (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 79, "Expected: low return level / Result: high return level (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 90, "Expected: low return level / Result: high return level [assignment] (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 99, "Constant high cause of PC -> invalid call of id function (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 101, "Expected: low return level / Result: high return level [pc] (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 112, "Expected: low return level / Result: high return level (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 123, "Expected: low return level / Result: high return level (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 134, "Expected: low return level / Result: high return level [assignment] (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 143, "Assignment of value with high security level value to low security level field (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 153, "Assignment of value with high security level value to low security level field [pc] (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 163, "Assignment of value with high security level value to low security level field (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 173, "Assignment of value with high security level value to low security level field [pc] (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 183, "Assignment of value with high security level value to low security level field (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 193, "Assignment of value with high security level value to low security level field (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 205, "Expected: low return level / Result: high return level (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 216, "Expected: low return level / Result: high return level (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 227, "Expected: low return level / Result: high return level [pc] (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 238, "Expected: low return level / Result: high return level [pc] (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 249, "Expected: low return level / Result: high return level (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 260, "Expected: low return level / Result: high return level (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 269, "Constant high cause of PC -> invalid call of id function (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 280, "Assignment of value with high security level value to low security level field (at line %d)."));
		listTwoError.add(new ExpectedError(classStart + 291, "Constant high cause of PC -> invalid call of id function & assignment to low (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 302, "Constant high cause of PC -> invalid call of id function (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 313, "Assignment of value with high security level value to low security level field (at line %d)."));
		listTwoError.add(new ExpectedError(classStart + 324, "Constant high cause of PC -> invalid call of id function & assignment to low (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 335, "Assignment of value with high security level value to low security level field (at line %d)."));
		listTwoError.add(new ExpectedError(classStart + 346, "Constant high cause of PC -> invalid call of id function & assignment to low (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 357, "Constant high cause of PC -> invalid call of id function (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 368, "Assignment of value with high security level value to low security level field (at line %d)."));
		
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(LOOP, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(LOOP, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(LOOP, SootLoggerLevel.SECURITY);
		assertTrue("Only 41 messages expected.", all.size() == 41 && count == 41);
		
		overAllCount += count;
	}
	
	@Test
	public final void sideEffectMessagesLoop() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 16, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 25, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 143, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 153, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 163, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 173, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 183, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 193, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 280, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 291, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 313, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 324, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 335, "Write effect to low inside of a high branch (at line %d)."));
		listOneError.add(new ExpectedError(classStart + 346, "Write effect to low inside of a high branch (at line %d)."));
		
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(LOOP, expectedError.lineNo, SootLoggerLevel.SIDEEFFECT);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(LOOP, expectedError.lineNo, SootLoggerLevel.SIDEEFFECT);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(LOOP, SootLoggerLevel.SIDEEFFECT);
		assertTrue("Only 14 messages expected.", all.size() == 14 && count == 14);
		
		overAllCount += count;
	}
	
	@Test
	public final void exceptionMessagesLoop() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(LOOP, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesLoop() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(LOOP, SootLoggerLevel.ERROR)));
	}
	
	/**
	 * If-Else
	 */
	
	@Test
	public final void securityMessagesIfElse() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 9, "Expected: low or weaker return level / Result: high return level inside of if-expression and high pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 19, "Expected: low or weaker return level / Result: high return level inside of if-expression (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 29, "Expected: low or weaker return level / Result: high return level inside of if-expression and high pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 39, "Expected: low or weaker return level / Result: high return level inside of if-expression (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 49, "Expected: low or weaker return level / Result: high return level inside of if-expression and high pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 59, "Expected: low or weaker return level / Result: high return level inside of if-expression (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 69, "Expected: low or weaker return level / Result: high return level because of high pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 79, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 89, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 99, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 109, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 119, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 129, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 139, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 149, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 160, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 162, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 173, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 175, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 186, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 188, "Expected: low or weaker return level / Result: high return level because of pc [if contains return] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 199, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 212, "Expected: low or weaker return level / Result: high return level because of pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 214, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 227, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 238, "Expected: low or weaker return level / Result: high return level because of pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 240, "Expected: low or weaker return level / Result: high return level because of pc [if contains return] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 251, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 253, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 264, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 266, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 277, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 279, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 290, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 292, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 303, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 305, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 316, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 318, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 329, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 331, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 342, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 344, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 355, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 357, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 369, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 371, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 383, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 385, "Expected: low or weaker return level / Result: high return level because of pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 397, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 411, "Expected: low or weaker return level / Result: high return level because og pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 413, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 427, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 439, "Expected: low or weaker return level / Result: high return level because of pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 441, "Expected: low or weaker return level / Result: high return level because of pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 453, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 455, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 467, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 469, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 481, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 483, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 495, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 497, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 509, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 511, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 523, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 525, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 537, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 539, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 551, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 553, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 570, "Expected: low or weaker return level / Result: high return level [assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 586, "Expected: low or weaker return level / Result: high return level [assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 602, "Expected: low or weaker return level / Result: high return level [assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 618, "Expected: low or weaker return level / Result: high return level [assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 634, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 650, "Expected: low or weaker return level / Result: high return level [possible assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 666, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 682, "Expected: low or weaker return level / Result: high return level [possible assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 698, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 714, "Expected: low or weaker return level / Result: high return level [possible assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 730, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 746, "Expected: low or weaker return level / Result: high return level [possible assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 762, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 778, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 789, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 791, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 804, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 806, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 819, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 821, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 834, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 849, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 851, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 866, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 879, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 881, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 898, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 913, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 924, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 926, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 939, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 941, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 958, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 973, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 984, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 986, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 999, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1018, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1033, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1044, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1046, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1061, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1078, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1093, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1104, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1106, "Assignment of high security level value to low level field [pc] (at line %d)"));
		
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(IF_ELSE, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(IF_ELSE, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.SECURITY);
		assertTrue("Only 117 messages expected.", all.size() == 117 && count == 117);
		
		overAllCount += count;
	}
	
	@Test
	public final void sideEffectMessagesIfElse() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 789, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 791, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 819, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 821, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 849, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 851, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 879, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 881, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 924, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 926, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 984, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 986, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1044, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1046, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1104, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1106, "Weaker write effect than the branch pc (at line %d)"));
		
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(IF_ELSE, expectedError.lineNo, SootLoggerLevel.SIDEEFFECT);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(IF_ELSE, expectedError.lineNo, SootLoggerLevel.SIDEEFFECT);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.SIDEEFFECT);
		assertTrue("Only 16 messages expected.", all.size() == 16 && count == 16);	
		
		overAllCount += count;
	}
	
	@Test
	public final void exceptionMessagesIfElse() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesIfElse() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.ERROR)));
	}
	
	/**
	 * If-Else 2
	 */
	
	@Test
	public final void securityMessagesIfElse2() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		listOneError.add(new ExpectedError(classStart + 9, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 21, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 33, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 47, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 57, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 71, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 81, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 95, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 105, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 119, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 131, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 143, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 155, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 167, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 179, "Expected: low or weaker return level / Result: high return level [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 191, "Expected: low or weaker return level / Result: high return level [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 203, "Expected: low or weaker return level / Result: high return level [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 215, "Expected: low or weaker return level / Result: high return level [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 227, "Expected: low or weaker return level / Result: high return level [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 239, "Expected: low or weaker return level / Result: high return level [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 251, "Expected: low or weaker return level / Result: high return level [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 263, "Expected: low or weaker return level / Result: high return level [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 275, "Expected: low or weaker return level / Result: high return level [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 287, "Expected: low or weaker return level / Result: high return level [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 303, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 319, "Assignment of high security level value to low level field [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 335, "Assignment of high security level value to low level field [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 351, "Assignment of high security level value to low level field [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 367, "Assignment of high security level value to low level field [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 383, "Assignment of high security level value to low level field [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 399, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 415, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 431, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 447, "Assignment of high security level value to low level field [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 463, "Assignment of high security level value to low level field [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 479, "Assignment of high security level value to low level field [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 495, "Assignment of high security level value to low level field [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 511, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 522, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 526, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 538, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 542, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 554, "Assignment of high security level value to low level field [pc](at line %d)"));
		listOneError.add(new ExpectedError(classStart + 558, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 570, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 574, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 586, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 590, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 602, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 618, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 622, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 634, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 650, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 654, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 666, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 670, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 686, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 702, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 714, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 718, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 730, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 750, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 761, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 764, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 774, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 787, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 790, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 800, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 813, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 816, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 826, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 839, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 842, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 852, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 855, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 858, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 868, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 871, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 884, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 887, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 890, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 900, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 903, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 916, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 919, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 922, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 932, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 935, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 948, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 951, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 954, "Assignment of high security level value to low level field (at line %d)"));
				
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(IF_ELSE_2, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(IF_ELSE_2, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}	
		
		List<Message> all = messageStore.getAllMessages(IF_ELSE_2, SootLoggerLevel.SECURITY);
		assertTrue("Only 91 messages expected.", all.size() == 91 && count == 91);
		
		overAllCount += count;
	}
	
	@Test
	public final void sideEffectMessagesIfElse2() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 9, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 33, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 57, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 105, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 522, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 526, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 538, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 542, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 554, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 558, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 570, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 574, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 586, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 602, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 618, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 634, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 654, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 670, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 686, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 702, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 761, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 774, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 787, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 800, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 813, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 826, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 852, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 855, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 868, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 871, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 884, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 887, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 900, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 903, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 916, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 919, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 932, "Weaker write effect than the branch pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 935, "Weaker write effect than the branch pc (at line %d)"));
		
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(IF_ELSE_2, expectedError.lineNo, SootLoggerLevel.SIDEEFFECT);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(IF_ELSE_2, expectedError.lineNo, SootLoggerLevel.SIDEEFFECT);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(IF_ELSE_2, SootLoggerLevel.SIDEEFFECT);
		assertTrue("Only 38 messages expected.", all.size() == 38 && count == 38);	
		
		overAllCount += count;
	}
	
	@Test
	public final void exceptionMessagesIfElse2() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(IF_ELSE_2, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesIfElse2() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(IF_ELSE_2, SootLoggerLevel.ERROR)));
	}
	
	/**
	 * Method
	 */
	
	@Test
	public final void securityMessagesMethod() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 6, "Expected: low return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 13, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 20, "Expected: low return level / Result: high return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 34, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 48, "Expected: low return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 55, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 63, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 79, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 95, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 103, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 110, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 126, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 136, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listTwoError.add(new ExpectedError(classStart + 146, "Invoked Method expects: 2x low argument level, but 2x high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 163, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 173, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 190, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 200, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 211, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 220, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 221, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 230, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 231, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 248, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(METHOD, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(METHOD, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(METHOD, SootLoggerLevel.SECURITY);
		assertTrue("Only 25 messages expected.", all.size() == 25 && count == 25);
		
		overAllCount += count;
	}
	
	@Test
	public final void sideEffectMessagesMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(METHOD, SootLoggerLevel.SIDEEFFECT)));
	}
	
	@Test
	public final void exceptionMessagesMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(METHOD, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(METHOD, SootLoggerLevel.ERROR)));
	}
	
	/**
	 * Object
	 */
	
	@Test
	public final void securityMessagesObject() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 8, "Expected: low return level / Result: high return because of object level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 17, "Expected: low return level / Result: high return because of field level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 26, "Expected: low return level / Result: high return because of field level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 33, "Expected: low return level / Result: high return because of field level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 42, "Assignment of high level to field with low level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 53, "Assignment of high level to field with low level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 62, "Assignment of high level to field with low level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 72, "Expected: low return level / Result: high return because of invocation base object level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 80, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 89, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 98, "Expected: low return level / Result: high return because of invocation base object and return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 108, "Expected: low return level / Result: high return because of invocation base object level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 117, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 126, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 136, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 146, "Expected: low return level / Result: high return because of invocation base object and return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 156, "Expected: low return level / Result: high return because of invocation base object and return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 165, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 166, "Expected: low return level / Result: high return because of invocation base object (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 175, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 183, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 190, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 198, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 207, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(OBJECT, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(OBJECT, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(OBJECT, SootLoggerLevel.SECURITY);
		assertTrue("Only 24 messages expected.", all.size() == 24 && count == 24);
		
		overAllCount += count;
	}
	
	@Test
	public final void sideEffectMessagesObject() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(OBJECT, SootLoggerLevel.SIDEEFFECT)));
	}
	
	@Test
	public final void exceptionMessagesObject() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(OBJECT, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesObject() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(OBJECT, SootLoggerLevel.ERROR)));
	}
	
	/**
	 * Static Field
	 */
	
	@Test
	public final void securityMessagesStaticField() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 6, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 14, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 22, "Assignment of high level to field with low level (at line %d)"));
		
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(STATIC_FIELD, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(STATIC_FIELD, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(STATIC_FIELD, SootLoggerLevel.SECURITY);
		assertTrue("Only 3 messages expected.", all.size() == 3 && count == 3);	
		
		overAllCount += count;
	}
	
	@Test
	public final void sideEffectMessagesStaticField() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_FIELD, SootLoggerLevel.SIDEEFFECT)));
	}
	
	@Test
	public final void exceptionMessagesStaticField() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_FIELD, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesStaticField() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_FIELD, SootLoggerLevel.ERROR)));
	}
	
	/**
	 * Static Method
	 */
	
	@Test
	public final void securityMessagesStaticMethod() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 6, "Expected: low return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 13, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 20, "Expected: low return level / Result: high return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 34, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 48, "Expected: low return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 55, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 63, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 79, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 95, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 103, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 110, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 126, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 136, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));		
		listTwoError.add(new ExpectedError(classStart + 146, "Invoked Method expects: 2x low argument level, but 2x high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 163, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 173, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 190, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 200, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 211, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 220, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 221, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 230, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 231, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 248, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		
		for (ExpectedError expectedError : listOneError) {
			List<Message> messages = messageStore.getAllMessages(STATIC_METHOD, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, oneMsg(messages));
			count++;
		}		
		for (ExpectedError expectedError : listTwoError) {
			List<Message> messages = messageStore.getAllMessages(STATIC_METHOD, expectedError.lineNo, SootLoggerLevel.SECURITY);
			assertTrue(expectedError.errorMsg, twoMsg(messages));
			count += 2;
		}
		
		List<Message> all = messageStore.getAllMessages(STATIC_METHOD, SootLoggerLevel.SECURITY);
		assertTrue("Only 25 messages expected.", all.size() == 25 && count == 25);
		
		overAllCount += count;
	}
	
	@Test
	public final void sideEffectMessagesStaticMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_METHOD, SootLoggerLevel.SIDEEFFECT)));
	}
	
	@Test
	public final void exceptionMessagesStaticMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_METHOD, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesStaticMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_METHOD, SootLoggerLevel.ERROR)));
	}
	
	/**
	 * Complete check
	 */
	
	@Test 
	public final void securityCompleteCheck() {
		List<Message> all = messageStore.getAllMessages(SootLoggerLevel.SECURITY);
		assertTrue("Only 419 messages expected.", all.size() == 419);
	}
	
	@Test 
	public final void sideEffectCheckerCompleteCheck() {
		List<Message> all = messageStore.getAllMessages(SootLoggerLevel.SIDEEFFECT);
		assertTrue("Only 68 messages expected.", all.size() == 68); 
	}
	
	@Test 
	public final void exceptionCompleteCheck() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(SootLoggerLevel.EXCEPTION)));
	}
	
	@Test 
	public final void errorCompleteCheck() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(SootLoggerLevel.ERROR)));
	}
	
	@Test 
	public final void securityCheckerCompleteCheck() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(SootLoggerLevel.SECURITYCHECKER)));
	}
	
	@AfterClass
	public static final void end() {
//		List<Message> all = messageStore.getAllMessages(LOOP, SootLoggerLevel.SIDEEFFECT);
////		StringBuilder builder = new StringBuilder();
//		for (Message message : all) {
//			if (message.getSrcLn() >= 0) {
//				System.out.println(message.getFileName() + " > " + (message.getSrcLn() - 7) + ": " + message.getMessage());
//			}
////			builder.append(((!builder.toString().equals("")) ? "," : "") + message.getSrcLn());
//			
//		}
////		System.out.println(builder.toString());
//		
//		//assertTrue("Only 358 messages expected.", overAllCount == 358);	// ohne ??? 352
	}
}
