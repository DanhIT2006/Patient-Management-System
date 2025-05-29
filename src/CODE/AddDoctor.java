package CODE;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class AddDoctor extends JFrame {
    private JTextField tfTen, tfDiaChi, tfSDT, tfEmail, tfCCCD, tfTrinhDo,tfMaBacSi;
    private JComboBox<String> cbChucVu, cbKhoa;
    private JPasswordField pfMatKhau;
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


        JLabel lbMaBacSi = new JLabel("Mã bác sĩ:");
        lbMaBacSi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfMaBacSi = new JTextField();

        // Row 0: "Mã bác sĩ" and "Ngày sinh"
        gbc.gridx = 0; gbc.gridy = 0; mainPanel.add(lbMaBacSi, gbc);
        gbc.gridx = 1; mainPanel.add(tfMaBacSi, gbc);
        gbc.gridx = 2; mainPanel.add(labels[8], gbc);
        gbc.gridx = 3; mainPanel.add(dateChooser, gbc);

// Row 1: "Họ và tên" and "Địa chỉ"
        gbc.gridy = 1; gbc.gridx = 0; mainPanel.add(labels[0], gbc);
        gbc.gridx = 1; mainPanel.add(tfTen, gbc);
        gbc.gridx = 2; mainPanel.add(labels[1], gbc);
        gbc.gridx = 3; mainPanel.add(tfDiaChi, gbc);

// Row 2: "Chức vụ" and "Khoa"
        gbc.gridy = 2; gbc.gridx = 0; mainPanel.add(labels[2], gbc);
        gbc.gridx = 1; mainPanel.add(cbChucVu, gbc);
        gbc.gridx = 2; mainPanel.add(labels[3], gbc);
        gbc.gridx = 3; mainPanel.add(cbKhoa, gbc);

// Row 3: "Số điện thoại" and "Email"
        gbc.gridy = 3; gbc.gridx = 0; mainPanel.add(labels[4], gbc);
        gbc.gridx = 1; mainPanel.add(tfSDT, gbc);
        gbc.gridx = 2; mainPanel.add(labels[5], gbc);
        gbc.gridx = 3; mainPanel.add(tfEmail, gbc);

// Row 4: "CCCD" and "Trình độ học vấn"
        gbc.gridy = 4; gbc.gridx = 0; mainPanel.add(labels[6], gbc);
        gbc.gridx = 1; mainPanel.add(tfCCCD, gbc);
        gbc.gridx = 2; mainPanel.add(labels[7], gbc);
        gbc.gridx = 3; mainPanel.add(tfTrinhDo, gbc);

        JLabel lbMatKhau = new JLabel("Mật khẩu:");
        lbMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pfMatKhau = new JPasswordField();
        gbc.gridy = 5; gbc.gridx = 2; mainPanel.add(lbMatKhau, gbc);
        gbc.gridx = 3; mainPanel.add(pfMatKhau, gbc);

// Row 5: "Ảnh đại diện"
        gbc.gridy = 5; gbc.gridx = 0; mainPanel.add(labels[9], gbc);
        JPanel imgPanel = new JPanel(new BorderLayout());
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(120, 150));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        imgPanel.add(imageLabel, BorderLayout.CENTER);
        gbc.gridx = 1; mainPanel.add(imgPanel, gbc);

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
        String maBacSi = tfMaBacSi.getText().trim();
        String ten = tfTen.getText().trim();
        String diaChi = tfDiaChi.getText().trim();
        String chucVu = (String) cbChucVu.getSelectedItem();
        String khoa = (String) cbKhoa.getSelectedItem();
        String sdt = tfSDT.getText().trim();
        String email = tfEmail.getText().trim();
        String cccd = tfCCCD.getText().trim();
        String trinhDo = tfTrinhDo.getText().trim();
        java.util.Date dob = dateChooser.getDate();
        String matKhau = new String(pfMatKhau.getPassword()).trim();

        // Validate required fields
        if (maBacSi.isEmpty() || ten.isEmpty() || diaChi.isEmpty() || sdt.isEmpty() || email.isEmpty() || matKhau.isEmpty() || dob == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin, bao gồm mã bác sĩ, số điện thoại, email và mật khẩu!");
            return;
        }

        // Validate phone number format (e.g., 10-11 digits)
        if (!sdt.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10-11 chữ số!");
            return;
        }

        // Validate email format (basic regex for email)
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
            return;
        }

        // Optionally, validate password strength
        if (matKhau.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự!");
            return;
        }

        Connection conn = null;
        try {
            // Start a transaction
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "");
            conn.setAutoCommit(false); // Disable auto-commit to use transaction

            // 1. Insert into tai_khoan_bs with password
            String sqlTaiKhoan = "INSERT INTO tai_khoan_bs (sdt, email, matkhau) VALUES (?, ?, ?)";
            PreparedStatement pstTaiKhoan = conn.prepareStatement(sqlTaiKhoan);
            pstTaiKhoan.setString(1, sdt);
            pstTaiKhoan.setString(2, email);
            pstTaiKhoan.setString(3, matKhau); // Add password
            pstTaiKhoan.executeUpdate();

            // 2. Insert into bac_si
            String sqlBacSi = "INSERT INTO bac_si (mabacsi, hoten, diachi, nghe_nghiep, khoa, sdt, email, cccd, trinh_do, ngaysinh, avatar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstBacSi = conn.prepareStatement(sqlBacSi);
            pstBacSi.setString(1, maBacSi);
            pstBacSi.setString(2, ten);
            pstBacSi.setString(3, diaChi);
            pstBacSi.setString(4, chucVu);
            pstBacSi.setString(5, khoa);
            pstBacSi.setString(6, sdt);
            pstBacSi.setString(7, email);
            pstBacSi.setString(8, cccd);
            pstBacSi.setString(9, trinhDo);
            pstBacSi.setDate(10, new java.sql.Date(dob.getTime()));
            pstBacSi.setString(11, imagePath);

            pstBacSi.executeUpdate();

            //iamas: // Commit the transaction
            conn.commit();
            JOptionPane.showMessageDialog(this, "Thêm bác sĩ và tài khoản thành công!");
            dispose();
        } catch (SQLException ex) {
            // Rollback the transaction on error
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu vào cơ sở dữ liệu: " + ex.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Restore auto-commit
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddDoctor::new);
    }
}