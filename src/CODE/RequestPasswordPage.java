package CODE;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import java.util.Random;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestPasswordPage {
    private JFrame frame;
    private JTextField phoneField;
    private JTextField emailField;
    private String verificationCode;


    public RequestPasswordPage() {
        frame = new JFrame("Yêu cầu cấp mật khẩu");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel);

        JLabel phoneLabel = new JLabel("Nhập Số điện thoại:");
        phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setMaximumSize(new Dimension(250, 30));
        phoneField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(phoneField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel emailLabel = new JLabel("Nhập Email:");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(250, 30));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(emailField);

        JButton sendButton = new JButton("Gửi mật khẩu");
        sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sendButton.addActionListener(e -> sendPassword());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(sendButton);

        frame.setVisible(true);
    }

    private void sendPassword() {
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Vui lòng nhập đầy đủ Số điện thoại và Email!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra tài khoản tồn tại
        String query = "SELECT * FROM taikhoan WHERE sdt = ? AND email = ?";
        try (PreparedStatement ps = ConnectionDatabase.getConnection().prepareStatement(query)) {
            ps.setString(1, phone);
            ps.setString(2, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Tài khoản hợp lệ
                verificationCode = generateVerificationCode(); // Tạo mã xác nhận 6 chữ số

                if (sendVerificationCode(email, verificationCode)) {
                    JOptionPane.showMessageDialog(frame, "Đã gửi mã xác nhận tới email!");
                    showVerificationDialog(phone, email); // Mở cửa sổ nhập mã xác nhận
                } else {
                    JOptionPane.showMessageDialog(frame, "Gửi mã xác nhận thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(frame, "Số điện thoại hoặc Email không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateVerificationCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    private boolean sendVerificationCode(String toEmail, String code) {
        final String fromEmail = "danhvt.24it@vku.udn.vn";
        final String password = "ofbclvzicxvvlosc";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã xác nhận cấp mật khẩu");
            message.setText("Mã xác nhận của bạn là: " + code);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showVerificationDialog(String phone, String email) {
        String inputCode = JOptionPane.showInputDialog(frame, "Nhập mã xác nhận đã gửi tới email:");

        if (inputCode != null) {
            if (inputCode.equals(verificationCode)) {
                // Mã đúng ➔ cập nhật mật khẩu
                String newPassword = generateRandomPassword(8);

                String updateQuery = "UPDATE taikhoan SET matkhau = ? WHERE sdt = ?";
                try (PreparedStatement updatePs = ConnectionDatabase.getConnection().prepareStatement(updateQuery)) {
                    updatePs.setString(1, newPassword);
                    updatePs.setString(2, phone);
                    int updatedRows = updatePs.executeUpdate();

                    if (updatedRows > 0) {
                        if (sendEmail(email, newPassword)) {
                            JOptionPane.showMessageDialog(frame, "Cấp mật khẩu mới thành công! Mật khẩu đã được gửi tới email.");
                            frame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(frame, "Gửi mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Cập nhật mật khẩu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Lỗi cập nhật mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Mã xác nhận không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private boolean sendEmail(String toEmail, String newPassword) {
        final String fromEmail = "danhvt.24it@vku.udn.vn"; // Email gửi (bệnh viện)
        final String password = "ofbclvzicxvvlosc";     // Mật khẩu ứng dụng

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mật khẩu đăng nhập hệ thống bệnh viện");
            message.setText("Mật khẩu mới của bạn là: " + newPassword);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

}


