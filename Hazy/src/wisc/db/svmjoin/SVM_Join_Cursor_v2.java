package wisc.db.svmjoin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import wisc.db.classification.JoinTuple;
import wisc.db.sql.DatabaseFactory;
import wisc.db.sql.QueryExecutor;

public class SVM_Join_Cursor_v2 {
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
			int M = 0;
			
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
							
						M += firstList.size();
						/*for (int i = 0; i < firstList.size(); i++) {
							JoinTuple tuple = firstList.get(i);
							writer.write(tuple.getId() + "," + tuple.getScore() + ","
									+ tuple2.getId() + "," + tuple2.getScore() + "\n");
						}*/
					}
				}
				System.out.println(M);
				conn.commit();
				set1.close();
				set2.close();
			
			
			System.out.println(M);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*ArrayList<Integer> t1 = new ArrayList<Integer>();
		ArrayList<Integer> t2 = new ArrayList<Integer>();
		
		t1.add(8);
		t1.add(5);
		t1.add(2);
		t1.add(-3);
		t2.add(-4);
		t2.add(-2);
		t2.add(-1);
		t2.add(4);
		
		int j = 0;
		for (int i = 0; i < t2.size(); i++) {
			
			while(j < t1.size() && t1.get(j) + t2.get(i) > 0) {
				j ++;
			}
			if (j >= t1.size()) {
				for (int j2 = i; j2 < t2.size(); j2++) {
					for (int k = 0; k < t1.size(); k++) {
						System.out.println(t1.get(k) + " " + t2.get(j2));
					}
				}
			}
			else {
				for (int j2 = 0; j2 < j; j2++) {
					System.out.println(t1.get(j2) + " " + t2.get(i));
				}
			}
		}*/
	}
	
	
}
