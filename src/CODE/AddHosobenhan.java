package CODE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddHosobenhan extends JFrame {

    private JTextField tfMaBenhNhan, tfLyDo, tfTomTat, tfTienSu, tfKetQua, tfNoiKhoa, tfPhauThuat, tfTinhTrang;
    private JButton btnLuu;

    public AddHosobenhan() {
        setTitle("Thêm Hồ Sơ Bệnh Án");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 2, 10, 10));

        add(new JLabel("Mã bệnh nhân:"));
        tfMaBenhNhan = new JTextField();
        add(tfMaBenhNhan);

        add(new JLabel("Lý do vào viện:"));
        tfLyDo = new JTextField();
        add(tfLyDo);

        add(new JLabel("Tóm tắt bệnh lý:"));
        tfTomTat = new JTextField();
        add(tfTomTat);

        add(new JLabel("Tiền sử bệnh:"));
        tfTienSu = new JTextField();
        add(tfTienSu);

        add(new JLabel("Kết quả xét nghiệm:"));
        tfKetQua = new JTextField();
        add(tfKetQua);

        add(new JLabel("Nội khoa:"));
        tfNoiKhoa = new JTextField();
        add(tfNoiKhoa);

        add(new JLabel("Phẫu thuật:"));
        tfPhauThuat = new JTextField();
        add(tfPhauThuat);

        add(new JLabel("Tình trạng ra viện:"));
        tfTinhTrang = new JTextField();
        add(tfTinhTrang);

        btnLuu = new JButton("Lưu hồ sơ");
        add(btnLuu);
        add(new JLabel());

        btnLuu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                luuHoSoBenhAn();
            }
        });

        setVisible(true);
    }

    private void luuHoSoBenhAn() {
        String maBenhNhan = tfMaBenhNhan.getText().trim();
        String lyDo = tfLyDo.getText().trim();
        String tomTat = tfTomTat.getText().trim();
        String tienSu = tfTienSu.getText().trim();
        String ketQua = tfKetQua.getText().trim();
        String noiKhoa = tfNoiKhoa.getText().trim();
        String phauThuat = tfPhauThuat.getText().trim();
        String tinhTrang = tfTinhTrang.getText().trim();

        if (maBenhNhan.isEmpty() || lyDo.isEmpty() || tomTat.isEmpty() || tienSu.isEmpty() ||
                ketQua.isEmpty() || noiKhoa.isEmpty() || phauThuat.isEmpty() || tinhTrang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tất cả các thông tin!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "")) {

            // Kiểm tra mã bệnh nhân có tồn tại không
            if (!kiemTraMaBenhNhan(maBenhNhan, conn)) {
                JOptionPane.showMessageDialog(this, "Mã bệnh nhân không tồn tại trong hệ thống!", "Lỗi khóa ngoại", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO ho_so_benh_an (ma_benh_nhan, ly_do_vao_vien, tom_tat_benh_ly, tien_su_benh, ket_qua_xet_nghiem, noi_khoa, phau_thuat, tinh_trang_ra_vien) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, maBenhNhan);
            stmt.setString(2, lyDo);
            stmt.setString(3, tomTat);
            stmt.setString(4, tienSu);
            stmt.setString(5, ketQua);
            stmt.setString(6, noiKhoa);
            stmt.setString(7, phauThuat);
            stmt.setString(8, tinhTrang);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Thêm hồ sơ thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm hồ sơ.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi kết nối hoặc ghi dữ liệu vào cơ sở dữ liệu.", "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean kiemTraMaBenhNhan(String maBenhNhan, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM benhnhan WHERE mabenhnhan = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maBenhNhan);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddHosobenhan().setVisible(true));
    }
}
