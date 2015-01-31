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
		 
		 String driver = "com.mysql.jdbc.Driver";         // 驱动程序名

         
         String url = "jdbc:mysql://localhost/newactivecite";     // URL指向要访问的数据库名scutcs          
        
         String user = "root";       // MySQL配置时的用户名

         String password = "yangxinscu";      // MySQL配置时的密码
         
        

         try {
         
          Class.forName(driver);    // 加载驱动程序
          conn = DriverManager.getConnection(url, user, password);      // 连续数据库
          if(!conn.isClosed())
           System.out.println("Succeeded connecting to the Database!");     //验证是否连接成功
          
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






















