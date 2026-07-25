package btl;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseHelper {

    // Cấu hình URL cho MySQL trên XAMPP (cổng mặc định 3306)
    private static final String URL = "jdbc:mysql://localhost:3306/QL_NhaThuoc";

    // Tài khoản mặc định của XAMPP
    private static final String USER = "root";
    private static final String PASS = ""; // XAMPP mặc định không có mật khẩu

    public static Connection connect() throws Exception {
        // Khai báo Driver của MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
