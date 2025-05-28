package CODE;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

public class PatientDetails extends JFrame {
    private JTable table;
    private JLabel imageLabel;
    private DefaultTableModel model;
    private JTextField tfSearch;
    private JComboBox<String> cbKhoa;
    private JButton btnEdit, btnDelete;

    public PatientDetails() {
        setTitle("Chi Tiết Bệnh Nhân");
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(255, 235, 245));

        tfSearch = new JTextField(20);
        cbKhoa = new JComboBox<>(new String[] {"Tất cả", "Đa khoa", "Chỉnh hình", "Nội tiết", "Tim mạch", "Hô hấp"});
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
        model.setColumnIdentifiers(new String[] {
                "Mã BN", "Họ tên", "Giới tính", "Ngày sinh", "SĐT", "Địa chỉ", "CCCD", "Ngày vào", "Ngày ra", "Nghề", "Khoa", "Bệnh", "Yêu cầu", "Thuốc", "Viện phí", "Phòng", "Avatar"
        });

        table = new JTable(model);
        table.setRowHeight(28);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(255, 235, 245));
        sidePanel.setBorder(BorderFactory.createTitledBorder("Ảnh đại diện"));
        sidePanel.setPreferredSize(new Dimension(200, 300));

        imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(150, 200));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        sidePanel.add(imageLabel);

        btnEdit = new JButton("Sửa thông tin");
        btnDelete = new JButton("Xóa bệnh nhân");
        btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(btnEdit);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(btnDelete);

        add(sidePanel, BorderLayout.EAST);

        loadPatientData("", "Tất cả");

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String imagePath = model.getValueAt(row, 16).toString();
                showImage(imagePath);
            }
        });

        btnSearch.addActionListener(ev -> {
            String name = tfSearch.getText().trim();
            String khoa = cbKhoa.getSelectedItem().toString();
            loadPatientData(name, khoa);
        });

        btnExport.addActionListener(ev -> exportToCSV());
        btnStats.addActionListener(ev -> showStats());
        btnDelete.addActionListener(e -> deletePatient());
        btnEdit.addActionListener(e -> editPatient());

        setVisible(true);
    }

    void loadPatientData(String nameFilter, String khoaFilter) {
        model.setRowCount(0);
        String query = "SELECT * FROM benhnhan WHERE 1=1";
        if (!nameFilter.isEmpty()) query += " AND hoten LIKE '%" + nameFilter + "%'";
        if (!khoaFilter.equals("Tất cả")) query += " AND khoa = '" + khoaFilter + "'";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("mabenhnhan"),
                        rs.getString("hoten"),
                        rs.getString("gioitinh"),
                        rs.getDate("ngaysinh"),
                        rs.getString("sdt"),
                        rs.getString("diachi"),
                        rs.getString("cccd"),
                        rs.getDate("ngayvaovien"),
                        rs.getDate("ngayravien"),
                        rs.getString("nghe_nghiep"),
                        rs.getString("khoa"),
                        rs.getString("benh"),
                        rs.getString("yeu_cau"),
                        rs.getString("thuoc"),
                        rs.getInt("vien_phi"),
                        rs.getString("phong"),
                        rs.getString("avatar")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu bệnh nhân");
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
        int confirm = JOptionPane.showConfirmDialog(this, "Xuất danh sách bệnh nhân ra CSV?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (FileWriter fw = new FileWriter("ds_benhnhan.csv")) {
            for (int i = 0; i < model.getColumnCount(); i++) fw.write(model.getColumnName(i) + ",");
            fw.write("\n");
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) fw.write(model.getValueAt(i, j).toString() + ",");
                fw.write("\n");
            }
            JOptionPane.showMessageDialog(this, "Xuất thành công.");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất file");
        }
    }

    private void showStats() {
        StringBuilder stats = new StringBuilder("Thống kê số bệnh nhân theo khoa:\n\n");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT khoa, COUNT(*) as sl FROM benhnhan GROUP BY khoa")) {
            while (rs.next()) {
                stats.append(rs.getString("khoa"))
                        .append(": ").append(rs.getInt("sl"))
                        .append(" bệnh nhân\n");
            }
            JOptionPane.showMessageDialog(this, stats.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thống kê");
        }
    }

    private void deletePatient() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn bệnh nhân để xóa.");
            return;
        }
        String id = model.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa bệnh nhân này?", "Xóa", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "");
             PreparedStatement pst = conn.prepareStatement("DELETE FROM benhnhan WHERE mabenhnhan = ?")) {
            pst.setString(1, id);
            pst.executeUpdate();
            model.removeRow(row);
            imageLabel.setIcon(null);
            JOptionPane.showMessageDialog(this, "Đã xóa bệnh nhân.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa bệnh nhân");
        }
    }

    private void editPatient() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn bệnh nhân để sửa.");
            return;
        }

        new EditPatientForm(
                PatientDetails.this,
                model.getValueAt(row, 0).toString(),   // mã BN
                model.getValueAt(row, 1).toString(),   // họ tên
                model.getValueAt(row, 2).toString(),   // giới tính
                (java.sql.Date) model.getValueAt(row, 3), // ngày sinh
                model.getValueAt(row, 4).toString(),   // sdt
                model.getValueAt(row, 5).toString(),   // địa chỉ
                model.getValueAt(row, 6).toString(),   // cccd
                (java.sql.Date) model.getValueAt(row, 7), // ngày vào
                (java.sql.Date) model.getValueAt(row, 8), // ngày ra
                model.getValueAt(row, 9).toString(),   // nghề
                model.getValueAt(row,10).toString(),   // khoa
                model.getValueAt(row,11).toString(),   // bệnh
                model.getValueAt(row,12).toString(),   // yêu cầu
                model.getValueAt(row,13).toString(),   // thuốc
                Integer.parseInt(model.getValueAt(row,14).toString()), // viện phí
                model.getValueAt(row,15).toString(),   // phòng
                model.getValueAt(row,16).toString()    // avatar
        );
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(PatientDetails::new);
    }
}


