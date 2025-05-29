package CODE;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

public class DoctorDetails extends JFrame {
    private JTable table;
    private JLabel imageLabel;
    private DefaultTableModel model;
    private JTextField tfSearch;
    private JComboBox<String> cbKhoa;
    private JButton btnEdit, btnDelete;

    public DoctorDetails() {
        setTitle("Chi Tiết Bác Sĩ");
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(197, 248, 229));

        tfSearch = new JTextField(20);
        cbKhoa = new JComboBox<>(new String[]{"Tất cả", "Đa khoa", "Chỉnh hình", "Nội tiết", "Tim mạch", "Hô hấp"});
        JButton btnSearch = new JButton("Tìm kiếm");
        JButton btnExport = new JButton("Xuất Excel");
        JButton btnStats = new JButton("Thống kê");

        searchPanel.add(new JLabel("Họ tên:"));
        searchPanel.add(tfSearch);
        searchPanel.add(new JLabel("Khoa:"));
        searchPanel.add(cbKhoa);
        searchPanel.add(btnSearch);
        searchPanel.add(btnExport);
        searchPanel.add(btnStats);
        add(searchPanel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "Mã bác sĩ", "Họ tên", "Khoa", "Chức vụ", "Trình độ", "SĐT", "Email", "CCCD", "Ngày sinh", "Avatar"
        });

        table = new JTable(model);
        table.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(197, 235, 245));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createTitledBorder("Ảnh đại diện"));
        sidePanel.setPreferredSize(new Dimension(200, 300));

        imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(150, 200));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        sidePanel.add(imageLabel);

        btnEdit = new JButton("Sửa thông tin");
        btnDelete = new JButton("Xóa bác sĩ");
        btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(btnEdit);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(btnDelete);

        add(sidePanel, BorderLayout.EAST);

        loadDoctorData("", "Tất cả");

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String imagePath = model.getValueAt(row, 9).toString();
                showImage(imagePath);
            }
        });

        btnSearch.addActionListener(searchEvent -> {
            String name = tfSearch.getText().trim();
            String khoa = cbKhoa.getSelectedItem().toString();
            loadDoctorData(name, khoa);
        });

        btnExport.addActionListener(exportEvent -> exportToCSV());
        btnStats.addActionListener(statsEvent -> showStats());

        btnDelete.addActionListener(e -> deleteDoctor());
        btnEdit.addActionListener(e -> editDoctor());

        setVisible(true);
    }

    public void loadDoctorData(String nameFilter, String khoaFilter) {
        model.setRowCount(0);
        String query = "SELECT * FROM bac_si WHERE 1=1";
        if (!nameFilter.isEmpty()) {
            query += " AND hoten LIKE '%" + nameFilter + "%'";
        }
        if (!khoaFilter.equals("Tất cả")) {
            query += " AND khoa = '" + khoaFilter + "'";
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("mabacsi"),
                        rs.getString("hoten"),
                        rs.getString("khoa"),
                        rs.getString("nghe_nghiep"),
                        rs.getString("trinh_do"),
                        rs.getString("sdt"),
                        rs.getString("email"),
                        rs.getString("cccd"),
                        rs.getDate("ngaysinh"),
                        rs.getString("avatar") != null ? rs.getString("avatar") : ""
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu từ cơ sở dữ liệu.");
        }
    }

    private void editDoctor() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bác sĩ để sửa.");
            return;
        }

        String mabacsi = (String) model.getValueAt(row, 0);
        String ten = (String) model.getValueAt(row, 1);
        String khoa = (String) model.getValueAt(row, 2);
        String chucVu = (String) model.getValueAt(row, 3);
        String trinhDo = (String) model.getValueAt(row, 4);
        String sdt = (String) model.getValueAt(row, 5);
        String email = (String) model.getValueAt(row, 6);
        String cccd = (String) model.getValueAt(row, 7);
        java.sql.Date ngaySinh = (java.sql.Date) model.getValueAt(row, 8);
        String avatar = (String) model.getValueAt(row, 9);

        new EditDoctorForm(this, mabacsi, ten, khoa, chucVu, trinhDo, sdt, email, cccd, ngaySinh, avatar);
    }

    private void deleteDoctor() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bác sĩ để xóa.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa bác sĩ này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String maBacSi = (String) model.getValueAt(row, 0);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "");
             PreparedStatement pst = conn.prepareStatement("DELETE FROM bac_si WHERE mabacsi = ?")) {

            pst.setString(1, maBacSi);
            pst.executeUpdate();
            model.removeRow(row);
            imageLabel.setIcon(null);
            JOptionPane.showMessageDialog(this, "Đã xóa bác sĩ thành công.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa dữ liệu.");
        }
    }

    private void showImage(String path) {
        if (path == null || path.trim().isEmpty()) {
            imageLabel.setIcon(null);
            return;
        }

        File imgFile = new File(path);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setIcon(null);
        }
    }

    private void exportToCSV() {
        try (FileWriter fw = new FileWriter("ds_bacsi.csv")) {
            for (int i = 0; i < model.getColumnCount(); i++) {
                fw.write(model.getColumnName(i) + ",");
            }
            fw.write("\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    fw.write(model.getValueAt(i, j).toString() + ",");
                }
                fw.write("\n");
            }

            JOptionPane.showMessageDialog(this, "Xuất danh sách thành công (ds_bacsi.csv).");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất file.");
        }
    }

    private void showStats() {
        StringBuilder report = new StringBuilder("Số lượng bác sĩ theo khoa: ");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT khoa, COUNT(*) AS sl FROM bac_si GROUP BY khoa")) {

            while (rs.next()) {
                report.append(rs.getString("khoa")).append(": ").append(rs.getInt("sl")).append(" bác sĩ\n");
            }
            JOptionPane.showMessageDialog(this, report.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thống kê.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DoctorDetails::new);
    }
}