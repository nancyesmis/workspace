package wisc.db.equivalence;

/**
 * Contains equivalence view schema related database items.
 * It contains table names, table attributes, trigger function names, trigger names, method to retrieve unique table names.
 * 
 * @author koc
 *
 */
public class EqViewSchema {
	/**
	 * Returns general catalog table name that contains view id and catalog name for that view
	 * 
	 * @return general catalog table name
	 */
	public static String getIdAllCatalogsTableName() {
		return EQ_VIEW_ID_ALL_CATALOGS_TABLE;
	}
	
	/**
	 * Returns catalog table name that contains rule table name, its view id and its weight
	 * 
	 * @return rules id weight table
	 */
	public static String getRulesIdWeightTableName() {
		return EQ_VIEW_RULES_ID_WEIGHT_TABLE;
	}
	
	/**
	 * Returns catalog table name for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return
	 */
	public static String getViewCatalogTableName(int viewId) {
		return getTableName(EQ_VIEW_CATALOG_TABLE_BASE, viewId);
	}
	
	/**
	 * Returns entity partition table name for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return entity partition table name
	 */
	public static String getEPTableName(int viewId) {
		return getTableName(ENTITY_PARTITIONS_TABLE_BASE, viewId);
	}
	
	/**
	 * Returns soft rule partition table name for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return soft rule partition table name
	 */
	public static String getSoftRulePartTableName(int viewId) {
		return getTableName(SOFT_RULE_W_PARTITIONS_TABLE_BASE, viewId);
	}
	
	/**
	 * Returns hard eq rule partition table name for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return hard eq rule partition table name
	 */
	public static String getHardEQRulePartTableName(int viewId) {
		return getTableName(HARD_EQ_RULE_W_PARTITIONS_TABLE_BASE, viewId);
	}
	
	/**
	 * Returns hard neq partition table name for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return hard neq rule partition table name
	 */
	public static String getHardNEQRulePartTableName(int viewId) {
		return getTableName(HARD_NEQ_RULE_W_PARTITIONS_TABLE_BASE, viewId);
	}
	
	/**
	 * Returns temp partitions table name for the view with given id.
	 * <p>
	 * This table is used temporarily during reorganization of partitions on the disk
	 * 
	 * @param viewId id of the view
	 * @return temp partitions table name
	 */
	public static String getTempPartitionsTableName(int viewId) {
		return getTableName(TEMP_PARTITIONS_TABLE_BASE, viewId);
	}
	
	/**
	 * Returns temp clusters table name for the view with given id
	 * <p>
	 * This table is used for bulk copying during update of view
	 * 
	 * @param viewId id of the view
	 * @return temp clusters table name
	 */
	public static String getTempClustersTableName(int viewId) {
		return getTableName(TEMP_CLUSTERS_TABLE_BASE, viewId);
	}
	
	/**
	 * Returns the index name for entity partitions table for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return index name for entity partitions table
	 */
	public static String getEnPTableIndexName(int viewId) {
		return getIndexName(EN_P_TABLE_INDEX_NAME, viewId);
	}
	
	/**
	 * Returns the index name for entity clusters table for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return index name for entity clusters table
	 */
	public static String getEnCTableIndexName(int viewId) {
		return getIndexName(EN_C_TABLE_INDEX_NAME, viewId);
	}
	
	/**
	 * Returns the index name for soft rule partitions table for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return index name for soft rule partitions table
	 */
	public static String getSoftRulePartTableIndexName(int viewId) {
		return getIndexName(SOFT_RULE_P_TABLE_INDEX_NAME, viewId);
	}
	
	/**
	 * Returns the index name for hard eq rule partitions table for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return index name for hard eq rule partitions table
	 */
	public static String getHardEQRulePartTableIndexName(int viewId) {
		return getIndexName(HARD_EQ_P_RULE_TABLE_INDEX_NAME, viewId);
	}
	
	/**
	 * Returns the index name for hard neq rule partitions table for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return index name for hard neq rule partitions table
	 */
	public static String getHardNEQRulePartTableIndexName(int viewId) {
		return getIndexName(HARD_NEQ_P_RULE_TABLE_INDEX_NAME, viewId);
	}
	
	/**
	 * Returns the trigger function name for entity table name. This function name is common for all views
	 * 
	 * @return trigger function name for entity table
	 */
	public static String getEntityTriggerFunction() {
		return ENTITY_TRIGGER_FUNCTION;
	}
	
