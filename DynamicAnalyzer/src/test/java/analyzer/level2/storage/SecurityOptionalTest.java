package analyzer.level2.storage;

import static org.junit.Assert.assertSame;
import analyzer.level2.HandleStmt;
import analyzer.level2.SecurityLevel;
import analyzer.level2.storage.LocalMap;

import org.junit.Before;
import org.junit.Test;

import utils.exceptions.InternalAnalyzerException;


public class SecurityOptionalTest {
	
	@Before
	public void init() {
		HandleStmt.init();
	}

	@Test
	public void testBasicInputOutput() {
		SecurityOptional so = new SecurityOptional(SecurityLevel.bottom(), true);
		assertSame(SecurityLevel.bottom(), so.getSecurityLevel());
		
		SecurityOptional so2 = new SecurityOptional(SecurityLevel.top(), true);
		assertSame(SecurityLevel.top(), so2.getSecurityLevel());
	}

	@Test(expected = InternalAnalyzerException.class)
	public void testUninitializedAccess() {
		SecurityOptional so = new SecurityOptional(SecurityLevel.bottom(), false);
		so.getSecurityLevel();
	}

	@Test
	public void testInitialisation() {
		SecurityOptional so = new SecurityOptional(SecurityLevel.bottom(), false);
		assertSame(false, so.isInitialized());
		so.initialize();
		assertSame(true, so.isInitialized());
		assertSame(SecurityLevel.bottom(), so.getSecurityLevel());
		
		SecurityOptional so2 = new SecurityOptional(SecurityLevel.top(), false);
		assertSame(false, so2.isInitialized());
		so2.initialize();
		assertSame(true, so2.isInitialized());
		assertSame(SecurityLevel.top(), so2.getSecurityLevel());
	}


}
