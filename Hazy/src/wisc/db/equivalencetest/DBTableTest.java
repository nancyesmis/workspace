package wisc.db.equivalencetest;

import wisc.db.equivalence.DBTable;
import junit.framework.TestCase;

/**
 * Tests {@link DBTable} class
 * 
 * @author koc
 *
 */
public class DBTableTest extends TestCase {
	DBTable table;
	String name = "tableName";
	
	/**
	 * default constructor
	 * 
	 * @param name
	 */
	public DBTableTest(String name) {
		super(name);
	}

	/**
	 * initializes DBTable object
	 */
	protected void setUp() throws Exception {
		super.setUp();
		table = new DBTable(name);
	}

	/**
	 * teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * tests correctly created or not
	 */
	public void testDBTable() {
		assertTrue(table.getTableName().equals(name));
	}

	/**
	 * tests correctly retrieve table name
	 */
	public void testGetTableName() {
		assertTrue(table.getTableName().equals(name));
	}

	/**
	 * tests correctly retrieve select query for table
	 */
	public void testGetSelectQuery() {
		assertTrue(table.getSelectQuery().equals("SELECT * FROM " + name + ";"));
	}

}
