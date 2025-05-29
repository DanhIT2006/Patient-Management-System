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

public class BacSi extends JFrame {

    private boolean isLoggedIn = false;
    private String selectedMaBacSi = null; // Lưu mã bệnh nhân được chọn

    private JLabel lblHoTen, lblGioiTinh, lblNgaySinh, lblSDT, lblDiaChi, lblCCCD, lblMaBenhNhan, lblNgayVaoVien, lblNgayRaVien;
    private JLabel lblLyDoVaoVien, lblTomTatBenhLy, lblTienSuBenh, lblKetQuaXetNghiem, lblPhuongPhapDieuTri, lblNoiKhoa, lblPhauThuat, lblTinhTrangRaVien;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public BacSi(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
        initUI();
    }

    private Image[] bannerImages;
    private int currentBannerIndex = 0;

    public BacSi() {
        this(false);
    }

    private void initUI() {
        UIManager.put("Menu.selectionBackground", new Color(0, 102, 204));
        UIManager.put("Menu.selectionForeground", Color.WHITE);
        UIManager.put("MenuItem.selectionBackground", new Color(0, 102, 204));
        UIManager.put("MenuItem.selectionForeground", Color.WHITE);

        setTitle("Doctor Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1550, 890);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        MarqueePanel marqueePanel = new MarqueePanel("Welcome to the Doctor Management System!");

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
                new ImageIcon(getClass().getResource("/Imgs/anhnen1.jpg")).getImage(),
                new ImageIcon(getClass().getResource("/Imgs/anhnen2.jpg")).getImage(),
                new ImageIcon(getClass().getResource("/Imgs/anhnen3.jpg")).getImage()
        };

        Timer bannerTimer = new Timer(2000, e -> {
            currentBannerIndex = (currentBannerIndex + 1) % bannerImages.length;
            backgroundPanel.repaint();
        });
        bannerTimer.start();

        backgroundPanel.setPreferredSize(new Dimension(0, 730));
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
        JMenu patientMenu = createMenu("Chi tiết bác sĩ", menuFont, normalColor, hoverColor);
        JMenu appointmentMenu = createMenu("Lịch khám", menuFont, normalColor, hoverColor);
        JMenu recordMenu = createMenu("Quản lí bệnh nhân", menuFont, normalColor, hoverColor);
        JMenu contactMenu = createMenu("Liên hệ", menuFont, normalColor, hoverColor);

        menuBar.add(homeMenu);
        menuBar.add(patientMenu);
        menuBar.add(appointmentMenu);
        menuBar.add(recordMenu);
        menuBar.add(contactMenu);

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
            dispose();
            new Login();
        });

        accountPanel.add(loginButton);

        if (isLoggedIn) {
            loginButton.setVisible(false);
            JLabel accountLabel = new JLabel("Tài khoản");
            accountLabel.setForeground(Color.WHITE);
            accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            accountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            accountLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem infoItem = new JMenuItem("Thông tin tài khoản");
                    JMenuItem logoutItem = new JMenuItem("Đăng xuất");

                    infoItem.addActionListener(evt -> {
                        cardLayout.show(mainPanel, "account");
                    });

                    logoutItem.addActionListener(evt -> {
                        UserSession.clear();
                        dispose();
                        new BacSi(false).setVisible(true);
                    });

                    menu.add(infoItem);
                    menu.add(logoutItem);
                    menu.show(accountLabel, e.getX(), e.getY());
                }
            });

            accountLabel.setBounds(350, 20, 100, 30);
            accountPanel.setLayout(null);
            accountPanel.add(accountLabel);
        }

        menuBar.add(accountPanel);
        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.add(topPanel, BorderLayout.NORTH);
        JPanel centerHome = new JPanel();
        centerHome.setBackground(Color.WHITE);
        homePanel.add(centerHome, BorderLayout.CENTER);
