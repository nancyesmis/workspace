package wisc.db.equivalence;

/**
 * Represents rule table.
 * <p>
 * This class also keeps weights of rule tables
 * 
 * @author koc
 *
 */
public class RuleTable extends DBTable{
	/**
	 * weight of rule table
	 */
	double weight;
	
	/**
	 * Constructs rule table object with given table name and weight
	 * @param tableName
	 * @param weight
	 */
	public RuleTable(String tableName, double weight) {
		super(tableName);
		this.weight = weight;
	}
	
	/**
	 * Returns weight of the table
	 * 
	 * @return weight
	 */
	public double getWeight() {
		return weight;
	}
}
