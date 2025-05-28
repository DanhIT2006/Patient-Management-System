package CODE;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class AddDoctor extends JFrame {
    private JTextField tfTen, tfDiaChi, tfSDT, tfEmail, tfCCCD, tfTrinhDo;
    private JComboBox<String> cbChucVu, cbKhoa;
    private JDateChooser dateChooser;
    private JLabel imageLabel;
    private String imagePath = "";

    public AddDoctor() {
        setTitle("Thêm Thông Tin Bác Sĩ");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel[] labels = {
                new JLabel("Họ và tên:"), new JLabel("Địa chỉ:"),
                new JLabel("Chức vụ:"), new JLabel("Khoa:"),
                new JLabel("Số điện thoại:"), new JLabel("Email:"),
                new JLabel("CCCD:"), new JLabel("Trình độ học vấn:"),
                new JLabel("Ngày sinh:"), new JLabel("Ảnh đại diện:")
        };

        for (JLabel lb : labels) {
            lb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }

        tfTen = new JTextField(); tfDiaChi = new JTextField();
        tfSDT = new JTextField(); tfEmail = new JTextField();
        tfCCCD = new JTextField(); tfTrinhDo = new JTextField();
        cbChucVu = new JComboBox<>(new String[]{ "Thạc sĩ", "Tiến sĩ", "Phó Giáo sư", "Giáo sư"});
        cbKhoa = new JComboBox<>(new String[]{"Đa khoa", "Chỉnh hình", "Nội tiết", "Tim mạch", "Hô hấp"});
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");


        gbc.gridx = 0; gbc.gridy = 0; mainPanel.add(labels[0], gbc);
        gbc.gridx = 1; mainPanel.add(tfTen, gbc);
        gbc.gridx = 2; mainPanel.add(labels[1], gbc);
        gbc.gridx = 3; mainPanel.add(tfDiaChi, gbc);


        gbc.gridy = 1; gbc.gridx = 0; mainPanel.add(labels[2], gbc);
        gbc.gridx = 1; mainPanel.add(cbChucVu, gbc);
        gbc.gridx = 2; mainPanel.add(labels[3], gbc);
        gbc.gridx = 3; mainPanel.add(cbKhoa, gbc);


        gbc.gridy = 2; gbc.gridx = 0; mainPanel.add(labels[4], gbc);
        gbc.gridx = 1; mainPanel.add(tfSDT, gbc);
        gbc.gridx = 2; mainPanel.add(labels[5], gbc);
        gbc.gridx = 3; mainPanel.add(tfEmail, gbc);


        gbc.gridy = 3; gbc.gridx = 0; mainPanel.add(labels[6], gbc);
        gbc.gridx = 1; mainPanel.add(tfCCCD, gbc);
        gbc.gridx = 2; mainPanel.add(labels[7], gbc);
        gbc.gridx = 3; mainPanel.add(tfTrinhDo, gbc);


        gbc.gridy = 4; gbc.gridx = 0; mainPanel.add(labels[8], gbc);
        gbc.gridx = 1; mainPanel.add(dateChooser, gbc);


        gbc.gridy = 5; gbc.gridx = 0; mainPanel.add(labels[9], gbc);
        JPanel imgPanel = new JPanel(new BorderLayout());
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(120, 150));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        imgPanel.add(imageLabel, BorderLayout.CENTER);

        JButton btnImage = new JButton("Chọn ảnh");
        btnImage.addActionListener(e -> chooseImage());
        imgPanel.add(btnImage, BorderLayout.SOUTH);

        gbc.gridx = 1; mainPanel.add(imgPanel, gbc);


        JButton btnAdd = new JButton("Thêm bác sĩ");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> insertDoctor());

        JPanel bottom = new JPanel();
        bottom.add(btnAdd);

        add(mainPanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Ảnh JPG/PNG", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            imagePath = file.getAbsolutePath();
            ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(120, 150, Image.SCALE_SMOOTH));
            imageLabel.setIcon(icon);
        }
    }

    private void insertDoctor() {
        String ten = tfTen.getText();
        String diaChi = tfDiaChi.getText();
        String chucVu = (String) cbChucVu.getSelectedItem();
        String khoa = (String) cbKhoa.getSelectedItem();
        String sdt = tfSDT.getText();
        String email = tfEmail.getText();
        String cccd = tfCCCD.getText();
        String trinhDo = tfTrinhDo.getText();
        java.util.Date dob = dateChooser.getDate();

        if (ten.isEmpty() || diaChi.isEmpty() || sdt.isEmpty() || dob == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "")) {
            String sql = "INSERT INTO bac_si (ten, dia_chi, chuc_vu, khoa, so_dien_thoai, email, cccd, trinh_do, ngay_sinh, avatar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, ten);
            pst.setString(2, diaChi);
            pst.setString(3, chucVu);
            pst.setString(4, khoa);
            pst.setString(5, sdt);
            pst.setString(6, email);
            pst.setString(7, cccd);
            pst.setString(8, trinhDo);
            pst.setDate(9, new java.sql.Date(dob.getTime()));
            pst.setString(10, imagePath);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm bác sĩ thành công!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu vào cơ sở dữ liệu!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddDoctor::new);
    }
}