	/**
	 * Returns the trigger function name for soft rule tables. This function name is common for all views
	 * 
	 * @return trigger function name for soft rule tables
	 */
	public static String getSoftRuleTriggerFunction() {
		return SOFT_RULE_TRIGGER_FUNCTION;
	}
	
	/**
	 * Returns the trigger function name for hard eq rule tables. This function name is common for all views
	 * 
	 * @return trigger function name for hard eq rule tables
	 */
	public static String getHardEQRuleTriggerFunction() {
		return HARD_EQ_RULE_TRIGGER_FUNCTION;
	}
	
	/**
	 * Returns the trigger function name for hard neq rule tables. This function name is common for all views
	 * 
	 * @return trigger function name fpr hard neq rule tables
	 */
	public static String getHardNEQRuleTriggerFunction() {
		return HARD_NEQ_RULE_TRIGGER_FUNCTION;
	}
	
	/**
	 * Returns the trigger name for entity table for the view with given id
	 * 
	 * @param viewId id of the view
	 * @return trigger name for entity table
	 */
	public static String getEntityTrigger(int viewId) {
		return ENTITY_TRIGGER + NAME_ID_DELIMITER + viewId;
	}
	
	/**
	 * Returns the trigger name for soft rule table for the view with given id
	 * <p>
	 * Since there might be more than one soft rule table, their index in the list is used to make sure that trigger names are unique
	 * 
	 * @param viewId id of the view
	 * @param ruleTableIndex index of the rule table
	 * @return trigger name for soft rule table
	 */
	public static String getSoftRuleTrigger(int viewId, int ruleTableIndex) {
		return getTriggerName(SOFT_RULE_TRIGGER, viewId, ruleTableIndex);
	}
	
	/**
	 * Returns the trigger name for hard eq rule table for the view with given id
	 * <p>
	 * Since there might be more than one hard eq rule table, their index in the list is used to make sure that trigger names are unique
	 * 
	 * @param viewId id of the view
	 * @param ruleTableIndex index of the rule table
	 * @return trigger name for hard eq rule table
	 */
	public static String getHardEQRuleTrigger(int viewId, int ruleTableIndex) {
		return getTriggerName(HARD_EQ_RULE_TRIGGER, viewId, ruleTableIndex);
	}
	
	/**
	 * Returns the trigger name for hard neq rule table for the view with given id
	 * <p>
	 * Since there might be more than one hard neq rule table, their index in the list is used to make sure that trigger names are unique
	 * 
	 * @param viewId id of the view
	 * @param ruleTableIndex index of the rule table
	 * @return trigger name for hard neq rule table
	 */
	public static String getHardNEQRuleTrigger(int viewId, int ruleTableIndex) {
		return getTriggerName(HARD_NEQ_RULE_TRIGGER, viewId, ruleTableIndex);
	}
	
	/**
	 * Returns the concatenated name of table name with the given view id
	 * 
	 * @param genericTableName generic table name
	 * @param viewId id of the view
	 * @return concatenated name of the table
	 */
	private static String getTableName(String genericTableName, int viewId) {
		return getName(genericTableName, viewId);
	}
	
	/**
	 * Returns the concatenated name of index name with the given view id
	 * 
	 * @param genericIndexName generic index name
	 * @param viewId id of the view
	 * @return concatenated name of the index
	 */
	private static String getIndexName(String genericIndexName, int viewId) {
		return getName(genericIndexName, viewId);
	}
	
	/**
	 * Returns the concatenated name of the trigger name with the given id and given index of rule table
	 * 
	 * @param genericTriggerName generic trigger name
	 * @param viewId id of the view
	 * @param tableIndex index of the rule table
	 * @return concatenated name of the trigger
	 */
	private static String getTriggerName(String genericTriggerName, int viewId, int tableIndex) {
		return getName(genericTriggerName, viewId) + NAME_ID_DELIMITER + tableIndex;
	}
	
	/**
	 * Returns the concatenated string of given name with given id
	 * 
	 * @param genericName generic name
	 * @param viewId id of the view
	 * @return generic name concatenated with view id
	 */
	private static String getName(String genericName, int viewId) {
		return genericName + NAME_ID_DELIMITER + viewId;
	}
	
