package wisc.db.classification;

/**
 * Contains classification view schema related database items.
 * It contains table names, table attributes, trigger function names, trigger names, method to retrieve unique table names.
 * 
 * @author koc
 *
 */
public class ClsViewSchema {
	
	public static String getEntityTriggerName(int viewId) {
		return getNameWithId(ENTITY_TRIGGER_BASE_NAME, viewId);
	}
	
	/**
	 * Returns trigger name for positive example table for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return trigger name for positive example table
	 */
	public static String getPositiveTriggerName(int viewId) {
		return getNameWithId(POS_TRIGGER_BASE_NAME, viewId);
	}
	
	/**
	 * Returns trigger name for negative example table for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return trigger name for negative example table
	 */
	public static String getNegativeTriggerName(int viewId) {
		return getNameWithId(NEG_TRIGGER_BASE_NAME, viewId);
	}
	
	/**
	 * Returns temp general r table name for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return temp general r table name
	 */
	public static String getTempGeneralRTableName(int viewId) {
		return getNameWithId(TEMP_GENERAL_R_TABLE_BASE_NAME, viewId);
	}
	
	/**
	 * Returns extracted features table name for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return extracted features table name
	 */
	public static String getExtractedFeaturesTable(int viewId) {
		return EXTRACTED_FEATURES_BASE_NAME + "" + viewId;
	}
	/**
	 * Returns general r table name for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return general r table name
	 */
	public static String getGeneralRTable(int viewId) {
		return getNameWithId(GENERAL_R_TABLE_BASE_NAME, viewId);
	}
	
	/**
	 * Returns reservoir table name for the view with given id and svm id
	 * 
	 * @param viewId id of the view
	 * @param svmId id of the svm (this is used since FIVE_FOLD is being used)
	 * @return reservoir table name
	 */
	public static String getReservoirWithViewId(int viewId) {
		return getNameWithId(RESERVOIR_TABLE_BASE_NAME, viewId) ;
	}
	
	/**
	 * Returns feature ids table name for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return feature ids table name
	 */
	public static String getFeatureIdsTableName(int viewId) {
		return FEATURE_IDS_TABLE_BASE_NAME + "" + viewId;
	}
	
	/**
	 * Returns training table name for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return training table name
	 */
	public static String getTrainingTableName(int viewId) {
		return TRAINING_TABLE_BASE_NAME + "" + viewId;
	}
	
	/**
	 * Returns cls view catalog table name for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return cls view catalog table name
	 */
	public static String getCatalogTableName(int viewId) {
		return getNameWithId(CLS_VIEW_CATALOG_BASE, viewId);
	}
	
	/**
	 * Concatenates given base name with the id by using NAME NAME_ID_DELIMITER
	 * 
	 * @param base base name
	 * @param viewId id of view
	 * @return concatenated unique name whose base is first parameter
	 */
	public static String getNameWithId(String base, int viewId) {
		return base + NAME_ID_DELIMITER + viewId;
	}
	
	/**
	 * Contains view id, catalog name pairs
	 */
	public static final String CLS_VIEW_ID_ALL_CATALOGS_TABLE = "general_cls_view_catalog";
	/**
	 * Contains update table, view id, reservoir option enabled and noop enabled.
	 * It provides retrieving view id, reservoir option and noop option when update table is provided
	 */
	public static final String UPDATE_TABLE_VIEW_ID_TABLE_NAME = "update_table_view_id_catalog";
	
	/**
	 * Contains training inputs that will be trained in the format of label_of_example feature_id:feature_value ...
	 */
	public static final String TRAINING_TABLE_BASE_NAME = "training";
	/**
	 * Contains feature id assigned to each feature and features
	 */
	public static final String FEATURE_IDS_TABLE_BASE_NAME = "featureIds";
	/**
	 * When reservoir option is enabled, then reservoir tables are used. This is the base name for it.
	 * Each view has reservoirs (if option is enabled) with name  RESERVOIR_TABLE_BASE_NAME + "_" + viewId + "_" + svmId
	 * where svmId provides uniquness, too.
	 */
	public static final String RESERVOIR_TABLE_BASE_NAME = "reservoir";
	/**
	 * Contains entity, feature id array, feature value array, prediction and label. It is used as intermediate table for Hazy algorithm
	 */
	public static final String GENERAL_R_TABLE_BASE_NAME = "general_r_table";
	/**
	 * Temporary table using to create GENERAL_R_TABLE
	 */
	public static final String TEMP_GENERAL_R_TABLE_BASE_NAME = "temp_for_general_r";
	/**
	 * Base name for extracted features table. This table contains entity id, feature_id and feature_value. Each view has this table
	 * for entity features with name EXTRACTED_FEATURES_BASE_NAME + "_" + viewId
	 */
	public static final String EXTRACTED_FEATURES_BASE_NAME = "extractedfeatures";
	/**
	 * Base name for view catalog table. Each view has a catalog with name CLS_VIEW_CATALOG_BASE + _ + viewId
	 */
	public static final String CLS_VIEW_CATALOG_BASE = "cls_view_catalog";
	
