package views;

import models.Account;
import utils.FileIO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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

        // Sự kiện nút Thoát
        btnThoat.addActionListener(e -> System.exit(0));

        // Sự kiện nút Đăng nhập kết nối với FileIO trong utils
        btnDangNhap.addActionListener(e -> xuLyDangNhap());

        // Cho phép nhấn Enter tại ô mật khẩu để đăng nhập nhanh
        txtMatKhau.addActionListener(e -> xuLyDangNhap());

        add(panelChinh);
    }

    private void xuLyDangNhap() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();

        // 1. Kiểm tra trường rỗng
        if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            lblThongBao.setForeground(Color.RED);
            lblThongBao.setText("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            return;
        }

        // 2. Đọc danh sách tài khoản từ file data/users.txt thông qua FileIO
        List<Account> danhSachAccount = FileIO.readUsers();
        Account loggedInAccount = null;

        if (danhSachAccount != null) {
            for (Account acc : danhSachAccount) {
                if (acc != null && acc.username != null && acc.password != null) {
                    if (acc.username.equals(tenDangNhap) && acc.password.equals(matKhau)) {
                        loggedInAccount = acc;
                        break;
                    }
                }
            }
        }

        // 3. Kiểm tra kết quả xác thực
        if (loggedInAccount != null) {
            lblThongBao.setForeground(new Color(0, 128, 0));
            lblThongBao.setText("Đăng nhập thành công!");

            JOptionPane.showMessageDialog(
                    this,
                    "Đăng nhập thành công!\nXin chào " + loggedInAccount.fullName + " (" + loggedInAccount.role + ")",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Đóng cửa sổ Login sau khi đăng nhập thành công
            this.dispose();
            try {
                new HomeFrame().setVisible(true);
            } catch (Exception ex) {
                // Mở HomeFrame nếu có
            }
        } else {
            lblThongBao.setForeground(Color.RED);
            lblThongBao.setText("Tên đăng nhập hoặc mật khẩu không chính xác!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