// thông tin bác sĩ
        JPanel patientPanel = new JPanel(new BorderLayout());
        patientPanel.setBackground(Color.WHITE);
        patientPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle_ctbn = new JLabel("Thông tin chi tiết bác sĩ", SwingConstants.CENTER);
        lblTitle_ctbn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle_ctbn.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        patientPanel.add(lblTitle_ctbn, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 50, 10, 50);
        contentPanel.setBackground(Color.WHITE);


        JLabel lblAvatar = new JLabel();
        lblAvatar.setPreferredSize(new Dimension(150, 180));
        lblAvatar.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton btnUploadImage = new JButton("Tải ảnh");
        btnUploadImage.setFocusPainted(false);

        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.add(lblAvatar, BorderLayout.CENTER);
        avatarPanel.add(btnUploadImage, BorderLayout.SOUTH);
        avatarPanel.setBackground(Color.WHITE);

        JTextField tfHoVaTen = new JTextField(13);
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField tfNgaySinh = new JTextField(13);
        JTextField tfCMND = new JTextField(13);
        JTextField tfSDT = new JTextField(13);
        JTextField tfNgheNghiep = new JTextField(13);
        JTextArea taDiaChi = new JTextArea(3, 20);
        taDiaChi.setLineWrap(true);
        taDiaChi.setWrapStyleWord(true);
        taDiaChi.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JTextField tfMaBS = new JTextField(13);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridheight = 5;
        contentPanel.add(avatarPanel, gbc);

        gbc.gridheight = 1;

        gbc.gridx = 1; gbc.gridy = 1; contentPanel.add(new JLabel("Họ và Tên:"), gbc);
        gbc.gridx = 2; contentPanel.add(tfHoVaTen, gbc);

        gbc.gridx = 1; gbc.gridy = 2; contentPanel.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 2; contentPanel.add(cbGioiTinh, gbc);

        gbc.gridx = 1; gbc.gridy = 3; contentPanel.add(new JLabel("Ngày sinh:"), gbc);
        gbc.gridx = 2; contentPanel.add(tfNgaySinh, gbc);

        gbc.gridx = 1; gbc.gridy = 4; contentPanel.add(new JLabel("Số CCCD:"), gbc);
        gbc.gridx = 2; contentPanel.add(tfCMND, gbc);

        gbc.gridx = 3; gbc.gridy = 1; contentPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 4; contentPanel.add(tfSDT, gbc);

        gbc.gridx = 3; gbc.gridy = 2; contentPanel.add(new JLabel("Nghề nghiệp:"), gbc);
        gbc.gridx = 4; contentPanel.add(tfNgheNghiep, gbc);


        gbc.gridx = 3; gbc.gridy = 3; contentPanel.add(new JLabel("Mã bác sĩ:"), gbc);
        gbc.gridx = 4; contentPanel.add(tfMaBS, gbc);

        gbc.gridx = 3; gbc.gridy = 4; contentPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 4; contentPanel.add(taDiaChi, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton("Lưu"));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));

        patientPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        patientPanel.add(buttonPanel, BorderLayout.SOUTH);

        if (isLoggedIn && UserSession.userId != null && !UserSession.userId.trim().isEmpty()) {
            loadDoctorData(tfHoVaTen, cbGioiTinh, tfNgaySinh, tfCMND, tfSDT, tfNgheNghiep, taDiaChi, tfMaBS);
        }

        // Xử lý nút tải thông tin bệnh nhân

        JPanel appointmentPanel = new JPanel(new BorderLayout());
        appointmentPanel.setBackground(Color.WHITE);

        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin bác sĩ"));
        infoPanel.setBackground(new Color(245, 245, 245));

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Ngày", "Khám","Mã bệnh nhân", "Bệnh nhân", "Phòng","Tòa", "Trạng thái"});

        JTable table = new JTable(tableModel);
        table.setEnabled(false);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);

