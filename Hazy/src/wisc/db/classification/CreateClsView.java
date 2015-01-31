package wisc.db.classification;

/**
 * Creates classification view on the database and creates required internal tables, functions and triggers
 * 
 * @author koc
 *
 */
public class CreateClsView {
	/**
	 * Static {@link ClsViewSetup} object to make the transition
	 */
	static ClsViewSetup clsVSetup;
	
	/**
	 * Default constructor that initializes {@link ClsViewSetup} from {@link ClsViewSetupFactory}
	 */
	public CreateClsView() {
		clsVSetup = ClsViewSetupFactory.getClsViewSetup();
	}
	
	/**
	 * This method creates view, required tables, train the model and insert it to the catalog of view table
	 * 
	 * @param viewId id of the view
	 * @param viewName view name
	 * @param entityTable entity table name
	 * @param posExampleTable positive example table
	 * @param negExampleTable negative example table
	 * @param reservoirOption reservoir option
	 * @param noOpOption no op option
	 */
	private static void train(int viewId, String viewName, String entityTable, String posExampleTable, String negExampleTable, int reservoirOption, int noOpOption) {
		//prepare stats table and training file
		double m_factor = clsVSetup.prepareTrain(viewId, entityTable, posExampleTable, negExampleTable);
		//retrieves # of features (dimension)
		int featureSize = clsVSetup.getNumberOfFeatures(viewId);
		//fills catalog table
		clsVSetup.fillsViewCatalogTable(viewId, viewName, entityTable, reservoirOption, noOpOption, m_factor);
		clsVSetup.fillsUpdateTableViewIdCatalogTable(posExampleTable, negExampleTable, reservoirOption, noOpOption, viewId);
		//if reservoir option is chosen, reservoirs are created
		if(reservoirOption > 0)
			clsVSetup.createReservoir(viewId);
		//train;
		int retValue = clsVSetup.train(viewId, featureSize, reservoirOption, noOpOption);
		if(retValue == -1)
			System.err.println("view could not be created");
		clsVSetup.prepareGeneralRTableForHazy(viewId, entityTable);
	}
	
	/**
	 * Creates triggers on the given positive and negative example tables
	 * 
	 * @param viewId id of the view
	 * @param posExampleTable positive example table
	 * @param negExampleTable negative example table
	 */
	private static void createTriggers(int viewId, String entityTable, String posExampleTable, String negExampleTable) {
		clsVSetup.createTriggers(viewId, entityTable, posExampleTable, negExampleTable);
	}
	
	/**
	 * Creates defined view and triggers on the tables
	 * 
	 * If resOption or noOpOption is not specified, then their default values are retrieved from {@link ClsViewConstants}
	 * 
	 * @param viewName name of the view
	 * @param entityTable entity table name
	 * @param posExTable positive example table
	 * @param negExTable negative example table
	 * @param resOption reservoir option
	 * @param noOpOption no op option
	 */
	public static void createViewAndTriggers(String viewName, String entityTable, String posExTable, String negExTable, int resOption, int noOpOption) {
		clsVSetup = ClsViewSetupFactory.getClsViewSetup();
		int viewId = clsVSetup.getUniqueViewId();
		clsVSetup.insertIntoIdCatalog(viewId, entityTable, viewName);
		clsVSetup.createViewCatalogTable(viewId);
		train(viewId, viewName, entityTable, posExTable, negExTable, resOption, noOpOption);
		createTriggers(viewId, entityTable, posExTable, negExTable);
		clsVSetup.createViewAfterTraining(viewId, viewName);
	}
}
