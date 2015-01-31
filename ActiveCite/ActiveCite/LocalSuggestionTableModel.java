import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;


class LocalSuggestionTableModel extends AbstractTableModel{
	

    private  ResultSet rs;
    private ResultSetMetaData rsmd;
    
	public LocalSuggestionTableModel(){
		String sql = "select * from paper";                  // ÒªÖ´ÐÐµÄSQLÓï¾ä
		DBConn dbconn = new DBConn();
		rs = dbconn.executeQuery(sql);
        try
        {
            rsmd=rs.getMetaData();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
	}

    public LocalSuggestionTableModel(ResultSet aResultSet)
    {
        rs=aResultSet;
        try
        {
            rsmd=rs.getMetaData();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

          public int getColumnCount()
          {
             try
             {

            return  rsmd.getColumnCount();
                   
             }catch(SQLException e)
             {
                 e.printStackTrace();
                 return 0;
             }
          }
          public Object getValueAt(int r,int c)
          {
               try
               {
                   rs.absolute(r+1);
             return       rs.getObject(c+1);
                    
               }catch(SQLException e)
               {
                   e.printStackTrace();
                   return null;
               }

          }
          public int getRowCount()
   {
     try
     {
         rs.last();
   
          return      rs.getRow();
 
     }catch(SQLException e)
     {
         e.printStackTrace();
         return 0;
     }
   }
        } 