// Method to load appointment data for the logged-in doctor
        Runnable loadAppointments = () -> {
            // Ensure the doctor is logged in
            if (!isLoggedIn || UserSession.userId == null || !UserSession.role.equals("bacsi")) {
                dispose();
                return;
            }

            tableModel.setRowCount(0);
            try (Connection conn = ConnectionDatabase.getConnection()) {
                // Load doctor info
                String query1 = "SELECT * FROM bac_si WHERE mabacsi = ?";
                PreparedStatement stmt1 = conn.prepareStatement(query1);
                stmt1.setString(1, UserSession.userId.trim()); // Use the logged-in doctor's ID
                ResultSet rs1 = stmt1.executeQuery();

                infoPanel.removeAll();
                if (rs1.next()) {
                    String hoTen = rs1.getString("hoten");
                    String gioiTinh = rs1.getString("gioitinh");
                    String ngaySinh = rs1.getDate("ngaysinh") != null ? rs1.getDate("ngaysinh").toString() : "";
                    String sdt = rs1.getString("sdt");
                    String diaChi = rs1.getString("diachi");

                    infoPanel.add(new JLabel("Họ và tên: " + hoTen));
                    infoPanel.add(new JLabel("Giới tính: " + gioiTinh));
                    infoPanel.add(new JLabel("Ngày sinh: " + ngaySinh));
                    infoPanel.add(new JLabel("SĐT: " + sdt));
                    infoPanel.add(new JLabel("Địa chỉ: " + diaChi));
                    infoPanel.add(new JLabel("Mã bác sĩ: " + UserSession.userId));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                } else {
                    infoPanel.add(new JLabel("Không tìm thấy thông tin bác sĩ."));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                    infoPanel.add(new JLabel(""));
                }

                // Load appointments for the doctor from the lichkham table
                String query2 = "SELECT ngay, kham, ma_benh_nhan, benhnhan, phong, toa, trangthai FROM lichkham WHERE ma_bac_si = ?";
                PreparedStatement stmt2 = conn.prepareStatement(query2);
                stmt2.setString(1, UserSession.userId != null ? UserSession.userId.trim() : "");
                System.out.println("Querying appointments for ma_bac_si: " + UserSession.userId);
                ResultSet rs2 = stmt2.executeQuery();

                boolean hasAppointments = false;
                while (rs2.next()) {
                    hasAppointments = true;
                    System.out.println("Found appointment for " + UserSession.userId);
                    String ngay = rs2.getDate("ngay") != null ? rs2.getDate("ngay").toString() : "";
                    String kham = rs2.getString("kham") != null ? rs2.getString("kham") : "";
                    String mabenhnhan = rs2.getString("ma_benh_nhan") != null ? rs2.getString("ma_benh_nhan") : "";
                    String benhnhan = rs2.getString("benhnhan") != null ? rs2.getString("benhnhan") : "";
                    String phong = rs2.getString("phong") != null ? rs2.getString("phong") : "";
                    String toa = rs2.getString("toa") != null ? rs2.getString("toa") : "";
                    String trangThai = rs2.getString("trangthai") != null ? rs2.getString("trangthai") : "";
                    tableModel.addRow(new Object[]{ngay, kham,mabenhnhan, benhnhan, phong, toa, trangThai});
                }

                if (!hasAppointments) {
                    tableModel.addRow(new Object[]{"", "Không có lịch khám nào.", "", "", "", "", ""});
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

        JButton refreshButton = new JButton("Làm mới");
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadAppointments.run());
        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel2.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));
        buttonPanel2.add(refreshButton);

        appointmentPanel.add(infoPanel, BorderLayout.NORTH);
        appointmentPanel.add(scrollPane, BorderLayout.CENTER);
        appointmentPanel.add(buttonPanel2, BorderLayout.SOUTH);

