package wisc.db.equivalencetest;

import wisc.db.equivalence.EqViewSetup;
import wisc.db.equivalence.EqViewSetupFactory;
import junit.framework.TestCase;

/**
 * Tests {@link EqViewSetupFactory} class
 * 
 * @author koc
 *
 */
public class EqViewSetupFactoryTest extends TestCase {
	/**
	 * Default constructor
	 * 
	 * @param name
	 */
	public EqViewSetupFactoryTest(String name) {
		super(name);
	}

	/**
	 * setup method
	 */
	protected void setUp() throws Exception {
		super.setUp();
		EqViewSetup.loadHazyConfParameters();
	}

	/**
	 * teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Retrieves eqViewSetup object from EqViewSetupFactory and checks whether it is null or not
	 */
	public void testGetEqViewSetup() {
		assertTrue(EqViewSetupFactory.getEqViewSetup() != null);
	}

}
