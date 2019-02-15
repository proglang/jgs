package analyzer.level2.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import analyzer.level2.CurrentSecurityDomain;
import analyzer.level2.HandleStmt;
import org.junit.Before;
import org.junit.Test;
import util.logging.L2Logger;

import java.util.logging.Logger;

public class ObjectMapTest {

	Logger logger = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void singletonTest() {
		
		logger.info("OBEJCT MAP AS SINGLETON TEST STARTED");
		
		ObjectMap m1 = ObjectMap.getInstance();
		ObjectMap m2 = ObjectMap.getInstance();
		
		assertSame("The two instances of ObjectMap are not the same", m1, m2);
		
		logger.info("OBEJCT MAP AS SINGLETON TEST FINISHED");
	}
	
	@Test
	public void globalPCTest() {
		
		logger.info("GLOBAL PC TEST STARTED");
		
		ObjectMap m = ObjectMap.getInstance();
		assertEquals(CurrentSecurityDomain.bottom(), m.getGlobalPC());
		m.pushGlobalPC(CurrentSecurityDomain.top());
		assertEquals(CurrentSecurityDomain.top(), m.getGlobalPC());

		logger.info("GLOBAL PC TEST FINISHED");
	}
	
	@Test
	public void insertNewObjectTest() {
		
		logger.info("INSERT NEW OBJECT TEST STARTED");
		
		ObjectMap m = ObjectMap.getInstance();
		
		Object o1 = new Object();
		Object o2 = new Object();
		assertNotSame(o1, o2);
		
		m.insertNewObject(o1);
		m.insertNewObject(o2);
		
		assertEquals(2, m.getNumberOfElements()); 
		// The same object should not be inserted a second time
		m.insertNewObject(o1);
		assertEquals(2, m.getNumberOfElements());
		
		logger.info("INSERT NEW OBJECT TEST FINISHED");
	}
	
	@Test
	public void fieldsTest() {

		logger.info("FIELDS IN OBJECT MAP TEST STARTED");
		
		ObjectMap m = ObjectMap.getInstance();
		Object o = new Object();
		m.insertNewObject(o);
		
		String f1 = "<int i1>";
		String f2 = "<int i2>";
		String f3 = "<String s1>";
		
		m.addField(o, f1);
		m.addField(o, f2);
		m.setField(o, f3, CurrentSecurityDomain.top());
		
		Object o2 = new Object();
		m.insertNewObject(o2);
		
		m.setField(o2, f1, CurrentSecurityDomain.top());
		m.setField(o2, f2, CurrentSecurityDomain.bottom());
		m.addField(o2, f3);

		assertSame(m.getFieldLevel(o, f1),CurrentSecurityDomain.bottom());
		assertSame(m.getFieldLevel(o, f2),CurrentSecurityDomain.bottom());
		assertSame(m.getFieldLevel(o, f3),CurrentSecurityDomain.top());

		assertSame(m.getFieldLevel(o2, f1),CurrentSecurityDomain.top());
		assertSame(m.getFieldLevel(o2, f2),CurrentSecurityDomain.bottom());
		assertSame(m.getFieldLevel(o2, f3),CurrentSecurityDomain.bottom());

		logger.info("FIELDS IN OBJECT MAP TEST FINISHED");
	}
	
	@Test 
	public void multipleObjectsTest() {

		logger.info("MULTIPLE OBJECTS TEST STARTED");
		
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		ObjectMap m = ObjectMap.getInstance();
		int numOfEl = m.getNumberOfElements();
		hs.addObjectToObjectMap(this);
		hs.addObjectToObjectMap(this);
		// The map should contain the same object twice
		assertEquals(numOfEl + 1, m.getNumberOfElements());
		Integer i = new Integer(3);
		hs.addObjectToObjectMap(i);
		assertEquals(numOfEl + 2, m.getNumberOfElements());
		hs.close();
		
		logger.info("MULTIPLE OBJECTS TEST FINISHED");
	}


}