	/**
	 * boolean type in PostGreSQL
	 */
	public static final String BOOLEAN_TYPE = "boolean";
	/**
	 * float array type in PostGreSQL
	 */
	public static final String FLOAT_ARRAY_TYPE = "float8[]";
	/**
	 * float type in PostGreSQL
	 */
	public static final String FLOAT_TYPE = "float8";
	/**
	 * integer array type in PostGreSQL
	 */
	public static final String INT_ARRAY_TYPE = "int[]";
	/**
	 * integer type in PostGreSQL
	 */
	public static final String INT_TYPE = "int";
	/**
	 * text type in PostGreSQL
	 */
	public static final String TEXT_TYPE = "text";
	
	/**
	 * id of the view 
	 */
	public static final String ALL_CATALOGS_VIEW_ID_ATTR_NAME = "view_id";
	/**
	 * Attribute type of view id
	 */
	public static final String ALL_CATALOGS_VIEW_ID_ATTR_TYPE = INT_TYPE;
	/**
	 * Name of the catalog table that view has
	 */
	public static final String ALL_CATALOGS_CATALOG_NAME_ATTR_NAME = "catalog_name";
	/**
	 * Attribute type of catalog name
	 */
	public static final String ALL_CATALOGS_CATALOG_NAME_ATTR_TYPE = TEXT_TYPE;
	/**
	 * Name of the entity table
	 */
	public static final String ALL_CATALOGS_ENTITY_NAME_ATTR_NAME = "entity_name";
	/**
	 * Attribute type of entity name
	 */
	public static final String ALL_CATALOGS_ENTITY_NAME_ATTR_TYPE = TEXT_TYPE;
	/**
	 * Name of the view table
	 */
	public static final String ALL_CATALOGS_VIEW_NAME_ATTR_NAME = "view_name";
	/**
	 * Attribute type of view name
	 */
	public static final String ALL_CATALOGS_VIEW_NAME_ATTR_TYPE = TEXT_TYPE;
	/**
	 * Name of the update table
	 */
	public static final String UPDATE_CATALOG_VIEW_UPDATE_TABLE_NAME_ATTR_NAME = "update_table";
	/**
	 * Attribute type of update_table
	 */
	public static final String UPDATE_CATALOG_VIEW_UPDATE_TABLE_NAME_ATTR_TYPE = TEXT_TYPE;
	/**
	 * Id of the view that update table belongs to
	 */
	public static final String UPDATE_CATALOG_VIEW_ID_ATTR_NAME = "view_id";
	/**
	 * Attribute type of view id
	 */
	public static final String UPDATE_CATALOG_VIEW_ID_ATTR_TYPE = INT_TYPE;
	/**
	 * Whether reservoir is used or not for the view
	 */
	public static final String UPDATE_CATALOG_USE_RESERVOIR_ATTR_NAME = "with_reservoir";
	/**
	 * Attribute type of with reservoir
	 */
	public static final String UPDATE_CATALOG_USE_RESERVOIR_ATTR_TYPE = INT_TYPE;
	/**
	 * Whether no op is used or not for the view
	 */
	public static final String UPDATE_CATALOG_NO_OP_OPTION_ATTR_NAME = "use_noop";
	/**
	 * Attribute type of noop
	 */
	public static final String UPDATE_CATALOG_NO_OP_OPTION_ATTR_TYPE = INT_TYPE;
	/**
	 * Whether example table is positive or not
	 */
	public static final String UPDATE_CATALOG_TYPE_OF_EXAMPLE_TABLE_ATTR_NAME = "typeofexample";
	/**
	 * Attribute type of typeofexample
	 */
	public static final String UPDATE_CATALOG_TYPE_OF_EXAMPLE_TABLE_ATTR_TYPE = TEXT_TYPE;
	
