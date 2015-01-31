package wisc.db.equivalencetest;

import wisc.db.equivalence.Vertex;
import junit.framework.TestCase;

/**
 * Tests {@link Vertex} class
 * 
 * @author koc
 *
 */
public class VertexTest extends TestCase {
	Vertex vertex1;
	Vertex vertex2;
	Vertex vertex3;
	int id1 = 5;
	String strId1 = "5";
	String id2 = "10";
	int parsedId2 = 10;
	
	/**
	 * Default constructor
	 * 
	 * @param name
	 */
	public VertexTest(String name) {
		super(name);
	}

	/**
	 * Initializes 3 Vertex objects
	 */
	protected void setUp() throws Exception {
		super.setUp();
		vertex1 = new Vertex(id1);
		vertex2 = new Vertex(id2);
		vertex3 = new Vertex(strId1);
	}

	/**
	 * teardown method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests whether hashcode of vertices are correct or not
	 */
	public void testHashCode() {
		boolean b1 = vertex1.hashCode() == (id1 + "").hashCode();
		boolean b2 = vertex2.hashCode() == (parsedId2 + "").hashCode();
		assertTrue(b1 & b2);
	}

	/**
	 * Tests whether Vertex constructor that takes id as a parameter, correctly creates vertex or not
	 */
	public void testVertexInt() {
		boolean b1 = vertex1.getId() == id1;
		boolean b2 = vertex2.getId() == parsedId2;
		assertTrue(b1 & b2);
	}

	/**
	 * Tests whether Vertex constructor that takes String as a parameter, correctly creates vertex or not
	 */
	public void testVertexString() {
		boolean b1 = (vertex1.toString()).equals(id1 + "");
		boolean b2 = (vertex2.toString()).equals(id2);
		assertTrue(b1 & b2);
	}

	/**
	 * Tests getId method of Vertex class retrieves correctly id of Vertex
	 */
	public void testGetId() {
		boolean b1 = vertex1.getId() == id1;
		boolean b2 = vertex2.getId() == parsedId2;
		assertTrue(b1 & b2);
	}

	/**
	 * Tests setId method of Vertex class sets correctly new id of Vertex
	 */
	public void testSetId() {
		vertex1.setId(7);
		vertex2.setId(11);
		boolean b1 = vertex1.getId() == 7;
		boolean b2 = vertex2.getId() == 11;
		assertTrue(b1 & b2);
	}

	/**
	 * Tests toString method of Vertex
	 */
	public void testToString() {
		boolean b1 = (vertex1.toString()).equals(id1 + "");
		boolean b2 = (vertex2.toString()).equals(id2);
		assertTrue(b1 & b2);
	}

	/**
	 * Tests .equals method of Vertex
	 */
	public void testEqualsObject() {
		boolean b1 = vertex1.equals(vertex2);
		boolean b2 = vertex1.equals(vertex3);
		boolean b3 = vertex2.equals(vertex3);
		assertTrue(!b1 & b2 & !b3);
	}

}
