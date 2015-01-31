package wisc.db.classification;

/**
 * Represents the database tuple structure that will be used for SVM Join.
 * It has tuple id and its score
 * 
 * @author koc
 *
 */
public class JoinTuple {
	int id;
	float score;
	
	/**
	 * Constructor that creates JoinTuple object from given id and score
	 * 
	 * @param id id of tuple
	 * @param score score of tuple
	 */
	public JoinTuple(int id, float score) {
		this.id = id;
		this.score = score;
	}
	
	/**
	 * Retrieves id of the tuple
	 * 
	 * @return id of the tuple
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Retrieves score of the tuple
	 * 
	 * @return score of the tuple
	 */
	public float getScore() {
		return score;
	}
	
	/**
	 * String representation of JoinTuple
	 */
	public String toString() {
		return id + ":" + score;
	}
}
