package btl_csdl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FormKhachHang extends JFrame {

    private JTable tbl;
    private DefaultTableModel model;

    private JTextField txtMa, txtTen, txtGT, txtSDT, txtDiaChi, txtNgaySinh;
    private JTextArea txtGhiChu;

    private JButton btnThem, btnSua, btnXoa, btnClear, btnLoad;

    public FormKhachHang() {
        setTitle("Quản Lý Khách Hàng");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
        loadData();
    }

    private void initUI() {

        JPanel panelTop = new JPanel(null);
        panelTop.setPreferredSize(new Dimension(900, 250));

        JLabel l1 = new JLabel("Mã KH:");
        JLabel l2 = new JLabel("Tên KH:");
        JLabel l3 = new JLabel("Giới tính:");
        JLabel l4 = new JLabel("SĐT:");
        JLabel l5 = new JLabel("Địa chỉ:");
        JLabel l6 = new JLabel("Ngày sinh (yyyy-MM-dd):");
        JLabel l7 = new JLabel("Ghi chú sức khoẻ:");

        txtMa = new JTextField();
        txtTen = new JTextField();
        txtGT = new JTextField();
        txtSDT = new JTextField();
        txtDiaChi = new JTextField();
        txtNgaySinh = new JTextField();
        txtGhiChu = new JTextArea();

        JScrollPane spNote = new JScrollPane(txtGhiChu);

        btnThem  = new JButton("Thêm");
        btnSua   = new JButton("Sửa");
        btnXoa   = new JButton("Xoá");
        btnClear = new JButton("Clear");
        btnLoad  = new JButton("Tải lại");

        // -------- Layout ----------
        l1.setBounds(20, 20, 120, 30);    txtMa.setBounds(150, 20, 200, 30);
        l2.setBounds(20, 60, 120, 30);    txtTen.setBounds(150, 60, 200, 30);
        l3.setBounds(20, 100, 120, 30);   txtGT.setBounds(150, 100, 200, 30);
        l4.setBounds(20, 140, 120, 30);   txtSDT.setBounds(150, 140, 200, 30);
        l5.setBounds(20, 180, 120, 30);   txtDiaChi.setBounds(150, 180, 200, 30);

        l6.setBounds(400, 20, 200, 30);   txtNgaySinh.setBounds(620, 20, 200, 30);
        l7.setBounds(400, 60, 150, 30);   spNote.setBounds(400, 90, 420, 120);

        btnThem.setBounds(20, 220, 100, 35);
        btnSua.setBounds(140, 220, 100, 35);
        btnXoa.setBounds(260, 220, 100, 35);
        btnClear.setBounds(380, 220, 100, 35);
        btnLoad.setBounds(500, 220, 100, 35);

        panelTop.add(l1); panelTop.add(txtMa);
        panelTop.add(l2); panelTop.add(txtTen);
        panelTop.add(l3); panelTop.add(txtGT);
        panelTop.add(l4); panelTop.add(txtSDT);
        panelTop.add(l5); panelTop.add(txtDiaChi);

        panelTop.add(l6); panelTop.add(txtNgaySinh);
        panelTop.add(l7); panelTop.add(spNote);

        panelTop.add(btnThem);
        panelTop.add(btnSua);
        panelTop.add(btnXoa);
        panelTop.add(btnClear);
        panelTop.add(btnLoad);

        add(panelTop, BorderLayout.NORTH);

        // =====================================================
        //                    BẢNG DỮ LIỆU
        // =====================================================
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
                "Mã KH", "Tên KH", "Giới tính", "Ngày sinh",
                "Địa chỉ", "SĐT", "Ghi chú"
        });

        tbl = new JTable(model);
        tbl.setRowHeight(25);
        tbl.getTableHeader().setReorderingAllowed(false);

        tbl.getSelectionModel().addListSelectionListener(e -> fillForm());

        add(new JScrollPane(tbl), BorderLayout.CENTER);

        // =====================================================
        //                     SỰ KIỆN BUTTON
        // =====================================================
        btnThem.addActionListener(e -> addKH());
        btnSua.addActionListener(e -> editKH());
        btnXoa.addActionListener(e -> deleteKH());
        btnClear.addActionListener(e -> clearForm());
        btnLoad.addActionListener(e -> loadData());
    }

    // =====================================================
    //                   HÀM KIỂM TRA DỮ LIỆU (VALIDATION)
    // =====================================================
    private boolean validateInput(boolean isEdit) {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        String ngaySinh = txtNgaySinh.getText().trim();

        // 1. Kiểm tra rỗng bắt buộc
        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã KH và Tên KH không được để trống!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 2. Validate định dạng Mã KH (VD: KH01, KH001, KH10)
        if (!ma.matches("^KH\\d+$")) {
            JOptionPane.showMessageDialog(this, "Mã KH phải có định dạng 'KH' theo sau là chữ số (VD: KH01, KH02)!", "Lỗi định dạng", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 3. Validate SĐT (Nếu nhập thì phải là 10 số bắt đầu bằng 0)
        if (!sdt.isEmpty() && !sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! SĐT phải gồm 10 chữ số và bắt đầu bằng 0.", "Lỗi định dạng", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 4. Validate Ngày sinh (định dạng yyyy-MM-dd)
        if (!ngaySinh.isEmpty()) {
            if (!ngaySinh.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                JOptionPane.showMessageDialog(this, "Ngày sinh phải đúng định dạng yyyy-MM-dd (VD: 1995-10-25)!", "Lỗi định dạng", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Bắt lỗi ngày không tồn tại như 2023-02-31
            try {
                sdf.parse(ngaySinh);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ về mặt thời gian!", "Lỗi định dạng", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        // 5. Kiểm tra trùng lặp trong Database
        try (Connection c = DatabaseHelper.connect()) {
            if (!isEdit) { // Khi THÊM MỚI
                // Check trùng Mã KH
                String checkMa = "SELECT COUNT(*) FROM KHACH_HANG WHERE MaKH = ?";
                PreparedStatement ps1 = c.prepareStatement(checkMa);
                ps1.setString(1, ma);
                ResultSet rs1 = ps1.executeQuery();
                if (rs1.next() && rs1.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Mã KH '" + ma + "' đã tồn tại!", "Trùng dữ liệu", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                // Check trùng SĐT
                if (!sdt.isEmpty()) {
                    String checkSDT = "SELECT COUNT(*) FROM KHACH_HANG WHERE SDT = ?";
                    PreparedStatement ps2 = c.prepareStatement(checkSDT);
                    ps2.setString(1, sdt);
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next() && rs2.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "Số điện thoại '" + sdt + "' đã thuộc về khách hàng khác!", "Trùng dữ liệu", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            } else { // Khi SỬA
                // Check trùng SĐT với KH khác (ngoại trừ bản ghi hiện tại)
                if (!sdt.isEmpty()) {
                    String checkSDT = "SELECT COUNT(*) FROM KHACH_HANG WHERE SDT = ? AND MaKH <> ?";
                    PreparedStatement ps2 = c.prepareStatement(checkSDT);
                    ps2.setString(1, sdt);
                    ps2.setString(2, ma);
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next() && rs2.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, "Số điện thoại '" + sdt + "' đã trùng với khách hàng khác!", "Trùng dữ liệu", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kiểm tra dữ liệu: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // =====================================================
    //                        LOAD DATA
    // =====================================================
    private void loadData() {
        model.setRowCount(0);

        try (Connection conn = DatabaseHelper.connect()) {
            String sql = "SELECT MaKH, HoTen, NgaySinh, GioiTinh, DiaChi, SDT, GhiChuSucKhoe FROM KHACH_HANG";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("MaKH"),
                        rs.getString("HoTen"),
                        rs.getString("GioiTinh"),
                        rs.getString("NgaySinh"),
                        rs.getString("DiaChi"),
                        rs.getString("SDT"),
                        rs.getString("GhiChuSucKhoe")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải dữ liệu:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================
    //                      THÊM KH
    // =====================================================
    private void addKH() {
        if (!validateInput(false)) return;

        String sql = "INSERT INTO KHACH_HANG (MaKH, HoTen, NgaySinh, GioiTinh, DiaChi, SDT, GhiChuSucKhoe) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = DatabaseHelper.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, txtMa.getText().trim());
            ps.setString(2, txtTen.getText().trim());

            String ngaySinh = txtNgaySinh.getText().trim();
            if (ngaySinh.isEmpty()) {
                ps.setNull(3, Types.DATE);
            } else {
                ps.setString(3, ngaySinh);
            }

            ps.setString(4, txtGT.getText().trim());
            ps.setString(5, txtDiaChi.getText().trim());
            ps.setString(6, txtSDT.getText().trim());
            ps.setString(7, txtGhiChu.getText().trim());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            loadData();
            clearForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi thêm khách hàng:\n" + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================
    //                      SỬA KH
    // =====================================================
    private void editKH() {
        if (!validateInput(true)) return;

        String sql = "UPDATE KHACH_HANG SET HoTen=?, NgaySinh=?, GioiTinh=?, DiaChi=?, SDT=?, GhiChuSucKhoe=? WHERE MaKH=?";

        try (Connection c = DatabaseHelper.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, txtTen.getText().trim());

            String ngaySinh = txtNgaySinh.getText().trim();
            if (ngaySinh.isEmpty()) {
                ps.setNull(2, Types.DATE);
            } else {
                ps.setString(2, ngaySinh);
            }

            ps.setString(3, txtGT.getText().trim());
            ps.setString(4, txtDiaChi.getText().trim());
            ps.setString(5, txtSDT.getText().trim());
            ps.setString(6, txtGhiChu.getText().trim());
            ps.setString(7, txtMa.getText().trim());

            int updated = ps.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Sửa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy Mã KH này!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi sửa khách hàng:\n" + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================
    //                      XOÁ KH
    // =====================================================
    private void deleteKH() {
        if (txtMa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa khách hàng " + txtMa.getText() + " không?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection c = DatabaseHelper.connect()) {
            String sql = "DELETE FROM KHACH_HANG WHERE MaKH=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, txtMa.getText().trim());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Xoá thành công!");
            loadData();
            clearForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi xoá:\n" + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtGT.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtNgaySinh.setText("");
        txtGhiChu.setText("");
        tbl.clearSelection();
    }

    private void fillForm() {
        int r = tbl.getSelectedRow();
        if (r < 0) return;

        txtMa.setText(getValueOrEmpty(r, 0));
        txtTen.setText(getValueOrEmpty(r, 1));
        txtGT.setText(getValueOrEmpty(r, 2));
        txtNgaySinh.setText(getValueOrEmpty(r, 3));
        txtDiaChi.setText(getValueOrEmpty(r, 4));
        txtSDT.setText(getValueOrEmpty(r, 5));
        txtGhiChu.setText(getValueOrEmpty(r, 6));
    }

    private String getValueOrEmpty(int row, int col) {
        Object val = model.getValueAt(row, col);
        return val == null ? "" : val.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormKhachHang().setVisible(true));
    }
}