package CODE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame implements ActionListener  {
    private JFrame frame;
    private JTextField userIDField;
    private JPasswordField userPasswordField;
    private JComboBox<String> roleComboBox;

    public Login() {
        frame = new JFrame("Đăng Nhập");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(650, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("D:\\JAVACODE\\DOANCOSO\\src\\Imgs\\anhnenlogin.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel glassPanel = new JPanel();
        glassPanel.setPreferredSize(new Dimension(300, 400));
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setBackground(new Color(255, 255, 255, 240));
        glassPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        glassPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Đăng nhập", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        glassPanel.add(titleLabel);

        // Thêm JComboBox để chọn vai trò
        String[] roles = {"Bệnh nhân", "Bác sĩ", "Admin"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setMaximumSize(new Dimension(250, 35));
        roleComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        roleComboBox.setBackground(Color.WHITE);
        roleComboBox.setFocusable(false);
        glassPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        glassPanel.add(roleComboBox);

        userIDField = new JTextField();
        userIDField.setMaximumSize(new Dimension(250, 35));
        userIDField.setAlignmentX(Component.CENTER_ALIGNMENT);
        userIDField.setBorder(BorderFactory.createTitledBorder("Số điện thoại"));
        glassPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        glassPanel.add(userIDField);

        userPasswordField = new JPasswordField();
        userPasswordField.setMaximumSize(new Dimension(250, 35));
        userPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPasswordField.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));
        glassPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        glassPanel.add(userPasswordField);

        JButton loginButton = new JButton("Đăng nhập");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setFocusPainted(false);
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(new Color(0, 51, 204));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
        glassPanel.add(Box.createRigidArea(new Dimension(0, 27)));
        glassPanel.add(loginButton);

        JLabel forgotPasswordLabel = new JLabel("");
        forgotPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        forgotPasswordLabel.setForeground(Color.DARK_GRAY);
        forgotPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        glassPanel.add(forgotPasswordLabel);

        JButton requestPasswordButton = new JButton("Yêu cầu cấp mật khẩu?");
        requestPasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        requestPasswordButton.setFocusPainted(false);
        requestPasswordButton.setBackground(Color.WHITE);
        requestPasswordButton.setForeground(Color.DARK_GRAY);
        requestPasswordButton.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        requestPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        requestPasswordButton.addActionListener(e -> new RequestPasswordPage());
        glassPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        glassPanel.add(requestPasswordButton);

        backgroundPanel.add(glassPanel);
        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        handleLogin();
    }

    private void handleLogin() {
        String userID = userIDField.getText().trim();
        String password = String.valueOf(userPasswordField.getPassword()).trim();
        String selectedRole = (String) roleComboBox.getSelectedItem();

        try (Connection conn = ConnectionDatabase.getConnection()) {
            String query = "";
            String role = "";
            String userIdField = "";

            if (selectedRole.equals("Bệnh nhân")) {
                query = "SELECT t.sdt, t.matkhau, b.mabenhnhan, b.hoten, b.gioitinh, b.ngaysinh, b.sdt AS benh_nhan_sdt, b.diachi " +
                        "FROM tai_khoan t JOIN benhnhan b ON t.sdt = b.sdt WHERE t.sdt = ?";
                role = "benhnhan";
                userIdField = "mabenhnhan";
            } else if (selectedRole.equals("Bác sĩ")) {
                query = "SELECT t.sdt, t.matkhau, b.mabacsi, b.hoten, b.gioitinh, b.ngaysinh, b.sdt AS bac_si_sdt, b.diachi " +
                        "FROM tai_khoan_bs t JOIN bac_si b ON t.sdt = b.sdt WHERE t.sdt = ?";
                role = "bacsi";
                userIdField = "mabacsi";
            } else if (selectedRole.equals("Admin")) {
                query = "SELECT sdt, matkhau, hoten, gioitinh, ngaysinh, diachi " +
                        "FROM tai_khoan_bpk " +
                        "WHERE sdt = ?";
                role = "admin";
                userIdField = "sdt"; // Hoặc trường khóa chính của bảng tai_khoan_bpk
            }

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("matkhau");
                if (storedPassword.equals(password)) {
                    String userId = rs.getString(userIdField);
                    String hoTen = rs.getString("hoten");
                    String gioiTinh = rs.getString("gioitinh");
                    String ngaySinh = rs.getDate("ngaysinh") != null ? rs.getDate("ngaysinh").toString() : "";
                    String sdt = rs.getString("sdt");
                    String diaChi = rs.getString("diachi");

                    UserSession.setUserSession(userId, role, hoTen != null ? hoTen : "", gioiTinh != null ? gioiTinh : "", ngaySinh, sdt, diaChi != null ? diaChi : "");

                    JOptionPane.showMessageDialog(frame, "Chào mừng bạn, " + hoTen + "!");
                    frame.dispose();

                    if (role.equals("benhnhan")) {
                        SwingUtilities.invokeLater(() -> new BenhNhan(true).setVisible(true));
                    } else if (role.equals("bacsi")) {
                        SwingUtilities.invokeLater(() -> new BacSi(true).setVisible(true));
                    } else if (role.equals("admin")) {
                        SwingUtilities.invokeLater(() -> new Admin(true).setVisible(true));
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Sai mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Tài khoản không tồn tại hoặc không liên kết với " + selectedRole + "!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage() + "\nSQL State: " + ex.getSQLState() + "\nError Code: " + ex.getErrorCode(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}