// quản lí
        JPanel recordPanel = new JPanel(new BorderLayout());
        recordPanel.setBackground(Color.WHITE);
        recordPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle_rc = new JLabel("QUẢN LÍ BỆNH NHÂN", SwingConstants.CENTER);
        lblTitle_rc.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle_rc.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        recordPanel.add(lblTitle_rc, BorderLayout.NORTH);

        DefaultTableModel tableModel_rc = new DefaultTableModel();
        tableModel_rc.setColumnIdentifiers(new String[]{"Mã bệnh nhân", "Họ tên", "Giới tính", "Ngày sinh", "SĐT", "Địa chỉ", "CCCD", "Ngày vào viện", "Ngày ra viện", "Nghề nghiệp"});

        JTable table_rc = new JTable(tableModel_rc);
        table_rc.setEnabled(false);
        table_rc.setRowHeight(28);
        table_rc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table_rc.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table_rc.getTableHeader().setBackground(new Color(0, 102, 204));
        table_rc.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane_rc = new JScrollPane(table_rc);

        Runnable loadPatients_rc = () -> {
            if (!isLoggedIn || UserSession.userId == null || !UserSession.role.equals("bacsi")) {

                return;
            }

            tableModel_rc.setRowCount(0);
            try (Connection conn = ConnectionDatabase.getConnection()) {
                String query = "SELECT DISTINCT b.mabenhnhan, b.hoten, b.gioitinh, b.ngaysinh, b.sdt, b.diachi, b.cccd, b.ngayvaovien, b.ngayravien, b.nghe_nghiep " +
                        "FROM benhnhan b " +
                        "JOIN lichkham l ON b.mabenhnhan = l.ma_benh_nhan " +
                        "WHERE l.ma_bac_si = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, UserSession.userId.trim());
                ResultSet rs = stmt.executeQuery();

                boolean hasPatients = false;
                while (rs.next()) {
                    hasPatients = true;
                    String mabenhnhan = rs.getString("mabenhnhan") != null ? rs.getString("mabenhnhan") : "";
                    String hoten = rs.getString("hoten") != null ? rs.getString("hoten") : "";
                    String gioitinh = rs.getString("gioitinh") != null ? rs.getString("gioitinh") : "";
                    String ngaysinh = rs.getDate("ngaysinh") != null ? rs.getDate("ngaysinh").toString() : "";
                    String sdt = rs.getString("sdt") != null ? rs.getString("sdt") : "";
                    String diachi = rs.getString("diachi") != null ? rs.getString("diachi") : "";
                    String cccd = rs.getString("cccd") != null ? rs.getString("cccd") : "";
                    String ngayvaovien = rs.getDate("ngayvaovien") != null ? rs.getDate("ngayvaovien").toString() : "";
                    String ngayravien = rs.getDate("ngayravien") != null ? rs.getDate("ngayravien").toString() : "";
                    String ngheNghiep = rs.getString("nghe_nghiep") != null ? rs.getString("nghe_nghiep") : "";
                    tableModel_rc.addRow(new Object[]{mabenhnhan, hoten, gioitinh, ngaysinh, sdt, diachi, cccd, ngayvaovien, ngayravien, ngheNghiep});
                }
                if (!hasPatients) {
                    tableModel_rc.addRow(new Object[]{"", "Không có bệnh nhân nào.", "", "", "", "", "", "", "", ""});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu bệnh nhân: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        };

// Load data initially
        loadPatients_rc.run();

        JButton refreshButton_rc = new JButton("Làm mới");
        refreshButton_rc.setFocusPainted(false);
        refreshButton_rc.addActionListener(e -> loadPatients_rc.run());
        JPanel buttonPanel_rc = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel_rc.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));
        buttonPanel_rc.add(refreshButton_rc);

        recordPanel.add(scrollPane_rc, BorderLayout.CENTER);
        recordPanel.add(buttonPanel_rc, BorderLayout.SOUTH);



        JPanel contactPanel = createContactContent();
        mainPanel.add(contactPanel, "contact");

        JPanel feedbackPanel;
        if (isLoggedIn && UserSession.role.equals("bacsi")) {
            feedbackPanel = new JPanel();
            JLabel placeholderLabel = new JLabel("Chức năng phản hồi dành cho bệnh nhân.", JLabel.CENTER);
            placeholderLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            feedbackPanel.setLayout(new BorderLayout());
            feedbackPanel.add(placeholderLabel, BorderLayout.CENTER);
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
        mainPanel.add(createAccountPanel(), "account");

        homeMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "home");
            }
        });

        patientMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isLoggedIn || UserSession.userId == null || !UserSession.role.equals("bacsi")) {
                    JOptionPane.showMessageDialog(null, "Bạn chưa đăng nhập với vai trò bác sĩ! Vui lòng đăng nhập lại.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new Login().setVisible(true);
                } else {
                    cardLayout.show(mainPanel, "patient");
                }
            }
        });

        appointmentMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isLoggedIn || UserSession.userId == null || !UserSession.role.equals("bacsi")) {
                    JOptionPane.showMessageDialog(null, "Bạn chưa đăng nhập với vai trò bác sĩ! Vui lòng đăng nhập lại.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new Login().setVisible(true);
                } else {
                    cardLayout.show(mainPanel, "appointment");
                }
            }
        });

        recordMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isLoggedIn || UserSession.userId == null || !UserSession.role.equals("bacsi")) {
                    JOptionPane.showMessageDialog(null, "Bạn chưa đăng nhập với vai trò bác sĩ! Vui lòng đăng nhập lại.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    new Login().setVisible(true);
                } else {
                    loadMedicalRecordData();
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

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "home");
        setVisible(true);

        System.out.println("UserSession.userId at initialization: " + UserSession.userId + ", Role: " + UserSession.role);
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

    private void loadDoctorData(JTextField tfHoVaTen, JComboBox<String> cbGioiTinh,
                                JTextField tfNgaySinh, JTextField tfCMND, JTextField tfSDT,
                                JTextField tfNgheNghiep, JTextArea taDiaChi, JTextField tfMaBS) {
        System.out.println("Loading doctor data for UserSession.userId: " + UserSession.userId);
        System.out.println("UserSession.role: " + UserSession.role);

        if (UserSession.userId == null || !UserSession.role.equals("bacsi")) {
            System.out.println("Invalid userId or role. Aborting data load.");
            JOptionPane.showMessageDialog(null, "Bạn chưa đăng nhập với vai trò bác sĩ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = ConnectionDatabase.getConnection()) {
            String sqlBacSi = "SELECT mabacsi, hoten, gioitinh, ngaysinh, sdt, diachi, cccd, nghe_nghiep " +
                    "FROM bac_si WHERE mabacsi = ?";
            PreparedStatement stmtBacSi = conn.prepareStatement(sqlBacSi);
            stmtBacSi.setString(1, UserSession.userId.trim());
            System.out.println("Executing query: " + sqlBacSi + " with mabacsi = " + UserSession.userId.trim());
            ResultSet rsBacSi = stmtBacSi.executeQuery();

            if (rsBacSi.next()) {
                System.out.println("Doctor data found for mabacsi: " + UserSession.userId);
                tfMaBS.setText(rsBacSi.getString("mabacsi") != null ? rsBacSi.getString("mabacsi") : "");
                tfHoVaTen.setText(rsBacSi.getString("hoten") != null ? rsBacSi.getString("hoten") : "");
                cbGioiTinh.setSelectedItem(rsBacSi.getString("gioitinh") != null ? rsBacSi.getString("gioitinh") : "Nam");
                tfNgaySinh.setText(rsBacSi.getDate("ngaysinh") != null ? rsBacSi.getDate("ngaysinh").toString() : "");
                tfCMND.setText(rsBacSi.getString("cccd") != null ? rsBacSi.getString("cccd") : "");
                tfSDT.setText(rsBacSi.getString("sdt") != null ? rsBacSi.getString("sdt") : "");
                tfNgheNghiep.setText(rsBacSi.getString("nghe_nghiep") != null ? rsBacSi.getString("nghe_nghiep") : "");
                taDiaChi.setText(rsBacSi.getString("diachi") != null ? rsBacSi.getString("diachi") : "");
            } else {
                System.out.println("No doctor data found for mabacsi: " + UserSession.userId);
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin bác sĩ.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu bác sĩ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMedicalRecordData() {
        if (selectedMaBacSi == null || selectedMaBacSi.isEmpty()) {
            return;
        }

        try (Connection conn = ConnectionDatabase.getConnection()) {
            String sqlBenhNhan = "SELECT hoten, gioitinh, ngaysinh, sdt, diachi, mabenhnhan, cccd, ngayvaovien, ngayravien " +
                    "FROM benhnhan WHERE mabenhnhan = ?";
            PreparedStatement stmtBenhNhan = conn.prepareStatement(sqlBenhNhan);
            stmtBenhNhan.setString(1, selectedMaBacSi);
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

            String sqlHoso = "SELECT ly_do_vao_vien, tom_tat_benh_ly, tien_su_benh, ket_qua_xet_nghiem, noi_khoa, phau_thuat, tinh_trang_ra_vien " +
                    "FROM ho_so_benh_an WHERE ma_benh_nhan = ?";
            PreparedStatement stmtHoso = conn.prepareStatement(sqlHoso);
            stmtHoso.setString(1, selectedMaBacSi);
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

    private JPanel createContactContent() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(1550, 890));

        JPanel topPanel_lh = createTopPanel();
        topPanel_lh.setBounds(0,0,1550,445);
        panel.add(topPanel_lh);

        JPanel footerPanel = createFooterPanel();
        footerPanel.setBounds(0, 400, 1550, 400);
        panel.add(footerPanel);

        return panel;
    }
    private JPanel createTopPanel(){

        JPanel topPanel_lh = new JPanel();
        topPanel_lh.setBackground(Color.WHITE);
        topPanel_lh.setPreferredSize(new Dimension(1550, 450));

        ImageIcon imageTop = new ImageIcon("src/Imgs/bando.png");
        Image scaledImage = imageTop.getImage().getScaledInstance(1550, 450, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        topPanel_lh.add(imageLabel, BorderLayout.CENTER);

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(topPanel_lh, BorderLayout.SOUTH);

        return container;
    }

    private JPanel createFooterPanel() {

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(106, 103, 103, 255));
        footerPanel.setPreferredSize(new Dimension(1550, 350));
        footerPanel.setLayout(new GridLayout(2, 3));

        // Các nhãn trong footer
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

        JLabel socialMedia = new JLabel("<html><b>KẾT NỐI VỚI CHÚNG TÔI</b><br>Facebook: fb.com/rkk<br>Twitter: twitter.com/rkk<br>LinkedIn: linkedin.com/hospital/rkk</html>");
        socialMedia.setForeground(Color.WHITE);
        socialMedia.setFont(new Font("Arial", Font.PLAIN, 14));
        socialMedia.setHorizontalAlignment(JLabel.CENTER);
        footerPanel.add(socialMedia);

        // Tạo một panel bao để chứa cả ảnh và footer
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(footerPanel, BorderLayout.SOUTH);

        return container;
    }

    private JPanel createAccountPanel() {
        // Kiểm tra vai trò và userId
        if (UserSession.userId == null || !UserSession.role.equals("bacsi")) {
            return new JPanel(); // Trả về panel rỗng nếu không hợp lệ
        }

        JPanel accountPanel = new JPanel(new BorderLayout());
        accountPanel.setBackground(new Color(0x044A04));
        accountPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));


        JLabel lblTitle = new JLabel("THÔNG TIN TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        lblTitle.setForeground(Color.white);
        accountPanel.add(lblTitle, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(0x044A04));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các ô
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Các trường thông tin
        JTextField tfHoTen = new JTextField(40);
        JTextField tfNgaySinh = new JTextField(40);
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbGioiTinh.setPreferredSize(new Dimension(405,30));
        JTextField tfSDT = new JTextField(40);
        JTextField tfEmail = new JTextField(40);
        JTextField tfCCCD = new JTextField(40);

        // Đặt các trường thành không chỉnh sửa được (chỉ hiển thị)
        tfHoTen.setEditable(false);
        tfNgaySinh.setEditable(false);
        cbGioiTinh.setEnabled(false);
        tfSDT.setEditable(false);
        tfEmail.setEditable(false);
        tfCCCD.setEditable(false);

        // Thêm các nhãn và trường nhập liệu vào panel
        JLabel lblHoTen = new JLabel("Họ và tên:");
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        JLabel lblSDT = new JLabel("Số điện thoại:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblCCCD = new JLabel("CCCD:");

        lblHoTen.setFont(labelFont);
        lblHoTen.setForeground(new Color(255, 255, 255)); // Màu xám đậm
        lblNgaySinh.setFont(labelFont);
        lblNgaySinh.setForeground(new Color(255, 255, 255));
        lblGioiTinh.setFont(labelFont);
        lblGioiTinh.setForeground(new Color(255, 255, 255));
        lblSDT.setFont(labelFont);
        lblSDT.setForeground(new Color(255, 255, 255));
        lblEmail.setFont(labelFont);
        lblEmail.setForeground(new Color(255, 255, 255));
        lblCCCD.setFont(labelFont);
        lblCCCD.setForeground(new Color(255, 255, 255));

        // Thêm các nhãn và trường nhập liệu vào panel
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        contentPanel.add(lblHoTen, gbc);
        gbc.gridx = 1; contentPanel.add(tfHoTen, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        contentPanel.add(lblNgaySinh, gbc);
        gbc.gridx = 3; contentPanel.add(tfNgaySinh, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(lblGioiTinh, gbc);
        gbc.gridx = 1; contentPanel.add(cbGioiTinh, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        contentPanel.add(lblSDT, gbc);
        gbc.gridx = 3; contentPanel.add(tfSDT, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(lblEmail, gbc);
        gbc.gridx = 1; contentPanel.add(tfEmail, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        contentPanel.add(lblCCCD, gbc);
        gbc.gridx = 3; contentPanel.add(tfCCCD, gbc);

        // Load dữ liệu từ cơ sở dữ liệu
        try (Connection conn = ConnectionDatabase.getConnection()) {
            String sql = "SELECT bs.hoten, bs.ngaysinh, bs.gioitinh, bs.sdt, tk.email, bs.cccd " +
                    "FROM bac_si bs JOIN tai_khoan_bs tk ON bs.sdt = tk.sdt " +
                    "WHERE bs.mabacsi = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, UserSession.userId.trim());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tfHoTen.setText(rs.getString("hoten") != null ? rs.getString("hoten") : "");
                tfNgaySinh.setText(rs.getDate("ngaysinh") != null ? rs.getDate("ngaysinh").toString() : "");
                cbGioiTinh.setSelectedItem(rs.getString("gioitinh") != null ? rs.getString("gioitinh") : "Nam");
                tfSDT.setText(rs.getString("sdt") != null ? rs.getString("sdt") : "");
                tfEmail.setText(rs.getString("email") != null ? rs.getString("email") : "");
                tfCCCD.setText(rs.getString("cccd") != null ? rs.getString("cccd") : "");
            } else {
                tfHoTen.setText("Không tìm thấy");
                tfNgaySinh.setText("");
                cbGioiTinh.setSelectedItem("Nam");
                tfSDT.setText("");
                tfEmail.setText("");
                tfCCCD.setText("");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải thông tin tài khoản: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setBackground(new Color(0x044A04));
        passwordPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Đổi mật khẩu",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, // Căn chỉnh tiêu đề (mặc định)
                javax.swing.border.TitledBorder.DEFAULT_POSITION, // Vị trí tiêu đề (mặc định)
                new Font("Segoe UI", Font.PLAIN, 14),
                Color.WHITE
        ));
        GridBagConstraints gbcPw = new GridBagConstraints();
        gbcPw.insets = new Insets(5, 10, 5, 10);
        gbcPw.anchor = GridBagConstraints.WEST;

        JLabel matkhaucu = new JLabel("Mật khẩu cũ:");
        JLabel matkhaumoi = new JLabel("Mật khẩu mới:");
        JLabel nhaplaimk = new JLabel("Xác nhận mật khẩu mới:");
        JPasswordField tfOldPassword = new JPasswordField(15);
        JPasswordField tfNewPassword = new JPasswordField(15);
        JPasswordField tfConfirmPassword = new JPasswordField(15);
        JButton btnChangePassword = new JButton("Đổi mật khẩu");

        matkhaucu.setFont(labelFont);
        matkhaucu.setForeground(Color.WHITE);
        matkhaumoi.setFont(labelFont);
        matkhaumoi.setForeground(Color.WHITE);
        nhaplaimk.setFont(labelFont);
        nhaplaimk.setForeground(Color.WHITE);
        btnChangePassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnChangePassword.setBackground(new Color(30, 144, 255));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setFocusPainted(false);

        gbcPw.gridx = 0; gbcPw.gridy = 0; passwordPanel.add(matkhaucu, gbcPw);
        gbcPw.gridx = 1; passwordPanel.add(tfOldPassword, gbcPw);
        gbcPw.gridx = 0; gbcPw.gridy = 1; passwordPanel.add(matkhaumoi, gbcPw);
        gbcPw.gridx = 1; passwordPanel.add(tfNewPassword, gbcPw);
        gbcPw.gridx = 0; gbcPw.gridy = 2; passwordPanel.add(nhaplaimk, gbcPw);
        gbcPw.gridx = 1; passwordPanel.add(tfConfirmPassword, gbcPw);
        gbcPw.gridx = 0; gbcPw.gridy = 3; gbcPw.gridwidth = 2;
        gbcPw.anchor = GridBagConstraints.CENTER;
        passwordPanel.add(btnChangePassword, gbcPw);

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
                String sqlVerify = "SELECT matkhau FROM tai_khoan_bs WHERE sdt = (SELECT sdt FROM bac_si WHERE mabacsi = ?)";
                PreparedStatement stmtVerify = conn.prepareStatement(sqlVerify);
                stmtVerify.setString(1, UserSession.userId.trim());
                ResultSet rsVerify = stmtVerify.executeQuery();
                if (rsVerify.next()) {
                    String storedPassword = rsVerify.getString("matkhau");
                    if (!storedPassword.equals(oldPassword)) {
                        JOptionPane.showMessageDialog(null, "Mật khẩu cũ không đúng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy tài khoản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update password
                String sqlUpdate = "UPDATE tai_khoan_bs SET matkhau = ? WHERE sdt = (SELECT sdt FROM bac_si WHERE mabacsi = ?)";
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                stmtUpdate.setString(1, newPassword);
                stmtUpdate.setString(2, UserSession.userId.trim());
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

        // Thêm phần đổi mật khẩu vào content panel
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        contentPanel.add(Box.createVerticalStrut(20), gbc);

// Create a centered panel for the password panel
        JPanel centeredPasswordPanel = new JPanel(new GridBagLayout());
        centeredPasswordPanel.setBackground(new Color(0x044A04));
        GridBagConstraints gbcCentered = new GridBagConstraints();
        gbcCentered.insets = new Insets(10, 10, 10, 10);
        gbcCentered.anchor = GridBagConstraints.CENTER;
        gbcCentered.gridx = 0;
        gbcCentered.gridy = 0;
        centeredPasswordPanel.add(passwordPanel, gbcCentered);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER; // Center the panel
        contentPanel.add(centeredPasswordPanel, gbc);

        // Thêm nút "Lưu" ở dưới cùng
        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Lưu");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(255, 215, 0)); // Màu vàng giống trong ảnh
        btnSave.setPreferredSize(new Dimension(1500,40));
        btnSave.setForeground(Color.BLACK);
        btnSave.setFocusPainted(false);
        buttonPanel.add(btnSave);
        buttonPanel.setBackground(new Color(0x044A04));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Tạm thời vô hiệu hóa nút Lưu vì các trường không cho phép chỉnh sửa
        btnSave.setEnabled(false);

        accountPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        accountPanel.add(buttonPanel, BorderLayout.SOUTH);

        return accountPanel;
    }

    public static void main(String[] args) {
        new BacSi();
    }
}