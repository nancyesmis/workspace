package wisc.db.equivalence;

/**
 *	Edge class that represents edge in a graph. It has source vertex number, dest vertex num and score
 *
 *  @author koc
 **/


public class Edge implements Comparable<Edge>{
	/**
	 * 	src vertex of the edge
	 */
	private Vertex src;
	/**
	 * 	dst vertex of the edge
	 */
	private Vertex dst;
	
	/**
	 * 	Constructor for edge.
	 * 
	 * @param src source vertex of the graph
	 * @param dst destination vertex of the graph
	 */
	public Edge(int src, int dst) 
	{
		this.src = new Vertex(src);
		this.dst = new Vertex(dst);
	}

	/**
	 * 	Returns source vertex
	 * 
	 * 	@return src vertex
	 */
	public Vertex getSrc() {
		return src;
	}

	/**
	 * 	Returns destination vertex
	 * 
	 * 	@return dst vertex
	 */
	public Vertex getDst() {
		return dst;
	}
	
	/**
	 * 	Returns id of source vertex
	 * @return source vertex id
	 */
	public int getSrcId() {
		return src.getId();
	}
	
	/**
	 * 	Returns id of destination vertex
	 * @return destination vertex id
	 */
	public int getDstId() {
		return dst.getId();
	}

	/**
	 * 	Returns string representation
	 * 
	 * 	@return string
	 */
	public String toString() {
		String result = src + GraphConstants.EDGE_DELIMITER + dst;
		return result;
	}
	
	/**	
	 *	Returns whether an edge is greater than or not. If source vertex is greater, returns 1. If it is small, returns -1.
	 *	if they are equal, check for destination vertex. If it is greater, returns 1, else returns 0. <src, dst> pairs
	 *	cannot be equal.
	 *
	 * 	@return comparison result
	 */
	public int compareTo(Edge edge) {
		if (this.getSrc().getId() < edge.getSrc().getId())
			return -1;
		else if (this.getSrc().getId() == edge.getSrc().getId())
		{
			if (this.getDst().getId() < edge.getDst().getId())
				return -1;
			else if (this.getDst().getId() == edge.getDst().getId())
				return 0;
			else
				return 1;
		}
		else
			return 1;
	}
	
	@Override
	/**
	 * Returns hashcode of the object
	 */
	public int hashCode() {
		String code = this.getSrc().getId() + GraphConstants.EDGE_DELIMITER + this.getDst().getId();
		return code.hashCode();
	}
	
	@Override
	/**
	 * Returns whether Edge object is same with the given Edge object
	 */
	public boolean equals(Object obj) {
		return this.compareTo((Edge) obj) == 0;
	}
}