	/**
	 * Global id table for equivalence views
	 * this table contains unique id for each view and its catalog table name
	 */
	public static final String EQ_VIEW_ID_ALL_CATALOGS_TABLE = "eq_view_id_table";
	/**
	 * Rule Table catalog. It contains rule table names, id of the views they are based on and weights of the rules
	 */
	public static final String EQ_VIEW_RULES_ID_WEIGHT_TABLE = "eq_view_rules_id_weight_table";
	/**
	 * Keeps view-related variables such as acc_cost, reorganizeTime. Each view has this catalog table 
	 * with the name format as BASE"_"view_id
	 */
	public static final String EQ_VIEW_CATALOG_TABLE_BASE = "eq_view_catalog";
	/**
	 * Contains entity ids with their partition ids. Each view has this table with the name format as
	 * BASE"_"view_id
	 */
	public static final String ENTITY_PARTITIONS_TABLE_BASE = "entity_partitions";
	/**
	 * Contains soft rule source and destination vertex(entity id) with partition ids of vertex.
	 * Each view has this table with the name format as BASE"_"view_id
	 */
	public static final String SOFT_RULE_W_PARTITIONS_TABLE_BASE = "soft_edges_partitions";
	/**
	 * Contains hard eq rule source and destination vertex(entity id) with partition ids of vertex.
	 * Each view has this table with the name format as BASE"_"view_id
	 */
	public static final String HARD_EQ_RULE_W_PARTITIONS_TABLE_BASE = "hard_eq_edges_partitions";
	/**
	 * Contains hard neq rule source and destination vertex(entity id) with partition ids of vertex.
	 * Each view has this table with the name format as BASE"_"view_id 
	 */
	public static final String HARD_NEQ_RULE_W_PARTITIONS_TABLE_BASE = "hard_neq_edges_partitions";
	
	/**
	 * Contains on-memory partitions and used when on-memory partitions are being written to disk
	 */
	public static final String TEMP_PARTITIONS_TABLE_BASE = "temppartitions";
	/**
	 * Contains entity ids with their cluster ids. This table is being used when cluster table is being update
	 * after update of each rule
	 */
	public static final String TEMP_CLUSTERS_TABLE_BASE = "tempclusters";
	
	/**
	 * index name base for entity partitions table
	 */
	public static final String EN_P_TABLE_INDEX_NAME = "ind1";
	/**
	 * index name base for soft rule partitions table
	 */
	public static final String SOFT_RULE_P_TABLE_INDEX_NAME = "ind2";
	/**
	 * index name base for hard eq rule partitions table
	 */
	public static final String HARD_EQ_P_RULE_TABLE_INDEX_NAME = "ind4";
	/**
	 * index name base for hard neq rule partitions table
	 */
	public static final String HARD_NEQ_P_RULE_TABLE_INDEX_NAME = "ind3";
	/**
	 * index name base for view (contains entity ids and clusters) table
	 */
	public static final String EN_C_TABLE_INDEX_NAME = "ind5";
	
	/**
	 * trigger function name of entity table
	 */
	public static final String ENTITY_TRIGGER_FUNCTION = "eq_entity_trig_fnc";
	
	/**
	 * trigger function name of soft rule table
	 */
	public static final String SOFT_RULE_TRIGGER_FUNCTION = "soft_trigger_fnc";
	/**
	 * trigger function name of hard eq rule table
	 */
	public static final String HARD_EQ_RULE_TRIGGER_FUNCTION = "hard_eq_trigger_fnc";
	/**
	 * trigger function name of hard neq rule table
	 */
	public static final String HARD_NEQ_RULE_TRIGGER_FUNCTION = "hard_neq_trigger_fnc";
	
	/**
	 * trigger name of entity table
	 */
	public static final String ENTITY_TRIGGER = "ent_trigger";
	
	/**
	 * trigger name of soft rule table
	 */
	public static final String SOFT_RULE_TRIGGER = "soft_trigger";
	/**
	 * trigger name of hard eq rule table
	 */
	public static final String HARD_EQ_RULE_TRIGGER = "hard_eq_trigger";
	/**
	 * trigger name of hard neq rule table
	 */
	public static final String HARD_NEQ_RULE_TRIGGER = "hard_neq_trigger";
	
	/**
	 * DELIMITER between table name and id
	 */
	public static final String NAME_ID_DELIMITER = "_";
	
	/**
	 * integer data type of PostGreSQL
	 */
	public static final String INT_TYPE = "int";
	/**
	 * float data type of PostGreSQL
	 */
	public static final String FLOAT8_TYPE = "float8";
	/**
	 * text data type of PostGreSQL
	 */
	public static final String TEXT_TYPE = "text";
	
