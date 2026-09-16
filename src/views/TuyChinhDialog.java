package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import models.ChiTietOrder;
import models.Mon;
import models.MucDo;
import models.Size;
import models.Topping;
import utils.TienTe;

/**
 * Hộp thoại chọn tùy chỉnh cho một món trước khi bỏ vào giỏ.
 *
 * Các ô chọn được tạo hết, nhưng chỉ ô nào mà NhomMon cho phép mới được gắn
 * lên giao diện. Nhờ vậy lúc đọc giá trị cứ đọc đều tay, không phải kiểm tra
 * null, mà chọn Croissant thì vẫn không thấy ô "lượng đá" nào hiện ra.
 */
public class TuyChinhDialog extends JDialog {

    private final Mon mon;

    /** Kết quả trả về, null nếu người dùng bấm Hủy. */
    private ChiTietOrder ketQua;

    private JSpinner spSoLuong;
    private JComboBox<Size> cbSize;
    private JComboBox<MucDo> cbCaPhe;
    private JComboBox<MucDo> cbDuong;
    private JComboBox<MucDo> cbDa;
    private JComboBox<MucDo> cbSua;
    private final List<JCheckBox> oTopping = new ArrayList<>();
    private JTextField txtGhiChu;
    private JLabel lblThanhTien;

    /**
     * @param sua món đã có sẵn trong giỏ muốn sửa lại, hoặc null nếu thêm mới
     */
    public TuyChinhDialog(Window cha, Mon mon, ChiTietOrder sua) {
        super(cha, "Tùy chỉnh - " + mon.ten, ModalityType.APPLICATION_MODAL);
        this.mon = mon;
        taoGiaoDien();
        if (sua != null) {
            napTu(sua);
        }
        capNhatThanhTien();
        pack();
        setResizable(false);
        setLocationRelativeTo(cha);
    }

    public ChiTietOrder getKetQua() {
        return ketQua;
    }

    // ----- Dựng giao diện -----

    private void taoGiaoDien() {
        JPanel chinh = new JPanel(new GridBagLayout());
        chinh.setBorder(BorderFactory.createEmptyBorder(16, 20, 12, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        JLabel lblTen = new JLabel(mon.ten);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = y++;
        gbc.gridwidth = 2;
        chinh.add(lblTen, gbc);

        JLabel lblGia = new JLabel("Giá gốc " + mon.giaDinhDang() + "   -   " + mon.nhom.nhan);
        lblGia.setForeground(Color.GRAY);
        gbc.gridy = y++;
        chinh.add(lblGia, gbc);

        gbc.gridy = y++;
        chinh.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // Số lượng: món nào cũng có
        spSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spSoLuong.addChangeListener(e -> capNhatThanhTien());
        y = themDong(chinh, gbc, y, "Số lượng:", spSoLuong);

        // Các ô còn lại: tạo hết, chỉ gắn lên giao diện khi nhóm món cho phép
        cbSize = new JComboBox<>(Size.values());
        cbSize.addActionListener(e -> capNhatThanhTien());
        if (mon.nhom.chinhSize) {
            y = themDong(chinh, gbc, y, "Cỡ ly:", cbSize);
        }

        cbCaPhe = taoComboMucDo();
        if (mon.nhom.chinhCaPhe) {
            y = themDong(chinh, gbc, y, "Lượng cà phê:", cbCaPhe);
        }

        cbDuong = taoComboMucDo();
        if (mon.nhom.chinhDuong) {
            y = themDong(chinh, gbc, y, "Lượng đường:", cbDuong);
        }

        cbDa = taoComboMucDo();
        if (mon.nhom.chinhDa) {
            y = themDong(chinh, gbc, y, "Lượng đá:", cbDa);
        }

        cbSua = taoComboMucDo();
        if (mon.nhom.chinhSua) {
            y = themDong(chinh, gbc, y, "Lượng sữa:", cbSua);
        }

        if (mon.nhom.chinhTopping) {
            JPanel pTopping = new JPanel(new GridBagLayout());
            GridBagConstraints g2 = new GridBagConstraints();
            g2.anchor = GridBagConstraints.WEST;
            g2.gridx = 0;
            int hang = 0;
            for (Topping t : Topping.values()) {
                JCheckBox o = new JCheckBox(t.nhan + "  (+" + TienTe.dinhDang(t.gia) + ")");
                o.putClientProperty("topping", t);
                o.addItemListener(e -> capNhatThanhTien());
                oTopping.add(o);
                g2.gridy = hang++;
                pTopping.add(o, g2);
            }
            y = themDong(chinh, gbc, y, "Món thêm:", pTopping);
        }

        txtGhiChu = new JTextField(18);
        txtGhiChu.setToolTipText("Ví dụ: ít đá thôi, không lấy ống hút");
        y = themDong(chinh, gbc, y, "Ghi chú:", txtGhiChu);

        gbc.gridx = 0;
        gbc.gridy = y++;
        gbc.gridwidth = 2;
        chinh.add(new JSeparator(), gbc);

        lblThanhTien = new JLabel(" ");
        lblThanhTien.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblThanhTien.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridy = y++;
        chinh.add(lblThanhTien, gbc);

        JButton btnOk = new JButton("Thêm vào giỏ");
        JButton btnHuy = new JButton("Hủy");
        btnOk.addActionListener(e -> xacNhan());
        btnHuy.addActionListener(e -> {
            ketQua = null;
            dispose();
        });

        JPanel pNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        pNut.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 14));
        pNut.add(btnHuy);
        pNut.add(btnOk);

        setLayout(new BorderLayout());
        add(chinh, BorderLayout.CENTER);
        add(pNut, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnOk);
    }

