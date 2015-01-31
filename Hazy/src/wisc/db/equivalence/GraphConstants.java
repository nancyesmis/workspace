package wisc.db.equivalence;

/**
 *	GraphConstants class that keeps useful constants for Graph
 *
 *  @author koc
 **/
public class GraphConstants {
	/**
	 * 	This is the delimiter between src id and dst id of an edge in string representation
	 */
	protected static final String EDGE_DELIMITER = "-";
	/**
	 * 	This is the threshold to count an edge as positive
	 */
	public static double EDGE_VOTE_THRESHOLD = 0.5;
}
