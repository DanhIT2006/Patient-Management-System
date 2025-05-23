package CODE;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BenhNhan extends JFrame {

    private boolean isLoggedIn = false;

    private JLabel lblHoTen, lblGioiTinh, lblNgaySinh, lblSDT, lblDiaChi,lblCCCD,lblMaBenhNhan,lblNgayVaoVien,lblNgayRaVien;
    private JLabel lblLyDoVaoVien, lblTomTatBenhLy,lblTienSuBenh,lblKetQuaXetNghiem,lblPhuongPhapDieuTri,lblNoiKhoa,lblPhauThuat,lblTinhTrangRaVien;

    public BenhNhan(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
        initUI(); // gọi phần setup UI ở đây
    }

    private Image[] bannerImages;
    private int currentBannerIndex = 0;

    public BenhNhan() {
        this(false);
    }


    private void initUI() {
        UIManager.put("Menu.selectionBackground", new Color(0, 102, 204));
        UIManager.put("Menu.selectionForeground", Color.WHITE);
        UIManager.put("MenuItem.selectionBackground", new Color(0, 102, 204));
        UIManager.put("MenuItem.selectionForeground", Color.WHITE);

        setTitle("Patient Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 890);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        MarqueePanel marqueePanel = new MarqueePanel("Welcome to the Patient Management System!");

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bannerImages != null && bannerImages.length > 0) {
                    g.drawImage(bannerImages[currentBannerIndex], 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        bannerImages = new Image[]{
                new ImageIcon(getClass().getResource("/anhnen1.jpg")).getImage(),
                new ImageIcon(getClass().getResource("/anhnen2.jpg")).getImage(),
                new ImageIcon(getClass().getResource("/anhnen3.jpg")).getImage()
        };

        Timer bannerTimer = new Timer(2000, e -> {
            currentBannerIndex = (currentBannerIndex + 1) % bannerImages.length;
            backgroundPanel.repaint();
        });
        bannerTimer.start();

        backgroundPanel.setPreferredSize(new Dimension(0, 350));
        backgroundPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(marqueePanel, BorderLayout.NORTH);
        topPanel.add(backgroundPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(0, 102, 204));
        menuBar.setPreferredSize(new Dimension(0, 70));
        menuBar.setBorderPainted(false);

        Font menuFont = new Font("Segoe UI", Font.BOLD, 16);
        Color normalColor = new Color(0, 102, 204);
        Color hoverColor = new Color(0, 102, 204);

        JMenu homeMenu = createMenu("Trang chủ", menuFont, normalColor, hoverColor);
        JMenu patientMenu = createMenu("Chi tiết bệnh nhân", menuFont, normalColor, hoverColor);
        JMenu appointmentMenu = createMenu("Lịch hẹn", menuFont, normalColor, hoverColor);
        JMenu recordMenu = createMenu("Hồ sơ bệnh án", menuFont, normalColor, hoverColor);
        JMenu contactMenu = createMenu("Liên hệ", menuFont, normalColor, hoverColor);
        JMenu commentMenu = createMenu("Phản hồi & Đánh giá", menuFont, normalColor, hoverColor);

        menuBar.add(homeMenu);
        menuBar.add(patientMenu);
        menuBar.add(appointmentMenu);
        menuBar.add(recordMenu);
        menuBar.add(contactMenu);
        menuBar.add(commentMenu);

        menuBar.add(Box.createHorizontalGlue());

        JPanel accountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        accountPanel.setOpaque(false);

        JButton loginButton = new JButton("Đăng nhập");
        loginButton.setFocusPainted(false);
        loginButton.setBackground(new Color(30, 144, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        loginButton.addActionListener(e -> {
            dispose(); // Close the current BenhNhan frame
            new LoginPage(); // Open a new LoginPage
        });

        accountPanel.add(loginButton);

        if (isLoggedIn) {
            loginButton.setVisible(false);

            JLabel accountLabel = new JLabel("Tài khoản");
            accountLabel.setForeground(Color.WHITE);
            accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            accountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            CardLayout cardLayout_tk = new CardLayout();
            JPanel mainPanel_tk = new JPanel(cardLayout_tk);

            accountLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem infoItem = new JMenuItem("Thông tin tài khoản");
                    JMenuItem logoutItem = new JMenuItem("Đăng xuất");

                    infoItem.addActionListener(evt -> {
                        cardLayout_tk.show(mainPanel_tk, "account"); // Chuyển sang bảng tài khoản
                    });

                    logoutItem.addActionListener(evt -> {
                        UserSession.clear();
                        dispose();
                        new BenhNhan(false);
                    });

                    menu.add(infoItem);
                    menu.add(logoutItem);
                    menu.show(accountLabel, e.getX(), e.getY());
                }
            });

            accountLabel.setBounds(250, 20, 100, 30);
            accountPanel.setLayout(null);
            accountPanel.add(accountLabel);
        }

        menuBar.add(accountPanel);
        setJMenuBar(menuBar);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.add(topPanel, BorderLayout.NORTH);
        JPanel centerHome = new JPanel();
        centerHome.setBackground(Color.WHITE);
        homePanel.add(centerHome, BorderLayout.CENTER);

        JPanel patientPanel = new JPanel(new BorderLayout());
        patientPanel.setBackground(Color.WHITE);
        patientPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle_ctbn = new JLabel("Thông tin chi tiết", SwingConstants.CENTER);
        lblTitle_ctbn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle_ctbn.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        patientPanel.add(lblTitle_ctbn, BorderLayout.NORTH);

        // Panel chứa tất cả
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 50, 10, 50); // Khoảng cách giữa các ô
        contentPanel.setBackground(Color.WHITE);

        JLabel lblAvatar = new JLabel();
        lblAvatar.setPreferredSize(new Dimension(150, 180));
        lblAvatar.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton btnUploadImage = new JButton("Tải ảnh");

        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.add(lblAvatar, BorderLayout.CENTER);
        avatarPanel.add(btnUploadImage, BorderLayout.SOUTH);
        avatarPanel.setBackground(Color.WHITE);

        // 2. Các ô nhập liệu
        JTextField tfMaBN = new JTextField(13);
        JTextField tfHo = new JTextField(13);
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField tfNgaySinh = new JTextField(13);
        JTextField tfCMND = new JTextField(13);
        JTextField tfSDT = new JTextField(13);
        JTextField tfNgheNghiep = new JTextField(13);
        JComboBox<String> cbDoiTuong = new JComboBox<>(new String[]{"Bình thường", "Ưu tiên"});
        JTextArea taDiaChi = new JTextArea(3,20);
        taDiaChi.setLineWrap(true);
        taDiaChi.setWrapStyleWord(true);
        taDiaChi.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // 3. Thông tin khám
        JTextField tfNgayKham = new JTextField(13);
        JTextField tfMaBS = new JTextField(13);
        JTextField tfBacSi = new JTextField(15);
        JTextArea taYeuCauKham = new JTextArea(3, 20);

        // 4. Add các thành phần vào form
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 5;
        contentPanel.add(avatarPanel, gbc);

        gbc.gridheight = 1;
        gbc.gridx = 1; gbc.gridy = 0; contentPanel.add(new JLabel("Mã bệnh nhân:"), gbc);
        gbc.gridx = 2; contentPanel.add(tfMaBN, gbc);

        gbc.gridx = 1; gbc.gridy = 1; contentPanel.add(new JLabel("Họ và Tên:"), gbc);
        gbc.gridx = 2; contentPanel.add(tfHo, gbc);

        gbc.gridx = 1; gbc.gridy = 2; contentPanel.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 2; contentPanel.add(cbGioiTinh, gbc);

        gbc.gridx = 1; gbc.gridy = 3; contentPanel.add(new JLabel("Ngày sinh:"), gbc);
        gbc.gridx = 2; contentPanel.add(tfNgaySinh, gbc);

        // Dòng mới
        gbc.gridx = 1; gbc.gridy = 4; contentPanel.add(new JLabel("Số CCCD:"), gbc);
        gbc.gridx = 2; contentPanel.add(tfCMND, gbc);

        gbc.gridx = 1; gbc.gridy = 5; contentPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 2; contentPanel.add(tfSDT, gbc);

        gbc.gridx = 1; gbc.gridy = 6; contentPanel.add(new JLabel("Nghề nghiệp:"), gbc);
        gbc.gridx = 2; contentPanel.add(tfNgheNghiep, gbc);

        // Thông tin khám bệnh
        gbc.gridx = 3; gbc.gridy = 0; contentPanel.add(new JLabel("Ngày vào viện:"), gbc);
        gbc.gridx = 4; contentPanel.add(tfNgayKham, gbc);

        gbc.gridx = 3; gbc.gridy = 1; contentPanel.add(new JLabel("Mã bác sĩ:"), gbc);
        gbc.gridx = 4; contentPanel.add(tfMaBS, gbc);

        gbc.gridx = 3; gbc.gridy = 2; contentPanel.add(new JLabel("Bác sĩ phụ trách:"), gbc);
        gbc.gridx = 4; contentPanel.add(tfBacSi, gbc);

        gbc.gridx = 3; gbc.gridy = 3; contentPanel.add(new JLabel("Yêu cầu khám:"), gbc);
        gbc.gridx = 4; contentPanel.add(new JScrollPane(taYeuCauKham), gbc);

        gbc.gridx = 3; gbc.gridy = 4; contentPanel.add(new JLabel("Đối tượng:"), gbc);
        gbc.gridx = 4; contentPanel.add(cbDoiTuong, gbc);

        gbc.gridx = 3; gbc.gridy = 5; contentPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 4; contentPanel.add(taDiaChi, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton("Nhập mới"));
        buttonPanel.add(new JButton("Lưu"));
        buttonPanel.add(new JButton("Đóng"));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));

        // Thêm vào patientPanel
        patientPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        patientPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Load data into the fields
        if (isLoggedIn && UserSession.maBenhNhan != null && !UserSession.maBenhNhan.trim().isEmpty()) {
            loadPatientData(tfMaBN, tfHo, cbGioiTinh, tfNgaySinh, tfCMND, tfSDT, tfNgheNghiep, cbDoiTuong, taDiaChi, tfNgayKham, tfMaBS, tfBacSi, taYeuCauKham);
        }

        JPanel appointmentPanel = new JPanel(new BorderLayout());
        appointmentPanel.setBackground(Color.WHITE);

        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin bệnh nhân"));
        infoPanel.setBackground(new Color(245, 245, 245));

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Ngày", "Khám", "Bác sĩ khám", "Phòng", "Tòa", "Trạng thái"});

        JTable table = new JTable(tableModel);
        table.setEnabled(false);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);

        // Method to load appointment data
        Runnable loadAppointments = () -> {
            tableModel.setRowCount(0); // Clear existing rows
            try (Connection conn = ConnectionDatabase.getConnection()) {
                // Debug: Check UserSession.maBenhNhan
                System.out.println("UserSession.maBenhNhan at loadAppointments: " + UserSession.maBenhNhan);

                // Load patient info
                String query1 = "SELECT * FROM benhnhan WHERE mabenhnhan = ?";
                PreparedStatement stmt1 = conn.prepareStatement(query1);
                stmt1.setString(1, UserSession.maBenhNhan != null ? UserSession.maBenhNhan.trim() : "");
                ResultSet rs1 = stmt1.executeQuery();

                infoPanel.removeAll(); // Clear existing labels
                if (rs1.next()) {
                    String hoTen = rs1.getString("hoten");
                    String gioiTinh = rs1.getString("gioitinh");
                    String ngaySinh = rs1.getDate("ngaysinh").toString();
                    String sdt = rs1.getString("sdt");
                    String diaChi = rs1.getString("diachi");

                    infoPanel.add(new JLabel("Họ và tên: " + hoTen));
                    infoPanel.add(new JLabel("Giới tính: " + gioiTinh));
                    infoPanel.add(new JLabel("Ngày sinh: " + ngaySinh));
                    infoPanel.add(new JLabel("SĐT: " + sdt));
                    infoPanel.add(new JLabel("Địa chỉ: " + diaChi));
                    infoPanel.add(new JLabel("Mã bệnh nhân: " + UserSession.maBenhNhan));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                } else {
                    infoPanel.add(new JLabel("Không tìm thấy thông tin bệnh nhân."));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                }

                // Load appointments
                String query2 = "SELECT ngay, ten_kham, bac_si, phong, toa, trang_thai FROM lichhen WHERE ma_benh_nhan = ?";
                PreparedStatement stmt2 = conn.prepareStatement(query2);
                stmt2.setString(1, UserSession.maBenhNhan != null ? UserSession.maBenhNhan.trim() : "");
                System.out.println("Querying appointments for ma_benh_nhan: " + UserSession.maBenhNhan);
                ResultSet rs2 = stmt2.executeQuery();

                boolean hasAppointments = false;
                while (rs2.next()) {
                    hasAppointments = true;
                    System.out.println("Found appointment for " + UserSession.maBenhNhan);
                    String ngay = rs2.getDate("ngay").toString();
                    String tenKham = rs2.getString("ten_kham");
                    String bacSi = rs2.getString("bac_si");
                    String phong = rs2.getString("phong");
                    String toa = rs2.getString("toa");
                    String trangThai = rs2.getString("trang_thai");
                    tableModel.addRow(new Object[]{ngay, tenKham, bacSi, phong, toa, trangThai});
                }

                // If no appointments are found, show a message in the table
                if (!hasAppointments) {
                    tableModel.addRow(new Object[]{"", "Không có lịch hẹn nào.", "", "", "", ""});
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            infoPanel.revalidate();
            infoPanel.repaint();
        };

        // Load data initially
        loadAppointments.run();

        // Add a Refresh button
        JButton refreshButton = new JButton("Làm mới");
        refreshButton.addActionListener(e -> loadAppointments.run());
        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel2.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));
        buttonPanel2.add(refreshButton);

        appointmentPanel.add(infoPanel, BorderLayout.NORTH);
        appointmentPanel.add(scrollPane, BorderLayout.CENTER);
        appointmentPanel.add(buttonPanel2, BorderLayout.SOUTH);

        // Trong initUI(), thay thế phần recordPanel
        JPanel recordPanel = new JPanel(new BorderLayout());
        recordPanel.setBackground(Color.WHITE);
        recordPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

