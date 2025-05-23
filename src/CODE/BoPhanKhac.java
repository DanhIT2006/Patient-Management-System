package CODE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoPhanKhac extends JFrame {

    private boolean isLoggedIn = false;

    public BoPhanKhac(boolean isLoggedIn){
        this.isLoggedIn = isLoggedIn;
        initUI();

    }
    private Image[] bannerImages;
    private int currentBannerIndex = 0;

    public BoPhanKhac(){
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

        MarqueePanel_BS marqueePanel = new MarqueePanel_BS("Welcome to the Patient Management System!");

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

    class MarqueePanel_BS extends JPanel implements ActionListener {
        private String text;
        private int x;
        private int textWidth;
        private Timer timer;

        public MarqueePanel_BS(String text) {
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

    public static void main(String[] args) {
        new BenhNhan();
    }
}
