package wisc.db.classification;

/**
 * Contains classification view algorithms related constants.
 * 
 * @author koc
 *
 */
public class ClsViewConstants {
	/**
	 * This is five fold used during the training
	 */
	public static final int FIVE_FOLD = 5;
	/**
	 * This is epsilon used for updating accumulated cost
	 */
	public static final double EPS = 0.005;
	
	/**
	 * Default value of reservoir option. When user does not specify using reservoir or not option, default value is not using
	 */
	public static final int DEFAULT_RESERVOIR_OPTION = 0;
	
	/**
	 * Default value of no op option. When user does not specify using no op option, default value is not using
	 */
	public static final int DEFAULT_NO_OP_OPTION = 0;
	
	/**
	 * Represents the value that reservoir option is not specified. Then, default value is used
	 */
	public static final int RESERVOIR_OPTION_NOT_SPECIFIED = -1;
	
	/**
	 * Represents the value that noop option is not specified. Then, default value is used
	 */
	public static final int NOOP_OPTION_NOT_SPECIFIED = -1;
	
	/**
	 * naive rescan algorithm. It never uses passive aggressive (noop)
	 */
	public static final String UPDATE_METHOD_NAIVE_RESCAN = "n_rescan";
	/**
	 * This algorithm scans all tables when model is updated. It uses noop when noOpOption is 1
	 */
	public static final String UPDATE_METHOD_RESCAN = "rescan";
	/**
	 * This is the Hazy algorithm used for efficient model update
	 */
	public static final String UPDATE_METHOD_HAZY = "hazy";
	/**
	 * How positive type of example table is represented in table catalog 
	 */
	public static final String REPRESENTATION_OF_POSITIVE_TYPE_EXAMPLE_TABLE = "+";
	/**
	 * How negative type of example table is represented in table catalog
	 */
	public static final String REPRESENTATION_OF_NEGATIVE_TYPE_EXAMPLE_TABLE = "-";
}
