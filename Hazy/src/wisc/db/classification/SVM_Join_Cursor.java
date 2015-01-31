package wisc.db.classification;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import wisc.db.sql.DatabaseFactory;
import wisc.db.sql.QueryExecutor;

/**
 * This class retrieves phi1 and phi2 tables and produces the positive tuples 
 * 
 * @author koc
 *
 */
public class SVM_Join_Cursor {
	public static void main(String[] args) {
		try {
			QueryExecutor executor = DatabaseFactory.getQueryExecutor();
			Connection conn = executor.getConn();
			conn.setAutoCommit(false);
			Statement st = conn.createStatement(ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);
			Statement st2 = conn.createStatement(ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);
			double b = Double.parseDouble(args[0]);
			String fileName = args[1];
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName), 262144);
			ResultSet set1 = st.executeQuery("SELECT * FROM PHI1 ORDER BY score DESC");
			ResultSet set2 = st2.executeQuery("SELECT * FROM PHI2 ORDER BY score");
			ArrayList<JoinTuple> firstList = new ArrayList<JoinTuple>();
			boolean finish = false;
			
			JoinTuple tuple1 = null;
			if (set1.next()) {
				tuple1 = new JoinTuple(set1.getInt(1), set1.getFloat(2));
			
				while(set2.next()) {
					JoinTuple tuple2 = new JoinTuple(set2.getInt(1), set2.getFloat(2));
					while (!finish && tuple1.getScore() + tuple2.getScore() - b > 0) {
						firstList.add(tuple1);
						if (set1.next())
							tuple1 = new JoinTuple(set1.getInt(1), set1.getFloat(2));
						else {
							finish = true;
							break;
						}
					}
						
					for (int i = 0; i < firstList.size(); i++) {
						JoinTuple tuple = firstList.get(i);
						writer.write(tuple.getId() + "," + tuple2.getId() + "," + 
								(tuple.getScore() + tuple2.getId() - b) + "\n");
					}
				}
			}
			writer.close();
			conn.commit();
			set1.close();
			set2.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
