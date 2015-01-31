package wisc.db.equivalence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;

/**
 * Graph class represents graph structure and implements clustering algorithm at
 * Dedupalog [Arasu et al.] paper
 * 
 * @author koc
 **/
public class Graph {
	/**
	 * soft edges that will be loaded from database
	 */
	private LinkedHashMap<Edge, Double> softEdges;
	/**
	 * hard eq edges that will be loaded from database
	 */
	private LinkedHashMap<Edge, Integer> hardEdges;
	/**
	 * hard neq edges that will be loaded from database
	 */
	private LinkedHashMap<Edge, Integer> hardNegEdges;
	/**
	 * hard neq edges after permutation
	 */
	private LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> processedHardNegEdges;
	/**
	 * soft edges after permutation
	 */
	private LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>> processedSoftEdges;

	/**
	 * This keeps list of database ids
	 */
	ArrayList<Integer> mapping;
	/**
	 * This maps id of vertex in database to consecutive ids
	 */
	private LinkedHashMap<Integer, Integer> backMapping;

	/**
	 * This keeps mapping of root id r to the set of elements in the set whose
	 * root is r
	 */
	private LinkedHashMap<Integer, ArrayList<Integer>> components;
	/**
	 * This maps root id r to to hard eq edges of which elements contained in
	 * the set whose root is r
	 */
	private LinkedHashMap<Integer, ArrayList<Edge>> hardEdgesComponents;
	/**
	 * This maps root id r to to soft edges of which elements contained in the
	 * set whose root is r
	 */
	private LinkedHashMap<Integer, ArrayList<Edge>> softEdgesComponents;
	/**
	 * This is used to keep on memory partitions of the given partition
	 */
	private ArrayList<Integer> onMemoryPartitionsOfGivenPartition;

	/**
	 * this is id of graph to cluster (batch clustering). used to retrieve graph
	 * edge queries from XML file
	 */
	private int graphId;
	/**
	 * # of vertices in the graph
	 */
	private int V;
	/**
	 * EqViewSetup object for SQL-related methods
	 */
	private EqViewSetup eqViewSetup;

	/**
	 * disjoint set structures for clusters and partitions
	 */
	private DisjointSets set;
	/**
	 * disjoint set structures being used during partition step
	 */
	private DisjointSets querySet;
	/**
	 * on-memory partitions used for Hazy algorithm
	 */
	private DisjointSets onMemoryPartitions;

	/**
	 * this is for efficiency purposes. If we know that, there is no multiple
	 * soft edges, then we also know that we dont need to take edge counts into
	 * consideration
	 */
	private boolean multipleSoftEdges;
	/**
	 * this is used for shuffling
	 */
	private Vertex[] vertices;
	/**
	 * this is shuffled vertex id to original vertex id shuffling
	 */
	private int[] reverseMap;
	/**
	 * Random number generator
	 */
	private Random rand;
	/**
	 * clustering and writing results to the database time
	 */
	private long clusteringAndWriteTime;
	/**
	 * update algorithm
	 */
	private String updateAlg;
	/**
	 * accumulated cost
	 */
	private double acc;
	/**
	 * re-organizing disk partitions time
	 */
	private double reorganizeTime;
	/**
	 * epsilon of update algorithm
	 */
	private double tao;
	/**
	 * elapsed time for update
	 */
	private float elapsedTime;
	/**
	 * entity clusters table name
	 */
	public String enC;
	/**
	 * total soft rule weights
	 */
	private double totalSoftRuleWeight;

	/**
	 * Constructor used during view creation
	 * <p>
	 * It retrieves all rule edges from the database and create Graph object
	 * 
	 * @param graphId
	 * @param entityTableName
	 * @param softRuleTableNames
	 * @param hardEQRuleTableNames
	 * @param hardNEQRuleTableNames
	 */
	public Graph(int graphId, DBTable entityTableName,
			ArrayList<RuleTable> softRuleTableNames,
			ArrayList<RuleTable> hardEQRuleTableNames,
			ArrayList<RuleTable> hardNEQRuleTableNames) {
		initializeVariables(graphId);
		this.multipleSoftEdges = softRuleTableNames.size() != 1;
		setMapping(entityTableName);
		initializeSetsArrays();
		// load edges
		loadSoftEdges(softRuleTableNames);
		loadHardEdges(hardEQRuleTableNames);
		loadHardNegEdges(hardNEQRuleTableNames);
		setViewName();
	}

	/**
	 * Constructor used during incremental update
	 * <p>
	 * It retrieves all rule edges from the database
	 * 
	 * @param graphId
	 */
	public Graph(int graphId) {
		initializeVariables(graphId);
		setViewName();
		onMemoryPartitions = null; // initially it is null
		// set onMemoryPartitions
		setOnMemoryPartitions();
	}

	/**
	 * Updates clusters based on recluster approach and writes it back to the
	 * cluster table
	 * 
	 * @param updateType
	 *            type of update (+: soft, =:hard eq, =/: hard neq)
	 * @param src
	 *            id of source vertex
	 * @param dst
	 *            id of destination vertex
	 */
	private void reclusterUpdate(String updateType, int src, int dst) {
		// cluster method is called
		this.cluster(true);

		// re-create cluster table
		this.writeClusters(enC, false);
	}

