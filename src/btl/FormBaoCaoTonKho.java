package btl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormBaoCaoTonKho extends JFrame {

    private JTable tbl;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JCheckBox chkSapHet;
    private JLabel lblTongTon, lblSoLuongLoai;

    public FormBaoCaoTonKho() {
        setTitle("Báo Cáo Tồn Kho Nhà Thuốc");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initUI();
        loadData();
    }

    private void initUI() {
        // ================= PANEL FILTER (TOP) =================
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        pnlTop.add(new JLabel("Tìm kiếm thuốc:"));
        txtSearch = new JTextField(15);
        pnlTop.add(txtSearch);

        JButton btnSearch = new JButton("Tìm");
        pnlTop.add(btnSearch);

        chkSapHet = new JCheckBox("Chỉ xem thuốc sắp hết (<= 10)");
        pnlTop.add(chkSapHet);

        JButton btnReload = new JButton("Tải lại");
        pnlTop.add(btnReload);

        add(pnlTop, BorderLayout.NORTH);

        // ================= TABLE (CENTER) =================
        model = new DefaultTableModel();
        // Chỉ giữ 3 cột chuẩn chắc chắn có trong DB của bạn
        model.setColumnIdentifiers(new Object[]{
                "Mã thuốc", "Tên thuốc", "Số lượng tồn kho"
        });

        tbl = new JTable(model);
        tbl.setRowHeight(25);
        tbl.getTableHeader().setReorderingAllowed(false);

        add(new JScrollPane(tbl), BorderLayout.CENTER);

        // ================= PANEL THỐNG KÊ (BOTTOM) =================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnlBottom.setBorder(BorderFactory.createTitledBorder("Thống kê tồn kho"));

        lblSoLuongLoai = new JLabel("Số mặt hàng: 0");
        lblTongTon = new JLabel("Tổng số lượng tồn: 0");

        lblSoLuongLoai.setFont(new Font("Arial", Font.BOLD, 13));
        lblTongTon.setFont(new Font("Arial", Font.BOLD, 13));

        pnlBottom.add(lblSoLuongLoai);
        pnlBottom.add(lblTongTon);

        add(pnlBottom, BorderLayout.SOUTH);

        // ================= SỰ KIỆN =================
        btnSearch.addActionListener(e -> loadData());
        chkSapHet.addActionListener(e -> loadData());
        btnReload.addActionListener(e -> {
            txtSearch.setText("");
            chkSapHet.setSelected(false);
            loadData();
        });
    }

    private void loadData() {
        model.setRowCount(0);
        int tongSoLuongTon = 0;
        int soMatHang = 0;

        String keyword = txtSearch.getText().trim();
        boolean onlySapHet = chkSapHet.isSelected();

        // Truy vấn 3 cột chuẩn tuyệt đối: MaThuoc, TenThuoc, TonKho
        StringBuilder sql = new StringBuilder(
            "SELECT MaThuoc, TenThuoc, TonKho FROM THUOC WHERE 1=1"
        );

        if (!keyword.isEmpty()) {
            sql.append(" AND (TenThuoc LIKE ? OR MaThuoc LIKE ?)");
        }
        if (onlySapHet) {
            sql.append(" AND TonKho <= 10");
        }
        sql.append(" ORDER BY TonKho ASC");

        try (Connection c = DatabaseHelper.connect();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (!keyword.isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword + "%");
                ps.setString(paramIndex++, "%" + keyword + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String ma = rs.getString("MaThuoc");
                String ten = rs.getString("TenThuoc");
                int ton = rs.getInt("TonKho");

                tongSoLuongTon += ton;
                soMatHang++;

                model.addRow(new Object[]{ ma, ten, ton });
            }

            // Cập nhật thông tin thống kê
            lblSoLuongLoai.setText("Số mặt hàng: " + soMatHang);
            lblTongTon.setText("Tổng số lượng tồn: " + tongSoLuongTon);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tải báo cáo tồn kho:\n" + e.getMessage(),
                    "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormBaoCaoTonKho().setVisible(true));
    }
}
