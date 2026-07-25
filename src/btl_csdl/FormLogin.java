package btl_csdl;

import javax.swing.*;
import java.sql.*;

public class FormLogin extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin, btnCancel;

    public FormLogin() {
        setTitle("Đăng nhập hệ thống");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        setResizable(false);
    }

    private void initComponents() {

        JLabel lblUser = new JLabel("Mã nhân viên:");
        JLabel lblPass = new JLabel("Mật khẩu:");

        txtUser = new JTextField();
        txtPass = new JPasswordField();

        btnLogin = new JButton("Đăng nhập");
        btnCancel = new JButton("Thoát");

        btnLogin.addActionListener(e -> login());
        btnCancel.addActionListener(e -> System.exit(0));

        setLayout(null);

        lblUser.setBounds(30, 30, 120, 30);
        lblPass.setBounds(30, 80, 120, 30);

        txtUser.setBounds(150, 30, 150, 30);
        txtPass.setBounds(150, 80, 150, 30);

        btnLogin.setBounds(50, 140, 100, 35);
        btnCancel.setBounds(180, 140, 100, 35);

        add(lblUser);
        add(lblPass);
        add(txtUser);
        add(txtPass);
        add(btnLogin);
        add(btnCancel);
    }

    private void login() {

        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DatabaseHelper.connect();

            String sql = "SELECT HoTen, MatKhau, VaiTro FROM NHAN_VIEN WHERE MaNV = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String matKhauDB = rs.getString("MatKhau");
                String role = rs.getString("VaiTro");

                if (role != null) {
                    role = role.trim(); // Xóa khoảng trắng thừa từ DB (nếu có)
                }

                if (pass.equals(matKhauDB)) {

                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                    this.dispose(); // Tắt form login

                    // CHIA QUYỀN VÀ TRUYỀN MÃ NHÂN VIÊN ĐĂNG NHẬP (user)
                    if (role != null && (role.equalsIgnoreCase("QuanLy") 
                                      || role.equalsIgnoreCase("Admin") 
                                      || role.equalsIgnoreCase("Quản lý"))) {
                        
                        // Mở Menu Quản Lý và truyền mã Quản lý vào
                        new FormMenu_QuanLy(user).setVisible(true);
                    } 
                    else {
                        // Mở Menu Nhân Viên và truyền mã Nhân viên vào
                        new FormMenu_NhanVien(user).setVisible(true); 
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Sai mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Mã nhân viên không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new FormLogin().setVisible(true);
    }
}