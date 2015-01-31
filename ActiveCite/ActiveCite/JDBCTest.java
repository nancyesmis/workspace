import java.sql.*;

public class JDBCTest {

public static void main(String[] args){

          
           String driver = "com.mysql.jdbc.Driver";         // ����������

         
           String url = "jdbc:mysql://localhost/abccs";     // URLָ��Ҫ���ʵ����ݿ���scutcs          
          
           String user = "root";       // MySQL����ʱ���û���
  
           String password = "yangxinscu";      // MySQL����ʱ������

           try {
           
            Class.forName(driver);    // ������������

          
            Connection conn = DriverManager.getConnection(url, user, password);      // �������ݿ�

            if(!conn.isClosed())
             System.out.println("Succeeded connecting to the Database!");     //��֤�Ƿ����ӳɹ�

           
            Statement statement = conn.createStatement();               // statement����ִ��SQL���

          
            String sql = "select * from mytable";                  // Ҫִ�е�SQL���

          
            ResultSet rs = statement.executeQuery(sql);       // �����
            
            System.out.println("-----------------------------------------");
            System.out.println("ִ�н��������ʾ:");
            System.out.println("-----------------------------------------");
            System.out.println(" ѧ��" + "\t" + " ����" + "\t\t" + "�Ա�");
            System.out.println("-----------------------------------------");

            String name = null;

            while(rs.next()) {
   
           
             name = rs.getString("name");  
             System.out.println(name);// ѡ��sname��������
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