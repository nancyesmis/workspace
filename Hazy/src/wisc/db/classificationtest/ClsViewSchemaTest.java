package wisc.db.classificationtest;

import wisc.db.classification.ClsViewSchema;
import wisc.db.classification.ClsViewSetup;
import junit.framework.TestCase;

/**
 * Tests {@link ClsViewSchema} class
 * 
 * @author koc
 *
 */
public class ClsViewSchemaTest extends TestCase {
	int viewId = 5;
	
	/**
	 * Test case constructor 
	 * 
	 * @param name
	 */
	public ClsViewSchemaTest(String name) {
		super(name);
	}

	/**
	 * Test case set up
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ClsViewSetup.loadHazyConfParameters();
	}

	/**
	 * Test case tear down
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * tests positive trigger name method
	 */
	public void testGetPositiveTriggerName() {
		assertTrue(ClsViewSchema.getPositiveTriggerName(viewId).equals(ClsViewSchema.POS_TRIGGER_BASE_NAME + "_" + viewId));
	}

	/**
	 * tests negative trigger name method
	 */
	public void testGetNegativeTriggerName() {
		assertTrue(ClsViewSchema.getNegativeTriggerName(viewId).equals(ClsViewSchema.NEG_TRIGGER_BASE_NAME + "_" + viewId));
	}

	/**
	 * tests temp general r table method
	 */
	public void testGetTempGeneralRTableName() {
		assertTrue(ClsViewSchema.getTempGeneralRTableName(viewId).equals(ClsViewSchema.TEMP_GENERAL_R_TABLE_BASE_NAME + "_" + viewId));
	}

	/**
	 * tests extracted features table method
	 */
	public void testGetExtractedFeaturesTable() {
		assertTrue(ClsViewSchema.getExtractedFeaturesTable(viewId).equals(ClsViewSchema.EXTRACTED_FEATURES_BASE_NAME + viewId));
	}

	/**
	 * tests general r table method
	 */
	public void testGetGeneralRTable() {
		assertTrue(ClsViewSchema.getGeneralRTable(viewId).equals(ClsViewSchema.GENERAL_R_TABLE_BASE_NAME + "_" + viewId));
	}

	/**
	 * tests reservoir name method
	 */
	public void testGetReservoirWithViewIdAndSvmId() {
		assertTrue(ClsViewSchema.getReservoirWithViewId(viewId).equals(ClsViewSchema.RESERVOIR_TABLE_BASE_NAME + "_" + viewId));
	}

	/**
	 * tests get feature ids table name method
	 */
	public void testGetFeatureIdsTableName() {
		assertTrue(ClsViewSchema.getFeatureIdsTableName(viewId).equals(ClsViewSchema.FEATURE_IDS_TABLE_BASE_NAME + viewId));
	}

	/**
	 * tests get training table name method
	 */
	public void testGetTrainingTableName() {
		assertTrue(ClsViewSchema.getTrainingTableName(viewId).equals(ClsViewSchema.TRAINING_TABLE_BASE_NAME + viewId));
	}

	/**
	 * tests get catalog table name method
	 */
	public void testGetCatalogTableName() {
		assertTrue(ClsViewSchema.getCatalogTableName(viewId).equals(ClsViewSchema.CLS_VIEW_CATALOG_BASE + "_" + viewId));
	}

	/**
	 * tests get name with id method
	 */
	public void testGetNameWithId() {
		String base = "baseTableName";
		assertTrue(ClsViewSchema.getNameWithId(base, viewId).equals(base + "_" + viewId));
	}

}
