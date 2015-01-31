package wisc.db.equivalencetest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import wisc.db.equivalence.CatalogAttributeValues;
import wisc.db.equivalence.Edge;
import wisc.db.equivalence.EqViewConstants;
import wisc.db.equivalence.EqViewSchema;
import wisc.db.equivalence.EqViewSetup;
import wisc.db.equivalence.EqViewSetupFactory;
import wisc.db.equivalence.RuleTable;
import wisc.db.sql.DatabaseFactory;
import wisc.db.sql.QueryExecutor;
import junit.framework.TestCase;

/**
 * Tests {@link EqViewSetup} class
 * 
 * @author koc
 *
 */
public class EqViewSetupTest extends TestCase {
	EqViewSetup eqVSetup;
	QueryExecutor sql;
	String cleanViewDatabaseQuery = "TRUNCATE eq_view_id_table; TRUNCATE eq_view_rules_id_weight_table;";
	
	ArrayList<RuleTable> softRules1;
	ArrayList<RuleTable> hardEQRules1;
	ArrayList<RuleTable> hardNEQRules1;
	ArrayList<RuleTable> softRules2;
	ArrayList<RuleTable> hardEQRules2;
	ArrayList<RuleTable> hardNEQRules2;
	int viewId1;
	int viewId2;
	String viewName1;
	String viewName2;
	double reorganizeTime1 = 3.45;
	double reorganizeTime2 = 5.67;
	double totalSweight1 = 14;
	double totalSweight2 = 10;
	
	double s_w_1_1 = 2.3;
	double s_w_1_2 = 3.5;
	double s_w_1_3 = 5.1;
	double s_w_1_4 = 3.1;
	
	double s_w_2_1 = 1.12;
	double s_w_2_2 = 1.56;
	double s_w_2_3 = 5.32;
	int s_w_2_4 = 2;
	
	/**
	 * defulat constructor
	 * @param name
	 */
	public EqViewSetupTest(String name) {
		super(name);
	}

	/**
	 * Initializes eqVSetup and QueryExecutor objects
	 */
	protected void setUp() throws Exception {
		super.setUp();
		EqViewSetup.loadHazyConfParameters();
		eqVSetup = EqViewSetupFactory.getEqViewSetup();
		sql = DatabaseFactory.getQueryExecutor();
	}

	/**
	 * Teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Checks retrieved eqViewSetup is null or not
	 */
	public void testEqViewSetup() {
		assertTrue(eqVSetup != null);
	}

