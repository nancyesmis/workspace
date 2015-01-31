package wisc.db.equivalence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import wisc.db.sql.DatabaseFactory;
import wisc.db.sql.QueryExecutor;
import wisc.db.sql.SQLConstants;

/**
 * Implements SQL-related calls such as inserts, updates, select queries etc.
 * for equivalence view. It also loads configuration parameters
 * 
 * {@link CreateEqView} class uses this class to execute SQL queries and
 * retrieve the results
 * 
 * @author koc
 * 
 */
public class EqViewSetup {
	/**
	 * QueryExecutor object is for database layer
	 */
	QueryExecutor executor;

	/**
	 * Default constructor get QueryExecutor from DatabaseFactory
	 */
	public EqViewSetup() {
		executor = DatabaseFactory.getQueryExecutor();
	}

	/**
	 * Loads database parameters from hazy configuration file
	 */
	public static void loadHazyConfParameters() {
		// load configuration parameters
		String hazyConfFileRelativePath = "../../../hazy.conf";
		BufferedReader reader;
		try {
			reader = new BufferedReader(
					new FileReader(hazyConfFileRelativePath));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] lineParts = line.split("=");
				if (lineParts[0].equals("POSTGRES_USER"))
					SQLConstants.USERNAME = lineParts[1];
				else if (lineParts[0].equals("POSTGRES_PASSWORD"))
					SQLConstants.PASSWORD = lineParts[1];
				else if (lineParts[0].equals("POSTGRES_DBNAME"))
					SQLConstants.DBNAME = lineParts[1];
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the largest view id max_view_id at catalog and returns
	 * max_view_id + 1
	 * 
	 * If there is no any view at the table, then it returns 0 as view_id
	 * 
	 * @return unique view id
	 */
	public int getUniqueViewId() {
		int viewId = 0;

		String query = "SELECT COUNT(*) FROM "
				+ EqViewSchema.EQ_VIEW_ID_ALL_CATALOGS_TABLE + ";";
		ResultSet rs = executor.executeSelectQuery(query);
		try {
			int count = 0;
			if (rs.next())
				count = rs.getInt(1);
			if (count > 0) {
				query = "SELECT MAX("
						+ EqViewSchema.EQ_V_C_ID_ALL_CATALOGS_ID_ATTR_NAME
						+ ") FROM "
						+ EqViewSchema.EQ_VIEW_ID_ALL_CATALOGS_TABLE + ";";
				rs = executor.executeSelectQuery(query);
				if (rs.next())
					viewId = rs.getInt(1) + 1;
			} else
				viewId = 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return viewId;
	}

	/**
	 * Inserts new created view id, view name and catalog name for that view to
	 * the general catalog table
	 * 
	 * @param viewId
	 *            id of the view
	 * @param viewName
	 *            name of the view
	 */
	public void insertIntoIdCatalog(int viewId, String viewName,
			String entityName) {
		String catalogName = EqViewSchema.getViewCatalogTableName(viewId);
		String idAlLCatalogsTableName = EqViewSchema
				.getIdAllCatalogsTableName();
		String query = "INSERT INTO " + idAlLCatalogsTableName + " VALUES("
				+ viewId + ", '" + viewName + "', '" + catalogName + "', '"
				+ entityName + "');";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Insert soft rule tables, weights and view id to the rule catalog table
	 * 
	 * @param viewId
	 *            id of the view
	 * @param softRuleTables
	 *            soft rule tables
	 */
	public void insertSoftTableIntoRulesIdWeightTab(int viewId,
			ArrayList<RuleTable> softRuleTables) {
		if (softRuleTables.size() > 0) {
			String rulesIdWeightTable = EqViewSchema
					.getRulesIdWeightTableName();
			String query = "INSERT INTO " + rulesIdWeightTable + " VALUES";
			for (int i = 0; i < softRuleTables.size(); i++)
				query += "(" + viewId + ", '"
						+ softRuleTables.get(i).getTableName() + "', "
						+ softRuleTables.get(i).getWeight() + "),";

			// remove last ,
			query = query.substring(0, query.length() - 1) + ";";
			executor.executeUpdateQuery(query);
		}
	}

	/**
	 * Insert hard rule tables, weights and view id to the rule catalog table
	 * 
	 * @param viewId
	 *            id of the view
	 * @param hardRuleTables
	 *            hard rule tables
	 * @param isHardEqRuleTable
	 *            whether hard rule table is hard eq or hard neq
	 */
	public void insertHardTableIntoRulesIdWeightTab(int viewId,
			ArrayList<RuleTable> hardRuleTables, boolean isHardEqRuleTable) {
		if (hardRuleTables.size() > 0) {
			String rulesIdWeightTable = EqViewSchema
					.getRulesIdWeightTableName();
			String weight = "";
			if (isHardEqRuleTable)
				weight = "'" + EqViewSchema.INFINITY + "'";
			else
				weight = "'" + EqViewSchema.MINUS_INFINITY + "'";

			String query = "INSERT INTO " + rulesIdWeightTable + " VALUES";
			for (int i = 0; i < hardRuleTables.size(); i++)
				query += "(" + viewId + ", '"
						+ hardRuleTables.get(i).getTableName() + "', " + weight
						+ "),";

			// remove last ,
			query = query.substring(0, query.length() - 1) + ";";
			executor.executeUpdateQuery(query);
		}
	}

	/**
	 * Returns view id and weight of the given rule table
	 * 
	 * @param ruleTableName
	 *            name of the rule table
	 * @return view id and weight
	 */
	public ArrayList<Object> getViewIdWeightFromRuleTableName(
			String ruleTableName) {
		String query = "SELECT "
				+ EqViewSchema.EQ_V_SOFT_RULES_WEIGHT_TABLE_VIEW_ID_ATTR_NAME
				+ ", "
				+ EqViewSchema.EQ_V_SOFT_RULES_WEIGHT_TABLE_SOFT_RULE_TABLE_WEIGHT_ATTR_NAME
				+ " FROM "
				+ EqViewSchema.getRulesIdWeightTableName()
				+ " WHERE "
				+ EqViewSchema.EQ_V_SOFT_RULES_WEIGHT_TABLE_SOFT_RULE_TABLE_ATTR_NAME
				+ " = '" + ruleTableName + "';";
		int viewId = 0;
		double weight = 0;

		ResultSet rs = executor.executeSelectQuery(query);
		try {
			if (rs.next()) {
				viewId = rs.getInt(1);
				weight = rs.getDouble(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ArrayList<Object> retList = new ArrayList<Object>();
		retList.add(viewId);
		retList.add(weight);

		return retList;
	}

	/**
	 * Create triggers on entity, soft, hard eq and hard neq rule tables
	 * 
	 * @param viewId
	 *            id of the view
	 * @param entityTableName
	 *            entity table name
	 * @param softRuleTables
	 *            list of soft rule tables
	 * @param hardEQRuleTables
	 *            list of hard eq rule tables
	 * @param hardNEQRuleTables
	 *            list of hard neq rule tables
	 */
	public void createTriggers(int viewId, String entityTableName,
			ArrayList<RuleTable> softRuleTables,
			ArrayList<RuleTable> hardEQRuleTables,
			ArrayList<RuleTable> hardNEQRuleTables) {

		createEntityTableTrigger(viewId, entityTableName);
		createSoftRuleTrigger(viewId, softRuleTables);
		createHardEQRuleTrigger(viewId, hardEQRuleTables);
		createHardNEQRuleTrigger(viewId, hardNEQRuleTables);
	}

	/**
	 * Create trigger on entity table name
	 * 
	 * @param viewId
	 *            id of the view
	 * @param entityTableName
	 *            name of the entity table
	 */
	private void createEntityTableTrigger(int viewId, String entityTableName) {
		String entityTriggerFunction = EqViewSchema.getEntityTriggerFunction();
		String entityTrigger = EqViewSchema.getEntityTrigger(viewId);
		createGenericTrigger(entityTrigger, entityTableName,
				entityTriggerFunction);
	}

	/**
	 * Create triggers on soft rule tables
	 * 
	 * @param viewId
	 *            id of the view
	 * @param softRuleTables
	 *            list of soft rule tables
	 */
	private void createSoftRuleTrigger(int viewId,
			ArrayList<RuleTable> softRuleTables) {
		String softRuleTriggerFunction = EqViewSchema
				.getSoftRuleTriggerFunction();

		for (int i = 0; i < softRuleTables.size(); i++) {
			String softRuleTrigger = EqViewSchema.getSoftRuleTrigger(viewId, i);
			String softRuleTable = softRuleTables.get(i).getTableName();

			createGenericTrigger(softRuleTrigger, softRuleTable,
					softRuleTriggerFunction);
		}
	}

	/**
	 * Create triggers on hard eq rule tables
	 * 
	 * @param viewId
	 *            id of the view
	 * @param hardEQRuleTables
	 *            list of hard eq rule tables
	 */
	private void createHardEQRuleTrigger(int viewId,
			ArrayList<RuleTable> hardEQRuleTables) {
		String hardEQRuleTriggerFunction = EqViewSchema
				.getHardEQRuleTriggerFunction();

		for (int i = 0; i < hardEQRuleTables.size(); i++) {
			String hardEQRuleTrigger = EqViewSchema.getHardEQRuleTrigger(
					viewId, i);
			String hardEQRuleTable = hardEQRuleTables.get(i).getTableName();

			createGenericTrigger(hardEQRuleTrigger, hardEQRuleTable,
					hardEQRuleTriggerFunction);
		}
	}

	/**
	 * Create triggers on hard neq rule tables
	 * 
	 * @param viewId
	 *            id of the view
	 * @param hardNEQRuleTables
	 *            list of hard neq rule tables
	 */
	private void createHardNEQRuleTrigger(int viewId,
			ArrayList<RuleTable> hardNEQRuleTables) {
		String hardNEQRuleTriggerFunction = EqViewSchema
				.getHardNEQRuleTriggerFunction();

		for (int i = 0; i < hardNEQRuleTables.size(); i++) {
			String hardNEQRuleTrigger = EqViewSchema.getHardNEQRuleTrigger(
					viewId, i);
			String hardNEQRuleTable = hardNEQRuleTables.get(i).getTableName();

			createGenericTrigger(hardNEQRuleTrigger, hardNEQRuleTable,
					hardNEQRuleTriggerFunction);
		}
	}

	/**
	 * This generic method creates trigger with the given trigger name, table
	 * name and trigger functon name
	 * 
	 * @param triggerName
	 *            name of the trigger
	 * @param tableName
	 *            name of the table that trigger will be created on
	 * @param triggerFunctionName
	 *            name of the trigger function
	 */
	private void createGenericTrigger(String triggerName, String tableName,
			String triggerFunctionName) {
		String query = "CREATE TRIGGER " + triggerName
				+ " AFTER INSERT OR UPDATE OR DELETE ON " + tableName
				+ " FOR EACH ROW EXECUTE PROCEDURE " + triggerFunctionName
				+ "();";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Creates indices on view, entity partitions, soft, hard and hard eq rule
	 * partitions table
	 * <p>
	 * Since other tables have same name convenience (BASE_NAME"_"view_id), only
	 * view name(encTableName) is given as a parameter
	 * 
	 * @param encTableName
	 *            view name
	 * @param viewId
	 *            id of the view
	 */
	public void createIndex(String encTableName, int viewId) {
		this.createEnPTableIndex(viewId);
		this.createEnCTableIndex(encTableName, viewId);
		this.createSoftRulePTableIndex(viewId);
		this.createHardEQRulePTableIndex(viewId);
		this.createHardNEQRulePTableIndex(viewId);
	}

	/**
	 * Creates index on entity partitions table
	 * 
	 * @param viewId
	 *            id of the view
	 */
	private void createEnPTableIndex(int viewId) {
		String enpTableName = EqViewSchema.getEPTableName(viewId);
		String enpIndexName = EqViewSchema.getEnPTableIndexName(viewId);
		String attrName = EqViewSchema.EP_PID_ATTR_NAME;

		createGenericIndex(enpIndexName, enpTableName, attrName);
	}

	/**
	 * Create index on view
	 * 
	 * @param encTableName
	 *            view name
	 * @param viewId
	 *            id of the view
	 */
	private void createEnCTableIndex(String encTableName, int viewId) {
		String encIndexName = EqViewSchema.getEnCTableIndexName(viewId);
		String attrName = EqViewSchema.EC_NODE_ATTR_NAME;

		createGenericIndex(encIndexName, encTableName, attrName);
	}

	/**
	 * Create index on soft rule partitions tables
	 * 
	 * @param viewId
	 *            id of the view
	 */
	private void createSoftRulePTableIndex(int viewId) {
		String softRulePTableName = EqViewSchema
				.getSoftRulePartTableName(viewId);
		String softRulePIndexName = EqViewSchema
				.getSoftRulePartTableIndexName(viewId);
		String attrNames = EqViewSchema.SOFT_R_P_PID1_ATTR_NAME + ", "
				+ EqViewSchema.SOFT_R_P_PID2_ATTR_NAME;

		createGenericIndex(softRulePIndexName, softRulePTableName, attrNames);
	}

	/**
	 * Create index on hard eq rule partitions tables
	 * 
	 * @param viewId
	 *            id of the view
	 */
	private void createHardEQRulePTableIndex(int viewId) {
		String hardEQRulePTableName = EqViewSchema
				.getHardEQRulePartTableName(viewId);
		String hardEQRulePIndexName = EqViewSchema
				.getHardEQRulePartTableIndexName(viewId);
		String attrName = EqViewSchema.H_EQ_R_P_PID_ATTR_NAME;

		createGenericIndex(hardEQRulePIndexName, hardEQRulePTableName, attrName);
	}

	/**
	 * Create index on hard neq rule partitions tables
	 * 
	 * @param viewId
	 *            id of the view
	 */
	private void createHardNEQRulePTableIndex(int viewId) {
		String hardNEQRulePTableName = EqViewSchema
				.getHardNEQRulePartTableName(viewId);
		String hardNEQRulePIndexName = EqViewSchema
				.getHardNEQRulePartTableIndexName(viewId);
		String attrNames = EqViewSchema.H_NEQ_R_P_PID1_ATTR_NAME + ", "
				+ EqViewSchema.H_NEQ_R_P_PID2_ATTR_NAME;

		createGenericIndex(hardNEQRulePIndexName, hardNEQRulePTableName,
				attrNames);
	}

	/**
	 * This generic method creates index with given name on the given table's
	 * given attributes
	 * 
	 * @param indexName
	 *            index name
	 * @param tableName
	 *            table name
	 * @param attrNames
	 *            attribute names
	 */
	private void createGenericIndex(String indexName, String tableName,
			String attrNames) {
		String query = this.getDropIndexQuery(indexName) + ";"
				+ "CREATE INDEX " + indexName + " ON " + tableName + "("
				+ attrNames + ");" + "CLUSTER " + indexName + " ON "
				+ tableName + ";";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Creates view catalog, entity partitions table, view table, soft, hard eq
	 * and hard neq rule partitions table
	 * <p>
	 * Since other tables have same name convenience (BASE_NAME"_"view_id), only
	 * view name(encTableName) is given as a table name parameter
	 * 
	 * @param viewId
	 *            id of the view
	 * @param viewName
	 *            view name
	 */
	public void createTables(int viewId, String viewName) {
		createCatalogTable(viewId);
		createEPTable(viewId);
		createECTable(viewId, viewName);
		createSoftRulePartitionsTable(viewId);
		createHardEQRulePartitionsTable(viewId);
		createHardNEQRulePartitionsTable(viewId);
	}

	/**
	 * Inserts view values to its catalog.
	 * <p>
	 * It inserts number of view id, update alg(hazy, default), number of soft
	 * rules, total soft rule weights, initial acc cost, reorganize time and
	 * tao.
	 * 
	 * @param viewId
	 *            id of the view
	 * @param reorganizeTime
	 *            reorganize time
	 * @param numSoftRule
	 *            number of soft rules
	 * @param totalSoftRuleWeight
	 *            total soft rule weight
	 */
	public void fillsCatalogTable(int viewId, double reorganizeTime,
			int numSoftRule, double totalSoftRuleWeight) {
		String tableName = EqViewSchema.getViewCatalogTableName(viewId);
		String query = "INSERT INTO " + tableName + " VALUES (" + viewId
				+ ", '" + EqViewConstants.HAZY_ALG + "', " + numSoftRule + ", "
				+ totalSoftRuleWeight + ", " + EqViewConstants.INITIAL_ACC_COST
				+ ", " + reorganizeTime + ", " + EqViewConstants.TAO + ");";
		executor.executeUpdateQuery(query);
	}

	/**
	 * Creates catalog table of the view
	 * 
	 * @param viewId
	 *            id of the view
	 */
	private void createCatalogTable(int viewId) {
		String tableName = EqViewSchema.getViewCatalogTableName(viewId);
		// this drop part should never happen, because id is unique
		String query = this.getDropTableQuery(tableName) + "CREATE TABLE "
				+ tableName + "(" + EqViewSchema.EQ_V_C_CATALOG_ID_ATTR_NAME
				+ " " + EqViewSchema.EQ_V_C_CATALOG_ID_ATTR_TYPE + ", "
				+ EqViewSchema.EQ_V_C_UPDATE_ALGORITHM_ATTR_NAME + " "
				+ EqViewSchema.EQ_V_C_UPDATE_ALGORITHM_ATTR_TYPE + ", "
				+ EqViewSchema.EQ_V_C_NUM_SOFT_RULE_ATTR_NAME + " "
				+ EqViewSchema.EQ_V_C_NUM_SOFT_RULE_ATTR_TYPE + ", "
				+ EqViewSchema.EQ_V_C_TOTAL_SOFT_RULE_WEIGHT_ATTR_NAME + " "
				+ EqViewSchema.EQ_V_C_TOTAL_SOFT_RULE_WEIGHT_ATTR_TYPE + ", "
				+ EqViewSchema.EQ_V_C_ACCUMULATED_COST_ATTR_NAME + " "
				+ EqViewSchema.EQ_V_C_ACCUMULATED_COST_ATTR_TYPE + ", "
				+ EqViewSchema.EQ_V_C_REORGANIZE_TIME_ATTR_NAME + " "
				+ EqViewSchema.EQ_V_C_REORGANIZE_TIME_ATTR_TYPE + ", "
				+ EqViewSchema.EQ_V_C_TAO_ATTR_NAME + " "
				+ EqViewSchema.EQ_V_C_TAO_ATTR_TYPE + ");";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Create entity partitions table of the view
	 * 
	 * @param viewId
	 *            id of the view
	 */
	private void createEPTable(int viewId) {
		String tableName = EqViewSchema.getEPTableName(viewId);
		String query = this.getDropTableQuery(tableName) + "CREATE TABLE "
				+ tableName + "(" + EqViewSchema.EP_PID_ATTR_NAME + " "
				+ EqViewSchema.EP_PID_ATTR_TYPE + ", "
				+ EqViewSchema.EP_NODE_ATTR_NAME + " "
				+ EqViewSchema.EP_NODE_ATTR_TYPE + ");";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Creates view table
	 * 
	 * @param viewId
	 *            id of the view
	 * @param viewName
	 *            name of the view
	 */
	private void createECTable(int viewId, String viewName) {
		String query = this.getDropTableQuery(viewName) + "CREATE TABLE "
				+ viewName + "(" + EqViewSchema.EC_NODE_ATTR_NAME + " "
				+ EqViewSchema.EC_NODE_ATTR_TYPE + ", "
				+ EqViewSchema.EC_CID_ATTR_NAME + " "
				+ EqViewSchema.EC_CID_ATTR_TYPE + ")";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Creates soft rule partitions table
	 * 
	 * @param viewId
	 *            id of the view
	 */
	private void createSoftRulePartitionsTable(int viewId) {
		String tableName = EqViewSchema.getSoftRulePartTableName(viewId);
		String query = this.getDropTableQuery(tableName) + "CREATE TABLE "
				+ tableName + "(" + EqViewSchema.SOFT_R_P_PID1_ATTR_NAME + " "
				+ EqViewSchema.SOFT_R_P_PID1_ATTR_TYPE + ", "
				+ EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME + " "
				+ EqViewSchema.SOFT_R_P_NODE1_ATTR_TYPE + ", "
				+ EqViewSchema.SOFT_R_P_PID2_ATTR_NAME + " "
				+ EqViewSchema.SOFT_R_P_PID2_ATTR_TYPE + ", "
				+ EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME + " "
				+ EqViewSchema.SOFT_R_P_NODE2_ATTR_TYPE + ", "
				+ EqViewSchema.SOFT_R_P_WEIGHT_ATTR_NAME + " "
				+ EqViewSchema.SOFT_R_P_WEIGHT_ATTR_TYPE + ");";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Creates hard eq rule partitions table
	 * 
	 * @param viewId
	 *            id of the view
	 */
	private void createHardEQRulePartitionsTable(int viewId) {
		String tableName = EqViewSchema.getHardEQRulePartTableName(viewId);
		String query = this.getDropTableQuery(tableName) + "CREATE TABLE "
				+ tableName + "(" + EqViewSchema.H_EQ_R_P_PID_ATTR_NAME + " "
				+ EqViewSchema.H_EQ_R_P_PID_ATTR_TYPE + ", "
				+ EqViewSchema.H_EQ_R_P_NODE1_ATTR_NAME + " "
				+ EqViewSchema.H_EQ_R_P_NODE1_ATTR_TYPE + ", "
				+ EqViewSchema.H_EQ_R_P_NODE2_ATTR_NAME + " "
				+ EqViewSchema.H_EQ_R_P_NODE2_ATTR_TYPE + ");";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Creates hard neq rule partitions table
	 * 
	 * @param viewId
	 *            id of the view
	 */
	private void createHardNEQRulePartitionsTable(int viewId) {
		String tableName = EqViewSchema.getHardNEQRulePartTableName(viewId);
		String query = this.getDropTableQuery(tableName) + "CREATE TABLE "
				+ tableName + "(" + EqViewSchema.H_NEQ_R_P_PID1_ATTR_NAME + " "
				+ EqViewSchema.H_NEQ_R_P_PID1_ATTR_TYPE + ", "
				+ EqViewSchema.H_NEQ_R_P_NODE1_ATTR_NAME + " "
				+ EqViewSchema.H_NEQ_R_P_NODE1_ATTR_TYPE + ", "
				+ EqViewSchema.H_NEQ_R_P_PID2_ATTR_NAME + " "
				+ EqViewSchema.H_NEQ_R_P_PID2_ATTR_TYPE + ", "
				+ EqViewSchema.H_NEQ_R_P_NODE2_ATTR_NAME + " "
				+ EqViewSchema.H_NEQ_R_P_NODE2_ATTR_TYPE + ");";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Create temporary partitions table from on-memory partitions
	 * 
	 * @param viewId
	 *            id of the view
	 */
	public void createTempPartitionsTable(int viewId) {
		String tempPartitionsTableName = EqViewSchema
				.getTempPartitionsTableName(viewId);
		String query = this.getDropTableQuery(tempPartitionsTableName)
				+ "CREATE TEMPORARY TABLE " + tempPartitionsTableName + "("
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_NEW_PARTITION_ATTR_NAME
				+ " "
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_NEW_PARTITION_ATTR_TYPE
				+ ", "
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_OLD_PARTITION_ATTR_NAME
				+ " "
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_OLD_PARTITION_ATTR_TYPE
				+ ");";
		executor.executeUpdateQuery(query);
	}

	/**
	 * Creates drop table query with the given table name
	 * 
	 * @param tableName
	 *            name of the table
	 * @return drop table query
	 */
	private String getDropTableQuery(String tableName) {
		return getDropQuery("TABLE", tableName);
	}

	/**
	 * Creates drop index query with the given index name
	 * 
	 * @param indexName
	 *            name of the index
	 * @return drop index query
	 */
	private String getDropIndexQuery(String indexName) {
		return getDropQuery("INDEX", indexName);
	}

	/**
	 * Creates drop query with the given drop item and it name
	 * 
	 * @param dropItem
	 *            table or index
	 * @param tableName
	 *            table or index name
	 * @return
	 */
	private String getDropQuery(String dropItem, String itemName) {
		return "DROP " + dropItem + " IF EXISTS " + itemName + ";";
	}

	/**
	 * Retrieves update alg, num soft rule, total soft rule weight, acc,
	 * reorganize time and tao from the catalog of the view
	 * 
	 * @param viewId
	 *            id of the view
	 * @return CatalogAttributeValues
	 */
	public CatalogAttributeValues getCatalogValues(int viewId) {
		String catalogTable = EqViewSchema.getViewCatalogTableName(viewId);
		String query = "SELECT "
				+ EqViewSchema.EQ_V_C_UPDATE_ALGORITHM_ATTR_NAME + ", "
				+ EqViewSchema.EQ_V_C_NUM_SOFT_RULE_ATTR_NAME + ", "
				+ EqViewSchema.EQ_V_C_TOTAL_SOFT_RULE_WEIGHT_ATTR_NAME + ", "
				+ EqViewSchema.EQ_V_C_ACCUMULATED_COST_ATTR_NAME + ", "
				+ EqViewSchema.EQ_V_C_REORGANIZE_TIME_ATTR_NAME + ", "
				+ EqViewSchema.EQ_V_C_TAO_ATTR_NAME + " FROM " + catalogTable
				+ ";";

		ResultSet rs = executor.executeSelectQuery(query);
		String updateAlg = "";
		int numSoftRule = 0;
		double totalSoftRuleWeight = 0;
		double acc = 0;
		double reorganizeTime = 0;
		double tao = 0;

		try {
			if (rs.next()) {
				updateAlg = rs.getString(1);
				numSoftRule = rs.getInt(2);
				totalSoftRuleWeight = rs.getDouble(3);
				acc = rs.getDouble(4);
				reorganizeTime = rs.getDouble(5);
				tao = rs.getDouble(6);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new CatalogAttributeValues(updateAlg, numSoftRule,
				totalSoftRuleWeight, acc, reorganizeTime, tao);
	}

	/**
	 * Updates catalog table with given acc and reorganize time values
	 * 
	 * @param viewId
	 * @param acc
	 * @param reorganizeTime
	 */
	public void updateCatalogTable(int viewId, double acc, double reorganizeTime) {
		String catalogTable = EqViewSchema.getViewCatalogTableName(viewId);
		String query = "UPDATE " + catalogTable + " SET "
				+ EqViewSchema.EQ_V_C_ACCUMULATED_COST_ATTR_NAME + " = " + acc
				+ ", " + EqViewSchema.EQ_V_C_REORGANIZE_TIME_ATTR_NAME + " = "
				+ reorganizeTime + ";";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Updates view table
	 * 
	 * @param enC
	 *            view table name
	 * @param tempC
	 *            temporary view that contains latest entity id - cluster id
	 *            pairs
	 */
	public void updateEntityClustersTable(String enC, String tempC) {
		String query = "UPDATE " + enC + " SET "
				+ EqViewSchema.EC_CID_ATTR_NAME + " = "
				+ EqViewSchema.TEMP_CLUSTERS_TABLE_CID_ATTR_NAME + " FROM "
				+ tempC + " WHERE " + EqViewSchema.EC_NODE_ATTR_NAME + " = "
				+ EqViewSchema.TEMP_CLUSTERS_TABLE_NODE_ATTR_NAME + ";";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Retrieves maximum partition id from the entity partitions table
	 * 
	 * @param viewId
	 *            id of the view
	 * @return max pid
	 */
	public int getMaxPid(int viewId) {
		String enP = EqViewSchema.getEPTableName(viewId);
		String query = "SELECT max(" + EqViewSchema.EP_PID_ATTR_NAME
				+ ") FROM " + enP;

		ResultSet rs = executor.executeSelectQuery(query);
		int maxPid = -1;

		try {
			if (rs.next())
				maxPid = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxPid;
	}

	/**
	 * Retrieves pid of a node from entity partitions table
	 * 
	 * @param viewId
	 *            id of the view
	 * @param node
	 *            entity id
	 * @return pid of node
	 */
	public int getPidForNode(int viewId, int node) {
		String enP = EqViewSchema.getEPTableName(viewId);
		String query = "SELECT " + EqViewSchema.EP_PID_ATTR_NAME + " FROM "
				+ enP + " WHERE " + EqViewSchema.EP_NODE_ATTR_NAME + " = "
				+ node;
		int pid = -1;

		ResultSet rs = executor.executeSelectQuery(query);
		try {
			if (rs.next())
				pid = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pid;
	}

	/**
	 * Retrieves view name from general catalog name
	 * 
	 * @param viewId
	 *            id of the view
	 * @return view name
	 */
	public String getViewName(int viewId) {
		String query = "SELECT "
				+ EqViewSchema.EQ_V_C_ID_ALL_CATALOGS_VIEW_NAME_ATTR_NAME
				+ " FROM " + EqViewSchema.getIdAllCatalogsTableName()
				+ " WHERE " + EqViewSchema.EQ_V_C_ID_ALL_CATALOGS_ID_ATTR_NAME
				+ " = " + viewId + ";";

		String viewName = "";
		ResultSet rs = executor.executeSelectQuery(query);
		try {
			if (rs.next())
				viewName = rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return viewName;
	}

	/**
	 * Retrieves weight of soft rule from rule catalog table
	 * 
	 * @param viewId
	 *            view id
	 * @param softRuleTableName
	 *            soft rule table name
	 * @return weight of the soft rule
	 */
	public double getSoftRuleTableWeight(int viewId, String softRuleTableName) {
		double weight = 0;
		String query = "SELECT "
				+ EqViewSchema.EQ_V_SOFT_RULES_WEIGHT_TABLE_SOFT_RULE_TABLE_WEIGHT_ATTR_NAME
				+ " FROM "
				+ EqViewSchema.getRulesIdWeightTableName()
				+ " WHERE "
				+ EqViewSchema.EQ_V_SOFT_RULES_WEIGHT_TABLE_SOFT_RULE_TABLE_ATTR_NAME
				+ " = '" + softRuleTableName + "' AND "
				+ EqViewSchema.EQ_V_SOFT_RULES_WEIGHT_TABLE_VIEW_ID_ATTR_NAME
				+ "= " + viewId + ";";

		ResultSet rs = executor.executeSelectQuery(query);
		try {
			if (rs.next())
				weight = rs.getDouble(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return weight;
	}

	/**
	 * Retrieves weight of an edge before update. If same edge comes from
	 * another soft rule table, then weight of this edge increases This method
	 * is used when update alg is Hazy (we have partitions in that alg)
	 * 
	 * @param viewId
	 *            id of the view
	 * @param pid1
	 *            pid of src of edge
	 * @param src
	 *            src of edge
	 * @param pid2
	 *            pid of dst of edge
	 * @param dst
	 *            dst of edge
	 * @return weight
	 */
	public double getInitialSoftEdgeWeight(int viewId, int pid1, int src,
			int pid2, int dst) {
		String softRulePartTableName = EqViewSchema
				.getSoftRulePartTableName(viewId);
		double initialWeight = 0;

		String query = "SELECT " + EqViewSchema.SOFT_R_P_WEIGHT_ATTR_NAME
				+ " FROM " + softRulePartTableName + " WHERE "
				+ EqViewSchema.SOFT_R_P_PID1_ATTR_NAME + " = " + pid1 + " AND "
				+ EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME + " = " + src + " AND "
				+ EqViewSchema.SOFT_R_P_PID2_ATTR_NAME + " = " + pid2 + " AND "
				+ EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME + " = " + dst + ";";

		ResultSet rs = executor.executeSelectQuery(query);
		try {
			if (rs.next())
				initialWeight = rs.getDouble(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return initialWeight;
	}

	/**
	 * Retrieves weight of an edge before update. If same edge comes from
	 * another soft rule table, then weight of this edge increases This method
	 * is used when update alg is recluster
	 * 
	 * @param viewId
	 *            id of the view
	 * @param src
	 *            src of edge
	 * @param dst
	 *            dst of edge
	 * @return weight
	 */
	public double getInitialSoftEdgeWeight(int viewId, int src, int dst) {
		String softRulePartTableName = EqViewSchema
				.getSoftRulePartTableName(viewId);
		double initialWeight = 0;

		String query = "SELECT " + EqViewSchema.SOFT_R_P_WEIGHT_ATTR_NAME
				+ " FROM " + softRulePartTableName + " WHERE "
				+ EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME + " = " + src + " AND "
				+ EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME + " = " + dst + ";";

		ResultSet rs = executor.executeSelectQuery(query);
		try {
			if (rs.next())
				initialWeight = rs.getDouble(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return initialWeight;
	}

	/**
	 * Inserts soft update edge to the soft rule partitions table.
	 * 
	 * @param viewId
	 *            id of the view
	 * @param pid1
	 *            pid of src of edge
	 * @param pid2
	 *            pid of dst of edge
	 * @param src
	 *            src of edge
	 * @param dst
	 *            dst of edge
	 * @param newWeight
	 *            new weight of the edge
	 */
	public void insertSoftUpdateEdge(int viewId, int pid1, int pid2, int src,
			int dst, double newWeight) {
		String softRulePartTable = EqViewSchema
				.getSoftRulePartTableName(viewId);
		String query = "INSERT INTO " + softRulePartTable + " VALUES(" + pid1
				+ "," + src + "," + pid2 + "," + dst + ", " + newWeight + ");";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Updates soft edge's weight on the soft rule partitions table and returns
	 * how many edge is updated. If it returns 1, then it is updated, else it
	 * needs to be inserted
	 * 
	 * @param viewId
	 *            id of the view
	 * @param pid1
	 *            pid of src of edge
	 * @param pid2
	 *            pid of dst of edge
	 * @param src
	 *            src of edge
	 * @param dst
	 *            dst of edge
	 * @param newWeight
	 *            new weight of edge
	 * @return whether edge exists or not
	 */
	public int updateWeightOfSoftUpdateEdge(int viewId, int pid1, int pid2,
			int src, int dst, double newWeight) {
		String softRulePartTable = EqViewSchema
				.getSoftRulePartTableName(viewId);
		String query = "UPDATE " + softRulePartTable + " SET "
				+ EqViewSchema.SOFT_R_P_WEIGHT_ATTR_NAME + " = " + newWeight
				+ " WHERE " + EqViewSchema.SOFT_R_P_PID1_ATTR_NAME + " = "
				+ pid1 + " AND " + EqViewSchema.SOFT_R_P_PID2_ATTR_NAME + " = "
				+ pid2 + " AND " + EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME
				+ " = " + src + " AND " + EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME
				+ " = " + dst + ";";
		return executor.executeUpdateQuery(query);
	}

	/**
	 * Inserts hard eq update edge to the hard eq rule partitions table
	 * 
	 * @param viewId
	 *            id of the view
	 * @param pid
	 *            pid of both src and dst
	 * @param src
	 *            src of edge
	 * @param dst
	 *            dst of edge
	 */
	public void insertHardEQUpdateEdge(int viewId, int pid, int src, int dst) {
		String hardEQRulePartTable = EqViewSchema
				.getHardEQRulePartTableName(viewId);
		String query = "INSERT INTO " + hardEQRulePartTable + " VALUES(" + pid
				+ "," + src + "," + dst + ");";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Inserts hard neq update edge to the hard neq rule partitions table
	 * 
	 * @param viewId
	 *            id of the view
	 * @param pid1
	 *            pid of src of edge
	 * @param pid2
	 *            pid of dst of edge
	 * @param src
	 *            src of edge
	 * @param dst
	 *            dst of edge
	 */
	public void insertHardNEQUpdateEdge(int viewId, int pid1, int pid2,
			int src, int dst) {
		String hardNEQRulePartTable = EqViewSchema
				.getHardNEQRulePartTableName(viewId);
		String query = "INSERT INTO " + hardNEQRulePartTable + " VALUES("
				+ pid1 + "," + src + "," + pid2 + "," + dst + ");";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Copy file content to entity partition table with bulk-loading
	 * 
	 * @param viewId
	 *            id of the view
	 * @param filePath
	 */
	public void copyFromFileToEnPTable(int viewId, String filePath) {
		String enPTable = EqViewSchema.getEPTableName(viewId);
		copyFromFileToTable(enPTable, filePath);
	}

	/**
	 * Copy file content to soft rule partition table with bulk-loading
	 * 
	 * @param viewId
	 *            id of the view
	 * @param filePath
	 *            path of the file
	 */
	public void copyFromFileToSoftRulePartTable(int viewId, String filePath) {
		String softRulePartTable = EqViewSchema
				.getSoftRulePartTableName(viewId);
		copyFromFileToTable(softRulePartTable, filePath);
	}

	/**
	 * Copy file content to hard eq rule partition table with bulk-loading
	 * 
	 * @param viewId
	 *            id of the view
	 * @param filePath
	 *            path of the file
	 */
	public void copyFromFileToHardEQRulePartTable(int viewId, String filePath) {
		String hardEQRulePartTable = EqViewSchema
				.getHardEQRulePartTableName(viewId);
		copyFromFileToTable(hardEQRulePartTable, filePath);
	}

	/**
	 * Copy file content to hard neq rule partition table with bulk-loading
	 * 
	 * @param viewId
	 *            id of the view
	 * @param filePath
	 *            path of the file
	 */
	public void copyFromFileToHardNEQRulePartTable(int viewId, String filePath) {
		String hardNEQRulePartTable = EqViewSchema
				.getHardNEQRulePartTableName(viewId);
		copyFromFileToTable(hardNEQRulePartTable, filePath);
	}

	/**
	 * Copy file content to temporary entity partition table with bulk-loading
	 * 
	 * @param viewId
	 *            id of the view
	 * @param filePath
	 *            path of the file
	 */
	public void copyFromFileToTempPartTable(int viewId, String filePath) {
		String tempPartitionsTableName = EqViewSchema
				.getTempPartitionsTableName(viewId);
		copyFromFileToTable(tempPartitionsTableName, filePath);
	}

	/**
	 * Copy file content to a given table name with bulk-loading
	 * 
	 * @param tableName
	 * @param filePath
	 */
	public void copyFromFileToTable(String tableName, String filePath) {
		String query = "COPY " + tableName + " FROM '" + filePath
				+ "' DELIMITER '" + EqViewSchema.DELIMITER_FOR_COPYING + "'";
		executor.executeUpdateQuery(query);
	}

	/**
	 * Updates partitions of entity partition table from on-memory partitions
	 * (temp partition table)
	 * 
	 * @param viewId
	 *            id of the view
	 * @param tableName
	 *            name of the table to be updated
	 * @param indexName
	 *            index name of the table to be updated
	 */
	private void forceEnPTableUpdate(int viewId, String tableName,
			String indexName) {
		String query = "DROP INDEX IF EXISTS " + indexName + ";";
		executor.executeUpdateQuery(query);

		String tempPartitionsTableName = EqViewSchema
				.getTempPartitionsTableName(viewId);
		query = "UPDATE " + tableName + " SET " + EqViewSchema.PID_ATTR + " = "
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_NEW_PARTITION_ATTR_NAME
				+ " FROM " + tempPartitionsTableName + " WHERE "
				+ EqViewSchema.PID_ATTR + " = "
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_OLD_PARTITION_ATTR_NAME
				+ ";";
		executor.executeUpdateQuery(query);
		query = "CREATE INDEX " + indexName + " ON " + tableName + "("
				+ EqViewSchema.PID_ATTR + "," + EqViewSchema.NODE_ATTR + ")";
		executor.executeUpdateQuery(query);
	}

	/**
	 * Updates partitions of heq partition table with one pid from on-memory
	 * partitions (temp partition table)
	 * 
	 * @param viewId
	 *            id of the view
	 * @param tableName
	 *            name of the table to be updated
	 * @param indexName
	 *            index name of the table to be updated
	 */
	private void forceHEQPTableUpdate(int viewId, String tableName,
			String indexName) {
		String query = "DROP INDEX IF EXISTS " + indexName + ";";
		executor.executeUpdateQuery(query);

		String tempPartitionsTableName = EqViewSchema
				.getTempPartitionsTableName(viewId);
		query = "UPDATE " + tableName + " SET " + EqViewSchema.PID_ATTR + " = "
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_NEW_PARTITION_ATTR_NAME
				+ " FROM " + tempPartitionsTableName + " WHERE "
				+ EqViewSchema.PID_ATTR + " = "
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_OLD_PARTITION_ATTR_NAME
				+ ";";
		executor.executeUpdateQuery(query);
		query = "CREATE INDEX " + indexName + " ON " + tableName + "("
				+ EqViewSchema.PID_ATTR + ")";
		executor.executeUpdateQuery(query);
	}

	/**
	 * Updates partitions of table with two pids (e.g. soft rule table) from
	 * on-memory partitions (temp partition table)
	 * 
	 * @param viewId
	 *            id of the view
	 * @param tableName
	 *            name of the table to be updated
	 * @param indexName
	 *            index name of the table to be updated
	 */
	private void forceTableUpdateWithTwoPids(int viewId, String tableName,
			String indexName) {
		String query = "DROP INDEX IF EXISTS " + indexName + ";";
		executor.executeUpdateQuery(query);

		String tempPartitionsTableName = EqViewSchema
				.getTempPartitionsTableName(viewId);
		query = "UPDATE " + tableName + " SET " + EqViewSchema.PID1_ATTR
				+ " = "
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_NEW_PARTITION_ATTR_NAME
				+ " FROM " + tempPartitionsTableName + " WHERE "
				+ EqViewSchema.PID1_ATTR + " = "
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_OLD_PARTITION_ATTR_NAME
				+ ";";
		executor.executeUpdateQuery(query);
		query = "UPDATE " + tableName + " SET " + EqViewSchema.PID2_ATTR
				+ " = "
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_NEW_PARTITION_ATTR_NAME
				+ " FROM " + tempPartitionsTableName + " WHERE "
				+ EqViewSchema.PID2_ATTR + " = "
				+ EqViewSchema.TEMP_PARTITIONS_TABLE_OLD_PARTITION_ATTR_NAME
				+ ";";
		executor.executeUpdateQuery(query);
		query = "CREATE INDEX " + indexName + " ON " + tableName + "("
				+ EqViewSchema.PID1_ATTR + ", " + EqViewSchema.PID2_ATTR + ")";
		executor.executeUpdateQuery(query);
	}

	/**
	 * Updates partitions of entity partition table of the view with given id
	 * 
	 * @param viewId
	 *            id of the view
	 */
	public void forceEnPTableUpdate(int viewId) {
		String tableName = EqViewSchema.getEPTableName(viewId);
		String indexName = EqViewSchema.getEnPTableIndexName(viewId);
		forceEnPTableUpdate(viewId, tableName, indexName);
	}

	/**
	 * Updates partitions of soft rule partition table of the view with given id
	 * 
	 * @param viewId
	 *            id of the view
	 */
	public void forceSoftRulePartTableUpdate(int viewId) {
		String tableName = EqViewSchema.getSoftRulePartTableName(viewId);
		String indexName = EqViewSchema.getSoftRulePartTableIndexName(viewId);
		forceTableUpdateWithTwoPids(viewId, tableName, indexName);
	}

	/**
	 * Updates partitions of hard eq rule partition table of the view with given
	 * id
	 * 
	 * @param viewId
	 *            id of the view
	 */
	public void forceHardEQRulePartTableUpdate(int viewId) {
		String tableName = EqViewSchema.getHardEQRulePartTableName(viewId);
		String indexName = EqViewSchema.getHardEQRulePartTableIndexName(viewId);
		forceHEQPTableUpdate(viewId, tableName, indexName);
	}

	/**
	 * Updates partitions of hard neq rule partition table of the view with
	 * given id
	 * 
	 * @param viewId
	 *            id of the view
	 */
	public void forceHardNEQRulePartTableUpdate(int viewId) {
		String tableName = EqViewSchema.getHardNEQRulePartTableName(viewId);
		String indexName = EqViewSchema
				.getHardNEQRulePartTableIndexName(viewId);
		forceTableUpdateWithTwoPids(viewId, tableName, indexName);
	}

	/**
	 * This method creates mapping objects between database vertex ids and Graph
	 * ids
	 * <p>
	 * It returns list of mapping between database vertex ids and graph ids, and
	 * list of database vertex ids
	 * 
	 * @param query
	 *            This query is retrieving list of database vertex ids
	 * @return list of mapping and database vertex ids list
	 */
	public ArrayList<Object> getMapping(String query) {
		ArrayList<Object> output = new ArrayList<Object>();
		ArrayList<Integer> mapping = new ArrayList<Integer>();
		LinkedHashMap<Integer, Integer> backMapping = new LinkedHashMap<Integer, Integer>();

		ResultSet rs = executor.executeSelectQuery(query);
		try {
			while (rs.next()) {
				int id = rs.getInt(1);
				int idMap = mapping.size();
				mapping.add(id);
				backMapping.put(id, idMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		output.add(mapping);
		output.add(backMapping);
		return output;
	}

	/**
	 * Creates query from the given view id and calls getMapping method
	 * 
	 * @param viewId
	 *            id of the view
	 * @return list of mapping and database vertex ids list
	 */
	public ArrayList<Object> getMappingFromEntityTable(int viewId) {
		String entityPartTable = EqViewSchema.getEPTableName(viewId);
		String query = "SELECT " + EqViewSchema.EP_NODE_ATTR_NAME + " FROM "
				+ entityPartTable + " ORDER BY "
				+ EqViewSchema.EP_NODE_ATTR_NAME + ";";
		return getMapping(query);
	}

	/**
	 * This method updates mapping objects with query
	 * 
	 * @param query
	 *            This query is retrieving list of database vertex ids
	 * @param mapping
	 *            This is list of database vertex ids
	 * @param backMapping
	 *            This is mapping between database vertex ids and graph vertex
	 *            ids
	 */
	public void getMappingIncrementally(int viewId, int pid,
			ArrayList<Integer> mapping,
			LinkedHashMap<Integer, Integer> backMapping) {
		String enPTableName = EqViewSchema.getEPTableName(viewId);
		String query = "SELECT DISTINCT(" + EqViewSchema.EP_NODE_ATTR_NAME
				+ ") FROM " + enPTableName + " WHERE "
				+ EqViewSchema.EP_PID_ATTR_NAME + " = " + pid;
		ResultSet rs = executor.executeSelectQuery(query);
		try {
			while (rs.next()) {
				int id = rs.getInt(1);
				int idMap = mapping.size();
				mapping.add(id);
				backMapping.put(id, idMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method loads hard eq and hard neq into graph object according to the
	 * query
	 * <p>
	 * With mapping object(backMapping), vertex ids in edges are converted into
	 * graph vertex ids during the loading
	 * 
	 * @param query
	 *            This retrieves hard eq or hard neq edges from the database
	 * @param backMapping
	 *            This is mapping between database vertex ids and graph vertex
	 *            ids
	 * @return hard eq or hard neq mapping that maps edge to count of the
	 *         occurrences of the edge
	 */
	public LinkedHashMap<Edge, Integer> generateHardPosNegEdges(
			ArrayList<RuleTable> query,
			LinkedHashMap<Integer, Integer> backMapping) {
		LinkedHashMap<Edge, Integer> hardPosNegEdges = new LinkedHashMap<Edge, Integer>();
		for (int i = 0; i < query.size(); i++) {
			if (!query.get(i).equals("")) {
				ResultSet rs = executor.executeSelectQuery(query.get(i)
						.getSelectQuery());
				try {
					while (rs.next()) {
						int src = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_SRC_ATTR));
						int dst = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_DST_ATTR));
						Edge edge = new Edge(src, dst);
						if (hardPosNegEdges.containsKey(edge)) {
							Integer count = new Integer(hardPosNegEdges.get(
									edge).intValue() + 1);
							hardPosNegEdges.put(edge, count);
						} else {
							hardPosNegEdges.put(edge, new Integer("1"));
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return hardPosNegEdges;
	}

	/**
	 * This method loads soft edges into memory from the database
	 * <p>
	 * With mapping object(backMapping), database vertex ids in edges are also
	 * converted into graph vertex ids during the loading
	 * 
	 * @param query
	 *            This retrieves soft edges from the database
	 * @param backMapping
	 *            This is mapping between database vertex ids and graph vertex
	 *            ids
	 * @param multipleRules
	 *            This is whether graph contains multiple rules or not (for
	 *            efficiency)
	 * @return soft edge mapping that maps edge to count of the occurrences of
	 *         the edge
	 */
	public LinkedHashMap<Edge, Double> generateSoftEdges(
			ArrayList<RuleTable> query,
			LinkedHashMap<Integer, Integer> backMapping, boolean multipleRules) {
		LinkedHashMap<Edge, Double> softEdges = new LinkedHashMap<Edge, Double>();

		for (int i = 0; i < query.size(); i++) {
			ResultSet rs = executor.executeSelectQuery(query.get(i)
					.getSelectQuery());

			try {
				while (rs.next()) {
					int src = backMapping.get(rs
							.getInt(EqViewSchema.ID_OF_SRC_ATTR));
					int dst = backMapping.get(rs
							.getInt(EqViewSchema.ID_OF_DST_ATTR));
					Edge edge = new Edge(src, dst);

					// this is for efficiency. If number of rules are not
					// multiple, then we do not need to have weight of that edge
					if (multipleRules) {
						Double num = 0.0;
						if (softEdges.containsKey(edge))
							num = new Double(softEdges.get(edge).doubleValue()
									+ query.get(i).getWeight());
						else
							num = new Double(query.get(i).getWeight());

						softEdges.put(edge, num);
					} else
						softEdges.put(edge, new Double("1")); // weight never
					// change if not
					// multiple
					// rules
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return softEdges;
	}

	/**
	 * Load list of soft edges from the database
	 * <p>
	 * With mapping object(backMapping), database vertex ids in edges are also
	 * converted into graph vertex ids during the loading
	 * 
	 * @param viewId
	 *            id of the view
	 * @param backMapping
	 *            This is mapping between database vertex ids and graph vertex
	 *            ids
	 * @param multipleSoftRule
	 *            number of soft rule > 1 or not
	 * @param totalWeightOfSoftRules
	 *            total weight of soft rules
	 * @return list of soft edges
	 */
	public LinkedHashMap<Edge, Double> generateSoftEdges(int viewId,
			LinkedHashMap<Integer, Integer> backMapping,
			boolean multipleSoftRule, double totalWeightOfSoftRules) {
		LinkedHashMap<Edge, Double> softEdges = new LinkedHashMap<Edge, Double>();

		String softRulePartTable = EqViewSchema
				.getSoftRulePartTableName(viewId);
		String query = "";
		if (multipleSoftRule) {
			double threshold = totalWeightOfSoftRules / 2.0;
			query = "SELECT " + EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME + ", "
					+ EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME + " FROM "
					+ softRulePartTable + " WHERE "
					+ EqViewSchema.SOFT_R_P_WEIGHT_ATTR_NAME + " >= "
					+ threshold + " ORDER BY "
					+ EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME + ", "
					+ EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME + ";";
		}
		// if there is no multiple soft rule, then we don't need to put
		// constaints on the edges where weight >= EDGE_VOTE_THRESHOLD
		else
			query = "SELECT " + EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME + ", "
					+ EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME + " FROM "
					+ softRulePartTable + " ORDER BY "
					+ EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME + ", "
					+ EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME + ";";

		if (!query.equals("")) {
			ResultSet rs = executor.executeSelectQuery(query);
			try {
				while (rs.next()) {
					if (backMapping.containsKey(rs
							.getInt(EqViewSchema.ID_OF_SRC_ATTR))
							&& backMapping.containsKey(rs
									.getInt(EqViewSchema.ID_OF_DST_ATTR))) {
						int src = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_SRC_ATTR));
						int dst = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_DST_ATTR));
						Edge edge = new Edge(Math.min(src, dst), Math.max(src,
								dst));
						if (softEdges.containsKey(edge)) {
							Double count = new Double(softEdges.get(edge)
									.intValue() + 1);
							softEdges.put(edge, count);
						} else
							softEdges.put(edge, new Double("1"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return softEdges;
	}

	/**
	 * Load list of hard eq or hard neq edges from the database
	 * <p>
	 * With mapping object(backMapping), database vertex ids in edges are also
	 * converted into graph vertex ids during the loading
	 * 
	 * @param viewId
	 *            id of the view
	 * @param backMappingThis
	 *            is mapping between database vertex ids and graph vertex ids
	 * @param hardEQRule
	 *            whether rule is hard eq or not
	 * @return list of hard eq or hard neq edges
	 */
	public LinkedHashMap<Edge, Integer> generateHardPosNegEdges(int viewId,
			LinkedHashMap<Integer, Integer> backMapping, boolean hardEQRule) {
		LinkedHashMap<Edge, Integer> hardPosNegEdges = new LinkedHashMap<Edge, Integer>();
		String tableName = "";

		if (hardEQRule)
			tableName = EqViewSchema.getHardEQRulePartTableName(viewId);
		else
			tableName = EqViewSchema.getHardNEQRulePartTableName(viewId);

		String query = "SELECT " + EqViewSchema.NODE1_ATTR + ", "
				+ EqViewSchema.NODE2_ATTR + " FROM " + tableName + " ORDER BY "
				+ EqViewSchema.NODE1_ATTR + ", " + EqViewSchema.NODE2_ATTR
				+ ";";

		if (!query.equals("")) {
			ResultSet rs = executor.executeSelectQuery(query);
			try {
				while (rs.next()) {
					if (backMapping.containsKey(rs
							.getInt(EqViewSchema.ID_OF_SRC_ATTR))
							&& backMapping.containsKey(rs
									.getInt(EqViewSchema.ID_OF_DST_ATTR))) {
						int src = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_SRC_ATTR));
						int dst = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_DST_ATTR));
						Edge edge = new Edge(Math.min(src, dst), Math.max(src,
								dst));
						if (hardPosNegEdges.containsKey(edge)) {
							Integer count = new Integer(hardPosNegEdges.get(
									edge).intValue() + 1);
							hardPosNegEdges.put(edge, count);
						} else
							hardPosNegEdges.put(edge, new Integer("1"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return hardPosNegEdges;
	}

	/**
	 * This method executes query and puts the results into 2D arraylist
	 * <p>
	 * Query results are parsed and in 2D array a, a[i][j] corresponds to the
	 * ith row of the resultset and jth column query result
	 * 
	 * @param query
	 * @param reqColumns
	 * @return 2D list of query result
	 */
	public ArrayList<ArrayList<String>> getQueryResult(String query,
			int[] reqColumns) {
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

		ResultSet rs = executor.executeSelectQuery(query);

		try {
			while (rs.next()) {
				ArrayList<String> tupleResult = new ArrayList<String>();
				for (int i = 0; i < reqColumns.length; i++) {
					String attrResult = rs.getString(reqColumns[i]);
					tupleResult.add(attrResult);
				}
				result.add(tupleResult);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * This method executes update query and returns result
	 * 
	 * @param query
	 *            query
	 * @return update query result
	 */
	public int executeUpdateQuery(String query) {
		return executor.executeUpdateQuery(query);
	}

	/**
	 * This method inserts retrieved edges from the database into the edge list,
	 * instead of returning a new list
	 * 
	 * @param hP
	 *            hard eq table name
	 * @param pid
	 *            partition id of the edge
	 * @param hardPosEdges
	 *            list to be updated
	 * @param backMapping
	 *            mapping between database vertex ids and graph ids
	 */
	public void getHardPosEdgesIncrementally(int viewId, int pid,
			LinkedHashMap<Edge, Integer> hardPosEdges,
			LinkedHashMap<Integer, Integer> backMapping) {

		String hardEQPartTable = EqViewSchema
				.getHardEQRulePartTableName(viewId);
		String query = "SELECT " + EqViewSchema.H_EQ_R_P_NODE1_ATTR_NAME + ", "
				+ EqViewSchema.H_EQ_R_P_NODE2_ATTR_NAME + " FROM "
				+ hardEQPartTable + " WHERE "
				+ EqViewSchema.H_EQ_R_P_PID_ATTR_NAME + " = " + pid
				+ " ORDER BY " + EqViewSchema.H_EQ_R_P_NODE1_ATTR_NAME + ", "
				+ EqViewSchema.H_EQ_R_P_NODE2_ATTR_NAME + ";";

		if (!query.equals("")) {
			ResultSet rs = executor.executeSelectQuery(query);
			try {
				while (rs.next()) {
					if (backMapping.containsKey(rs
							.getInt(EqViewSchema.ID_OF_SRC_ATTR))
							&& backMapping.containsKey(rs
									.getInt(EqViewSchema.ID_OF_DST_ATTR))) {
						int src = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_SRC_ATTR));
						int dst = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_DST_ATTR));
						Edge edge = new Edge(Math.min(src, dst), Math.max(src,
								dst));
						if (hardPosEdges.containsKey(edge)) {
							Integer count = new Integer(hardPosEdges.get(edge)
									.intValue() + 1);
							hardPosEdges.put(edge, count);
						} else
							hardPosEdges.put(edge, new Integer("1"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This method inserts retrieved edges from the database into the edge list,
	 * instead of returning a new list
	 * 
	 * @param hnP
	 *            hard neq table name
	 * @param pid1
	 *            partition id of src vertex
	 * @param pid2
	 *            partition id of dst vertex
	 * @param hardNegEdges
	 *            list to be updated
	 * @param backMapping
	 *            mapping between database vertex ids and graph ids
	 */
	public void getHardNegEdgesIncrementally(int viewId, int pid1, int pid2,
			LinkedHashMap<Edge, Integer> hardNegEdges,
			LinkedHashMap<Integer, Integer> backMapping) {

		String hardNEQRulePartTable = EqViewSchema
				.getHardNEQRulePartTableName(viewId);
		String query = "SELECT " + EqViewSchema.H_NEQ_R_P_NODE1_ATTR_NAME
				+ ", " + EqViewSchema.H_NEQ_R_P_NODE2_ATTR_NAME + " FROM "
				+ hardNEQRulePartTable + " WHERE "
				+ EqViewSchema.H_NEQ_R_P_PID1_ATTR_NAME + " = " + pid1
				+ " AND " + EqViewSchema.H_NEQ_R_P_PID2_ATTR_NAME + " = "
				+ pid2 + " ORDER BY " + EqViewSchema.H_NEQ_R_P_NODE1_ATTR_NAME
				+ ", " + EqViewSchema.H_NEQ_R_P_NODE2_ATTR_NAME + ";";

		if (!query.equals("")) {
			ResultSet rs = executor.executeSelectQuery(query);
			try {
				while (rs.next()) {
					if (backMapping.containsKey(rs
							.getInt(EqViewSchema.ID_OF_SRC_ATTR))
							&& backMapping.containsKey(rs
									.getInt(EqViewSchema.ID_OF_DST_ATTR))) {
						int src = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_SRC_ATTR));
						int dst = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_DST_ATTR));
						Edge edge = new Edge(Math.min(src, dst), Math.max(src,
								dst));
						if (hardNegEdges.containsKey(edge)) {
							Integer count = new Integer(hardNegEdges.get(edge)
									.intValue() + 1);
							hardNegEdges.put(edge, count);
						} else
							hardNegEdges.put(edge, new Integer("1"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method inserts retrieved edges from the database into the given edge
	 * list, instead of returning a new list
	 * 
	 * @param viewId
	 * @param pid1
	 *            partition id of src vertex
	 * @param pid2
	 *            partition id of dst vertex
	 * @param softEdges
	 *            list to be updated
	 * @param backMapping
	 *            mapping between database vertex ids and graph ids
	 * @param multipleSoftRule
	 *            whether number of soft rules > 1 or not
	 * @param totalWeightOfSoftRules
	 *            total weight of soft rules
	 */
	public void getSoftEdgesIncrementally(int viewId, int pid1, int pid2,
			LinkedHashMap<Edge, Double> softEdges,
			LinkedHashMap<Integer, Integer> backMapping,
			boolean multipleSoftRule, double totalWeightOfSoftRules) {

		String softRulePartTable = EqViewSchema
				.getSoftRulePartTableName(viewId);
		String query = "";
		if (multipleSoftRule) {
			double threshold = totalWeightOfSoftRules / 2.0;
			query = "SELECT " + EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME + ", "
					+ EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME + " FROM "
					+ softRulePartTable + " WHERE "
					+ EqViewSchema.SOFT_R_P_PID1_ATTR_NAME + " = " + pid1
					+ " AND " + EqViewSchema.SOFT_R_P_PID2_ATTR_NAME + " = "
					+ pid2 + " AND " + EqViewSchema.SOFT_R_P_WEIGHT_ATTR_NAME
					+ " >= " + threshold + " ORDER BY "
					+ EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME + ", "
					+ EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME + ";";
		}
		// if there is no multiple soft rule, then we don't need to put
		// constaints on the edges where weight >= EDGE_VOTE_THRESHOLD
		else
			query = "SELECT " + EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME + ", "
					+ EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME + " FROM "
					+ softRulePartTable + " WHERE "
					+ EqViewSchema.SOFT_R_P_PID1_ATTR_NAME + " = " + pid1
					+ " AND " + EqViewSchema.SOFT_R_P_PID2_ATTR_NAME + " = "
					+ pid2 + " ORDER BY "
					+ EqViewSchema.SOFT_R_P_NODE1_ATTR_NAME + ", "
					+ EqViewSchema.SOFT_R_P_NODE2_ATTR_NAME + ";";

		if (!query.equals("")) {
			ResultSet rs = executor.executeSelectQuery(query);
			try {
				while (rs.next()) {
					if (backMapping.containsKey(rs
							.getInt(EqViewSchema.ID_OF_SRC_ATTR))
							&& backMapping.containsKey(rs
									.getInt(EqViewSchema.ID_OF_DST_ATTR))) {
						int src = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_SRC_ATTR));
						int dst = backMapping.get(rs
								.getInt(EqViewSchema.ID_OF_DST_ATTR));
						Edge edge = new Edge(Math.min(src, dst), Math.max(src,
								dst));
						if (softEdges.containsKey(edge)) {
							Double count = new Double(softEdges.get(edge)
									.intValue() + 1);
							softEdges.put(edge, count);
						} else
							softEdges.put(edge, new Double("1"));
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
