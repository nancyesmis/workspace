package wisc.db.equivalence;

/**
 * Contains equivalence algorithm related constants
 * 
 * @author koc
 *
 */
public class EqViewConstants {
	/**
	 * Initial value of accumulated cost. (Also value when recluster algorithm is used, because there is no acc_cost for recluster)
	 */
	public static final double INITIAL_ACC_COST = 0;
	/**
	 * This is used for update of acc_cost. Equation is: acc_cost = (1 - tao) * acc_cost + update_cost
	 */
	public static final double TAO = 0.005;
	
	/**
	 * HAZY algorithm
	 */
	public static final String HAZY_ALG = "hazy";
	/**
	 * Recluster algorithm
	 */
	public static final String RECLUSTER_ALG = "recluster";
	
	/**
	 * Representation of soft edge update
	 */
	public static final String SOFT_EDGE_UPDATE = "+";
	/**
	 * Representation of hard eq edge update
	 */
	public static final String HARD_EQ_EDGE_UPDATE = "=";
	/**
	 * Representation of hard neq edge update
	 */
	public static final String HARD_NEQ_EDGE_UPDATE = "=/";
	/**
	 * During update, if on-memory partitions exceed memory usage limit, then it is forced to disk.
	 */
	public static final long MEMORY_USAGE_LIMIT = 107374182;
}
