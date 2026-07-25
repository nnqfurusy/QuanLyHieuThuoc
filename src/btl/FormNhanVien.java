package btl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class FormNhanVien extends JFrame {

    private JTable tblNV;
    private DefaultTableModel model;

    private JTextField txtMa, txtTen, txtGT, txtSDT, txtLuong, txtMK;
    private JButton btnThem, btnSua, btnXoa, btnClear, btnLoad;

    public FormNhanVien() {
        setTitle("Quản Lý Nhân Viên");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
        loadData();
    }

    // =====================================================
    //                     GIAO DIỆN
    // =====================================================
    private void initUI() {

        JPanel panelTop = new JPanel(null);
        panelTop.setPreferredSize(new Dimension(900, 250));

        JLabel l1 = new JLabel("Mã NV:");
        JLabel l2 = new JLabel("Tên NV:");
        JLabel l3 = new JLabel("Giới tính:");
        JLabel l4 = new JLabel("SĐT:");
        JLabel l5 = new JLabel("Lương:");
        JLabel l6 = new JLabel("Mật khẩu:");

        txtMa = new JTextField();
        txtTen = new JTextField();
        txtGT = new JTextField();
        txtSDT = new JTextField();
        txtLuong = new JTextField();
        txtMK = new JTextField();

        l1.setBounds(20, 20, 120, 30);  txtMa.setBounds(140, 20, 200, 30);
        l2.setBounds(20, 60, 120, 30);  txtTen.setBounds(140, 60, 200, 30);
        l3.setBounds(20, 100, 120, 30); txtGT.setBounds(140, 100, 200, 30);
        l4.setBounds(20, 140, 120, 30); txtSDT.setBounds(140, 140, 200, 30);
        l5.setBounds(20, 180, 120, 30); txtLuong.setBounds(140, 180, 200, 30);
        l6.setBounds(20, 220, 120, 30); txtMK.setBounds(140, 220, 200, 30);

        btnThem  = new JButton("Thêm");
        btnSua   = new JButton("Sửa");
        btnXoa   = new JButton("Xóa");
        btnClear = new JButton("Clear");
        btnLoad  = new JButton("Tải lại");

        btnThem.setBounds(380, 20, 120, 40);
        btnSua.setBounds(380, 70, 120, 40);
        btnXoa.setBounds(380, 120, 120, 40);
        btnClear.setBounds(380, 170, 120, 40);
        btnLoad.setBounds(380, 220, 120, 40);

        panelTop.add(l1); panelTop.add(txtMa);
        panelTop.add(l2); panelTop.add(txtTen);
        panelTop.add(l3); panelTop.add(txtGT);
        panelTop.add(l4); panelTop.add(txtSDT);
        panelTop.add(l5); panelTop.add(txtLuong);
        panelTop.add(l6); panelTop.add(txtMK);

        panelTop.add(btnThem);
        panelTop.add(btnSua);
        panelTop.add(btnXoa);
        panelTop.add(btnClear);
        panelTop.add(btnLoad);

        add(panelTop, BorderLayout.NORTH);

        // =====================================================
        //                    BẢNG DỮ LIỆU
        // =====================================================
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(new Object[]{
                "Mã NV", "Tên NV", "Giới tính", "SĐT", "Lương", "Mật khẩu"
        });

        tblNV = new JTable(model);
        tblNV.setRowHeight(25);
        tblNV.getTableHeader().setReorderingAllowed(false);

        tblNV.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillForm();
            }
        });

        add(new JScrollPane(tblNV), BorderLayout.CENTER);

        // =====================================================
        //               SỰ KIỆN NÚT BẤM
        // =====================================================
        btnThem.addActionListener(e -> addNV());
        btnSua.addActionListener(e -> editNV());
        btnXoa.addActionListener(e -> deleteNV());
        btnClear.addActionListener(e -> clearForm());
        btnLoad.addActionListener(e -> {
            clearForm();
            loadData();
        });
    }

    // =====================================================
    //                    LOAD DỮ LIỆU
    // =====================================================
    private void loadData() {
        model.setRowCount(0);

        try (Connection conn = DatabaseHelper.connect()) {
            String sql = "SELECT * FROM NHAN_VIEN";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("MaNV"),
                        rs.getString("HoTen"),
                        rs.getString("GioiTinh"),
                        rs.getString("SDT"),
                        rs.getDouble("Luong"),
                        rs.getString("MatKhau")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi load dữ liệu:\n" + e.getMessage());
        }
    }

    // =====================================================
    //               HÀM KIỂM TRA TRÙNG SĐT
    // =====================================================
    private boolean isDuplicateSDT(String sdt, String maNV) {
        if (sdt.isEmpty()) return false;

        String sql = "SELECT COUNT(*) FROM NHAN_VIEN WHERE SDT = ? AND MaNV <> ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sdt);
            ps.setString(2, maNV == null ? "" : maNV); // maNV để bỏ qua chính nó khi bấm Sửa

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =====================================================
    //                     THÊM NV
    // =====================================================
    private void addNV() {
        String maNV = txtMa.getText().trim();
        String tenNV = txtTen.getText().trim();
        String sdt = txtSDT.getText().trim();

        if (maNV.isEmpty() || tenNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã NV và Tên NV!");
            return;
        }

        // Validate SĐT
        if (!sdt.isEmpty()) {
            if (!sdt.matches("^0\\d{9,10}$")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! (Phải bắt đầu bằng 0 và gồm 10-11 chữ số)");
                return;
            }
            if (isDuplicateSDT(sdt, null)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại '" + sdt + "' đã tồn tại ở nhân viên khác!");
                return;
            }
        }

        try (Connection conn = DatabaseHelper.connect()) {
            String sql = "INSERT INTO NHAN_VIEN(MaNV, HoTen, GioiTinh, SDT, Luong, MatKhau) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, maNV);
            ps.setString(2, tenNV);
            ps.setString(3, txtGT.getText().trim());
            ps.setString(4, sdt);

            String rawLuong = txtLuong.getText().replaceAll("[^0-9.]", "").trim();
            double luong = rawLuong.isEmpty() ? 0 : Double.parseDouble(rawLuong);
            ps.setDouble(5, luong);

            ps.setString(6, txtMK.getText().trim());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            clearForm();
            loadData();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương phải là số hợp lệ!");
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627 || e.getMessage().contains("PRIMARY KEY")) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên này đã tồn tại!");
            } else if (e.getMessage().contains("UQ_") || e.getErrorCode() == 2601) {
                JOptionPane.showMessageDialog(this, "Số điện thoại đã bị trùng trong CSDL!");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi SQL thêm nhân viên:\n" + e.getMessage());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm:\n" + e.getMessage());
        }
    }

    // =====================================================
    //                     SỬA NV
    // =====================================================
    private void editNV() {
        String maNV = txtMa.getText().trim();
        String sdt = txtSDT.getText().trim();

        if (maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa từ bảng!");
            return;
        }

        // Validate SĐT
        if (!sdt.isEmpty()) {
            if (!sdt.matches("^0\\d{9,10}$")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! (Phải bắt đầu bằng 0 và gồm 10-11 chữ số)");
                return;
            }
            // Truyền maNV vào để không bị trùng với chính nhân viên đang sửa
            if (isDuplicateSDT(sdt, maNV)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại '" + sdt + "' đã tồn tại ở nhân viên khác!");
                return;
            }
        }

        try (Connection conn = DatabaseHelper.connect()) {
            String sql = "UPDATE NHAN_VIEN SET HoTen=?, GioiTinh=?, SDT=?, Luong=?, MatKhau=? WHERE MaNV=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtTen.getText().trim());
            ps.setString(2, txtGT.getText().trim());
            ps.setString(3, sdt);

            String rawLuong = txtLuong.getText().replaceAll("[^0-9.]", "").trim();
            double luong = rawLuong.isEmpty() ? 0 : Double.parseDouble(rawLuong);
            ps.setDouble(4, luong);

            ps.setString(5, txtMK.getText().trim());
            ps.setString(6, maNV);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Sửa nhân viên thành công!");
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy Mã NV '" + maNV + "' để sửa!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương phải là số hợp lệ!");
        } catch (SQLException e) {
            if (e.getMessage().contains("UQ_") || e.getErrorCode() == 2601) {
                JOptionPane.showMessageDialog(this, "Số điện thoại đã bị trùng trong CSDL!");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi SQL sửa nhân viên:\n" + e.getMessage());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa:\n" + e.getMessage());
        }
    }

    // =====================================================
    //                     XOÁ NV
    // =====================================================
    // =====================================================
