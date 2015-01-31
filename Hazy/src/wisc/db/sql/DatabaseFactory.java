package wisc.db.sql;

/**
 *	DatabaseFactory class keeps a static QueryExecutor object.
 *
 *	If it is null, create it.
 *
 *  @author M. Levent Koc
 **/
public class DatabaseFactory {
	static QueryExecutor queryExecutor = null;
	
	/**
	 * This method returns query executor object
	 * 
	 * @return query executor object
	 * 
	 * @see QueryExecutor
	 */
	public static QueryExecutor getQueryExecutor()
	{
		if (queryExecutor == null)
			queryExecutor = new QueryExecutor();
		
		return queryExecutor;
	}
}
