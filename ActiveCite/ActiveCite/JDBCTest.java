import java.sql.*;

public class JDBCTest {

public static void main(String[] args){

          
           String driver = "com.mysql.jdbc.Driver";         // 驱动程序名

         
           String url = "jdbc:mysql://localhost/abccs";     // URL指向要访问的数据库名scutcs          
          
           String user = "root";       // MySQL配置时的用户名
  
           String password = "yangxinscu";      // MySQL配置时的密码

           try {
           
            Class.forName(driver);    // 加载驱动程序

          
            Connection conn = DriverManager.getConnection(url, user, password);      // 连续数据库

            if(!conn.isClosed())
             System.out.println("Succeeded connecting to the Database!");     //验证是否连接成功

           
            Statement statement = conn.createStatement();               // statement用来执行SQL语句

          
            String sql = "select * from mytable";                  // 要执行的SQL语句

          
            ResultSet rs = statement.executeQuery(sql);       // 结果集
            
            System.out.println("-----------------------------------------");
            System.out.println("执行结果如下所示:");
            System.out.println("-----------------------------------------");
            System.out.println(" 学号" + "\t" + " 姓名" + "\t\t" + "性别");
            System.out.println("-----------------------------------------");

            String name = null;

            while(rs.next()) {
   
           
             name = rs.getString("name");  
             System.out.println(name);// 选择sname这列数据
            }

            rs.close();
            conn.close();

           } catch(ClassNotFoundException e) {


            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();


           } catch(SQLException e) {


            e.printStackTrace();


           } catch(Exception e) {


            e.printStackTrace();


           }
}
}