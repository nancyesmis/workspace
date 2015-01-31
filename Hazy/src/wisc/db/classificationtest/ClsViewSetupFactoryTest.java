package wisc.db.classificationtest;

import wisc.db.classification.ClsViewSetup;
import wisc.db.classification.ClsViewSetupFactory;
import junit.framework.TestCase;

/**
 * Tests {@link ClsViewSetupFactory} class
 * 
 * @author koc
 *
 */
public class ClsViewSetupFactoryTest extends TestCase {
	ClsViewSetup clsVSetup;
	
	/**
	 * Default constructor
	 * 
	 * @param name
	 */
	public ClsViewSetupFactoryTest(String name) {
		super(name);
	}

	/**
	 * setup method
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ClsViewSetup.loadHazyConfParameters();
		clsVSetup = ClsViewSetupFactory.getClsViewSetup();
	}

	/**
	 * teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test whether retrieved {@link ClsViewSetup} object is null or not
	 */
	public void testGetClsViewSetup() {
		assertTrue(clsVSetup != null);
	}

}
