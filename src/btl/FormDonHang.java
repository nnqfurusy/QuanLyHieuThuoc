package btl_csdl;

import javax.swing.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.Color;

public class FormDonHang extends JFrame {

    private JTextField txtMaDH, txtNgay, txtNhanVien;
    private JComboBox<String> cbbKH;
    private JButton btnTaoDH, btnXemCT;
    
    // Lưu mã nhân viên đang đăng nhập hệ thống
    private String maNVHienTai;

    // Constructor nhận vào mã nhân viên đã đăng nhập (Ví dụ: "NV01")
    public FormDonHang(String maNV) {
        this.maNVHienTai = maNV;

        setTitle("Tạo Đơn Hàng");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        init();
        loadKH();
    }

    // Constructor mặc định cho việc test độc lập (default giả định NV01)
    public FormDonHang() {
        this("NV01");
    }

    private void init() {

        JLabel a = new JLabel("Mã đơn:");
        JLabel b = new JLabel("Nhân viên:");
        JLabel c = new JLabel("Khách hàng:");
        JLabel d = new JLabel("Ngày lập:");

        txtMaDH = new JTextField();
        txtNgay = new JTextField();

        // Ô Nhân viên hiển thị tài khoản đang đăng nhập & không cho phép chỉnh sửa
        txtNhanVien = new JTextField(maNVHienTai);
        txtNhanVien.setEditable(false);
        txtNhanVien.setBackground(new Color(240, 240, 240));

        cbbKH = new JComboBox<>();

        btnTaoDH = new JButton("Tạo đơn");
        btnXemCT = new JButton("Chi tiết đơn");

        // Mở form chi tiết đơn
        btnXemCT.addActionListener(x -> new FormChiTietDon().setVisible(true));

        // Lệnh tạo đơn hàng
        btnTaoDH.addActionListener(x -> taoDH());

        setLayout(null);

        a.setBounds(20, 20, 120, 30);
        txtMaDH.setBounds(140, 20, 200, 30);

        b.setBounds(20, 60, 120, 30);
        txtNhanVien.setBounds(140, 60, 200, 30);

        c.setBounds(20, 100, 120, 30);
        cbbKH.setBounds(140, 100, 200, 30);

        d.setBounds(20, 140, 120, 30);
        txtNgay.setBounds(140, 140, 200, 30);

        btnTaoDH.setBounds(40, 195, 120, 40);
        btnXemCT.setBounds(200, 195, 140, 40);

        add(a); add(txtMaDH);
        add(b); add(txtNhanVien);
        add(c); add(cbbKH);
        add(d); add(txtNgay);
        add(btnTaoDH); add(btnXemCT);
    }

    // ==================================================
    //                  LOAD KHÁCH HÀNG
    // ==================================================
    private void loadKH() {
        try (Connection c = DatabaseHelper.connect()) {

            PreparedStatement ps = c.prepareStatement("SELECT MaKH FROM KHACH_HANG");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) cbbKH.addItem(rs.getString(1));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải khách hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================================================
    //                   TẠO ĐƠN HÀNG
    // ==================================================
    private void taoDH() {
        String maDH = txtMaDH.getText().trim();
        String ngayLap = txtNgay.getText().trim();

        // 1. Kiểm tra rỗng
        if (maDH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã đơn hàng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtMaDH.requestFocus();
            return;
        }

        if (ngayLap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Ngày lập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtNgay.requestFocus();
            return;
        }

        // 2. Validation định dạng Mã đơn
        if (!maDH.matches("^[a-zA-Z0-9]+$")) {
            JOptionPane.showMessageDialog(this, "Mã đơn hàng chỉ bao gồm chữ cái và chữ số, không được chứa ký tự đặc biệt!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtMaDH.requestFocus();
            return;
        }

        // 3. Validation định dạng Ngày lập (yyyy-MM-dd)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(ngayLap);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày lập không đúng định dạng chuẩn (yyyy-MM-dd)!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtNgay.requestFocus();
            return;
        }

        // 4. Kiểm tra ComboBox Khách hàng
        if (cbbKH.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Khách hàng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 5. Thêm dữ liệu vào Database
        try (Connection c = DatabaseHelper.connect()) {

            String sql = "INSERT INTO DON_HANG(MaDH, NgayLap, TrangThai, TongTien, MaNV, MaKH) "
                       + "VALUES(?,?,?,?,?,?)";

            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, maDH);
            ps.setString(2, ngayLap);
            ps.setString(3, "Chưa thanh toán");
            ps.setDouble(4, 0); // Mặc định khởi tạo tổng tiền = 0
            ps.setString(5, maNVHienTai); // Tự động lấy MaNV đang đăng nhập
            ps.setString(6, cbbKH.getSelectedItem().toString());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Tạo đơn hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
                JOptionPane.showMessageDialog(this, "Mã đơn hàng '" + maDH + "' đã tồn tại trong hệ thống!", "Lỗi trùng dữ liệu", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi thực thi CSDL: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================================================
    //                  CHẠY ĐƠN HÀNG
    // ==================================================
    public static void main(String[] args) {
        new FormDonHang("NV02").setVisible(true);
    }
}