	/**
	 * Id of view. Each view has a unique id
	 */
	public static final String CATALOG_TABLE_VIEW_ID_ATTR_NAME = "view_id";
	/**
	 * Attribute type of view_id attribute
	 */
	public static final String CATALOG_TABLE_VIEW_ID_ATTR_TYPE = INT_TYPE;
	/**
	 * Name of the view that contains positively classified entities
	 */
	public static final String CATALOG_TABLE_VIEW_NAME_ATTR_NAME = "view_name";
	/**
	 * Attribute type of view_name attribute
	 */
	public static final String CATALOG_TABLE_VIEW_NAME_ATTR_TYPE = TEXT_TYPE;
	/**
	 * Name of the entity table
	 */
	public static final String CATALOG_TABLE_ENTITY_TABLE_ATTR_NAME = "entity_table";
	/**
	 * Attribute type of entity_table attribute
	 */
	public static final String CATALOG_TABLE_ENTITY_TABLE_ATTR_TYPE = TEXT_TYPE;
	/**
	 * Update method for incremental update
	 * 
	 * @see ClsViewConstants
	 */
	public static final String CATALOG_TABLE_UPDATE_METHOD_ATTR_NAME = "update_method";
	/**
	 * Attribute type of update_method
	 */
	public static final String CATALOG_TABLE_UPDATE_METHOD_ATTR_TYPE = TEXT_TYPE;
	/**
	 * Training option whether to use reservoir during the training or not. 1 for using, 0 for not using
	 */
	public static final String CATALOG_TABLE_USE_RESERVOIR_ATTR_NAME = "with_reservoir";
	/**
	 * Attribute type of with_reservoir
	 */
	public static final String CATALOG_TABLE_USE_RESERVOIR_ATTR_TYPE = INT_TYPE;
	/**
	 * Training option whether to use noop during the training or not. 1 for using, 0 for not using
	 */
	public static final String CATALOG_TABLE_NO_OP_OPTION_ATTR_NAME = "use_noop";
	/**
	 * Attribute type of use_noop
	 */
	public static final String CATALOG_TABLE_NO_OP_OPTION_ATTR_TYPE = INT_TYPE;
	/**
	 * Model(weights) for the view. This is the model that result table is sorted on.
	 */
	public static final String CATALOG_TABLE_W_ARRAYSCALED_ORIGINAL_ATTR_NAME = "w_arrayscaled_original";
	/**
	 * Attribute type of model
	 */
	public static final String CATALOG_TABLE_W_ARRAYSCALED_ORIGINAL_ATTR_TYPE = FLOAT_ARRAY_TYPE;
	/**
	 * Bias term for the view. This is the one that used for sorting the result table
	 */
	public static final String CATALOG_TABLE_BIAS_ORIGINAL_ATTR_NAME = "bias_original";
	/**
	 * Attribute type of bias
	 */
	public static final String CATALOG_TABLE_BIAS_ORIGINAL_ATTR_TYPE = FLOAT_TYPE;
	/**
	 * Model(weights) for the view. This model is the last one learned
	 */
	public static final String CATALOG_TABLE_W_ARRAYSCALED_ATTR_NAME = "w_arrayscaled";
	/**
	 * Attribute type of model
	 */
	public static final String CATALOG_TABLE_W_ARRAYSCALED_ATTR_TYPE = FLOAT_ARRAY_TYPE;
	/**
	 * Bias term for the view. This is the one that learned last
	 */
	public static final String CATALOG_TABLE_BIAS_ATTR_NAME = "bias";
	/**
	 * Attribute type of bias
	 */
	public static final String CATALOG_TABLE_BIAS_ATTR_TYPE = FLOAT_TYPE;
	/**
	 * Lambda (model-related variable)
	 */
	public static final String CATALOG_TABLE_LAMBDA_ATTR_NAME = "lambda";
	/**
	 * Attribute type of lambda
	 */
	public static final String CATALOG_TABLE_LAMBDA_ATTR_TYPE = FLOAT_TYPE;
	/**
	 * Number of features (dimension) of the view
	 */
	public static final String CATALOG_TABLE_DIM_ATTR_NAME = "dim";
	/**
	 * Attribute type of dim
	 */
	public static final String CATALOG_TABLE_DIM_ATTR_TYPE = INT_TYPE;
	/**
	 * t (model-related variable)
	 */
	public static final String CATALOG_TABLE_T_ATTR_NAME = "t";
	/**
	 * Attribute type of t
	 */
	public static final String CATALOG_TABLE_T_ATTR_TYPE = FLOAT_TYPE;
	/**
	 * Attribute for accumulated cost. This is used for Hazy update algorithm
	 */
	public static final String CATALOG_TABLE_ACC_COST_ATTR_NAME = "acc";
	/**
	 * Attribute type of acc
	 */
	public static final String CATALOG_TABLE_ACC_COST_ATTR_TYPE = FLOAT_TYPE;
	/**
	 * Attribute for resort cost. This is used for Hazy update algorithm
	 */
	public static final String CATALOG_TABLE_RESORT_COST_ATTR_NAME = "resort_time";
	/**
	 * Attribute type of resort time
	 */
	public static final String CATALOG_TABLE_RESORT_COST_ATTR_TYPE = FLOAT_TYPE;
	/**
	 * Eps is used when updating accumulated cost. acc_cost = (1 - eps) * acc_cost + update_cost
	 */
	public static final String CATALOG_TABLE_EPSILON_ATTR_NAME = "eps";
	/**
	 * Attribute type of eps
	 */
	public static final String CATALOG_TABLE_EPSILON_ATTR_TYPE = FLOAT_TYPE;
	/**
	 * Maximum norm1 of entity feature vectors. It is used for Hazy
	 */
	public static final String CATALOG_TABLE_M_FACTOR_ATTR_NAME = "m_factor";
	/**
	 * Attribute type of m_factor
	 */
	public static final String CATALOG_TABLE_M_FACTOR_ATTR_TYPE = FLOAT_TYPE;
	/**
	 * Attribute for lower bound on prediction values of entities. Below this bound, labels cannot change
	 */
	public static final String CATALOG_TABLE_LOW_WATER_ATTR_NAME = "low_water";
	/**
	 * Attribute type of low water
	 */
	public static final String CATALOG_TABLE_LOW_WATER_ATTR_TYPE = FLOAT_TYPE;
	/**
	 * Attribute for upper bound on prediction values of entities. Above this bound, labels cannot change
	 */
	public static final String CATALOG_TABLE_HIGH_WATER_ATTR_NAME = "high_water";
	/**
	 * Attribute type of high water
	 */
	public static final String CATALOG_TABLE_HIGH_WATER_ATTR_TYPE = FLOAT_TYPE;
	
