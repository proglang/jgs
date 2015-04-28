package tests.handleStmtTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import analyzer.level2.storage.ObjectMap;

public class switchStmtFail {

	@Test
	public void test() {
	}
	
	@After
	public void close() {
	    ObjectMap m = ObjectMap.getInstance();
	    m.deleteLocalMapStack();
	}

}
