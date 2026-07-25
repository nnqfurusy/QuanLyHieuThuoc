package btl_csdl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.*;
import java.sql.*;
import java.text.DecimalFormat;

public class FormHoaDon extends JFrame {

    private String maDH;
    private JLabel lblMaDH, lblNgayLap, lblNhanVien, lblKhachHang, lblTongTien;
    private JTable tblChiTiet;
    private DefaultTableModel modelChiTiet;
    private JButton btnIn;

    public FormHoaDon(String maDH) {
        this.maDH = maDH;

        setTitle("HÓA ĐƠN BÁN THUỐC - " + maDH);
        setSize(550, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        loadHoaDonFromDB();
    }

    private void initComponents() {
        // --- HEADER HÓA ĐƠN ---
        JPanel panelHeader = new JPanel(new GridLayout(6, 1, 5, 5));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel lblTitle = new JLabel("NHÀ THUỐC TÂY", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel lblSubTitle = new JLabel("--- HÓA ĐƠN THANH TOÁN ---", SwingConstants.CENTER);
        lblSubTitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        lblMaDH = new JLabel("Mã hóa đơn: " + maDH);
        lblNgayLap = new JLabel("Ngày lập: ...");
        lblNhanVien = new JLabel("Nhân viên bán hàng: ...");
        lblKhachHang = new JLabel("Khách hàng: ...");

        panelHeader.add(lblTitle);
        panelHeader.add(lblSubTitle);
        panelHeader.add(lblMaDH);
        panelHeader.add(lblNgayLap);
        panelHeader.add(lblNhanVien);
        panelHeader.add(lblKhachHang);

        // --- BẢNG CHI TIẾT SẢN PHẨM ---
        modelChiTiet = new DefaultTableModel();
        modelChiTiet.setColumnIdentifiers(new Object[]{"Tên Thuốc", "Số Lượng", "Đơn Giá", "Thành Tiền"});
        tblChiTiet = new JTable(modelChiTiet);
        tblChiTiet.setRowHeight(25);
        JScrollPane sp = new JScrollPane(tblChiTiet);

        // --- FOOTER HÓA ĐƠN ---
        JPanel panelFooter = new JPanel(new BorderLayout(10, 10));
        panelFooter.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        lblTongTien = new JLabel("TỔNG CỘNG: 0 VNĐ", SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTien.setForeground(new Color(220, 53, 69));

        btnIn = new JButton("🖨️ In Hóa Đơn");
        btnIn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIn.setPreferredSize(new Dimension(150, 40));

        // ================= XỬ LÝ IN TOÀN BỘ FRAME =================
        btnIn.addActionListener(e -> {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("In_Hoa_Don_" + maDH);

            job.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex > 0) {
                        return Printable.NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    
                    // Dịch chuyển theo lề trang in
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Thu nhỏ nhẹ giao diện để đảm bảo vừa khít trang in (A4/A5)
                    double scaleX = pageFormat.getImageableWidth() / getWidth();
                    double scaleY = pageFormat.getImageableHeight() / getHeight();
                    double scale = Math.min(scaleX, scaleY);
                    if (scale < 1.0) {
                        g2d.scale(scale, scale);
                    }

                    // Ẩn nút In đi trước khi vẽ để nút không xuất hiện trên hóa đơn in ra
                    btnIn.setVisible(false);

                    // Vẽ toàn bộ JFrame (Gồm Header, Table, Footer tổng tiền)
                    getContentPane().printAll(g2d);

                    // Hiện lại nút In trên màn hình sau khi in xong
                    btnIn.setVisible(true);

                    return Printable.PAGE_EXISTS;
                }
            });

            boolean doPrint = job.printDialog();
            if (doPrint) {
                try {
                    job.print();
                    JOptionPane.showMessageDialog(this, "In hóa đơn thành công!");
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi in: " + ex.getMessage());
                }
            }
        });

        panelFooter.add(lblTongTien, BorderLayout.NORTH);
        panelFooter.add(btnIn, BorderLayout.SOUTH);

        add(panelHeader, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        add(panelFooter, BorderLayout.SOUTH);
    }

    private void loadHoaDonFromDB() {
        DecimalFormat df = new DecimalFormat("#,##0 VNĐ");

        // Query lấy thông tin chung của Đơn hàng, Nhân viên, Khách hàng
        String sqlOrder = "SELECT dh.NgayLap, nv.HoTen AS TenNV, kh.HoTen AS TenKH " +
                  "FROM DON_HANG dh " +
                  "LEFT JOIN NHAN_VIEN nv ON dh.MaNV = nv.MaNV " +
                  "LEFT JOIN KHACH_HANG kh ON dh.MaKH = kh.MaKH " +
                  "WHERE dh.MaDH = ?";

        try (Connection c = DatabaseHelper.connect();
             PreparedStatement ps = c.prepareStatement(sqlOrder)) {

            ps.setString(1, maDH);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblNgayLap.setText("Ngày lập: " + rs.getString("NgayLap"));
                lblNhanVien.setText("Nhân viên: " + (rs.getString("TenNV") != null ? rs.getString("TenNV") : "N/A"));
                lblKhachHang.setText("Khách hàng: " + (rs.getString("TenKH") != null ? rs.getString("TenKH") : "Khách lẻ"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Query danh sách thuốc và tính tổng tiền
        String sqlDetails = "SELECT t.TenThuoc, dt.SoLuong, dt.Gia, (dt.SoLuong * dt.Gia) AS ThanhTien " +
                            "FROM DON_HANG_THUOC dt " +
                            "JOIN THUOC t ON dt.MaThuoc = t.MaThuoc " +
                            "WHERE dt.MaDH = ?";

        double tongTien = 0;
        modelChiTiet.setRowCount(0);

        try (Connection c = DatabaseHelper.connect();
             PreparedStatement ps = c.prepareStatement(sqlDetails)) {

            ps.setString(1, maDH);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double thanhTien = rs.getDouble("ThanhTien");
                tongTien += thanhTien;

                modelChiTiet.addRow(new Object[]{
                        rs.getString("TenThuoc"),
                        rs.getInt("SoLuong"),
                        df.format(rs.getDouble("Gia")),
                        df.format(thanhTien)
                });
            }
            lblTongTien.setText("TỔNG CỘNG: " + df.format(tongTien));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}