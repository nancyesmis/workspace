package wisc.db.equivalence;

/**
 * Represents the structure for some of catalog attributes that are being used during the Hazy update
 * 
 * @author koc
 *
 */
public class CatalogAttributeValues {
	String updateAlg;
	double acc;
	double reorganizeTime;
	double tao;
	int numOfSoftRule;
	double totalSoftRw;
	
	/**
	 * Craetes a CatalogAttributeValues objects from the given algorithm, number of soft rules, total soft rules weights, accumulator cost,
	 * reorganize time and tao.
	 * 
	 * If algorithm is hazy, then other values are also used.
	 * If algorithm is recluster, then acc, reorganizetime and tao are not used.
	 * 
	 * For efficiency, they are retrieved with one query
	 * 
	 * @param updateAlg update algorithm
	 * @param numOfSoftRule number of soft rules
	 * @param totalSoftRw total soft rule weights
	 * @param acc accumulated cost
	 * @param reorganizeTime reorganize time
	 * @param tao tao
	 */
	public CatalogAttributeValues(String updateAlg, int numOfSoftRule, double totalSoftRw, double acc,
			double reorganizeTime, double tao) {
		this.updateAlg = updateAlg;
		this.acc = acc;
		this.reorganizeTime = reorganizeTime;
		this.tao = tao;
		this.numOfSoftRule = numOfSoftRule;
		this.totalSoftRw = totalSoftRw;
	}

	/**
	 * Returns update algorithm
	 * 
	 * @return update algorithm
	 */
	public String getUpdateAlg() {
		return updateAlg;
	}

	/**
	 * Returns accumulated cost
	 * 
	 * @return accumulated cost
	 */
	public double getAcc() {
		return acc;
	}

	/**
	 * Returns reorganize time
	 * 
	 * @return reorganize time
	 */
	public double getReorganizeTime() {
		return reorganizeTime;
	}

	/**
	 * Returns tao
	 * 
	 * @return tao
	 */
	public double getTao() {
		return tao;
	}
	
	/**
	 * Returns number of soft rules
	 * 
	 * @return number of soft rule
	 */
	public int getNumOfSoftRule() {
		return numOfSoftRule;
	}
	
	/**
	 * Returns total soft rule weights
	 * 
	 * @return total soft rule weights
	 */
	public double getTotalSoftRw() {
		return totalSoftRw;
	}
}