	/**
	 * partition id
	 */
	public static final String PID_ATTR = "pid";
	/**
	 * partition id 1 (used when two vertices have different partition ids e.g. edge for hard neq rule, since they cannot be in same partition)
	 */
	public static final String PID1_ATTR = "pid1";
	/**
	 * partition id 2 (used when two vertices have different partition ids e.g. edge for hard neq rule, since they cannot be in same partition)
	 */
	public static final String PID2_ATTR = "pid2";
	/**
	 * node(entity) id
	 */
	public static final String NODE_ATTR = "node";
	/**
	 * node1 (this is used for rule partition tables since they contain an edge of source and destination vertex. node1 represents src)
	 */
	public static final String NODE1_ATTR = "node1";
	/**
	 * node2 (this is used for rule partition tables since they contain an edge of source and destination vertex. node1 represents dst)
	 */
	public static final String NODE2_ATTR = "node2";
	/**
	 * src of an edge in rule table
	 */
	public static final String SRC_ATTR = "src";
	/**
	 * dst of an edge in rule table
	 */
	public static final String DST_ATTR = "dst";
	/**
	 * id of the view
	 */
	public static final String VIEW_ID_ATTR = "view_id";
	
	/**
	 * id of the view attribute in general catalog table
	 */
	public static final String EQ_V_C_ID_ALL_CATALOGS_ID_ATTR_NAME = VIEW_ID_ATTR;
	/**
	 * attribute type of view id
	 */
	public static final String EQ_V_C_ID_ALL_CATALOGS_ID_ATTR_TYPE = INT_TYPE;
	/**
	 * name of the view attribute in general catalog table
	 */
	public static final String EQ_V_C_ID_ALL_CATALOGS_VIEW_NAME_ATTR_NAME = "view_name";
	/**
	 * attribute type of view name
	 */
	public static final String EQ_V_C_ID_ALL_CATALOGS_VIEW_NAME_ATTR_TYPE = TEXT_TYPE;
	/**
	 * name of the catalog of view in general catalog table
	 */
	public static final String EQ_V_C_ID_ALL_CATALOGS_CATALOG_NAME_ATTR_NAME = "catalog_name";
	/**
	 * attribute type of catalog name
	 */
	public static final String EQ_V_C_ID_ALL_CATALOGS_CATALOG_NAME_ATTR_TYPE = TEXT_TYPE;
	
	/**
	 * id of the view in rule table catalog
	 */
	public static final String EQ_V_SOFT_RULES_WEIGHT_TABLE_VIEW_ID_ATTR_NAME = VIEW_ID_ATTR;
	/**
	 * attribute type of view id
	 */
	public static final String EQ_V_SOFT_RULES_WEIGHT_TABLE_VIEW_ID_ATTR_TYPE = INT_TYPE;
	/**
	 * name of the rule table in rule table catalog
	 */
	public static final String EQ_V_SOFT_RULES_WEIGHT_TABLE_SOFT_RULE_TABLE_ATTR_NAME = "rule_table";
	/**
	 * attribute type of rule table
	 */
	public static final String EQ_V_SOFT_RULES_WEIGHT_TABLE_SOFT_RULE_TABLE_ATTR_TYPE = TEXT_TYPE;
	/**
	 * weight of the rule table in rule table catalog
	 */
	public static final String EQ_V_SOFT_RULES_WEIGHT_TABLE_SOFT_RULE_TABLE_WEIGHT_ATTR_NAME = "weight";
	/**
	 * attribute type of weight
	 */
	public static final String EQ_V_SOFT_RULES_WEIGHT_TABLE_SOFT_RULE_TABLE_WEIGHT_ATTR_TYPE = FLOAT8_TYPE;
	
