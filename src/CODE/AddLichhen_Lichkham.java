package CODE;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class AddLichhen_Lichkham extends JFrame {
    private JTextField tfMaBenhNhan, tfTenKham, tfBacSi, tfPhong, tfToa, tfMaBacSi;
    private JDateChooser dateChooser;
    private JComboBox<String> cbTrangThai;
    private JButton btnLuu;

    public AddLichhen_Lichkham() {
        setTitle("Thêm lịch hẹn/lịch khám");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblMaBN = new JLabel("Mã bệnh nhân:");
        tfMaBenhNhan = new JTextField();

        JLabel lblNgay = new JLabel("Ngày:");
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        JLabel lblTenKham = new JLabel("Tên khám:");
        tfTenKham = new JTextField();

        JLabel lblBacSi = new JLabel("Bác sĩ:");
        tfBacSi = new JTextField();

        JLabel lblPhong = new JLabel("Phòng:");
        tfPhong = new JTextField();

        JLabel lblToa = new JLabel("Toa:");
        tfToa = new JTextField();

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        cbTrangThai = new JComboBox<>(new String[]{"Chưa khám", "Đã khám"});

        JLabel lblMaBacSi = new JLabel("Mã bác sĩ:");
        tfMaBacSi = new JTextField();

        btnLuu = new JButton("Lưu lịch hẹn");

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblMaBN, gbc);
        gbc.gridx = 1;
        panel.add(tfMaBenhNhan, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblNgay, gbc);
        gbc.gridx = 1;
        panel.add(dateChooser, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblTenKham, gbc);
        gbc.gridx = 1;
        panel.add(tfTenKham, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblBacSi, gbc);
        gbc.gridx = 1;
        panel.add(tfBacSi, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(lblPhong, gbc);
        gbc.gridx = 1;
        panel.add(tfPhong, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(lblToa, gbc);
        gbc.gridx = 1;
        panel.add(tfToa, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(lblTrangThai, gbc);
        gbc.gridx = 1;
        panel.add(cbTrangThai, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(lblMaBacSi, gbc);
        gbc.gridx = 1;
        panel.add(tfMaBacSi, gbc);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnLuu, gbc);

        add(panel);

        btnLuu.addActionListener(e -> luuLichHen());
    }

    private void luuLichHen() {
        String maBN = tfMaBenhNhan.getText().trim();
        java.util.Date ngay = dateChooser.getDate();
        String tenKham = tfTenKham.getText().trim();
        String bacSi = tfBacSi.getText().trim();
        String phong = tfPhong.getText().trim();
        String toa = tfToa.getText().trim();
        String trangThai = (String) cbTrangThai.getSelectedItem();
        String maBacSi = tfMaBacSi.getText().trim();

        if (maBN.isEmpty() || ngay == null || tenKham.isEmpty() || bacSi.isEmpty() || phong.isEmpty() || toa.isEmpty() || maBacSi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ngayStr = sdf.format(ngay);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "")) {
            // Lưu vào bảng lichhen
            String sqlLichHen = "INSERT INTO lichhen (ma_benh_nhan, ngay, ten_kham, bac_si, phong, toa, trang_thai, ma_bac_si) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psLichHen = conn.prepareStatement(sqlLichHen);
            psLichHen.setString(1, maBN);
            psLichHen.setString(2, ngayStr);
            psLichHen.setString(3, tenKham);
            psLichHen.setString(4, bacSi);
            psLichHen.setString(5, phong);
            psLichHen.setString(6, toa);
            psLichHen.setString(7, trangThai);
            psLichHen.setString(8, maBacSi);
            int rowsLichHen = psLichHen.executeUpdate();

            if (rowsLichHen > 0) {
                // Lấy tên bệnh nhân từ bảng benhnhan
                String sqlGetTenBenhNhan = "SELECT hoten FROM benhnhan WHERE mabenhnhan = ?";
                PreparedStatement psGetTenBenhNhan = conn.prepareStatement(sqlGetTenBenhNhan);
                psGetTenBenhNhan.setString(1, maBN);
                ResultSet rs = psGetTenBenhNhan.executeQuery();
                String hoten = "";
                if (rs.next()) {
                    hoten = rs.getString("hoten");
                }
                rs.close();
                psGetTenBenhNhan.close();

                if (hoten.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy tên bệnh nhân với mã: " + maBN);
                    return;
                }

                // Tự động điền vào bảng lichkham
                String sqlLichKham = "INSERT INTO lichkham (ngay, kham, sdt, benhnhan, phong, toa, trangthai, ma_benh_nhan, ma_bac_si) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement psLichKham = conn.prepareStatement(sqlLichKham);
                psLichKham.setString(1, ngayStr); // ngay
                psLichKham.setString(2, tenKham); // kham (ánh xạ từ ten_kham)
                psLichKham.setString(3, ""); // sdt (để trống hoặc lấy từ benhnhan nếu có)
                psLichKham.setString(4, hoten); // benhnhan (lấy từ benhnhan)
                psLichKham.setString(5, phong); // phong
                psLichKham.setString(6, toa); // toa
                psLichKham.setString(7, trangThai); // trangthai
                psLichKham.setString(8, maBN); // ma_benh_nhan
                psLichKham.setString(9, maBacSi); // ma_bac_si

                int rowsLichKham = psLichKham.executeUpdate();
                if (rowsLichKham > 0) {
                    JOptionPane.showMessageDialog(this, "Đã lưu lịch hẹn và lịch khám thành công.");
                } else {
                    JOptionPane.showMessageDialog(this, "Lưu lịch khám thất bại.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lưu lịch hẹn thất bại.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddLichhen_Lichkham().setVisible(true));
    }
}