    private JComboBox<MucDo> taoComboMucDo() {
        JComboBox<MucDo> cb = new JComboBox<>(MucDo.values());
        cb.setSelectedItem(MucDo.VUA);
        cb.addActionListener(e -> capNhatThanhTien());
        return cb;
    }

    /** Thêm một dòng "nhãn + ô nhập" vào lưới, trả về chỉ số dòng kế tiếp. */
    private int themDong(JPanel p, GridBagConstraints gbc, int y, String nhan, JComponent o) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        p.add(new JLabel(nhan), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        p.add(o, gbc);
        return y + 1;
    }

    // ----- Đọc ghi giá trị -----

    /** Đổ dữ liệu một món đã có trong giỏ lên các ô, dùng khi bấm Sửa. */
    private void napTu(ChiTietOrder ct) {
        spSoLuong.setValue(ct.soLuong);
        cbSize.setSelectedItem(ct.size);
        cbCaPhe.setSelectedItem(ct.luongCaPhe);
        cbDuong.setSelectedItem(ct.luongDuong);
        cbDa.setSelectedItem(ct.luongDa);
        cbSua.setSelectedItem(ct.luongSua);
        txtGhiChu.setText(ct.ghiChu);
        for (JCheckBox o : oTopping) {
            o.setSelected(ct.toppings.contains((Topping) o.getClientProperty("topping")));
        }
    }

    /** Gom giá trị trên các ô thành một ChiTietOrder. */
    private ChiTietOrder doc() {
        ChiTietOrder ct = new ChiTietOrder(mon);
        ct.soLuong = (Integer) spSoLuong.getValue();
        ct.size = (Size) cbSize.getSelectedItem();
        ct.luongCaPhe = (MucDo) cbCaPhe.getSelectedItem();
        ct.luongDuong = (MucDo) cbDuong.getSelectedItem();
        ct.luongDa = (MucDo) cbDa.getSelectedItem();
        ct.luongSua = (MucDo) cbSua.getSelectedItem();
        ct.ghiChu = ChiTietOrder.lamSach(txtGhiChu.getText());
        ct.toppings.clear();
        for (JCheckBox o : oTopping) {
            if (o.isSelected()) {
                ct.toppings.add((Topping) o.getClientProperty("topping"));
            }
        }
        return ct;
    }

    private void capNhatThanhTien() {
        if (lblThanhTien == null) {
            return; // đang dựng giao diện, chưa đủ ô để tính
        }
        ChiTietOrder ct = doc();
        String chiTiet = (ct.soLuong > 1)
                ? TienTe.dinhDang(ct.donGia()) + "  x " + ct.soLuong + "  =  "
                : "";
        lblThanhTien.setText(chiTiet + TienTe.dinhDang(ct.thanhTien()));
    }

    private void xacNhan() {
        String goc = txtGhiChu.getText().trim();
        String sach = ChiTietOrder.lamSach(goc);
        if (!goc.equals(sach)) {
            int chon = JOptionPane.showConfirmDialog(this,
                    "Ghi chú có ký tự không dùng được, sẽ được lưu thành: " + sach,
                    "Ghi chú sẽ bị sửa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (chon != JOptionPane.OK_OPTION) {
                return;
            }
        }
        ketQua = doc();
        dispose();
    }
}
