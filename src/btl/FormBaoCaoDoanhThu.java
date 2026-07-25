package btl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;

public class FormBaoCaoDoanhThu extends JFrame {

    private JTable tbl;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JCheckBox chkHomNay;
    private JLabel lblTongDonHang, lblTongDoanhThu;

    private DecimalFormat df = new DecimalFormat("#,##0 VNĐ");

    public FormBaoCaoDoanhThu() {
        setTitle("Báo Cáo Doanh Thu Nhà Thuốc");
        setSize(800, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initUI();
        loadData();
    }

    private void initUI() {
        // ================= PANEL FILTER (TOP) =================
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        pnlTop.add(new JLabel("Tìm mã đơn/ngày:"));
        txtSearch = new JTextField(15);
        pnlTop.add(txtSearch);

        JButton btnSearch = new JButton("Tìm");
        pnlTop.add(btnSearch);

        chkHomNay = new JCheckBox("Chỉ xem hôm nay");
        pnlTop.add(chkHomNay);

        JButton btnReload = new JButton("Tải lại");
        pnlTop.add(btnReload);

        add(pnlTop, BorderLayout.NORTH);

        // ================= TABLE (CENTER) =================
        model = new DefaultTableModel();
        // Cột hiển thị chuẩn khớp với bảng DON_HANG
        model.setColumnIdentifiers(new Object[]{
                "STT", "Mã đơn hàng", "Ngày lập", "Tổng tiền"
        });

        tbl = new JTable(model);
        tbl.setRowHeight(25);
        tbl.getTableHeader().setReorderingAllowed(false);

        add(new JScrollPane(tbl), BorderLayout.CENTER);

        // ================= PANEL THỐNG KÊ (BOTTOM) =================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnlBottom.setBorder(BorderFactory.createTitledBorder("Thống kê doanh thu"));

        lblTongDonHang = new JLabel("Tổng số đơn: 0");
        lblTongDoanhThu = new JLabel("Tổng doanh thu: 0 VNĐ");

        lblTongDonHang.setFont(new Font("Arial", Font.BOLD, 13));
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 13));
        lblTongDoanhThu.setForeground(new Color(0, 102, 204)); // Màu xanh nổi bật

        pnlBottom.add(lblTongDonHang);
        pnlBottom.add(lblTongDoanhThu);

        add(pnlBottom, BorderLayout.SOUTH);

        // ================= SỰ KIỆN =================
        btnSearch.addActionListener(e -> loadData());
        chkHomNay.addActionListener(e -> loadData());
        btnReload.addActionListener(e -> {
            txtSearch.setText("");
            chkHomNay.setSelected(false);
            loadData();
        });
    }

    private void loadData() {
        model.setRowCount(0);
        int tongSoDon = 0;
        double tongDoanhThu = 0;

        String keyword = txtSearch.getText().trim();
        boolean onlyHomNay = chkHomNay.isSelected();

        // Truy vấn bảng DON_HANG với 3 cột chuẩn: MaDH, NgayLap, TongTien
        StringBuilder sql = new StringBuilder(
            "SELECT MaDH, NgayLap, TongTien FROM DON_HANG WHERE 1=1"
        );

        if (!keyword.isEmpty()) {
            sql.append(" AND (MaDH LIKE ? OR CONVERT(VARCHAR, NgayLap, 120) LIKE ?)");
        }
        if (onlyHomNay) {
            // Lọc các đơn hàng trong ngày hôm nay (tương thích SQL Server)
            sql.append(" AND DATEDIFF(day, NgayLap, GETDATE()) = 0");
        }
        sql.append(" ORDER BY NgayLap DESC");

        try (Connection c = DatabaseHelper.connect();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (!keyword.isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword + "%");
                ps.setString(paramIndex++, "%" + keyword + "%");
            }

            ResultSet rs = ps.executeQuery();
            int stt = 1;
            while (rs.next()) {
                String maDH = rs.getString("MaDH");
                Date ngayLap = rs.getDate("NgayLap");
                double tongTien = rs.getDouble("TongTien");

                tongDoanhThu += tongTien;
                tongSoDon++;

                model.addRow(new Object[]{
                        stt++,
                        maDH,
                        ngayLap != null ? ngayLap.toString() : "",
                        df.format(tongTien)
                });
            }

            // Cập nhật nhãn thống kê
            lblTongDonHang.setText("Tổng số đơn: " + tongSoDon);
            lblTongDoanhThu.setText("Tổng doanh thu: " + df.format(tongDoanhThu));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải báo cáo doanh thu:\n" + e.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ======= HÀM MAIN ĐỂ TEST TRỰC TIẾP =======
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormBaoCaoDoanhThu().setVisible(true);
        });
    }
}
