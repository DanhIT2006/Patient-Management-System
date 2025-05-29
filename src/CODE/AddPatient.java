

package CODE;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;

public class AddPatient extends JFrame {

    private JTextField tfMaBN, tfHoTen, tfSDT, tfCCCD, tfNgheNghiep, tfKhoa, tfBenh, tfYeuCau, tfThuoc, tfVienPhi, tfPhong, tfEmail;
    private JTextArea taDiaChi;
    private JComboBox<String> cbGioiTinh;
    private JLabel lblAvatar;
    private JDateChooser dcNgaySinh, dcVaoVien, dcRaVien;
    private File avatarFile;
    private String selectedImagePath = "";

    // Database info
    private final String DB_URL = "jdbc:mysql://localhost:3306/doancoso";
    private final String USER = "root";
    private final String PASS = "";

    public AddPatient() {
        setTitle("Thêm bệnh nhân");
        setSize(950, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 230, 230));
        setLayout(null);

        JLabel title = new JLabel("THÊM BỆNH NHÂN", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(300, 10, 300, 40);
        add(title);

        int x = 50, y = 60, w = 150, h = 25, gap = 35;

        tfMaBN = addLabeledField("Mã BN:", x, y);
        tfHoTen = addLabeledField("Họ tên:", x, y += gap);
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        addLabel("Giới tính:", x, y += gap);
        cbGioiTinh.setBounds(x + w, y, 200, h);
        add(cbGioiTinh);

        addLabel("Ngày sinh:", x, y += gap);
        dcNgaySinh = new JDateChooser();
        dcNgaySinh.setBounds(x + w, y, 200, h);
        add(dcNgaySinh);

        tfSDT = addLabeledField("SĐT:", x, y += gap);
        tfCCCD = addLabeledField("CCCD:", x, y += gap);

        addLabel("Địa chỉ:", x, y += gap);
        taDiaChi = new JTextArea();
        JScrollPane sp = new JScrollPane(taDiaChi);
        sp.setBounds(x + w, y, 200, 50);
        add(sp);
        y += 60;

        addLabel("Ngày vào viện:", x, y);
        dcVaoVien = new JDateChooser();
        dcVaoVien.setBounds(x + w, y, 200, h);
        add(dcVaoVien);

        addLabel("Ngày ra viện:", x, y += gap);
        dcRaVien = new JDateChooser();
        dcRaVien.setBounds(x + w, y, 200, h);
        add(dcRaVien);

        tfNgheNghiep = addLabeledField("Nghề nghiệp:", x, y += gap);

        int x2 = 500, y2 = 60;

        tfKhoa = addLabeledField("Khoa:", x2, y2);
        tfBenh = addLabeledField("Bệnh:", x2, y2 += gap);
        tfYeuCau = addLabeledField("Yêu cầu:", x2, y2 += gap);
        tfThuoc = addLabeledField("Thuốc:", x2, y2 += gap);
        tfPhong = addLabeledField("Phòng:", x2, y2 += gap);
        tfEmail = addLabeledField("Email:", x2, y2 += gap);
        tfVienPhi = addLabeledField("Viện phí:", x2, y2 += gap);
        tfVienPhi.setEditable(false);

        addLabel("Ảnh đại diện:", x2, y2 += gap);
        lblAvatar = new JLabel();
        lblAvatar.setBounds(x2 + w, y2, 100, 100);
        lblAvatar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(lblAvatar);
        JButton btnChonAnh = new JButton("Chọn ảnh");
        btnChonAnh.setBounds(x2 + w + 110, y2 + 35, 100, 25);
        btnChonAnh.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                avatarFile = chooser.getSelectedFile();
                selectedImagePath = avatarFile.getAbsolutePath();
                lblAvatar.setIcon(new ImageIcon(new ImageIcon(selectedImagePath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            }
        });
        add(btnChonAnh);

        JButton btnThem = new JButton("Thêm bệnh nhân");
        btnThem.setBounds(300, 630, 150, 30);
        btnThem.addActionListener(e -> {
            String email = tfEmail.getText().trim();
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(null, "Email không hợp lệ!");
                return;
            }

            insertBenhNhanToDatabase();
        });
        add(btnThem);

