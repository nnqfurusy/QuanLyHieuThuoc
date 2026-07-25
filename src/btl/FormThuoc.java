package btl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormThuoc extends JFrame {

    Connection conn;
    DefaultTableModel model;

    JTextField txtMa, txtTen, txtDang, txtHSD, txtTon, txtGia;
    JTable table;

    public FormThuoc() {
        setTitle("Quản lý Thuốc");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // ********** KẾT NỐI SQL SERVER **********
        try {
            conn = DatabaseHelper.connect();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối SQL Server!");
            e.printStackTrace();
        }

        // ********** LABELS **********
        JLabel l1 = new JLabel("Mã Thuốc:");
        JLabel l2 = new JLabel("Tên Thuốc:");
        JLabel l3 = new JLabel("Dạng Bào Chế:");
        JLabel l4 = new JLabel("Hạn SD (yyyy-MM-dd):");
        JLabel l5 = new JLabel("Tồn Kho:");
        JLabel l6 = new JLabel("Giá Bán:");

        txtMa = new JTextField();
        txtTen = new JTextField();
        txtDang = new JTextField();
        txtHSD = new JTextField();
        txtTon = new JTextField();
        txtGia = new JTextField();

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnTim = new JButton("Tìm Kiếm");
        JButton btnList = new JButton("Liệt Kê");

        // ********** VỊ TRÍ **********
        l1.setBounds(30, 20, 150, 30);
        l2.setBounds(30, 60, 150, 30);
        l3.setBounds(30, 100, 150, 30);
        l4.setBounds(450, 20, 150, 30);
        l5.setBounds(450, 60, 150, 30);
        l6.setBounds(450, 100, 150, 30);

        txtMa.setBounds(160, 20, 250, 30);
        txtTen.setBounds(160, 60, 250, 30);
        txtDang.setBounds(160, 100, 250, 30);
        txtHSD.setBounds(600, 20, 250, 30);
        txtTon.setBounds(600, 60, 250, 30);
        txtGia.setBounds(600, 100, 250, 30);

        btnThem.setBounds(160, 150, 100, 30);
        btnSua.setBounds(270, 150, 100, 30);
        btnXoa.setBounds(380, 150, 100, 30);
        btnTim.setBounds(490, 150, 120, 30);
        btnList.setBounds(620, 150, 120, 30);

        add(l1); add(l2); add(l3); add(l4); add(l5); add(l6);
        add(txtMa); add(txtTen); add(txtDang); add(txtHSD); add(txtTon); add(txtGia);
        add(btnThem); add(btnSua); add(btnXoa); add(btnTim); add(btnList);

        // ********** TABLE **********
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép edit trực tiếp trên ô của bảng
            }
        };
        model.addColumn("Mã Thuốc");
        model.addColumn("Tên Thuốc");
        model.addColumn("Dạng Bào Chế");
        model.addColumn("Hạn SD");
        model.addColumn("Tồn Kho");
        model.addColumn("Giá Bán");

        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(30, 200, 830, 300);
        add(sp);

        // ********** SỰ KIỆN CLICK BẢNG (MỚI THÊM) **********
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtMa.setText(model.getValueAt(row, 0).toString());
                    txtTen.setText(model.getValueAt(row, 1).toString());
                    txtDang.setText(model.getValueAt(row, 2).toString());
                    txtHSD.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
                    txtTon.setText(model.getValueAt(row, 4).toString());
                    txtGia.setText(model.getValueAt(row, 5).toString());
                }
            }
        });

        // ********** SỰ KIỆN NÚT **********
        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnTim.addActionListener(e -> tim());
        btnList.addActionListener(e -> {
            clearForm();
            loadData();
        });

        loadData();
    }

    // ================= CLEAR FORM =================
    void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtDang.setText("");
        txtHSD.setText("");
        txtTon.setText("");
        txtGia.setText("");
        table.clearSelection();
    }

    // ================= LOAD DATA =================
    void loadData() {
        model.setRowCount(0);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM THUOC");

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getString("MaThuoc"),
                    rs.getString("TenThuoc"),
                    rs.getString("DangBaoChe"),
                    rs.getString("HanSuDung"),
                    rs.getInt("TonKho"),
                    rs.getDouble("GiaBan")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= THÊM =================
    void them() {
        if (txtMa.getText().trim().isEmpty() || txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã và Tên thuốc!");
            return;
        }

        try {
            String sql = "INSERT INTO THUOC(MaThuoc, TenThuoc, DangBaoChe, HanSuDung, TonKho, GiaBan) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtMa.getText().trim());
            ps.setString(2, txtTen.getText().trim());
            ps.setString(3, txtDang.getText().trim());
            ps.setString(4, txtHSD.getText().trim());
            ps.setInt(5, Integer.parseInt(txtTon.getText().trim()));
            ps.setDouble(6, Double.parseDouble(txtGia.getText().trim()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm thuốc thành công!");
            clearForm();
            loadData();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tồn kho và Giá bán phải là số hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi thêm thuốc! (Có thể do trùng Mã Thuốc)");
        }
    }

    // ================= SỬA =================
    void sua() {
        if (txtMa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thuốc từ bảng hoặc nhập Mã Thuốc để sửa!");
            return;
        }

        try {
            String sql = "UPDATE THUOC SET TenThuoc=?, DangBaoChe=?, HanSuDung=?, TonKho=?, GiaBan=? WHERE MaThuoc=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtTen.getText().trim());
            ps.setString(2, txtDang.getText().trim());
            ps.setString(3, txtHSD.getText().trim());
            ps.setInt(4, Integer.parseInt(txtTon.getText().trim()));
            ps.setDouble(5, Double.parseDouble(txtGia.getText().trim()));
            ps.setString(6, txtMa.getText().trim());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Sửa thông tin thuốc thành công!");
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy Mã Thuốc '" + txtMa.getText() + "' để sửa!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tồn kho và Giá bán phải là số hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi sửa!");
        }
    }

    // ================= XÓA =================
    void xoa() {
        int selectedRow = table.getSelectedRow();
        String maXoa = txtMa.getText().trim();

        if (selectedRow == -1 && maXoa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng trong bảng hoặc nhập Mã Thuốc để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa thuốc mã: " + maXoa + " không?", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            String sql = "DELETE FROM THUOC WHERE MaThuoc=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maXoa);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                clearForm();
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy Mã Thuốc '" + maXoa + "' trong cơ sở dữ liệu!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi xóa! (Có thể thuốc đang bị ràng buộc khóa ngoại)");
        }
    }

    // ================= TÌM =================
    void tim() {
        String keyword = txtMa.getText().trim();
        if (keyword.isEmpty()) {
            keyword = txtTen.getText().trim(); // Nếu ô mã trống thì thử lấy theo tên
        }

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Thuốc hoặc Tên Thuốc vào ô để tìm kiếm!");
            return;
        }

        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM THUOC WHERE MaThuoc LIKE ? OR TenThuoc LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                model.addRow(new Object[] {
                    rs.getString("MaThuoc"),
                    rs.getString("TenThuoc"),
                    rs.getString("DangBaoChe"),
                    rs.getString("HanSuDung"),
                    rs.getInt("TonKho"),
                    rs.getDouble("GiaBan")
                });
            }

            if (!hasData) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thuốc nào khớp với từ khóa!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FormThuoc().setVisible(true);
    }
}