	/**
	 * Attribute for entity id in view table
	 */
	public static final String VIEW_TABLE_ID_ATTR_NAME = "id";
	/**
	 * Attribute type of id
	 */
	public static final String VIEW_TABLE_ID_ATTR_TYPE = INT_TYPE;
	
	/**
	 * Id of example in reservoir
	 */
	public static final String RESERVOIR_TABLE_ID_ATTR_NAME = "id";
	/**
	 * Attribute type of id column
	 */
	public static final String RESERVOIR_TABLE_ID_ATTR_TYPE = INT_TYPE;
	/**
	 * Svm representation of an example. svm representation is: label_of_example feature_id:feature_value, feature_id:feature_value ...
	 */
	public static final String RESERVOIR_TABLE_NAME_ATTR_NAME = "name";
	/**
	 * Attribute type of name column
	 */
	public static final String RESERVOIR_TABLE_NAME_ATTR_TYPE = TEXT_TYPE;
	
	/**
	 * Id column of general result table. It represents id of entity
	 */
	public static final String GENERAL_R_TABLE_ID_ATTR_NAME = "id";
	/**
	 * Attribute type of id column
	 */
	public static final String GENERAL_R_TABLE_ID_ATTR_TYPE = INT_TYPE;
	/**
	 * Feature value array column of general result table. It is an array contains feature values for that entity
	 */
	public static final String GENERAL_R_TABLE_FEATURE_VALUE_ARRAY_ATTR_NAME = "feature_value_array";
	/**
	 * Attribute type of feature_value_array column
	 */
	public static final String GENERAL_R_TABLE_FEATURE_VALUE_ARRAY_ATTR_TYPE = FLOAT_ARRAY_TYPE;
	/**
	 * Prediction value column of general result table. For each entity, learned model calculates entity's prediction value
	 */
	public static final String GENERAL_R_TABLE_C_ATTR_NAME = "c";
	/**
	 * Attribute type of c column
	 */
	public static final String GENERAL_R_TABLE_C_ATTR_TYPE = FLOAT_TYPE;
	/**
	 * Truth value column of general result table. For each entity, learned model determines whether entity is positive or not
	 */
	public static final String GENERAL_R_TABLE_TRUTH_VAL_ATTR_NAME = "truth_val";
	/**
	 * Attribute type of truth_value column
	 */
	public static final String GENERAL_R_TABLE_TRUTH_VAL_ATTR_TYPE = BOOLEAN_TYPE;
	/**
	 * Feature id array column of general result table. It is an array contains feature ids whose values are nonzero for that entity
	 */
	public static final String GENERAL_R_TABLE_FEATURE_ID_ARRAY_ATTR_NAME = "feature_id_array";
	/**
	 * Attribute type of feature id array
	 */
	public static final String GENERAL_R_TABLE_FEATURE_ID_ARRAY_ATTR_TYPE = INT_ARRAY_TYPE;
	