        JButton btnXemTruoc = new JButton("In hóa đơn");
        btnXemTruoc.setBounds(500, 630, 120, 30);
        btnXemTruoc.addActionListener(e -> {
            showInvoicePreview();
            exportInvoiceToPDF();
        });
        add(btnXemTruoc);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(regex);
    }


    private JTextField addLabeledField(String label, int x, int y) {
        addLabel(label, x, y);
        JTextField tf = new JTextField();
        tf.setBounds(x + 150, y, 200, 25);
        add(tf);
        return tf;
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 150, 25);
        add(label);
    }

    private String getLoaiPhong(int soPhong) {
        if (soPhong >= 1 && soPhong <= 200) return "BHYT";
        else if (soPhong >= 201 && soPhong <= 300) return "PĐB";
        else if (soPhong >= 301 && soPhong <= 350) return "ĐTTYC";
        else return "Không hợp lệ";
    }

    private int getDonGia(int soPhong) {
        if (soPhong >= 1 && soPhong <= 200) return 60000;
        else if (soPhong >= 201 && soPhong <= 300) return 800000;
        else if (soPhong >= 301 && soPhong <= 350) return 4000000;
        else return 0;
    }

    private long tinhSoNgay(Date from, Date to) {
        long diff = to.getTime() - from.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }



    private void showInvoicePreview() {

        String phongText = tfPhong.getText().trim();
        if (phongText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số phòng.");
            return;
        }
        int soPhong;
        try {
            soPhong = Integer.parseInt(phongText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số phòng phải là số.");
            return;
        }

        String maBN = tfMaBN.getText();
        String hoTen = tfHoTen.getText();
        String thuoc = tfThuoc.getText();
        String benh = tfBenh.getText();
        Date ngayVao = dcVaoVien.getDate();
        Date ngayRa = dcRaVien.getDate();
        String avatarPath = selectedImagePath;


        String loaiPhong = getLoaiPhong(soPhong);
        int donGia = getDonGia(soPhong);
        long soNgay = tinhSoNgay(ngayVao, ngayRa);
        int tongVienPhi = (int) (soNgay * donGia);

        tfVienPhi.setText(String.valueOf(tongVienPhi));

        ImageIcon avatar = new ImageIcon(new ImageIcon(avatarPath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        JTextArea area = new JTextArea(
                "Mã BN: " + maBN + "\n" +
                        "Họ tên: " + hoTen + "\n" +
                        "Phòng: " + soPhong + " (" + loaiPhong + ")\n" +
                        "Bệnh: " + benh + "\n" +
                        "Thuốc: " + thuoc + "\n" +
                        "Ngày vào viện: " + ngayVao + "\n" +
                        "Ngày ra viện: " + ngayRa + "\n" +
                        "Số ngày: " + soNgay + "\n" +
                        "Đơn giá: " + donGia + " đ/ngày\n" +
                        "Tổng viện phí: " + tongVienPhi + " đ"
        );
        area.setEditable(false);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(avatar), BorderLayout.WEST);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        JOptionPane.showMessageDialog(null, panel, "Xem trước hóa đơn", JOptionPane.INFORMATION_MESSAGE);
    }

    private void insertBenhNhanToDatabase() {

        Connection conn = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;

        String phongText = tfPhong.getText().trim();
        if (phongText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số phòng.");
            return;
        }
        int soPhong;
        try {
            soPhong = Integer.parseInt(phongText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số phòng phải là số.");
            return;
        }

        Date ngayVao = dcVaoVien.getDate();
        Date ngayRa = dcRaVien.getDate();
        int donGia = getDonGia(soPhong);
        long soNgay = tinhSoNgay(ngayVao, ngayRa);
        int vienPhi = (int) (donGia * soNgay);
        tfVienPhi.setText(String.valueOf(vienPhi));

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "");

            String ma;
            do {
                ma = "BN" + UUID.randomUUID().toString().substring(0, 8);
                PreparedStatement check = conn.prepareStatement("SELECT COUNT(*) FROM benhnhan WHERE mabenhnhan = ?");
                check.setString(1, ma);
                ResultSet rs = check.executeQuery();
                rs.next();
                if (rs.getInt(1) == 0) break;
            } while (true);
            String hoten = tfHoTen.getText().trim();
            java.sql.Date ngaysinh = new java.sql.Date(dcNgaySinh.getDate().getTime());
            String gioitinh = (String) cbGioiTinh.getSelectedItem();
            String diachi = taDiaChi.getText().trim();
            String sdt = tfSDT.getText().trim();
            String cccd = tfCCCD.getText().trim();
            java.sql.Date ngaynhap = new java.sql.Date(dcVaoVien.getDate().getTime());
            java.sql.Date ngayxuat = new java.sql.Date(dcRaVien.getDate().getTime());
            String nghe = tfNgheNghiep.getText().trim();
            String khoa = tfKhoa.getText().trim();
            String benh = tfBenh.getText().trim();
            String yeucau = tfYeuCau.getText().trim();
            String thuoc = tfThuoc.getText().trim();
            int phong = Integer.parseInt(tfPhong.getText().trim());
            int songay = (int) TimeUnit.DAYS.convert(ngayxuat.getTime() - ngaynhap.getTime(), TimeUnit.MILLISECONDS);
            int dongia = getDonGia(phong);
            int vienphi = songay * dongia;
            tfVienPhi.setText(String.valueOf(vienphi));
            String email = tfEmail.getText().trim();
            String avatar = selectedImagePath;


            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
                return;
            }


            String sql1 = "INSERT INTO benhnhan (mabenhnhan, hoten, ngaysinh, gioitinh, diachi, sdt, cccd, ngayvaovien, ngayravien, nghe_nghiep, khoa, benh, yeu_cau, thuoc, vien_phi, phong, avatar, email) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst1 = conn.prepareStatement(sql1);
            pst1.setString(1, ma);
            pst1.setString(2, hoten);
            pst1.setDate(3, ngaysinh);
            pst1.setString(4, gioitinh);
            pst1.setString(5, diachi);
            pst1.setString(6, sdt);
            pst1.setString(7, cccd);
            pst1.setDate(8, ngaynhap);
            pst1.setDate(9, ngayxuat);
            pst1.setString(10, nghe);
            pst1.setString(11, khoa);
            pst1.setString(12, benh);
            pst1.setString(13, yeucau);
            pst1.setString(14, thuoc);
            pst1.setInt(15, vienphi);
            pst1.setInt(16, phong);
            pst1.setString(17, avatar);
            pst1.setString(18, email);
            pst1.executeUpdate();

            String matkhau = UUID.randomUUID().toString().substring(0, 8);

            String sql2 = "INSERT INTO tai_khoan (sdt, matkhau, email) VALUES (?, ?, ?)";
            pst2 = conn.prepareStatement(sql2);
            pst2.setString(1, sdt);
            pst2.setString(2, matkhau);
            pst2.setString(3, email);
            pst2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Thêm bệnh nhân và tạo tài khoản thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Thêm bệnh nhân và tạo tài khoản thành công!");
        } finally {
            try {
                if (pst1 != null) pst1.close();
                if (pst2 != null) pst2.close();
                if (conn != null) conn.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }
    private void exportInvoiceToPDF() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xuất hóa đơn?", "Xác nhận", JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) {
            try {
                Document doc = new Document();
                String fileName = "hoadon_" + tfMaBN.getText() + ".pdf";
                PdfWriter.getInstance(doc, new FileOutputStream(fileName));
                doc.open();

                // Tải font tiếng Việt từ file
                BaseFont bf = BaseFont.createFont("src/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 16, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.NORMAL);

                // Ghi nội dung hóa đơn
                doc.add(new Paragraph("HÓA ĐƠN BỆNH NHÂN", fontTitle));
                doc.add(new Paragraph("Mã BN: " + tfMaBN.getText(), fontNormal));
                doc.add(new Paragraph("Họ tên: " + tfHoTen.getText(), fontNormal));
                doc.add(new Paragraph("Phòng: " + tfPhong.getText(), fontNormal));
                doc.add(new Paragraph("Bệnh: " + tfBenh.getText(), fontNormal));
                doc.add(new Paragraph("Thuốc: " + tfThuoc.getText(), fontNormal));
                doc.add(new Paragraph("Ngày vào viện: " + dcVaoVien.getDate(), fontNormal));
                doc.add(new Paragraph("Ngày ra viện: " + dcRaVien.getDate(), fontNormal));
                doc.add(new Paragraph("Viện phí: " + tfVienPhi.getText() + " đ", fontNormal));

                doc.close();
                JOptionPane.showMessageDialog(this, "Đã xuất hóa đơn ra file: " + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất hóa đơn: " + e.getMessage());
            }
        }
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddPatient().setVisible(true));
    }
}
