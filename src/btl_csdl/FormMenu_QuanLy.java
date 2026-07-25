package btl_csdl;

import javax.swing.*;
import java.awt.*;

public class FormMenu_QuanLy extends JFrame {

    private JButton btnThuoc, btnNhanVien, btnKhachHang, btnDonHang, btnBaoCaoTon, btnBaoCaoDoanhThu, btnLogout;

    // Biến lưu mã nhân viên/quản lý đang đăng nhập
    private String maNV;

    // CONSTRUCTOR NHẬN MÃ NV TỪ FORMLOGIN
    public FormMenu_QuanLy(String maNV) {
        this.maNV = maNV;

        setTitle("Menu Quản Lý - [" + maNV + "]");
        setSize(500, 480); // Tăng chiều cao lên 480 để vừa thêm 1 nút
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    // CONSTRUCTOR MẶC ĐỊNH (TEST ĐỘC LẬP)
    public FormMenu_QuanLy() {
        this("ADMIN01");
    }

    // =============================== GIAO DIỆN ===============================
    private void initUI() {

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(500, 480));

        btnThuoc = new JButton("Quản lý Thuốc");
        btnNhanVien = new JButton("Quản lý Nhân viên");
        btnKhachHang = new JButton("Quản lý Khách hàng"); // Nút mới bổ sung
        btnDonHang = new JButton("Quản lý Đơn hàng");
        btnBaoCaoTon = new JButton("Báo cáo Tồn kho");
        btnBaoCaoDoanhThu = new JButton("Báo cáo Doanh thu");
        btnLogout = new JButton("Đăng xuất");

        // Căn chỉnh vị trí các nút theo thứ tự
        btnThuoc.setBounds(150, 20, 200, 45);
        btnNhanVien.setBounds(150, 80, 200, 45);
        btnKhachHang.setBounds(150, 140, 200, 45);
        btnDonHang.setBounds(150, 200, 200, 45);
        btnBaoCaoTon.setBounds(150, 260, 200, 45);
        btnBaoCaoDoanhThu.setBounds(150, 320, 200, 45);
        btnLogout.setBounds(150, 380, 200, 45);

        panel.add(btnThuoc);
        panel.add(btnNhanVien);
        panel.add(btnKhachHang);
        panel.add(btnDonHang);
        panel.add(btnBaoCaoTon);
        panel.add(btnBaoCaoDoanhThu);
        panel.add(btnLogout);

        add(panel, BorderLayout.CENTER);

        // ========================= SỰ KIỆN NÚT =========================

        btnThuoc.addActionListener(e -> new FormThuoc().setVisible(true));
        btnNhanVien.addActionListener(e -> new FormNhanVien().setVisible(true));
        btnKhachHang.addActionListener(e -> new FormKhachHang().setVisible(true)); // Mở Form Khách Hàng
        
        // Truyền chính xác maNV của Quản lý sang FormDonHang
        btnDonHang.addActionListener(e -> new FormDonHang(maNV).setVisible(true));
        
        btnBaoCaoTon.addActionListener(e -> new FormBaoCaoTonKho().setVisible(true));
        btnBaoCaoDoanhThu.addActionListener(e -> new FormBaoCaoDoanhThu().setVisible(true));

        btnLogout.addActionListener(e -> {
            dispose();
            new FormLogin().setVisible(true);
        });
    }

    // ============================== MAIN TEST ==============================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormMenu_QuanLy("ADMIN01").setVisible(true));
    }
}