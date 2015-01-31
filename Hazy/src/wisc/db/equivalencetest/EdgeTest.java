package wisc.db.equivalencetest;

import wisc.db.equivalence.Edge;
import wisc.db.equivalence.Vertex;
import junit.framework.TestCase;

/**
 * Tests {@link Edge} class
 * 
 * @author koc
 *
 */
public class EdgeTest extends TestCase {
	Edge edge1;
	Edge edge2;
	Edge edge3;
	
	int src1 = 3;
	int dst1 = 4;
	int src2 = 5;
	int dst2 = 7;
	int src3 = 3;
	int dst3 = 4;
	
	/**
	 * Default constructor
	 * 
	 * @param name
	 */
	public EdgeTest(String name) {
		super(name);
	}

	/**
	 * Initializes 3 edges
	 */
	protected void setUp() throws Exception {
		super.setUp();
		edge1 = new Edge(src1, dst1);
		edge2 = new Edge(src2, dst2);
		edge3 = new Edge(src3, dst3);
	}

	/**
	 * teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests hashcode of edge1.
	 */
	public void testHashCode() {
		assertTrue(edge1.hashCode() == (src1 + "-" + dst1).hashCode());
	}

	/**
	 * Tests edge1 is correctly created or not
	 */
	public void testEdge() {
		assertTrue(edge1.getSrcId() == src1);
		assertTrue(edge1.getDstId() == dst1);
	}

	/**
	 * Tests edge1's src is correctly returned or not
	 */
	public void testGetSrc() {
		Vertex srcV = new Vertex(src1);
		assertTrue(edge1.getSrc().equals(srcV));
	}

	/**
	 * Tests edge1's dst is correctly returned or not
	 */
	public void testGetDst() {
		Vertex dstV = new Vertex(dst1);
		assertTrue(edge1.getDst().equals(dstV));
	}

	/**
	 * Tests edge1's src id is correctly returned or not
	 */
	public void testGetSrcId() {
		assertTrue(edge1.getSrcId() == src1);
	}

	/**
	 * Tests edge1's dst id is correctly returned or not
	 */
	public void testGetDstId() {
		assertTrue(edge1.getDstId() == dst1);
	}

	/**
	 * Tests toString method is correct
	 */
	public void testToString() {
		assertTrue(edge1.toString().equals(src1 + "-" + dst1));
	}

	/**
	 * Performs 3 comparison between 3 edges and checks comparisons are correctly labeled
	 */
	public void testCompareTo() {
		assertTrue(edge1.compareTo(edge2) < 0);
		assertTrue(edge1.compareTo(edge3) == 0);
		assertTrue(edge2.compareTo(edge3) > 0);
	}

	/**
	 * Performs 3 comparison between 3 edges and checks equality relations are handled correctly
	 */
	public void testEqualsObject() {
		assertFalse(edge1.equals(edge2));
		assertTrue(edge1.equals(edge3));
		assertFalse(edge2.equals(edge3));
	}

}
