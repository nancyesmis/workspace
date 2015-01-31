package wisc.db.equivalencetest;

import wisc.db.equivalence.CatalogAttributeValues;
import junit.framework.TestCase;

/**
 * Tests {@link CatalogAttributeValues} class
 * 
 * @author koc
 *
 */
public class CatalogAttributeValuesTest extends TestCase {
	CatalogAttributeValues test;
	String updateAlg = "hazy";
	int numOfSoftRule = 4;
	double totalSoftRw = 12.5;
	double acc = 4.5;
	double reorganizeTime = 7.2;
	double tao = 0.005;
	
	/**
	 * Default constructor
	 * 
	 * @param name
	 */
	public CatalogAttributeValuesTest(String name) {
		super(name);
	}

	/**
	 * Initializes CatalogAttributeValues object
	 */
	protected void setUp() throws Exception {
		super.setUp();
		test = new CatalogAttributeValues(updateAlg, numOfSoftRule, totalSoftRw, acc, reorganizeTime, tao);
	}

	/**
	 * teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests whether correctly retrieves update alg
	 */
	public void testGetUpdateAlg() {
		assertTrue(test.getUpdateAlg().equals(updateAlg));
	}

	/**
	 * Tests whether correctly retrieves acc
	 */
	public void testGetAcc() {
		assertTrue(test.getAcc() == acc);
	}

	/**
	 * Tests whether correctly retrieves reorganize time
	 */
	public void testGetReorganizeTime() {
		assertTrue(test.getReorganizeTime() == reorganizeTime);
	}

	/**
	 * Tests whether correctly retrieves tao
	 */
	public void testGetTao() {
		assertTrue(test.getTao() == tao);
	}

	/**
	 * Tests whether correctly retrieves num of soft rule
	 */
	public void testGetNumOfSoftRule() {
		assertTrue(test.getNumOfSoftRule() == numOfSoftRule);
	}

	/**
	 * Tests whether correctly retrieves total soft rule weights
	 */
	public void testGetTotalSoftRw() {
		assertTrue(test.getTotalSoftRw() == totalSoftRw);
	}

}
