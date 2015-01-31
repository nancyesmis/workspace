package wisc.db.equivalencetest;

import wisc.db.equivalence.RuleTable;
import junit.framework.TestCase;

/**
 * Tests {@link RuleTable} class
 * 
 * @author koc
 *
 */
public class RuleTableTest extends TestCase {
	RuleTable table;
	String name = "tableName";
	double weight = 3.2;
	
	/**
	 * Default constructor
	 * 
	 * @param name
	 */
	public RuleTableTest(String name) {
		super(name);
	}

	/**
	 * Iniitializes RuleTable object
	 */
	protected void setUp() throws Exception {
		super.setUp();
		table = new RuleTable(name, weight);
	}

	/**
	 * teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests whether RuleTable object is correctly created from given table name and weight
	 */
	public void testRuleTable() {
		assertTrue(table.getTableName().equals(name));
		assertTrue(table.getWeight() == weight);
	}

	/**
	 * Tests whether getWeight method of RuleTable returns the correct weight or not
	 */
	public void testGetWeight() {
		assertTrue(table.getWeight() == weight);
	}

	/**
	 * Tests whether name of the table is correct
	 */
	public void testDBTable() {
		assertTrue(table.getTableName().equals(name));
	}

	/**
	 * Tests whether getTableName method of RuleTable returns the correct name or not
	 */
	public void testGetTableName() {
		assertTrue(table.getTableName().equals(name));
	}

	/**
	 * Tests whether getSelectQuery method of RuleTable returns the correct query
	 */
	public void testGetSelectQuery() {
		assertTrue(table.getSelectQuery().equals("SELECT * FROM " + name + ";"));
	}

}
