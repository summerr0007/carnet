import java.net.*;
import java.io.*;
import java.sql.*;



public class server {
    public static int port = 29481; // 連接埠

    public static void main(String args[]) throws Exception {
        ServerSocket ss = new ServerSocket(port);     // 建立 TCP 伺服器。

        try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); // Load the JDBC driver
            Connection con = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/AAA/IdeaProjects/net/carpark.mdb");//Connect to the databse
            System.out.println("資料庫連線測試成功！");
            Statement stmt=con.createStatement();

            while (true) {                                // 不斷的接收處理輸入訊息。
                Socket sc = ss.accept();                // 接收輸入訊息。
                OutputStream os = sc.getOutputStream();    // 取得輸出串流。
                ResultSet rs=stmt.executeQuery("select * from park where used=0");
                if(rs.next()){
                    String no=rs.getString("non");
                    String used=rs.getString("used");
                    System.out.println(no+","+used);
                    os.write(no.getBytes("UTF-8"));// 送訊息到 Client 端。
                    int cnt=stmt.executeUpdate("update park set used=1 where non="+no);
                    os.close();                                // 關閉輸出串流。
                    sc.close(); // 關閉 TCP 伺服器。
                }else{
                    os.write("0".getBytes("UTF-8"));// 送訊息到 Client 端。
                    os.close();                                // 關閉輸出串流。
                    sc.close(); // 關閉 TCP 伺服器。
                }
                rs=stmt.executeQuery("select * from park");
                while(rs.next()){
                    String no=rs.getString("non");
                    String used=rs.getString("used");
                    System.out.println(no+"<,>"+used);
                }
            }
        }catch (IOException ioe){

        }catch (SQLException e){
            e.printStackTrace();
        }

    }


}


//try
//        {
//        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); // Load the JDBC driver
//        Connection con = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/AAA/IdeaProjects/work/src/db2.mdb");//Connect to the databse
//        System.out.println("資料庫連線測試成功！");
//        Statement stmt=con.createStatement();
//        int cnt=stmt.executeUpdate("insert into students values('94610060','新同學','M','彰化')");
//
//        ResultSet rs=stmt.executeQuery("select * from students where stud_addr='彰化' and stud_sex='M'");
//        while(rs.next())
//        {
//        String stud_no=rs.getString(1);
//        String stud_name=rs.getString(2);
//        String stud_sex=rs.getString("stud_sex");
//        String stud_addr=rs.getString("stud_addr");
//        System.out.println(stud_no+","+stud_name+","+stud_sex+","+stud_addr);
//        }
//        }
//        catch(ClassNotFoundException e)
//        {
//        System.out.println("找不到驅動程式類別");
//        e.printStackTrace();
//        }
//        catch(SQLException e)
//        {
//        e.printStackTrace();
//        }