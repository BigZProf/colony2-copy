package colony.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(ColonyTest.class);
		suite.addTestSuite(GameTest.class);
		suite.addTestSuite(TaskTest.class);
		//$JUnit-END$
		return suite;
	}

}
