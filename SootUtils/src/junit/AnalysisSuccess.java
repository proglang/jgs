package junit;

import static org.junit.Assert.*;

import java.util.List;

import logging.SootLoggerConfiguration;
import logging.SootLoggerLevel;
import model.MessageStore;
import model.MessageStore.Message;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class AnalysisSuccess {
	
	private static final String MAIN = "TaintTrackingSuccess";
	private static final String FIELD = "TaintTrackingField";
	private static final String ID_METHODS = "TaintTrackingIdMethods";
	private static final String IF_ELSE = "TaintTrackingIfElse";
	private static final String METHOD = "TaintTrackingMethod";
	private static final String OBJECT = "TaintTrackingObject";
	private static final String STATIC_FIELD = "TaintTrackingStaticField";
	private static final String STATIC_METHOD = "TaintTrackingStaticMethod";
	
	private static MessageStore messageStore;

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
	
	@Ignore
	public final void warningMessagesMain() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(MAIN, SootLoggerLevel.WARNING)));
	}
	
	@Test
	public final void securityMessagesField() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(FIELD, SootLoggerLevel.SECURITY)));
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
	public final void securityMessagesIdMethods() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(ID_METHODS, SootLoggerLevel.SECURITY)));
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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.SECURITY)));
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
	public final void securityMessagesMethod() {
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(METHOD, SootLoggerLevel.SECURITY)));
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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(OBJECT, SootLoggerLevel.SECURITY)));
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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_FIELD, SootLoggerLevel.SECURITY)));
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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(STATIC_METHOD, SootLoggerLevel.SECURITY)));
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
		assertTrue("No messages expected.", noMsg(messageStore.getAllMessages(SootLoggerLevel.SECURITY)));
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
}
