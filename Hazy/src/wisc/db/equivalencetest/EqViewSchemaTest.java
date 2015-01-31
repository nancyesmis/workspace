package wisc.db.equivalencetest;

import wisc.db.equivalence.EqViewSchema;
import junit.framework.TestCase;

/**
 * Tests {@link EqViewSchema} class
 * 
 * @author koc
 *
 */
public class EqViewSchemaTest extends TestCase {
	int viewId = 6;
	
	/**
	 * default constructor
	 * 
	 * @param name
	 */
	public EqViewSchemaTest(String name) {
		super(name);
	}

	/**
	 * setup method
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests get id all catalogs table name is returned correctly
	 */
	public void testGetIdAllCatalogsTableName() {
		assertTrue(EqViewSchema.getIdAllCatalogsTableName().equals(EqViewSchema.EQ_VIEW_ID_ALL_CATALOGS_TABLE));
	}

	/**
	 * Tests rules id weight table name is returned correctly
	 */
	public void testGetRulesIdWeightTableName() {
		assertTrue(EqViewSchema.getRulesIdWeightTableName().equals(EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE));
	}

	/**
	 * Tests view catalog table name is returned correctly for given view id
	 */
	public void testGetViewCatalogTableName() {
		assertTrue(EqViewSchema.getViewCatalogTableName(viewId).equals(EqViewSchema.EQ_VIEW_CATALOG_TABLE_BASE + "_" + viewId));
	}

	/**
	 * Tests view entity partition table name is returned correctly for given view id
	 */
	public void testGetEPTableName() {
		assertTrue(EqViewSchema.getEPTableName(viewId).equals(EqViewSchema.ENTITY_PARTITIONS_TABLE_BASE + "_" + viewId));
	}

	/**
	 * Tests view soft rule partition table name is returned correctly for given view id
	 */
	public void testGetSoftRulePartTableName() {
		assertTrue(EqViewSchema.getSoftRulePartTableName(viewId).equals(EqViewSchema.SOFT_RULE_W_PARTITIONS_TABLE_BASE + "_" + viewId));
	}

	/**
	 * Tests view hard eq rule partition table name is returned correctly for given view id
	 */
	public void testGetHardEQRulePartTableName() {
		assertTrue(EqViewSchema.getHardEQRulePartTableName(viewId).equals(EqViewSchema.HARD_EQ_RULE_W_PARTITIONS_TABLE_BASE + "_" + viewId));
	}

	/**
	 * Tests view hard neq ruel partition table name is returned correctly for given view id
	 */
	public void testGetHardNEQRulePartTableName() {
		assertTrue(EqViewSchema.getHardNEQRulePartTableName(viewId).equals(EqViewSchema.HARD_NEQ_RULE_W_PARTITIONS_TABLE_BASE + "_" + viewId));
	}

	/**
	 * Tests view temp partitions table name is returned correctly for given view id
	 */
	public void testGetTempPartitionsTableName() {
		assertTrue(EqViewSchema.getTempPartitionsTableName(viewId).equals(EqViewSchema.TEMP_PARTITIONS_TABLE_BASE + "_" + viewId));
	}

	/**
	 * Tests view temp clusters table name is returned correctly for given view id
	 */
	public void testGetTempClustersTableName() {
		assertTrue(EqViewSchema.getTempClustersTableName(viewId).equals(EqViewSchema.TEMP_CLUSTERS_TABLE_BASE + "_" + viewId));
	}

	/**
	 * Tests view entity partition table index name is returned correctly for given view id
	 */
	public void testGetEnPTableIndexName() {
		assertTrue(EqViewSchema.getEnPTableIndexName(viewId).equals(EqViewSchema.EN_P_TABLE_INDEX_NAME + "_" + viewId));
	}

	/**
	 * Tests view entity cluster table index name is returned correctly for given view id
	 */
	public void testGetEnCTableIndexName() {
		assertTrue(EqViewSchema.getEnCTableIndexName(viewId).equals(EqViewSchema.EN_C_TABLE_INDEX_NAME + "_" + viewId));
	}

	/**
	 * Tests view soft rule partition table index name is returned correctly for given view id
	 */
	public void testGetSoftRulePartTableIndexName() {
		assertTrue(EqViewSchema.getSoftRulePartTableIndexName(viewId).equals(EqViewSchema.SOFT_RULE_P_TABLE_INDEX_NAME + "_" + viewId));
	}

	/**
	 * Tests view hard eq rule partition table index name is returned correctly for given view id
	 */
	public void testGetHardEQRulePartTableIndexName() {
		assertTrue(EqViewSchema.getHardEQRulePartTableIndexName(viewId).equals(EqViewSchema.HARD_EQ_P_RULE_TABLE_INDEX_NAME + "_" + viewId));
	}

	/**
	 * Tests view hard neq rule partition table index name is returned correctly for given view id
	 */
	public void testGetHardNEQRulePartTableIndexName() {
		assertTrue(EqViewSchema.getHardNEQRulePartTableIndexName(viewId).equals(EqViewSchema.HARD_NEQ_P_RULE_TABLE_INDEX_NAME + "_" + viewId));
	}

	/**
	 * Tests view soft rule trigger function name is returned correctly
	 */
	public void testGetSoftRuleTriggerFunction() {
		assertTrue(EqViewSchema.getSoftRuleTriggerFunction().equals(EqViewSchema.SOFT_RULE_TRIGGER_FUNCTION));
	}

	/**
	 * Tests view hard eq rule trigger function name is returned correctly
	 */
	public void testGetHardEQRuleTriggerFunction() {
		assertTrue(EqViewSchema.getHardEQRuleTriggerFunction().equals(EqViewSchema.HARD_EQ_RULE_TRIGGER_FUNCTION));
	}

	/**
	 * Tests view hard neq rule trigger function name is returned correctly
	 */
	public void testGetHardNEQRuleTriggerFunction() {
		assertTrue(EqViewSchema.getHardNEQRuleTriggerFunction().equals(EqViewSchema.HARD_NEQ_RULE_TRIGGER_FUNCTION));
	}

	/**
	 * Tests view soft rule trigger name is returned correctly for given view id
	 */
	public void testGetSoftRuleTrigger() {
		int ruleTableIndex = 5;	
		assertTrue(EqViewSchema.getSoftRuleTrigger(viewId, ruleTableIndex).equals(EqViewSchema.SOFT_RULE_TRIGGER + "_" + viewId + "_" + ruleTableIndex));
	}

	/**
	 * Tests view hard eq rule trigger name is returned correctly for given view id
	 */
	public void testGetHardEQRuleTrigger() {
		int ruleTableIndex = 5;
		assertTrue(EqViewSchema.getHardEQRuleTrigger(viewId, ruleTableIndex).equals(EqViewSchema.HARD_EQ_RULE_TRIGGER + "_" + viewId + "_" + ruleTableIndex));
	}

	/**
	 * Tests view hard neq rule trigger name is returned correctly for given view id
	 */
	public void testGetHardNEQRuleTrigger() {
		int ruleTableIndex = 5;
		assertTrue(EqViewSchema.getHardNEQRuleTrigger(viewId, ruleTableIndex).equals(EqViewSchema.HARD_NEQ_RULE_TRIGGER + "_" + viewId + "_" + ruleTableIndex));
	}

}
