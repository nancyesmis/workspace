package wisc.db.classificationtest;

import java.sql.ResultSet;
import java.sql.SQLException;

import wisc.db.classification.ClsViewSchema;
import wisc.db.classification.ClsViewSetup;
import wisc.db.classification.ClsViewSetupFactory;
import wisc.db.sql.DatabaseFactory;
import wisc.db.sql.QueryExecutor;
import junit.framework.TestCase;

/**
 * Tests {@link ClsViewSetup} class
 * 
 * @author koc
 *
 */
public class ClsViewSetupTest extends TestCase {
	ClsViewSetup clsVSetup;
	QueryExecutor sql;
	int reservoir1 = 0;
	int reservoir2 = 20;
	int reservoir3 = 0;
	int reservoir4 = 50;
	int noOp1 = 0;
	int noOp2 = 0;
	int noOp3 = 1;
	int noOp4 = 1;
	int viewId1;
	int viewId2;
	int viewId3;
	int viewId4;
	String viewName1 = "clsView1";
	String viewName2 = "clsView2";
	String viewName3 = "clsView3";
	String viewName4 = "clsView4";
	
	String cleanViewDatabaseQuery = "TRUNCATE general_cls_view_catalog; TRUNCATE update_table_view_id_catalog;";
	
	/**
	 * Default constructor
	 * 
	 * @param name
	 */
	public ClsViewSetupTest(String name) {
		super(name);
	}

	/**
	 * Retrieves clsVSetup object from {@link ClsViewSetupFactory} and retrieves QueryExecutor object from {@link DatabaseFactory}
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ClsViewSetup.loadHazyConfParameters();
		clsVSetup = ClsViewSetupFactory.getClsViewSetup();
		sql = DatabaseFactory.getQueryExecutor();
	}

	/**
	 * teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * tests clsViewSetup constructor
	 */
	public void testClsViewSetup() {
	}

