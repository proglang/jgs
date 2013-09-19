package junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logging.SootLoggerConfiguration;
import logging.SootLoggerLevel;
import model.MessageStore;
import model.MessageStore.Message;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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
	private static final String IF_ELSE = "TaintTrackingIfElse";
	private static final String IF_ELSE_2 = "TaintTrackingIfElse2";
	private static final String METHOD = "TaintTrackingMethod";
	private static final String OBJECT = "TaintTrackingObject";
	private static final String STATIC_FIELD = "TaintTrackingStaticField";
	private static final String STATIC_METHOD = "TaintTrackingStaticMethod";
	
	private static MessageStore messageStore;
	private static int overAllCount = 0;

	@BeforeClass
	public static final void init() {
		try {
			messageStore = SootLoggerConfiguration.restoreSerializedMessageStore();
		} catch (NullPointerException e) {
			fail("Can't fetch the serialized message store.");
		}		
	}
	
	@AfterClass
	public static final void finish() {
		System.out.println("Message Report");
		System.out.println("Security: " + messageStore.getAllMessages(SootLoggerLevel.SECURITY).size());
		System.out.println("Exception: " + messageStore.getAllMessages(SootLoggerLevel.EXCEPTION).size());
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
		
	@Test
	public final void securityMessagesMain() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(MAIN, SootLoggerLevel.SECURITY)));
	}
	
	@Test
	public final void exceptionMessagesMain() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(MAIN, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesMain() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(MAIN, SootLoggerLevel.ERROR)));
	}
	
	@Test
	public final void securityMessagesArray() {
		int count = 0;
		int classStart = 6;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 7, "Assignment of value with high security level value to low security level array (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 17, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 26, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 34, "Assignment of value with high security level value to low security level array (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 43, "Assignment of value with high security level value to low security level array (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 53, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 62, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 70, "Assignment of value with high security level value to low security level array (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 79, "Assignment of value with high security level value to low security level array (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 89, "Expected: low return level / Result: high return level [length of high level array] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 98, "Expected: low return level / Result: high return level [length of high level array] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 106, "Assignment of value with high security level value to low security level array (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 116, "Assignment of value with high security level value to low security level array (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 126, "Assignment of value with high security level value to low security level array (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 137, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 147, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 157, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 167, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 176, "Assignment of value with high security level value to low security level array (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 186, "Assignment of value with high security level value to low security level array (at line %d)"));
	
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
		
		List<Message> all = messageStore.getAllMessages(ARRAY, SootLoggerLevel.SECURITY);
		assertTrue("Only 20 messages expected.", all.size() == 20 && count == 20);
		
		overAllCount += count;
	}
	
	@Test
	public final void exceptionMessagesArray() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ARRAY, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesArray() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ARRAY, SootLoggerLevel.ERROR)));
	}
	
	@Test
	public final void securityMessagesField() {
		int count = 0;
		int classStart = 6;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 11, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 18, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 25, "Assignment of high level to field with low level (at line %d)"));

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
	public final void exceptionMessagesField() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(FIELD, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesField() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(FIELD, SootLoggerLevel.ERROR)));
	}
	
	@Test
	public final void securityMessagesExpr() {
		int count = 0;
		int classStart = 6;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 7, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 15, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 23, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 31, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 39, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 47, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 55, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 63, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 71, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 79, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 87, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 95, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 103, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 111, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 119, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 127, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 135, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 143, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 151, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 159, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 167, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 175, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 183, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 191, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 199, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 207, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 215, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 223, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 231, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 239, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 247, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 255, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 263, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 271, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 279, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 287, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 295, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 303, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 311, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 319, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 327, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 335, "Expected: low return level / Result: high return level (at line %d)"));
		
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
	public final void exceptionMessagesExpr() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(EXPR, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesExpr() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(EXPR, SootLoggerLevel.ERROR)));
	}
	
	@Test
	public final void securityMessagesIdMethods() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 6, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 14, "Expected: low or weaker argument level / Result: high argument level (at line %d)"));		

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
	public final void exceptionMessagesIdMethods() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ID_METHODS, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesIdMethods() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ID_METHODS, SootLoggerLevel.ERROR)));
	}
	
	@Test
	public final void securityMessagesIfElse() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 8, "Expected: low or weaker return level / Result: high return level inside of if-expression and high pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 17, "Expected: low or weaker return level / Result: high return level inside of if-expression (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 26, "Expected: low or weaker return level / Result: high return level inside of if-expression and high pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 35, "Expected: low or weaker return level / Result: high return level inside of if-expression (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 44, "Expected: low or weaker return level / Result: high return level inside of if-expression and high pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 53, "Expected: low or weaker return level / Result: high return level inside of if-expression (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 62, "Expected: low or weaker return level / Result: high return level because of high pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 71, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 80, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 89, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 98, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 107, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 116, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 125, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 134, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 144, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 146, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 156, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 158, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 168, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 170, "Expected: low or weaker return level / Result: high return level because of pc [if contains return] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 180, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 192, "Expected: low or weaker return level / Result: high return level because of pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 194, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 206, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 216, "Expected: low or weaker return level / Result: high return level because of pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 218, "Expected: low or weaker return level / Result: high return level because of pc [if contains return] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 228, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 230, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 240, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 242, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 252, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 254, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 264, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 266, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 276, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 278, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 288, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 290, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 300, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 302, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 312, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 314, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 324, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 326, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 337, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 339, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 350, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 352, "Expected: low or weaker return level / Result: high return level because of pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 363, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 376, "Expected: low or weaker return level / Result: high return level because og pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 378, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 391, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 402, "Expected: low or weaker return level / Result: high return level because of pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 404, "Expected: low or weaker return level / Result: high return level because of pc (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 415, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 417, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 428, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 430, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 441, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 443, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 454, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 456, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 467, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 469, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 480, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 482, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 493, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 495, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 506, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 508, "Expected: void return / Result: non void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 524, "Expected: low or weaker return level / Result: high return level [assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 539, "Expected: low or weaker return level / Result: high return level [assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 554, "Expected: low or weaker return level / Result: high return level [assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 569, "Expected: low or weaker return level / Result: high return level [assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 584, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 599, "Expected: low or weaker return level / Result: high return level [possible assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 614, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 629, "Expected: low or weaker return level / Result: high return level [possible assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 644, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 659, "Expected: low or weaker return level / Result: high return level [possible assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 674, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 689, "Expected: low or weaker return level / Result: high return level [possible assign] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 704, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 719, "Expected: low or weaker return level / Result: high return level [assign, pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 729, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 731, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 743, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 745, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 757, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 759, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 771, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 785, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 787, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 801, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 813, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 815, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 831, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 845, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 855, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 857, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 869, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 871, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 887, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 901, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 911, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 913, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 925, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 943, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 957, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 967, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 969, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 983, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 999, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1013, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1023, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 1025, "Assignment of high security level value to low level field [pc] (at line %d)"));
		
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
	public final void exceptionMessagesIfElse() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesIfElse() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.ERROR)));
	}
	
	@Test
	public final void securityMessagesIfElse2() {
		int count = 0;
		int classStart = 7;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		listOneError.add(new ExpectedError(classStart + 8, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 19, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 30, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 43, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 52, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 65, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 74, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 87, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 96, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 109, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 120, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 131, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 142, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 153, "Expected: low or weaker return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 164, "Expected: low or weaker return level / Result: high return level [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 175, "Expected: low or weaker return level / Result: high return level [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 186, "Expected: low or weaker return level / Result: high return level [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 197, "Expected: low or weaker return level / Result: high return level [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 208, "Expected: low or weaker return level / Result: high return level [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 219, "Expected: low or weaker return level / Result: high return level [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 230, "Expected: low or weaker return level / Result: high return level [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 241, "Expected: low or weaker return level / Result: high return level [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 252, "Expected: low or weaker return level / Result: high return level [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 263, "Expected: low or weaker return level / Result: high return level [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 278, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 293, "Assignment of high security level value to low level field [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 308, "Assignment of high security level value to low level field [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 323, "Assignment of high security level value to low level field [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 338, "Assignment of high security level value to low level field [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 353, "Assignment of high security level value to low level field [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 368, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 383, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 398, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 413, "Assignment of high security level value to low level field [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 428, "Assignment of high security level value to low level field [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 443, "Assignment of high security level value to low level field [possible pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 458, "Assignment of high security level value to low level field [possible] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 473, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 483, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 487, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 498, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 502, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 513, "Assignment of high security level value to low level field [pc](at line %d)"));
		listOneError.add(new ExpectedError(classStart + 517, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 528, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 532, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 543, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 547, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 558, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 573, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 577, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 588, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 603, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 607, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 618, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 622, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 637, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 652, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 663, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 667, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 678, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 697, "Assignment of high security level value to low level field (at line %d)"));
		
		listOneError.add(new ExpectedError(classStart + 707, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 710, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 719, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 722, "(at line %d)")); // ???
		listOneError.add(new ExpectedError(classStart + 731, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 734, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 743, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 746, "(at line %d)")); // ???
		listOneError.add(new ExpectedError(classStart + 755, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 758, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 767, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 770, "(at line %d)")); // ???
		listOneError.add(new ExpectedError(classStart + 779, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 782, "Assignment of high security level value to low level field (at line %d)"));
		
		listOneError.add(new ExpectedError(classStart + 791, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 794, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 797, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 806, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 809, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 812, "(at line %d)")); // ???
		listOneError.add(new ExpectedError(classStart + 821, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 824, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 827, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 836, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 839, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 842, "(at line %d)")); // ???
		listOneError.add(new ExpectedError(classStart + 851, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 854, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 857, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 866, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 869, "Assignment of high security level value to low level field [pc] (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 872, "(at line %d)")); // ???
		listOneError.add(new ExpectedError(classStart + 881, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 884, "Assignment of high security level value to low level field (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 887, "Assignment of high security level value to low level field (at line %d)"));
		
		
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
		assertTrue("Only 97 messages expected.", all.size() == 97 && count == 97); // ohne ?? 91
		
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
	
	@Test
	public final void securityMessagesMethod() {
		int count = 0;
		int classStart = 6;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 5, "Expected: low return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 11, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 17, "Expected: low return level / Result: high return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 29, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 41, "Expected: low return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 47, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 54, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 68, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 82, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 89, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 95, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 109, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 118, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listTwoError.add(new ExpectedError(classStart + 127, "Invoked Method expects: 2x low argument level, but 2x high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 142, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 151, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 166, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 175, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 185, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 193, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 194, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 202, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 203, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 218, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		
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
	public final void exceptionMessagesMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(METHOD, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(METHOD, SootLoggerLevel.ERROR)));
	}
	
	@Test
	public final void securityMessagesObject() {
		int count = 0;
		int classStart = 6;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 27, "Expected: low return level / Result: high return because of object level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 35, "Expected: low return level / Result: high return because of field level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 43, "Expected: low return level / Result: high return because of field level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 49, "Expected: low return level / Result: high return because of field level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 57, "Assignment of high level to field with low level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 67, "Assignment of high level to field with low level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 75, "Assignment of high level to field with low level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 111, "Expected: low return level / Result: high return because of invocation base object level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 118, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 126, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 134, "Expected: low return level / Result: high return because of invocation base object and return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 143, "Expected: low return level / Result: high return because of invocation base object level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 151, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 159, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 168, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 177, "Expected: low return level / Result: high return because of invocation base object and return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 186, "Expected: low return level / Result: high return because of invocation base object and return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 194, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 195, "Expected: low return level / Result: high return because of invocation base object (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 203, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 237, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 243, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 250, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 258, "Expected: low return level / Result: high return because of invocation return level (at line %d)"));
		
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
	public final void exceptionMessagesObject() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(OBJECT, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesObject() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(OBJECT, SootLoggerLevel.ERROR)));
	}
	
	@Test
	public final void securityMessagesStaticField() {
		int count = 0;
		int classStart = 6;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 11, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 18, "Expected: low return level / Result: high return level (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 25, "Assignment of high level to field with low level (at line %d)"));
		
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
	public final void exceptionMessagesStaticField() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_FIELD, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesStaticField() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_FIELD, SootLoggerLevel.ERROR)));
	}
	
	@Test
	public final void securityMessagesStaticMethod() {
		int count = 0;
		int classStart = 8;
		List<ExpectedError> listOneError = new ArrayList<ExpectedError>();
		List<ExpectedError> listTwoError = new ArrayList<ExpectedError>();
		
		listOneError.add(new ExpectedError(classStart + 5, "Expected: low return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 11, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 17, "Expected: low return level / Result: high return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 29, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 41, "Expected: low return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 47, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 54, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 68, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 82, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 89, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 95, "Expected: high return level / Result: void return (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 109, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 118, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));		
		listTwoError.add(new ExpectedError(classStart + 127, "Invoked Method expects: 2x low argument level, but 2x high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 142, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 151, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 166, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 175, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 185, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 193, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 194, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 202, "Invoked Method expects: low argument level, but high argument level given (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 203, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		listOneError.add(new ExpectedError(classStart + 218, "Expected: low return level / Result: high return because of invoke (at line %d)"));
		
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
	public final void exceptionMessagesStaticMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_METHOD, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesStaticMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_METHOD, SootLoggerLevel.ERROR)));
	}
	
	@Test 
	public final void securityCompleteCheck() {
		List<Message> all = messageStore.getAllMessages(SootLoggerLevel.SECURITY);
		assertTrue("Only 358 messages expected.", all.size() == 358); // ohne ??? 352
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
		List<Message> all = messageStore.getAllMessages(IF_ELSE_2, SootLoggerLevel.SECURITY);
		StringBuilder builder = new StringBuilder();
		for (Message message : all) {
			if (message.getSourceLine() >= 708) {
				System.out.println("> " + message.getSourceLine() + ": " + message.getMessage());
			}
			builder.append(((!builder.toString().equals("")) ? "," : "") + message.getSourceLine());
			
		}
		System.out.println(builder.toString());
		
		assertTrue("Only 358 messages expected.", overAllCount == 358);	// ohne ??? 352
	}
}
