package CODE;

import javax.swing.*;
import java.awt.*;

public class BoPhanKhac extends JFrame {
    public BoPhanKhac(boolean b) {
        setTitle("Chức năng khác");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(new Color(230, 255, 250));

        JLabel titleLabel = new JLabel("Chức năng khác", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 102, 102));
        panel.add(titleLabel);

        JButton btnAddDoctor = new JButton(" Thêm Bác Sĩ");
        JButton btnAddPatient = new JButton("Thêm Bệnh Nhân");
        JButton btnDoctorDetails = new JButton(" Chi Tiết Bác Sĩ");
        JButton btnPatientDetails = new JButton(" Chi Tiết Bệnh Nhân");
        JButton btnBack = new JButton(" Quay về trang chủ");

        JButton[] buttons = {btnAddDoctor, btnAddPatient, btnDoctorDetails, btnPatientDetails, btnBack};
        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            btn.setBackground(new Color(102, 204, 204));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            panel.add(btn);
        }

        btnAddDoctor.addActionListener(e -> new AddDoctor().setVisible(true));
        btnAddPatient.addActionListener(e -> new AddPatient().setVisible(true));
        btnDoctorDetails.addActionListener(e -> new DoctorDetails().setVisible(true));
        btnPatientDetails.addActionListener(e -> new PatientDetails().setVisible(true));
        btnBack.addActionListener(e -> new Login().setVisible(true));

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BoPhanKhac(true));
    }
}