	/**
	 * id of the view in view catalog table
	 */
	public static final String EQ_V_C_CATALOG_ID_ATTR_NAME = VIEW_ID_ATTR;
	/**
	 * attribute type of view id
	 */
	public static final String EQ_V_C_CATALOG_ID_ATTR_TYPE = INT_TYPE;
	/**
	 * update algorithm used for the view in view catalog table
	 */
	public static final String EQ_V_C_UPDATE_ALGORITHM_ATTR_NAME = "update_alg";
	/**
	 * attribute type of update alg
	 */
	public static final String EQ_V_C_UPDATE_ALGORITHM_ATTR_TYPE = TEXT_TYPE;
	/**
	 * number of soft rules in view catalog table
	 * <p>
	 * This attribute used for efficiency, because when there is only 1 soft rule, then it is not needed to calculate weights
	 * and check a weight is more than threshold
	 */
	public static final String EQ_V_C_NUM_SOFT_RULE_ATTR_NAME = "num_soft_rule";
	/**
	 * attribute type of num soft rule
	 */
	public static final String EQ_V_C_NUM_SOFT_RULE_ATTR_TYPE = INT_TYPE;
	/**
	 * total soft rule weights in view catalog table
	 * <p>
	 * This attribute is used to check whether an edge's weight is more than threshold
	 */
	public static final String EQ_V_C_TOTAL_SOFT_RULE_WEIGHT_ATTR_NAME = "total_soft_rw";
	/**
	 * attribute type of total soft rule weights
	 */
	public static final String EQ_V_C_TOTAL_SOFT_RULE_WEIGHT_ATTR_TYPE = FLOAT8_TYPE;
	/**
	 * accumulated cost in view catalog table
	 */
	public static final String EQ_V_C_ACCUMULATED_COST_ATTR_NAME = "acc";
	/**
	 * attribute type of acc
	 */
	public static final String EQ_V_C_ACCUMULATED_COST_ATTR_TYPE = FLOAT8_TYPE;
	/**
	 * reorganize time in view catalog table
	 */
	public static final String EQ_V_C_REORGANIZE_TIME_ATTR_NAME = "reorganize_time";
	/**
	 * attribute type of reorganize time
	 */
	public static final String EQ_V_C_REORGANIZE_TIME_ATTR_TYPE = FLOAT8_TYPE;
	/**
	 * tao in view catalog table
	 */
	public static final String EQ_V_C_TAO_ATTR_NAME = "tao";
	/**
	 * attribute type of tao
	 */
	public static final String EQ_V_C_TAO_ATTR_TYPE = FLOAT8_TYPE;
	
	/**
	 * pid in entity partitions table
	 */
	public static final String EP_PID_ATTR_NAME = PID_ATTR;
	/**
	 * attribute type of pid
	 */
	public static final String EP_PID_ATTR_TYPE = INT_TYPE;
	/**
	 * node(entity id) in entity partitions table
	 */
	public static final String EP_NODE_ATTR_NAME = NODE_ATTR;
	/**
	 * attribute type of node
	 */
	public static final String EP_NODE_ATTR_TYPE = INT_TYPE;
	
	/**
	 * node(entity id) in view
	 */
	public static final String EC_NODE_ATTR_NAME = NODE_ATTR;
	/**
	 * attribute type of node
	 */
	public static final String EC_NODE_ATTR_TYPE = INT_TYPE;
	/**
	 * cluster id in view
	 */
	public static final String EC_CID_ATTR_NAME = "cid";
	/**
	 * attribute type of cid
	 */
	public static final String EC_CID_ATTR_TYPE = INT_TYPE;
	
	/**
	 * pid1 (partition id of node1) in soft rule partitions table
	 */
	public static final String SOFT_R_P_PID1_ATTR_NAME = PID1_ATTR;
	/**
	 * attribute type of pid1
	 */
	public static final String SOFT_R_P_PID1_ATTR_TYPE = INT_TYPE;
	/**
	 * node1(src of soft edge) in soft rule partitions table
	 */
	public static final String SOFT_R_P_NODE1_ATTR_NAME = NODE1_ATTR;
	/**
	 * attribute type of node1
	 */
	public static final String SOFT_R_P_NODE1_ATTR_TYPE = INT_TYPE;
	/**
	 * pid2 (partition id of node2) in soft rule partitions table
	 */
	public static final String SOFT_R_P_PID2_ATTR_NAME = PID2_ATTR;
	/**
	 * attribute type of pid2
	 */
	public static final String SOFT_R_P_PID2_ATTR_TYPE = INT_TYPE;
	/**
	 * node2(dst of soft edge) in soft rule partitions table
	 */
	public static final String SOFT_R_P_NODE2_ATTR_NAME = NODE2_ATTR;
	/**
	 * attribute type of node2
	 */
	public static final String SOFT_R_P_NODE2_ATTR_TYPE = INT_TYPE;
	/**
	 * weight of the edge in soft rule partitions table
	 */
	public static final String SOFT_R_P_WEIGHT_ATTR_NAME = "weight";
	/**
	 * attribute type of weight 
	 */
	public static final String SOFT_R_P_WEIGHT_ATTR_TYPE = FLOAT8_TYPE;
	
