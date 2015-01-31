import java.awt.Component;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;


class GlobalSuggestionTable extends AbstractTableModel {
    

	ResultSet rs = null;
	private int numberOfItems = 0;
	

	ImageIcon icon = createImageIcon("images/drag.png");
	private String[] columnNames = {"Drag&Insert",
            "Authors",
            "Title",
            "Conference",
            "Year","Citation-info"};
    
    public GlobalSuggestionTable(){
		
		//rs.next();
		int row = 0; 
		int col = 0;
		int mark = 0;
		try{
			//暂时testpos的位置为50
			//position = 10;
			String sql = "select * from globalsuggestion, paper where globalsuggestion.title = paper.title";  // 要执行的SQL语句
			
			System.out.println("SQL: = " + sql);
			
			DBConn dbconn = new DBConn();
			rs = dbconn.executeQuery(sql);
			Paper instance;
			Vector suggestedPaper = new Vector();
			
			if(rs == null){
				System.out.println("rs is null, stop");
			}

				
			while (rs.next()){
				numberOfItems++;
				col = 0;

				data[row][col] = icon;

				col++;
				data[row][col] = rs.getObject("author");
				
				col++;
				data[row][col] = rs.getObject("title");
				
				col++;
				data[row][col] = rs.getObject("conference");
				
				col++;
				data[row][col] = (rs.getObject("publication_year"));
				
				col++;
				//处理有多少出reference过该篇paper
				sql = "select * from reference where paperTitle = '" + rs.getObject("title").toString() + "'";
				ResultSet rset  = dbconn.executeQuery(sql);
				
				int count = 0;
				String temp = "";
				while (rset.next()){
					count ++;
					temp = temp + "[" + count + "]";
				}
				
				data[row][col] = new String (temp);
				
				row++;
		}
			
		}catch (Exception e){
			System.out.println("Find the Exception while assining value");
			e.printStackTrace();
		}
		System.out.println("numberOfItems is " + numberOfItems);
    }
    

    private Object[][] data = new Object[50][6];
    
    
    
    //初始化Object表
    public void setPos(int pos){
    	
    	//position = pos;
    	
    }
    public int getPos(){
    	
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
    
    public String getRow(int row)
    {
    	String rawdata = null;
    	rawdata = (String)data[row][2];
    	return rawdata;
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
    	return false;
    	
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = CombinationDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
