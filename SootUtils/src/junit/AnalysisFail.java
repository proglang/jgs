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
	private static final String EXPR = "TaintTrackingExpr";
	private static final String ID_METHODS = "TaintTrackingIdMethods";
	private static final String IF_ELSE = "TaintTrackingIfElse";
	private static final String IF_ELSE_2 = "TaintTrackingIfElse2";
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
	public final void securityMessagesExpr() {
		List<Message> line13 = messageStore.getAllMessages(EXPR, 13, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 13)", oneMsg(line13));

		List<Message> line21 = messageStore.getAllMessages(EXPR, 21, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 21)", oneMsg(line21));

		List<Message> line29 = messageStore.getAllMessages(EXPR, 29, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 29)", oneMsg(line29));

		List<Message> line37 = messageStore.getAllMessages(EXPR, 37, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 37)", oneMsg(line37));

		List<Message> line45 = messageStore.getAllMessages(EXPR, 45, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 45)", oneMsg(line45));

		List<Message> line53 = messageStore.getAllMessages(EXPR, 53, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 53)", oneMsg(line53));

		List<Message> line61 = messageStore.getAllMessages(EXPR, 61, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 61)", oneMsg(line61));

		List<Message> line69 = messageStore.getAllMessages(EXPR, 69, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 69)", oneMsg(line69));

		List<Message> line77 = messageStore.getAllMessages(EXPR, 77, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 77)", oneMsg(line77));

		List<Message> line85 = messageStore.getAllMessages(EXPR, 85, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 85)", oneMsg(line85));

		List<Message> line93 = messageStore.getAllMessages(EXPR, 93, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 93)", oneMsg(line93));

		List<Message> line101 = messageStore.getAllMessages(EXPR, 101, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 101)", oneMsg(line101));

		List<Message> line109 = messageStore.getAllMessages(EXPR, 109, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 109)", oneMsg(line109));

		List<Message> line117 = messageStore.getAllMessages(EXPR, 117, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 117)", oneMsg(line117));

		List<Message> line125 = messageStore.getAllMessages(EXPR, 125, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 125)", oneMsg(line125));

		List<Message> line133 = messageStore.getAllMessages(EXPR, 133, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 133)", oneMsg(line133));

		List<Message> line141 = messageStore.getAllMessages(EXPR, 141, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 141)", oneMsg(line141));

		List<Message> line149 = messageStore.getAllMessages(EXPR, 149, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 149)", oneMsg(line149));

		List<Message> line157 = messageStore.getAllMessages(EXPR, 157, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 157)", oneMsg(line157));

		List<Message> line165 = messageStore.getAllMessages(EXPR, 165, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 165)", oneMsg(line165));

		List<Message> line173 = messageStore.getAllMessages(EXPR, 173, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 173)", oneMsg(line173));

		List<Message> line181 = messageStore.getAllMessages(EXPR, 181, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 181)", oneMsg(line181));

		List<Message> line189 = messageStore.getAllMessages(EXPR, 189, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 189)", oneMsg(line189));

		List<Message> line197 = messageStore.getAllMessages(EXPR, 197, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 197)", oneMsg(line197));

		List<Message> line205 = messageStore.getAllMessages(EXPR, 205, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 205)", oneMsg(line205));

		List<Message> line213 = messageStore.getAllMessages(EXPR, 213, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 213)", oneMsg(line213));

		List<Message> line221 = messageStore.getAllMessages(EXPR, 221, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 221)", oneMsg(line221));

		List<Message> line229 = messageStore.getAllMessages(EXPR, 229, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 229)", oneMsg(line229));

		List<Message> line237 = messageStore.getAllMessages(EXPR, 237, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 237)", oneMsg(line237));

		List<Message> line245 = messageStore.getAllMessages(EXPR, 245, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 245)", oneMsg(line245));

		List<Message> line253 = messageStore.getAllMessages(EXPR, 253, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 253)", oneMsg(line253));

		List<Message> line261 = messageStore.getAllMessages(EXPR, 261, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 261)", oneMsg(line261));

		List<Message> line269 = messageStore.getAllMessages(EXPR, 269, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 269)", oneMsg(line269));

		List<Message> line277 = messageStore.getAllMessages(EXPR, 277, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 277)", oneMsg(line277));

		List<Message> line285 = messageStore.getAllMessages(EXPR, 285, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 285)", oneMsg(line285));

		List<Message> line293 = messageStore.getAllMessages(EXPR, 293, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 293)", oneMsg(line293));

		List<Message> line301 = messageStore.getAllMessages(EXPR, 301, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 301)", oneMsg(line301));

		List<Message> line309 = messageStore.getAllMessages(EXPR, 309, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 309)", oneMsg(line309));

		List<Message> line317 = messageStore.getAllMessages(EXPR, 317, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 317)", oneMsg(line317));

		List<Message> line325 = messageStore.getAllMessages(EXPR, 325, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 325)", oneMsg(line325));

		List<Message> line333 = messageStore.getAllMessages(EXPR, 333, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 333)", oneMsg(line333));

		List<Message> line341 = messageStore.getAllMessages(EXPR, 341, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return level (at line 341)", oneMsg(line341));
		
		List<Message> all = messageStore.getAllMessages(EXPR, SootLoggerLevel.SECURITY);
		assertTrue("Only 42 messages expected.", all.size() == 42);
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
		List<Message> line15 = messageStore.getAllMessages(IF_ELSE, 15, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level inside of if-expression and high pc (at line 15)", oneMsg(line15));

		List<Message> line24 = messageStore.getAllMessages(IF_ELSE, 24, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level inside of if-expression (at line 24)", oneMsg(line24));

		List<Message> line33 = messageStore.getAllMessages(IF_ELSE, 33, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level inside of if-expression and high pc (at line 33)", oneMsg(line33));

		List<Message> line42 = messageStore.getAllMessages(IF_ELSE, 42, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level inside of if-expression (at line 42)", oneMsg(line42));

		List<Message> line51 = messageStore.getAllMessages(IF_ELSE, 51, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level inside of if-expression and high pc (at line 51)", oneMsg(line51));

		List<Message> line60 = messageStore.getAllMessages(IF_ELSE, 60, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level inside of if-expression (at line 60)", oneMsg(line60));

		List<Message> line69 = messageStore.getAllMessages(IF_ELSE, 69, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of high pc (at line 69)", oneMsg(line69));

		List<Message> line78 = messageStore.getAllMessages(IF_ELSE, 78, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 78)", oneMsg(line78));

		List<Message> line87 = messageStore.getAllMessages(IF_ELSE, 87, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 87)", oneMsg(line87));

		List<Message> line96 = messageStore.getAllMessages(IF_ELSE, 96, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 96)", oneMsg(line96));

		List<Message> line105 = messageStore.getAllMessages(IF_ELSE, 105, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 105)", oneMsg(line105));

		List<Message> line114 = messageStore.getAllMessages(IF_ELSE, 114, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 114)", oneMsg(line114));

		List<Message> line123 = messageStore.getAllMessages(IF_ELSE, 123, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 123)", oneMsg(line123));

		List<Message> line132 = messageStore.getAllMessages(IF_ELSE, 132, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 132)", oneMsg(line132));

		List<Message> line141 = messageStore.getAllMessages(IF_ELSE, 141, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 141)", oneMsg(line141));

		List<Message> line151 = messageStore.getAllMessages(IF_ELSE, 151, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 151)", oneMsg(line151));

		List<Message> line153 = messageStore.getAllMessages(IF_ELSE, 153, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 153)", oneMsg(line153));

		List<Message> line163 = messageStore.getAllMessages(IF_ELSE, 163, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 163)", oneMsg(line163));

		List<Message> line165 = messageStore.getAllMessages(IF_ELSE, 165, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 165)", oneMsg(line165));

		List<Message> line175 = messageStore.getAllMessages(IF_ELSE, 175, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 175)", oneMsg(line175));

		List<Message> line177 = messageStore.getAllMessages(IF_ELSE, 177, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of pc [if contains return] (at line 177)", oneMsg(line177));

		List<Message> line187 = messageStore.getAllMessages(IF_ELSE, 187, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 187)", oneMsg(line187));

		List<Message> line199 = messageStore.getAllMessages(IF_ELSE, 199, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of pc (at line 199)", oneMsg(line199));

		List<Message> line201 = messageStore.getAllMessages(IF_ELSE, 201, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 201)", oneMsg(line201));

		List<Message> line213 = messageStore.getAllMessages(IF_ELSE, 213, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 213)", oneMsg(line213));

		List<Message> line223 = messageStore.getAllMessages(IF_ELSE, 223, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of pc (at line 223)", oneMsg(line223));

		List<Message> line225 = messageStore.getAllMessages(IF_ELSE, 225, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of pc [if contains return] (at line 225)", oneMsg(line225));

		List<Message> line235 = messageStore.getAllMessages(IF_ELSE, 235, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 235)", oneMsg(line235));

		List<Message> line237 = messageStore.getAllMessages(IF_ELSE, 237, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 237)", oneMsg(line237));

		List<Message> line247 = messageStore.getAllMessages(IF_ELSE, 247, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 247)", oneMsg(line247));

		List<Message> line249 = messageStore.getAllMessages(IF_ELSE, 249, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 249)", oneMsg(line249));

		List<Message> line259 = messageStore.getAllMessages(IF_ELSE, 259, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 259)", oneMsg(line259));

		List<Message> line261 = messageStore.getAllMessages(IF_ELSE, 261, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 261)", oneMsg(line261));

		List<Message> line271 = messageStore.getAllMessages(IF_ELSE, 271, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 271)", oneMsg(line271));

		List<Message> line273 = messageStore.getAllMessages(IF_ELSE, 273, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 273)", oneMsg(line273));

		List<Message> line283 = messageStore.getAllMessages(IF_ELSE, 283, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 283)", oneMsg(line283));

		List<Message> line285 = messageStore.getAllMessages(IF_ELSE, 285, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 285)", oneMsg(line285));

		List<Message> line295 = messageStore.getAllMessages(IF_ELSE, 295, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 295)", oneMsg(line295));

		List<Message> line297 = messageStore.getAllMessages(IF_ELSE, 297, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 297)", oneMsg(line297));

		List<Message> line307 = messageStore.getAllMessages(IF_ELSE, 307, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 307)", oneMsg(line307));

		List<Message> line309 = messageStore.getAllMessages(IF_ELSE, 309, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 309)", oneMsg(line309));

		List<Message> line319 = messageStore.getAllMessages(IF_ELSE, 319, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 319)", oneMsg(line319));

		List<Message> line321 = messageStore.getAllMessages(IF_ELSE, 321, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 321)", oneMsg(line321));

		List<Message> line331 = messageStore.getAllMessages(IF_ELSE, 331, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 331)", oneMsg(line331));

		List<Message> line333 = messageStore.getAllMessages(IF_ELSE, 333, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 333)", oneMsg(line333));

		List<Message> line344 = messageStore.getAllMessages(IF_ELSE, 344, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 344)", oneMsg(line344));

		List<Message> line346 = messageStore.getAllMessages(IF_ELSE, 346, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 346)", oneMsg(line346));

		List<Message> line357 = messageStore.getAllMessages(IF_ELSE, 357, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 357)", oneMsg(line357));

		List<Message> line359 = messageStore.getAllMessages(IF_ELSE, 359, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of pc (at line 359)", oneMsg(line359));

		List<Message> line370 = messageStore.getAllMessages(IF_ELSE, 370, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 370)", oneMsg(line370));

		List<Message> line383 = messageStore.getAllMessages(IF_ELSE, 383, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because og pc (at line 383)", oneMsg(line383));

		List<Message> line385 = messageStore.getAllMessages(IF_ELSE, 385, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 385)", oneMsg(line385));

		List<Message> line398 = messageStore.getAllMessages(IF_ELSE, 398, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 398)", oneMsg(line398));

		List<Message> line409 = messageStore.getAllMessages(IF_ELSE, 409, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of pc (at line 409)", oneMsg(line409));

		List<Message> line411 = messageStore.getAllMessages(IF_ELSE, 411, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level because of pc (at line 411)", oneMsg(line411));

		List<Message> line422 = messageStore.getAllMessages(IF_ELSE, 422, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 422)", oneMsg(line422));

		List<Message> line424 = messageStore.getAllMessages(IF_ELSE, 424, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 424)", oneMsg(line424));

		List<Message> line435 = messageStore.getAllMessages(IF_ELSE, 435, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 435)", oneMsg(line435));

		List<Message> line437 = messageStore.getAllMessages(IF_ELSE, 437, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 437)", oneMsg(line437));

		List<Message> line448 = messageStore.getAllMessages(IF_ELSE, 448, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 448)", oneMsg(line448));

		List<Message> line450 = messageStore.getAllMessages(IF_ELSE, 450, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 450)", oneMsg(line450));

		List<Message> line461 = messageStore.getAllMessages(IF_ELSE, 461, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 461)", oneMsg(line461));

		List<Message> line463 = messageStore.getAllMessages(IF_ELSE, 463, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 463)", oneMsg(line463));

		List<Message> line474 = messageStore.getAllMessages(IF_ELSE, 474, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 474)", oneMsg(line474));

		List<Message> line476 = messageStore.getAllMessages(IF_ELSE, 476, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 476)", oneMsg(line476));

		List<Message> line487 = messageStore.getAllMessages(IF_ELSE, 487, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 487)", oneMsg(line487));

		List<Message> line489 = messageStore.getAllMessages(IF_ELSE, 489, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 489)", oneMsg(line489));

		List<Message> line500 = messageStore.getAllMessages(IF_ELSE, 500, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 500)", oneMsg(line500));

		List<Message> line502 = messageStore.getAllMessages(IF_ELSE, 502, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 502)", oneMsg(line502));

		List<Message> line513 = messageStore.getAllMessages(IF_ELSE, 513, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 513)", oneMsg(line513));

		List<Message> line515 = messageStore.getAllMessages(IF_ELSE, 515, SootLoggerLevel.SECURITY);
		assertTrue("Expected: void return / Result: non void return (at line 515)", oneMsg(line515));

		List<Message> line531 = messageStore.getAllMessages(IF_ELSE, 531, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [assign] (at line 531)", oneMsg(line531));

		List<Message> line546 = messageStore.getAllMessages(IF_ELSE, 546, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [assign] (at line 546)", oneMsg(line546));

		List<Message> line561 = messageStore.getAllMessages(IF_ELSE, 561, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [assign] (at line 561)", oneMsg(line561));

		List<Message> line576 = messageStore.getAllMessages(IF_ELSE, 576, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [assign] (at line 576)", oneMsg(line576));

		List<Message> line591 = messageStore.getAllMessages(IF_ELSE, 591, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [assign, pc] (at line 591)", oneMsg(line591));

		List<Message> line606 = messageStore.getAllMessages(IF_ELSE, 606, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible assign] (at line 606)", oneMsg(line606));

		List<Message> line621 = messageStore.getAllMessages(IF_ELSE, 621, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [assign, pc] (at line 621)", oneMsg(line621));

		List<Message> line636 = messageStore.getAllMessages(IF_ELSE, 636, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible assign] (at line 636)", oneMsg(line636));

		List<Message> line651 = messageStore.getAllMessages(IF_ELSE, 651, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [assign, pc] (at line 651)", oneMsg(line651));

		List<Message> line666 = messageStore.getAllMessages(IF_ELSE, 666, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible assign] (at line 666)", oneMsg(line666));

		List<Message> line681 = messageStore.getAllMessages(IF_ELSE, 681, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [assign, pc] (at line 681)", oneMsg(line681));

		List<Message> line696 = messageStore.getAllMessages(IF_ELSE, 696, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible assign] (at line 696)", oneMsg(line696));

		List<Message> line711 = messageStore.getAllMessages(IF_ELSE, 711, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [assign, pc] (at line 711)", oneMsg(line711));

		List<Message> line726 = messageStore.getAllMessages(IF_ELSE, 726, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [assign, pc] (at line 726)", oneMsg(line726));

		List<Message> line736 = messageStore.getAllMessages(IF_ELSE, 736, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 736)", oneMsg(line736));

		List<Message> line738 = messageStore.getAllMessages(IF_ELSE, 738, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 738)", oneMsg(line738));

		List<Message> line750 = messageStore.getAllMessages(IF_ELSE, 750, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 750)", oneMsg(line750));

		List<Message> line752 = messageStore.getAllMessages(IF_ELSE, 752, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 752)", oneMsg(line752));

		List<Message> line764 = messageStore.getAllMessages(IF_ELSE, 764, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 764)", oneMsg(line764));

		List<Message> line766 = messageStore.getAllMessages(IF_ELSE, 766, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 766)", oneMsg(line766));

		List<Message> line778 = messageStore.getAllMessages(IF_ELSE, 778, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 778)", oneMsg(line778));

		List<Message> line792 = messageStore.getAllMessages(IF_ELSE, 792, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 792)", oneMsg(line792));

		List<Message> line794 = messageStore.getAllMessages(IF_ELSE, 794, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 794)", oneMsg(line794));

		List<Message> line808 = messageStore.getAllMessages(IF_ELSE, 808, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 808)", oneMsg(line808));

		List<Message> line820 = messageStore.getAllMessages(IF_ELSE, 820, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 820)", oneMsg(line820));

		List<Message> line822 = messageStore.getAllMessages(IF_ELSE, 822, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 822)", oneMsg(line822));

		List<Message> line838 = messageStore.getAllMessages(IF_ELSE, 838, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 838)", oneMsg(line838));

		List<Message> line852 = messageStore.getAllMessages(IF_ELSE, 852, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 852)", oneMsg(line852));

		List<Message> line862 = messageStore.getAllMessages(IF_ELSE, 862, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 862)", oneMsg(line862));

		List<Message> line864 = messageStore.getAllMessages(IF_ELSE, 864, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 864)", oneMsg(line864));

		List<Message> line876 = messageStore.getAllMessages(IF_ELSE, 876, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 876)", oneMsg(line876));

		List<Message> line878 = messageStore.getAllMessages(IF_ELSE, 878, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 878)", oneMsg(line878));

		List<Message> line894 = messageStore.getAllMessages(IF_ELSE, 894, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 894)", oneMsg(line894));

		List<Message> line908 = messageStore.getAllMessages(IF_ELSE, 908, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 908)", oneMsg(line908));

		List<Message> line918 = messageStore.getAllMessages(IF_ELSE, 918, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 918)", oneMsg(line918));

		List<Message> line920 = messageStore.getAllMessages(IF_ELSE, 920, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 920)", oneMsg(line920));

		List<Message> line932 = messageStore.getAllMessages(IF_ELSE, 932, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 932)", oneMsg(line932));

		List<Message> line950 = messageStore.getAllMessages(IF_ELSE, 950, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 950)", oneMsg(line950));

		List<Message> line964 = messageStore.getAllMessages(IF_ELSE, 964, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 964)", oneMsg(line964));

		List<Message> line974 = messageStore.getAllMessages(IF_ELSE, 974, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 974)", oneMsg(line974));

		List<Message> line976 = messageStore.getAllMessages(IF_ELSE, 976, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 976)", oneMsg(line976));

		List<Message> line990 = messageStore.getAllMessages(IF_ELSE, 990, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 990)", oneMsg(line990));

		List<Message> line1006 = messageStore.getAllMessages(IF_ELSE, 1006, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 1006)", oneMsg(line1006));

		List<Message> line1020 = messageStore.getAllMessages(IF_ELSE, 1020, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 1020)", oneMsg(line1020));

		List<Message> line1030 = messageStore.getAllMessages(IF_ELSE, 1030, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 1030)", oneMsg(line1030));

		List<Message> line1032 = messageStore.getAllMessages(IF_ELSE, 1032, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 1032)", oneMsg(line1032));
		
		List<Message> all = messageStore.getAllMessages(IF_ELSE, SootLoggerLevel.SECURITY);
		assertTrue("Only 117 messages expected.", all.size() == 117);
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
		List<Message> line15 = messageStore.getAllMessages(IF_ELSE_2, 15, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 15)", oneMsg(line15));

		List<Message> line26 = messageStore.getAllMessages(IF_ELSE_2, 26, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 26)", oneMsg(line26));

		List<Message> line37 = messageStore.getAllMessages(IF_ELSE_2, 37, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 37)", oneMsg(line37));

		List<Message> line50 = messageStore.getAllMessages(IF_ELSE_2, 50, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 50)", oneMsg(line50));

		List<Message> line59 = messageStore.getAllMessages(IF_ELSE_2, 59, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 59)", oneMsg(line59));

		List<Message> line72 = messageStore.getAllMessages(IF_ELSE_2, 72, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 72)", oneMsg(line72));

		List<Message> line81 = messageStore.getAllMessages(IF_ELSE_2, 81, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 81)", oneMsg(line81));

		List<Message> line94 = messageStore.getAllMessages(IF_ELSE_2, 94, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 94)", oneMsg(line94));

		List<Message> line103 = messageStore.getAllMessages(IF_ELSE_2, 103, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 103)", oneMsg(line103));

		List<Message> line116 = messageStore.getAllMessages(IF_ELSE_2, 116, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 116)", oneMsg(line116));

		List<Message> line127 = messageStore.getAllMessages(IF_ELSE_2, 127, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 127)", oneMsg(line127));

		List<Message> line138 = messageStore.getAllMessages(IF_ELSE_2, 138, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 138)", oneMsg(line138));

		List<Message> line149 = messageStore.getAllMessages(IF_ELSE_2, 149, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 149)", oneMsg(line149));

		List<Message> line160 = messageStore.getAllMessages(IF_ELSE_2, 160, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level (at line 160)", oneMsg(line160));

		List<Message> line171 = messageStore.getAllMessages(IF_ELSE_2, 171, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible pc] (at line 171)", oneMsg(line171));

		List<Message> line182 = messageStore.getAllMessages(IF_ELSE_2, 182, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible] (at line 182)", oneMsg(line182));

		List<Message> line193 = messageStore.getAllMessages(IF_ELSE_2, 193, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible pc] (at line 193)", oneMsg(line193));

		List<Message> line204 = messageStore.getAllMessages(IF_ELSE_2, 204, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible] (at line 204)", oneMsg(line204));

		List<Message> line215 = messageStore.getAllMessages(IF_ELSE_2, 215, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible pc] (at line 215)", oneMsg(line215));

		List<Message> line226 = messageStore.getAllMessages(IF_ELSE_2, 226, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible] (at line 226)", oneMsg(line226));

		List<Message> line237 = messageStore.getAllMessages(IF_ELSE_2, 237, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible pc] (at line 237)", oneMsg(line237));

		List<Message> line248 = messageStore.getAllMessages(IF_ELSE_2, 248, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [possible] (at line 248)", oneMsg(line248));

		List<Message> line259 = messageStore.getAllMessages(IF_ELSE_2, 259, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [pc] (at line 259)", oneMsg(line259));

		List<Message> line270 = messageStore.getAllMessages(IF_ELSE_2, 270, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low or weaker return level / Result: high return level [pc] (at line 270)", oneMsg(line270));

		List<Message> line285 = messageStore.getAllMessages(IF_ELSE_2, 285, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 285)", oneMsg(line285));

		List<Message> line300 = messageStore.getAllMessages(IF_ELSE_2, 300, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [possible pc] (at line 300)", oneMsg(line300));

		List<Message> line315 = messageStore.getAllMessages(IF_ELSE_2, 315, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [possible pc] (at line 315)", oneMsg(line315));

		List<Message> line330 = messageStore.getAllMessages(IF_ELSE_2, 330, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [possible] (at line 330)", oneMsg(line330));

		List<Message> line345 = messageStore.getAllMessages(IF_ELSE_2, 345, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [possible pc] (at line 345)", oneMsg(line345));

		List<Message> line360 = messageStore.getAllMessages(IF_ELSE_2, 360, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [possible] (at line 360)", oneMsg(line360));

		List<Message> line375 = messageStore.getAllMessages(IF_ELSE_2, 375, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 375)", oneMsg(line375));

		List<Message> line390 = messageStore.getAllMessages(IF_ELSE_2, 390, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 390)", oneMsg(line390));

		List<Message> line405 = messageStore.getAllMessages(IF_ELSE_2, 405, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field (at line 405)", oneMsg(line405));

		List<Message> line420 = messageStore.getAllMessages(IF_ELSE_2, 420, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [possible pc] (at line 420)", oneMsg(line420));

		List<Message> line435 = messageStore.getAllMessages(IF_ELSE_2, 435, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [possible] (at line 435)", oneMsg(line435));

		List<Message> line450 = messageStore.getAllMessages(IF_ELSE_2, 450, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [possible pc] (at line 450)", oneMsg(line450));

		List<Message> line465 = messageStore.getAllMessages(IF_ELSE_2, 465, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [possible] (at line 465)", oneMsg(line465));

		List<Message> line480 = messageStore.getAllMessages(IF_ELSE_2, 480, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high security level value to low level field [pc] (at line 480)", oneMsg(line480));

		List<Message> all = messageStore.getAllMessages(IF_ELSE_2, SootLoggerLevel.SECURITY);
		assertTrue("Only 38 messages expected.", all.size() == 38);
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
		
		List<Message> line41 = messageStore.getAllMessages(OBJECT, 41, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of field level (at line 41)", oneMsg(line41));
		
		List<Message> line49 = messageStore.getAllMessages(OBJECT, 49, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of field level (at line 49)", oneMsg(line49));
		
		List<Message> line55 = messageStore.getAllMessages(OBJECT, 55, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of field level (at line 55)", oneMsg(line55));
		
		List<Message> line63 = messageStore.getAllMessages(OBJECT, 63, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high level to field with low level (at line 63)", oneMsg(line63));
		
		List<Message> line73 = messageStore.getAllMessages(OBJECT, 73, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high level to field with low level (at line 73)", oneMsg(line73));
		
		List<Message> line81 = messageStore.getAllMessages(OBJECT, 81, SootLoggerLevel.SECURITY);
		assertTrue("Assignment of high level to field with low level (at line 81)", oneMsg(line81));
		
		List<Message> line117 = messageStore.getAllMessages(OBJECT, 117, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation base object level (at line 117)", oneMsg(line117));
		
		List<Message> line124 = messageStore.getAllMessages(OBJECT, 124, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 124)", oneMsg(line124));
		
		List<Message> line132 = messageStore.getAllMessages(OBJECT, 132, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 132)", oneMsg(line132));
		
		List<Message> line140 = messageStore.getAllMessages(OBJECT, 140, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation base object and return level (at line 140)", oneMsg(line140));
		
		List<Message> line149 = messageStore.getAllMessages(OBJECT, 149, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation base object level (at line 149)", oneMsg(line149));
		
		List<Message> line157 = messageStore.getAllMessages(OBJECT, 157, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 157)", oneMsg(line157));
		
		List<Message> line165 = messageStore.getAllMessages(OBJECT, 165, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 165)", oneMsg(line165));
		
		List<Message> line174 = messageStore.getAllMessages(OBJECT, 174, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 174)", oneMsg(line174));
		
		List<Message> line183 = messageStore.getAllMessages(OBJECT, 183, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation base object and return level (at line 183)", oneMsg(line183));
		
		List<Message> line192 = messageStore.getAllMessages(OBJECT, 192, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation base object and return level (at line 192)", oneMsg(line192));
		
		List<Message> line200 = messageStore.getAllMessages(OBJECT, 200, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 200)", oneMsg(line200));

		List<Message> line201 = messageStore.getAllMessages(OBJECT, 201, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation base object (at line 201)", oneMsg(line201));

		List<Message> line209 = messageStore.getAllMessages(OBJECT, 209, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 209)", oneMsg(line209));

		List<Message> line243 = messageStore.getAllMessages(OBJECT, 243, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 243)", oneMsg(line243));

		List<Message> line249 = messageStore.getAllMessages(OBJECT, 249, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 249)", oneMsg(line249));

		List<Message> line256 = messageStore.getAllMessages(OBJECT, 256, SootLoggerLevel.SECURITY);
		assertTrue("Invoked Method expects: low argument level, but high argument level given (at line 256)", oneMsg(line256));

		List<Message> line264 = messageStore.getAllMessages(OBJECT, 264, SootLoggerLevel.SECURITY);
		assertTrue("Expected: low return level / Result: high return because of invocation return level (at line 264)", oneMsg(line264));
		
		List<Message> all = messageStore.getAllMessages(OBJECT, SootLoggerLevel.SECURITY);
		assertTrue("Only 24 messages expected.", all.size() == 24);
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
		assertTrue("Only 279 messages expected.", all.size() == 279);
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
//		List<Message> all = messageStore.getAllMessages(OBJECT, SootLoggerLevel.SECURITY);
//		StringBuilder builder = new StringBuilder();
//		for (Message message : all) {
//			if (message.getSourceLine() >= 0) {
//				System.out.println("> " + message.getSourceLine() + ": " + message.getMessage());
//			}
//			builder.append(((!builder.toString().equals("")) ? "," : "") + message.getSourceLine());
//			
//		}
//		System.out.println(builder.toString());
	}
}