//                     XOÁ NV
// =====================================================
    private void deleteNV() {
        String maNV = txtMa.getText().trim();

        if (maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên trên bảng hoặc nhập Mã NV để xóa!");
            return;
        }

        // --- CHẶN XÓA TÀI KHOẢN ADMIN HỆ THỐNG ---
        if (maNV.equalsIgnoreCase("ADMIN01") || maNV.equalsIgnoreCase("ADMIN")) {
            JOptionPane.showMessageDialog(this, 
                    "Không thể xóa tài khoản Quản trị viên (Admin) của hệ thống!", 
                    "Cảnh báo bảo mật", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa nhân viên có mã: " + maNV + "?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DatabaseHelper.connect()) {
            String sql = "DELETE FROM NHAN_VIEN WHERE MaNV=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maNV);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy Mã NV '" + maNV + "' trong cơ sở dữ liệu!");
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 547 || e.getMessage().contains("REFERENCE constraint")) {
                JOptionPane.showMessageDialog(this, "Không thể xóa! Nhân viên này đã lập Hóa đơn / Đơn hàng trong hệ thống.");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi SQL xóa:\n" + e.getMessage());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa:\n" + e.getMessage());
        }
    }

    // =====================================================
    //                    CLEAR FORM
    // =====================================================
    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtGT.setText("");
        txtSDT.setText("");
        txtLuong.setText("");
        txtMK.setText("");
        tblNV.clearSelection();
    }

    // =====================================================
    //                    FILL FORM
    // =====================================================
    private void fillForm() {
        int r = tblNV.getSelectedRow();
        if (r < 0) return;

        txtMa.setText(model.getValueAt(r, 0) != null ? model.getValueAt(r, 0).toString() : "");
        txtTen.setText(model.getValueAt(r, 1) != null ? model.getValueAt(r, 1).toString() : "");
        txtGT.setText(model.getValueAt(r, 2) != null ? model.getValueAt(r, 2).toString() : "");
        txtSDT.setText(model.getValueAt(r, 3) != null ? model.getValueAt(r, 3).toString() : "");
        
        // Hiển thị Lương dưới dạng số chuẩn (tránh hiện kiểu Sci-notation như 1.5E7)
        if (model.getValueAt(r, 4) != null) {
            try {
                double l = Double.parseDouble(model.getValueAt(r, 4).toString());
                txtLuong.setText(String.format("%.0f", l));
            } catch (Exception e) {
                txtLuong.setText(model.getValueAt(r, 4).toString());
            }
        } else {
            txtLuong.setText("");
        }

        txtMK.setText(model.getValueAt(r, 5) != null ? model.getValueAt(r, 5).toString() : "");
    }

    // =====================================================
    //                    MAIN TEST
    // =====================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormNhanVien().setVisible(true));
    }
}
