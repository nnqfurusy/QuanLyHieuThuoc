package btl;

import javax.swing.*;
import java.awt.*;

public class FormMenu_NhanVien extends JFrame {

    private JButton btnBanHang, btnXemThuoc, btnKhachHang, btnLogout;
    
    // Lưu Mã nhân viên đang đăng nhập
    private String maNV;

    // Constructor nhận Mã NV từ FormLogin truyền sang
    public FormMenu_NhanVien(String maNV) {
        this.maNV = maNV;

        setTitle("Menu Nhân Viên - [" + maNV + "]");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    // Constructor mặc định (dùng để test form độc lập, mặc định NV01)
    public FormMenu_NhanVien() {
        this("NV01");
    }

    private void initComponents() {

        // ======= KHỞI TẠO NÚT =======
        btnBanHang    = new JButton("Bán thuốc (Đơn hàng)");
        btnXemThuoc   = new JButton("Danh sách Thuốc");
        btnKhachHang  = new JButton("Khách hàng");
        btnLogout     = new JButton("Đăng xuất");

        // ======= SET LAYOUT =======
        setLayout(null);

        btnBanHang.setBounds(100, 40, 250, 45);
        btnXemThuoc.setBounds(100, 100, 250, 45);
        btnKhachHang.setBounds(100, 160, 250, 45);
        btnLogout.setBounds(100, 220, 250, 45);

        add(btnBanHang);
        add(btnXemThuoc);
        add(btnKhachHang);
        add(btnLogout);

        // ======= SỰ KIỆN =======
        // Truyền trực tiếp maNV sang FormDonHang
        btnBanHang.addActionListener(e -> new FormDonHang(maNV).setVisible(true));
        
        btnXemThuoc.addActionListener(e -> new FormThuoc().setVisible(true));
        btnKhachHang.addActionListener(e -> new FormKhachHang().setVisible(true));

        btnLogout.addActionListener(e -> {
            this.dispose();
            new FormLogin().setVisible(true);
        });
    }

    // ======= HÀM MAIN ĐỂ TEST FORM =======
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormMenu_NhanVien("NV01").setVisible(true);
        });
    }
}