// Tiêu đề chính
        JLabel lblTitle = new JLabel("BẢN TÓM TẮT HỒ SƠ BỆNH ÁN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        recordPanel.add(lblTitle, BorderLayout.NORTH);

// Panel chính chứa thông tin
        JPanel contentPanelrc = new JPanel();
        contentPanelrc.setLayout(new BoxLayout(contentPanelrc, BoxLayout.Y_AXIS));
        contentPanelrc.setBackground(Color.WHITE);

// Font cho các nhãn
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

// Phần 1: Thông tin bệnh nhân
        JPanel patientInfoPanel = new JPanel(new GridBagLayout());
        patientInfoPanel.setBackground(Color.WHITE);
        patientInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "1. Thông tin bệnh nhân"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblHoTen = new JLabel("Họ và tên: ");
        lblGioiTinh = new JLabel("Giới tính: ");
        lblNgaySinh = new JLabel("Ngày sinh: ");
        lblSDT = new JLabel("Số điện thoại: ");
        lblDiaChi = new JLabel("Địa chỉ: ");
        lblMaBenhNhan = new JLabel("Mã bệnh nhân: ");
        lblCCCD = new JLabel("CCCD: ");
        lblNgayVaoVien = new JLabel("Vào viện ngày: ");
        lblNgayRaVien = new JLabel("Ra viện ngày: ");

        lblHoTen.setFont(labelFont);
        lblGioiTinh.setFont(labelFont);
        lblNgaySinh.setFont(labelFont);
        lblSDT.setFont(labelFont);
        lblDiaChi.setFont(labelFont);
        lblMaBenhNhan.setFont(labelFont);
        lblCCCD.setFont(labelFont);
        lblNgayVaoVien.setFont(labelFont);
        lblNgayRaVien.setFont(labelFont);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; patientInfoPanel.add(lblHoTen, gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; patientInfoPanel.add(lblGioiTinh, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; patientInfoPanel.add(lblNgaySinh, gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; patientInfoPanel.add(lblSDT, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; patientInfoPanel.add(lblDiaChi, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 3; patientInfoPanel.add(lblMaBenhNhan, gbc);
        gbc.gridx = 1; patientInfoPanel.add(lblCCCD, gbc);

        gbc.gridx = 0; gbc.gridy = 4; patientInfoPanel.add(lblNgayVaoVien, gbc);
        gbc.gridx = 1; patientInfoPanel.add(lblNgayRaVien, gbc);

        patientInfoPanel.setPreferredSize(new Dimension(600, 150));

// Phần 2: Tóm tắt quá trình điều trị
        JPanel treatmentInfoPanel = new JPanel(new GridBagLayout());
        treatmentInfoPanel.setBackground(Color.WHITE);
        treatmentInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "2. Tóm tắt quá trình điều trị"));
        GridBagConstraints gbcTreatment = new GridBagConstraints();
        gbcTreatment.insets = new Insets(5, 10, 5, 10);
        gbcTreatment.anchor = GridBagConstraints.WEST;
        gbcTreatment.fill = GridBagConstraints.HORIZONTAL;

        lblLyDoVaoVien = new JLabel("Lý do vào viện: ");
        lblTomTatBenhLy = new JLabel("Tóm tắt quá trình bệnh lý và diễn biến lâm sàng: ");
        lblTienSuBenh = new JLabel("Tiền sử bệnh: ");
        lblKetQuaXetNghiem = new JLabel("Tóm tắt kết quả xét nghiệm, cận lâm sàng: ");
        lblPhuongPhapDieuTri = new JLabel("Phương pháp điều trị: ");
        lblNoiKhoa = new JLabel("Nội khoa: ");
        lblPhauThuat = new JLabel("Phẫu thuật, thủ thuật: ");
        lblTinhTrangRaVien = new JLabel("Tình trạng ra viện: ");

        lblLyDoVaoVien.setFont(labelFont);
        lblTomTatBenhLy.setFont(labelFont);
        lblTienSuBenh.setFont(labelFont);
        lblKetQuaXetNghiem.setFont(labelFont);
        lblPhuongPhapDieuTri.setFont(labelFont);
        lblNoiKhoa.setFont(labelFont);
        lblPhauThuat.setFont(labelFont);
        lblTinhTrangRaVien.setFont(labelFont);


        gbcTreatment.gridx = 0; gbcTreatment.gridy = 0; gbcTreatment.gridwidth = 2; treatmentInfoPanel.add(lblLyDoVaoVien, gbcTreatment);
        gbcTreatment.gridy = 1; treatmentInfoPanel.add(lblTomTatBenhLy, gbcTreatment);
        gbcTreatment.gridy = 2; treatmentInfoPanel.add(lblTienSuBenh, gbcTreatment);
        gbcTreatment.gridy = 3; treatmentInfoPanel.add(lblKetQuaXetNghiem, gbcTreatment);
        gbcTreatment.gridy = 4; treatmentInfoPanel.add(lblPhuongPhapDieuTri, gbcTreatment);
        gbcTreatment.gridy = 5; treatmentInfoPanel.add(lblNoiKhoa, gbcTreatment);
        gbcTreatment.gridy = 6; treatmentInfoPanel.add(lblPhauThuat, gbcTreatment);
        gbcTreatment.gridy = 7; treatmentInfoPanel.add(lblTinhTrangRaVien, gbcTreatment);

        treatmentInfoPanel.setPreferredSize(new Dimension(600, 250));

        loadMedicalRecordData();

        contentPanelrc.add(patientInfoPanel);
        contentPanelrc.add(Box.createVerticalStrut(20));
        contentPanelrc.add(treatmentInfoPanel);

        recordPanel.add(new JScrollPane(contentPanelrc), BorderLayout.CENTER);

        JButton btnTaiVe = new JButton("Tải về");
        JPanel buttonPanelba = new JPanel();
        buttonPanelba.add(btnTaiVe);
        buttonPanelba.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        recordPanel.add(buttonPanelba, BorderLayout.SOUTH);

// Sự kiện nút Tải về
        btnTaiVe.addActionListener(e -> {
            generateAndDownloadWordDocument();
        });

        //Phần liên hệ
        JPanel contactPanel = createContactContent();
        mainPanel.add(contactPanel, "contact");

        contactMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "contact");
            }
        });

        JPanel feedbackPanel;
        if (isLoggedIn && UserSession.maBenhNhan != null && !UserSession.maBenhNhan.trim().isEmpty()) {
            PhanhoiDanhgia feedbackForm = new PhanhoiDanhgia();
            feedbackPanel = feedbackForm.createFeedbackPanel();
        } else {
            feedbackPanel = new JPanel();
            JLabel placeholderLabel = new JLabel("Vui lòng đăng nhập để gửi phản hồi.", JLabel.CENTER);
            placeholderLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            feedbackPanel.setLayout(new BorderLayout());
            feedbackPanel.add(placeholderLabel, BorderLayout.CENTER);
        }
        mainPanel.add(feedbackPanel, "feedback");

        mainPanel.add(homePanel, "home");
        mainPanel.add(patientPanel, "patient");
        mainPanel.add(appointmentPanel, "appointment");
        mainPanel.add(recordPanel, "record");
        mainPanel.add(contactPanel, "contact");
        mainPanel.add(feedbackPanel, "feedback");
        mainPanel.add(createAccountPanel(),"account");

        homeMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "home");
            }
        });

        patientMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isLoggedIn || UserSession.maBenhNhan == null) {
                    JOptionPane.showMessageDialog(null, "Bạn chưa đăng nhập! Vui lòng đăng nhập lại.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new LoginPage();
                } else {
                    cardLayout.show(mainPanel, "patient");
                }
            }
        });

        appointmentMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isLoggedIn || UserSession.maBenhNhan == null) {
                    JOptionPane.showMessageDialog(null, "Bạn chưa đăng nhập! Vui lòng đăng nhập lại.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new LoginPage();
                } else {
                    cardLayout.show(mainPanel, "appointment");
                }
            }
        });

        recordMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isLoggedIn || UserSession.maBenhNhan == null) {
                    JOptionPane.showMessageDialog(null, "Bạn chưa đăng nhập! Vui lòng đăng nhập lại.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new LoginPage();
                } else {
                    cardLayout.show(mainPanel, "record");
                }
            }
        });

        contactMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "contact");
            }
        });

        commentMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isLoggedIn || UserSession.maBenhNhan == null) {
                    JOptionPane.showMessageDialog(null, "Bạn chưa đăng nhập! Vui lòng đăng nhập lại.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new LoginPage();
                } else {
                    cardLayout.show(mainPanel, "feedback");
                }
            }
        });

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "home");
        setVisible(true);

        // Debug: Check UserSession.maBenhNhan at initialization
        System.out.println("UserSession.maBenhNhan at initialization: " + UserSession.maBenhNhan);
    }

    private JMenu createMenu(String title, Font font, Color normalBg, Color hoverBg) {
        JMenu menu = new JMenu(title);
        menu.setFont(font);
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menu.setBorderPainted(false);
        menu.setForeground(Color.WHITE);
        menu.setOpaque(true);
        menu.setBackground(normalBg);

        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menu.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menu.setBackground(normalBg);
            }
        });
        return menu;
    }

    class MarqueePanel extends JPanel implements ActionListener {
        private String text;
        private int x;
        private int textWidth;
        private Timer timer;

        public MarqueePanel(String text) {
            this.text = text;
            setFont(new Font("Times New Roman", Font.BOLD, 24));
            setBackground(Color.YELLOW);
            setPreferredSize(new Dimension(0, 50));
            x = -100;
            timer = new Timer(15, this);
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            FontMetrics fm = g.getFontMetrics();
            textWidth = fm.stringWidth(text);
            g.setColor(Color.BLACK);
            g.drawString(text, x, getHeight() / 2 + fm.getAscent() / 2 - 4);
            x += 2;
            if (x > getWidth()) {
                x = -textWidth;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }

    // chi tiết bệnh nhân
    private void loadPatientData(JTextField tfMaBN, JTextField tfHo, JComboBox<String> cbGioiTinh,
                                 JTextField tfNgaySinh, JTextField tfCMND, JTextField tfSDT,
                                 JTextField tfNgheNghiep, JComboBox<String> cbDoiTuong, JTextArea taDiaChi,
                                 JTextField tfNgayKham, JTextField tfMaBS, JTextField tfBacSi, JTextArea taYeuCauKham) {
        try (Connection conn = ConnectionDatabase.getConnection()) {
            String sqlBenhNhan = "SELECT mabenhnhan, hoten, gioitinh, ngaysinh, sdt, diachi, cccd, ngayvaovien, ngayravien, nghe_nghiep " +
                    "FROM benhnhan WHERE mabenhnhan = ?";
            PreparedStatement stmtBenhNhan = conn.prepareStatement(sqlBenhNhan);
            stmtBenhNhan.setString(1, UserSession.maBenhNhan != null ? UserSession.maBenhNhan.trim() : "");
            ResultSet rsBenhNhan = stmtBenhNhan.executeQuery();

            if (rsBenhNhan.next()) {
                tfMaBN.setText(rsBenhNhan.getString("mabenhnhan") != null ? rsBenhNhan.getString("mabenhnhan") : "");
                tfHo.setText(rsBenhNhan.getString("hoten") != null ? rsBenhNhan.getString("hoten") : "");
                cbGioiTinh.setSelectedItem(rsBenhNhan.getString("gioitinh") != null ? rsBenhNhan.getString("gioitinh") : "Nam");
                tfNgaySinh.setText(rsBenhNhan.getDate("ngaysinh") != null ? rsBenhNhan.getDate("ngaysinh").toString() : "");
                tfCMND.setText(rsBenhNhan.getString("cccd") != null ? rsBenhNhan.getString("cccd") : "");
                tfSDT.setText(rsBenhNhan.getString("sdt") != null ? rsBenhNhan.getString("sdt") : "");
                tfNgheNghiep.setText(rsBenhNhan.getString("nghe_nghiep") != null ? rsBenhNhan.getString("nghe_nghiep") : "");
                taDiaChi.setText(rsBenhNhan.getString("diachi") != null ? rsBenhNhan.getString("diachi") : "");
                tfNgayKham.setText(rsBenhNhan.getDate("ngayvaovien") != null ? rsBenhNhan.getDate("ngayvaovien").toString() : "");
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin bệnh nhân.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return; // Exit if patient not found
            }

            String sqlPhieuKham = "SELECT ma_bac_si, bac_si_phu_trach, yeu_cau_kham, doi_tuong " +
                    "FROM phieukham WHERE ma_benh_nhan = ?";
            PreparedStatement stmtPhieuKham = conn.prepareStatement(sqlPhieuKham);
            stmtPhieuKham.setString(1, UserSession.maBenhNhan != null ? UserSession.maBenhNhan.trim() : "");
            ResultSet rsPhieuKham = stmtPhieuKham.executeQuery();

            if (rsPhieuKham.next()) {
                tfMaBS.setText(rsPhieuKham.getString("ma_bac_si") != null ? rsPhieuKham.getString("ma_bac_si") : "");
                tfBacSi.setText(rsPhieuKham.getString("phu_trach") != null ? rsPhieuKham.getString("bac_si_phu_trach") : "");
                taYeuCauKham.setText(rsPhieuKham.getString("yeu_cau_kham") != null ? rsPhieuKham.getString("yeu_cau_kham") : "");
                cbDoiTuong.setSelectedItem(rsPhieuKham.getString("doi_tuong") != null ? rsPhieuKham.getString("doi_tuong") : "Bình thường");
            } else {
                tfMaBS.setText("");
                tfBacSi.setText("");
                taYeuCauKham.setText("");
                cbDoiTuong.setSelectedItem("Bình thường");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu bệnh nhân: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMedicalRecordData() {
        try (Connection conn = ConnectionDatabase.getConnection()) {
            // Lấy thông tin từ bảng benh_nhan
            String sqlBenhNhan = "SELECT hoten, gioitinh, ngaysinh, sdt, diachi, mabenhnhan, cccd, ngayvaovien, ngayravien " +
                    "FROM benhnhan WHERE mabenhnhan = ?";
            PreparedStatement stmtBenhNhan = conn.prepareStatement(sqlBenhNhan);
            stmtBenhNhan.setString(1, UserSession.maBenhNhan);
            ResultSet rsBenhNhan = stmtBenhNhan.executeQuery();
            if (rsBenhNhan.next()) {
                lblHoTen.setText("Họ và tên: " + (rsBenhNhan.getString("hoten") != null ? rsBenhNhan.getString("hoten") : ""));
                lblGioiTinh.setText("Giới tính: " + (rsBenhNhan.getString("gioitinh") != null ? rsBenhNhan.getString("gioitinh") : ""));
                lblNgaySinh.setText("Ngày sinh: " + (rsBenhNhan.getDate("ngaysinh") != null ? rsBenhNhan.getDate("ngaysinh").toString() : ""));
                lblSDT.setText("Số điện thoại: " + (rsBenhNhan.getString("sdt") != null ? rsBenhNhan.getString("sdt") : ""));
                lblDiaChi.setText("Địa chỉ: " + (rsBenhNhan.getString("diachi") != null ? rsBenhNhan.getString("diachi") : ""));
                lblMaBenhNhan.setText("Mã bệnh nhân: " + (rsBenhNhan.getString("mabenhnhan") != null ? rsBenhNhan.getString("mabenhnhan") : ""));
                lblCCCD.setText("CCCD: " + (rsBenhNhan.getString("cccd") != null ? rsBenhNhan.getString("cccd") : ""));
                lblNgayVaoVien.setText("Vào viện ngày: " + (rsBenhNhan.getDate("ngayvaovien") != null ? rsBenhNhan.getDate("ngayvaovien").toString() : ""));
                lblNgayRaVien.setText("Ra viện ngày: " + (rsBenhNhan.getDate("ngayravien") != null ? rsBenhNhan.getDate("ngayravien").toString() : ""));
            } else {
                lblHoTen.setText("Họ và tên: ");
                lblGioiTinh.setText("Giới tính: ");
                lblNgaySinh.setText("Ngày sinh: ");
                lblSDT.setText("Số điện thoại: ");
                lblDiaChi.setText("Địa chỉ: ");
                lblMaBenhNhan.setText("Mã bệnh nhân: ");
                lblCCCD.setText("CCCD: ");
                lblNgayVaoVien.setText("Vào viện ngày: ");
                lblNgayRaVien.setText("Ra viện ngày: ");
            }

            // Lấy thông tin từ bảng hosobenhan
            String sqlHoso = "SELECT ly_do_vao_vien, tom_tat_benh_ly, tien_su_benh, ket_qua_xet_nghiem, noi_khoa, phau_thuat, tinh_trang_ra_vien " +
                    "FROM ho_so_benh_an WHERE ma_benh_nhan = ?";
            PreparedStatement stmtHoso = conn.prepareStatement(sqlHoso);
            stmtHoso.setString(1, UserSession.maBenhNhan);
            ResultSet rsHoso = stmtHoso.executeQuery();
            if (rsHoso.next()) {
                lblLyDoVaoVien.setText("Lý do vào viện: " + (rsHoso.getString("ly_do_vao_vien") != null ? rsHoso.getString("ly_do_vao_vien") : ""));
                lblTomTatBenhLy.setText("Tóm tắt quá trình bệnh lý và diễn biến lâm sàng: " + (rsHoso.getString("tom_tat_benh_ly") != null ? rsHoso.getString("tom_tat_benh_ly") : ""));
                lblTienSuBenh.setText("Tiền sử bệnh: " + (rsHoso.getString("tien_su_benh") != null ? rsHoso.getString("tien_su_benh") : ""));
                lblKetQuaXetNghiem.setText("Tóm tắt kết quả xét nghiệm, cận lâm sàng: " + (rsHoso.getString("ket_qua_xet_nghiem") != null ? rsHoso.getString("ket_qua_xet_nghiem") : ""));
                lblNoiKhoa.setText("Nội khoa: " + (rsHoso.getString("noi_khoa") != null ? rsHoso.getString("noi_khoa") : "Không"));
                lblPhauThuat.setText("Phẫu thuật, thủ thuật: " + (rsHoso.getString("phau_thuat") != null ? rsHoso.getString("phau_thuat") : "Không"));
                lblTinhTrangRaVien.setText("Tình trạng ra viện: " + (rsHoso.getString("tinh_trang_ra_vien") != null ? rsHoso.getString("tinh_trang_ra_vien") : ""));
            } else {
                lblLyDoVaoVien.setText("Lý do vào viện: ");
                lblTomTatBenhLy.setText("Tóm tắt quá trình bệnh lý và diễn biến lâm sàng: ");
                lblTienSuBenh.setText("Tiền sử bệnh: ");
                lblKetQuaXetNghiem.setText("Tóm tắt kết quả xét nghiệm, cận lâm sàng: ");
                lblNoiKhoa.setText("Nội khoa: Không");
                lblPhauThuat.setText("Phẫu thuật, thủ thuật: Không");
                lblTinhTrangRaVien.setText("Tình trạng ra viện: ");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải thông tin hồ sơ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateAndDownloadWordDocument() {
        try {
            XWPFDocument document = new XWPFDocument();

            XWPFParagraph header1 = document.createParagraph();
            header1.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun headerRun1 = header1.createRun();
            headerRun1.setFontFamily("Times New Roman");
            headerRun1.setFontSize(12);
            headerRun1.setBold(true);
            headerRun1.setText("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM");

            XWPFParagraph header2 = document.createParagraph();
            header2.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun headerRun2 = header2.createRun();
            headerRun2.setFontFamily("Times New Roman");
            headerRun2.setFontSize(12);
            headerRun2.setBold(true);
            headerRun2.setText("Độc lập - Tự do - Hạnh phúc");

            XWPFParagraph soYTe = document.createParagraph();
            soYTe.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun soYTeRun = soYTe.createRun();
            soYTeRun.setFontFamily("Times New Roman");
            soYTeRun.setFontSize(12);
            soYTeRun.setBold(true);
            soYTeRun.setText("Sở y tế: Đà Nẵng");


            // Dòng trống
            XWPFParagraph emptyLine = document.createParagraph();
            emptyLine.createRun().addBreak();

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun xuat_title = title.createRun();
            xuat_title.setFontFamily("Times New Roman");
            xuat_title.setFontSize(14);
            xuat_title.setBold(true);
            xuat_title.setText("BẢN TÓM TẮT HỒ SƠ BỆNH ÁN");

            // Dòng trống
            emptyLine.createRun().addBreak();

            XWPFParagraph tieudethongtin = document.createParagraph();
            XWPFRun xuat_title_tht = tieudethongtin.createRun();
            xuat_title_tht.setFontFamily("Times New Roman");
            xuat_title_tht.setFontSize(12);
            xuat_title_tht.setBold(true);
            xuat_title_tht.setText("1. Thông tin bệnh nhân");

            XWPFParagraph info_bn = document.createParagraph();
            XWPFRun xuat_info_bn = info_bn.createRun();
            xuat_info_bn.setFontFamily("Times New Roman");
            xuat_info_bn.setFontSize(12);
            xuat_info_bn.setText(lblHoTen.getText() + " ".repeat(40) + lblGioiTinh.getText());
            xuat_info_bn.addBreak();
            xuat_info_bn.setText(lblNgaySinh.getText() + " ".repeat(40) + lblSDT.getText());
            xuat_info_bn.addBreak();
            xuat_info_bn.setText(lblDiaChi.getText());
            xuat_info_bn.addBreak();
            xuat_info_bn.setText(lblMaBenhNhan.getText() + " ".repeat(40) + lblCCCD.getText());
            xuat_info_bn.addBreak();
            xuat_info_bn.setText(lblNgayVaoVien.getText() + " ".repeat(40) + lblNgayRaVien.getText());
            xuat_info_bn.addBreak();


            XWPFParagraph tieudetomtat = document.createParagraph();
            XWPFRun xuat_title_tt = tieudetomtat.createRun();
            xuat_title_tt.setFontFamily("Times New Roman");
            xuat_title_tt.setFontSize(12);
            xuat_title_tt.setBold(true);
            xuat_title_tt.setText("2. Tóm tắt quá trình điều trị");

            XWPFParagraph thongtintomtat = document.createParagraph();
            XWPFRun xuatthongtin = thongtintomtat.createRun();
            xuatthongtin.setFontFamily("Times New Roman");
            xuatthongtin.setFontSize(12);
            xuatthongtin.setBold(true);
            xuatthongtin.setText("Lý do vào viện: ");
            xuatthongtin.setBold(false);
            xuatthongtin.setText(lblLyDoVaoVien.getText().replace("Lý do vào viện: ", ""));
            xuatthongtin.addBreak();

            xuatthongtin.setBold(true);
            xuatthongtin.setText("Tóm tắt quá trình bệnh lý và diễn biến lâm sàng: ");
            xuatthongtin.setBold(false);
            xuatthongtin.setText(lblTomTatBenhLy.getText().replace("Tóm tắt quá trình bệnh lý và diễn biến lâm sàng: ", ""));
            xuatthongtin.addBreak();

            xuatthongtin.setBold(true);
            xuatthongtin.setText("Tiền sử bệnh: ");
            xuatthongtin.setBold(false);
            xuatthongtin.setText(lblTienSuBenh.getText().replace("Tiền sử bệnh: ", ""));
            xuatthongtin.addBreak();

            xuatthongtin.setBold(true);
            xuatthongtin.setText("Tóm tắt kết quả xét nghiệm, cận lâm sàng: ");
            xuatthongtin.setBold(false);
            xuatthongtin.setText(lblKetQuaXetNghiem.getText().replace("Tóm tắt kết quả xét nghiệm, cận lâm sàng: ", ""));
            xuatthongtin.addBreak();

            xuatthongtin.setBold(true);
            xuatthongtin.setText("Phương pháp điều trị: ");
            xuatthongtin.addBreak();

            xuatthongtin.setBold(true);
            xuatthongtin.setText("Nội khoa: ");
            xuatthongtin.setBold(false);
            xuatthongtin.setText(lblNoiKhoa.getText().replace("Nội khoa: ", ""));
            xuatthongtin.addBreak();

            xuatthongtin.setBold(true);
            xuatthongtin.setText("Phẫu thuật, thủ thuật: ");
            xuatthongtin.setBold(false);
            xuatthongtin.setText(lblPhauThuat.getText().replace("Phẫu thuật, thủ thuật: ", ""));
            xuatthongtin.addBreak();

            xuatthongtin.setBold(true);
            xuatthongtin.setText("Tình trạng ra viện: ");
            xuatthongtin.setBold(false);
            xuatthongtin.setText(lblTinhTrangRaVien.getText().replace("Tình trạng ra viện: ", ""));
            xuatthongtin.addBreak();

            XWPFParagraph signature = document.createParagraph();
            signature.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun signatureRun = signature.createRun();
            signatureRun.setFontFamily("Times New Roman");
            signatureRun.setFontSize(12);
            signatureRun.setText("Ngày………tháng………năm……");
            signatureRun.addBreak();
            signatureRun.setBold(true);
            signatureRun.setText("Đại diện đơn vị");
            signatureRun.addBreak();
            signatureRun.setText("(Ký, đóng dấu)");

            String filePath = "HosoBenhAn_" + UserSession.maBenhNhan + "_" + System.currentTimeMillis() + ".docx";
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                document.write(out);
                JOptionPane.showMessageDialog(null, "Tải về thành công! File: " + filePath);
            }
            document.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tạo file Word: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createContactContent() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(1500, 890));


        JPanel footerPanel = createFooterPanel();
        footerPanel.setBounds(0, 400, 1500, 400);
        panel.add(footerPanel);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(106, 103, 103, 255));
        footerPanel.setPreferredSize(new Dimension(1500, 400));
        footerPanel.setLayout(new GridLayout(2, 3));

        JLabel customerInfo = new JLabel("<html><b>THÔNG TIN KHÁCH HÀNG</b><br>Chính sách sử dụng dịch vụ<br>Hướng dẫn đặt lịch hẹn<br>Hỗ trợ bệnh nhân<br>Điều khoản dịch vụ</html>");
        customerInfo.setForeground(Color.WHITE);
        customerInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        customerInfo.setHorizontalAlignment(JLabel.CENTER);
        footerPanel.add(customerInfo);

        JLabel hospitalPolicy = new JLabel("<html><b>CHÍNH SÁCH BỆNH VIỆN</b><br>Chính sách bảo mật thông tin<br>Quy trình khám chữa bệnh<br>Chính sách thanh toán<br>Quy định an toàn y tế</html>");
        hospitalPolicy.setForeground(Color.WHITE);
        hospitalPolicy.setFont(new Font("Arial", Font.PLAIN, 14));
        hospitalPolicy.setHorizontalAlignment(JLabel.CENTER);
        footerPanel.add(hospitalPolicy);

        JLabel supportService = new JLabel("<html><b>DỊCH VỤ HỖ TRỢ</b><br>Giờ làm việc: 8h00 AM - 22h00 PM<br>Hotline: 0921205231<br>Email: danhvt.24it@vku.udn.vn</html>");
        supportService.setForeground(Color.WHITE);
        supportService.setFont(new Font("Arial", Font.PLAIN, 14));
        supportService.setHorizontalAlignment(JLabel.CENTER);
        footerPanel.add(supportService);

        JLabel contactInfo = new JLabel("<html><b>BỆNH VIỆN RKK</b><br>Địa chỉ: Đà Nẵng<br>Hotline: 0921205231<br>Email: danhvt.24it@vku.udn.vn</html>");
        contactInfo.setForeground(Color.WHITE);
        contactInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        contactInfo.setHorizontalAlignment(JLabel.CENTER);
        footerPanel.add(contactInfo);

        JLabel paymentMethods = new JLabel("<html><b>PHƯƠNG THỨC THANH TOÁN</b><br>Chuyển khoản ngân hàng<br>Thanh toán trực tiếp<br>Thanh toán qua ví điện tử</html>");
        paymentMethods.setForeground(Color.WHITE);
        paymentMethods.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentMethods.setHorizontalAlignment(JLabel.CENTER);
        footerPanel.add(paymentMethods);

        JLabel socialMedia = new JLabel("<html><b>KẾT NỐI VỚI CHÚNG TÔI</b><br>Facebook: fb.com/rkk<br>Twitter: twitter.com/rkk<br>LinkedIn: linkedin.com/company/rkk</html>");
        socialMedia.setForeground(Color.WHITE);
        socialMedia.setFont(new Font("Arial", Font.PLAIN, 14));
        socialMedia.setHorizontalAlignment(JLabel.CENTER);
        footerPanel.add(socialMedia);

        return footerPanel;
    }

    // thông tin tài khoản
    private JPanel createAccountPanel() {
        JPanel accountPanel = new JPanel(new BorderLayout());
        accountPanel.setBackground(Color.WHITE);
        accountPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel lblTitle = new JLabel("THÔNG TIN TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        accountPanel.add(lblTitle, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Labels for user information
        JLabel lblHoTen = new JLabel("Họ và tên: ");
        JLabel lblNgaySinh = new JLabel("Ngày sinh: ");
        JLabel lblGioiTinh = new JLabel("Giới tính: ");
        JLabel lblSDT = new JLabel("Số điện thoại: ");
        JLabel lblEmail = new JLabel("Email: ");

        lblHoTen.setFont(labelFont);
        lblNgaySinh.setFont(labelFont);
        lblGioiTinh.setFont(labelFont);
        lblSDT.setFont(labelFont);
        lblEmail.setFont(labelFont);

        // Add labels to panel
        gbc.gridx = 0; gbc.gridy = 0; contentPanel.add(lblHoTen, gbc);
        gbc.gridy = 1; contentPanel.add(lblNgaySinh, gbc);
        gbc.gridy = 2; contentPanel.add(lblGioiTinh, gbc);
        gbc.gridy = 3; contentPanel.add(lblSDT, gbc);
        gbc.gridy = 4; contentPanel.add(lblEmail, gbc);

        // Password change section
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Đổi mật khẩu"));
        GridBagConstraints gbcPw = new GridBagConstraints();
        gbcPw.insets = new Insets(5, 10, 5, 10);
        gbcPw.anchor = GridBagConstraints.WEST;

        JLabel lblOldPassword = new JLabel("Mật khẩu cũ:");
        JLabel lblNewPassword = new JLabel("Mật khẩu mới:");
        JLabel lblConfirmPassword = new JLabel("Xác nhận mật khẩu mới:");
        JPasswordField tfOldPassword = new JPasswordField(20);
        JPasswordField tfNewPassword = new JPasswordField(20);
        JPasswordField tfConfirmPassword = new JPasswordField(20);
        JButton btnChangePassword = new JButton("Đổi mật khẩu");

        lblOldPassword.setFont(labelFont);
        lblNewPassword.setFont(labelFont);
        lblConfirmPassword.setFont(labelFont);
        btnChangePassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnChangePassword.setBackground(new Color(30, 144, 255));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setFocusPainted(false);

        gbcPw.gridx = 0; gbcPw.gridy = 0; passwordPanel.add(lblOldPassword, gbcPw);
        gbcPw.gridx = 1; passwordPanel.add(tfOldPassword, gbcPw);
        gbcPw.gridx = 0; gbcPw.gridy = 1; passwordPanel.add(lblNewPassword, gbcPw);
        gbcPw.gridx = 1; passwordPanel.add(tfNewPassword, gbcPw);
        gbcPw.gridx = 0; gbcPw.gridy = 2; passwordPanel.add(lblConfirmPassword, gbcPw);
        gbcPw.gridx = 1; passwordPanel.add(tfConfirmPassword, gbcPw);
        gbcPw.gridx = 0; gbcPw.gridy = 3; gbcPw.gridwidth = 2;
        gbcPw.anchor = GridBagConstraints.CENTER;
        passwordPanel.add(btnChangePassword, gbcPw);

        // Load user data
        try (Connection conn = ConnectionDatabase.getConnection()) {
            String sql = "SELECT bn.hoten, bn.ngaysinh, bn.gioitinh, bn.sdt, tk.email " +
                    "FROM benhnhan bn JOIN tai_khoan tk ON bn.sdt = tk.sdt " +
                    "WHERE bn.mabenhnhan = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, UserSession.maBenhNhan != null ? UserSession.maBenhNhan.trim() : "");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                lblHoTen.setText("Họ và tên: " + (rs.getString("hoten") != null ? rs.getString("hoten") : ""));
                lblNgaySinh.setText("Ngày sinh: " + (rs.getDate("ngaysinh") != null ? rs.getDate("ngaysinh").toString() : ""));
                lblGioiTinh.setText("Giới tính: " + (rs.getString("gioitinh") != null ? rs.getString("gioitinh") : ""));
                lblSDT.setText("Số điện thoại: " + (rs.getString("sdt") != null ? rs.getString("sdt") : ""));
                lblEmail.setText("Email: " + (rs.getString("email") != null ? rs.getString("email") : ""));
            } else {
                lblHoTen.setText("Họ và tên: Không tìm thấy");
                lblNgaySinh.setText("Ngày sinh: ");
                lblGioiTinh.setText("Giới tính: ");
                lblSDT.setText("Số điện thoại: ");
                lblEmail.setText("Email: ");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải thông tin tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // Password change action
        btnChangePassword.addActionListener(e -> {
            String oldPassword = new String(tfOldPassword.getPassword());
            String newPassword = new String(tfNewPassword.getPassword());
            String confirmPassword = new String(tfConfirmPassword.getPassword());

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ các trường mật khẩu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "Mật khẩu mới và xác nhận mật khẩu không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = ConnectionDatabase.getConnection()) {
                // Verify old password
                String sqlVerify = "SELECT mat_khau FROM tai_khoan WHERE sdt = (SELECT sdt FROM benhnhan WHERE mabenhnhan = ?)";
                PreparedStatement stmtVerify = conn.prepareStatement(sqlVerify);
                stmtVerify.setString(1, UserSession.maBenhNhan);
                ResultSet rsVerify = stmtVerify.executeQuery();
                if (rsVerify.next()) {
                    String storedPassword = rsVerify.getString("mat_khau");
                    if (!storedPassword.equals(oldPassword)) {
                        JOptionPane.showMessageDialog(null, "Mật khẩu cũ không đúng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy tài khoản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update password
                String sqlUpdate = "UPDATE tai_khoan SET mat_khau = ? WHERE sdt = (SELECT sdt FROM benhnhan WHERE mabenhnhan = ?)";
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                stmtUpdate.setString(1, newPassword);
                stmtUpdate.setString(2, UserSession.maBenhNhan);
                int rowsAffected = stmtUpdate.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Đổi mật khẩu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    tfOldPassword.setText("");
                    tfNewPassword.setText("");
                    tfConfirmPassword.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Không thể cập nhật mật khẩu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi đổi mật khẩu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to content panel
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        contentPanel.add(Box.createVerticalStrut(20), gbc);
        gbc.gridy = 6; contentPanel.add(passwordPanel, gbc);

        accountPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        return accountPanel;
    }

    public static void main(String[] args) {
        new BenhNhan();
    }
}