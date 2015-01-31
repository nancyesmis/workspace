package wisc.db.equivalence;

/**
 *	Vertex class that represents vertex in the graph
 *
 *  @author koc
 **/
public class Vertex {
	/**
	 * 	id of the graph
	 */
	int id;
	
	/**
	 * 	Constructor for vertex.
	 * @param vertex_num id of the graph
	 */
	public Vertex (int vertex_num) {
		this.id = vertex_num;
	}
	
	/**
	 * 	Constructor for vertex
	 * @param vertex_num id of the graph
	 */
	public Vertex (String vertex_num) {
		this.id = Integer.parseInt(vertex_num);
	}
	
	/**
	 * 	This method returns id of the graph
	 * @return id of the graph
	 */
	public int getId() {
		return id;
	}

	/**
	 * 	This method sets id of the graph
	 * @param id new id of the graph
	 */
	public void setId(int id) {
		this.id = id;
	}

	@Override
	/**
	 * Returns string representation of Vertex object
	 */
	public String toString() {
		return id + "";
	}
	
	@Override
	/**
	 * Returns whether Vertex object is equal to given Vertex object
	 */
	public boolean equals(Object obj) {
		return ((Vertex) obj).getId() == id;
	}
	
	@Override
	/**
	 * Returns hashcode of vertex
	 */
	public int hashCode() {
		String str = id + "";
		return str.hashCode();
	}
}
