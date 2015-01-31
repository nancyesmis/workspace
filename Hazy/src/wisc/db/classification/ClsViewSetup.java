package wisc.db.classification;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import wisc.db.sql.DatabaseFactory;
import wisc.db.sql.QueryExecutor;
import wisc.db.sql.SQLConstants;

/**
 * Implements SQL-related calls such as inserts, updates, select queries etc.
 * for classification view. It also loads configuration parameters
 * 
 * {@link CreateClsView} class uses this class to execute SQL queries and
 * retrieve the results
 * 
 * @author koc
 * 
 */
public class ClsViewSetup {
	/**
	 * QueryExecutor object is for database layer
	 */
	QueryExecutor executor;

	/**
	 * Default constructor gets QueryExecutor from DatabaseFactory
	 */
	public ClsViewSetup() {
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
				+ ClsViewSchema.CLS_VIEW_ID_ALL_CATALOGS_TABLE + ";";
		ResultSet rs = executor.executeSelectQuery(query);
		try {
			int count = 0;
			if (rs.next())
				count = rs.getInt(1);
			if (count > 0) {
				query = "SELECT MAX("
						+ ClsViewSchema.ALL_CATALOGS_VIEW_ID_ATTR_NAME
						+ ") FROM "
						+ ClsViewSchema.CLS_VIEW_ID_ALL_CATALOGS_TABLE + ";";
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
	 * Inserts new created view id and catalog name for that view to the general
	 * catalog table
	 * 
	 * @param viewId
	 *            id of the view
	 */
	public void insertIntoIdCatalog(int viewId, String entityName,
			String viewName) {
		String catalogName = ClsViewSchema.getCatalogTableName(viewId);
		String idAlLCatalogsTableName = ClsViewSchema.CLS_VIEW_ID_ALL_CATALOGS_TABLE;
		String query = "INSERT INTO " + idAlLCatalogsTableName + " VALUES("
				+ viewId + ", '" + catalogName + "', '" + entityName + "', '"
				+ viewName + "');";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Creates catalog table for the view with given id
	 * 
	 * @param viewId
	 *            id of the view
	 */
	public void createViewCatalogTable(int viewId) {
		String catalogTableName = ClsViewSchema.getCatalogTableName(viewId);
		String query = "DROP TABLE IF EXISTS " + catalogTableName + ";"
				+ "CREATE TABLE " + catalogTableName + "("
				+ ClsViewSchema.CATALOG_TABLE_VIEW_ID_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_VIEW_ID_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_VIEW_NAME_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_VIEW_NAME_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_ENTITY_TABLE_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_ENTITY_TABLE_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_UPDATE_METHOD_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_UPDATE_METHOD_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_USE_RESERVOIR_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_USE_RESERVOIR_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_NO_OP_OPTION_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_NO_OP_OPTION_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_W_ARRAYSCALED_ORIGINAL_ATTR_NAME
				+ " "
				+ ClsViewSchema.CATALOG_TABLE_W_ARRAYSCALED_ORIGINAL_ATTR_TYPE
				+ ", " + ClsViewSchema.CATALOG_TABLE_BIAS_ORIGINAL_ATTR_NAME
				+ " " + ClsViewSchema.CATALOG_TABLE_BIAS_ORIGINAL_ATTR_TYPE
				+ ", " + ClsViewSchema.CATALOG_TABLE_W_ARRAYSCALED_ATTR_NAME
				+ " " + ClsViewSchema.CATALOG_TABLE_W_ARRAYSCALED_ATTR_TYPE
				+ ", " + ClsViewSchema.CATALOG_TABLE_BIAS_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_BIAS_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_LAMBDA_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_LAMBDA_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_DIM_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_DIM_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_T_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_T_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_ACC_COST_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_ACC_COST_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_RESORT_COST_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_RESORT_COST_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_EPSILON_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_EPSILON_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_M_FACTOR_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_M_FACTOR_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_LOW_WATER_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_LOW_WATER_ATTR_TYPE + ", "
				+ ClsViewSchema.CATALOG_TABLE_HIGH_WATER_ATTR_NAME + " "
				+ ClsViewSchema.CATALOG_TABLE_HIGH_WATER_ATTR_TYPE + ")";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Creates general r table for the view with given id and fills id,
	 * feature_id_array and feature_value_array columns
	 * 
	 * @param viewId
	 *            id of the view
	 */
	public void prepareGeneralRTableForHazy(int viewId, String entityTableName) {
		createGeneralRTable(viewId, entityTableName);
		fillGeneral_R_TABLE_AND_CREATE_INDEX(viewId);
	}

	/**
	 * Creates view that consists of positively labeled entities
	 * 
	 * @param viewId
	 *            id of the view
	 * @param viewName
	 *            name of the view
	 */
	public void createViewAfterTraining(int viewId, String viewName) {
		String general_r_table = ClsViewSchema.getGeneralRTable(viewId);
		String query = "DROP TABLE IF EXISTS " + viewName + "; CREATE TABLE "
				+ viewName + "(" + ClsViewSchema.VIEW_TABLE_ID_ATTR_NAME + " "
				+ ClsViewSchema.VIEW_TABLE_ID_ATTR_TYPE + "); " + ""
				+ "INSERT INTO " + viewName + "(SELECT "
				+ ClsViewSchema.GENERAL_R_TABLE_ID_ATTR_NAME + " FROM "
				+ general_r_table + " WHERE "
				+ ClsViewSchema.GENERAL_R_TABLE_TRUTH_VAL_ATTR_NAME
				+ " = 't');";
		executor.executeUpdateQuery(query);
	}

	/**
	 * Creates general r table with for the view with given id
	 * 
	 * @param viewId
	 *            id of the view
	 */
	private void createGeneralRTable(int viewId, String entityTableName) {
		String general_r_table = ClsViewSchema.getGeneralRTable(viewId);
		String extractedFeaturesTable = ClsViewSchema
				.getExtractedFeaturesTable(viewId);
		String featureIdsTable = ClsViewSchema.getFeatureIdsTableName(viewId);
		String tempGeneralRTable = ClsViewSchema
				.getTempGeneralRTableName(viewId);

		String dropTableQuery = "DROP TABLE IF EXISTS " + general_r_table + ";";
		String createTableQuery = "CREATE TABLE " + general_r_table + "("
				+ ClsViewSchema.GENERAL_R_TABLE_ID_ATTR_NAME + " "
				+ ClsViewSchema.GENERAL_R_TABLE_ID_ATTR_TYPE + ", "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_VALUE_ARRAY_ATTR_NAME
				+ " "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_VALUE_ARRAY_ATTR_TYPE
				+ ", " + ClsViewSchema.GENERAL_R_TABLE_C_ATTR_NAME + " "
				+ ClsViewSchema.GENERAL_R_TABLE_C_ATTR_TYPE + ", "
				+ ClsViewSchema.GENERAL_R_TABLE_TRUTH_VAL_ATTR_NAME + " "
				+ ClsViewSchema.GENERAL_R_TABLE_TRUTH_VAL_ATTR_TYPE + ", "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_ID_ARRAY_ATTR_NAME
				+ " "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_ID_ARRAY_ATTR_TYPE
				+ ");";

		String insertQuery = "INSERT INTO " + general_r_table + "("
				+ ClsViewSchema.GENERAL_R_TABLE_ID_ATTR_NAME + ", "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_VALUE_ARRAY_ATTR_NAME
				+ ", "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_ID_ARRAY_ATTR_NAME
				+ ") (SELECT " + ClsViewSchema.EXTRACTED_FEATURES_ID_ATTR_NAME
				+ ", " + ClsViewSchema.ARRAY_AGGREGATE_FUNCTION_NAME + "("
				+ ClsViewSchema.EXTRACTED_FEATURES_FEATURE_VALUE_ATTR_NAME
				+ "), " + ClsViewSchema.ARRAY_AGGREGATE_FUNCTION_NAME + "("
				+ ClsViewSchema.EXTRACTED_FEATURES_FEATURE_ID_ATTR_NAME
				+ ") FROM (SELECT "
				+ ClsViewSchema.EXTRACTED_FEATURES_ID_ATTR_NAME + ", "
				+ ClsViewSchema.EXTRACTED_FEATURES_FEATURE_ID_ATTR_NAME + ", "
				+ ClsViewSchema.EXTRACTED_FEATURES_FEATURE_VALUE_ATTR_NAME
				+ " FROM " + extractedFeaturesTable + " e, " + featureIdsTable
				+ " f where e."
				+ ClsViewSchema.EXTRACTED_FEATURES_FEATURE_NAME_ATTR_NAME
				+ " = f."
				+ ClsViewSchema.EXTRACTED_FEATURES_FEATURE_NAME_ATTR_NAME
				+ " ORDER BY " + ClsViewSchema.EXTRACTED_FEATURES_ID_ATTR_NAME
				+ ", " + ClsViewSchema.EXTRACTED_FEATURES_FEATURE_ID_ATTR_NAME
				+ ") AS " + tempGeneralRTable + " GROUP BY "
				+ ClsViewSchema.EXTRACTED_FEATURES_ID_ATTR_NAME + " ORDER BY "
				+ ClsViewSchema.EXTRACTED_FEATURES_ID_ATTR_NAME + ");";

		String insertRemainingQuery = "INSERT INTO " + general_r_table + "("
				+ ClsViewSchema.GENERAL_R_TABLE_ID_ATTR_NAME + ", "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_VALUE_ARRAY_ATTR_NAME
				+ ", "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_ID_ARRAY_ATTR_NAME
				+ ") (SELECT " + "id" + ", '{}', '{}' FROM " + entityTableName
				+ " WHERE " + "id" + " NOT IN (SELECT "
				+ ClsViewSchema.GENERAL_R_TABLE_ID_ATTR_NAME + " FROM "
				+ general_r_table + "))";

		String query = dropTableQuery + createTableQuery + insertQuery
				+ insertRemainingQuery;
		executor.executeUpdateQuery(query);
	}

	/**
	 * Fills id, feature_id_array and feature_value_array columns of general r
	 * table for the view with given id
	 * 
	 * @param viewId
	 *            id of the view
	 */
	private void fillGeneral_R_TABLE_AND_CREATE_INDEX(int viewId) {
		String general_r_table = ClsViewSchema.getGeneralRTable(viewId);
		String catalogTableName = ClsViewSchema.getCatalogTableName(viewId);
		String indexName = "general_r_index_" + viewId;

		String fillQuery = "UPDATE " + general_r_table + " SET "
				+ ClsViewSchema.GENERAL_R_TABLE_C_ATTR_NAME + " = "
				+ ClsViewSchema.DOT_PRODUCT_FUNCTION_NAME + "("
				+ ClsViewSchema.CATALOG_TABLE_W_ARRAYSCALED_ORIGINAL_ATTR_NAME
				+ ", "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_ID_ARRAY_ATTR_NAME
				+ ", "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_VALUE_ARRAY_ATTR_NAME
				+ ") + " + ClsViewSchema.CATALOG_TABLE_BIAS_ORIGINAL_ATTR_NAME
				+ ", " + ClsViewSchema.GENERAL_R_TABLE_TRUTH_VAL_ATTR_NAME
				+ " = " + ClsViewSchema.DOT_PRODUCT_FUNCTION_NAME + "("
				+ ClsViewSchema.CATALOG_TABLE_W_ARRAYSCALED_ORIGINAL_ATTR_NAME
				+ ", "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_ID_ARRAY_ATTR_NAME
				+ ", "
				+ ClsViewSchema.GENERAL_R_TABLE_FEATURE_VALUE_ARRAY_ATTR_NAME
				+ ") + " + ClsViewSchema.CATALOG_TABLE_BIAS_ORIGINAL_ATTR_NAME
				+ " > 0 FROM " + catalogTableName + ";";

		String createIndexQuery = "CREATE INDEX " + indexName + " ON "
				+ general_r_table + "("
				+ ClsViewSchema.GENERAL_R_TABLE_C_ATTR_NAME + ");" + "CLUSTER "
				+ indexName + " ON " + general_r_table + ";";

		String query = fillQuery + createIndexQuery;
		executor.executeUpdateQuery(query);
	}

	/**
	 * Inserts view id, view name, entity table name, use reservoir, no op and
	 * m_factor entries to the global table of the view with given id
	 * 
	 * @param viewId
	 *            id of the view
	 * @param viewName
	 *            view name
	 * @param entityTable
	 *            entity table name
	 * @param useReservoir
	 *            use reservoir or not
	 * @param noOp
	 *            no op
	 * @param m_factor
	 *            max norm1 value of feature vector of entities
	 */
	public void fillsViewCatalogTable(int viewId, String viewName,
			String entityTable, int useReservoir, int noOp, double m_factor) {
		String catalogTableName = ClsViewSchema.getCatalogTableName(viewId);
		String query = "INSERT INTO " + catalogTableName + "("
				+ ClsViewSchema.CATALOG_TABLE_VIEW_ID_ATTR_NAME + ", "
				+ ClsViewSchema.CATALOG_TABLE_VIEW_NAME_ATTR_NAME + ", "
				+ ClsViewSchema.CATALOG_TABLE_ENTITY_TABLE_ATTR_NAME + ", "
				+ ClsViewSchema.CATALOG_TABLE_UPDATE_METHOD_ATTR_NAME + ", "
				+ ClsViewSchema.CATALOG_TABLE_USE_RESERVOIR_ATTR_NAME + ", "
				+ ClsViewSchema.CATALOG_TABLE_NO_OP_OPTION_ATTR_NAME + ", "
				+ ClsViewSchema.CATALOG_TABLE_EPSILON_ATTR_NAME + ", "
				+ ClsViewSchema.CATALOG_TABLE_M_FACTOR_ATTR_NAME + ") VALUES ("
				+ viewId + ", '" + viewName + "', '" + entityTable + "', '"
				+ ClsViewConstants.UPDATE_METHOD_HAZY + "', " + useReservoir
				+ ", " + noOp + ", " + ClsViewConstants.EPS + ", " + m_factor
				+ ");";

		executor.executeUpdateQuery(query);
	}

	/**
	 * Inserts negative and positive example tables to the update table catalog
	 * with the given view id, reservoir option and noop option
	 * 
	 * @param posExTable
	 *            positive example table
	 * @param negExTable
	 *            negative example table
	 * @param reservoirOption
	 *            reservoir option
	 * @param noOpOption
	 *            noop option
	 * @param viewId
	 *            id of the view
	 */
	public void fillsUpdateTableViewIdCatalogTable(String posExTable,
			String negExTable, int reservoirOption, int noOpOption, int viewId) {
		String insertPosExQuery = "INSERT INTO "
				+ ClsViewSchema.UPDATE_TABLE_VIEW_ID_TABLE_NAME + " VALUES('"
				+ posExTable + "', " + viewId + ", " + reservoirOption + ", "
				+ noOpOption + ", '+');";
		String insertNegExQuery = "INSERT INTO "
				+ ClsViewSchema.UPDATE_TABLE_VIEW_ID_TABLE_NAME + " VALUES('"
				+ negExTable + "', " + viewId + ", " + reservoirOption + ", "
				+ noOpOption + ", '-');";

		String query = insertPosExQuery + insertNegExQuery;

		executor.executeUpdateQuery(query);
	}

	/**
	 * Calls getStatsFeatures function on database to extract feature vectors of
	 * entities
	 * 
	 * @param viewId
	 *            id of the view
	 * @param entityTableName
	 *            entity table name
	 */
	private void callgetStatsFeaturesFunction(int viewId, String entityTableName) {
		String query = "SELECT * FROM "
				+ ClsViewSchema.GET_STATS_FEATURES_FUNCTION_NAME + "(" + viewId
				+ ", '" + entityTableName + "');";
		executor.executeSelectQuery(query);
		query = "SELECT COUNT(*) FROM " + entityTableName + ";";
	}

	/**
	 * Calls getStatsFeatures function on database to extract feature vectors of
	 * entities with newly inserted entity
	 * 
	 * @param viewId
	 *            id of the view
	 * @param newEntityId
	 *            id of the newly inserted entity
	 * @param newEntityName
	 *            name of the newly inserted entity
	 * @param entityTableName
	 *            entity table name
	 */
	private void callgetStatsFeaturesFunctionWithNewEntity(int viewId,
			String entityTableName, int newEntityId, String newEntityName) {
		String query = "SELECT * FROM "
				+ ClsViewSchema.GET_STATS_FEATURES_WITH_NEW_ENTITY_FUNCTION_NAME
				+ "(" + viewId + ", '" + entityTableName + "', " + newEntityId
				+ ", '" + newEntityName + "');";
		executor.executeSelectQuery(query);
		query = "SELECT COUNT(*) FROM " + entityTableName + ";";
	}

	/**
	 * Calls prepare train function in order to prepare training table that
	 * contains training entries in the form of label_of_example
	 * feature_id:feature_value ...
	 * 
	 * @param viewId
	 *            id of the view
	 * @param posTable
	 *            positive example table
	 * @param negTable
	 *            negative example table
	 */
	private void callPrepareTrainFunction(int viewId, String posTable,
			String negTable) {
		String query = "SELECT * FROM "
				+ ClsViewSchema.PREPARE_TRAIN_FUNCTION_NAME + "(" + viewId
				+ ", '" + posTable + "', '" + negTable + "');";
		executor.executeSelectQuery(query);
	}

	/**
	 * Calls the function that normalizes feature vectors of entities and
	 * retrieve the maximum norm1 of feature vectors
	 * 
	 * @param viewId
	 *            id of the view
	 * @return max norm1 of feature vectors
	 */
	private double normalizeAndGetM(int viewId) {
		double m_factor = 0;
		String query = "SELECT * FROM "
				+ ClsViewSchema.NORMALIZE_GET_M_FACTOR_FUNCTION_NAME + "("
				+ viewId + ");";
		ResultSet rs = executor.executeSelectQuery(query);
		try {
			if (rs.next())
				m_factor = rs.getDouble(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return m_factor;
	}

	/**
	 * Extracts feature vectors of entities, normalize them and creates training
	 * table
	 * 
	 * <p>
	 * Calls function that extracts feature vectors of entities and then calls
	 * normalizeAndGetM function in order to normalize feature vectors and
	 * returns the max norm1 of feature vectors.
	 * <p>
	 * Finally, it calls prepareTrain function to create training table that
	 * contains training entries in the form of label_of_example
	 * feature_id:feature_value ...
	 * 
	 * @param viewId
	 *            id of the view
	 * @param entityTableName
	 *            entity table name
	 * @param posTable
	 *            positive example table
	 * @param negTable
	 *            negative example table
	 * @return
	 */
	public double prepareTrain(int viewId, String entityTableName,
			String posTable, String negTable) {
		callgetStatsFeaturesFunction(viewId, entityTableName);
		double m_factor = normalizeAndGetM(viewId);
		callPrepareTrainFunction(viewId, posTable, negTable);

		return m_factor;
	}

	/**
	 * Extracts feature vectors of entities with newly inserted entity,
	 * normalize them and creates training table
	 * 
	 * <p>
	 * Calls function that extracts feature vectors of entities and then calls
	 * normalizeAndGetM function in order to normalize feature vectors and
	 * returns the max norm1 of feature vectors.
	 * <p>
	 * Finally, it calls prepareTrain function to create training table that
	 * contains training entries in the form of label_of_example
	 * feature_id:feature_value ...
	 * 
	 * @param viewId
	 *            id of the view
	 * @param entityTableName
	 *            entity table name
	 * @param newEntityId
	 *            id of the newly inserted entity
	 * @param newEntityName
	 *            name of the newly inserted entity
	 * @param posTable
	 *            positive example table
	 * @param negTable
	 *            negative example table
	 * @return
	 */
	public double prepareTrainWithNewEntity(int viewId, String entityTableName,
			int newEntityId, String newEntityName, String posTable,
			String negTable) {
		callgetStatsFeaturesFunctionWithNewEntity(viewId, entityTableName,
				newEntityId, newEntityName);
		double m_factor = normalizeAndGetM(viewId);
		callPrepareTrainFunction(viewId, posTable, negTable);

		return m_factor;
	}

	/**
	 * Calls train function at database to train a model with the training table
	 * 
	 * @param viewId
	 *            id of the view
	 * @param dim
	 *            dimension of feature vectors
	 * @param reservoirOption
	 *            reservoir option
	 * @param noOpOption
	 *            noop option
	 * @return success (1, if success. Otherwise, not successful)
	 */
	public int train(int viewId, int dim, int reservoirOption, int noOpOption) {
		String trainTable = ClsViewSchema.getTrainingTableName(viewId);
		String dbName = SQLConstants.DBNAME;
		int numOfArguments = 7;

		String query = "SELECT * FROM " + ClsViewSchema.TRAIN_FUNCTION_NAME
				+ "(" + numOfArguments + ", '{" + dbName + ", "
				+ ClsViewSchema.TRAIN_MODE + ", " + viewId + ", " + trainTable
				+ ", " + dim + ", " + reservoirOption + ", " + noOpOption
				+ "}');";

		int retValue = 0;
		ResultSet rs = executor.executeSelectQuery(query);
		try {
			if (rs.next())
				retValue = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return retValue;
	}

	/**
	 * Retrieves number of features from featureIds table
	 * 
	 * @param viewId
	 *            id of the view
	 * @return dimension of feature space
	 */
	public int getNumberOfFeatures(int viewId) {
		String featureTableName = ClsViewSchema.getFeatureIdsTableName(viewId);
		String query = "SELECT COUNT(*) FROM " + featureTableName + ";";
		int noOfFeature = 0;

		ResultSet rs = executor.executeSelectQuery(query);
		try {
			if (rs.next())
				noOfFeature = rs.getInt(1) + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return noOfFeature;
	}

	/**
	 * Creates required reservoir tables for the view with given id, if
	 * reservoirOption is enabled
	 * 
	 * @param viewId
	 *            id of the view
	 */
	public void createReservoir(int viewId) {
		String query = "";

		String reservoirName = ClsViewSchema.getReservoirWithViewId(viewId);
		query += "DROP TABLE IF EXISTS " + reservoirName + "; CREATE TABLE "
				+ reservoirName + "("
				+ ClsViewSchema.RESERVOIR_TABLE_ID_ATTR_NAME + " "
				+ ClsViewSchema.RESERVOIR_TABLE_ID_ATTR_TYPE + ", "
				+ ClsViewSchema.RESERVOIR_TABLE_NAME_ATTR_NAME + " "
				+ ClsViewSchema.RESERVOIR_TABLE_NAME_ATTR_TYPE + ");";

		executor.executeUpdateQuery(query);
	}

	private void createEntityTableTrigger(int viewId, String entityTableName) {
		String triggerName = ClsViewSchema.getEntityTriggerName(viewId);
		String triggerFunctionName = ClsViewSchema.ENTITY_TRIGGER_FUNCTION_NAME;
		createTrigger(triggerName, entityTableName, triggerFunctionName);
	}

	/**
	 * Creates trigger for positive example table
	 * 
	 * @param viewId
	 *            id of the view
	 * @param posExTableName
	 *            positive example table
	 */
	private void createPositiveExTableTrigger(int viewId, String posExTableName) {
		String triggerName = ClsViewSchema.getPositiveTriggerName(viewId);
		String triggerFunctionName = ClsViewSchema.POS_TRIGGER_FUNCTION_NAME;
		createTrigger(triggerName, posExTableName, triggerFunctionName);
	}

	/**
	 * Creates trigger for negative example table
	 * 
	 * @param viewId
	 *            id of the view
	 * @param negExTableName
	 *            negative example table
	 */
	private void createNegativeExTableTrigger(int viewId, String negExTableName) {
		String triggerName = ClsViewSchema.getNegativeTriggerName(viewId);
		String triggerFunctionName = ClsViewSchema.NEG_TRIGGER_FUNCTION_NAME;
		createTrigger(triggerName, negExTableName, triggerFunctionName);
	}

	/**
	 * Creates triggers for positive and negative example tables
	 * 
	 * @param viewId
	 *            id of the view
	 * @param posExTableName
	 *            positive example table
	 * @param negExTableName
	 *            negative example table
	 */
	public void createTriggers(int viewId, String entityTable,
			String posExTableName, String negExTableName) {
		this.createEntityTableTrigger(viewId, entityTable);
		this.createPositiveExTableTrigger(viewId, posExTableName);
		this.createNegativeExTableTrigger(viewId, negExTableName);
	}

	/**
	 * Generic method that creates trigger with the given trigger name, table
	 * name and trigger function name
	 * 
	 * @param triggerName
	 *            trigger name
	 * @param tableName
	 *            table name
	 * @param triggerFunctionName
	 *            trigger function name
	 */
	private void createTrigger(String triggerName, String tableName,
			String triggerFunctionName) {
		String query = "DROP TRIGGER IF EXISTS " + triggerName + " ON "
				+ tableName + "; CREATE TRIGGER " + triggerName
				+ " AFTER INSERT OR UPDATE OR DELETE ON " + tableName
				+ " FOR EACH ROW EXECUTE PROCEDURE " + triggerFunctionName
				+ "();";
		executor.executeUpdateQuery(query);
	}

	/**
	 * Retrieves view id and name from general catalog table
	 * <p>
	 * This information is used when an entity table is updated
	 * 
	 * @param entityName
	 *            entity table name
	 * @return view id and name
	 */
	public ArrayList<String> retrieveViewIdNameFromGeneralCatalog(
			String entityName) {
		ArrayList<String> viewIdName = new ArrayList<String>();
		String viewId = "";
		String viewName = "";

		String query = "SELECT " + ClsViewSchema.ALL_CATALOGS_VIEW_ID_ATTR_NAME
				+ ", " + ClsViewSchema.ALL_CATALOGS_VIEW_NAME_ATTR_NAME
				+ " FROM " + ClsViewSchema.CLS_VIEW_ID_ALL_CATALOGS_TABLE
				+ " WHERE " + ClsViewSchema.ALL_CATALOGS_ENTITY_NAME_ATTR_NAME
				+ " = '" + entityName + "';";

		ResultSet set = executor.executeSelectQuery(query);
		try {
			if (set.next()) {
				viewId = set.getString(1);
				viewName = set.getString(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		viewIdName.add(viewId);
		viewIdName.add(viewName);

		return viewIdName;
	}

	/**
	 * Retrieve positive, negative example table and reservoir and passive
	 * aggressive options from given view id
	 * 
	 * @param viewId
	 *            id of the view
	 * @return pos, neg ex table and reservoir and pa options
	 */
	public ArrayList<String> retrievePosNegExTableReservoirNoOpFromUpdateTable(
			int viewId) {
		ArrayList<String> results = new ArrayList<String>();

		String posExTable = "";
		String negExTable = "";
		String withReservoir = "";
		String noOp = "";

		String query = "SELECT "
				+ ClsViewSchema.UPDATE_CATALOG_VIEW_UPDATE_TABLE_NAME_ATTR_NAME
				+ ", "
				+ ClsViewSchema.UPDATE_CATALOG_USE_RESERVOIR_ATTR_NAME
				+ ", "
				+ ClsViewSchema.UPDATE_CATALOG_NO_OP_OPTION_ATTR_NAME
				+ " FROM "
				+ ClsViewSchema.UPDATE_TABLE_VIEW_ID_TABLE_NAME
				+ " WHERE "
				+ ClsViewSchema.UPDATE_CATALOG_VIEW_ID_ATTR_NAME
				+ " = "
				+ viewId
				+ " AND "
				+ ClsViewSchema.UPDATE_CATALOG_TYPE_OF_EXAMPLE_TABLE_ATTR_NAME
				+ "='"
				+ ClsViewConstants.REPRESENTATION_OF_POSITIVE_TYPE_EXAMPLE_TABLE
				+ "';";

		ResultSet set = executor.executeSelectQuery(query);
		try {
			if (set.next()) {
				posExTable = set.getString(1);
				withReservoir = set.getString(2);
				noOp = set.getString(3);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// negative example table
		query = "SELECT "
				+ ClsViewSchema.UPDATE_CATALOG_VIEW_UPDATE_TABLE_NAME_ATTR_NAME
				+ " FROM "
				+ ClsViewSchema.UPDATE_TABLE_VIEW_ID_TABLE_NAME
				+ " WHERE "
				+ ClsViewSchema.UPDATE_CATALOG_VIEW_ID_ATTR_NAME
				+ " = "
				+ viewId
				+ " AND "
				+ ClsViewSchema.UPDATE_CATALOG_TYPE_OF_EXAMPLE_TABLE_ATTR_NAME
				+ "='"
				+ ClsViewConstants.REPRESENTATION_OF_NEGATIVE_TYPE_EXAMPLE_TABLE
				+ "';";

		set = executor.executeSelectQuery(query);
		try {
			if (set.next()) {
				negExTable = set.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		results.add(posExTable);
		results.add(negExTable);
		results.add(withReservoir);
		results.add(noOp);

		return results;
	}

	/**
	 * Updates catalog table with given new m_factor
	 * 
	 * @param viewId
	 *            id of the view
	 * @param mFactor
	 *            m factor
	 */
	public void updateMFactorInCatalog(int viewId, double mFactor) {
		String catalogName = ClsViewSchema.getCatalogTableName(viewId);
		String query = "UPDATE " + catalogName + " SET "
				+ ClsViewSchema.CATALOG_TABLE_M_FACTOR_ATTR_NAME + " = "
				+ mFactor + ";";
		executor.executeUpdateQuery(query);

	}
}