	/**
	 * Inserts 3 views into the database and checks their ids are correct 
	 */
	public void testGetUniqueViewId() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		int id = clsVSetup.getUniqueViewId();
		assertTrue(id == 0);
		clsVSetup.insertIntoIdCatalog(0, "e1", viewName1);
		id = clsVSetup.getUniqueViewId();
		assertTrue(id == 1);
		clsVSetup.insertIntoIdCatalog(5, "e5", viewName2);
		id = clsVSetup.getUniqueViewId();
		assertTrue(id == 6);
	}
	
	/**
	 * Insert id and catalog name into general catalog table and checks whether they are inserted or not
	 */
	public void testInsertIntoIdCatalog() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		int id1 = clsVSetup.getUniqueViewId();
		clsVSetup.insertIntoIdCatalog(id1, "e1", viewName1);
		String query1 = "SELECT COUNT(*) FROM " + ClsViewSchema.CLS_VIEW_ID_ALL_CATALOGS_TABLE + " WHERE view_id = " + id1 + 
				" AND catalog_name = '" + ClsViewSchema.getCatalogTableName(id1) + "';";
		ResultSet set = sql.executeSelectQuery(query1);
		int count1 = 0;
		try {
			if(set.next())
				count1 = set.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int id2 = clsVSetup.getUniqueViewId();
		int count2 = 0;
		clsVSetup.insertIntoIdCatalog(id2, "e2", viewName2);
		String query2 = "SELECT COUNT(*) FROM " + ClsViewSchema.CLS_VIEW_ID_ALL_CATALOGS_TABLE + " WHERE view_id = " + id2 + 
				" AND catalog_name = '" + ClsViewSchema.getCatalogTableName(id2) + "';";
		ResultSet set2 = sql.executeSelectQuery(query2);
		
		try {
			if(set2.next())
				count2 = set2.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(id1 == 0);
		assertTrue(count1 == 1);
		assertTrue(id2 == 1);
		assertTrue(count2 == 1);
	}

	/**
	 * creates 2 view catalog tables and check whether they are created
	 */
	public void testCreateViewCatalogTable() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		int id1 = clsVSetup.getUniqueViewId();
		clsVSetup.insertIntoIdCatalog(id1, "e1", viewName1);
		int id2 = clsVSetup.getUniqueViewId();
		clsVSetup.insertIntoIdCatalog(id2, "e2", viewName2);
		clsVSetup.createViewCatalogTable(id1);
		clsVSetup.createViewCatalogTable(id2);
		
		int count1 = 0;
		int count2 = 0;
		
		try {
			String query = "select count(*) from pg_class where relname = '" + ClsViewSchema.getCatalogTableName(id1) + "'";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1 = set.getInt(1);
			
			query = "select count(*) from pg_class where relname = '" + ClsViewSchema.getCatalogTableName(id2) + "'";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				count2 = set.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertTrue(count1 == 1);
		assertTrue(count2 == 1);
	}

	/**
	 * creates 4 entity, pos example and neg example tables, insert them into general catalog and create catalog for them
	 */
	private void prepareForTest() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		String query = "DROP TABLE IF EXISTS e1; CREATE TABLE e1 (id int, name text); INSERT INTO e1 (SELECT * FROM testclassentities);";
		query += "DROP TABLE IF EXISTS e2; CREATE TABLE e2 (id int, name text); INSERT INTO e2 (SELECT * FROM testclassentities);";
		query += "DROP TABLE IF EXISTS e3; CREATE TABLE e3 (id int, name text); INSERT INTO e3 (SELECT * FROM testclassentities);";
		query += "DROP TABLE IF EXISTS e4; CREATE TABLE e4 (id int, name text); INSERT INTO e4 (SELECT * FROM testclassentities);";
		query += "DROP TABLE IF EXISTS p1; CREATE TABLE p1 (id int); INSERT INTO p1 (SELECT * FROM testclasspos);";
		query += "DROP TABLE IF EXISTS p2; CREATE TABLE p2 (id int); INSERT INTO p2 (SELECT * FROM testclasspos);";
		query += "DROP TABLE IF EXISTS p3; CREATE TABLE p3 (id int); INSERT INTO p3 (SELECT * FROM testclasspos);";
		query += "DROP TABLE IF EXISTS p4; CREATE TABLE p4 (id int); INSERT INTO p4 (SELECT * FROM testclasspos);";
		query += "DROP TABLE IF EXISTS n1; CREATE TABLE n1 (id int); INSERT INTO n1 (SELECT * FROM testclassneg);";
		query += "DROP TABLE IF EXISTS n2; CREATE TABLE n2 (id int); INSERT INTO n2 (SELECT * FROM testclassneg);";
		query += "DROP TABLE IF EXISTS n3; CREATE TABLE n3 (id int); INSERT INTO n3 (SELECT * FROM testclassneg);";
		query += "DROP TABLE IF EXISTS n4; CREATE TABLE n4 (id int); INSERT INTO n4 (SELECT * FROM testclassneg);";
		sql.executeUpdateQuery(query);
		
		viewId1 = clsVSetup.getUniqueViewId();
		clsVSetup.insertIntoIdCatalog(viewId1, "e1", viewName1);
		viewId2 = clsVSetup.getUniqueViewId();
		clsVSetup.insertIntoIdCatalog(viewId2, "e2", viewName2);
		viewId3 = clsVSetup.getUniqueViewId();
		clsVSetup.insertIntoIdCatalog(viewId3, "e3", viewName3);
		viewId4 = clsVSetup.getUniqueViewId();
		clsVSetup.insertIntoIdCatalog(viewId4, "e4", viewName4);
		clsVSetup.createViewCatalogTable(viewId1);
		clsVSetup.createViewCatalogTable(viewId2);
		clsVSetup.createViewCatalogTable(viewId3);
		clsVSetup.createViewCatalogTable(viewId4);
	}
	
	/**
	 * Tests creating of views until general r table for hazy are created
	 */
	public void testPrepareGeneralRTableForHazy() {
		prepareForTest();
		double m_factor1 = clsVSetup.prepareTrain(viewId1, "e1", "p1", "n1");
		clsVSetup.fillsViewCatalogTable(viewId1, viewName1, "e1", reservoir1, noOp1, m_factor1);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p1", "n1", reservoir1, noOp1, viewId1);
		double m_factor2 = clsVSetup.prepareTrain(viewId2, "e2", "p2", "n2");
		clsVSetup.fillsViewCatalogTable(viewId2, viewName2, "e2", reservoir2, noOp2, m_factor2);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p2", "n2", reservoir2, noOp2, viewId2);
		double m_factor3 = clsVSetup.prepareTrain(viewId3, "e3", "p3", "n3");
		clsVSetup.fillsViewCatalogTable(viewId3, viewName3, "e3", reservoir3, noOp3, m_factor3);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p3", "n3", reservoir3, noOp3, viewId3);
		double m_factor4 = clsVSetup.prepareTrain(viewId4, "e4", "p4", "n4");
		clsVSetup.fillsViewCatalogTable(viewId4, viewName4, "e4", reservoir4, noOp4, m_factor4);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p4", "n4", reservoir4, noOp4, viewId4);
		
		if(reservoir1 > 0)
			clsVSetup.createReservoir(viewId1);
		if(reservoir2 > 0)
			clsVSetup.createReservoir(viewId2);
		if(reservoir3 > 0)
			clsVSetup.createReservoir(viewId3);
		if(reservoir4 > 0)
			clsVSetup.createReservoir(viewId4);
		int featureSize1 = clsVSetup.getNumberOfFeatures(viewId1);
		int featureSize2 = clsVSetup.getNumberOfFeatures(viewId2);
		int featureSize3 = clsVSetup.getNumberOfFeatures(viewId3);
		int featureSize4 = clsVSetup.getNumberOfFeatures(viewId4);
		//train;
		int retValue1 = clsVSetup.train(viewId1, featureSize1, reservoir1, noOp1);
		assertTrue(retValue1 != -1);
		int retValue2 = clsVSetup.train(viewId2, featureSize2, reservoir2, noOp2);
		assertTrue(retValue2 != -1);
		int retValue3 = clsVSetup.train(viewId3, featureSize3, reservoir3, noOp3);
		assertTrue(retValue3 != -1);
		int retValue4 = clsVSetup.train(viewId4, featureSize4, reservoir4, noOp4);
		assertTrue(retValue4 != -1);
		
		clsVSetup.prepareGeneralRTableForHazy(viewId1, "e1");
		clsVSetup.prepareGeneralRTableForHazy(viewId2, "e2");
		clsVSetup.prepareGeneralRTableForHazy(viewId3, "e3");
		clsVSetup.prepareGeneralRTableForHazy(viewId4, "e4");
	}

	/**
	 * Tests of creating views until view catalog tables are filled
	 */
	public void testFillsViewCatalogTable() {
		prepareForTest();
		double m_factor1 = clsVSetup.prepareTrain(viewId1, "e1", "p1", "n1");
		clsVSetup.fillsViewCatalogTable(viewId1, viewName1, "e1", reservoir1, noOp1, m_factor1);
		double m_factor2 = clsVSetup.prepareTrain(viewId2, "e2", "p2", "n2");
		clsVSetup.fillsViewCatalogTable(viewId2, viewName2, "e2", reservoir2, noOp2, m_factor2);
		double m_factor3 = clsVSetup.prepareTrain(viewId3, "e3", "p3", "n3");
		clsVSetup.fillsViewCatalogTable(viewId3, viewName3, "e3", reservoir3, noOp3, m_factor3);
		double m_factor4 = clsVSetup.prepareTrain(viewId4, "e4", "p4", "n4");
		clsVSetup.fillsViewCatalogTable(viewId4, viewName4, "e4", reservoir4, noOp4, m_factor4);
	}

	/**
	 * Tests of creating views until view catalog table is updated 
	 */
	public void testFillsUpdateTableViewIdCatalogTable() {
		prepareForTest();
		double m_factor1 = clsVSetup.prepareTrain(viewId1, "e1", "p1", "n1");
		clsVSetup.fillsViewCatalogTable(viewId1, viewName1, "e1", reservoir1, noOp1, m_factor1);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p1", "n1", reservoir1, noOp1, viewId1);
		double m_factor2 = clsVSetup.prepareTrain(viewId2, "e2", "p2", "n2");
		clsVSetup.fillsViewCatalogTable(viewId2, viewName2, "e2", reservoir2, noOp2, m_factor2);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p2", "n2", reservoir2, noOp2, viewId2);
		double m_factor3 = clsVSetup.prepareTrain(viewId3, "e3", "p3", "n3");
		clsVSetup.fillsViewCatalogTable(viewId3, viewName3, "e3", reservoir3, noOp3, m_factor3);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p3", "n3", reservoir3, noOp3, viewId3);
		double m_factor4 = clsVSetup.prepareTrain(viewId4, "e4", "p4", "n4");
		clsVSetup.fillsViewCatalogTable(viewId4, viewName4, "e4", reservoir4, noOp4, m_factor4);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p4", "n4", reservoir4, noOp4, viewId4);
	}

	/**
	 * Tests of creating views until training
	 */
	public void testPrepareTrain() {
		prepareForTest();
		double m_factor1 = clsVSetup.prepareTrain(viewId1, "e1", "p1", "n1");
		System.out.println(m_factor1);
		double m_factor2 = clsVSetup.prepareTrain(viewId2, "e2", "p2", "n2");
		System.out.println(m_factor2);
		double m_factor3 = clsVSetup.prepareTrain(viewId3, "e3", "p3", "n3");
		System.out.println(m_factor3);
		double m_factor4 = clsVSetup.prepareTrain(viewId4, "e4", "p4", "n4");
		System.out.println(m_factor4);
	}

	/**
	 * Tests of training
	 */
	public void testTrain() {
		prepareForTest();
		double m_factor1 = clsVSetup.prepareTrain(viewId1, "e1", "p1", "n1");
		clsVSetup.fillsViewCatalogTable(viewId1, viewName1, "e1", reservoir1, noOp1, m_factor1);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p1", "n1", reservoir1, noOp1, viewId1);
		double m_factor2 = clsVSetup.prepareTrain(viewId2, "e2", "p2", "n2");
		clsVSetup.fillsViewCatalogTable(viewId2, viewName2, "e2", reservoir2, noOp2, m_factor2);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p2", "n2", reservoir2, noOp2, viewId2);
		double m_factor3 = clsVSetup.prepareTrain(viewId3, "e3", "p3", "n3");
		clsVSetup.fillsViewCatalogTable(viewId3, viewName3, "e3", reservoir3, noOp3, m_factor3);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p3", "n3", reservoir3, noOp3, viewId3);
		double m_factor4 = clsVSetup.prepareTrain(viewId4, "e4", "p4", "n4");
		clsVSetup.fillsViewCatalogTable(viewId4, viewName4, "e4", reservoir4, noOp4, m_factor4);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p4", "n4", reservoir4, noOp4, viewId4);
		
		if(reservoir1 > 0)
			clsVSetup.createReservoir(viewId1);
		if(reservoir2 > 0)
			clsVSetup.createReservoir(viewId2);
		if(reservoir3 > 0)
			clsVSetup.createReservoir(viewId3);
		if(reservoir4 > 0)
			clsVSetup.createReservoir(viewId4);
		int featureSize1 = clsVSetup.getNumberOfFeatures(viewId1);
		int featureSize2 = clsVSetup.getNumberOfFeatures(viewId2);
		int featureSize3 = clsVSetup.getNumberOfFeatures(viewId3);
		int featureSize4 = clsVSetup.getNumberOfFeatures(viewId4);
		//train;
		int retValue1 = clsVSetup.train(viewId1, featureSize1, reservoir1, noOp1);
		assertTrue(retValue1 != -1);
		int retValue2 = clsVSetup.train(viewId2, featureSize2, reservoir2, noOp2);
		assertTrue(retValue2 != -1);
		int retValue3 = clsVSetup.train(viewId3, featureSize3, reservoir3, noOp3);
		assertTrue(retValue3 != -1);
		int retValue4 = clsVSetup.train(viewId4, featureSize4, reservoir4, noOp4);
		assertTrue(retValue4 != -1);
	}

	/**
	 * Tests whether number of features are correctly extracted or not
	 */
	public void testGetNumberOfFeatures() {
		prepareForTest();
		double m_factor1 = clsVSetup.prepareTrain(viewId1, "e1", "p1", "n1");
		clsVSetup.fillsViewCatalogTable(viewId1, viewName1, "e1", reservoir1, noOp1, m_factor1);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p1", "n1", reservoir1, noOp1, viewId1);
		double m_factor2 = clsVSetup.prepareTrain(viewId2, "e2", "p2", "n2");
		clsVSetup.fillsViewCatalogTable(viewId2, viewName2, "e2", reservoir2, noOp2, m_factor2);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p2", "n2", reservoir2, noOp2, viewId2);
		double m_factor3 = clsVSetup.prepareTrain(viewId3, "e3", "p3", "n3");
		clsVSetup.fillsViewCatalogTable(viewId3, viewName3, "e3", reservoir3, noOp3, m_factor3);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p3", "n3", reservoir3, noOp3, viewId3);
		double m_factor4 = clsVSetup.prepareTrain(viewId4, "e4", "p4", "n4");
		clsVSetup.fillsViewCatalogTable(viewId4, viewName4, "e4", reservoir4, noOp4, m_factor4);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p4", "n4", reservoir4, noOp4, viewId4);
		
		if(reservoir1 > 0)
			clsVSetup.createReservoir(viewId1);
		if(reservoir2 > 0)
			clsVSetup.createReservoir(viewId2);
		if(reservoir3 > 0)
			clsVSetup.createReservoir(viewId3);
		if(reservoir4 > 0)
			clsVSetup.createReservoir(viewId4);
		int featureSize1 = clsVSetup.getNumberOfFeatures(viewId1);
		int featureSize2 = clsVSetup.getNumberOfFeatures(viewId2);
		int featureSize3 = clsVSetup.getNumberOfFeatures(viewId3);
		int featureSize4 = clsVSetup.getNumberOfFeatures(viewId4);
		assertTrue(featureSize1 == 5037);
		assertTrue(featureSize2 == 5037);
		assertTrue(featureSize3 == 5037);
		assertTrue(featureSize4 == 5037);
	}

	/**
	 * Tests whether reservoirs are correctly created or not
	 */
	public void testCreateReservoirs() {
		prepareForTest();
		double m_factor1 = clsVSetup.prepareTrain(viewId1, "e1", "p1", "n1");
		clsVSetup.fillsViewCatalogTable(viewId1, viewName1, "e1", reservoir1, noOp1, m_factor1);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p1", "n1", reservoir1, noOp1, viewId1);
		double m_factor2 = clsVSetup.prepareTrain(viewId2, "e2", "p2", "n2");
		clsVSetup.fillsViewCatalogTable(viewId2, viewName2, "e2", reservoir2, noOp2, m_factor2);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p2", "n2", reservoir2, noOp2, viewId2);
		double m_factor3 = clsVSetup.prepareTrain(viewId3, "e3", "p3", "n3");
		clsVSetup.fillsViewCatalogTable(viewId3, viewName3, "e3", reservoir3, noOp3, m_factor3);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p3", "n3", reservoir3, noOp3, viewId3);
		double m_factor4 = clsVSetup.prepareTrain(viewId4, "e4", "p4", "n4");
		clsVSetup.fillsViewCatalogTable(viewId4, viewName4, "e4", reservoir4, noOp4, m_factor4);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p4", "n4", reservoir4, noOp4, viewId4);
		
		if(reservoir1 > 0)
			clsVSetup.createReservoir(viewId1);
		if(reservoir2 > 0)
			clsVSetup.createReservoir(viewId2);
		if(reservoir3 > 0)
			clsVSetup.createReservoir(viewId3);
		if(reservoir4 > 0)
			clsVSetup.createReservoir(viewId4);
	}

	/**
	 * Tests creating of views until triggers are being created
	 */
	public void testCreateTriggers() {
		prepareForTest();
		double m_factor1 = clsVSetup.prepareTrain(viewId1, "e1", "p1", "n1");
		clsVSetup.fillsViewCatalogTable(viewId1, viewName1, "e1", reservoir1, noOp1, m_factor1);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p1", "n1", reservoir1, noOp1, viewId1);
		double m_factor2 = clsVSetup.prepareTrain(viewId2, "e2", "p2", "n2");
		clsVSetup.fillsViewCatalogTable(viewId2, viewName2, "e2", reservoir2, noOp2, m_factor2);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p2", "n2", reservoir2, noOp2, viewId2);
		double m_factor3 = clsVSetup.prepareTrain(viewId3, "e3", "p3", "n3");
		clsVSetup.fillsViewCatalogTable(viewId3, viewName3, "e3", reservoir3, noOp3, m_factor3);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p3", "n3", reservoir3, noOp3, viewId3);
		double m_factor4 = clsVSetup.prepareTrain(viewId4, "e4", "p4", "n4");
		clsVSetup.fillsViewCatalogTable(viewId4, viewName4, "e4", reservoir4, noOp4, m_factor4);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p4", "n4", reservoir4, noOp4, viewId4);
		
		if(reservoir1 > 0)
			clsVSetup.createReservoir(viewId1);
		if(reservoir2 > 0)
			clsVSetup.createReservoir(viewId2);
		if(reservoir3 > 0)
			clsVSetup.createReservoir(viewId3);
		if(reservoir4 > 0)
			clsVSetup.createReservoir(viewId4);
		int featureSize1 = clsVSetup.getNumberOfFeatures(viewId1);
		int featureSize2 = clsVSetup.getNumberOfFeatures(viewId2);
		int featureSize3 = clsVSetup.getNumberOfFeatures(viewId3);
		int featureSize4 = clsVSetup.getNumberOfFeatures(viewId4);
		//train;
		int retValue1 = clsVSetup.train(viewId1, featureSize1, reservoir1, noOp1);
		assertTrue(retValue1 != -1);
		int retValue2 = clsVSetup.train(viewId2, featureSize2, reservoir2, noOp2);
		assertTrue(retValue2 != -1);
		int retValue3 = clsVSetup.train(viewId3, featureSize3, reservoir3, noOp3);
		assertTrue(retValue3 != -1);
		int retValue4 = clsVSetup.train(viewId4, featureSize4, reservoir4, noOp4);
		assertTrue(retValue4 != -1);
		
		clsVSetup.prepareGeneralRTableForHazy(viewId1, "e1");
		clsVSetup.prepareGeneralRTableForHazy(viewId2, "e2");
		clsVSetup.prepareGeneralRTableForHazy(viewId3, "e3");
		clsVSetup.prepareGeneralRTableForHazy(viewId4, "e4");
		
		clsVSetup.createTriggers(viewId1, "e1", "p1", "n1");
		clsVSetup.createTriggers(viewId2, "e2", "p2", "n2");
		clsVSetup.createTriggers(viewId3, "e3", "p3", "n3");
		clsVSetup.createTriggers(viewId4, "e4", "p4", "n4");
	}

	/**
	 * Tests all process of creating views. Since this is the last step of creating view, all steps are also tested here
	 */
	public void testCreateViewAfterTraining() {
		prepareForTest();
		double m_factor1 = clsVSetup.prepareTrain(viewId1, "e1", "p1", "n1");
		clsVSetup.fillsViewCatalogTable(viewId1, viewName1, "e1", reservoir1, noOp1, m_factor1);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p1", "n1", reservoir1, noOp1, viewId1);
		double m_factor2 = clsVSetup.prepareTrain(viewId2, "e2", "p2", "n2");
		clsVSetup.fillsViewCatalogTable(viewId2, viewName2, "e2", reservoir2, noOp2, m_factor2);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p2", "n2", reservoir2, noOp2, viewId2);
		double m_factor3 = clsVSetup.prepareTrain(viewId3, "e3", "p3", "n3");
		clsVSetup.fillsViewCatalogTable(viewId3, viewName3, "e3", reservoir3, noOp3, m_factor3);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p3", "n3", reservoir3, noOp3, viewId3);
		double m_factor4 = clsVSetup.prepareTrain(viewId4, "e4", "p4", "n4");
		clsVSetup.fillsViewCatalogTable(viewId4, viewName4, "e4", reservoir4, noOp4, m_factor4);
		clsVSetup.fillsUpdateTableViewIdCatalogTable("p4", "n4", reservoir4, noOp4, viewId4);
		
		if(reservoir1 > 0)
			clsVSetup.createReservoir(viewId1);
		if(reservoir2 > 0)
			clsVSetup.createReservoir(viewId2);
		if(reservoir3 > 0)
			clsVSetup.createReservoir(viewId3);
		if(reservoir4 > 0)
			clsVSetup.createReservoir(viewId4);
		int featureSize1 = clsVSetup.getNumberOfFeatures(viewId1);
		int featureSize2 = clsVSetup.getNumberOfFeatures(viewId2);
		int featureSize3 = clsVSetup.getNumberOfFeatures(viewId3);
		int featureSize4 = clsVSetup.getNumberOfFeatures(viewId4);
		//train;
		int retValue1 = clsVSetup.train(viewId1, featureSize1, reservoir1, noOp1);
		assertTrue(retValue1 != -1);
		int retValue2 = clsVSetup.train(viewId2, featureSize2, reservoir2, noOp2);
		assertTrue(retValue2 != -1);
		int retValue3 = clsVSetup.train(viewId3, featureSize3, reservoir3, noOp3);
		assertTrue(retValue3 != -1);
		int retValue4 = clsVSetup.train(viewId4, featureSize4, reservoir4, noOp4);
		assertTrue(retValue4 != -1);
		
		clsVSetup.prepareGeneralRTableForHazy(viewId1, "e1");
		clsVSetup.prepareGeneralRTableForHazy(viewId2, "e2");
		clsVSetup.prepareGeneralRTableForHazy(viewId3, "e3");
		clsVSetup.prepareGeneralRTableForHazy(viewId4, "e4");
		
		clsVSetup.createTriggers(viewId1, "e1", "p1", "n1");
		clsVSetup.createTriggers(viewId2, "e2", "p2", "n2");
		clsVSetup.createTriggers(viewId3, "e3", "p3", "n3");
		clsVSetup.createTriggers(viewId4, "e4", "p4", "n4");
		
		clsVSetup.createViewAfterTraining(viewId1, viewName1);
		clsVSetup.createViewAfterTraining(viewId2, viewName2);
		clsVSetup.createViewAfterTraining(viewId3, viewName3);
		clsVSetup.createViewAfterTraining(viewId4, viewName4);
	}
}
