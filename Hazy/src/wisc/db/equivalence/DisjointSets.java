package wisc.db.equivalence;


import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 
 *  A disjoint sets ADT.  Performs union-by-rank and path compression.
 *  Implemented using arrays.  There is no error checking whatsoever.
 *  By adding your own error-checking, you might save yourself a lot of time
 *  finding bugs in your application code for Project 3 and Homework 9.
 *  Without error-checking, expect bad things to happen if you try to unite
 *  two elements that are not roots of their respective sets, or are not
 *  distinct.
 *
 *  Elements are represented by ints, numbered from zero.
 *
 *  @author Mark Allen Weiss
 *  @author koc (modified)
 *
 */
public class DisjointSets {

	/**
	 * 	this keeps all elements in all sets
	 */
	private int[] array;
	/**
	 * 	number of all elements in all lists
	 */
	private int numElements;
	/**
	 * 	number of disjoint sets
	 */
	private int numSets;

	/**
	 *  Construct a disjoint sets object.
	 *  <p>
	 *  Initially, creates n disjoint sets where n is number of elements
	 *
	 *  @param numElements the initial number of elements
	 **/
	public DisjointSets(int numElements) {
		array = new int[numElements];
		for (int i = 0; i < array.length; i++)
			array[i] = -1;
		this.numElements = numElements;
		this.numSets = numElements;
	}
	
	/**
	 *	Clear disjoint sets. After call this method, every disjoint set becomes an independent set 
	 **/
	public void clear() {
		this.numSets = this.numElements;
		for (int i = 0; i < array.length; i++)
			array[i] = -1;
	}

	/**
	 *  union() unites two disjoint sets into a single set.  A union-by-rank
	 *  heuristic is used to choose the new root.  This method will corrupt
	 *  the data structure if root1 and root2 are not roots of their respective
	 *  sets, or if they're identical.
	 *
	 *  @param root1 the root of the first set.
	 *  @param root2 the root of the other set.
	 **/
	public void unionByRank(int root1, int root2) {
		if (array[root2] < array[root1]) {
			array[root1] = root2;             // root2 is taller; make root2 new root
		} else {
			if (array[root1] == array[root2]) {
				array[root1]--;            // Both trees same height; new one is taller
			}
			array[root2] = root1;       // root1 equal or taller; make root1 new root
		}
		numSets--;
	}
	
	/**
	 * This method unites two disjoint sets into a single set. New root is the one whose root's numeric value is the smaller.
	 * 
	 * @param root1
	 * @param root2
	 */
	public void unionByValue(int root1, int root2) {
		if (root2 < root1)
			array[root1] = root2;
		else
			array[root2] = root1;
		numSets --;
	}

	/**
	 *	This method units the sets of the given elements.
	 *	<p> 
	 *	In this method, first root of the sets that contain elm1 and elm2 are found, then they are united with union-by-rank heuristic, if they are not 
	 * 	identical
	 * 
	 * 	@param elm1 first element
	 * 	@param elm2 second element
	 */
	public void unionElementsByRank(int elm1, int elm2) {
		int root1 = find(elm1);
		int root2 = find(elm2);
		if (root1 != root2)
			unionByRank(root1, root2);
	}
	
	/**
	 *	This method units the sets of the given elements.
	 *	<p> 
	 *	In this method, first root of the sets that contain elm1 and elm2 are found, then they are united with unionByValue method, if they are not 
	 * 	identical
	 * 
	 * 	@param elm1 first element
	 * 	@param elm2 second element
	 */
	public void unionElementsByValue(int elm1, int elm2) {
		int root1 = find(elm1);
		int root2 = find(elm2);
		if (root1 != root2)
			unionByValue(root1, root2);
	}

	/**
	 *  find() finds the root of the set containing a given element.
	 *  Performs path compression along the way.
	 *
	 *  @param x the element sought.
	 *  @return the set containing x.
	 **/
	public int find(int x) {
		if (array[x] < 0) {
			return x;                         // x is the root of the tree; return it
		} else {
			// Find out who the root is; compress path by making the root x's parent.
			array[x] = find(array[x]);
			return array[x];                                       // Return the root
		}
	}

	/**
	 *	this method return number of elements in the list
	 * 
	 * 	@return numElements
	 */
	public int getNumElements() {
		return numElements;
	}

	/**
	 * 	this method returns number of sets
	 * 
	 * 	@return numSets
	 */
	public int getNumSets() {
		return numSets;
	}

	/**
	 * 	retrieveSets() method returns string representation of the sets
	 * 	
	 * 
	 * 	@return the array of strings that i th string of array contains string representation(set elements compose a string with space delimiter) of the set whose root is i.
	 **/
	public String[] retrieveSets()
	{
		String[] results = new String[numElements];
		for (int i = 0; i < results.length; i++) {
			results[i] = "";
		}
		for (int i = 0; i < array.length; i++) {
			int cluster = find(i);
			results[cluster] += i + " ";
		}

		return results;
	}
	
	/**
	 * 	This method returns the elements in the set of given element id
	 * 
	 * @param id id of the element
	 * @return list of the elements that are in the same set of given element id
	 */
	public ArrayList<Integer> getCluster(int id) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		int clusterRep = this.find(id);
		for (int i = 0; i < array.length; i++) {
			if (this.find(i) == clusterRep)
				list.add(new Integer(i));
		}
		
		return list;
	}
	
	/**
	 * 	This method gets the list of sets. Each set contains list of elements in the set
	 * 
	 * @return List of sets
	 */
	public ArrayList<ArrayList<Integer>> getClusters(){
		ArrayList<ArrayList<Integer>> returnArray = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < this.numSets; i++) {
			returnArray.add(new ArrayList<Integer>());
		}
		String[] results = new String[numElements];
		for (int i = 0; i < results.length; i++) {
			results[i] = "";
		}
		for (int i = 0; i < array.length; i++) {
			int cluster = find(i);
			results[cluster] += i + " ";
		}

		int index = 0;
		for (int i = 0; i < results.length; i++) {
			if (!results[i].equals("")) {
				String[] splitted = results[i].split(" ");
				//Since there will be additional " ", it is not inserted to arraylist
				for (int j = 0; j < splitted.length; j++)
					returnArray.get(index).add(new Integer(splitted[j]));
				index ++;
			}
		}

		return returnArray;
	}
	
	/**
	 * 	This method returns mapping of root of a set to elements of the set
	 * 
	 * @return mapping of root of the set to elements of the set
	 */
	public LinkedHashMap<Integer, ArrayList<Integer>> retrieveClusters() {
		LinkedHashMap<Integer, ArrayList<Integer>> clusters = new LinkedHashMap<Integer, ArrayList<Integer>>();
		
		for (int i = 0; i < array.length; i++) {
			int clusterId = this.find(i);
			if (clusters.containsKey(new Integer(clusterId)))
				clusters.get(clusterId).add(new Integer(i));
			else {
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(new Integer(i));
				clusters.put(new Integer(clusterId), list);
			}
		}
		return clusters;
	}
}
