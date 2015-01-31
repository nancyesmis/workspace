package wisc.db.sql;

import java.sql.*;
import java.util.Properties;

/**
 * QueryExecutor class makes the connection to the database and runs queries
 * 	
 * @author M. Levent Koc
 */
public class QueryExecutor {
	/**
	 * Statement object
	 */
	Statement st;
	/**
	 * Statement object
	 */
	Statement st2;
	/**
	 * Connection object
	 */
	Connection conn;
	
	/**
	 * This constructor makes the connection to the database
	 * 
	 * It fetchs result by 100,000 tuples at a time
	 */
	public QueryExecutor()
	{
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://localhost/" + SQLConstants.DBNAME;
			Properties props = new Properties();
			props.setProperty("user",SQLConstants.USERNAME);
			props.setProperty("password",SQLConstants.PASSWORD);
			conn = DriverManager.getConnection(url, props);
			conn.setAutoCommit(false);
			st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
			st.setFetchSize(100000);
			st2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method returns the connection to the database
	 * @return
	 */
	public Connection getConn() {
		return conn;
	}
	
	/**
	 * 	This method executes given select query
	 * 
	 * @param query to be executed
	 * @return result set of the query
	 */
	public ResultSet executeSelectQuery(String query)
	{
		ResultSet rs = null;
		
		try {
			rs = st.executeQuery(query);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * 	This method executes given update query
	 * 
	 * @param query to be executed
	 */
	public int executeUpdateQuery(String query) {
		int retValue = 0;
		
		try {
			retValue = st2.executeUpdate(query);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return retValue;
	}
}
