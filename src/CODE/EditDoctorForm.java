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
    private String doctorMaBacSi;
    private DoctorDetails parent; // Tham chiếu đến DoctorDetails để làm mới bảng

    public EditDoctorForm(DoctorDetails parent, String maBacSi, String ten, String khoa, String chucVu, String trinhDo, String sdt,
                          String email, String cccd, java.sql.Date ngaySinh, String avatar) {
        super(parent, "Sửa Thông Tin Bác Sĩ", true);
        this.parent = parent; // Lưu tham chiếu đến DoctorDetails
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(10, 2, 5, 5));

        doctorMaBacSi = maBacSi;
        avatarPath = avatar;

        // Khởi tạo các trường nhập liệu
        add(new JLabel("Họ tên:"));
        tfTen = new JTextField(ten);
        add(tfTen);

        add(new JLabel("Khoa:"));
        cbKhoa = new JComboBox<>(new String[]{"Đa khoa", "Chỉnh hình", "Nội tiết", "Tim mạch", "Hô hấp"});
        cbKhoa.setSelectedItem(khoa);
        add(cbKhoa);

        add(new JLabel("Chức vụ:"));
        tfChucVu = new JTextField(chucVu);
        add(tfChucVu);

        add(new JLabel("Trình độ:"));
        tfTrinhDo = new JTextField(trinhDo);
        add(tfTrinhDo);

        add(new JLabel("SĐT:"));
        tfSdt = new JTextField(sdt);
        add(tfSdt);

        add(new JLabel("Email:"));
        tfEmail = new JTextField(email);
        add(tfEmail);

        add(new JLabel("CCCD:"));
        tfCCCD = new JTextField(cccd);
        add(tfCCCD);

        add(new JLabel("Ngày sinh:"));
        dateChooser = new JDateChooser();
        dateChooser.setDate(ngaySinh);
        add(dateChooser);

        JButton btnLuu = new JButton("Cập nhật");
        btnLuu.addActionListener(e -> updateDoctor());
        add(btnLuu);

        JButton btnHuy = new JButton("Hủy");
        btnHuy.addActionListener(e -> dispose());
        add(btnHuy);

        setVisible(true);
    }

    private void updateDoctor() {
        // Kiểm tra dữ liệu đầu vào
        if (tfTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên.");
            return;
        }
        if (tfSdt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại.");
            return;
        }
        if (tfEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập email.");
            return;
        }
        if (tfCCCD.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập CCCD.");
            return;
        }
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày sinh.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "");
             PreparedStatement pst = conn.prepareStatement(
                     "UPDATE bac_si SET hoten=?, khoa=?, nghe_nghiep=?, trinh_do=?, sdt=?, email=?, cccd=?, ngaysinh=?, avatar=? WHERE ma_bac_si=?")) {

            pst.setString(1, tfTen.getText().trim());
            pst.setString(2, cbKhoa.getSelectedItem().toString());
            pst.setString(3, tfChucVu.getText().trim());
            pst.setString(4, tfTrinhDo.getText().trim());
            pst.setString(5, tfSdt.getText().trim());
            pst.setString(6, tfEmail.getText().trim());
            pst.setString(7, tfCCCD.getText().trim());
            pst.setDate(8, new java.sql.Date(dateChooser.getDate().getTime()));
            pst.setString(9, avatarPath);
            pst.setString(10, doctorMaBacSi);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                parent.loadDoctorData("", "Tất cả"); // Làm mới bảng trong DoctorDetails
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy bác sĩ với mã: " + doctorMaBacSi);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật bác sĩ: " + ex.getMessage());
        }
    }
}