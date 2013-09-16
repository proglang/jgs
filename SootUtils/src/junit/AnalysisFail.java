package junit;

import static org.junit.Assert.*;

import java.util.List;

import logging.SootLoggerConfiguration;
import logging.SootLoggerLevel;
import model.MessageStore;
import model.MessageStore.Message;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AnalysisFail {
	
	private static final String MAIN = "TaintTrackingFail";
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
	public final void securityMessagesField() {
		List<Message> line17 = messageStore.getAllMessages(FIELD, 17, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 17)", oneMsg(line17));
		
		List<Message> line24 = messageStore.getAllMessages(FIELD, 24, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 24)", oneMsg(line24));
		
		List<Message> line31 = messageStore.getAllMessages(FIELD, 31, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high level to field with low level (at line 31)", oneMsg(line31));
		
		List<Message> all = messageStore.getAllMessages(FIELD, SootLoggerLevel.SECURITY);
		assertTrue("Only 3 messages expected.", all.size() == 3);
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
		List<Message> line13 = messageStore.getAllMessages(ID_METHODS, 13, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 13)", oneMsg(line13));
		
		List<Message> line21 = messageStore.getAllMessages(ID_METHODS, 21, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker argument level / Result: high argument level (at line 21)", oneMsg(line21));
		
		List<Message> all = messageStore.getAllMessages(ID_METHODS, SootLoggerLevel.SECURITY);
		assertTrue("Only 2 messages expected.", all.size() == 2);
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
		List<Message> line14 = messageStore.getAllMessages(IF_ELSE, 14, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of if-expression (at line 14)", oneMsg(line14));
		
		List<Message> line23 = messageStore.getAllMessages(IF_ELSE, 23, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of if-expression (at line 23)", oneMsg(line23));
		
		List<Message> line32 = messageStore.getAllMessages(IF_ELSE, 32, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of high condition (at line 32)", oneMsg(line32));
		
		List<Message> line41 = messageStore.getAllMessages(IF_ELSE, 41, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of high condition and if-expression (at line 41)", oneMsg(line41));
		
		List<Message> line51 = messageStore.getAllMessages(IF_ELSE, 51, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of pc (at line 51)", oneMsg(line51));
		
		List<Message> line63 = messageStore.getAllMessages(IF_ELSE, 63, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of pc (at line 63)", oneMsg(line63));
		
		List<Message> line65 = messageStore.getAllMessages(IF_ELSE, 65, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 65)", oneMsg(line65));
		
		List<Message> line75 = messageStore.getAllMessages(IF_ELSE, 75, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 75)", oneMsg(line75));
		
		List<Message> line89 = messageStore.getAllMessages(IF_ELSE, 89, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 89)", oneMsg(line89));
		
		List<Message> line99 = messageStore.getAllMessages(IF_ELSE, 99, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 99)", oneMsg(line99));
		
		List<Message> all = messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.SECURITY);
		assertTrue("Only 10 messages expected.", all.size() == 10);
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
		List<Message> line11 = messageStore.getAllMessages(METHOD, 11, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: void return (at line 11)", oneMsg(line11));
		
		List<Message> line17 = messageStore.getAllMessages(METHOD, 17, SootLoggerLevel.SECURITY);
		assertTrue("Expected: high return level / Result: void return (at line 17)", oneMsg(line17));
		
		List<Message> line23 = messageStore.getAllMessages(METHOD, 23, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return (at line 23)", oneMsg(line23));
		
		List<Message> line35 = messageStore.getAllMessages(METHOD, 35, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invoke (at line 35)", oneMsg(line35));
		
		List<Message> line47 = messageStore.getAllMessages(METHOD, 47, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: void return (at line 47)", oneMsg(line47));
		
		List<Message> line53 = messageStore.getAllMessages(METHOD, 53, SootLoggerLevel.SECURITY);
		assertTrue("Expected: high return level / Result: void return (at line 53)", oneMsg(line53));
		
		List<Message> line60 = messageStore.getAllMessages(METHOD, 60, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 60)", oneMsg(line60));
		
		List<Message> line74 = messageStore.getAllMessages(METHOD, 74, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 74)", oneMsg(line74));
		
		List<Message> line88 = messageStore.getAllMessages(METHOD, 88, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 88)", oneMsg(line88));
		
		List<Message> line95 = messageStore.getAllMessages(METHOD, 95, SootLoggerLevel.SECURITY);
		assertTrue("Expected: high return level / Result: void return (at line 95)", oneMsg(line95));
		
		List<Message> line101 = messageStore.getAllMessages(METHOD, 101, SootLoggerLevel.SECURITY);
		assertTrue("Expected: high return level / Result: void return (at line 101)", oneMsg(line101));
		
		List<Message> line115 = messageStore.getAllMessages(METHOD, 115, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 115)", oneMsg(line115));
		
		List<Message> line124 = messageStore.getAllMessages(METHOD, 124, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 124)", oneMsg(line124));
		
		// 2x -> same source line
		List<Message> line133 = messageStore.getAllMessages(METHOD, 133, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: 2x low argument level, but 2x high argument level given (at line 133)", twoMsg(line133));
		
		List<Message> line148 = messageStore.getAllMessages(METHOD, 148, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 148)", oneMsg(line148));
		
		List<Message> line157 = messageStore.getAllMessages(METHOD, 157, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 157)", oneMsg(line157));
		
		List<Message> line172 = messageStore.getAllMessages(METHOD, 172, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 172)", oneMsg(line172));
		
		List<Message> line181 = messageStore.getAllMessages(METHOD, 181, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 181)", oneMsg(line181));
		
		List<Message> line191 = messageStore.getAllMessages(METHOD, 191, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invoke (at line 191)", oneMsg(line191));
		
		List<Message> line199 = messageStore.getAllMessages(METHOD, 199, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 199)", oneMsg(line199));
		
		List<Message> line200 = messageStore.getAllMessages(METHOD, 200, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invoke (at line 200)", oneMsg(line200));
		
		List<Message> line208 = messageStore.getAllMessages(METHOD, 208, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 208)", oneMsg(line208));
		
		List<Message> line209 = messageStore.getAllMessages(METHOD, 209, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invoke (at line 209)", oneMsg(line209));
		
		List<Message> line224 = messageStore.getAllMessages(METHOD, 224, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invoke (at line 224)", oneMsg(line224));
		
		List<Message> all = messageStore.getAllMessages(METHOD, SootLoggerLevel.SECURITY);
		assertTrue("Only 25 messages expected.", all.size() == 25);
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
		List<Message> line33 = messageStore.getAllMessages(OBJECT, 33, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of object level (at line 33)", oneMsg(line33));
		
		List<Message> line42 = messageStore.getAllMessages(OBJECT, 42, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of field level (at line 42)", oneMsg(line42));
		
		List<Message> line48 = messageStore.getAllMessages(OBJECT, 48, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of field level (at line 48)", oneMsg(line48));
		
		List<Message> line56 = messageStore.getAllMessages(OBJECT, 56, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high level to field with low level (at line 56)", oneMsg(line56));
		
		List<Message> line66 = messageStore.getAllMessages(OBJECT, 66, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high level to field with low level (at line 66)", oneMsg(line66));
		
		List<Message> line102 = messageStore.getAllMessages(OBJECT, 102, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation base object level (at line 102)", oneMsg(line102));
		
		List<Message> line109 = messageStore.getAllMessages(OBJECT, 109, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 109)", oneMsg(line109));
		
		List<Message> line117 = messageStore.getAllMessages(OBJECT, 117, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 117)", oneMsg(line117));
		
		List<Message> line125 = messageStore.getAllMessages(OBJECT, 125, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation base object and return level (at line 125)", oneMsg(line125));
		
		List<Message> line134 = messageStore.getAllMessages(OBJECT, 134, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation base object level (at line 134)", oneMsg(line134));
		
		List<Message> line142 = messageStore.getAllMessages(OBJECT, 142, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 142)", oneMsg(line142));
		
		List<Message> line150 = messageStore.getAllMessages(OBJECT, 150, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 150)", oneMsg(line150));
		
		List<Message> line159 = messageStore.getAllMessages(OBJECT, 159, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 159)", oneMsg(line159));
		
		List<Message> line168 = messageStore.getAllMessages(OBJECT, 168, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation base object and return level (at line 168)", oneMsg(line168));
		
		List<Message> line177 = messageStore.getAllMessages(OBJECT, 177, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation base object and return level (at line 177)", oneMsg(line177));
		
		List<Message> all = messageStore.getAllMessages(OBJECT, SootLoggerLevel.SECURITY);
		assertTrue("Only 15 messages expected.", all.size() == 15);
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
		List<Message> line17 = messageStore.getAllMessages(STATIC_FIELD, 17, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 17)", oneMsg(line17));
		
		List<Message> line24 = messageStore.getAllMessages(STATIC_FIELD, 24, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 24)", oneMsg(line24));
		
		List<Message> line31 = messageStore.getAllMessages(STATIC_FIELD, 31, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high level to field with low level (at line 31)", oneMsg(line31));
		
		List<Message> all = messageStore.getAllMessages(STATIC_FIELD, SootLoggerLevel.SECURITY);
		assertTrue("Only 3 messages expected.", all.size() == 3);
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
		List<Message> line13 = messageStore.getAllMessages(STATIC_METHOD, 13, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: void return (at line 13)", oneMsg(line13));
		
		List<Message> line19 = messageStore.getAllMessages(STATIC_METHOD, 19, SootLoggerLevel.SECURITY);
		assertTrue("Expected: high return level / Result: void return (at line 19)", oneMsg(line19));
		
		List<Message> line25 = messageStore.getAllMessages(STATIC_METHOD, 25, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return (at line 25)", oneMsg(line25));
		
		List<Message> line37 = messageStore.getAllMessages(STATIC_METHOD, 37, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invoke (at line 37)", oneMsg(line37));
		
		List<Message> line49 = messageStore.getAllMessages(STATIC_METHOD, 49, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: void return (at line 49)", oneMsg(line49));
		
		List<Message> line55 = messageStore.getAllMessages(STATIC_METHOD, 55, SootLoggerLevel.SECURITY);
		assertTrue("Expected: high return level / Result: void return (at line 55)", oneMsg(line55));
		
		List<Message> line62 = messageStore.getAllMessages(STATIC_METHOD, 62, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 62)", oneMsg(line62));
		
		List<Message> line76 = messageStore.getAllMessages(STATIC_METHOD, 76, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 76)", oneMsg(line76));
		
		List<Message> line90 = messageStore.getAllMessages(STATIC_METHOD, 90, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 90)", oneMsg(line90));
		
		List<Message> line97 = messageStore.getAllMessages(STATIC_METHOD, 97, SootLoggerLevel.SECURITY);
		assertTrue("Expected: high return level / Result: void return (at line 97)", oneMsg(line97));
		
		List<Message> line103 = messageStore.getAllMessages(STATIC_METHOD, 103, SootLoggerLevel.SECURITY);
		assertTrue("Expected: high return level / Result: void return (at line 103)", oneMsg(line103));
		
		List<Message> line117 = messageStore.getAllMessages(STATIC_METHOD, 117, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 117)", oneMsg(line117));
		
		List<Message> line126 = messageStore.getAllMessages(STATIC_METHOD, 126, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 126)", oneMsg(line126));
		
		List<Message> line135 = messageStore.getAllMessages(STATIC_METHOD, 135, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: 2x low argument level, but 2x high argument level given (at line 135)", twoMsg(line135));
		
		List<Message> line150 = messageStore.getAllMessages(STATIC_METHOD, 150, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 150)", oneMsg(line150));
		
		List<Message> line159 = messageStore.getAllMessages(STATIC_METHOD, 159, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 159)", oneMsg(line159));
		
		List<Message> line174 = messageStore.getAllMessages(STATIC_METHOD, 174, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 174)", oneMsg(line174));
		
		List<Message> line183 = messageStore.getAllMessages(STATIC_METHOD, 183, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 183)", oneMsg(line183));
		
		List<Message> line193 = messageStore.getAllMessages(STATIC_METHOD, 193, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invoke (at line 193)", oneMsg(line193));
		
		List<Message> line201 = messageStore.getAllMessages(STATIC_METHOD, 201, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 201)", oneMsg(line201));
		
		List<Message> line202 = messageStore.getAllMessages(STATIC_METHOD, 202, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invoke (at line 202)", oneMsg(line202));
		
		List<Message> line210 = messageStore.getAllMessages(STATIC_METHOD, 210, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 210)", oneMsg(line210));
		
		List<Message> line211 = messageStore.getAllMessages(STATIC_METHOD, 211, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invoke (at line 211)", oneMsg(line211));
		
		List<Message> line226 = messageStore.getAllMessages(STATIC_METHOD, 226, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invoke (at line 226)", oneMsg(line226));
		
		List<Message> all = messageStore.getAllMessages(STATIC_METHOD, SootLoggerLevel.SECURITY);
		assertTrue("Only 25 messages expected.", all.size() == 25);
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
		assertTrue("Only 83 messages expected.", all.size() == 83);
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
		List<Message> all = messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.SECURITY);
		for (Message message : all) {
			System.out.println("> " + message.getSourceLine() + ": " + message.getMessage());
		}
	}
}
