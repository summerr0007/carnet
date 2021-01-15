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
                ResultSet rs=stmt.executeQuery("select * from park where used=0");// 查詢資料庫空閒車位
                if(rs.next()){                                                       //used 為0表示車位未被使用
                    String no=rs.getString("non");
                    String used=rs.getString("used");
                    System.out.println(no+","+used);
                    os.write(no.getBytes("UTF-8"));// 送訊息到 Client 端。
                    int cnt=stmt.executeUpdate("update park set used=1 where non="+no);
                    os.close();                                // 關閉輸出串流。
                    sc.close(); // 關閉 TCP
                }else{
                    os.write("0".getBytes("UTF-8"));// 送訊息到 Client 端。
                    os.close();                                // 關閉輸出串流。
                    sc.close(); // 關閉 TCP
                }
                rs=stmt.executeQuery("select * from park");//測試訊息
                while(rs.next()){//
                    String no=rs.getString("non");//
                    String used=rs.getString("used");//
                    System.out.println(no+"<,>"+used);//
                }//測試訊息
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }


}

