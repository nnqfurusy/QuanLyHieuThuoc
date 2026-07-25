package btl;

import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseHelper.connect();
            // Đổi thông báo để phù hợp với CSDL mới
            System.out.println("KẾT NỐI MYSQL OK!");

            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM NHAN_VIEN");
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                System.out.println("Số nhân viên: " + rs.getInt(1));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