	/**
	 * Id column of extracted features table. It is id of the entity
	 */
	public static final String EXTRACTED_FEATURES_ID_ATTR_NAME = "id";
	/**
	 * Feature. When tf idf bag of words is used, features are words. This column represents the feature word
	 */
	public static final String EXTRACTED_FEATURES_FEATURE_NAME_ATTR_NAME = "feature";
	/**
	 * Value of feature. When tf-idf bag of words is used, its tf-idf value of that word in that example
	 */
	public static final String EXTRACTED_FEATURES_FEATURE_VALUE_ATTR_NAME = "feature_value";
	/**
	 * This represents id of the feature. Each feature has a unique id. Feature ids start from 1.
	 */
	public static final String EXTRACTED_FEATURES_FEATURE_ID_ATTR_NAME = "feature_id";
	
	/**
	 * This function extracts feature values for each entity
	 */
	public static final String GET_STATS_FEATURES_FUNCTION_NAME = "getStatsFeatures";
	/**
	 * This function extracts feature values for each entity including the newly inserted entity
	 */
	public static final String GET_STATS_FEATURES_WITH_NEW_ENTITY_FUNCTION_NAME = "getStatsFeaturesWithNewEntity";
	/**
	 * This function normalize extracted features with feature = (feature - min_of_feature) / (max_of_feature - min_of_feature)
	 * Then, it retrieves m factor which is maximum norm 1 of example
	 */
	public static final String NORMALIZE_GET_M_FACTOR_FUNCTION_NAME = "normalizeGetM_Factor";
	/**
	 * This function creates training table that sgd will use. It contains sgd format of examples i.e. example_label feature_id:feature_value ...
	 */
	public static final String PREPARE_TRAIN_FUNCTION_NAME = "prepare_train";
	/**
	 * This function calls sgd to make training
	 */
	public static final String TRAIN_FUNCTION_NAME = "runsgd";
	/**
	 * This function calls dotproduct method to obtain dotproduct of two vectors. It is optimized for sparse vectors.
	 */
	public static final String DOT_PRODUCT_FUNCTION_NAME = "dotprdctint";
	/**
	 * This function is standard PostGreSQL function to convert list of tuples of one attribute to an array.
	 */
	public static final String ARRAY_AGGREGATE_FUNCTION_NAME = "array_agg";
	/**
	 * Name of the trigger function for entity tables
	 */
	public static final String ENTITY_TRIGGER_FUNCTION_NAME = "cls_entity_trig_fnc";
	/**
	 * Name of the trigger function for positive example tables
	 */
	public static final String POS_TRIGGER_FUNCTION_NAME = "ptrig";
	/**
	 * Name of the trigger function for negative example tables
	 */
	public static final String NEG_TRIGGER_FUNCTION_NAME = "ntrig";
	/**
	 * The base trigger name for entity table (that might be updated)
	 */
	public static final String ENTITY_TRIGGER_BASE_NAME = "ent_trigger";
	/**
	 * The base trigger name for positive example table (that might be updated)
	 */
	public static final String POS_TRIGGER_BASE_NAME = "pos_trigger";
	/**
	 * The base trigger name for negative example table (that might be updated)
	 */
	public static final String NEG_TRIGGER_BASE_NAME = "neg_trigger";
	
	/**
	 * When using sgd, there are two modes: train and incremental (this is for updates). 
	 */
	public static final String TRAIN_MODE = "train";
	
	/**
	 * delimiter between a name and id to make that name unique since id is unique
	 */
	private static final String NAME_ID_DELIMITER = "_";
}
