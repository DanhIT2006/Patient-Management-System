package CODE;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EditPatientForm extends JDialog {
    private JTextField tfTen, tfSdt, tfDiaChi, tfCCCD, tfNghe, tfBenh, tfYeuCau, tfThuoc, tfVienPhi, tfPhong;
    private JComboBox<String> cbGioiTinh, cbKhoa;
    private JDateChooser dateNgSinh, dateVao, dateRa;
    private String avatarPath;
    private String maBenhNhan;
    private PatientDetails parentWindow;


    public EditPatientForm(PatientDetails parentWindow, String mabenhnhan, String hoten, String gioitinh, java.sql.Date ngaysinh,
                           String sdt, String diachi, String cccd, java.sql.Date ngayvao, java.sql.Date ngayra,
                           String nghe, String khoa, String benh, String yeucau, String thuoc,
                           int vienphi, String phong, String avatar) {

        super(parentWindow, "Sửa Thông Tin Bệnh Nhân", true);
        setSize(600, 600);
        setLocationRelativeTo(parentWindow);

        JPanel mainPanel = new JPanel(new GridLayout(18, 2, 8, 6));
        mainPanel.setBackground(new Color(255, 240, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.parentWindow = parentWindow;
        this.maBenhNhan = mabenhnhan;
        this.avatarPath = avatar;

        mainPanel.add(new JLabel("Họ tên:")); tfTen = new JTextField(hoten); mainPanel.add(tfTen);
        mainPanel.add(new JLabel("Giới tính:")); cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"}); cbGioiTinh.setSelectedItem(gioitinh); mainPanel.add(cbGioiTinh);
        mainPanel.add(new JLabel("Ngày sinh:")); dateNgSinh = new JDateChooser(); dateNgSinh.setDate(ngaysinh); mainPanel.add(dateNgSinh);
        mainPanel.add(new JLabel("SĐT:")); tfSdt = new JTextField(sdt); mainPanel.add(tfSdt);
        mainPanel.add(new JLabel("Địa chỉ:")); tfDiaChi = new JTextField(diachi); mainPanel.add(tfDiaChi);
        mainPanel.add(new JLabel("CCCD:")); tfCCCD = new JTextField(cccd); mainPanel.add(tfCCCD);
        mainPanel.add(new JLabel("Ngày vào viện:")); dateVao = new JDateChooser(); dateVao.setDate(ngayvao); mainPanel.add(dateVao);
        mainPanel.add(new JLabel("Ngày ra viện:")); dateRa = new JDateChooser(); dateRa.setDate(ngayra); mainPanel.add(dateRa);
        mainPanel.add(new JLabel("Nghề nghiệp:")); tfNghe = new JTextField(nghe); mainPanel.add(tfNghe);
        mainPanel.add(new JLabel("Khoa:")); cbKhoa = new JComboBox<>(new String[]{"Đa khoa", "Chỉnh hình", "Nội tiết", "Tim mạch", "Hô hấp"}); cbKhoa.setSelectedItem(khoa); mainPanel.add(cbKhoa);
        mainPanel.add(new JLabel("Bệnh:")); tfBenh = new JTextField(benh); mainPanel.add(tfBenh);
        mainPanel.add(new JLabel("Yêu cầu:")); tfYeuCau = new JTextField(yeucau); mainPanel.add(tfYeuCau);
        mainPanel.add(new JLabel("Thuốc:")); tfThuoc = new JTextField(thuoc); mainPanel.add(tfThuoc);
        mainPanel.add(new JLabel("Viện phí:")); tfVienPhi = new JTextField(String.valueOf(vienphi)); mainPanel.add(tfVienPhi);
        mainPanel.add(new JLabel("Phòng:")); tfPhong = new JTextField(phong); mainPanel.add(tfPhong);

        JButton btnLuu = new JButton("💾 Cập nhật");
        btnLuu.setBackground(new Color(100, 200, 100));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFocusPainted(false);
        btnLuu.addActionListener(e -> updatePatient());

        JButton btnHuy = new JButton("❌ Hủy");
        btnHuy.setBackground(new Color(220, 100, 100));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFocusPainted(false);
        btnHuy.addActionListener(e -> dispose());

        mainPanel.add(btnLuu);
        mainPanel.add(btnHuy);

        JButton btnAnh = new JButton("🖼️ Chọn ảnh");
        btnAnh.setBackground(new Color(100, 150, 250));
        btnAnh.setForeground(Color.WHITE);
        btnAnh.setFocusPainted(false);
        btnAnh.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                avatarPath = fileChooser.getSelectedFile().getAbsolutePath();
                ImageIcon icon = new ImageIcon(new ImageIcon(avatarPath).getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH));
                JLabel imagePreview = new JLabel(icon);
                JOptionPane.showMessageDialog(this, imagePreview, "Ảnh đại diện đã chọn", JOptionPane.PLAIN_MESSAGE);
            }
        });
        mainPanel.add(new JLabel("")); // để cân lề
        mainPanel.add(btnAnh);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void updatePatient() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "");
             PreparedStatement pst = conn.prepareStatement(
                     "UPDATE benhnhan SET hoten=?, gioitinh=?, ngaysinh=?, sdt=?, diachi=?, cccd=?, ngayvaovien=?, ngayravien=?, nghe_nghiep=?, khoa=?, benh=?, yeu_cau=?, thuoc=?, vien_phi=?, phong=?, avatar=? WHERE mabenhnhan=?")) {

            pst.setString(1, tfTen.getText());
            pst.setString(2, cbGioiTinh.getSelectedItem().toString());
            pst.setDate(3, new java.sql.Date(dateNgSinh.getDate().getTime()));
            pst.setString(4, tfSdt.getText());
            pst.setString(5, tfDiaChi.getText());
            pst.setString(6, tfCCCD.getText());
            pst.setDate(7, new java.sql.Date(dateVao.getDate().getTime()));
            pst.setDate(8, new java.sql.Date(dateRa.getDate().getTime()));
            pst.setString(9, tfNghe.getText());
            pst.setString(10, cbKhoa.getSelectedItem().toString());
            pst.setString(11, tfBenh.getText());
            pst.setString(12, tfYeuCau.getText());
            pst.setString(13, tfThuoc.getText());
            pst.setInt(14, Integer.parseInt(tfVienPhi.getText()));
            pst.setString(15, tfPhong.getText());
            pst.setString(16, avatarPath);
            pst.setString(17, maBenhNhan);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            parentWindow.loadPatientData("", "Tất cả");
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật bệnh nhân.");
        }
    }
}
