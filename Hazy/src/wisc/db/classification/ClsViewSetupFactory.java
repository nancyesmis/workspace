package wisc.db.classification;

/**
 * Creates a static {@link ClsViewSetup} object.
 * <p>If it is null, then creates new one. Else, returns the existing one
 * 
 * @author koc
 *
 */
public class ClsViewSetupFactory {
	/**
	 * ClsViewSetup object
	 */
	static ClsViewSetup clsVSetup = null;
	
	/**
	 * This method returns static ClsViewSetup object
	 * <p>
	 * If it is null, it creates new
	 * @return ClsViewSetup object
	 */
	public static ClsViewSetup getClsViewSetup()
	{
		if (clsVSetup == null)
			clsVSetup = new ClsViewSetup();

		return clsVSetup;
	}

}