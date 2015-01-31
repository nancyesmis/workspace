package wisc.db.equivalencetest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import wisc.db.equivalence.DBTable;
import wisc.db.equivalence.EqViewSchema;
import wisc.db.equivalence.EqViewSetup;
import wisc.db.equivalence.EqViewSetupFactory;
import wisc.db.equivalence.Graph;
import wisc.db.equivalence.RuleTable;
import wisc.db.sql.DatabaseFactory;
import wisc.db.sql.QueryExecutor;
import junit.framework.TestCase;

/**
 * Tests {@link Graph} class
 * 
 * @author koc
 *
 */
public class GraphTest extends TestCase {
	EqViewSetup eqVSetup;
	QueryExecutor sql;
	String cleanViewDatabaseQuery = "TRUNCATE eq_view_id_table; TRUNCATE eq_view_rules_id_weight_table;";
	Graph g1;
	Graph g2;
	String viewName1 = "view1";
	String viewName2 = "view2";
	int viewId1;
	int viewId2;
	double reorganizeTime1;
	double reorganizeTime2;
	
	DBTable viewT1;
	DBTable entityT1;
	ArrayList<RuleTable> softRules1;
	ArrayList<RuleTable> hardEQRules1;
	ArrayList<RuleTable> hardNEQRules1;
	DBTable viewT2;
	DBTable entityT2;
	ArrayList<RuleTable> softRules2;
	ArrayList<RuleTable> hardEQRules2;
	ArrayList<RuleTable> hardNEQRules2;
	
