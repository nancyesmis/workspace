import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

class ReferenceTable extends AbstractTableModel {
	        

	    	ResultSet rs = null;
	    	private int numberOfItems = 0;
	    	
	    	private String[] columnNames = {"Cite","author",
	                "Title",
	                "Conference",
	                "Year","Citation-info"};
	        
	        //后来加上的将要处理表数据的显示问题
	        
	        public ReferenceTable(){
				
				//rs.next();
				int row = 0; 
				int col = 0;
				ResultSet rset = null;
				int mark = 0;
				try{
					String sql = "select distinct paperTitle from reference";   // 要执行的SQL语句
					
					System.out.println("SQL: = " + sql);
					
					DBConn dbconn = new DBConn();
					rs = dbconn.executeQuery(sql);
					Paper instance;
					Vector suggestedPaper = new Vector();
					
					if(rs == null){
						System.out.println("rs is null, stop");
					}
					while(rs.next()){
						
						mark = 1;//rs返回的是有结果的
						
						String title = rs.getObject("papertitle").toString();
						//sql = "select * from paper where title = '" + title +"'";
						sql = "select * from paper where title = '" + title +"'";
						rset = dbconn.executeQuery(sql);
						
						//int pos = Integer.parseInt((rs.getObject("refpos").toString()));
						
						//System.out.println("refpos = " + pos);
						
						if(rset.next()){
							instance = new Paper();
							instance.setAuthors(rset.getObject("author").toString());
							
							System.out.println(rset.getObject("author").toString());
							
							instance.setTitle(rset.getObject("title").toString());
							
							System.out.println(rset.getObject("title").toString());
							
							instance.setPublish_place(rset.getObject("conference").toString());
							instance.setYear(rset.getObject("publication_year").toString());
							//将suggest的paper保存下来...
							suggestedPaper.add(instance);	
						}
					}
						numberOfItems = suggestedPaper.size();
						System.out.println("numberOfItems = " + numberOfItems);
						
						for(int i = 0; i < suggestedPaper.size(); i ++){
							col = 0;
							//如果这篇paper没有在这个地方已经被引用过，就是false，也就是说未被checked
							data[row][col] = new Boolean(false);
							col++;
							data[row][col] = ((Paper)suggestedPaper.get(i)).getAuthors();
							
							col++;
							data[row][col] =((Paper)suggestedPaper.get(i)).getTitle();
							
							col++;
							data[row][col] = ((Paper)suggestedPaper.get(i)).getPublish_place();
							
							col++;
							data[row][col] = ((Paper)suggestedPaper.get(i)).getYear();
							
							col++;
							sql = "select * from reference where paperTitle = '" + ((Paper)suggestedPaper.get(i)).getTitle() + "'";
							rs  = dbconn.executeQuery(sql);
							
							int count = 0;
							String temp = "";
							while (rs.next()){
								count ++;
								temp = temp + "[" + count + "]";
							}
							
							data[row][col] = new String (temp);
							
							row++;
						}
					
					
					
					/*while(rs.next()){
						System.out.println("rs is not null");
						col = 0;
						//如果这篇paper没有在这个地方已经被引用过，就是false，也就是说未被checked
						data[row][col] = new Boolean(true);
						col++;
						data[row][col] = rs.getObject("author").toString();
						System.out.println((data[row][col].toString()));
						col++;
						data[row][col] = rs.getObject("title").toString();
						System.out.println((data[row][col].toString()));
						col++;
						data[row][col] = rs.getObject("conference").toString();
						System.out.println((data[row][col].toString()));
						col++;
						data[row][col] = rs.getObject("publication_year").toString();
						System.out.println((data[row][col].toString()));
						col++;
						data[row][col] = new String ("[1][2]");
						
						row++;
					}*/
					//dbconn.close();
					
				}catch (Exception e){
					System.out.println("Find the Exception while assining value");
					e.printStackTrace();
				}
	        }
	        
	        
	        /*private Object[][] data = {
					{new Boolean(true), "M. Kass, et.",
			             "Snakes:Active contour models", "IJCV",new Integer(1998), "[1],[2]"},
			            {new Boolean(false), "S. J. Osher, et.",
			             "Level Set Methods and Dynamic Implicit Surfaces", "Springer", new Integer(2002), "[1]"},
			            {new Boolean(true), "M. B. Nielsen, et.",
			             "Out-of-core and compressed level set methods", "Transactions on Graphics", new Integer(2007), " "},
			};*/
	        

	        private Object[][] data = new Object[50][6];
	        
	        
	        
	        //初始化Object表
	        public void setPos(int pos){
	        	
	        	//position = pos;
	        	
	        }
	        public int getPos(){
	        	
	        	//return position;
	        	return 0;
	        	
	        }
	        public int getColumnCount() {
	            return columnNames.length;
	        }

	        public int getRowCount() {
	        	
	        	//int i = 0;
	        	if(data == null){
	        		return 0;
	        	}
	        	//try{
	            //rs.last();
	            //i = rs.getRow();
	        		
	            //System.out.println("i = " + i);
	            //rs.absolute(1);
	        	//}catch(Exception e){
	        		//System.out.println("Exception Find in get rowcont");
	        		//e.printStackTrace();
	        	//}
	        	
	        	return numberOfItems;
	        	//return data.length;
	        }

	        public String getColumnName(int col) {
	            return columnNames[col];
	        }

	        public Object getValueAt(int row, int col) {
	        	//System.out.println("getValueAt function is called");
	        	return data[row][col];
	        	
	        }

	        /*
	         * JTable uses this method to determine the default renderer/
	         * editor for each cell.  If we didn't implement this method,
	         * then the last column would contain text ("true"/"false"),
	         * rather than a check box.
	         */
	        public Class getColumnClass(int c) {
	        	if(numberOfItems == 0){
	        		return null;
	        	}
	            return getValueAt(0, c).getClass();
	        }

	        /*
	         * Don't need to implement this method unless your table's
	         * editable.
	         */
	        public boolean isCellEditable(int row, int col) {
	            //Note that the data/cell address is constant,
	            //no matter where the cell appears onscreen.
	            if (col < 1) {
	                return true;
	            } else {
	                return false;
	            }
	        }

	        /*
	         * Don't need to implement this method unless your table's
	         * data can change.
	         */
	        public void setValueAt(Object value, int row, int col) {
	            data[row][col] = value;
	            fireTableCellUpdated(row, col);
	        }
	    }