	/**
	 * Retrieves unique id from the databases and check whether they are valid or not
	 * <p>
	 * It inserts some views into the table and then check whether id retrieved for the new view is valid or not
	 * 
	 */
	public void testGetUniqueViewId() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		int id = eqVSetup.getUniqueViewId();
		assertTrue(id == 0);
		eqVSetup.insertIntoIdCatalog(0, "viewName", "entityName1");
		id = eqVSetup.getUniqueViewId();
		assertTrue(id == 1);
		eqVSetup.insertIntoIdCatalog(5, "viewName", "entityName5");
		id = eqVSetup.getUniqueViewId();
		assertTrue(id == 6);
	}

	/**
	 * Checks whether views are correctly inserted general catalog or not
	 */
	public void testInsertIntoIdCatalog() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		int id1 = eqVSetup.getUniqueViewId();
		String viewName1 = "newView";
		eqVSetup.insertIntoIdCatalog(id1, viewName1, "e1");
		String query1 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_ID_ALL_CATALOGS_TABLE + " WHERE view_id = " + id1 + 
				" AND view_name = '" + viewName1 + "' AND catalog_name = '" + EqViewSchema.getViewCatalogTableName(id1) + "';";
		ResultSet set = sql.executeSelectQuery(query1);
		int count1 = 0;
		try {
			if(set.next())
				count1 = set.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int id2 = eqVSetup.getUniqueViewId();
		String viewName2 = "newView";
		eqVSetup.insertIntoIdCatalog(id2, viewName2, "e2");
		String query2 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_ID_ALL_CATALOGS_TABLE + " WHERE view_id = " + id2 + 
				" AND view_name = '" + viewName2 + "' AND catalog_name = '" + EqViewSchema.getViewCatalogTableName(id2) + "';";
		ResultSet set2 = sql.executeSelectQuery(query2);
		int count2 = 0;
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
	 * Tests whether soft rule tables are correctly inserted to rules id weight table or not
	 */
	public void testInsertSoftTableIntoRulesIdWeightTab() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		int viewId1 = eqVSetup.getUniqueViewId();
		String viewName1 = "view1";
		eqVSetup.insertIntoIdCatalog(viewId1, viewName1, "e1");
		ArrayList<RuleTable> softRuleTables1 = new ArrayList<RuleTable>();
		String tableName1_1 = "sr1_1";
		String tableName1_2 = "sr1_2";
		String tableName1_3 = "sr1_3";
		String tableName1_4 = "sr1_4";
		double weight1_1 = 4.2;
		double weight1_2 = 1.6;
		double weight1_3 = 3.7;
		double weight1_4 = 7.7;
		
		int count1_1 = 0;
		int count1_2 = 0;
		int count1_3 = 0;
		int count1_4 = 0;
		int count1_5 = 0;
		
		
		softRuleTables1.add(new RuleTable(tableName1_1, weight1_1));
		softRuleTables1.add(new RuleTable(tableName1_2, weight1_2));
		softRuleTables1.add(new RuleTable(tableName1_3, weight1_3));
		softRuleTables1.add(new RuleTable(tableName1_4, weight1_4));
		eqVSetup.insertSoftTableIntoRulesIdWeightTab(viewId1, softRuleTables1);
		
		String query1_1 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId1 + 
				" AND rule_table = '" + tableName1_1 + "' AND weight = " + weight1_1 + ";";
		String query1_2 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId1 + 
		" AND rule_table = '" + tableName1_2 + "' AND weight = " + weight1_2 + ";";
		String query1_3 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId1 + 
		" AND rule_table = '" + tableName1_3 + "' AND weight = " + weight1_3 + ";";
		String query1_4 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId1 + 
		" AND rule_table = '" + tableName1_4 + "' AND weight = " + weight1_4 + ";";
		//wrong query. Expected count to be 0
		String query1_5 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId1 + 
		" AND rule_table = '" + tableName1_1 + "' AND weight = " + weight1_3 + ";";
		
		try {
			ResultSet set = sql.executeSelectQuery(query1_1);
				
			if(set.next())
				count1_1 = set.getInt(1);
			
			set = sql.executeSelectQuery(query1_2);
			
			if(set.next())
				count1_2 = set.getInt(1);
			
			set = sql.executeSelectQuery(query1_3);
			
			if(set.next())
				count1_3 = set.getInt(1);
			
			//dummy query. expect count to be 0
			set = sql.executeSelectQuery(query1_5);
			
			if(set.next())
				count1_5 = set.getInt(1);
			
			set = sql.executeSelectQuery(query1_4);
			
			if(set.next())
				count1_4 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		int viewId2 = eqVSetup.getUniqueViewId();
		String viewName2 = "view2";
		eqVSetup.insertIntoIdCatalog(viewId2, viewName2, "e2");
		
		String tableName2_1 = "sr2_1";
		String tableName2_2 = "sr2_2";
		String tableName2_3 = "sr2_3";
		double weight2_1 = 1.122;
		double weight2_2 = 3.5216;
		double weight2_3 = 7.89;
		
		int count2_1 = 0;
		int count2_2 = 0;
		int count2_3 = 0;
		int count2_4 = 0;
		
		
		ArrayList<RuleTable> softRuleTables2 = new ArrayList<RuleTable>();
		softRuleTables2.add(new RuleTable(tableName2_1, weight2_1));
		softRuleTables2.add(new RuleTable(tableName2_2, weight2_2));
		softRuleTables2.add(new RuleTable(tableName2_3, weight2_3));
		eqVSetup.insertSoftTableIntoRulesIdWeightTab(viewId2, softRuleTables2);
		
		String query2_1 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId2 + 
				" AND rule_table = '" + tableName2_1 + "' AND weight = " + weight2_1 + ";";
		
		String query2_2 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId2 + 
				" AND rule_table = '" + tableName2_2 + "' AND weight = " + weight2_2 + ";";
			
		String query2_3 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId2 + 
				" AND rule_table = '" + tableName2_3 + "' AND weight = " + weight2_3 + ";";

		//wrong query. Expected count to be 0
		String query2_4 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId2 + 
			" AND rule_table = '" + tableName2_1 + "' AND weight = " + weight2_3 + ";";
		
		try {
			ResultSet set = sql.executeSelectQuery(query2_1);
				
			if(set.next())
				count2_1 = set.getInt(1);
			
			set = sql.executeSelectQuery(query2_2);
			
			if(set.next())
				count2_2 = set.getInt(1);
			
			set = sql.executeSelectQuery(query2_3);
			
			if(set.next())
				count2_3 = set.getInt(1);
			
			//wrong query. expect count to be 0			
			set = sql.executeSelectQuery(query2_4);
			
			if(set.next())
				count2_4 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(count1_1 == 1);
		assertTrue(count1_2 == 1);
		assertTrue(count1_3 == 1);
		assertTrue(count1_4 == 1);
		assertTrue(count1_5 == 0);
		assertTrue(count2_1 == 1);
		assertTrue(count2_2 == 1);
		assertTrue(count2_3 == 1);
		assertTrue(count2_4 == 0);
	}

	/**
	 * Tests whether hard eq and hard neq rule tables are correctly inserted to rules id weight table or not
	 */
	public void testInsertHardTableIntoRulesIdWeightTab() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		int viewId1 = eqVSetup.getUniqueViewId();
		String viewName1 = "view1";
		eqVSetup.insertIntoIdCatalog(viewId1, viewName1, "e1");
		ArrayList<RuleTable> hardEQRuleTables1 = new ArrayList<RuleTable>();
		String tableName1_1 = "heq1_1";
		String tableName1_2 = "heq1_2";
		String tableName1_3 = "heq1_3";
		String tableName1_4 = "heq1_4";
		double weight1_1 = Double.POSITIVE_INFINITY;
		double weight1_2 = Double.POSITIVE_INFINITY;
		double weight1_3 = Double.POSITIVE_INFINITY;
		double weight1_4 = Double.POSITIVE_INFINITY;
		
		int count1_1 = 0;
		int count1_2 = 0;
		int count1_3 = 0;
		int count1_4 = 0;
		int count1_5 = 0;
		
		
		hardEQRuleTables1.add(new RuleTable(tableName1_1, weight1_1));
		hardEQRuleTables1.add(new RuleTable(tableName1_2, weight1_2));
		hardEQRuleTables1.add(new RuleTable(tableName1_3, weight1_3));
		hardEQRuleTables1.add(new RuleTable(tableName1_4, weight1_4));
		
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId1, hardEQRuleTables1, true);
		
		String query1_1 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId1 + 
				" AND rule_table = '" + tableName1_1 + "' AND weight = '" + weight1_1 + "';";
		String query1_2 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId1 + 
		" AND rule_table = '" + tableName1_2 + "' AND weight = '" + weight1_2 + "';";
		String query1_3 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId1 + 
		" AND rule_table = '" + tableName1_3 + "' AND weight = '" + weight1_3 + "';";
		String query1_4 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId1 + 
		" AND rule_table = '" + tableName1_4 + "' AND weight = '" + weight1_4 + "';";
		//wrong query. Expected count to be 0
		String query1_5 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId1 + 
		" AND rule_table = '" + tableName1_1 + "' AND weight = " + 3 + ";";
		
		try {
			ResultSet set = sql.executeSelectQuery(query1_1);
				
			if(set.next())
				count1_1 = set.getInt(1);
			
			set = sql.executeSelectQuery(query1_2);
			
			if(set.next())
				count1_2 = set.getInt(1);
			
			set = sql.executeSelectQuery(query1_3);
			
			if(set.next())
				count1_3 = set.getInt(1);
			
			//dummy query. expect count to be 0
			set = sql.executeSelectQuery(query1_5);
			
			if(set.next())
				count1_5 = set.getInt(1);
			
			set = sql.executeSelectQuery(query1_4);
			
			if(set.next())
				count1_4 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		int viewId2 = eqVSetup.getUniqueViewId();
		String viewName2 = "view2";
		eqVSetup.insertIntoIdCatalog(viewId2, viewName2, "e2");
		
		String tableName2_1 = "heq2_1";
		String tableName2_2 = "heq2_2";
		String tableName2_3 = "heq2_3";
		double weight2_1 = Double.POSITIVE_INFINITY;
		double weight2_2 = Double.POSITIVE_INFINITY;
		double weight2_3 = Double.POSITIVE_INFINITY;
		
		int count2_1 = 0;
		int count2_2 = 0;
		int count2_3 = 0;
		int count2_4 = 0;
		
		
		ArrayList<RuleTable> hardEQRuleTables2 = new ArrayList<RuleTable>();
		hardEQRuleTables2.add(new RuleTable(tableName2_1, weight2_1));
		hardEQRuleTables2.add(new RuleTable(tableName2_2, weight2_2));
		hardEQRuleTables2.add(new RuleTable(tableName2_3, weight2_3));
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId2, hardEQRuleTables2, true);
		
		String query2_1 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId2 + 
				" AND rule_table = '" + tableName2_1 + "' AND weight = '" + weight2_1 + "';";
		
		String query2_2 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId2 + 
				" AND rule_table = '" + tableName2_2 + "' AND weight = '" + weight2_2 + "';";
			
		String query2_3 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId2 + 
				" AND rule_table = '" + tableName2_3 + "' AND weight = '" + weight2_3 + "';";

		//wrong query. Expected count to be 0
		String query2_4 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId2 + 
			" AND rule_table = '" + tableName2_1 + "' AND weight = " + 12.32 + ";";
		
		try {
			ResultSet set = sql.executeSelectQuery(query2_1);
				
			if(set.next())
				count2_1 = set.getInt(1);
			
			set = sql.executeSelectQuery(query2_2);
			
			if(set.next())
				count2_2 = set.getInt(1);
			
			set = sql.executeSelectQuery(query2_3);
			
			if(set.next())
				count2_3 = set.getInt(1);
			
			//wrong query. expect count to be 0			
			set = sql.executeSelectQuery(query2_4);
			
			if(set.next())
				count2_4 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int viewId3 = eqVSetup.getUniqueViewId();
		String viewName3 = "view3";
		eqVSetup.insertIntoIdCatalog(viewId3, viewName3, "e3");
		ArrayList<RuleTable> hardNEQRuleTables3 = new ArrayList<RuleTable>();
		String tableName3_1 = "hneq1_1";
		String tableName3_2 = "hneq1_2";
		String tableName3_3 = "hneq1_3";
		String tableName3_4 = "hneq1_4";
		double weight3_1 = Double.NEGATIVE_INFINITY;
		double weight3_2 = Double.NEGATIVE_INFINITY;
		double weight3_3 = Double.NEGATIVE_INFINITY;
		double weight3_4 = Double.NEGATIVE_INFINITY;
		
		int count3_1 = 0;
		int count3_2 = 0;
		int count3_3 = 0;
		int count3_4 = 0;
		int count3_5 = 0;
		
		
		hardNEQRuleTables3.add(new RuleTable(tableName3_1, weight3_1));
		hardNEQRuleTables3.add(new RuleTable(tableName3_2, weight3_2));
		hardNEQRuleTables3.add(new RuleTable(tableName3_3, weight3_3));
		hardNEQRuleTables3.add(new RuleTable(tableName3_4, weight3_4));
		
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId3, hardNEQRuleTables3, false);
		
		String query3_1 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId3 + 
				" AND rule_table = '" + tableName3_1 + "' AND weight = '" + weight3_1 + "';";
		String query3_2 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId3 + 
		" AND rule_table = '" + tableName3_2 + "' AND weight = '" + weight3_2 + "';";
		String query3_3 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId3 + 
		" AND rule_table = '" + tableName3_3 + "' AND weight = '" + weight3_3 + "';";
		String query3_4 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId3 + 
		" AND rule_table = '" + tableName3_4 + "' AND weight = '" + weight3_4 + "';";
		//wrong query. Expected count to be 0
		String query3_5 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId3 + 
		" AND rule_table = '" + tableName3_1 + "' AND weight = " + 3 + ";";
		
		try {
			ResultSet set = sql.executeSelectQuery(query3_1);
				
			if(set.next())
				count3_1 = set.getInt(1);
			
			set = sql.executeSelectQuery(query3_2);
			
			if(set.next())
				count3_2 = set.getInt(1);
			
			set = sql.executeSelectQuery(query3_3);
			
			if(set.next())
				count3_3 = set.getInt(1);
			
			//dummy query. expect count to be 0
			set = sql.executeSelectQuery(query3_5);
			
			if(set.next())
				count3_5 = set.getInt(1);
			
			set = sql.executeSelectQuery(query3_4);
			
			if(set.next())
				count3_4 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		int viewId4 = eqVSetup.getUniqueViewId();
		String viewName4 = "view4";
		eqVSetup.insertIntoIdCatalog(viewId4, viewName4, "e4");
		
		String tableName4_1 = "hneq2_1";
		String tableName4_2 = "hneq2_2";
		String tableName4_3 = "hneq2_3";
		double weight4_1 = Double.NEGATIVE_INFINITY;
		double weight4_2 = Double.NEGATIVE_INFINITY;
		double weight4_3 = Double.NEGATIVE_INFINITY;
		
		int count4_1 = 0;
		int count4_2 = 0;
		int count4_3 = 0;
		int count4_4 = 0;
		
		
		ArrayList<RuleTable> hardNEQRuleTables4 = new ArrayList<RuleTable>();
		hardNEQRuleTables4.add(new RuleTable(tableName4_1, weight4_1));
		hardNEQRuleTables4.add(new RuleTable(tableName4_2, weight4_2));
		hardNEQRuleTables4.add(new RuleTable(tableName4_3, weight4_3));
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId4, hardNEQRuleTables4, false);
		
		String query4_1 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId4 + 
				" AND rule_table = '" + tableName4_1 + "' AND weight = '" + weight4_1 + "';";
		
		String query4_2 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId4 + 
				" AND rule_table = '" + tableName4_2 + "' AND weight = '" + weight4_2 + "';";
			
		String query4_3 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId4 + 
				" AND rule_table = '" + tableName4_3 + "' AND weight = '" + weight4_3 + "';";

		//wrong query. Expected count to be 0
		String query4_4 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_RULES_ID_WEIGHT_TABLE + " WHERE view_id = " + viewId4 + 
			" AND rule_table = '" + tableName4_1 + "' AND weight = " + 12.32 + ";";
		
		try {
			ResultSet set = sql.executeSelectQuery(query4_1);
				
			if(set.next())
				count4_1 = set.getInt(1);
			
			set = sql.executeSelectQuery(query4_2);
			
			if(set.next())
				count4_2 = set.getInt(1);
			
			set = sql.executeSelectQuery(query4_3);
			
			if(set.next())
				count4_3 = set.getInt(1);
			
			//wrong query. expect count to be 0			
			set = sql.executeSelectQuery(query4_4);
			
			if(set.next())
				count4_4 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(count1_1 == 1);
		assertTrue(count1_2 == 1);
		assertTrue(count1_3 == 1);
		assertTrue(count1_4 == 1);
		assertTrue(count1_5 == 0);
		assertTrue(count2_1 == 1);
		assertTrue(count2_2 == 1);
		assertTrue(count2_3 == 1);
		assertTrue(count2_4 == 0);
		assertTrue(count3_1 == 1);
		assertTrue(count3_2 == 1);
		assertTrue(count3_3 == 1);
		assertTrue(count3_4 == 1);
		assertTrue(count3_5 == 0);
		assertTrue(count4_1 == 1);
		assertTrue(count4_2 == 1);
		assertTrue(count4_3 == 1);
		assertTrue(count4_4 == 0);
	}

	/**
	 * Tests given rule table name, whether view id and weights are correctly retrieved from rules id weight table
	 */
	public void testGetViewIdWeightFromRuleTableName() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		this.testInsertSoftTableIntoRulesIdWeightTab();
		int viewId1 = 0;
		String tableName1_1 = "sr1_1";
		String tableName1_2 = "sr1_2";
		String tableName1_3 = "sr1_3";
		double weight1_1 = 4.2;
		double weight1_2 = 1.6;
		double weight1_3 = 3.7;
		
		int viewId2 = 1;
		String tableName2_1 = "sr2_1";
		String tableName2_2 = "sr2_2";
		String tableName2_3 = "sr2_3";
		double weight2_1 = 1.122;
		double weight2_2 = 3.5216;
		double weight2_3 = 7.89;
		
		ArrayList<Object> viewIdWeight1_1 = eqVSetup.getViewIdWeightFromRuleTableName(tableName1_1);
		ArrayList<Object> viewIdWeight1_2 = eqVSetup.getViewIdWeightFromRuleTableName(tableName1_2);
		ArrayList<Object> viewIdWeight1_3 = eqVSetup.getViewIdWeightFromRuleTableName(tableName1_3);
		ArrayList<Object> viewIdWeight2_1 = eqVSetup.getViewIdWeightFromRuleTableName(tableName2_1);
		ArrayList<Object> viewIdWeight2_2 = eqVSetup.getViewIdWeightFromRuleTableName(tableName2_2);
		ArrayList<Object> viewIdWeight2_3 = eqVSetup.getViewIdWeightFromRuleTableName(tableName2_3);
			
		assertTrue((Integer) viewIdWeight1_1.get(0) == viewId1);
		assertTrue((Double) viewIdWeight1_1.get(1) == weight1_1);
		assertTrue((Integer) viewIdWeight1_2.get(0) == viewId1);
		assertTrue((Double) viewIdWeight1_2.get(1) == weight1_2);
		assertTrue((Integer) viewIdWeight1_3.get(0) == viewId1);
		assertTrue((Double) viewIdWeight1_3.get(1) == weight1_3);
		
		assertTrue((Integer) viewIdWeight2_1.get(0) == viewId2);
		assertTrue((Double) viewIdWeight2_1.get(1) == weight2_1);
		assertTrue((Integer) viewIdWeight2_2.get(0) == viewId2);
		assertTrue((Double) viewIdWeight2_2.get(1) == weight2_2);
		assertTrue((Integer) viewIdWeight2_3.get(0) == viewId2);
		assertTrue((Double) viewIdWeight2_3.get(1) == weight2_3);
	}

	/**
	 * This is auxiliary method for tests. It creates rule tables with given names
	 * 
	 * @param softRules list of soft rule tables
	 * @param hardEQRules list of hard eq rule tables
	 * @param hardNEQRules list of hard neq rule tables
	 */
	private void createSoftHardEQHardNEQRuleTablesForTest(ArrayList<RuleTable> softRules, ArrayList<RuleTable> hardEQRules, ArrayList<RuleTable> hardNEQRules) {
		String query = "";
		for (int i = 0; i < softRules.size(); i++)
			query += "CREATE TABLE " + softRules.get(i).getTableName() + "(src int, dst int);";
		for (int i = 0; i < hardEQRules.size(); i++)
			query += "CREATE TABLE " + hardEQRules.get(i).getTableName() + "(src int, dst int);";
		for (int i = 0; i < hardNEQRules.size(); i++)
			query += "CREATE TABLE " + hardNEQRules.get(i).getTableName() + "(src int, dst int);";
		
		sql.executeUpdateQuery(query);
	}
	
	/**
	 * This is auxiliary method for tests. It drops rule tables from the database with given names
	 * 
	 * @param softRules list of soft rule tables
	 * @param hardEQRules list of hard rule tables
	 * @param hardNEQRules list of hard neq rule tables
	 */
	private void cleanRuleTables(ArrayList<RuleTable> softRules, ArrayList<RuleTable> hardEQRules, ArrayList<RuleTable> hardNEQRules) {
		String query = "";
		for (int i = 0; i < softRules.size(); i++)
			query += "DROP TABLE IF EXISTS " + softRules.get(i).getTableName() + ";";
		for (int i = 0; i < hardEQRules.size(); i++)
			query += "DROP TABLE IF EXISTS " + hardEQRules.get(i).getTableName() + ";";
		for (int i = 0; i < hardNEQRules.size(); i++)
			query += "DROP TABLE IF EXISTS " + hardNEQRules.get(i).getTableName() + ";";
		
		sql.executeUpdateQuery(query);
	}
	
	/**
	 * This is auxiliary method for tests. It drops entity partition, soft rule partition, hard eq and hard neq partition and view tables from the database 
	 * 
	 * @param viewId id of the view
	 * @param viewName view name
	 */
	private void cleanViewTables(int viewId, String viewName) {
		String query = this.getDropQuery(EqViewSchema.getViewCatalogTableName(viewId));
		query += this.getDropQuery(EqViewSchema.getEPTableName(viewId));
		query += this.getDropQuery(viewName);
		query += this.getDropQuery(EqViewSchema.getSoftRulePartTableName(viewId));
		query += this.getDropQuery(EqViewSchema.getHardEQRulePartTableName(viewId));
		query += this.getDropQuery(EqViewSchema.getHardNEQRulePartTableName(viewId));
	}
	
	/**
	 * Returns drop table query from given table name
	 * 
	 * @param tableName name of the table
	 * @return drop table query
	 */
	private String getDropQuery(String tableName) {
		return "DROP TABLE IF EXISTS " + tableName + ";";
	}
	
	/**
	 * Auxiliary method. It first clean database if rule tables are created before and clean old views. Then, it creates rule tables, required
	 * view tables and fills catalog table
	 */
	private void prepare() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		softRules1 = new ArrayList<RuleTable>();
		
		softRules1.add(new RuleTable("sr1_1", s_w_1_1));
		softRules1.add(new RuleTable("sr1_2", s_w_1_2));
		softRules1.add(new RuleTable("sr1_3", s_w_1_3));
		softRules1.add(new RuleTable("sr1_4", s_w_1_4));
		hardEQRules1 = new ArrayList<RuleTable>();
		hardEQRules1.add(new RuleTable("heqr1", Double.POSITIVE_INFINITY));
		hardEQRules1.add(new RuleTable("heqr2", Double.POSITIVE_INFINITY));
		hardEQRules1.add(new RuleTable("heqr3", Double.POSITIVE_INFINITY));
		hardNEQRules1 = new ArrayList<RuleTable>();
		hardNEQRules1.add(new RuleTable("hneqr1", Double.NEGATIVE_INFINITY));
		hardNEQRules1.add(new RuleTable("hneqr2", Double.NEGATIVE_INFINITY));
		hardNEQRules1.add(new RuleTable("hneqr3", Double.NEGATIVE_INFINITY));
		hardNEQRules1.add(new RuleTable("hneqr4", Double.NEGATIVE_INFINITY));
		
		viewId1 = eqVSetup.getUniqueViewId();
		viewName1 = "view1";
		eqVSetup.insertIntoIdCatalog(viewId1, viewName1, "e1");
		eqVSetup.insertSoftTableIntoRulesIdWeightTab(viewId1, softRules1);
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId1, hardEQRules1, true);
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId1, hardNEQRules1, false);
		
		softRules2 = new ArrayList<RuleTable>();
		softRules2.add(new RuleTable("sr2_1", s_w_2_1));
		softRules2.add(new RuleTable("sr2_2", s_w_2_2));
		softRules2.add(new RuleTable("sr2_3", s_w_2_3));
		softRules2.add(new RuleTable("sr2_4", s_w_2_4));
		hardEQRules2 = new ArrayList<RuleTable>();
		hardEQRules2.add(new RuleTable("heqr2_1", Double.POSITIVE_INFINITY));
		hardEQRules2.add(new RuleTable("heqr2_2", Double.POSITIVE_INFINITY));
		hardEQRules2.add(new RuleTable("heqr2_3", Double.POSITIVE_INFINITY));
		hardNEQRules2 = new ArrayList<RuleTable>();
		hardNEQRules2.add(new RuleTable("hneqr2_1", Double.NEGATIVE_INFINITY));
		hardNEQRules2.add(new RuleTable("hneqr2_2", Double.NEGATIVE_INFINITY));
		hardNEQRules2.add(new RuleTable("hneqr2_3", Double.NEGATIVE_INFINITY));
		hardNEQRules2.add(new RuleTable("hneqr2_4", Double.NEGATIVE_INFINITY));
		
		viewId2 = eqVSetup.getUniqueViewId();
		viewName2 = "view2";
		eqVSetup.insertIntoIdCatalog(viewId2, viewName2, "e2");
		eqVSetup.insertSoftTableIntoRulesIdWeightTab(viewId2, softRules2);
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId2, hardEQRules2, true);
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId2, hardNEQRules2, false);
		
		cleanRuleTables(softRules1, hardEQRules1, hardNEQRules1);
		cleanRuleTables(softRules2, hardEQRules2, hardNEQRules2);
		cleanViewTables(viewId1, viewName1);
		cleanViewTables(viewId2, viewName2);
		createSoftHardEQHardNEQRuleTablesForTest(softRules1, hardEQRules1, hardNEQRules1);
		createSoftHardEQHardNEQRuleTablesForTest(softRules2, hardEQRules2, hardNEQRules2);
		
		eqVSetup.createTables(viewId1, viewName1);
		eqVSetup.createTables(viewId2, viewName2);
		
		eqVSetup.fillsCatalogTable(viewId1, reorganizeTime1, 4, totalSweight1);
		eqVSetup.fillsCatalogTable(viewId2, reorganizeTime2, 4, totalSweight2);
		sql.executeUpdateQuery("DROP TABLE IF EXISTS ent_tab1; CREATE TABLE ent_tab1(id int);");
		sql.executeUpdateQuery("DROP TABLE IF EXISTS ent_tab2; CREATE TABLE ent_tab2(id int);");
	}
	
	/**
	 * Tests whether triggers are correctly created on ruel tables
	 */
	public void testCreateTriggers() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		prepare();
		
		//now create triggers and test it
		eqVSetup.createTriggers(viewId1, "ent_tab1", softRules1, hardEQRules1, hardNEQRules1);
		eqVSetup.createTriggers(viewId2, "ent_tab2", softRules2, hardEQRules2, hardNEQRules2);
		
		String tg_e1 = "";
		String tg_s_1_1 = "";
		String tg_s_1_2 = "";
		String tg_s_1_3 = "";
		String tg_s_1_4 = "";
		String tg_heq_1_1 = "";
		String tg_heq_1_2 = "";
		String tg_heq_1_3 = "";
		String tg_hneq_1_1 = "";
		String tg_hneq_1_2 = "";
		String tg_hneq_1_3 = "";
		String tg_hneq_1_4 = "";
		
		String tg_e2 = "";
		String tg_s_2_1 = "";
		String tg_s_2_2 = "";
		String tg_s_2_3 = "";
		String tg_s_2_4 = "";
		String tg_heq_2_1 = "";
		String tg_heq_2_2 = "";
		String tg_heq_2_3 = "";
		String tg_hneq_2_1 = "";
		String tg_hneq_2_2 = "";
		String tg_hneq_2_3 = "";
		String tg_hneq_2_4 = "";
		
		try {
			String query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = 'ent_tab1';";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_e1 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + softRules1.get(0).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_s_1_1 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + softRules1.get(1).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_s_1_2 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + softRules1.get(2).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_s_1_3 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + softRules1.get(3).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_s_1_4 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardEQRules1.get(0).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_heq_1_1 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardEQRules1.get(1).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_heq_1_2 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardEQRules1.get(2).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_heq_1_3 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardNEQRules1.get(0).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_hneq_1_1 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardNEQRules1.get(1).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_hneq_1_2 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardNEQRules1.get(2).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_hneq_1_3 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardNEQRules1.get(3).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_hneq_1_4 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = 'ent_tab2';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_e2 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + softRules2.get(0).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_s_2_1 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + softRules2.get(1).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_s_2_2 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + softRules2.get(2).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_s_2_3 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + softRules2.get(3).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_s_2_4 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardEQRules2.get(0).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_heq_2_1 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardEQRules2.get(1).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_heq_2_2 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardEQRules2.get(2).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_heq_2_3 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardNEQRules2.get(0).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_hneq_2_1 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardNEQRules2.get(1).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_hneq_2_2 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardNEQRules2.get(2).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_hneq_2_3 = set.getString(1);
			
			query = "select tgname from pg_trigger p1, pg_class p2 where p1.tgrelid = p2.oid and p2.relname = '" + hardNEQRules2.get(3).getTableName() + "';";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				tg_hneq_2_4 = set.getString(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(tg_e1.equals(EqViewSchema.getEntityTrigger(viewId1)));
		assertTrue(tg_s_1_1.equals(EqViewSchema.getSoftRuleTrigger(viewId1, 0)));
		assertTrue(tg_s_1_2.equals(EqViewSchema.getSoftRuleTrigger(viewId1, 1)));
		assertTrue(tg_s_1_3.equals(EqViewSchema.getSoftRuleTrigger(viewId1, 2)));
		assertTrue(tg_s_1_4.equals(EqViewSchema.getSoftRuleTrigger(viewId1, 3)));
		assertTrue(tg_heq_1_1.equals(EqViewSchema.getHardEQRuleTrigger(viewId1, 0)));
		assertTrue(tg_heq_1_2.equals(EqViewSchema.getHardEQRuleTrigger(viewId1, 1)));
		assertTrue(tg_heq_1_3.equals(EqViewSchema.getHardEQRuleTrigger(viewId1, 2)));
		assertTrue(tg_hneq_1_1.equals(EqViewSchema.getHardNEQRuleTrigger(viewId1, 0)));
		assertTrue(tg_hneq_1_2.equals(EqViewSchema.getHardNEQRuleTrigger(viewId1, 1)));
		assertTrue(tg_hneq_1_3.equals(EqViewSchema.getHardNEQRuleTrigger(viewId1, 2)));
		assertTrue(tg_hneq_1_4.equals(EqViewSchema.getHardNEQRuleTrigger(viewId1, 3)));
		assertTrue(tg_e2.equals(EqViewSchema.getEntityTrigger(viewId2)));
		assertTrue(tg_s_2_1.equals(EqViewSchema.getSoftRuleTrigger(viewId2, 0)));
		assertTrue(tg_s_2_2.equals(EqViewSchema.getSoftRuleTrigger(viewId2, 1)));
		assertTrue(tg_s_2_3.equals(EqViewSchema.getSoftRuleTrigger(viewId2, 2)));
		assertTrue(tg_s_2_4.equals(EqViewSchema.getSoftRuleTrigger(viewId2, 3)));
		assertTrue(tg_heq_2_1.equals(EqViewSchema.getHardEQRuleTrigger(viewId2, 0)));
		assertTrue(tg_heq_2_2.equals(EqViewSchema.getHardEQRuleTrigger(viewId2, 1)));
		assertTrue(tg_heq_2_3.equals(EqViewSchema.getHardEQRuleTrigger(viewId2, 2)));
		assertTrue(tg_hneq_2_1.equals(EqViewSchema.getHardNEQRuleTrigger(viewId2, 0)));
		assertTrue(tg_hneq_2_2.equals(EqViewSchema.getHardNEQRuleTrigger(viewId2, 1)));
		assertTrue(tg_hneq_2_3.equals(EqViewSchema.getHardNEQRuleTrigger(viewId2, 2)));
		assertTrue(tg_hneq_2_4.equals(EqViewSchema.getHardNEQRuleTrigger(viewId2, 3)));
	}

	/**
	 * Tests indices are correctly created on entity partition, view, soft, hard eq and hard neq rule partitions table for two views
	 */
	public void testCreateIndex() {
		prepare();
		eqVSetup.createIndex(viewName1, viewId1);
		eqVSetup.createIndex(viewName2, viewId2);
		
		String indexEP1 = "";
		String indexView1 = "";
		String indexSP1 = "";
		String indexHEQP1 = "";
		String indexHNEQP1 = "";
		String indexEP2 = "";
		String indexView2 = "";
		String indexSP2 = "";
		String indexHEQP2 = "";
		String indexHNEQP2 = "";
		
		try {
			String query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getEPTableName(viewId1) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			ResultSet set = sql.executeSelectQuery(query);

			if(set.next())
				indexEP1 = set.getString(1);

			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + viewName1 + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexView1 = set.getString(1);

			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getSoftRulePartTableName(viewId1) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexSP1 = set.getString(1);

			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getHardEQRulePartTableName(viewId1) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexHEQP1 = set.getString(1);

			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getHardNEQRulePartTableName(viewId1) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexHNEQP1 = set.getString(1);
			
			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getEPTableName(viewId2) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexEP2 = set.getString(1);

			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + viewName2 + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexView2 = set.getString(1);

			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getSoftRulePartTableName(viewId2) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexSP2 = set.getString(1);

			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getHardEQRulePartTableName(viewId2) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexHEQP2 = set.getString(1);

			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getHardNEQRulePartTableName(viewId2) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexHNEQP2 = set.getString(1);
			
			
		} catch (SQLException e) {
				e.printStackTrace();
		}
		
		assertTrue(indexEP1.equals(EqViewSchema.getEnPTableIndexName(viewId1)));
		assertTrue(indexView1.equals(EqViewSchema.getEnCTableIndexName(viewId1)));
		assertTrue(indexSP1.equals(EqViewSchema.getSoftRulePartTableIndexName(viewId1)));
		assertTrue(indexHEQP1.equals(EqViewSchema.getHardEQRulePartTableIndexName(viewId1)));
		assertTrue(indexHNEQP1.equals(EqViewSchema.getHardNEQRulePartTableIndexName(viewId1)));
		assertTrue(indexEP2.equals(EqViewSchema.getEnPTableIndexName(viewId2)));
		assertTrue(indexView2.equals(EqViewSchema.getEnCTableIndexName(viewId2)));
		assertTrue(indexSP2.equals(EqViewSchema.getSoftRulePartTableIndexName(viewId2)));
		assertTrue(indexHEQP2.equals(EqViewSchema.getHardEQRulePartTableIndexName(viewId2)));
		assertTrue(indexHNEQP2.equals(EqViewSchema.getHardNEQRulePartTableIndexName(viewId2)));
	}

	/**
	 * Tests entity partition, view, soft, hard eq and hard neq rule partition tables are created correctly
	 */
	public void testCreateTables() {
		prepare();
		
		int epCount1 = 0;
		int viewNameCount1 = 0;
		int sPCount1 = 0;
		int heqCount1 = 0;
		int hneqCount1 = 0;
		int epCount2 = 0;
		int viewNameCount2 = 0;
		int sPCount2 = 0;
		int heqCount2 = 0;
		int hneqCount2 = 0;

		try {
			String query = "select count(*) from pg_class where relname = '" + EqViewSchema.getEPTableName(viewId1) + "'";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				epCount1 = set.getInt(1);
			
			query = "select count(*) from pg_class where relname = '" + viewName1 + "'";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				viewNameCount1 = set.getInt(1);
			
			query = "select count(*) from pg_class where relname = '" + EqViewSchema.getSoftRulePartTableName(viewId1) + "'";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				sPCount1 = set.getInt(1);
			
			query = "select count(*) from pg_class where relname = '" + EqViewSchema.getHardEQRulePartTableName(viewId1) + "'";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				heqCount1 = set.getInt(1);
			
			query = "select count(*) from pg_class where relname = '" + EqViewSchema.getHardNEQRulePartTableName(viewId1) + "'";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				hneqCount1 = set.getInt(1);
			
			query = "select count(*) from pg_class where relname = '" + EqViewSchema.getEPTableName(viewId2) + "'";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				epCount2 = set.getInt(1);
			
			query = "select count(*) from pg_class where relname = '" + viewName2 + "'";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				viewNameCount2 = set.getInt(1);
			
			query = "select count(*) from pg_class where relname = '" + EqViewSchema.getSoftRulePartTableName(viewId2) + "'";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				sPCount2 = set.getInt(1);
			
			query = "select count(*) from pg_class where relname = '" + EqViewSchema.getHardEQRulePartTableName(viewId2) + "'";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				heqCount2 = set.getInt(1);
			
			query = "select count(*) from pg_class where relname = '" + EqViewSchema.getHardNEQRulePartTableName(viewId2) + "'";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				hneqCount2 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(epCount1 == 1);
		assertTrue(viewNameCount1 == 1);
		assertTrue(sPCount1 == 1);
		assertTrue(heqCount1 == 1);
		assertTrue(hneqCount1 == 1);
		assertTrue(epCount2 == 1);
		assertTrue(viewNameCount2 == 1);
		assertTrue(sPCount2 == 1);
		assertTrue(heqCount2 == 1);
		assertTrue(hneqCount2 == 1);
	}

	/**
	 * Tests view related values (such as tao, acc) are correctly inserted to view catalogs
	 */
	public void testFillsCatalogTable() {
		prepare();
		
		int count1 = 0;
		int count2 = 0;
		
		try {
			String query = "SELECT COUNT(*) FROM " + EqViewSchema.getViewCatalogTableName(viewId1) + " WHERE view_id = " + viewId1 + " AND update_alg = '" + EqViewConstants.HAZY_ALG + 
				"' AND num_soft_rule = " + 4 + " AND total_soft_rw = " + totalSweight1 + " AND acc = " + EqViewConstants.INITIAL_ACC_COST + 
				" AND reorganize_time = " + reorganizeTime1 + " AND tao = " + EqViewConstants.TAO + ";";
			
			ResultSet set = sql.executeSelectQuery(query);
	
			if(set.next())
				count1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getViewCatalogTableName(viewId2) + " WHERE view_id = " + viewId2 + " AND update_alg = '" + EqViewConstants.HAZY_ALG + 
				"' AND num_soft_rule = " + 4 + " AND total_soft_rw = " + totalSweight2 + " AND acc = " + EqViewConstants.INITIAL_ACC_COST + 
				" AND reorganize_time = " + reorganizeTime2 + " AND tao = " + EqViewConstants.TAO + ";";
		
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(count1 == 1);
		assertTrue(count2 == 1);
	}

	/**
	 * Creates temp part table for viewid1 and viewid2 and then check whether they are actually created
	 */
	public void testCreateTempPartitionsTable() {
		eqVSetup.createTempPartitionsTable(viewId1);
		eqVSetup.createTempPartitionsTable(viewId2);
		
		int count1 = 0;
		int count2 = 0;
		
		try {
			String query = "select count(*) from pg_class where relname = '" + EqViewSchema.getTempPartitionsTableName(viewId1) + "'";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1 = set.getInt(1);
			
			query = "select count(*) from pg_class where relname = '" + EqViewSchema.getTempPartitionsTableName(viewId2) + "'";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				count2 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(count1 == 1);
		assertTrue(count2 == 1);
	}

	/**
	 * Retrieves catalog values for view1 and view2 and then check correct values are retrieved
	 */
	public void testGetCatalogValues() {
		prepare();
		CatalogAttributeValues c1 = eqVSetup.getCatalogValues(viewId1);
		CatalogAttributeValues c2 = eqVSetup.getCatalogValues(viewId2);
		
		assertTrue(c1.getUpdateAlg().equals(EqViewConstants.HAZY_ALG));
		assertTrue(c1.getNumOfSoftRule() == 4);
		assertTrue(c1.getTotalSoftRw() == totalSweight1);
		assertTrue(c1.getAcc() == EqViewConstants.INITIAL_ACC_COST);
		assertTrue(c1.getReorganizeTime() == reorganizeTime1);
		assertTrue(c1.getTao() == EqViewConstants.TAO);
		assertTrue(c2.getUpdateAlg().equals(EqViewConstants.HAZY_ALG));
		assertTrue(c2.getNumOfSoftRule() == 4);
		assertTrue(c2.getTotalSoftRw() == totalSweight2);
		assertTrue(c2.getAcc() == EqViewConstants.INITIAL_ACC_COST);
		assertTrue(c2.getReorganizeTime() == reorganizeTime2);
		assertTrue(c2.getTao() == EqViewConstants.TAO);
	}

	/**
	 * Updates catalog and then retrieve these updated values back. Check catalog tables is correctly updated or not
	 */
	public void testUpdateCatalogTable() {
		prepare();
		double newAcc1 = 3.2;
		double newReorganizeTime1 = 8.2;
		double newAcc2 = 5.7;
		double newReorganizeTime2 = 4.2;
		
		eqVSetup.updateCatalogTable(viewId1, newAcc1, newReorganizeTime1);
		eqVSetup.updateCatalogTable(viewId2, newAcc2, newReorganizeTime2);
		
		CatalogAttributeValues c1 = eqVSetup.getCatalogValues(viewId1);
		CatalogAttributeValues c2 = eqVSetup.getCatalogValues(viewId2);
		
		assertTrue(c1.getUpdateAlg().equals(EqViewConstants.HAZY_ALG));
		assertTrue(c1.getNumOfSoftRule() == 4);
		assertTrue(c1.getTotalSoftRw() == totalSweight1);
		assertFalse(c1.getAcc() == EqViewConstants.INITIAL_ACC_COST);
		assertTrue(c1.getAcc() == newAcc1);
		assertFalse(c1.getReorganizeTime() == reorganizeTime1);
		assertTrue(c1.getReorganizeTime() == newReorganizeTime1);
		assertTrue(c1.getTao() == EqViewConstants.TAO);
		assertTrue(c2.getUpdateAlg().equals(EqViewConstants.HAZY_ALG));
		assertTrue(c2.getNumOfSoftRule() == 4);
		assertTrue(c2.getTotalSoftRw() == totalSweight2);
		assertFalse(c2.getAcc() == EqViewConstants.INITIAL_ACC_COST);
		assertTrue(c2.getAcc() == newAcc2);
		assertFalse(c2.getReorganizeTime() == reorganizeTime2);
		assertTrue(c2.getReorganizeTime() == newReorganizeTime2);
		assertTrue(c2.getTao() == EqViewConstants.TAO);
	}

	/**
	 * Creates dummy temp entity cluster table and checks actualy view is correctly updated from temp entity cluster table 
	 */
	public void testUpdateEntityClustersTable() {
		//create dummy entity cluster table
		String viewQuery = "DROP TABLE IF EXISTS view; CREATE TABLE view(node int, cid int); INSERT INTO view VALUES(0, 0), (1, 1), (2, 2), (3, 1), (4, 0), (5, 2), (6, 6);";
		String tempCQuery = "DROP TABLE IF EXISTS tempC; CREATE TABLE tempC(nd int, clsid int);INSERT INTO tempc VALUES(0, 0), (1, 0), (2, 2), (3, 0), (4, 2), (5, 2), (6, 6);";
		sql.executeUpdateQuery(viewQuery);
		sql.executeUpdateQuery(tempCQuery);
		
		eqVSetup.updateEntityClustersTable("view", "tempC");
		
		int clsIdFor0 = 0;
		int clsIdFor1 = 0;
		int clsIdFor2 = 0;
		int clsIdFor3 = 0;
		int clsIdFor4 = 0;
		int clsIdFor5 = 0;
		int clsIdFor6 = 0;
		
		try {
			String query = "SELECT cid FROM view where node = 0;";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				clsIdFor0 = set.getInt(1);
			
			query = "SELECT cid FROM view where node = 1;";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				clsIdFor1 = set.getInt(1);
			
			query = "SELECT cid FROM view where node = 2;";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				clsIdFor2 = set.getInt(1);
			
			query = "SELECT cid FROM view where node = 3;";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				clsIdFor3 = set.getInt(1);
			
			query = "SELECT cid FROM view where node = 4;";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				clsIdFor4 = set.getInt(1);
			
			query = "SELECT cid FROM view where node = 5;";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				clsIdFor5 = set.getInt(1);
			
			query = "SELECT cid FROM view where node = 6;";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				clsIdFor6 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(clsIdFor0 == 0);
		assertTrue(clsIdFor1 == 0);
		assertTrue(clsIdFor2 == 2);
		assertTrue(clsIdFor3 == 0);
		assertTrue(clsIdFor4 == 2);
		assertTrue(clsIdFor5 == 2);
		assertTrue(clsIdFor6 == 6);
	}

	/**
	 * Creates dummy entity partition tables and check max pids are correctly retrieved or not
	 */
	public void testGetMaxPid() {
		prepare();
		String insertEP1Query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(0, 0), (1, 1), (2, 2), (0, 3), (1, 4), (5, 5), (5, 6), (2, 7);";
		String insertEP2Query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId2) + " VALUES(0, 0), (0, 1), (0, 2), (0, 3), (0, 4);";
		sql.executeUpdateQuery(insertEP1Query);
		sql.executeUpdateQuery(insertEP2Query);
		
		int maxPid1 = eqVSetup.getMaxPid(viewId1);
		int maxPid2 = eqVSetup.getMaxPid(viewId2);
		
		assertTrue(maxPid1 == 5);
		assertTrue(maxPid2 == 0);
	}

	/**
	 * For created two entity partition tables, check pids of nodes are correctly retrieved
	 */
	public void testGetPidForNode() {
		prepare();
		String insertEP1Query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(0, 0), (1, 1), (2, 2), (0, 3), (1, 4), (5, 5), (5, 6), (2, 7);";
		String insertEP2Query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId2) + " VALUES(0, 0), (0, 1), (0, 2), (0, 3), (0, 4);";
		sql.executeUpdateQuery(insertEP1Query);
		sql.executeUpdateQuery(insertEP2Query);
		
		int pid1_1 = eqVSetup.getPidForNode(viewId1, 0);
		int pid1_2 = eqVSetup.getPidForNode(viewId1, 1);
		int pid1_3 = eqVSetup.getPidForNode(viewId1, 2);
		int pid1_4 = eqVSetup.getPidForNode(viewId1, 3);
		int pid1_5 = eqVSetup.getPidForNode(viewId1, 4);
		int pid1_6 = eqVSetup.getPidForNode(viewId1, 5);
		int pid1_7 = eqVSetup.getPidForNode(viewId1, 6);
		int pid1_8 = eqVSetup.getPidForNode(viewId1, 7);
		int pid2_1 = eqVSetup.getPidForNode(viewId2, 0);
		int pid2_2 = eqVSetup.getPidForNode(viewId2, 1);
		int pid2_3 = eqVSetup.getPidForNode(viewId2, 2);
		int pid2_4 = eqVSetup.getPidForNode(viewId2, 3);
		int pid2_5 = eqVSetup.getPidForNode(viewId2, 4);
		
		assertTrue(pid1_1 == 0);
		assertTrue(pid1_2 == 1);
		assertTrue(pid1_3 == 2);
		assertTrue(pid1_4 == 0);
		assertTrue(pid1_5 == 1);
		assertTrue(pid1_6 == 5);
		assertTrue(pid1_7 == 5);
		assertTrue(pid1_8 == 2);
		assertTrue(pid2_1 == 0);
		assertTrue(pid2_2 == 0);
		assertTrue(pid2_3 == 0);
		assertTrue(pid2_4 == 0);
		assertTrue(pid2_5 == 0);
	}

	/**
	 * Tests view name with given view id is correctly retrieved
	 */
	public void testGetViewName() {
		prepare();
		
		String v1 = eqVSetup.getViewName(viewId1);
		String v2 = eqVSetup.getViewName(viewId2);
		
		assertTrue(v1.equals(viewName1));
		assertTrue(v2.equals(viewName2));
	}

	/**
	 * Tests weight of soft rule table is correctly retrieved with given view id and rule table name
	 */
	public void testGetSoftRuleTableWeight() {
		prepare();
		
		double w_1_1 = eqVSetup.getSoftRuleTableWeight(viewId1, softRules1.get(0).getTableName());
		double w_1_2 = eqVSetup.getSoftRuleTableWeight(viewId1, softRules1.get(1).getTableName());
		double w_1_3 = eqVSetup.getSoftRuleTableWeight(viewId1, softRules1.get(2).getTableName());
		double w_1_4 = eqVSetup.getSoftRuleTableWeight(viewId1, softRules1.get(3).getTableName());
		double w_2_1 = eqVSetup.getSoftRuleTableWeight(viewId2, softRules2.get(0).getTableName());
		double w_2_2 = eqVSetup.getSoftRuleTableWeight(viewId2, softRules2.get(1).getTableName());
		double w_2_3 = eqVSetup.getSoftRuleTableWeight(viewId2, softRules2.get(2).getTableName());
		double w_2_4 = eqVSetup.getSoftRuleTableWeight(viewId2, softRules2.get(3).getTableName());
		
		assertTrue(w_1_1 == s_w_1_1);
		assertTrue(w_1_2 == s_w_1_2);
		assertTrue(w_1_3 == s_w_1_3);
		assertTrue(w_1_4 == s_w_1_4);
		assertTrue(w_2_1 == s_w_2_1);
		assertTrue(w_2_2 == s_w_2_2);
		assertTrue(w_2_3 == s_w_2_3);
		assertTrue(w_2_4 == s_w_2_4);
	}

	/**
	 * It inserts edges to soft rule partition table and then tests whether their weights are correctly retrieved with given
	 * view id, pid1, node1, pid2 and node2
	 */
	public void testGetInitialSoftEdgeWeightIntIntIntIntInt() {
		prepare();
		String query1 = "INSERT INTO " + EqViewSchema.getSoftRulePartTableName(viewId1) + " VALUES(0, 0, 1, 1, 0.2), (1, 1, 1, 3, 2.12), (4, 4, 5, 5, 0.43), (1, 3, 5, 5, 0.12);";
		String query2 = "INSERT INTO " + EqViewSchema.getSoftRulePartTableName(viewId2) + " VALUES(0, 0, 0, 1, 3.2), (0, 1, 0, 3, 2.12);";
		sql.executeUpdateQuery(query1);
		sql.executeUpdateQuery(query2);
		
		double w1_1 = eqVSetup.getInitialSoftEdgeWeight(viewId1, 0, 0, 1, 1);
		double w1_2 = eqVSetup.getInitialSoftEdgeWeight(viewId1, 1, 1, 1, 3);
		double w1_3 = eqVSetup.getInitialSoftEdgeWeight(viewId1, 4, 4, 5, 5);
		double w1_4 = eqVSetup.getInitialSoftEdgeWeight(viewId1, 1, 3, 5, 5);
		double w2_1 = eqVSetup.getInitialSoftEdgeWeight(viewId2, 0, 0, 0, 1);
		double w2_2 = eqVSetup.getInitialSoftEdgeWeight(viewId2, 0, 1, 0, 3);
		
		assertTrue(w1_1 == 0.2);
		assertTrue(w1_2 == 2.12);
		assertTrue(w1_3 == 0.43);
		assertTrue(w1_4 == 0.12);
		assertTrue(w2_1 == 3.2);
		assertTrue(w2_2 == 2.12);
	}

	/**
	 * It inserts edges to soft rule partition table and then tests whether their weights are correctly retrieved with given
	 * view id, node1, and node2
	 */
	public void testGetInitialSoftEdgeWeightIntIntInt() {
		prepare();
		String query1 = "INSERT INTO " + EqViewSchema.getSoftRulePartTableName(viewId1) + " VALUES(0, 0, 1, 1, 0.2), (1, 1, 1, 3, 2.12), (4, 4, 5, 5, 0.43), (1, 3, 5, 5, 0.12);";
		String query2 = "INSERT INTO " + EqViewSchema.getSoftRulePartTableName(viewId2) + " VALUES(0, 0, 0, 1, 3.2), (0, 1, 0, 3, 2.12);";
		sql.executeUpdateQuery(query1);
		sql.executeUpdateQuery(query2);
		
		double w1_1 = eqVSetup.getInitialSoftEdgeWeight(viewId1, 0, 1);
		double w1_2 = eqVSetup.getInitialSoftEdgeWeight(viewId1, 1, 3);
		double w1_3 = eqVSetup.getInitialSoftEdgeWeight(viewId1, 4, 5);
		double w1_4 = eqVSetup.getInitialSoftEdgeWeight(viewId1, 3, 5);
		double w2_1 = eqVSetup.getInitialSoftEdgeWeight(viewId2, 0, 1);
		double w2_2 = eqVSetup.getInitialSoftEdgeWeight(viewId2, 1, 3);
		
		assertTrue(w1_1 == 0.2);
		assertTrue(w1_2 == 2.12);
		assertTrue(w1_3 == 0.43);
		assertTrue(w1_4 == 0.12);
		assertTrue(w2_1 == 3.2);
		assertTrue(w2_2 == 2.12);
	}

	/**
	 * Tests whether update edges are correctly inserted or not
	 */
	public void testInsertSoftUpdateEdge() {
		prepare();
		int src1_1 = 3;
		int dst1_1 = 6;
		int pid1_1_1 = 3;
		int pid2_1_1 = 6;
		double weight1_1 = 0.125;
		
		eqVSetup.insertSoftUpdateEdge(viewId1, pid1_1_1, pid2_1_1, src1_1, dst1_1, weight1_1);
		
		int src1_2 = 1;
		int dst1_2 = 4;
		int pid1_1_2 = -1;
		int pid2_1_2 = -1;
		double weight1_2 = 1.29;
		
		eqVSetup.insertSoftUpdateEdge(viewId1, pid1_1_2, pid2_1_2, src1_2, dst1_2, weight1_2);
		
		int src2_1 = 3;
		int dst2_1 = 6;
		int pid1_2_1 = 3;
		int pid2_2_1 = 6;
		double weight2_1 = 0.125;
		
		eqVSetup.insertSoftUpdateEdge(viewId2, pid1_2_1, pid2_2_1, src2_1, dst2_1, weight2_1);
		
		int src2_2 = 1;
		int dst2_2 = 4;
		int pid1_2_2 = -1;
		int pid2_2_2 = -1;
		double weight2_2 = 1.29;
		
		eqVSetup.insertSoftUpdateEdge(viewId2, pid1_2_2, pid2_2_2, src2_2, dst2_2, weight2_2);
		
		//for hazy
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, pid1_1_1, src1_1, pid2_1_1, dst1_1) == weight1_1);
		//for rescan
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, src1_2, dst1_2) == weight1_2);
		//for hazy
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, pid1_2_1, src2_1, pid2_2_1, dst2_1) == weight2_1);
		//for rescan
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, src2_2, dst2_2) == weight2_2);
	}

	/**
	 * Tests whether weight of update edges (also exists in soft rule partition table. hence, it needs to update weight only rather than inserting edge)
	 * are correclty updated
	 */
	public void testUpdateWeightOfSoftUpdateEdge() {
		prepare();
		int src1_1 = 3;
		int dst1_1 = 6;
		int pid1_1_1 = 3;
		int pid2_1_1 = 6;
		double weight1_1 = 0.125;
		
		eqVSetup.insertSoftUpdateEdge(viewId1, pid1_1_1, pid2_1_1, src1_1, dst1_1, weight1_1);
		
		int src1_2 = 1;
		int dst1_2 = 4;
		int pid1_1_2 = -1;
		int pid2_1_2 = -1;
		double weight1_2 = 1.29;
		
		eqVSetup.insertSoftUpdateEdge(viewId1, pid1_1_2, pid2_1_2, src1_2, dst1_2, weight1_2);
		
		int src2_1 = 3;
		int dst2_1 = 6;
		int pid1_2_1 = 3;
		int pid2_2_1 = 6;
		double weight2_1 = 0.125;
		
		eqVSetup.insertSoftUpdateEdge(viewId2, pid1_2_1, pid2_2_1, src2_1, dst2_1, weight2_1);
		
		int src2_2 = 1;
		int dst2_2 = 4;
		int pid1_2_2 = -1;
		int pid2_2_2 = -1;
		double weight2_2 = 1.29;
		
		eqVSetup.insertSoftUpdateEdge(viewId2, pid1_2_2, pid2_2_2, src2_2, dst2_2, weight2_2);
		
		int numUpd1_1 = eqVSetup.updateWeightOfSoftUpdateEdge(viewId1, pid1_1_1, pid2_1_1, src1_1, dst1_1, weight1_1 + 1 * 1);
		int numUpd1_2 = eqVSetup.updateWeightOfSoftUpdateEdge(viewId1, pid1_1_2, pid2_1_2, src1_2, dst1_2, weight1_2 + 1 * 2);
		//dummy update. no match
		int numUpd1_3 = eqVSetup.updateWeightOfSoftUpdateEdge(viewId1, 10, 1, 10, 1, weight1_2 + 1 * 2);

		int numUpd2_1 = eqVSetup.updateWeightOfSoftUpdateEdge(viewId2, pid1_2_1, pid2_2_1, src2_1, dst2_1, weight2_1 + 2 * 1);
		int numUpd2_2 = eqVSetup.updateWeightOfSoftUpdateEdge(viewId2, pid1_2_2, pid2_2_2, src2_2, dst2_2, weight2_2 + 2 * 2);
		//dummy update. no match
		int numUpd2_3 = eqVSetup.updateWeightOfSoftUpdateEdge(viewId2, 10, 1, 10, 1, weight1_2 + 1 * 2);
		
		//1 or 0(for dummy updates) should occur
		assertTrue(numUpd1_1 == 1);
		assertTrue(numUpd1_2 == 1);
		assertTrue(numUpd1_3 == 0);
		assertTrue(numUpd2_1 == 1);
		assertTrue(numUpd2_2 == 1);
		assertTrue(numUpd2_3 == 0);
		
		//for hazy
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, pid1_1_1, src1_1, pid2_1_1, dst1_1) == weight1_1 + 1 * 1);
		//for rescan
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, src1_2, dst1_2) == weight1_2 + 1 * 2);
		//for hazy
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, pid1_2_1, src2_1, pid2_2_1, dst2_1) == weight2_1 + 2 * 1);
		//for rescan
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, src2_2, dst2_2) == weight2_2 + 2 * 2);
	}

	/**
	 * Tests hard eq edge is correctly inserted
	 */
	public void testInsertHardEQUpdateEdge() {
		prepare();
		
		int src1_1 = 3;
		int dst1_1 = 5;
		int pid1_1 = 3;
		
		int src1_2 = 1;
		int dst1_2 = 2;
		int pid1_2 = 1;
		
		int src2_1 = 5;
		int dst2_1 = 6;
		int pid2_1 = 5;
		
		int src2_2 = 1;
		int dst2_2 = 3;
		int pid2_2 = 1;
		
		eqVSetup.insertHardEQUpdateEdge(viewId1, pid1_1, src1_1, dst1_1);
		eqVSetup.insertHardEQUpdateEdge(viewId1, pid1_2, src1_2, dst1_2);
		eqVSetup.insertHardEQUpdateEdge(viewId2, pid2_1, src2_1, dst2_1);
		eqVSetup.insertHardEQUpdateEdge(viewId2, pid2_2, src2_2, dst2_2);

		int count1_1 = 0;
		int count1_2 = 0;
		int count2_1 = 0;
		int count2_2 = 0;
		
		try {
			String query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_1 + 
					" AND node2 = " + dst1_1 + " AND pid = " + pid1_1 + ";";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_2 + 
				" AND node2 = " + dst1_2 + " AND pid = " + pid1_2 + ";";
			set = sql.executeSelectQuery(query);
	
			if(set.next())
				count1_2 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_1 + 
				" AND node2 = " + dst2_1 + " AND pid = " + pid2_1 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_2 + 
				" AND node2 = " + dst2_2 + " AND pid = " + pid2_2 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_2 = set.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(count1_1 == 1);
		assertTrue(count1_2 == 1);
		assertTrue(count2_1 == 1);
		assertTrue(count2_2 == 1);
	}

	/**
	 * Tests hard neq edge is correctly inserted
	 */
	public void testInsertHardNEQUpdateEdge() {
		prepare();
		
		int src1_1 = 3;
		int dst1_1 = 5;
		int pid1_1_1 = 3;
		int pid2_1_1 = 5;
		
		int src1_2 = 1;
		int dst1_2 = 2;
		int pid1_1_2 = 0;
		int pid2_1_2 = 2;
		
		int src2_1 = 5;
		int dst2_1 = 6;
		int pid1_2_1 = 5;
		int pid2_2_1 = 2;
		
		int src2_2 = 1;
		int dst2_2 = 3;
		int pid1_2_2 = 1;
		int pid2_2_2 = 0;
		
		eqVSetup.insertHardNEQUpdateEdge(viewId1, pid1_1_1, pid2_1_1, src1_1, dst1_1);
		eqVSetup.insertHardNEQUpdateEdge(viewId1, pid1_1_2, pid2_1_2, src1_2, dst1_2);
		eqVSetup.insertHardNEQUpdateEdge(viewId2, pid1_2_1, pid2_2_1, src2_1, dst2_1);
		eqVSetup.insertHardNEQUpdateEdge(viewId2, pid1_2_2, pid2_2_2, src2_2, dst2_2);

		int count1_1 = 0;
		int count1_2 = 0;
		int count2_1 = 0;
		int count2_2 = 0;
		
		try {
			String query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_1 + 
					" AND node2 = " + dst1_1 + " AND pid1 = " + pid1_1_1 + " AND pid2 = " + pid2_1_1 + ";";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_2 + 
				" AND node2 = " + dst1_2 + " AND pid1 = " + pid1_1_2 + " AND pid2 = " + pid2_1_2 + ";";
			set = sql.executeSelectQuery(query);
	
			if(set.next())
				count1_2 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_1 + 
				" AND node2 = " + dst2_1 + " AND pid1 = " + pid1_2_1 + " AND pid2 = " + pid2_2_1 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_2 + 
				" AND node2 = " + dst2_2 + " AND pid1 = " + pid1_2_2 + " AND pid2 = " + pid2_2_2 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_2 = set.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(count1_1 == 1);
		assertTrue(count1_2 == 1);
		assertTrue(count2_1 == 1);
		assertTrue(count2_2 == 1);
	}

	/**
	 * Tests entity partition table is correctly bulk-loaded from given file
	 */
	public void testCopyFromFileToEnPTable() {
		prepare();
		
		try {
			File file = new File("tempEnp");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(0 + "," + 0 + "\n");
			writer.write(0 + "," + 1 + "\n");
			writer.write(1 + "," + 2 + "\n");
			writer.write(3 + "," + 3 + "\n");
			writer.write(4 + "," + 4 + "\n");
			writer.write(0 + "," + 5 + "\n");
			writer.close();
			
			eqVSetup.copyFromFileToEnPTable(viewId1, file.getAbsolutePath());
			file.delete();
			
			file = new File("tempEnp");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(0 + "," + 0 + "\n");
			writer.write(1 + "," + 1 + "\n");
			writer.write(2 + "," + 2 + "\n");
			writer.write(3 + "," + 3 + "\n");
			writer.write(4 + "," + 4 + "\n");
			writer.write(5 + "," + 5 + "\n");
			writer.close();
			
			eqVSetup.copyFromFileToEnPTable(viewId2, file.getAbsolutePath());
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(eqVSetup.getPidForNode(viewId1, 0) == 0);
		assertTrue(eqVSetup.getPidForNode(viewId1, 1) == 0);
		assertTrue(eqVSetup.getPidForNode(viewId1, 2) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId1, 3) == 3);
		assertTrue(eqVSetup.getPidForNode(viewId1, 4) == 4);
		assertTrue(eqVSetup.getPidForNode(viewId1, 5) == 0);
		
		assertTrue(eqVSetup.getPidForNode(viewId2, 0) == 0);
		assertTrue(eqVSetup.getPidForNode(viewId2, 1) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId2, 2) == 2);
		assertTrue(eqVSetup.getPidForNode(viewId2, 3) == 3);
		assertTrue(eqVSetup.getPidForNode(viewId2, 4) == 4);
		assertTrue(eqVSetup.getPidForNode(viewId2, 5) == 5);
	}

	/**
	 * Tests soft rule partition table is correctly bulk-loaded from given file
	 */
	public void testCopyFromFileToSoftRulePartTable() {
		prepare();
		
		try {
			File file = new File("tempS");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(0 + "," + 0 + "," + 1 + "," + 2 + "," + 0.13 +  "\n");
			writer.write(1 + "," + 1 + "," + 1 + "," + 2 + "," + 1.13 +  "\n");
			writer.write(3 + "," + 3 + "," + 4 + "," + 5 + "," + 0.47 +  "\n");
			writer.close();
			
			eqVSetup.copyFromFileToSoftRulePartTable(viewId1, file.getAbsolutePath());
			file.delete();
			
			file = new File("tempS");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(1 + "," + 1 + "," + 4 + "," + 4 + "," + 0.14 + "\n");
			writer.write(2 + "," + 2 + "," + 4 + "," + 4 + "," + 0.23 +  "\n");
			writer.write(5 + "," + 5 + "," + 6 + "," + 6 + "," + 0.13 +  "\n");
			writer.close();
			
			eqVSetup.copyFromFileToSoftRulePartTable(viewId2, file.getAbsolutePath());
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 0, 0, 1, 2) == 0.13);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 1, 1, 1, 2) == 1.13);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 3, 3, 4, 5) == 0.47);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 1, 1, 4, 4) == 0.14);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 2, 2, 4, 4) == 0.23);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 5, 5, 6, 6) == 0.13);
	}

	/**
	 * Tests hard eq rule partition table is correctly bulk-loaded from given file
	 */
	public void testCopyFromFileToHardEQRulePartTable() {
		prepare();
		
		int src1_1 = 3;
		int dst1_1 = 5;
		int pid1_1 = 3;
		
		int src1_2 = 1;
		int dst1_2 = 2;
		int pid1_2 = 1;
		
		int src2_1 = 5;
		int dst2_1 = 6;
		int pid2_1 = 5;
		
		int src2_2 = 1;
		int dst2_2 = 3;
		int pid2_2 = 1;
		
		try {
			File file = new File("tempS");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(pid1_1 + "," + src1_1 + "," + dst1_1 +  "\n");
			writer.write(pid1_2 + "," + src1_2 + "," + dst1_2 +  "\n");
			writer.close();
			
			eqVSetup.copyFromFileToHardEQRulePartTable(viewId1, file.getAbsolutePath());
			file.delete();
			
			file = new File("tempS");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(pid2_1 + "," + src2_1 + "," + dst2_1 + "\n");
			writer.write(pid2_2 + "," + src2_2 + "," + dst2_2 +  "\n");
			writer.close();
			
			eqVSetup.copyFromFileToHardEQRulePartTable(viewId2, file.getAbsolutePath());
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int count1_1 = 0;
		int count1_2 = 0;
		int count2_1 = 0;
		int count2_2 = 0;
		
		try {
			String query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_1 + 
					" AND node2 = " + dst1_1 + " AND pid = " + pid1_1 + ";";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_2 + 
				" AND node2 = " + dst1_2 + " AND pid = " + pid1_2 + ";";
			set = sql.executeSelectQuery(query);
	
			if(set.next())
				count1_2 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_1 + 
				" AND node2 = " + dst2_1 + " AND pid = " + pid2_1 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_2 + 
				" AND node2 = " + dst2_2 + " AND pid = " + pid2_2 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_2 = set.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(count1_1 == 1);
		assertTrue(count1_2 == 1);
		assertTrue(count2_1 == 1);
		assertTrue(count2_2 == 1);
	}

	/**
	 * Tests hard neq rule partition table is correctly bulk-loaded from given file
	 */
	public void testCopyFromFileToHardNEQRulePartTable() {
		prepare();
		
		int src1_1 = 3;
		int dst1_1 = 5;
		int pid1_1_1 = 3;
		int pid2_1_1 = 5;
		
		int src1_2 = 1;
		int dst1_2 = 2;
		int pid1_1_2 = 0;
		int pid2_1_2 = 2;
		
		int src2_1 = 5;
		int dst2_1 = 6;
		int pid1_2_1 = 5;
		int pid2_2_1 = 2;
		
		int src2_2 = 1;
		int dst2_2 = 3;
		int pid1_2_2 = 1;
		int pid2_2_2 = 0;
		
		try {
			File file = new File("tempS");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(pid1_1_1 + "," + src1_1 + "," + pid2_1_1 + "," + dst1_1 +  "\n");
			writer.write(pid1_1_2 + "," + src1_2 + "," + pid2_1_2 + "," + dst1_2 +  "\n");
			writer.close();
			
			eqVSetup.copyFromFileToHardNEQRulePartTable(viewId1, file.getAbsolutePath());
			file.delete();
			
			file = new File("tempS");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(pid1_2_1 + "," + src2_1 + "," + pid2_2_1 + "," + dst2_1 + "\n");
			writer.write(pid1_2_2 + "," + src2_2 + "," + pid2_2_2 + "," + dst2_2 +  "\n");
			writer.close();
			
			eqVSetup.copyFromFileToHardNEQRulePartTable(viewId2, file.getAbsolutePath());
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int count1_1 = 0;
		int count1_2 = 0;
		int count2_1 = 0;
		int count2_2 = 0;
		
		try {
			String query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_1 + 
					" AND node2 = " + dst1_1 + " AND pid1 = " + pid1_1_1 + " AND pid2 = " + pid2_1_1 + ";";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_2 + 
				" AND node2 = " + dst1_2 + " AND pid1 = " + pid1_1_2 + " AND pid2 = " + pid2_1_2 + ";";
			set = sql.executeSelectQuery(query);
	
			if(set.next())
				count1_2 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_1 + 
				" AND node2 = " + dst2_1 + " AND pid1 = " + pid1_2_1 + " AND pid2 = " + pid2_2_1 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_2 + 
				" AND node2 = " + dst2_2 + " AND pid1 = " + pid1_2_2 + " AND pid2 = " + pid2_2_2 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_2 = set.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(count1_1 == 1);
		assertTrue(count1_2 == 1);
		assertTrue(count2_1 == 1);
		assertTrue(count2_2 == 1);
	}

	/**
	 * Tests temp entity partition table is correctly bulk-loaded from given file
	 */
	public void testCopyFromFileToTempPartTable() {
		prepare();
		eqVSetup.createTempPartitionsTable(viewId1);
		eqVSetup.createTempPartitionsTable(viewId2);
		
		try {
			File file = new File("tempEnp");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(0 + "," + 0 + "\n");
			writer.write(0 + "," + 1 + "\n");
			writer.write(2 + "," + 2 + "\n");
			writer.close();
			
			eqVSetup.copyFromFileToTempPartTable(viewId1, file.getAbsolutePath());
			file.delete();
			
			file = new File("tempEnp");
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(0 + "," + 0 + "\n");
			writer.write(1 + "," + 1 + "\n");
			writer.write(2 + "," + 2 + "\n");
			writer.close();
			
			eqVSetup.copyFromFileToTempPartTable(viewId2, file.getAbsolutePath());
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int count1_1 = 0;
		int count1_2 = 0;
		int count1_3 = 0;
		int count2_1 = 0;
		int count2_2 = 0;
		int count2_3 = 0;

		try {
			String query = "SELECT COUNT(*) FROM " + EqViewSchema.getTempPartitionsTableName(viewId1) + " WHERE partid = " + 0 + " AND oldpartid = " + 0 + ";";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getTempPartitionsTableName(viewId1) + " WHERE partid = " + 0 + " AND oldpartid = " + 1 + ";";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1_2 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getTempPartitionsTableName(viewId1) + " WHERE partid = " + 2 + " AND oldpartid = " + 2 + ";";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1_3 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getTempPartitionsTableName(viewId2) + " WHERE partid = " + 0 + " AND oldpartid = " + 0 + ";";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				count2_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getTempPartitionsTableName(viewId2) + " WHERE partid = " + 1 + " AND oldpartid = " + 1 + ";";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				count2_2 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getTempPartitionsTableName(viewId2) + " WHERE partid = " + 2 + " AND oldpartid = " + 2 + ";";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				count2_3 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		assertTrue(count1_1 == 1);
		assertTrue(count1_2 == 1);
		assertTrue(count1_3 == 1);
		assertTrue(count2_1 == 1);
		assertTrue(count2_2 == 1);
		assertTrue(count2_3 == 1);
	}

	/**
	 * Tests arbitrary table is correctly bulk-loaded from given file
	 */
	public void testCopyFromFileToTable() {
		prepare();
		String createQuery = "DROP TABLE IF EXISTS test_method_table; CREATE TABLE test_method_table(id1 int, id2 int, id3 int);";
		sql.executeUpdateQuery(createQuery);
		
		try {
			File file = new File("tempF");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(0 + "," + 0 + "," + 1 + "\n");
			writer.write(0 + "," + 1 + "," + 2 + "\n");
			writer.write(2 + "," + 2 + "," + -1 + "\n");
			writer.close();
			
			eqVSetup.copyFromFileToTable("test_method_table", file.getAbsolutePath());
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		
		try {
			String query = "SELECT COUNT(*) FROM test_method_table WHERE id1 = " + 0 + " AND id2 = " + 0 + " AND id3 = " + 1 + ";";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM test_method_table WHERE id1 = " + 0 + " AND id2 = " + 1 + " AND id3 = " + 2 + ";";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				count2 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM test_method_table WHERE id1 = " + 2 + " AND id2 = " + 2 + " AND id3 = " + -1 + ";";
			set = sql.executeSelectQuery(query);
			
			if(set.next())
				count3 = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(count1 == 1);
		assertTrue(count2 == 1);
		assertTrue(count3 == 1);
	}

	/**
	 * Creates two entity partition tables (disk partitions) and two temp partition tables (on-memory partitions) and then checks entity partitions are
	 * correctly updated from temp partitions
	 */
	public void testForceEnPTableUpdate() {
		prepare();
		String insertEP1Query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(0, 0), (1, 1), (2, 2), (0, 3), (1, 4), (5, 5), (5, 6), (2, 7);";
		String insertEP2Query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId2) + " VALUES(0, 0), (0, 1), (2, 2), (3, 3), (0, 4);";
		eqVSetup.createTempPartitionsTable(viewId1);
		eqVSetup.createTempPartitionsTable(viewId2);
		
		String insertTemp1Query = "INSERT INTO " + EqViewSchema.getTempPartitionsTableName(viewId1) + " VALUES(1, 5), (0, 2);";
		String insertTemp2Query = "INSERT INTO " + EqViewSchema.getTempPartitionsTableName(viewId2) + " VALUES(0, 2), (0, 3);";
		
		sql.executeUpdateQuery(insertEP1Query);
		sql.executeUpdateQuery(insertEP2Query);
		sql.executeUpdateQuery(insertTemp1Query);
		sql.executeUpdateQuery(insertTemp2Query);
		
		eqVSetup.forceEnPTableUpdate(viewId1);
		eqVSetup.forceEnPTableUpdate(viewId2);
		
		assertTrue(eqVSetup.getPidForNode(viewId1, 0) == 0);
		assertTrue(eqVSetup.getPidForNode(viewId1, 1) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId1, 2) == 0);
		assertTrue(eqVSetup.getPidForNode(viewId1, 3) == 0);
		assertTrue(eqVSetup.getPidForNode(viewId1, 4) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId1, 5) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId1, 6) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId1, 7) == 0);
		assertTrue(eqVSetup.getPidForNode(viewId2, 0) == 0);
		assertTrue(eqVSetup.getPidForNode(viewId2, 1) == 0);
		assertTrue(eqVSetup.getPidForNode(viewId2, 2) == 0);
		assertTrue(eqVSetup.getPidForNode(viewId2, 3) == 0);
		assertTrue(eqVSetup.getPidForNode(viewId2, 4) == 0);
		
		String indexEP1 = "";
		String indexEP2 = "";

		try {
			String query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getEPTableName(viewId1) + "'" + 
				" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				indexEP1 = set.getString(1);
			
			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getEPTableName(viewId2) + "'" + 
				" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);
		
			if(set.next())
				indexEP2 = set.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(indexEP1.equals(EqViewSchema.getEnPTableIndexName(viewId1)));
		assertTrue(indexEP2.equals(EqViewSchema.getEnPTableIndexName(viewId2)));
	}

	/**
	 * Creates two temp partition tables (on-memory partitions) and then update partitions of soft rule table edges. Then, checks partitions are
	 * correctly updated from temp partitions
	 */
	public void testForceSoftRulePartTableUpdate() {
		testCopyFromFileToSoftRulePartTable();
		eqVSetup.createTempPartitionsTable(viewId1);
		eqVSetup.createTempPartitionsTable(viewId2);
		String insertTemp1Query = "INSERT INTO " + EqViewSchema.getTempPartitionsTableName(viewId1) + " VALUES(0, 4), (1, 3);";
		String insertTemp2Query = "INSERT INTO " + EqViewSchema.getTempPartitionsTableName(viewId2) + " VALUES(2, 4), (5, 6);";
		sql.executeUpdateQuery(insertTemp1Query);
		sql.executeUpdateQuery(insertTemp2Query);
		
		eqVSetup.forceSoftRulePartTableUpdate(viewId1);
		eqVSetup.forceSoftRulePartTableUpdate(viewId2);
		
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 0, 0, 1, 2) == 0.13);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 1, 1, 1, 2) == 1.13);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 1, 3, 0, 5) == 0.47);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 1, 1, 2, 4) == 0.14);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 2, 2, 2, 4) == 0.23);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 5, 5, 5, 6) == 0.13);
		
		String indexSP1 = "";
		String indexSP2 = "";
		
		try {
			String query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getSoftRulePartTableName(viewId1) + "'" + 
				" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			ResultSet set = sql.executeSelectQuery(query);

			if(set.next())
				indexSP1 = set.getString(1);

			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getSoftRulePartTableName(viewId2) + "'" + 
				" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexSP2 = set.getString(1);

		} catch (SQLException e) {
				e.printStackTrace();
		}
		
		assertTrue(indexSP1.equals(EqViewSchema.getSoftRulePartTableIndexName(viewId1)));
		assertTrue(indexSP2.equals(EqViewSchema.getSoftRulePartTableIndexName(viewId2)));
	}

	/**
	 * Creates two temp partition tables (on-memory partitions) and then update partitions of hard eq rule table edges. Then, checks partitions are
	 * correctly updated from temp partitions
	 */
	public void testForceHardEQRulePartTableUpdate() {
		testCopyFromFileToHardEQRulePartTable();
		eqVSetup.createTempPartitionsTable(viewId1);
		eqVSetup.createTempPartitionsTable(viewId2);
		
		String insertTemp1Query = "INSERT INTO " + EqViewSchema.getTempPartitionsTableName(viewId1) + " VALUES(0, 3), (0, 1);";
		String insertTemp2Query = "INSERT INTO " + EqViewSchema.getTempPartitionsTableName(viewId2) + " VALUES(1, 5);";
		sql.executeUpdateQuery(insertTemp1Query);
		sql.executeUpdateQuery(insertTemp2Query);
		eqVSetup.forceHardEQRulePartTableUpdate(viewId1);
		eqVSetup.forceHardEQRulePartTableUpdate(viewId2);
		
		int src1_1 = 3;
		int dst1_1 = 5;
		int pid1_1 = 0;
		
		int src1_2 = 1;
		int dst1_2 = 2;
		int pid1_2 = 0;
		
		int src2_1 = 5;
		int dst2_1 = 6;
		int pid2_1 = 1;
		
		int src2_2 = 1;
		int dst2_2 = 3;
		int pid2_2 = 1;
		
		int count1_1 = 0;
		int count1_2 = 0;
		int count2_1 = 0;
		int count2_2 = 0;
		
		try {
			String query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_1 + 
					" AND node2 = " + dst1_1 + " AND pid = " + pid1_1 + ";";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_2 + 
				" AND node2 = " + dst1_2 + " AND pid = " + pid1_2 + ";";
			set = sql.executeSelectQuery(query);
	
			if(set.next())
				count1_2 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_1 + 
				" AND node2 = " + dst2_1 + " AND pid = " + pid2_1 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_2 + 
				" AND node2 = " + dst2_2 + " AND pid = " + pid2_2 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_2 = set.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String indexHEQP1 = "";
		String indexHEQP2 = "";
		
		try {
			String query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getHardEQRulePartTableName(viewId1) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			ResultSet set = sql.executeSelectQuery(query);

			if(set.next())
				indexHEQP1 = set.getString(1);

			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getHardEQRulePartTableName(viewId2) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexHEQP2 = set.getString(1);
			
		} catch (SQLException e) {
				e.printStackTrace();
		}
		
		assertTrue(count1_1 == 1);
		assertTrue(count1_2 == 1);
		assertTrue(count2_1 == 1);
		assertTrue(count2_2 == 1);
		assertTrue(indexHEQP1.equals(EqViewSchema.getHardEQRulePartTableIndexName(viewId1)));
		assertTrue(indexHEQP2.equals(EqViewSchema.getHardEQRulePartTableIndexName(viewId2)));
	}

	/**
	 * Creates two temp partition tables (on-memory partitions) and then update partitions of hard neq rule table edges. Then, checks partitions are
	 * correctly updated from temp partitions
	 */
	public void testForceHardNEQRulePartTableUpdate() {
		testCopyFromFileToHardNEQRulePartTable();
		eqVSetup.createTempPartitionsTable(viewId1);
		eqVSetup.createTempPartitionsTable(viewId2);
		
		String insertTemp1Query = "INSERT INTO " + EqViewSchema.getTempPartitionsTableName(viewId1) + " VALUES(0, 3), (0, 1);";
		String insertTemp2Query = "INSERT INTO " + EqViewSchema.getTempPartitionsTableName(viewId2) + " VALUES(1, 5);";
		sql.executeUpdateQuery(insertTemp1Query);
		sql.executeUpdateQuery(insertTemp2Query);
		eqVSetup.forceHardNEQRulePartTableUpdate(viewId1);
		eqVSetup.forceHardNEQRulePartTableUpdate(viewId2);
		
		int src1_1 = 3;
		int dst1_1 = 5;
		int pid1_1_1 = 0;
		int pid2_1_1 = 5;
		
		int src1_2 = 1;
		int dst1_2 = 2;
		int pid1_1_2 = 0;
		int pid2_1_2 = 2;
		
		int src2_1 = 5;
		int dst2_1 = 6;
		int pid1_2_1 = 1;
		int pid2_2_1 = 2;
		
		int src2_2 = 1;
		int dst2_2 = 3;
		int pid1_2_2 = 1;
		int pid2_2_2 = 0;
		

		int count1_1 = 0;
		int count1_2 = 0;
		int count2_1 = 0;
		int count2_2 = 0;
		
		try {
			String query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_1 + 
					" AND node2 = " + dst1_1 + " AND pid1 = " + pid1_1_1 + " AND pid2 = " + pid2_1_1 + ";";
			ResultSet set = sql.executeSelectQuery(query);
			
			if(set.next())
				count1_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + " WHERE node1 = " + src1_2 + 
				" AND node2 = " + dst1_2 + " AND pid1 = " + pid1_1_2 + " AND pid2 = " + pid2_1_2 + ";";
			set = sql.executeSelectQuery(query);
	
			if(set.next())
				count1_2 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_1 + 
				" AND node2 = " + dst2_1 + " AND pid1 = " + pid1_2_1 + " AND pid2 = " + pid2_2_1 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_1 = set.getInt(1);
			
			query = "SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + " WHERE node1 = " + src2_2 + 
				" AND node2 = " + dst2_2 + " AND pid1 = " + pid1_2_2 + " AND pid2 = " + pid2_2_2 + ";";
			set = sql.executeSelectQuery(query);

			if(set.next())
				count2_2 = set.getInt(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String indexHNEQP1 = "";
		String indexHNEQP2 = "";
		
		try {
			String query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getHardNEQRulePartTableName(viewId1) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			ResultSet set = sql.executeSelectQuery(query);

			if(set.next())
				indexHNEQP1 = set.getString(1);
			
			query = "select p1.relname from pg_class p1, pg_index p2, pg_class p3 where p3.relname='" + EqViewSchema.getHardNEQRulePartTableName(viewId2) + "'" + 
			" and p2.indexrelid = p1.oid and p2.indrelid = p3.oid;";
			set = sql.executeSelectQuery(query);

			if(set.next())
				indexHNEQP2 = set.getString(1);
			
			
		} catch (SQLException e) {
				e.printStackTrace();
		}
		
		assertTrue(count1_1 == 1);
		assertTrue(count1_2 == 1);
		assertTrue(count2_1 == 1);
		assertTrue(count2_2 == 1);
		assertTrue(indexHNEQP1.equals(EqViewSchema.getHardNEQRulePartTableIndexName(viewId1)));
		assertTrue(indexHNEQP2.equals(EqViewSchema.getHardNEQRulePartTableIndexName(viewId2)));
	}

	/**
	 * Creates an entity table and then checks mapping between entity ids and graph ids are consistent
	 */
	@SuppressWarnings("unchecked")
	public void testGetMapping() {
		prepare();
		String query = "DROP TABLE IF EXISTS entityT; CREATE TABLE entityT(id int); INSERT INTO entityT VALUES(5),(6),(7),(8),(9),(12)";
		sql.executeUpdateQuery(query);
		
		query = "SELECT id FROM entityT ORDER BY id;";
		ArrayList<Object> list = eqVSetup.getMapping(query);
		ArrayList<Integer> mapping = (ArrayList<Integer>) list.get(0);
		LinkedHashMap<Integer, Integer> backMapping = (LinkedHashMap<Integer, Integer>) list.get(1);
		
		assertTrue(backMapping.get(5) == 0);
		assertTrue(backMapping.get(6) == 1);
		assertTrue(backMapping.get(7) == 2);
		assertTrue(backMapping.get(8) == 3);
		assertTrue(backMapping.get(9) == 4);
		assertTrue(backMapping.get(12) == 5);
		assertTrue(mapping.get(0) == 5);
		assertTrue(mapping.get(1) == 6);
		assertTrue(mapping.get(2) == 7);
		assertTrue(mapping.get(3) == 8);
		assertTrue(mapping.get(4) == 9);
		assertTrue(mapping.get(5) == 12);
	}

	/**
	 * Creates an entity partition table and then checks mapping between entity ids and graph ids are consistent
	 */
	@SuppressWarnings("unchecked")
	public void testGetMappingFromEntityTable() {
		prepare();
		String query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(5, 5),(5, 6),(7, 7),(5, 8),(7, 9),(12, 12)";
		sql.executeUpdateQuery(query);
		ArrayList<Object> list = eqVSetup.getMappingFromEntityTable(viewId1);
		ArrayList<Integer> mapping = (ArrayList<Integer>) list.get(0);
		LinkedHashMap<Integer, Integer> backMapping = (LinkedHashMap<Integer, Integer>) list.get(1);
		
		assertTrue(backMapping.get(5) == 0);
		assertTrue(backMapping.get(6) == 1);
		assertTrue(backMapping.get(7) == 2);
		assertTrue(backMapping.get(8) == 3);
		assertTrue(backMapping.get(9) == 4);
		assertTrue(backMapping.get(12) == 5);
		assertTrue(mapping.get(0) == 5);
		assertTrue(mapping.get(1) == 6);
		assertTrue(mapping.get(2) == 7);
		assertTrue(mapping.get(3) == 8);
		assertTrue(mapping.get(4) == 9);
		assertTrue(mapping.get(5) == 12);
	}

	/**
	 * Creates an entity partition table and tests whether mapping retrieved for two different partitions are correct.
	 */
	public void testGetMappingIncrementally() {
		prepare();
		String query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(5, 5),(5, 6),(7, 7),(5, 8),(7, 9),(12, 12)";
		sql.executeUpdateQuery(query);
		
		ArrayList<Integer> mapping = new ArrayList<Integer>();
		LinkedHashMap<Integer, Integer> backMapping = new LinkedHashMap<Integer, Integer>();
		
		eqVSetup.getMappingIncrementally(viewId1, 7, mapping, backMapping);
		assertTrue(backMapping.get(7) == 0);
		assertTrue(backMapping.get(9) == 1);
		assertTrue(backMapping.keySet().size() == 2);
		assertTrue(mapping.size() == 2);
		eqVSetup.getMappingIncrementally(viewId1, 5, mapping, backMapping);
		assertTrue(backMapping.get(5) == 2);
		assertTrue(backMapping.get(6) == 3);
		assertTrue(backMapping.get(8) == 4);
		assertTrue(backMapping.keySet().size() == 5);
		assertTrue(mapping.size() == 5);
	}

	/**
	 * Creates two hard eq rule and two hard neq rule tables. and then checks retrieved edges (with mapping) are correct.
	 * <p>
	 * Before edges from rule tables are retrieved, their ids are mapped into graph ids and edges are created with graph ids.
	 * This method checks these edges are correct.
	 */
	@SuppressWarnings("unchecked")
	public void testGenerateHardPosNegEdgesArrayListOfRuleTableLinkedHashMapOfIntegerInteger() {
		prepare();
		String query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(5, 5),(5, 6),(7, 7),(5, 8),(7, 9),(12, 12)";
		sql.executeUpdateQuery(query);
		ArrayList<Object> list = eqVSetup.getMappingFromEntityTable(viewId1);
		LinkedHashMap<Integer, Integer> backMapping = (LinkedHashMap<Integer, Integer>) list.get(1);
		
		query = "DROP TABLE IF EXISTS hardeq1; CREATE TABLE hardeq1(src int, dst int); INSERT INTO hardeq1 VALUES(5, 7), (5, 6), (9, 12);";
		query += "DROP TABLE IF EXISTS hardeq2; CREATE TABLE hardeq2(src int, dst int); INSERT INTO hardeq2 VALUES(5, 8), (6, 8), (8, 12);";
		sql.executeUpdateQuery(query);
		
		ArrayList<RuleTable> tables = new ArrayList<RuleTable>();
		tables.add(new RuleTable("hardeq1", Double.POSITIVE_INFINITY));
		tables.add(new RuleTable("hardeq2", Double.POSITIVE_INFINITY));
		LinkedHashMap<Edge, Integer> hardEQEdges = eqVSetup.generateHardPosNegEdges(tables, backMapping);
		assertTrue(hardEQEdges.get(new Edge(0, 1)) == 1);
		assertTrue(hardEQEdges.get(new Edge(0, 2)) == 1);
		assertTrue(hardEQEdges.get(new Edge(0, 3)) == 1);
		assertTrue(hardEQEdges.get(new Edge(1, 3)) == 1);
		assertTrue(hardEQEdges.get(new Edge(3, 5)) == 1);
		assertTrue(hardEQEdges.get(new Edge(4, 5)) == 1);
		
		query = "DROP TABLE IF EXISTS hardneq1; CREATE TABLE hardneq1(src int, dst int); INSERT INTO hardneq1 VALUES(5, 7), (5, 6), (9, 12);";
		query += "DROP TABLE IF EXISTS hardneq2; CREATE TABLE hardneq2(src int, dst int); INSERT INTO hardneq2 VALUES(5, 8), (6, 8), (8, 12);";
		sql.executeUpdateQuery(query);
		
		ArrayList<RuleTable> hardNEQtables = new ArrayList<RuleTable>();
		hardNEQtables.add(new RuleTable("hardneq1", Double.NEGATIVE_INFINITY));
		hardNEQtables.add(new RuleTable("hardneq2", Double.NEGATIVE_INFINITY));
		LinkedHashMap<Edge, Integer> hardNEQEdges = eqVSetup.generateHardPosNegEdges(tables, backMapping);
		assertTrue(hardNEQEdges.get(new Edge(0, 1)) == 1);
		assertTrue(hardNEQEdges.get(new Edge(0, 2)) == 1);
		assertTrue(hardNEQEdges.get(new Edge(0, 3)) == 1);
		assertTrue(hardNEQEdges.get(new Edge(1, 3)) == 1);
		assertTrue(hardNEQEdges.get(new Edge(3, 5)) == 1);
		assertTrue(hardNEQEdges.get(new Edge(4, 5)) == 1);
	}

	/**
	 * Creates three soft rule tables. and then checks retrieved edges (with mapping) are correct.
	 * <p>
	 * Before edges from rule tables are retrieved, their ids are mapped into graph ids and edges are created with graph ids.
	 * This method checks these edges are correct. Also it tests whether weight of retrieved edges are correct.
	 */
	@SuppressWarnings("unchecked")
	public void testGenerateSoftEdgesArrayListOfRuleTableLinkedHashMapOfIntegerIntegerBoolean() {
		prepare();
		String query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(5, 5),(5, 6),(7, 7),(5, 8),(7, 9),(12, 12)";
		sql.executeUpdateQuery(query);
		ArrayList<Object> list = eqVSetup.getMappingFromEntityTable(viewId1);
		LinkedHashMap<Integer, Integer> backMapping = (LinkedHashMap<Integer, Integer>) list.get(1);
		
		query = "DROP TABLE IF EXISTS sr1; CREATE TABLE sr1(src int, dst int); INSERT INTO sr1 VALUES(5, 7), (5, 6), (9, 12);";
		query += "DROP TABLE IF EXISTS sr2; CREATE TABLE sr2(src int, dst int); INSERT INTO sr2 VALUES(5, 8), (6, 8), (8, 12);";
		query += "DROP TABLE IF EXISTS sr3; CREATE TABLE sr3(src int, dst int); INSERT INTO sr3 VALUES(5, 7), (6, 8), (9, 12);";
		sql.executeUpdateQuery(query);
		
		ArrayList<RuleTable> srTables = new ArrayList<RuleTable>();
		srTables.add(new RuleTable("sr1", 1.11));
		srTables.add(new RuleTable("sr2", 2.22));
		srTables.add(new RuleTable("sr3", 1.1));
		LinkedHashMap<Edge, Double> sEdges = eqVSetup.generateSoftEdges(srTables, backMapping, true);

		assertTrue(sEdges.get(new Edge(0, 1)) == 1.11);
		assertTrue(sEdges.get(new Edge(0, 2)) == (1.11 + 1.1));
		assertTrue(sEdges.get(new Edge(0, 3)) == 2.22);
		assertTrue(sEdges.get(new Edge(1, 3)) == (2.22 + 1.1));
		assertTrue(sEdges.get(new Edge(3, 5)) == 2.22);
		assertTrue(sEdges.get(new Edge(4, 5)) == 2.21);
	}

	/**
	 * Creates soft rule partition table and checks retrieved edges (with mapping) are correct.
	 * <p>
	 * Before edges from rule partition table is retrieved, their ids are mapped into graph ids and edges are created with graph ids.
	 * If there is only one soft rule table, then all edges are retrieved (weights are not checked)
	 * Else edges with weight / total weight >= THRESHOLD are retrieved
	 */
	@SuppressWarnings("unchecked")
	public void testGenerateSoftEdgesIntLinkedHashMapOfIntegerIntegerBoolean() {
		prepare();
		String query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(5, 5),(5, 6),(7, 7),(5, 8),(7, 9),(12, 12)";
		sql.executeUpdateQuery(query);
		ArrayList<Object> list = eqVSetup.getMappingFromEntityTable(viewId1);
		LinkedHashMap<Integer, Integer> backMapping = (LinkedHashMap<Integer, Integer>) list.get(1);
		
		query = "INSERT INTO " + EqViewSchema.getSoftRulePartTableName(viewId1) + " VALUES(5,5,5,6,1.11), (5,5,7,7,2.21), (5,5,5,8,2.22), " +
				"(5,6,5,8,3.32), (5,8,12,12,2.22), (7,9,12,12,2.21);";
		sql.executeUpdateQuery(query);
		LinkedHashMap<Edge, Double> softEdges1 = eqVSetup.generateSoftEdges(viewId1, backMapping, true, 4.43);
		LinkedHashMap<Edge, Double> softEdges2 = eqVSetup.generateSoftEdges(viewId1, backMapping, false, 4.43);
		
		assertTrue(softEdges1.get(new Edge(0, 3)) == 1);
		assertTrue(softEdges1.get(new Edge(1, 3)) == 1);
		assertTrue(softEdges1.get(new Edge(3, 5)) == 1);
		assertTrue(softEdges1.keySet().size() == 3);
		assertTrue(softEdges2.get(new Edge(0, 1)) == 1);
		assertTrue(softEdges2.get(new Edge(0, 2)) == 1);
		assertTrue(softEdges2.get(new Edge(0, 3)) == 1);
		assertTrue(softEdges2.get(new Edge(1, 3)) == 1);
		assertTrue(softEdges2.get(new Edge(3, 5)) == 1);
		assertTrue(softEdges2.get(new Edge(4, 5)) == 1);
		assertTrue(softEdges2.keySet().size() == 6);
	}

	/**
	 * For given hard eq and hard neq rule partition tables, check they are retrieved correctly or not.
	 */
	@SuppressWarnings("unchecked")
	public void testGenerateHardPosNegEdgesIntLinkedHashMapOfIntegerIntegerBoolean() {
		prepare();
		String query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(5, 5),(5, 6),(7, 7),(5, 8),(7, 9),(12, 12)";
		sql.executeUpdateQuery(query);
		ArrayList<Object> list = eqVSetup.getMappingFromEntityTable(viewId1);
		LinkedHashMap<Integer, Integer> backMapping = (LinkedHashMap<Integer, Integer>) list.get(1);
		
		query = "INSERT INTO " + EqViewSchema.getHardEQRulePartTableName(viewId1) + " VALUES(5, 5, 6), (5, 6, 8), (7, 7, 9);";
		query += "INSERT INTO " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + " VALUES(5, 6, 7, 7), (5, 8, 7, 9);";
		sql.executeUpdateQuery(query);
		
		LinkedHashMap<Edge, Integer> hardEQEdges = eqVSetup.generateHardPosNegEdges(viewId1, backMapping, true);
		LinkedHashMap<Edge, Integer> hardNEQEdges = eqVSetup.generateHardPosNegEdges(viewId1, backMapping, false);
		
		assertTrue(hardEQEdges.get(new Edge(0, 1)) == 1);
		assertTrue(hardEQEdges.get(new Edge(1, 3)) == 1);
		assertTrue(hardEQEdges.get(new Edge(2, 4)) == 1);
		assertTrue(hardEQEdges.keySet().size() == 3);
		assertTrue(hardNEQEdges.get(new Edge(1, 2)) == 1);
		assertTrue(hardNEQEdges.get(new Edge(3, 4)) == 1);
		assertTrue(hardNEQEdges.keySet().size() == 2);
	}

	/**
	 * Hard eq rule partition table is created and check for given two different partitions, related hard eq edges are retrieved correctly
	 */
	@SuppressWarnings("unchecked")
	public void testGetHardPosEdgesIncrementally() {
		prepare();
		String query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(5, 5),(5, 6),(7, 7),(5, 8),(7, 9),(12, 12)";
		sql.executeUpdateQuery(query);
		ArrayList<Object> list = eqVSetup.getMappingFromEntityTable(viewId1);
		LinkedHashMap<Integer, Integer> backMapping = (LinkedHashMap<Integer, Integer>) list.get(1);
		
		query = "INSERT INTO " + EqViewSchema.getHardEQRulePartTableName(viewId1) + " VALUES(5, 5, 6), (5, 6, 8), (7, 7, 9);";
		sql.executeUpdateQuery(query);
		
		LinkedHashMap<Edge, Integer> hardEQEdges = new LinkedHashMap<Edge, Integer>();
		eqVSetup.getHardPosEdgesIncrementally(viewId1, 7, hardEQEdges, backMapping);
		assertTrue(hardEQEdges.get(new Edge(2, 4)) == 1);
		assertTrue(hardEQEdges.keySet().size() == 1);
		eqVSetup.getHardPosEdgesIncrementally(viewId1, 5, hardEQEdges, backMapping);
		assertTrue(hardEQEdges.get(new Edge(2, 4)) == 1);
		assertTrue(hardEQEdges.get(new Edge(0, 1)) == 1);
		assertTrue(hardEQEdges.get(new Edge(1, 3)) == 1);
		assertTrue(hardEQEdges.keySet().size() == 3);
	}

	/**
	 * Hard neq rule partition table is created and check for given two different partitions, related hard neq edges are retrieved correctly
	 */
	@SuppressWarnings("unchecked")
	public void testGetHardNegEdgesIncrementally() {
		prepare();
		String query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(5, 5),(5, 6),(7, 7),(5, 8),(7, 9),(12, 12)";
		sql.executeUpdateQuery(query);
		ArrayList<Object> list = eqVSetup.getMappingFromEntityTable(viewId1);
		LinkedHashMap<Integer, Integer> backMapping = (LinkedHashMap<Integer, Integer>) list.get(1);
		
		query = "INSERT INTO " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + " VALUES(5, 6, 7, 7), (5, 8, 7, 9), (7, 9, 12, 12);";
		sql.executeUpdateQuery(query);
		
		LinkedHashMap<Edge, Integer> hardNEQEdges = new LinkedHashMap<Edge, Integer>();
		eqVSetup.getHardNegEdgesIncrementally(viewId1, 5, 7, hardNEQEdges, backMapping);
		assertTrue(hardNEQEdges.get(new Edge(1, 2)) == 1);
		assertTrue(hardNEQEdges.get(new Edge(3, 4)) == 1);
		assertTrue(hardNEQEdges.size() == 2);
		eqVSetup.getHardNegEdgesIncrementally(viewId1, 7, 12, hardNEQEdges, backMapping);
		assertTrue(hardNEQEdges.get(new Edge(1, 2)) == 1);
		assertTrue(hardNEQEdges.get(new Edge(3, 4)) == 1);
		assertTrue(hardNEQEdges.get(new Edge(4, 5)) == 1);
		assertTrue(hardNEQEdges.size() == 3);
	}

	/**
	 * Soft rule partition table is created and check for given two different partitions, related soft edges are retrieved correctly
	 */
	@SuppressWarnings("unchecked")
	public void testGetSoftEdgesIncrementally() {
		prepare();
		String query = "INSERT INTO " + EqViewSchema.getEPTableName(viewId1) + " VALUES(5, 5),(5, 6),(7, 7),(5, 8),(7, 9),(12, 12);";
		query += "INSERT INTO " + EqViewSchema.getEPTableName(viewId2) + " VALUES(5, 5),(5, 6),(7, 7),(5, 8),(7, 9),(12, 12)";
		sql.executeUpdateQuery(query);
		ArrayList<Object> list = eqVSetup.getMappingFromEntityTable(viewId1);
		LinkedHashMap<Integer, Integer> backMapping = (LinkedHashMap<Integer, Integer>) list.get(1);
		
		query = "INSERT INTO " + EqViewSchema.getSoftRulePartTableName(viewId1) + " VALUES(5,5,5,6,1.11), (5,5,7,7,2.21), (5,5,5,8,2.22), " +
				"(5,6,5,8,3.32), (5,8,12,12,2.22), (7,9,12,12,2.21);";
		query += "INSERT INTO " + EqViewSchema.getSoftRulePartTableName(viewId2) + " VALUES(5,5,5,6,1.11), (5,5,7,7,2.21), (5,5,5,8,2.22), " +
		"(5,6,5,8,3.32), (5,8,12,12,2.22), (7,9,12,12,2.21);";
		sql.executeUpdateQuery(query);
		
		LinkedHashMap<Edge, Double> softEdges1 = new LinkedHashMap<Edge, Double>();
		LinkedHashMap<Edge, Double> softEdges2 = new LinkedHashMap<Edge, Double>();
		
		eqVSetup.getSoftEdgesIncrementally(viewId1, 5, 5, softEdges1, backMapping, true, 4.43);
		eqVSetup.getSoftEdgesIncrementally(viewId2, 5, 5, softEdges2, backMapping, false, 4.43);
		
		assertTrue(softEdges1.get(new Edge(0, 3)) == 1);
		assertTrue(softEdges1.get(new Edge(1, 3)) == 1);
		assertTrue(softEdges1.keySet().size() == 2);
		assertTrue(softEdges2.get(new Edge(0, 1)) == 1);
		assertTrue(softEdges2.get(new Edge(0, 3)) == 1);
		assertTrue(softEdges2.get(new Edge(1, 3)) == 1);
		assertTrue(softEdges2.keySet().size() == 3);
		
		eqVSetup.getSoftEdgesIncrementally(viewId1, 5, 12, softEdges1, backMapping, true, 4.43);
		eqVSetup.getSoftEdgesIncrementally(viewId2, 5, 12, softEdges2, backMapping, false, 4.43);
		assertTrue(softEdges1.get(new Edge(0, 3)) == 1);
		assertTrue(softEdges1.get(new Edge(1, 3)) == 1);
		assertTrue(softEdges1.get(new Edge(3, 5)) == 1);
		assertTrue(softEdges1.keySet().size() == 3);
		assertTrue(softEdges2.get(new Edge(0, 1)) == 1);
		assertTrue(softEdges2.get(new Edge(0, 3)) == 1);
		assertTrue(softEdges2.get(new Edge(1, 3)) == 1);
		assertTrue(softEdges2.get(new Edge(3, 5)) == 1);
		assertTrue(softEdges2.keySet().size() == 4);
	}

}
