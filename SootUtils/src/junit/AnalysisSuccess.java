package junit;

import static org.junit.Assert.*;

import java.util.List;

import logging.SootLoggerUtils;
import logging.SootLoggerLevel;
import model.MessageStore;
import model.MessageStore.Message;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class AnalysisSuccess {
	
	private static final String MAIN = "TaintTrackingSuccess";
	private static final String ARRAY = "TaintTrackingArray";
	private static final String FIELD = "TaintTrackingField";
	private static final String EXPR = "TaintTrackingExpr";
	private static final String ID_METHODS = "TaintTrackingIdMethods";
	private static final String IF_ELSE = "TaintTrackingIfElse";
	private static final String METHOD = "TaintTrackingMethod";
	private static final String OBJECT = "TaintTrackingObject";
	private static final String LOOP = "TaintTrackingLoop";
	private static final String STATIC_FIELD = "TaintTrackingStaticField";
	private static final String STATIC_METHOD = "TaintTrackingStaticMethod";
	
	private static MessageStore messageStore;

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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ARRAY, SootLoggerLevel.SECURITY)));
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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(FIELD, SootLoggerLevel.SECURITY)));
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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(EXPR, SootLoggerLevel.SECURITY)));
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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ID_METHODS, SootLoggerLevel.SECURITY)));
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
	 * If-Else
	 */
	
	@Test
	public final void securityMessagesIfElse() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.SECURITY)));
	}
	
	@Test
	public final void sideEffectMessagesIfElse() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.SIDEEFFECT)));
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
	 * Method
	 */
	
	@Test
	public final void securityMessagesMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(METHOD, SootLoggerLevel.SECURITY)));
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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(OBJECT, SootLoggerLevel.SECURITY)));
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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_FIELD, SootLoggerLevel.SECURITY)));
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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_METHOD, SootLoggerLevel.SECURITY)));
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
	 * Loop
	 */
	
	@Test
	public final void securityMessagesLoop() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(LOOP, SootLoggerLevel.SECURITY)));
	}
	
	@Test
	public final void sideEffectMessagesLoop() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(LOOP, SootLoggerLevel.SIDEEFFECT)));
	}
	
	@Test
	public final void exceptionMessagesLoop() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(LOOP, SootLoggerLevel.EXCEPTION)));
	}
	
	@Test
	public final void errorMessagesLoop() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(LOOP, SootLoggerLevel.ERROR)));
	}
	
	@Ignore
	public final void warningMessagesLoop() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(LOOP, SootLoggerLevel.WARNING)));
	}
	
	/**
	 * Complete check
	 */
	
	@Test 
	public final void securityCompleteCheck() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(SootLoggerLevel.SECURITY)));
	}
	
	@Test 
	public final void sideEffectCheckerCompleteCheck() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(SootLoggerLevel.SIDEEFFECT)));
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
//		List<Message> all = messageStore.getAllMessages(LOOP, SootLoggerLevel.SECURITY);
//////		StringBuilder builder = new StringBuilder();
//		for (Message message : all) {
//			if (message.getSrcLn() >= 0) {
//				System.out.println(message.getFileName() + " > " + (message.getSrcLn() - 7) + ": " + message.getMessage());
//			}
//////			builder.append(((!builder.toString().equals("")) ? "," : "") + message.getSrcLn());
//			
//		}
////		System.out.println(builder.toString());
//		
//		//assertTrue("Only 358 messages expected.", overAllCount == 358);	// ohne ??? 352
	}
	
}