	/**
	 * pid (partition id of node1 and node2. Since this is hard eq rule, if such rule exists between node1 and node2,
	 * then they are in same partition for sure) in hard eq rule partitions table
	 */
	public static final String H_EQ_R_P_PID_ATTR_NAME = PID_ATTR;
	/**
	 * attribute type of pid
	 */
	public static final String H_EQ_R_P_PID_ATTR_TYPE = INT_TYPE;
	/**
	 * node1(src of hard eq edge) in hard eq rule partitions table
	 */
	public static final String H_EQ_R_P_NODE1_ATTR_NAME = NODE1_ATTR;
	/**
	 * attribute type of node1
	 */
	public static final String H_EQ_R_P_NODE1_ATTR_TYPE = INT_TYPE;
	/**
	 * node2(dst of hard eq edge) in hard eq rule partitions table
	 */
	public static final String H_EQ_R_P_NODE2_ATTR_NAME = NODE2_ATTR;
	/**
	 * attribute type of node2
	 */
	public static final String H_EQ_R_P_NODE2_ATTR_TYPE = INT_TYPE;
	
	/**
	 * pid1 (partition id of node1) in hard neq rule partitions table
	 */
	public static final String H_NEQ_R_P_PID1_ATTR_NAME = PID1_ATTR;
	/**
	 * attribute type of pid1
	 */
	public static final String H_NEQ_R_P_PID1_ATTR_TYPE = INT_TYPE;
	/**
	 * node1(src of hard neq edge) in hard neq rule partitions table
	 */
	public static final String H_NEQ_R_P_NODE1_ATTR_NAME = NODE1_ATTR;
	/**
	 * attribute type of node1
	 */
	public static final String H_NEQ_R_P_NODE1_ATTR_TYPE = INT_TYPE;
	/**
	 * pid2 (partition id of node2) in hard neq rule partitions table
	 */
	public static final String H_NEQ_R_P_PID2_ATTR_NAME = PID2_ATTR;
	/**
	 * attribute type of pid2
	 */
	public static final String H_NEQ_R_P_PID2_ATTR_TYPE = INT_TYPE;
	/**
	 * node2(dst of hard neq edge) in hard neq rule partitions table
	 */
	public static final String H_NEQ_R_P_NODE2_ATTR_NAME = NODE2_ATTR;
	/**
	 * attribute type of node2
	 */
	public static final String H_NEQ_R_P_NODE2_ATTR_TYPE = INT_TYPE;
	
	/**
	 * node of temp partitions table
	 */
	public static final String TEMP_PARTITIONS_TABLE_OLD_PARTITION_ATTR_NAME = "oldpartid";
	/**
	 * attribute type of node
	 */
	public static final String TEMP_PARTITIONS_TABLE_OLD_PARTITION_ATTR_TYPE = INT_TYPE;
	/**
	 * partition id of temp partitions table
	 */
	public static final String TEMP_PARTITIONS_TABLE_NEW_PARTITION_ATTR_NAME = "partid";
	/**
	 * attribute type of partition id
	 */
	public static final String TEMP_PARTITIONS_TABLE_NEW_PARTITION_ATTR_TYPE = INT_TYPE;
	

	/**
	 * node of temp view table
	 */
	public static final String TEMP_CLUSTERS_TABLE_NODE_ATTR_NAME = "nd";
	/**
	 * attribute type of node
	 */
	public static final String TEMP_CLUSTERS_TABLE_NODE_ATTR_TYPE = INT_TYPE;
	/**
	 * cluster id of temp view table
	 */
	public static final String TEMP_CLUSTERS_TABLE_CID_ATTR_NAME = "clsid";
	/**
	 * attribute type of cluster id
	 */
	public static final String TEMP_CLUSTERS_TABLE_CID_ATTR_TYPE = INT_TYPE;
	
	/**
	 * Represents src attribute of rule table can be found at 1st column
	 */
	public final static int ID_OF_SRC_ATTR = 1;
	/**
	 * Represents dst attribute of rule table can be found at 2nd column
	 */
	public final static int ID_OF_DST_ATTR = 2;
	
	/**
	 * Used during information is written to the file for bulk copying
	 */
	public static final String DELIMITER_FOR_COPYING = ",";
	/**
	 * infinity (weight of hard eq rule)
	 */
	public static final String INFINITY = "infinity";
	/**
	 * minus infinity (weight of hard neq rule)
	 */
	public static final String MINUS_INFINITY = "-infinity";
}
