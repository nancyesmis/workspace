package wisc.db.equivalence;

/**
 * Represents view table.
 * <p>Class keeps table name and create select query
 * 
 * @author koc
 *
 */
public class DBTable {
	/**
	 * name of the table
	 */
	String tableName;
	
	/**
	 * Constructs ViewTable object with the given table name
	 * 
	 * @param tableName
	 */
	public DBTable(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * Returns table name of the view
	 * 
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Returns select query for the view
	 * 
	 * @return select query
	 */
	public String getSelectQuery() {
		return "SELECT * FROM " + tableName + ";";
	}
}