	/**
	 * Updates view when new edge arrives to the rule tables
	 * <p>
	 * It first retrieves catalog attribute values(update alg, acc, reorganize
	 * time, tao, total soft rule weight) Then, initial weight of edge is
	 * retrieved and new weight is calculated with current weight of the rule
	 * table that edge comes from After that, hazy or rescan algorithm is used
	 * to update view
	 * 
	 * @param updatedTable
	 *            table triggered for update
	 * @param updateType
	 *            soft, hard eq or hard neq
	 * @param src
	 *            src of edge
	 * @param dst
	 *            dst of edge
	 * @param updateTableWeight
	 *            table triggered weight
	 */
	public void update(String updatedTable, String updateType, int src,
			int dst, double updateTableWeight) {
		CatalogAttributeValues values = eqViewSetup.getCatalogValues(graphId);
		this.updateAlg = values.getUpdateAlg();
		this.acc = values.getAcc();
		this.reorganizeTime = values.getReorganizeTime();
		this.tao = values.getTao();
		this.totalSoftRuleWeight = values.getTotalSoftRw();
		this.multipleSoftEdges = values.getNumOfSoftRule() != 1;

		// call hazy
		if (updateAlg.equals(EqViewConstants.HAZY_ALG)) {
			// is there no op?
			int pid1 = eqViewSetup.getPidForNode(graphId, src);
			int pid2 = eqViewSetup.getPidForNode(graphId, dst);
			double initialWeight = 1; // if there is only one rule, then weight
			// of that edge is 1, if exists
			if (updateType.equals(EqViewConstants.SOFT_EDGE_UPDATE)
					&& this.getMultipleSoftEdges())
				initialWeight = eqViewSetup.getInitialSoftEdgeWeight(graphId,
						pid1, src, pid2, dst);

			double newWeight = initialWeight + updateTableWeight;
			if (!(updateType.equals(EqViewConstants.HARD_NEQ_EDGE_UPDATE) && onMemoryPartitions
					.find(pid1) != onMemoryPartitions.find(pid2))) {
				// load updateAlg, acc, re-organize time and tao
				boolean partitionOfEdgeChanged = prepareForHazyUpdate(
						updateType, src, dst, pid1, pid2, newWeight);
				hazyUpdate(updateType, src, dst, pid1, pid2);

				if (!partitionOfEdgeChanged)
					insertUpdateEdge(updateType, src, dst, pid1, pid2,
							newWeight);
				else
					insertUpdateEdge(updateType, src, dst,
							Math.min(pid1, pid2), Math.min(pid1, pid2),
							newWeight);
			}
			// else noop
		}
		// call rescan
		else {
			double initialWeight = 1; // if there is only one rule, then weight
			// of that edge is 1, if exists
			if (updateType.equals(EqViewConstants.SOFT_EDGE_UPDATE)
					&& this.getMultipleSoftEdges())
				initialWeight = eqViewSetup.getInitialSoftEdgeWeight(graphId,
						src, dst);
			double newWeight = initialWeight + updateTableWeight;
			prepareForReclusterUpdate(updateType, src, dst, newWeight);
			reclusterUpdate(updateType, src, dst);
			insertUpdateEdge(updateType, src, dst, -1, -1, newWeight);
		}
	}

	/**
	 * Inserts the update edge with new weight to the partition table
	 * <p>
	 * If algorithm is recluster, then pid1 and pid2 are inserted as -1
	 * 
	 * @param updateType
	 *            soft, hard eq or hard neq
	 * @param src
	 *            src of edge
	 * @param dst
	 *            dst of edge
	 * @param pid1
	 *            pid of src of edge
	 * @param pid2
	 *            pid of dst of edge
	 * @param newWeight
	 *            new weight
	 */
	private void insertUpdateEdge(String updateType, int src, int dst,
			int pid1, int pid2, double newWeight) {
		// add new edge to the related edge partition table
		if (updateType.equals(EqViewConstants.HARD_EQ_EDGE_UPDATE)) {
			int minpid = pid1;
			if (pid2 < pid1)
				minpid = pid2;
			eqViewSetup.insertHardEQUpdateEdge(graphId, minpid, src, dst);
		} else if (updateType.equals(EqViewConstants.HARD_NEQ_EDGE_UPDATE))
			eqViewSetup.insertHardNEQUpdateEdge(graphId, pid1, pid2, src, dst);
		else if (updateType.equals(EqViewConstants.SOFT_EDGE_UPDATE)) {
			if (!this.getMultipleSoftEdges())
				eqViewSetup.insertSoftUpdateEdge(graphId, pid1, pid2, src, dst,
						1.0);
			else {
				int numUpdatedTuple = eqViewSetup.updateWeightOfSoftUpdateEdge(
						graphId, pid1, pid2, src, dst, newWeight);
				if (numUpdatedTuple == 0)
					eqViewSetup.insertSoftUpdateEdge(graphId, pid1, pid2, src,
							dst, newWeight);
			}
		}
	}

	/**
	 * initializes on-memory partitions by using max pid of entity partitions
	 * table
	 */
	private void setOnMemoryPartitions() {
		if (onMemoryPartitions == null) {
			int maxPid = eqViewSetup.getMaxPid(graphId);
			onMemoryPartitions = new DisjointSets(maxPid + 1);
		}
	}

	/**
	 * Prepare for recluster update
	 * <p>
	 * soft, hard and hard neq rules are retrieved from the database
	 * 
	 * @param updateType
	 *            soft, hard eq or hard neq
	 * @param src
	 *            src of the edge
	 * @param dst
	 *            dst of the edge
	 * @param newWeight
	 *            new weight
	 */
	private void prepareForReclusterUpdate(String updateType, int src, int dst,
			double newWeight) {
		initializeVariables(graphId); // since this is update, graph id is not
		// important
		// queries
		setMappingFromEntityTable();
		initializeSetsArrays();

		// we retrieve soft, hard eq and hard neq edges
		softEdges = eqViewSetup.generateSoftEdges(graphId, backMapping, this
				.getMultipleSoftEdges(), totalSoftRuleWeight);
		hardEdges = eqViewSetup.generateHardPosNegEdges(graphId, backMapping,
				true);
		hardNegEdges = eqViewSetup.generateHardPosNegEdges(graphId,
				backMapping, false);

		boolean isSoftEdgeQualified = this.getIsSoftEdgeQualified(newWeight);
		Edge edge = new Edge(backMapping.get(src), backMapping.get(dst));
		// according to the edge label, we put it to the edges list
		if (updateType.equals(EqViewConstants.HARD_EQ_EDGE_UPDATE)) {
			if (!hardEdges.containsKey(edge))
				hardEdges.put(edge, new Integer(1));
		}
		// if that
		else if (updateType.equals(EqViewConstants.SOFT_EDGE_UPDATE)) {
			// isSoftEdgeQualified means that its weight is >=
			// EDGE_VOTE_THRESHOLD
			if (isSoftEdgeQualified && !softEdges.containsKey(edge))
				softEdges.put(edge, new Double(1));
		} else {
			if (!hardNegEdges.containsKey(edge))
				hardNegEdges.put(edge, new Integer(1));
		}
	}

