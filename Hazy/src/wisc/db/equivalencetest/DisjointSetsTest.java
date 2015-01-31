package wisc.db.equivalencetest;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import wisc.db.equivalence.DisjointSets;
import junit.framework.TestCase;

/**
 * Tests {@link DisjointSets} class
 * 
 * @author koc
 *
 */
public class DisjointSetsTest extends TestCase {
	DisjointSets set;
	int V = 5;
	
	/**
	 * default constructor
	 * 
	 * @param name
	 */
	public DisjointSetsTest(String name) {
		super(name);
	}

	/**
	 * Initializes disjoint set object
	 */
	protected void setUp() throws Exception {
		super.setUp();
		set = new DisjointSets(V);
	}

	/**
	 * teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests whether disjointset object is correctly created
	 */
	public void testDisjointSets() {
		assertTrue(set.getNumElements() == V);
		assertTrue(set.getNumSets() == V);
	}

	/**
	 * Tests whether clear method correctly works. It unites two sets twice and check whether number of sets is equal to number of elements after cleaning
	 */
	public void testClear() {
		set.unionByRank(3, 4);
		set.unionByValue(2, 3);
		assertTrue(set.getNumElements() != set.getNumSets());
		set.clear();
		assertTrue(set.getNumElements() == set.getNumSets());
	}

	/**
	 * Tests unionByRank method.
	 * <p>
	 * It unites two sets twice and check whether root of sets are correct
	 */
	public void testUnionByRank() {
		//now 3 becomes root
		set.unionByRank(3, 4);
		//since 3 is parent of 4, 3 will be the parent of 2, too
		set.unionByRank(2, 3);
		assertTrue(set.find(0) == 0);
		assertTrue(set.find(1) == 1);
		assertTrue(set.find(2) == 3);
		assertTrue(set.find(3) == 3);
		assertTrue(set.find(4) == 3);
	}

	/**
	 * Tests unionByValue method
	 * <p>
	 * It unites two sets twice and check whether root of sets are correct
	 */
	public void testUnionByValue() {
		//now 3 becomes root
		set.unionByValue(3, 4);
		//since 2 < 3, 2 will be the parent of 3
		set.unionByValue(2, 3);
		assertTrue(set.find(0) == 0);
		assertTrue(set.find(1) == 1);
		assertTrue(set.find(2) == 2);
		assertTrue(set.find(3) == 2);
		assertTrue(set.find(4) == 2);
	}

	/**
	 * Tests unionElementsByRank
	 * <p>
	 * It unites two elements twice and check whether root of sets are correct
	 */
	public void testUnionElementsByRank() {
		//now 3 becomes root
		set.unionElementsByRank(3, 4);
		//since 3 is parent of 4, 3 will be the parent of 2, too
		set.unionElementsByRank(2, 4);
		assertTrue(set.find(0) == 0);
		assertTrue(set.find(1) == 1);
		assertTrue(set.find(2) == 3);
		assertTrue(set.find(3) == 3);
		assertTrue(set.find(4) == 3);
	}

	/**
	 * Tests unionElementsByValue
	 * <p>
	 * It unites two elements twice and check whether root of sets are correct
	 */
	public void testUnionElementsByValue() {
		//now 3 becomes root
		set.unionElementsByValue(3, 4);
		//since 2 < 3(parent of 4), 2 will be the parent of 3
		set.unionElementsByValue(2, 4);
		assertTrue(set.find(0) == 0);
		assertTrue(set.find(1) == 1);
		assertTrue(set.find(2) == 2);
		assertTrue(set.find(3) == 2);
		assertTrue(set.find(4) == 2);
	}

	/**
	 * Tests find method
	 * <p>
	 * It unites two sets and then check whether root of sets are correct
	 */
	public void testFind() {
		set.unionByRank(1, 2);
		assertTrue(set.find(0) == 0);
		assertTrue(set.find(1) == 1);
		assertTrue(set.find(2) == 1);
		assertTrue(set.find(3) == 3);
		assertTrue(set.find(4) == 4);
	}

	/**
	 * Tests getNumElements method
	 * <p>
	 * It checks whether number of elements is equal to V or not
	 */
	public void testGetNumElements() {
		assertTrue(set.getNumElements() == V);
	}

	/**
	 * Tests getNumSets method
	 * <p>
	 * It unites two sets twice and check that number of sets is decreased by 2 or not
	 */
	public void testGetNumSets() {
		//now 3 becomes root
		set.unionByRank(3, 4);
		//since 3 is parent of 4, 3 will be the parent of 2, too
		set.unionByRank(2, 3);
		assertTrue(set.getNumSets() == (V - 2));
	}

	/**
	 * Tests retrieveSets method
	 * <p>
	 * It unites two sets twice and check string of retrieveSets are correct or not
	 */
	public void testRetrieveSets() {
		//now 3 becomes root
		set.unionByRank(3, 4);
		//since 3 is parent of 4, 3 will be the parent of 2, too
		set.unionByRank(2, 3);
		String[] retrieveSets = set.retrieveSets();
		assertTrue(retrieveSets[0].equals("0 "));
		assertTrue(retrieveSets[1].equals("1 "));
		assertTrue(retrieveSets[2].equals(""));
		assertTrue(retrieveSets[3].equals("2 3 4 "));
		assertTrue(retrieveSets[4].equals(""));
	}

	/**
	 * Tests getCluster method
	 * <p>
	 * It unites two sets twice and check clusters arraylist are correct or not
	 */
	public void testGetCluster() {
		//now 3 becomes root
		set.unionByRank(3, 4);
		//since 3 is parent of 4, 3 will be the parent of 2, too
		set.unionByRank(2, 3);
		ArrayList<Integer> clusters = set.getCluster(2);
		assertTrue(clusters.get(0) == 2);
		assertTrue(clusters.get(1) == 3);
		assertTrue(clusters.get(2) == 4);
	}

	/**
	 * Tests getClusters method
	 * <p>
	 * It unites two sets twice and check list of arraylist are correct or not
	 */
	public void testGetClusters() {
		//now 3 becomes root
		set.unionByRank(3, 4);
		//since 3 is parent of 4, 3 will be the parent of 1, too
		set.unionByRank(1, 3);
		ArrayList<ArrayList<Integer>> lists = set.getClusters();
		assertTrue(lists.get(0).get(0) == 0);
		assertTrue(lists.get(1).get(0) == 2);
		assertTrue(lists.get(2).get(0) == 1);
		assertTrue(lists.get(2).get(1) == 3);
		assertTrue(lists.get(2).get(2) == 4);
	}

	/**
	 * Tests retrieveClusters method
	 * <p>
	 * It unites two sets twice and check returned map of root node to cluster nodes are correct or not
	 */
	public void testRetrieveClusters() {
		//now 3 becomes root
		set.unionByRank(3, 4);
		//since 3 is parent of 4, 3 will be the parent of 1, too
		set.unionByRank(1, 3);
		LinkedHashMap<Integer, ArrayList<Integer>> map = set.retrieveClusters();
		assertTrue(map.get(0).get(0) == 0);
		assertTrue(map.get(2).get(0) == 2);
		assertTrue(map.get(3).get(0) == 1);
		assertTrue(map.get(3).get(1) == 3);
		assertTrue(map.get(3).get(2) == 4);
	}

}
