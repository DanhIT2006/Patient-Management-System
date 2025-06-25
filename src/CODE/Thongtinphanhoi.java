package CODE;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Thongtinphanhoi extends JFrame {

    private JTable tableDanhGia;
    private DefaultTableModel modelDanhGia;

    public Thongtinphanhoi() {
        setTitle("Phản hồi từ bệnh nhân");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        loadDanhGia();
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Danh sách phản hồi"));
        getContentPane().add(mainPanel);

        // Cập nhật các cột dựa trên bảng `danhgia`
        modelDanhGia = new DefaultTableModel();
        modelDanhGia.setColumnIdentifiers(new String[]{
                 "Mã bệnh nhân", "Số sao", "Bình luận", "Hài lòng", "Lý do", "Trải nghiệm"
        });

        tableDanhGia = new JTable(modelDanhGia);
        tableDanhGia.setEnabled(false);
        tableDanhGia.setRowHeight(28);
        tableDanhGia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableDanhGia.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableDanhGia.getTableHeader().setBackground(new Color(0, 102, 204));
        tableDanhGia.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tableDanhGia);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void loadDanhGia() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/doancoso", "root", "")) {
            String sql = "SELECT * FROM danhgia";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelDanhGia.addRow(new Object[]{
                        rs.getString("ma_benh_nhan"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getString("hailong"),
                        rs.getString("lydo"),
                        rs.getString("trainghiem")
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu phản hồi.\nChi tiết: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Thongtinphanhoi() .setVisible(true));
    }
}