	public GraphTest(String name) {
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
	 * teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Creates two views and create required trigger and indexes
	 */
	private void prepareViews() {
		prepare();
		Graph graph1 = new Graph(viewId1, entityT1, softRules1, hardEQRules1, hardNEQRules1);
		Graph graph2 = new Graph(viewId2, entityT2, softRules2, hardEQRules2, hardNEQRules2);
		
		graph1.batchCluster();
		graph2.batchCluster();
		
		reorganizeTime1 = graph1.getClusterAndWriteTime();
		double totalSoftRuleWeight1 = graph1.getTotalSoftRuleWeight();
		reorganizeTime2 = graph2.getClusterAndWriteTime();
		double totalSoftRuleWeight2 = graph2.getTotalSoftRuleWeight();
		
		eqVSetup.fillsCatalogTable(viewId1, reorganizeTime1, softRules1.size(), totalSoftRuleWeight1);
		eqVSetup.fillsCatalogTable(viewId2, reorganizeTime2, softRules2.size(), totalSoftRuleWeight2);
		
		eqVSetup.createIndex(viewName1, viewId1);
		eqVSetup.createTriggers(viewId1, "e_t1", softRules1, hardEQRules1, hardNEQRules1);
		eqVSetup.createIndex(viewName2, viewId2);
		eqVSetup.createTriggers(viewId2, "e_t2", softRules2, hardEQRules2, hardNEQRules2);
	}
	
	/**
	 * Create two views
	 * <p>
	 * First one consists of 1 rule table for each soft, hard eq and hard neq. Second view contains 3 soft rule, 2 hard eq and 2 hard neq rule tables
	 * After creating the view in database, checks whether rule, entity, view tables are created or not Also checks view information
	 * is added to the general catalog table
	 * 
	 */
	private void prepare() {
		sql.executeUpdateQuery(cleanViewDatabaseQuery);
		//graph1 tables (only one soft rule)
		String query = "DROP TABLE IF EXISTS e_t1; CREATE TABLE e_t1(id int); INSERT INTO e_t1 VALUES(1),(2),(3),(4),(5);";
		query += "DROP TABLE IF EXISTS sr_t1; CREATE TABLE sr_t1(src int, dst int);INSERT INTO sr_t1 VALUES(1,3),(1,4),(2,5);";
		query += "DROP TABLE IF EXISTS heq_t1; CREATE TABLE heq_t1(src int, dst int); INSERT INTO heq_t1 VALUES(1, 4);";
		query += "DROP TABLE IF EXISTS hneq_t1; CREATE TABLE hneq_t1(src int, dst int); INSERT INTO hneq_t1 VALUES(1, 5);";
		sql.executeUpdateQuery(query);
		softRules1 = new ArrayList<RuleTable>();
		softRules1.add(new RuleTable("sr_t1", 1));
		hardEQRules1 = new ArrayList<RuleTable>();
		hardEQRules1.add(new RuleTable("heq_t1", Double.POSITIVE_INFINITY));
		hardNEQRules1 = new ArrayList<RuleTable>();
		hardNEQRules1.add(new RuleTable("hneq_t1", Double.NEGATIVE_INFINITY));
		entityT1 = new DBTable("e_t1");
		viewT1 = new DBTable(viewName1);
		//graph2 tables (3 soft rules)
		query = "DROP TABLE IF EXISTS e_t2; CREATE TABLE e_t2(id int); INSERT INTO e_t2 VALUES(1), (2), (3), (4), (5), (6), (7), (8), (9), (10);";
		query += "DROP TABLE IF EXISTS sr_t2_1; CREATE TABLE sr_t2_1(src int, dst int); INSERT INTO sr_t2_1 VALUES(1, 2), (1, 3), (5, 6), (6, 7), (9, 10);";
		query += "DROP TABLE IF EXISTS sr_t2_2; CREATE TABLE sr_t2_2(src int, dst int); INSERT INTO sr_t2_2 VALUES(1, 2), (8, 9);";
		query += "DROP TABLE IF EXISTS sr_t2_3; CREATE TABLE sr_t2_3(src int, dst int); INSERT INTO sr_t2_3 VALUES(1, 3), (8, 9), (9, 10);";
		query += "DROP TABLE IF EXISTS heq_t2_1; CREATE TABLE heq_t2_1(src int, dst int); INSERT INTO heq_t2_1 VALUES(1, 3), (1, 4);";
		query += "DROP TABLE IF EXISTS heq_t2_2; CREATE TABLE heq_t2_2(src int, dst int); INSERT INTO heq_t2_2 VALUES(8, 9);";
		query += "DROP TABLE IF EXISTS hneq_t2_1; CREATE TABLE hneq_t2_1(src int, dst int); INSERT INTO hneq_t2_1 VALUES(1, 8);";
		query += "DROP TABLE IF EXISTS hneq_t2_2; CREATE TABLE hneq_t2_2(src int, dst int); INSERT INTO hneq_t2_2 VALUES(3, 5);";
		sql.executeUpdateQuery(query);
		softRules2 = new ArrayList<RuleTable>();
		softRules2.add(new RuleTable("sr_t2_1", 1.1));
		softRules2.add(new RuleTable("sr_t2_2", 2.2));
		softRules2.add(new RuleTable("sr_t2_3", 1.1));
		hardEQRules2 = new ArrayList<RuleTable>();
		hardEQRules2.add(new RuleTable("heq_t2_1", Double.POSITIVE_INFINITY));
		hardEQRules2.add(new RuleTable("heq_t2_2", Double.POSITIVE_INFINITY));
		hardNEQRules2 = new ArrayList<RuleTable>();
		hardNEQRules2.add(new RuleTable("hneq_t2_1", Double.NEGATIVE_INFINITY));
		hardNEQRules2.add(new RuleTable("hneq_t2_2", Double.NEGATIVE_INFINITY));
		entityT2 = new DBTable("e_t2");
		viewT2 = new DBTable(viewName2);
		
		//get view ids
		viewId1 = eqVSetup.getUniqueViewId();
		eqVSetup.createTables(viewId1, viewName1);	
		eqVSetup.insertIntoIdCatalog(viewId1, viewName1, "e_t1");
		eqVSetup.insertSoftTableIntoRulesIdWeightTab(viewId1, softRules1);
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId1, hardEQRules1, true);
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId1, hardNEQRules1, false);
		
		
		viewId2 = eqVSetup.getUniqueViewId();
		eqVSetup.createTables(viewId2, viewName2);	
		eqVSetup.insertIntoIdCatalog(viewId2, viewName2, "e_t2");
		eqVSetup.insertSoftTableIntoRulesIdWeightTab(viewId2, softRules2);
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId2, hardEQRules2, true);
		eqVSetup.insertHardTableIntoRulesIdWeightTab(viewId2, hardNEQRules2, false);
		
		assertTrue(viewId1 == 0);
		assertTrue(viewId2 == 1);
		
		//test create table is ok
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
			query = "select count(*) from pg_class where relname = '" + EqViewSchema.getEPTableName(viewId1) + "'";
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
		
		//test insert into id catalog is OK
		int count1 = 0;
		int count2 = 0;
		
		String query1 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_ID_ALL_CATALOGS_TABLE + " WHERE view_id = " + viewId1 + 
		" AND view_name = '" + viewName1 + "' AND catalog_name = '" + EqViewSchema.getViewCatalogTableName(viewId1) + "';";
		ResultSet set = sql.executeSelectQuery(query1);
		try {
			if(set.next())
				count1 = set.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String query2 = "SELECT COUNT(*) FROM " + EqViewSchema.EQ_VIEW_ID_ALL_CATALOGS_TABLE + " WHERE view_id = " + viewId2 + 
		" AND view_name = '" + viewName2 + "' AND catalog_name = '" + EqViewSchema.getViewCatalogTableName(viewId2) + "';";
		ResultSet set2 = sql.executeSelectQuery(query2);
		
		try {
			if(set2.next())
				count2 = set2.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertTrue(count1 == 1);
		assertTrue(count2 == 1);
	}
	
	/**
	 * Create two graph objects for the given views and checks whether there is any problem. This constructor is for batch cluster
	 */
	@SuppressWarnings("unused")
	public void testGraphIntDBTableArrayListOfRuleTableArrayListOfRuleTableArrayListOfRuleTable() {
		prepare();
		Graph graph1 = new Graph(viewId1, entityT1, softRules1, hardEQRules1, hardNEQRules1);
		Graph graph2 = new Graph(viewId2, entityT2, softRules2, hardEQRules2, hardNEQRules2);
	}

	/**
	 * Create two graph objects for the given views and checks whether there is any problem. This constructor is for update
	 */
	@SuppressWarnings("unused")
	public void testGraphInt() {
		prepareViews();
		Graph graph1 = new Graph(viewId1);
		Graph graph2 = new Graph(viewId2);
	}

	/**
	 * Tests update method of Graph.
	 * <p>
	 * It updates two views with soft edges. Then, it checks
	 * partitions are correct
	 * soft partitions are correct
	 * hard eq and hard neq partitions are correct
	 * some clusters are correct. Although algorithm is non-deterministic, we can make sure that some clusters have to be correct. For example,
	 * if there is a partition consists of two vertices, then, they have to be cluster, too. Or, if there is hard neq edge, then they cannot
	 * be in same cluster
	 */
	public void testUpdate() {
		prepareViews();
		Graph graph1 = new Graph(viewId1);
		Graph graph2 = new Graph(viewId2);
		
		graph1.update("sr_t1", "+", 1, 3, 1);
		graph2.update("sr_t2_3", "+", 5, 6, 1.1);
		
		//check partitions
		//view1
		assertTrue(eqVSetup.getPidForNode(viewId1, 1) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId1, 2) == 2);
		assertTrue(eqVSetup.getPidForNode(viewId1, 3) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId1, 4) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId1, 5) == 2);
		//view2
		assertTrue(eqVSetup.getPidForNode(viewId2, 1) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId2, 2) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId2, 3) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId2, 4) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId2, 5) == 5);
		assertTrue(eqVSetup.getPidForNode(viewId2, 6) == 6);
		assertTrue(eqVSetup.getPidForNode(viewId2, 7) == 7);
		assertTrue(eqVSetup.getPidForNode(viewId2, 8) == 8);
		assertTrue(eqVSetup.getPidForNode(viewId2, 9) == 8);
		assertTrue(eqVSetup.getPidForNode(viewId2, 10) == 8);
		
		//check soft partitions
		//view1
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 1, 1, 1, 3) == 1);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 1, 1, 1, 4) == 1);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 2, 2, 2, 5) == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getSoftRulePartTableName(viewId1) + ";") == 4);
		//view2
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 1, 1, 1, 2) == (2.2 + 1.1));
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 1, 1, 1, 3) == 2.2);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 5, 6) == 2.2);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 6, 6, 7, 7) == 1.1);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 8, 8, 8, 9) == (2.2 + 1.1));
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 8, 9, 8, 10) == 2.2);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getSoftRulePartTableName(viewId2) + ";") == 6);
		
		//check hard eq partitions
		//view1
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId1) + " WHERE pid = 1 AND node1 = 1 AND node2 = 4;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId1) + ";") == 1);
		//view2
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE pid = 1 AND node1 = 1 AND node2 = 3;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE pid = 1 AND node1 = 1 AND node2 = 4;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE pid = 8 AND node1 = 8 AND node2 = 9;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + ";") == 3);
		
		//check hard neq partitions
		//view1
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + " WHERE pid1 = 1 AND node1 = 1 AND pid2 = 2 AND node2 = 5;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + ";") == 1);
		//view2
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + " WHERE pid1 = 1 AND node1 = 1 AND pid2 = 8 AND node2 = 8;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + " WHERE pid1 = 1 AND node1 = 3 AND pid2 = 5 AND node2 = 5;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + ";") == 2);
		
		//check cluster table
		//view1
		//hard eq edge has to combine clusters (1, 4)
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " v1, " + viewName1 + " v2 WHERE v1.cid = v2.cid AND v1.node = 1 AND v2.node = 4 AND v1.node < v2.node;") == 1);
		//since partition of 2 consists of 2 and 5, they have to be together
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " v1, " + viewName1 + " v2 WHERE v1.cid = v2.cid AND v1.node = 2 AND v2.node = 5 AND v1.node < v2.node;") == 1);
		//since there is hard neq between 1 and 5, they cannot be in same cluster
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " v1, " + viewName1 + " v2 WHERE v1.cid <> v2.cid AND v1.node = 1 AND v2.node = 5 AND v1.node < v2.node;") == 1);
		//since 1 and 4 has to be in same cluster, their representative becomes 1 and 3 might join them or stay alone
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " WHERE cid = 1 AND node = 1;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " WHERE cid = 1 AND node = 4;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " WHERE (cid = 1 OR cid = 3) AND node = 3;") == 1);
		//view2
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " v1, " + viewName2 + " v2 WHERE v1.cid = v2.cid AND v1.node = 1 AND v2.node = 3 AND v1.node < v2.node;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " v1, " + viewName2 + " v2 WHERE v1.cid = v2.cid AND v1.node = 1 AND v2.node = 4 AND v1.node < v2.node;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " v1, " + viewName2 + " v2 WHERE v1.cid = v2.cid AND v1.node = 8 AND v2.node = 9 AND v1.node < v2.node;") == 1);
		//new edge
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " v1, " + viewName2 + " v2 WHERE v1.cid = v2.cid AND v1.node = 5 AND v2.node = 6 AND v1.node < v2.node;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " v1, " + viewName2 + " v2 WHERE v1.cid <> v2.cid AND v1.node = 1 AND v2.node = 8 AND v1.node < v2.node;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " v1, " + viewName2 + " v2 WHERE v1.cid <> v2.cid AND v1.node = 3 AND v2.node = 5 AND v1.node < v2.node;") == 1);
		//since 1, 3 and 4 will be in same cluster and their representative will be 1, 2 might join them or stay alone
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " WHERE cid = 1 AND node = 1;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " WHERE cid = 1 AND node = 3;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " WHERE cid = 1 AND node = 4;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " WHERE (cid = 2 OR cid = 1) AND node = 2;") == 1);
	}

	/**
	 * checks whether reorganize time are written correctly for two views
	 */
	public void testGetClusterAndWriteTime() {
		prepareViews();
		
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getViewCatalogTableName(viewId1) + " WHERE reorganize_time = " + reorganizeTime1 + ";") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getViewCatalogTableName(viewId2) + " WHERE reorganize_time = " + reorganizeTime2 + ";") == 1);
	}

	/**
	 * checks total soft rule weights of graphs are correctly retrieved by getTotalSoftRuleWeight method
	 */
	public void testGetTotalSoftRuleWeight() {
		prepare();
		Graph graph1 = new Graph(viewId1, entityT1, softRules1, hardEQRules1, hardNEQRules1);
		Graph graph2 = new Graph(viewId2, entityT2, softRules2, hardEQRules2, hardNEQRules2);
		assertTrue(graph1.getTotalSoftRuleWeight() == 1);
		assertTrue(graph2.getTotalSoftRuleWeight() == 4.4);
	}

	/**
	 * Creates two Graph objects and makes batch clustering.
	 * <p>
	 * It checks
	 * partitions are correct
	 * soft partitions are correct
	 * hard eq and hard neq partitions are correct
	 * some clusters are correct. Although algorithm is non-deterministic, we can make sure that some clusters have to be correct. For example,
	 * if there is a partition consists of two vertices, then, they have to be cluster, too. Or, if there is hard neq edge, then they cannot
	 * be in same cluster
	 */
	public void testBatchCluster() {
		prepare();
		Graph graph1 = new Graph(viewId1, entityT1, softRules1, hardEQRules1, hardNEQRules1);
		Graph graph2 = new Graph(viewId2, entityT2, softRules2, hardEQRules2, hardNEQRules2);
		
		graph1.batchCluster();
		graph2.batchCluster();
		
		//check partitions
		//view1
		assertTrue(eqVSetup.getPidForNode(viewId1, 1) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId1, 2) == 2);
		assertTrue(eqVSetup.getPidForNode(viewId1, 3) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId1, 4) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId1, 5) == 2);
		//view2
		assertTrue(eqVSetup.getPidForNode(viewId2, 1) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId2, 2) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId2, 3) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId2, 4) == 1);
		assertTrue(eqVSetup.getPidForNode(viewId2, 5) == 5);
		assertTrue(eqVSetup.getPidForNode(viewId2, 6) == 6);
		assertTrue(eqVSetup.getPidForNode(viewId2, 7) == 7);
		assertTrue(eqVSetup.getPidForNode(viewId2, 8) == 8);
		assertTrue(eqVSetup.getPidForNode(viewId2, 9) == 8);
		assertTrue(eqVSetup.getPidForNode(viewId2, 10) == 8);
		
		//check soft partitions
		//view1
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 1, 1, 1, 3) == 1);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 1, 1, 1, 4) == 1);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId1, 2, 2, 2, 5) == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getSoftRulePartTableName(viewId1) + ";") == 3);
		//view2
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 1, 1, 1, 2) == (2.2 + 1.1));
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 1, 1, 1, 3) == 2.2);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 5, 5, 6, 6) == 1.1);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 6, 6, 7, 7) == 1.1);
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 8, 8, 8, 9) == (2.2 + 1.1));
		assertTrue(eqVSetup.getInitialSoftEdgeWeight(viewId2, 8, 9, 8, 10) == 2.2);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getSoftRulePartTableName(viewId2) + ";") == 6);
		
		//check hard eq partitions
		//view1
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId1) + " WHERE pid = 1 AND node1 = 1 AND node2 = 4;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId1) + ";") == 1);
		//view2
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE pid = 1 AND node1 = 1 AND node2 = 3;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE pid = 1 AND node1 = 1 AND node2 = 4;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + " WHERE pid = 8 AND node1 = 8 AND node2 = 9;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardEQRulePartTableName(viewId2) + ";") == 3);
		
		//check hard neq partitions
		//view1
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + " WHERE pid1 = 1 AND node1 = 1 AND pid2 = 2 AND node2 = 5;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId1) + ";") == 1);
		//view2
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + " WHERE pid1 = 1 AND node1 = 1 AND pid2 = 8 AND node2 = 8;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + " WHERE pid1 = 1 AND node1 = 3 AND pid2 = 5 AND node2 = 5;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + EqViewSchema.getHardNEQRulePartTableName(viewId2) + ";") == 2);
		
		//check cluster table
		//view1
		//hard eq edge has to combine clusters (1, 4)
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " v1, " + viewName1 + " v2 WHERE v1.cid = v2.cid AND v1.node = 1 AND v2.node = 4 AND v1.node < v2.node;") == 1);
		//since partition of 2 consists of 2 and 5, they have to be together
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " v1, " + viewName1 + " v2 WHERE v1.cid = v2.cid AND v1.node = 2 AND v2.node = 5 AND v1.node < v2.node;") == 1);
		//since there is hard neq between 1 and 5, they cannot be in same cluster
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " v1, " + viewName1 + " v2 WHERE v1.cid <> v2.cid AND v1.node = 1 AND v2.node = 5 AND v1.node < v2.node;") == 1);
		//since 1 and 4 has to be in same cluster, their representative becomes 1 and 3 might join them or stay alone
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " WHERE cid = 1 AND node = 1;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " WHERE cid = 1 AND node = 4;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName1 + " WHERE (cid = 1 OR cid = 3) AND node = 3;") == 1);
		//view2
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " v1, " + viewName2 + " v2 WHERE v1.cid = v2.cid AND v1.node = 1 AND v2.node = 3 AND v1.node < v2.node;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " v1, " + viewName2 + " v2 WHERE v1.cid = v2.cid AND v1.node = 1 AND v2.node = 4 AND v1.node < v2.node;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " v1, " + viewName2 + " v2 WHERE v1.cid = v2.cid AND v1.node = 8 AND v2.node = 9 AND v1.node < v2.node;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " v1, " + viewName2 + " v2 WHERE v1.cid <> v2.cid AND v1.node = 1 AND v2.node = 8 AND v1.node < v2.node;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " v1, " + viewName2 + " v2 WHERE v1.cid <> v2.cid AND v1.node = 3 AND v2.node = 5 AND v1.node < v2.node;") == 1);
		//since 1, 3 and 4 will be in same cluster and their representative will be 1, 2 might join them or stay alone
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " WHERE cid = 1 AND node = 1;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " WHERE cid = 1 AND node = 3;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " WHERE cid = 1 AND node = 4;") == 1);
		assertTrue(getQueryResult("SELECT COUNT(*) FROM " + viewName2 + " WHERE (cid = 2 OR cid = 1) AND node = 2;") == 1);
	}
	
	/**
	 * Auxiliary method to retrieve integer result of the query
	 * 
	 * @param query
	 * @return
	 */
	private int getQueryResult(String query) {
		ResultSet set = sql.executeSelectQuery(query);
		int result = 0;
		try {
			if(set.next())
				result = set.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

}