	/**
	 * Returns whether soft edge qualifies as positive edge or not. If edge is
	 * hard eq or hard neq, then it is already positive or negative, for sure
	 * 
	 * @param edgeWeight
	 *            weight of the edge
	 * @return whether soft edge qualifies as positive edge or not
	 */
	private boolean isSoftEdgeMoreThanThreshold(double edgeWeight) {
		return (edgeWeight / totalSoftRuleWeight) >= GraphConstants.EDGE_VOTE_THRESHOLD;
	}

	/**
	 * Returns whether soft edge qualifies as positive edge or not.
	 * <p>
	 * It qualifies when there is only one rule or, there is more than one rule
	 * and soft edge weight is more than threshold
	 * 
	 * @param softRuleWeight
	 *            soft rule weight
	 * @return whether soft edge qualifies as positive edge or not
	 */
	private boolean getIsSoftEdgeQualified(double softRuleWeight) {
		return !this.multipleSoftEdges
				|| (this.multipleSoftEdges && this
						.isSoftEdgeMoreThanThreshold(softRuleWeight));
	}

	/**
	 * Prepares for hazy update
	 * <p>
	 * Initially, if acc > reorganize time, then partitions on disk are updated.
	 * It loads soft, hard eq and hard neq rules from the database and updates
	 * catalog table with acc, reorganize time
	 * 
	 * @param updateType
	 *            soft, hard eq or hard neq
	 * @param src
	 *            src of the edge
	 * @param dst
	 *            dst of the edge
	 * @param pid1
	 *            pid of src of the edge
	 * @param pid2
	 *            pid of dst of the edge
	 * @param newWeight
	 *            new weight
	 * @return whether partition of edges change
	 */
	private boolean prepareForHazyUpdate(String updateType, int src, int dst,
			int pid1, int pid2, double newWeight) {
		boolean merged = false;
		boolean forced = false;
		// update lookup
		// if new edge is hard eq or soft and they combine vertices in different
		// partitions, then partitions are combined
		boolean isSoftEdgeQualified = this.getIsSoftEdgeQualified(newWeight);
		if ((updateType.equals(EqViewConstants.HARD_EQ_EDGE_UPDATE) || (updateType
				.equals(EqViewConstants.SOFT_EDGE_UPDATE) && isSoftEdgeQualified))
				&& (pid1 != pid2)) {
			merged = true;
			onMemoryPartitions.unionElementsByValue(Math.min(pid1, pid2), Math
					.max(pid1, pid2));
		}

		// if accumulated cost is less than reorganize time and memory limit is
		// not exceeded, we just update, else reorganize disk partitions
		if (acc < reorganizeTime) {
			// if new edge is hard eq or soft and partitions merged, then we
			// need to look at minimum of them, and use it to get its other
			// partitions
			if ((updateType.equals(EqViewConstants.HARD_EQ_EDGE_UPDATE) || updateType
					.equals(EqViewConstants.SOFT_EDGE_UPDATE))
					&& (pid1 != pid2)) {
				int min = Math.min(pid1, pid2);
				onMemoryPartitionsOfGivenPartition = onMemoryPartitions
						.getCluster(min);
			} else
				onMemoryPartitionsOfGivenPartition = onMemoryPartitions
						.getCluster(pid1);

			// time for retrieve
			long st = System.currentTimeMillis();

			// we get mapping here
			for (int i = 0; i < onMemoryPartitionsOfGivenPartition.size(); i++) {
				eqViewSetup.getMappingIncrementally(graphId,
						onMemoryPartitionsOfGivenPartition.get(i), mapping,
						backMapping);
			}

			// for each pair of partitions, we retrieve soft, hard eq and hard
			// neq edges
			for (int i = 0; i < onMemoryPartitionsOfGivenPartition.size(); i++) {
				for (int j = 0; j < onMemoryPartitionsOfGivenPartition.size(); j++) {
					eqViewSetup.getSoftEdgesIncrementally(graphId,
							onMemoryPartitionsOfGivenPartition.get(i),
							onMemoryPartitionsOfGivenPartition.get(j),
							softEdges, backMapping,
							this.getMultipleSoftEdges(), totalSoftRuleWeight);
					eqViewSetup.getHardNegEdgesIncrementally(graphId,
							onMemoryPartitionsOfGivenPartition.get(i),
							onMemoryPartitionsOfGivenPartition.get(j),
							hardNegEdges, backMapping);
					if (i == j)
						eqViewSetup.getHardPosEdgesIncrementally(graphId,
								onMemoryPartitionsOfGivenPartition.get(i),
								hardEdges, backMapping);
				}

			}
			elapsedTime = System.currentTimeMillis() - st;
			// accumulator is updated
			acc = (acc * (1 - tao) + (float) elapsedTime);
		} else {
			long t1 = System.currentTimeMillis();
			int pid = onMemoryPartitions.find(Math.min(pid1, pid2));
			// reorganize disk
			forceToDisk();
			forced = true;
			// load edges
			eqViewSetup.getMappingIncrementally(graphId, pid, mapping,
					backMapping);
			eqViewSetup.getSoftEdgesIncrementally(graphId, pid, pid, softEdges,
					backMapping, this.getMultipleSoftEdges(),
					totalSoftRuleWeight);
			eqViewSetup.getHardNegEdgesIncrementally(graphId, pid, pid,
					hardNegEdges, backMapping);
			eqViewSetup.getHardPosEdgesIncrementally(graphId, pid, hardEdges,
					backMapping);
			// /numQuery = numQuery + 4;
			reorganizeTime = System.currentTimeMillis() - t1;
			acc = 0;
		}

		// write acc, write time to the catalog table
		eqViewSetup.updateCatalogTable(graphId, acc, reorganizeTime);

		initializeSetsArrays();

		Edge edge = new Edge(backMapping.get(src), backMapping.get(dst));
		// according to the edge label, we put it to the edges list
		if (updateType.equals(EqViewConstants.HARD_EQ_EDGE_UPDATE)) {
			if (!hardEdges.containsKey(edge))
				hardEdges.put(edge, new Integer(1));
		}
		// if that
		else if (updateType.equals(EqViewConstants.SOFT_EDGE_UPDATE)) {
			// isSoftEdgeQualified means that its weight is >=
			// EDGE_VOTE_THRESHOLD
			if (isSoftEdgeQualified && !softEdges.containsKey(edge))
				softEdges.put(edge, new Double(1));
		} else {
			if (!hardNegEdges.containsKey(edge))
				hardNegEdges.put(edge, new Integer(1));
		}

		return merged && forced;
	}

