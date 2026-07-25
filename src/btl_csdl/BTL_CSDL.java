//package btl_csdl;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//
//public class BTL_CSDL {
//
//   public static Connection getConnection() {
//    try {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//
//        Connection conn = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/ban_thuoc?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
//                "root",
//                "123456"
//        );
//
//
//        System.out.println("KẾT NỐI THÀNH CÔNG!");
//        return conn;
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        return null;
//    }
//}
//}

