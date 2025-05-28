package CODE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;

public class EditDoctorForm extends JDialog {
    private JTextField tfTen, tfChucVu, tfTrinhDo, tfSdt, tfEmail, tfCCCD;
    private JComboBox<String> cbKhoa;
    private JDateChooser dateChooser;
    private String avatarPath;
    private int doctorId;

    public EditDoctorForm(JFrame parent, int id, String ten, String khoa, String chucVu, String trinhDo, String sdt,
                          String email, String cccd, java.sql.Date ngaySinh, String avatar) {
        super(parent, "Sửa Thông Tin Bác Sĩ", true);
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(10, 2, 5, 5));

        doctorId = id;
        avatarPath = avatar;

        add(new JLabel("Họ tên:")); tfTen = new JTextField(ten); add(tfTen);
        add(new JLabel("Khoa:")); cbKhoa = new JComboBox<>(new String[]{"Đa khoa", "Chỉnh hình", "Nội tiết", "Tim mạch", "Hô hấp"});
        cbKhoa.setSelectedItem(khoa); add(cbKhoa);
        add(new JLabel("Chức vụ:")); tfChucVu = new JTextField(chucVu); add(tfChucVu);
        add(new JLabel("Trình độ:")); tfTrinhDo = new JTextField(trinhDo); add(tfTrinhDo);
        add(new JLabel("SĐT:")); tfSdt = new JTextField(sdt); add(tfSdt);
        add(new JLabel("Email:")); tfEmail = new JTextField(email); add(tfEmail);
        add(new JLabel("CCCD:")); tfCCCD = new JTextField(cccd); add(tfCCCD);
        add(new JLabel("Ngày sinh:")); dateChooser = new JDateChooser(); dateChooser.setDate(ngaySinh); add(dateChooser);

        JButton btnLuu = new JButton("Cập nhật");
        btnLuu.addActionListener(e -> updateDoctor());
        add(btnLuu);

        JButton btnHuy = new JButton("Hủy");
        btnHuy.addActionListener(e -> dispose());
        add(btnHuy);

        setVisible(true);
    }

    private void updateDoctor() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "");
             PreparedStatement pst = conn.prepareStatement(
                     "UPDATE bac_si SET ten=?, khoa=?, chuc_vu=?, trinh_do=?, so_dien_thoai=?, email=?, cccd=?, ngay_sinh=?, avatar=? WHERE id=?")) {

            pst.setString(1, tfTen.getText());
            pst.setString(2, cbKhoa.getSelectedItem().toString());
            pst.setString(3, tfChucVu.getText());
            pst.setString(4, tfTrinhDo.getText());
            pst.setString(5, tfSdt.getText());
            pst.setString(6, tfEmail.getText());
            pst.setString(7, tfCCCD.getText());
            pst.setDate(8, new java.sql.Date(dateChooser.getDate().getTime()));
            pst.setString(9, avatarPath);
            pst.setInt(10, doctorId);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật bác sĩ.");
        }
    }
}