	/**
	 * Updates clusters based on hazy approach and updates cluster table
	 * 
	 * @param updateType
	 *            type of update (+: soft, =:hard eq, =/: hard neq)
	 * @param src
	 *            id of source vertex
	 * @param dst
	 *            id of destination vertex
	 * @param pid1
	 *            partition id of src
	 * @param pid2
	 *            partition id of dst
	 * @throws Exception
	 */
	private void hazyUpdate(String updateType, int src, int dst, int pid1,
			int pid2) {
		// we get edge after mapped from backMapping
		this.cluster(true);
		updateClusterTable();

		if (((updateType.equals(EqViewConstants.SOFT_EDGE_UPDATE) || updateType
				.equals(EqViewConstants.HARD_EQ_EDGE_UPDATE)) && pid1 != pid2)
				|| (updateType.equals(EqViewConstants.HARD_NEQ_EDGE_UPDATE) && pid1 == pid2)) {
			// then partitions might be changed
		}

		// if(pid1 != pid2 and update type is hard or soft)
		// then update partitions

		// if edge is hard not eq, then might need to re-partition
		// even if they become in different partitions, we ignore them.
		/*
		 * initializeClusterQuery(); if(components.size() < 2) { //no update on
		 * pids this.clusterUpdate(); updateClusterTable(); } else { int compId1
		 * = 0; int compId2 = 0;
		 * 
		 * Iterator<Integer> iterator = components.keySet().iterator(); compId1
		 * = iterator.next().intValue(); ArrayList<Integer> comp1 =
		 * components.get(compId1); compId2 = iterator.next().intValue();
		 * ArrayList<Integer> comp2 = components.get(compId2);
		 * 
		 * lookup.increaseSize(); int newPid = lookup.array.length;
		 * if(comp1.size() < comp2.size()) for (int i = 0; i < comp1.size();
		 * i++) genret.executeUpdateQuery("UPDATE " + enP + " SET pid = " +
		 * newPid + " WHERE node = " + mapping.get(comp1.get(i))); else for (int
		 * i = 0; i < comp2.size(); i++) genret.executeUpdateQuery("UPDATE " +
		 * enP + " SET pid = " + newPid + " WHERE node = " +
		 * mapping.get(comp2.get(i)));
		 * 
		 * this.clusterComponent(compId1); updateClusterTable();
		 * 
		 * this.clusterComponent(compId2); updateClusterTable(); }
		 */
	}

	/**
	 * Reorganizes disk partitions
	 * <p>
	 * Updates partitions of entity partitions table, soft, hard eq and hard neq
	 * rule partitions table
	 * 
	 */
	public void forceToDisk() {
		// prepare partition table
		eqViewSetup.createTempPartitionsTable(graphId);
		File tmp = new File("tmpFile");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(tmp));
			int lkupNumElements = onMemoryPartitions.getNumElements();

			for (int i = 0; i < lkupNumElements; i++) {
				int cls = onMemoryPartitions.find(i);
				writer.write(cls + EqViewSchema.DELIMITER_FOR_COPYING + i
						+ "\n");
			}
			writer.close();
			eqViewSetup.copyFromFileToTempPartTable(graphId, tmp
					.getAbsolutePath());
			tmp.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		eqViewSetup.forceEnPTableUpdate(graphId);
		eqViewSetup.forceSoftRulePartTableUpdate(graphId);
		eqViewSetup.forceHardNEQRulePartTableUpdate(graphId);
		eqViewSetup.forceHardEQRulePartTableUpdate(graphId);

