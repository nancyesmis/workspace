package wisc.db.equivalence;

import java.util.ArrayList;

/**
 * Creates equivalence view on the database and creates required internal tables, functions and triggers
 * <p>
 * Initially create all required tables such as catalog, internal tables. Then insert values into the catalogs.
 * By using {@link Graph}, make a clustering and then writing results to the database
 * Finally, creates required triggers on the tables for future updates
 * 
 * @author koc
 *
 */
public class CreateEqView {
	/**
	 * Static {@link EqViewSetup} object to make the transition
	 */
	static EqViewSetup eqVSetup;
	
	/**
	 * Default constructor that initializes {@link EqViewSetup} from {@link EqViewSetupFactory}
	 */
	public CreateEqView() {
		eqVSetup = EqViewSetupFactory.getEqViewSetup();
	}
	
	/**
	 * Does preprocessing such as creating required internal tables and catalog
	 * 
	 * @param viewId id of the view
	 * @param viewName name of the view
	 * @param softRuleTables soft rule tables
	 * @param hardEQRuleTables hard eq rule tables
	 * @param hardNEQRuleTables hard neq rule tables
	 */
	private static void prepareTables(int viewId, String viewName, String entityName, ArrayList<RuleTable> softRuleTables, ArrayList<RuleTable> hardEQRuleTables, ArrayList<RuleTable> hardNEQRuleTables) {
		eqVSetup.createTables(viewId, viewName);	
		eqVSetup.insertIntoIdCatalog(viewId, viewName, entityName);
		eqVSetup.insertSoftTableIntoRulesIdWeightTab(viewId, softRuleTables);
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId, hardEQRuleTables, true);
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId, hardNEQRuleTables, false);
	}
	
	/**
	 * Creates indices on view and entity partition table(internal table that keeps partitions) and create triggers on rule tables for future updates
	 * 
	 * @param viewId id of the view
	 * @param viewName name of the view
	 * @param softRuleTables soft rule tables
	 * @param hardEQRuleTables hard eq rule tables
	 * @param hardNEQRuleTables hard neq rule tables
	 */
	private static void createIndicesTriggers(int viewId, String viewName, String entityTableName, ArrayList<RuleTable> softRuleTables, 
			ArrayList<RuleTable> hardEQRuleTables, ArrayList<RuleTable> hardNEQRuleTables) {
		createIndices(viewId, viewName);
		createTriggers(viewId, entityTableName, softRuleTables, hardEQRuleTables, hardNEQRuleTables);
	}
	
	/**
	 * Create triggers on rule tables for future updates
	 * 
	 * @param viewId id of the view
	 * @param softRuleTables soft rule tables
	 * @param hardEQRuleTables hard eq rule tables
	 * @param hardNEQRuleTables hard neq rule tables
	 */
	private static void createTriggers(int viewId, String entityTableName, ArrayList<RuleTable> softRuleTables, ArrayList<RuleTable> hardEQRuleTables, 
			ArrayList<RuleTable> hardNEQRuleTables) {
		eqVSetup.createTriggers(viewId, entityTableName, softRuleTables, hardEQRuleTables, hardNEQRuleTables);
	}
	
	/**
	 * Creates indices on view table and entity partition table(internal table for partitions)
	 * <p>
	 * Entity table name is 
	 * 
	 * @param viewId id of the view
	 * @param viewName name of the view
	 */
	private static void createIndices(int viewId, String viewName) {
		eqVSetup.createIndex(viewName, viewId);
	}
	
	/**
	 * Inserts catalog values that are obtained after clustering to the catalog
	 * 
	 * @param viewId id of the view
	 * @param reorganizeTime reorganization time (During the view creation(clustering), 
	 * there is no reorganization. Hence, clustering time which is roughly same with reorganization time is used )
	 * @param numSoftRule number of soft rules (this is used, because when it is 1, calculation of weights is not required, which is more efficient)
	 * @param totalSoftRuleWeight total number of soft rule weights
	 */
	private static void fillsCatalogTable(int viewId, double reorganizeTime, int numSoftRule, double totalSoftRuleWeight) {
		eqVSetup.fillsCatalogTable(viewId, reorganizeTime, numSoftRule, totalSoftRuleWeight);
	}
	
	/**
	 * Creates an equivalence view from view, entity, soft rule, hard eq rule and hard neq rule tables
	 * <p>
	 * Initially create all required tables such as catalog, internal tables. Then insert values into the catalogs. 
	 * By using {@link Graph}, make a clustering and then writing results to the database 
	 * Finally, creates required triggers on the tables for future updates
	 * 
	 * @param softRuleTables soft rule tables
	 * @param hardEQRuleTables hard eq rule tables
	 * @param hardNEQRuleTables hard neq rule tables
	 * @param entityTable entity table
	 * @param viewTable view table
	 */
	public static void createViewMethod(DBTable viewTable, DBTable entityTable, ArrayList<RuleTable> softRuleTables,
			ArrayList<RuleTable> hardEQRuleTables,
			ArrayList<RuleTable> hardNEQRuleTables) {
		eqVSetup = EqViewSetupFactory.getEqViewSetup();
		int viewId = eqVSetup.getUniqueViewId();
		String viewName = viewTable.getTableName();
		//create tables
		prepareTables(viewId, viewName, entityTable.getTableName(), softRuleTables, hardEQRuleTables, hardNEQRuleTables);
		
		//generate graph
		Graph graph = new Graph(viewId, entityTable, softRuleTables, hardEQRuleTables, hardNEQRuleTables);
		graph.batchCluster();
		double reorganizeTime = graph.getClusterAndWriteTime();
		double totalSoftRuleWeight = graph.getTotalSoftRuleWeight();
		fillsCatalogTable(viewId, reorganizeTime, softRuleTables.size(), totalSoftRuleWeight);
		createIndicesTriggers(viewId, viewName, entityTable.getTableName(), softRuleTables, hardEQRuleTables, hardNEQRuleTables);
	}
}
