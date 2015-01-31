package wisc.db.equivalence;

/**
 * Creates a static {@link EqViewSetup} object.
 * <p>If it is null, then creates new one. Else, returns the existing one
 * 
 * @author koc
 *
 */
public class EqViewSetupFactory {
	static EqViewSetup eqVSetup = null;
	
	/**
	 * This method returns static EqViewSetup object
	 * <p>
	 * If it is null, it creates new
	 * @return EqViewSetup object
	 */
	public static EqViewSetup getEqViewSetup()
	{
		if (eqVSetup == null)
			eqVSetup = new EqViewSetup();
		
		return eqVSetup;
	}

}