		onMemoryPartitions.clear();
	}

	/**
	 * Updates view
	 */
	private void updateClusterTable() {
		String tmpClstr = EqViewSchema.getTempClustersTableName(graphId);
		this.writeClusters(tmpClstr, true);
		eqViewSetup.updateEntityClustersTable(enC, tmpClstr);
	}

	/**
	 * Initializes variables as auxiliary method for the constructors
	 * 
	 * @param graphId
	 *            id of the view
	 */
	private void initializeVariables(int graphId) {
		clusteringAndWriteTime = 0;
		this.graphId = graphId;
		this.eqViewSetup = EqViewSetupFactory.getEqViewSetup();
		rand = new Random();
		softEdges = new LinkedHashMap<Edge, Double>();
		hardEdges = new LinkedHashMap<Edge, Integer>();
		hardNegEdges = new LinkedHashMap<Edge, Integer>();
		processedHardNegEdges = new LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>>();
		processedSoftEdges = new LinkedHashMap<Integer, LinkedHashMap<Integer, Integer>>();
		hardEdgesComponents = new LinkedHashMap<Integer, ArrayList<Edge>>();
		softEdgesComponents = new LinkedHashMap<Integer, ArrayList<Edge>>();
		mapping = new ArrayList<Integer>();
		backMapping = new LinkedHashMap<Integer, Integer>();
	}

	/**
	 * Returns clustering and writing results to the database time.
	 * <p>
	 * During the create view, this is used as roughly reorganize time
	 * 
	 * @return cluster and write time
	 */
	public long getClusterAndWriteTime() {
		return clusteringAndWriteTime;
	}

	/**
	 * Returns summation of soft rule weights
	 * 
	 * @return total soft rule weights
	 */
	public double getTotalSoftRuleWeight() {
		return totalSoftRuleWeight;
	}

	/**
	 * Initializes sets and arrays as auxiliary method for the constructors
	 */
	private void initializeSetsArrays() {
		// adjust V
		V = mapping.size();
		vertices = new Vertex[V];
		reverseMap = new int[V];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = new Vertex(i);
			reverseMap[i] = i;
		}
		set = new DisjointSets(V);
	}

	/**
	 * Retrieves view name from the database with the view id
	 */
	private void setViewName() {
		enC = eqViewSetup.getViewName(graphId);
	}

	/**
	 * Creates mapping between database ids and graph ids from the given entity
	 * table
	 * 
	 * @param entityTable
	 *            entity table
	 */
	private void setMapping(DBTable entityTable) {
		this.setMapping(entityTable.getSelectQuery());
	}

	/**
	 * Creates mapping between database ids and graph ids
	 */
	@SuppressWarnings("unchecked")
	private void setMappingFromEntityTable() {
		ArrayList returnList = eqViewSetup.getMappingFromEntityTable(graphId);
		mapping = (ArrayList<Integer>) returnList.get(0);
		backMapping = (LinkedHashMap<Integer, Integer>) returnList.get(1);
	}

	/**
	 * Creates mapping between database ids and graph ids from the given query
	 * 
	 * @param query
	 *            query for mapping
	 */
	@SuppressWarnings("unchecked")
	private void setMapping(String query) {
		ArrayList returnList = eqViewSetup.getMapping(query);
		mapping = (ArrayList<Integer>) returnList.get(0);
		backMapping = (LinkedHashMap<Integer, Integer>) returnList.get(1);
	}

	/**
	 * Loads soft edges from the database to the memory
	 * 
	 * @param softEdgeSQLQuery
	 *            list of soft rules
	 */
	private void loadSoftEdges(ArrayList<RuleTable> softRuleTableNames) {
		for (int i = 0; i < softRuleTableNames.size(); i++)
			totalSoftRuleWeight += softRuleTableNames.get(i).getWeight();

		if (softRuleTableNames.size() > 0)
			softEdges = eqViewSetup.generateSoftEdges(softRuleTableNames,
					backMapping, this.getMultipleSoftEdges());
	}

	/**
	 * Loads hard eq edges from the database to the memory
	 * 
	 * @param hardEdgeSQLQuery
	 *            list of hard eq rules
	 */
	private void loadHardEdges(ArrayList<RuleTable> hardEdgeSQLQuery) {
		if (hardEdgeSQLQuery.size() > 0)
			hardEdges = eqViewSetup.generateHardPosNegEdges(hardEdgeSQLQuery,
					backMapping);
	}

	/**
	 * Loads hard neq edges from the database to the memory
	 * 
	 * @param hardNegEdgeSQLQuery
	 *            list of hard neq rules
	 */
	private void loadHardNegEdges(ArrayList<RuleTable> hardNegEdgeSQLQuery) {
		if (hardNegEdgeSQLQuery.size() > 0)
			hardNegEdges = eqViewSetup.generateHardPosNegEdges(
					hardNegEdgeSQLQuery, backMapping);
	}

	/**
	 * Creates partitions for batch clustering
	 */
	private void initializeClusterQuery() {
		querySet = new DisjointSets(V);
		processShuffledHardNegEdges();
		processShuffledHardEdges(querySet);
		processSoftEdgesForQuery();
		components = querySet.retrieveClusters();
		hardEdgesComponents.clear();
		softEdgesComponents.clear();

		for (Iterator<Integer> iterator = components.keySet().iterator(); iterator
				.hasNext();) {
			Integer clsId = iterator.next();
			// initilization.. if for a cluster, there is only itself, it means
			// there is no edge for that.
			if (components.get(clsId).size() > 1) {
				ArrayList<Edge> hardList = new ArrayList<Edge>();
				hardEdgesComponents.put(clsId, hardList);
				ArrayList<Edge> softList = new ArrayList<Edge>();
				softEdgesComponents.put(clsId, softList);
			}
		}

		for (Iterator<Edge> iterator = hardEdges.keySet().iterator(); iterator
				.hasNext();) {
			Edge edge = iterator.next();
			hardEdgesComponents
					.get(new Integer(querySet.find(edge.getSrcId()))).add(edge);
		}

		if (this.getMultipleSoftEdges()) {
			for (Iterator<Edge> iterator = softEdges.keySet().iterator(); iterator
					.hasNext();) {
				Edge edge = iterator.next();
				double edgeCount = softEdges.get(edge);

				if ((edgeCount / totalSoftRuleWeight) >= GraphConstants.EDGE_VOTE_THRESHOLD) {
					if (querySet.find(edge.getSrcId()) == querySet.find(edge
							.getDstId()))
						softEdgesComponents.get(
								new Integer(querySet.find(edge.getSrcId())))
								.add(edge);
				}
			}
		} else {
			for (Iterator<Edge> iterator = softEdges.keySet().iterator(); iterator
					.hasNext();) {
				Edge edge = iterator.next();
				if (querySet.find(edge.getSrcId()) == querySet.find(edge
						.getDstId()))
					softEdgesComponents.get(
							new Integer(querySet.find(edge.getSrcId()))).add(
							edge);
			}
		}

	}

	/**
	 * As a result of batch update, partitions, clusters and edges are written
	 * to the database
	 */
	private void writeInitialTables() {
		writePartitions();
		writeClusters(enC, false);
		writeEdgePartitionsTables();
	}

	/**
	 * Partitions are written to the database with bulk load
	 */
	private void writePartitions() {
		File file = new File(graphId + ".comp");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			int querySetNumElements = querySet.getNumElements();
			for (int i = 0; i < querySetNumElements; i++)
				writer.write(mapping.get(querySet.find(i))
						+ EqViewSchema.DELIMITER_FOR_COPYING + mapping.get(i)
						+ "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		eqViewSetup.copyFromFileToEnPTable(graphId, file.getAbsolutePath());
		file.delete();
	}

	/**
	 * Clusters are written to the database with bulk load
	 * 
	 * @param tableName
	 *            table name to write clusters
	 * @param tempTable
	 *            whether table is temp or not
	 */
	private void writeClusters(String tableName, boolean tempTable) {
		if (tempTable)
			eqViewSetup.executeUpdateQuery("DROP TABLE IF EXISTS " + tableName
					+ "; CREATE TABLE " + tableName + "(nd int, clsId int)");
		else
			eqViewSetup.executeUpdateQuery("DROP TABLE IF EXISTS " + tableName
					+ "; CREATE TABLE " + tableName + "(node int, cid int)");
		File file = new File(graphId + ".cls");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			int setNumElements = set.getNumElements();
			LinkedHashMap<Integer, Integer> changedClusterReps = new LinkedHashMap<Integer, Integer>();
			LinkedHashMap<Integer, ArrayList<Integer>> minClsId = new LinkedHashMap<Integer, ArrayList<Integer>>();

			for (int i = 0; i < setNumElements; i++) {
				int cls = set.find(i);
				int mappedId = mapping.get(reverseMap[i]);
				int mappedCls = mapping.get(reverseMap[cls]);
				if (minClsId.containsKey(mappedCls)) {
					minClsId.get(mappedCls).add(mappedId);
					if (mappedId < changedClusterReps.get(mappedCls))
						changedClusterReps.put(mappedCls, mappedId);
				} else {
					ArrayList<Integer> nList = new ArrayList<Integer>();
					nList.add(mappedId);
					minClsId.put(mappedCls, nList);
					changedClusterReps.put(mappedCls, Math.min(mappedCls,
							mappedId));
				}
			}

			for (Iterator<Integer> iterator = minClsId.keySet().iterator(); iterator
					.hasNext();) {
				Integer clsRep = iterator.next();
				int clsMinRep = changedClusterReps.get(clsRep);
				ArrayList<Integer> list = minClsId.get(clsRep);
				for (int i = 0; i < list.size(); i++) {
					writer.write(list.get(i)
							+ EqViewSchema.DELIMITER_FOR_COPYING + clsMinRep
							+ "\n");
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		eqViewSetup.copyFromFileToTable(tableName, file.getAbsolutePath());
		file.delete();
	}

	/**
	 * Edges with partitions are written to the database
	 */
	private void writeEdgePartitionsTables() {
		if (softEdges.keySet().size() > 0) {
			File file = new File(graphId + ".s");
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(file));
				for (Iterator<Edge> iterator = softEdges.keySet().iterator(); iterator
						.hasNext();) {
					Edge edge = iterator.next();
					double weight = softEdges.get(edge);
					int src = edge.getSrcId();
					int dst = edge.getDstId();
					int srcP = querySet.find(src);
					int dstP = querySet.find(dst);
					writer.write(mapping.get(srcP)
							+ EqViewSchema.DELIMITER_FOR_COPYING
							+ mapping.get(src)
							+ EqViewSchema.DELIMITER_FOR_COPYING
							+ mapping.get(dstP)
							+ EqViewSchema.DELIMITER_FOR_COPYING
							+ mapping.get(dst) + ", " + weight + "\n");
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			eqViewSetup.copyFromFileToSoftRulePartTable(graphId, file
					.getAbsolutePath());
			file.delete();
		}

		if (hardEdges.keySet().size() > 0) {
			File file = new File(graphId + ".h");
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				for (Iterator<Edge> iterator = hardEdges.keySet().iterator(); iterator
						.hasNext();) {
					Edge edge = iterator.next();
					int src = edge.getSrcId();
					int dst = edge.getDstId();
					if (querySet.find(src) != querySet.find(dst))
						System.err
								.println("Hard edges are not in same partition");
					int srcdstP = querySet.find(src);
					writer.write(mapping.get(srcdstP)
							+ EqViewSchema.DELIMITER_FOR_COPYING
							+ mapping.get(src) + "," + mapping.get(dst) + "\n");
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			eqViewSetup.copyFromFileToHardEQRulePartTable(graphId, file
					.getAbsolutePath());
			file.delete();
		}

		if (hardNegEdges.keySet().size() > 0) {
			File file = new File(graphId + ".hn");
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				for (Iterator<Edge> iterator = hardNegEdges.keySet().iterator(); iterator
						.hasNext();) {
					Edge edge = iterator.next();
					int src = edge.getSrcId();
					int dst = edge.getDstId();
					int srcP = querySet.find(src);
					int dstP = querySet.find(dst);
					writer.write(mapping.get(srcP)
							+ EqViewSchema.DELIMITER_FOR_COPYING
							+ mapping.get(src)
							+ EqViewSchema.DELIMITER_FOR_COPYING
							+ mapping.get(dstP)
							+ EqViewSchema.DELIMITER_FOR_COPYING
							+ mapping.get(dst) + "\n");
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			eqViewSetup.copyFromFileToHardNEQRulePartTable(graphId, file
					.getAbsolutePath());
			file.delete();
		}
	}

	/**
	 * Clusters the graph using approach in dedupalog paper
	 * <p>
	 * When we are in update mode, we do not calculate edge weights again
	 * 
	 * @param update
	 *            whether it is update mode or not
	 */
	private void cluster(boolean update) {
		set.clear();
		shuffle();
		processShuffledHardNegEdges();
		processShuffledHardEdges(set);
		processedSoftEdges.clear();
		ArrayList<Integer>[] processedSoftEdgesAL = this
				.convertedSrcLstDstEdges(softEdges, vertices, update, this
						.getMultipleSoftEdges(), totalSoftRuleWeight);
		clusterSoftEdgesArrayArrayList(processedSoftEdgesAL);
	}

	/**
	 * Process permuted hard neq edges
	 */
	private void processShuffledHardNegEdges() {
		processedHardNegEdges.clear();
		for (Iterator<Edge> iterator = hardNegEdges.keySet().iterator(); iterator
				.hasNext();) {
			Edge edge = iterator.next();
			int src = new Integer(vertices[edge.getSrcId()].getId());
			int dst = new Integer(vertices[edge.getDstId()].getId());
			Integer newSrc = new Integer(Math.min(src, dst));
			Integer newDst = new Integer(Math.max(src, dst));

			if (processedHardNegEdges.containsKey(newSrc)) {
				LinkedHashMap<Integer, Integer> listOfSrc = processedHardNegEdges
						.get(newSrc);
				if (!listOfSrc.containsKey(newDst))
					listOfSrc.put(newDst, new Integer("1"));
			} else {
				LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
				map.put(newDst, new Integer("1"));
				processedHardNegEdges.put(newSrc, map);
			}

			if (processedHardNegEdges.containsKey(newDst)) {
				LinkedHashMap<Integer, Integer> listOfDst = processedHardNegEdges
						.get(newDst);
				if (!listOfDst.containsKey(newSrc))
					listOfDst.put(newSrc, new Integer("1"));
			} else {
				LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
				map.put(newSrc, new Integer("1"));
				processedHardNegEdges.put(newDst, map);
			}
		}
	}

	/**
	 * Processes permuted hard eq edges
	 * 
	 * @param dsjSet
	 *            With permuted hard eq edges, sets might be merged and its the
	 *            parameter of the method
	 */
	private void processShuffledHardEdges(DisjointSets dsjSet) {
		for (Iterator<Edge> iterator = hardEdges.keySet().iterator(); iterator
				.hasNext();) {
			Edge edge = iterator.next();
			int src = vertices[edge.getSrcId()].getId();
			int dst = vertices[edge.getDstId()].getId();
			int cls1 = dsjSet.find(src);
			int cls2 = dsjSet.find(dst);
			if (cls1 != cls2) {
				Integer newSrc = new Integer(Math.min(cls1, cls2));
				Integer newDst = new Integer(Math.max(cls1, cls2));
				if (processedHardNegEdges.containsKey(newSrc)
						&& processedHardNegEdges.get(newSrc)
								.containsKey(newDst)) {
					System.err.println("Conflict");
				} else {
					appendNegEdgesOfDstToSrcForHard(newSrc, newDst);
					dsjSet.unionByValue(Math.min(cls1, cls2), Math.max(cls1,
							cls2));
				}
			}
		}
	}

	/**
	 * Processes permuted soft edges
	 * <p>
	 * This is used in batch clustering
	 */
	private void processSoftEdgesForQuery() {
		if (this.getMultipleSoftEdges()) {
			for (Iterator<Edge> iterator = softEdges.keySet().iterator(); iterator
					.hasNext();) {
				Edge edge = iterator.next();
				double edgeCount = softEdges.get(edge);

				if (((double) edgeCount / (double) totalSoftRuleWeight) >= GraphConstants.EDGE_VOTE_THRESHOLD) {
					int cls1 = querySet.find(edge.getSrcId());
					int cls2 = querySet.find(edge.getDstId());
					if (cls1 != cls2) {
						Integer newSrc = new Integer(Math.min(cls1, cls2));
						Integer newDst = new Integer(Math.max(cls1, cls2));
						if (processedHardNegEdges.containsKey(newSrc)
								&& processedHardNegEdges.get(newSrc)
										.containsKey(newDst)) {
							System.err.println("Conflict");
						} else {
							appendNegEdgesOfDstToSrcForHard(newSrc, newDst);
							querySet.unionByValue(Math.min(cls1, cls2), Math
									.max(cls1, cls2));
						}
					}
				}
			}
		} else {
			for (Iterator<Edge> iterator = softEdges.keySet().iterator(); iterator
					.hasNext();) {
				Edge edge = iterator.next();
				int cls1 = querySet.find(edge.getSrcId());
				int cls2 = querySet.find(edge.getDstId());
				if (cls1 != cls2) {
					Integer newSrc = new Integer(Math.min(cls1, cls2));
					Integer newDst = new Integer(Math.max(cls1, cls2));
					if (processedHardNegEdges.containsKey(newSrc)
							&& processedHardNegEdges.get(newSrc).containsKey(
									newDst)) {
						System.err.println("Conflict");
					} else {
						appendNegEdgesOfDstToSrcForHard(newSrc, newDst);
						querySet.unionByValue(Math.min(cls1, cls2), Math.max(
								cls1, cls2));
					}
				}
			}
		}
	}

	/**
	 * Processes soft edges for clustering
	 * 
	 * @param sEdges
	 *            list of soft edges
	 */
	private void clusterSoftEdgesArrayArrayList(ArrayList<Integer>[] sEdges) {
		// here, maybe we might use hashmap to keep clustered index i.e.
		// vertices whose
		// representative is not equal to themselves
		for (int i = 0; i < V; i++) {
			int cls1 = set.find(i);
			if (cls1 == i) {
				ArrayList<Integer> vList = sEdges[i];

				for (int j = 0; j < vList.size(); j++) {
					int elt2 = vList.get(j).intValue();
					int cls2 = set.find(elt2);
					if ((processedHardNegEdges.containsKey(cls1) && processedHardNegEdges
							.get(cls1).containsKey(cls2))) {
					}
					if (cls2 == elt2
							&& cls1 != cls2
							&& !(processedHardNegEdges.containsKey(cls1) && processedHardNegEdges
									.get(cls1).containsKey(cls2))) {
						appendNegEdgesOfDstToSrcForSoft(new Integer(cls1),
								new Integer(cls2));
						set.unionByValue(cls1, cls2);
					}
				}
			}
		}
	}

	/**
	 * Updates hard neq edges for merged sets because of soft edge
	 * 
	 * @param src
	 *            src vertex of the edge
	 * @param dst
	 *            dst vertex of the edge
	 */
	private void appendNegEdgesOfDstToSrcForSoft(Integer src, Integer dst) {
		if (processedHardNegEdges.containsKey(dst)) {
			if (!processedHardNegEdges.containsKey(src))
				processedHardNegEdges.put(src,
						new LinkedHashMap<Integer, Integer>());

			LinkedHashMap<Integer, Integer> listOfDst = processedHardNegEdges
					.get(dst);
			LinkedHashMap<Integer, Integer> listOfSrc = processedHardNegEdges
					.get(src);
			for (Iterator<Integer> iterator = listOfDst.keySet().iterator(); iterator
					.hasNext();) {
				Integer negOfDst = iterator.next();
				// here # of occurrences is not important
				if (!listOfSrc.containsKey(negOfDst))
					listOfSrc.put(negOfDst, new Integer("1"));
			}
		}
	}

	/**
	 * Updates hard neq edges for merged sets because of hard eq edge
	 * 
	 * @param src
	 *            src vertex of the edge
	 * @param dst
	 *            dst vertex of the edge
	 */
	private void appendNegEdgesOfDstToSrcForHard(Integer src, Integer dst) {
		boolean srcExists = processedHardNegEdges.containsKey(src);
		boolean dstExists = processedHardNegEdges.containsKey(dst);
		if (srcExists && dstExists) {
			LinkedHashMap<Integer, Integer> listOfSrc = processedHardNegEdges
					.get(src);
			LinkedHashMap<Integer, Integer> listOfDst = processedHardNegEdges
					.get(dst);
			for (Iterator<Integer> iterator = listOfDst.keySet().iterator(); iterator
					.hasNext();) {
				Integer negOfDst = iterator.next();
				// here # of occurrences is not important
				if (!listOfSrc.containsKey(negOfDst))
					listOfSrc.put(negOfDst, new Integer("1"));
			}

			for (Iterator<Integer> iterator = listOfSrc.keySet().iterator(); iterator
					.hasNext();) {
				Integer negOfSrc = iterator.next();
				if (!listOfDst.containsKey(negOfSrc))
					listOfDst.put(negOfSrc, new Integer("1"));
			}
		} else if (srcExists || dstExists) {
			if (srcExists) {
				processedHardNegEdges.put(dst,
						new LinkedHashMap<Integer, Integer>());
				LinkedHashMap<Integer, Integer> listOfSrc = processedHardNegEdges
						.get(src);
				LinkedHashMap<Integer, Integer> listOfDst = processedHardNegEdges
						.get(dst);
				for (Iterator<Integer> iterator = listOfSrc.keySet().iterator(); iterator
						.hasNext();) {
					Integer negOfSrc = iterator.next();
					if (!listOfDst.containsKey(negOfSrc))
						listOfDst.put(negOfSrc, new Integer("1"));
				}
			} else {
				processedHardNegEdges.put(src,
						new LinkedHashMap<Integer, Integer>());
				LinkedHashMap<Integer, Integer> listOfSrc = processedHardNegEdges
						.get(src);
				LinkedHashMap<Integer, Integer> listOfDst = processedHardNegEdges
						.get(dst);
				for (Iterator<Integer> iterator = listOfDst.keySet().iterator(); iterator
						.hasNext();) {
					Integer negOfDst = iterator.next();
					// here # of occurrences is not important
					if (!listOfSrc.containsKey(negOfDst))
						listOfSrc.put(negOfDst, new Integer("1"));
				}
			}
		}
	}

	/**
	 * Shuffles the vertices list
	 */
	private void shuffle() {
		for (int i = 0; i < vertices.length; i++) {
			int randIndex = rand.nextInt(V);
			reverseMap[vertices[randIndex].getId()] = i;
			reverseMap[vertices[i].getId()] = randIndex;
			int temp = vertices[i].getId();
			vertices[i].setId(vertices[randIndex].getId());
			vertices[randIndex].setId(temp);
		}
	}

	/**
	 * Returns whether the graph contains multiple soft rules or not
	 * 
	 * @return whether the graph contains multiple soft rules or not
	 */
	private boolean getMultipleSoftEdges() {
		return this.multipleSoftEdges;
	}

	/**
	 * Clusters the graph
	 * <p>
	 * After permutation, it is clustered and results (entity partitions, view,
	 * soft rule, hard eq, hard neq rule partitions table) written to the
	 * database
	 */
	public void batchCluster() {
		long t = System.currentTimeMillis();
		this.initializeClusterQuery();
		this.cluster(false);
		this.writeInitialTables();
		clusteringAndWriteTime = System.currentTimeMillis() - t;
	}

	/**
	 * Creates new edge list from shuffled vertices (in order)
	 * <p>
	 * During the update mode, since edges with weight >= EDGE_THRESHOLD is
	 * already retrieved, it is not necessary to check this again.
	 * 
	 * @param edges
	 *            list of edges
	 * @param vertices
	 *            shuffled vertices
	 * @param update
	 *            whether it is update mode or not
	 * @param multiEdges
	 *            whether graph contains multiple rules or not
	 * @param numSoftRules
	 *            number of soft rules
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Integer>[] convertedSrcLstDstEdges(
			LinkedHashMap<Edge, Double> edges, Vertex[] vertices,
			boolean update, boolean multiEdges, double numSoftRules) {
		ArrayList<Integer>[] array = new ArrayList[vertices.length];
		for (int i = 0; i < array.length; i++)
			array[i] = new ArrayList<Integer>();

		if (!update && multiEdges) {
			for (Iterator<Edge> iterator = edges.keySet().iterator(); iterator
					.hasNext();) {
				Edge edge = iterator.next();
				double edgeCount = edges.get(edge);
				if (((double) edgeCount / numSoftRules) >= GraphConstants.EDGE_VOTE_THRESHOLD) {
					int src = vertices[edge.getSrcId()].getId();
					int dst = vertices[edge.getDstId()].getId();
					if (src < dst)
						array[src].add(dst);
					else
						array[dst].add(src);
				}
			}
		} else {
			Set<Edge> set = edges.keySet();
			for (Iterator<Edge> iterator = set.iterator(); iterator.hasNext();) {
				Edge edge = iterator.next();
				int src = vertices[edge.getSrcId()].getId();
				int dst = vertices[edge.getDstId()].getId();
				array[Math.min(src, dst)].add(Math.max(src, dst));
			}
		}

		return array;
	}
}
