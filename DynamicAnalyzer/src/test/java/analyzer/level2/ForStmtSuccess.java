package analyzer.level2;

import org.junit.Before;
import org.junit.Test;
import util.logging.L2Logger;

import java.util.logging.Level;
import java.util.logging.Logger;


public class ForStmtSuccess {
	
	Logger LOGGER = L2Logger.getLogger();
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@SuppressWarnings("unused")
	@Test
	public void forStmtLocalTestSuccess() {
		
		LOGGER.log(Level.INFO, "FOR STMT LOCAL TEST SUCCESS STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addLocal("int_i");
		hs.addLocal("int_res");
		
		int res = 0;

		hs.setLocal("int_i", CurrentSecurityDomain.bottom());

		hs.checkCondition("123", "int_i");
		for (int i = 0;i < 1; i++) {
			
			hs.checkLocalPC("int_res");
			hs.setLocalToCurrentAssignmentLevel("int_res");
			res = 2;
			
			hs.exitInnerScope("123");
		}
		
		hs.close();
		
		LOGGER.log(Level.INFO, "FOR STMT LOCAL TEST SUCCESS FINISHED");
	}
	
	@SuppressWarnings("unused")
	@Test
	public void forStmtFieldTestSuccess() {
		
		LOGGER.log(Level.INFO, "FOR STMT FIELD TEST SUCCESS STARTED");
		
		HandleStmt hs = new HandleStmt();
		hs.initHandleStmtUtils(false, 0);
		hs.addObjectToObjectMap(this);
		hs.addLocal("int_i");
		hs.addFieldToObjectMap(this, "int_res");
		
		int i = 0; // Local
		int res; // Field

		hs.setLocal("int_i", CurrentSecurityDomain.bottom());
		
		hs.checkCondition("123", "int_i");
		for (i = 0;i < 1; i++) {
			
			hs.checkGlobalPC(this, "int_res");
			hs.setLevelOfField(this, "int_res");
			res = 2;
			
			hs.exitInnerScope("123");
		}
		
		hs.close();
		
		LOGGER.log(Level.INFO, "FOR STMT FIELD TEST SUCCESS FINISHED");
	}

}
