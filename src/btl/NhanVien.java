package btl_csdl;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class NhanVien extends JFrame {

    Connection conn;
    DefaultTableModel model;

    JTextField txtMa, txtTen, txtChucVu, txtLuong, txtSDT;
    JPasswordField txtPass;
    JTable table;

    public NhanVien() {
        setTitle("Quản lý Nhân viên");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // ===== KẾT NỐI SQL SERVER =====
        try {
            conn = DatabaseHelper.connect();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể kết nối SQL Server");
        }

        JLabel l1 = new JLabel("Mã NV:");
        JLabel l2 = new JLabel("Họ Tên:");
        JLabel l3 = new JLabel("Chức vụ:");
        JLabel l4 = new JLabel("Lương:");
        JLabel l5 = new JLabel("Mật khẩu:");
        JLabel l6 = new JLabel("SĐT:");

        txtMa = new JTextField();
        txtTen = new JTextField();
        txtChucVu = new JTextField();
        txtLuong = new JTextField();
        txtPass = new JPasswordField();
        txtSDT = new JTextField();

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnTim = new JButton("Tìm kiếm");
        JButton btnList = new JButton("Liệt kê");

        l1.setBounds(30, 20, 100, 30);
        l2.setBounds(30, 60, 100, 30);
        l3.setBounds(30, 100, 100, 30);
        l4.setBounds(400, 20, 100, 30);
        l5.setBounds(400, 60, 100, 30);
        l6.setBounds(400, 100, 100, 30);

        txtMa.setBounds(120, 20, 200, 30);
        txtTen.setBounds(120, 60, 200, 30);
        txtChucVu.setBounds(120, 100, 200, 30);
        txtLuong.setBounds(500, 20, 200, 30);
        txtPass.setBounds(500, 60, 200, 30);
        txtSDT.setBounds(500, 100, 200, 30);

        btnThem.setBounds(120, 150, 100, 30);
        btnSua.setBounds(230, 150, 100, 30);
        btnXoa.setBounds(340, 150, 100, 30);
        btnTim.setBounds(450, 150, 120, 30);
        btnList.setBounds(580, 150, 120, 30);

        add(l1); add(l2); add(l3); add(l4); add(l5); add(l6);
        add(txtMa); add(txtTen); add(txtChucVu); add(txtLuong); add(txtPass); add(txtSDT);
        add(btnThem); add(btnSua); add(btnXoa); add(btnTim); add(btnList);

        model = new DefaultTableModel();
        model.addColumn("Mã NV");
        model.addColumn("Họ Tên");
        model.addColumn("Chức vụ");
        model.addColumn("Lương");
        model.addColumn("SĐT");

        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(30, 200, 740, 230);
        add(sp);

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnTim.addActionListener(e -> tim());
        btnList.addActionListener(e -> loadData());

        loadData();
    }

    void loadData() {
        model.setRowCount(0);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT MaNV, HoTen, ChucVu, Luong, SDT FROM NhanVien");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("MaNV"),
                        rs.getString("HoTen"),
                        rs.getString("ChucVu"),
                        rs.getDouble("Luong"),
                        rs.getString("SDT")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void them() {
        try {
            String sql = "INSERT INTO NhanVien(MaNV, HoTen, ChucVu, Luong, MatKhau, SDT) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtMa.getText());
            ps.setString(2, txtTen.getText());
            ps.setString(3, txtChucVu.getText());
            ps.setDouble(4, Double.parseDouble(txtLuong.getText()));
            ps.setString(5, new String(txtPass.getPassword()));
            ps.setString(6, txtSDT.getText());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sua() {
        try {
            String sql = "UPDATE NhanVien SET HoTen=?, ChucVu=?, Luong=?, MatKhau=?, SDT=? WHERE MaNV=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtTen.getText());
            ps.setString(2, txtChucVu.getText());
            ps.setDouble(3, Double.parseDouble(txtLuong.getText()));
            ps.setString(4, new String(txtPass.getPassword()));
            ps.setString(5, txtSDT.getText());
            ps.setString(6, txtMa.getText());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sửa thành công!");
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void xoa() {
        try {
            String sql = "DELETE FROM NhanVien WHERE MaNV=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtMa.getText());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void tim() {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM NhanVien WHERE MaNV=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtMa.getText());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("MaNV"),
                        rs.getString("HoTen"),
                        rs.getString("ChucVu"),
                        rs.getDouble("Luong"),
                        rs.getString("SDT")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new NhanVien().setVisible(true);
    }
}
