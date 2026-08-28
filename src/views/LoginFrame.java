package views;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JButton btnThoat;
    private JLabel lblThongBao;

    public LoginFrame() {
        setTitle("Đăng nhập - Quán Cà Phê");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        taoGiaoDien();

        pack();
        setLocationRelativeTo(null);
    }

    private void taoGiaoDien() {
        JPanel panelChinh = new JPanel(new GridBagLayout());
        panelChinh.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTieuDe = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelChinh.add(lblTieuDe, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panelChinh.add(new JLabel("Tên đăng nhập:"), gbc);

        txtTenDangNhap = new JTextField(18);
        gbc.gridx = 1; gbc.gridy = 1;
        panelChinh.add(txtTenDangNhap, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelChinh.add(new JLabel("Mật khẩu:"), gbc);

        txtMatKhau = new JPasswordField(18);
        gbc.gridx = 1; gbc.gridy = 2;
        panelChinh.add(txtMatKhau, gbc);

        lblThongBao = new JLabel(" ", SwingConstants.CENTER);
        lblThongBao.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelChinh.add(lblThongBao, gbc);

        JPanel panelNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnDangNhap = new JButton("Đăng nhập");
        btnThoat = new JButton("Thoát");
        panelNut.add(btnDangNhap);
        panelNut.add(btnThoat);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panelChinh.add(panelNut, gbc);

        btnThoat.addActionListener(e -> System.exit(0));
        btnDangNhap.addActionListener(e -> {
        });

        add(panelChinh);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}