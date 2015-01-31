import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConn {
	
	 private static Connection conn = null;
     private ResultSet   rs   =   null;  
     private Statement   stmt   =   null; 
	 
	 public DBConn(){
		 
		 String driver = "com.mysql.jdbc.Driver";         // ����������

         
         String url = "jdbc:mysql://localhost/newactivecite";     // URLָ��Ҫ���ʵ����ݿ���scutcs          
        
         String user = "root";       // MySQL����ʱ���û���

         String password = "yangxinscu";      // MySQL����ʱ������
         
        

         try {
         
          Class.forName(driver);    // ������������
          conn = DriverManager.getConnection(url, user, password);      // �������ݿ�
          if(!conn.isClosed())
           System.out.println("Succeeded connecting to the Database!");     //��֤�Ƿ����ӳɹ�
          
          stmt = conn.createStatement();

         } catch(ClassNotFoundException e) {

          System.out.println("Sorry,can`t find the Driver!");
          e.printStackTrace();


         } catch(SQLException e) {


          e.printStackTrace();


         } catch(Exception e) {

          e.printStackTrace();


         }
		 
	 }

	 public static Connection getConn(){
           return conn;
	 }
	 public Statement getStatement() {
         try {
             return conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return null;
     }


	 public ResultSet executeQuery(String sql){

		 try{
			 
			 rs = getStatement().executeQuery(sql);
			 
		 }catch(SQLException e){
			
			 e.printStackTrace();
			 
	     }catch(Exception e) {
	         
	    	 e.printStackTrace();
	    	 
	     }	 
	         return rs;
	 }
	 public void executeUpdate(String sql){
		 
		 try{
			 
			 getStatement().executeUpdate(sql); 
			 
		 }catch(SQLException e){
			
			 e.printStackTrace();
			 
	     }catch(Exception e) {
	        
	    	 e.printStackTrace();
	    	 
	     }	 
	 }
	 public ResultSet executeSelect(String sql){
		 
		 try{
			 
			 rs = getStatement().executeQuery(sql); 
			 
		 }catch(SQLException e){
			 
			 e.printStackTrace();
			 
	     }catch(Exception e) {
	         
	    	 e.printStackTrace();
	    	 
	     }	 
	     return rs;
	 }
	 public void executeDelete(String sql){
		 
		 try{
			 
			 getStatement().executeUpdate(sql); 
			 
		 }catch(SQLException e){
			 
			 e.printStackTrace();
			 
	     }catch(Exception e) {
	         
	    	 e.printStackTrace();
	    	 
	     }	 
	 }
	 public void close(){
		 try{
		 if(rs != null){
			 rs.close();
		 }
		 if(stmt != null){
			 stmt.close();
		 }
		 if(conn != null){
			 conn.close();
		 }
		 }catch(Exception e){
			 
		 }
	 }
	}






















