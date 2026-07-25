package btl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormChiTietDon extends JFrame {

    // Bảng giỏ hàng tạm
    private JTable tblGioHang;
    private DefaultTableModel modelGioHang;

    // Bảng hiển thị đơn hàng đã lưu trong DB
    private JTable tblDaLuu;
    private DefaultTableModel modelDaLuu;

    private JComboBox<String> cbbDH, cbbThuoc;
    private JTextField txtSL, txtGia, txtLieu;

    private JButton btnThemGio, btnXoaDong, btnLuuDB, btnClear, btnXoaDB, btnXuatHD;

    public FormChiTietDon() {
        setTitle("Chi tiết đơn hàng - Quản lý giỏ hàng & Đơn đã lưu");
        setSize(950, 720); // Mở rộng chiều cao chút để xếp vừa nút Xuất hóa đơn
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        loadDonHang();
        loadThuoc();

        // 1. Chuyển Mã đơn hàng -> Tự động load bảng dữ liệu đã lưu tương ứng
        cbbDH.addActionListener(e -> loadDataDaLuu());

        // 2. Chuyển Mã thuốc -> Tự động lấy Đơn giá từ CSDL điền vào ô Đơn giá
        cbbThuoc.addActionListener(e -> updateDonGiaTuDB());
    }

    // =========================== GIAO DIỆN ===========================
    private void initComponents() {

        JPanel panelTop = new JPanel(null);
        panelTop.setPreferredSize(new Dimension(930, 240));

        JLabel a = new JLabel("Mã đơn hàng:");
        JLabel b = new JLabel("Mã thuốc:");
        JLabel c = new JLabel("Số lượng:");
        JLabel d = new JLabel("Đơn giá (Tự động):");
        JLabel e = new JLabel("Liều dùng:");

        cbbDH = new JComboBox<>();
        cbbThuoc = new JComboBox<>();

        txtSL = new JTextField();
        txtGia = new JTextField();
        txtGia.setEditable(false); // CHỈ ĐỌC: Không cho nhập tay đơn giá
        txtGia.setBackground(new Color(240, 240, 240));

        txtLieu = new JTextField();

        btnThemGio = new JButton("Thêm vào giỏ");
        btnXoaDong = new JButton("Xóa dòng giỏ");
        btnLuuDB = new JButton("Lưu Đơn Hàng");
        btnClear = new JButton("Làm mới ô nhập");
        btnXoaDB = new JButton("Xóa khỏi DB");

        // NÚT XUẤT HÓA ĐƠN MỚI
        btnXuatHD = new JButton("🧾 Xuất Hóa Đơn");
        btnXuatHD.setBackground(new Color(23, 162, 184));
        btnXuatHD.setForeground(Color.WHITE);
        btnXuatHD.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Gán sự kiện cho nút bấm
        btnThemGio.addActionListener(x -> themVaoGio());
        btnXoaDong.addActionListener(x -> xoaDongTrongGio());
        btnLuuDB.addActionListener(x -> luuVaoDatabase());
        btnClear.addActionListener(x -> clearInput());
        btnXoaDB.addActionListener(x -> xoaChitietTrongDB());
        btnXuatHD.addActionListener(x -> xuatHoaDon());

        // Định vị các ô nhập
        a.setBounds(20, 15, 110, 30);   cbbDH.setBounds(140, 15, 210, 30);
        b.setBounds(20, 50, 110, 30);   cbbThuoc.setBounds(140, 50, 210, 30);
        c.setBounds(20, 85, 110, 30);   txtSL.setBounds(140, 85, 210, 30);
        d.setBounds(20, 120, 110, 30);  txtGia.setBounds(140, 120, 210, 30);
        e.setBounds(20, 155, 110, 30);  txtLieu.setBounds(140, 155, 210, 30);

        // Nút chức năng phía bên phải
        btnThemGio.setBounds(380, 15, 180, 32);
        btnXoaDong.setBounds(380, 52, 180, 32);
        btnClear.setBounds(380, 89, 180, 32);

        btnXoaDB.setBounds(380, 126, 180, 32);
        btnXoaDB.setForeground(Color.RED);

        // Nút "Lưu Đơn Hàng"
        btnLuuDB.setBounds(380, 163, 180, 32);
        btnLuuDB.setBackground(new Color(40, 167, 69));
        btnLuuDB.setForeground(Color.WHITE);
        btnLuuDB.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Nút "Xuất Hóa Đơn"
        btnXuatHD.setBounds(380, 200, 180, 32);

        panelTop.add(a);  panelTop.add(cbbDH);
        panelTop.add(b);  panelTop.add(cbbThuoc);
        panelTop.add(c);  panelTop.add(txtSL);
        panelTop.add(d);  panelTop.add(txtGia);
        panelTop.add(e);  panelTop.add(txtLieu);

        panelTop.add(btnThemGio);
        panelTop.add(btnXoaDong);
        panelTop.add(btnClear);
        panelTop.add(btnXoaDB);
        panelTop.add(btnLuuDB);
        panelTop.add(btnXuatHD);

        // ====== 1. BẢNG GIỎ HÀNG TẠM (Chưa lưu DB) ======
        modelGioHang = new DefaultTableModel();
        modelGioHang.setColumnIdentifiers(new Object[]{
                "Mã Thuốc", "Số lượng", "Đơn giá", "Thành tiền", "Liều dùng"
        });
        tblGioHang = new JTable(modelGioHang);
        tblGioHang.setRowHeight(22);

        JScrollPane spGioHang = new JScrollPane(tblGioHang);
        spGioHang.setBorder(BorderFactory.createTitledBorder("🛒 Giỏ hàng tạm (Chuẩn bị lưu)"));

        // ====== 2. BẢNG ĐƠN HÀNG ĐÃ LƯU TRONG DATABASE ======
        modelDaLuu = new DefaultTableModel();
        modelDaLuu.setColumnIdentifiers(new Object[]{
                "Mã Đơn", "Mã Thuốc", "Số lượng", "Đơn giá", "Thành tiền", "Liều dùng"
        });
        tblDaLuu = new JTable(modelDaLuu);
        tblDaLuu.setRowHeight(22);

        JScrollPane spDaLuu = new JScrollPane(tblDaLuu);
        spDaLuu.setBorder(BorderFactory.createTitledBorder("💾 Chi tiết đơn hàng ĐÃ LƯU trong CSDL"));

        // Chia đôi màn hình hiển thị 2 bảng bằng JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spGioHang, spDaLuu);
        splitPane.setResizeWeight(0.5);

        add(panelTop, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    // =========================== XỬ LÝ NÚT XUẤT HÓA ĐƠN ===========================
    private void xuatHoaDon() {
        if (cbbDH.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Mã đơn hàng cần xuất hóa đơn!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (modelDaLuu.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Đơn hàng này chưa có dữ liệu trong CSDL! Hãy lưu giỏ hàng trước khi xuất hóa đơn.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maDH = cbbDH.getSelectedItem().toString();
        // Mở cửa sổ Xem / In hóa đơn
        new FormHoaDon(maDH).setVisible(true);
    }

    // =========================== CẬP NHẬT ĐƠN GIÁ TỰ ĐỘNG TỪ DB ===========================
    private void updateDonGiaTuDB() {
        if (cbbThuoc.getSelectedItem() == null) {
            txtGia.setText("");
            return;
        }

        String maThuoc = cbbThuoc.getSelectedItem().toString();
        String sql = "SELECT GiaBan FROM THUOC WHERE MaThuoc = ?"; 

        try (Connection c = DatabaseHelper.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maThuoc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtGia.setText(String.valueOf(rs.getDouble(1)));
            } else {
                txtGia.setText("0");
            }
        } catch (Exception e) {
            txtGia.setText("0");
        }
    }

    // =========================== 1. THÊM VÀO GIỎ HÀNG TẠM ===========================
    private void themVaoGio() {
        if (cbbThuoc.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thuốc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String maThuoc = cbbThuoc.getSelectedItem().toString();
            int soLuong = Integer.parseInt(txtSL.getText().trim());
            double gia = Double.parseDouble(txtGia.getText().trim());
            String lieuDung = txtLieu.getText().trim();

            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải > 0!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < modelGioHang.getRowCount(); i++) {
                if (modelGioHang.getValueAt(i, 0).toString().equals(maThuoc)) {
                    int slCu = Integer.parseInt(modelGioHang.getValueAt(i, 1).toString());
                    int slMoi = slCu + soLuong;
                    modelGioHang.setValueAt(slMoi, i, 1);
                    modelGioHang.setValueAt(slMoi * gia, i, 3);
                    modelGioHang.setValueAt(lieuDung, i, 4);
                    clearInput();
                    return;
                }
            }

            double thanhTien = soLuong * gia;
            modelGioHang.addRow(new Object[]{
                    maThuoc, soLuong, gia, thanhTien, lieuDung
            });

            clearInput();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Số lượng là số nguyên hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================== 2. XÓA DÒNG TRONG GIỎ HÀNG TẠM ===========================
    private void xoaDongTrongGio() {
        int selectedRow = tblGioHang.getSelectedRow();
        if (selectedRow >= 0) {
            modelGioHang.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng trong Giỏ hàng tạm để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    // =========================== 3. LƯU GIỎ HÀNG VÀO DATABASE (MYSQL UPSERT) ===========================
    private void luuVaoDatabase() {
        if (cbbDH.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Mã đơn hàng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (modelGioHang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống! Hãy thêm ít nhất 1 món thuốc.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maDH = cbbDH.getSelectedItem().toString();

        int xacNhan = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn lưu " + modelGioHang.getRowCount() + " món thuốc vào đơn hàng " + maDH + "?", 
                "Xác nhận lưu", JOptionPane.YES_NO_OPTION);

        if (xacNhan != JOptionPane.YES_OPTION) return;

        Connection c = null;
        try {
            c = DatabaseHelper.connect();
            c.setAutoCommit(false);

            // Cú pháp chuẩn MySQL: INSERT ... ON DUPLICATE KEY UPDATE
            String sql = "INSERT INTO DON_HANG_THUOC (MaDH, MaThuoc, SoLuong, Gia, LieuDung) " +
                         "VALUES (?, ?, ?, ?, ?) " +
                         "ON DUPLICATE KEY UPDATE " +
                         "SoLuong = SoLuong + VALUES(SoLuong), " +
                         "Gia = VALUES(Gia), " +
                         "LieuDung = VALUES(LieuDung);";

            PreparedStatement ps = c.prepareStatement(sql);

            for (int i = 0; i < modelGioHang.getRowCount(); i++) {
                String maThuoc = modelGioHang.getValueAt(i, 0).toString();
                int soLuong = Integer.parseInt(modelGioHang.getValueAt(i, 1).toString());
                double gia = Double.parseDouble(modelGioHang.getValueAt(i, 2).toString());
                String lieuDung = modelGioHang.getValueAt(i, 4).toString();

                ps.setString(1, maDH);
                ps.setString(2, maThuoc);
                ps.setInt(3, soLuong);
                ps.setDouble(4, gia);
                ps.setString(5, lieuDung);

                ps.addBatch();
            }

            ps.executeBatch();
            c.commit();

            JOptionPane.showMessageDialog(this, "Lưu đơn hàng thành công!");
            modelGioHang.setRowCount(0); // Clear giỏ tạm
            
            // Tải lại bảng chi tiết đã lưu
            loadDataDaLuu();

        } catch (SQLException e) {
            if (c != null) {
                try { c.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            JOptionPane.showMessageDialog(this, "Lỗi lưu vào CSDL: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (c != null) {
                try { c.setAutoCommit(true); c.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // =========================== 4. LOAD BẢNG ĐƠN HÀNG ĐÃ LƯU TỪ CSDL ===========================
    private void loadDataDaLuu() {
        modelDaLuu.setRowCount(0);
        if (cbbDH.getSelectedItem() == null) return;

        String maDH = cbbDH.getSelectedItem().toString();
        String sql = "SELECT MaDH, MaThuoc, SoLuong, Gia, (SoLuong * Gia) AS ThanhTien, LieuDung FROM DON_HANG_THUOC WHERE MaDH = ?";

        try (Connection c = DatabaseHelper.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maDH);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelDaLuu.addRow(new Object[]{
                        rs.getString("MaDH"),
                        rs.getString("MaThuoc"),
                        rs.getInt("SoLuong"),
                        rs.getDouble("Gia"),
                        rs.getDouble("ThanhTien"),
                        rs.getString("LieuDung")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================== 5. XÓA DÒNG CHI TIẾT TRONG CSDL ===========================
    private void xoaChitietTrongDB() {
        int selectedRow = tblDaLuu.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa ở bảng ĐÃ LƯU!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maDH = modelDaLuu.getValueAt(selectedRow, 0).toString();
        String maThuoc = modelDaLuu.getValueAt(selectedRow, 1).toString();

        int xacNhan = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa thuốc " + maThuoc + " khỏi đơn hàng " + maDH + " trong CSDL?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (xacNhan == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM DON_HANG_THUOC WHERE MaDH = ? AND MaThuoc = ?";
            try (Connection c = DatabaseHelper.connect();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setString(1, maDH);
                ps.setString(2, maThuoc);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadDataDaLuu();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =========================== 6. LOAD COMBOBOX ===========================
    private void loadDonHang() {
        cbbDH.removeAllItems();
        try (Connection c = DatabaseHelper.connect()) {
            PreparedStatement ps = c.prepareStatement("SELECT MaDH FROM DON_HANG");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) cbbDH.addItem(rs.getString(1));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải đơn hàng: " + e.getMessage());
        }
    }

    private void loadThuoc() {
        cbbThuoc.removeAllItems();
        try (Connection c = DatabaseHelper.connect()) {
            PreparedStatement ps = c.prepareStatement("SELECT MaThuoc FROM THUOC");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) cbbThuoc.addItem(rs.getString(1));
            
            updateDonGiaTuDB();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải thuốc: " + e.getMessage());
        }
    }

    private void clearInput() {
        txtSL.setText("");
        txtLieu.setText("");
        if (cbbThuoc.getItemCount() > 0) {
            cbbThuoc.setSelectedIndex(0);
            updateDonGiaTuDB();
        }
    }

    // =========================== MAIN TEST ===========================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormChiTietDon().setVisible(true));
    }
}
