package CODE;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class PhanhoiDanhgia {
    private JTextArea feedbackTextArea;
    private JPanel starPanel;
    private JButton submitButton;
    private DefaultListModel<String> feedbackListModel;
    private int selectedRating = 0;
    private ArrayList<JLabel> stars = new ArrayList<>();
    private String maBenhNhan;

    public JPanel createFeedbackPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        Border border = new LineBorder(Color.GRAY, 2);
        mainPanel.setBorder(new CompoundBorder(new EmptyBorder(40, 280, 40, 280), border));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,0 , 10,0 );

        maBenhNhan = UserSession.userId;
        if (maBenhNhan == null || maBenhNhan.trim().isEmpty()) {
            JLabel placeholderLabel = new JLabel("Vui lòng đăng nhập để gửi phản hồi.", JLabel.CENTER);
            placeholderLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            mainPanel.add(placeholderLabel);
            return mainPanel;
        }

        JLabel titleLabel = new JLabel("Hãy phản hồi và đánh giá ý kiến của bạn nhé!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JLabel satisfactionLabel = new JLabel("Bạn có hài lòng với sự phục vụ của bệnh viện chúng tôi không?:");
        satisfactionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(satisfactionLabel, gbc);

        ButtonGroup satisfactionGroup = new ButtonGroup();
        JRadioButton yesRadio = new JRadioButton("Có");
        yesRadio.setFocusPainted(false);
        JRadioButton noRadio = new JRadioButton("Không");
        noRadio.setFocusPainted(false);
        satisfactionGroup.add(yesRadio);
        satisfactionGroup.add(noRadio);
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        radioPanel.add(yesRadio);
        radioPanel.add(noRadio);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(radioPanel, gbc);

        JLabel trustReasonLabel = new JLabel("Lý do bạn tin tưởng bệnh viện chúng tôi là gì:");
        trustReasonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(trustReasonLabel, gbc);

        JTextField trustReasonField = new JTextField(30);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(trustReasonField, gbc);

        JLabel dissatisfactionLabel = new JLabel("Trải nghiệm dịch vụ nào bạn cảm thấy hài lòng nhất:");
        dissatisfactionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(dissatisfactionLabel, gbc);

        JTextField dissatisfactionField = new JTextField(30);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        mainPanel.add(dissatisfactionField, gbc);

        JLabel starLabel = new JLabel("Mức độ hài lòng của bạn:");
        starLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        mainPanel.add(starLabel, gbc);

        starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        for (int i = 1; i <= 5; i++) {
            JLabel starIcon = new JLabel("☆");
            starIcon.setFont(new Font("Segoe UI", Font.PLAIN, 40));
            starIcon.setForeground(Color.LIGHT_GRAY);
            final int rating = i;
            starIcon.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    PhanhoiDanhgia.this.setRating(rating);
                }
            });
            stars.add(starIcon);
            starPanel.add(starIcon);
        }
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(starPanel, gbc);

        JLabel additionalFeedbackLabel = new JLabel("Hãy viết thêm nhận xét của bạn ở đây nhé!");
        additionalFeedbackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        mainPanel.add(additionalFeedbackLabel, gbc);

        feedbackTextArea = new JTextArea(5, 30);
        feedbackTextArea.setLineWrap(true);
        feedbackTextArea.setWrapStyleWord(true);
        feedbackTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(feedbackTextArea);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        mainPanel.add(scrollPane, gbc);

        submitButton = new JButton("Gửi phản hồi");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitFeedback(yesRadio.isSelected(), trustReasonField.getText(), dissatisfactionField.getText());
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(submitButton, gbc);

        return mainPanel;
    }

    private void setRating(int rating) {
        this.selectedRating = rating;
        for (int i = 0; i < stars.size(); i++) {
            if (i < rating) {
                stars.get(i).setForeground(Color.ORANGE);
            } else {
                stars.get(i).setForeground(Color.LIGHT_GRAY);
            }
        }
        starPanel.revalidate();
        starPanel.repaint();
    }

    private void submitFeedback(boolean isSatisfied, String trustReason, String dissatisfactionReason) {
        String feedbackText = feedbackTextArea.getText().trim();
        if (feedbackText.isEmpty() || selectedRating == 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập phản hồi và chọn mức đánh giá!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (Connection conn = ConnectionDatabase.getConnection()) {
            String sql = "INSERT INTO danhgia (ma_benh_nhan, rating, comment, hailong, lydo, trainghiem) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, maBenhNhan);
            stmt.setInt(2, selectedRating);
            stmt.setString(3, feedbackText);
            stmt.setBoolean(4, isSatisfied);
            stmt.setString(5, trustReason);
            stmt.setString(6, dissatisfactionReason);
            stmt.executeUpdate();
            feedbackTextArea.setText("");
            setRating(0);
            JOptionPane.showMessageDialog(null, "Cảm ơn bạn đã gửi phản hồi!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi gửi phản hồi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}