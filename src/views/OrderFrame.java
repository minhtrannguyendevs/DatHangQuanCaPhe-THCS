package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Account;
import models.ChiTietOrder;
import models.HoaDon;
import models.LoaiPhucVu;
import models.Mon;
import utils.ChuoiViet;
import utils.FileIO;
import utils.TienTe;

/**
 * Màn hình đặt hàng: bên trái là menu để chọn món, bên phải là giỏ hàng.
 *
 * Giỏ hàng chính là đối tượng HoaDon đang dựng dở. Khi nào thanh toán
 * (giai đoạn 4) thì đem HoaDon đó đi ghi file là xong.
 */
public class OrderFrame extends JFrame {

    private final Account taiKhoan;

    private List<Mon> menu = new ArrayList<>();
    private List<Mon> dangHienThi = new ArrayList<>();

    /** Đơn hàng đang dựng. Mỗi lần thanh toán xong sẽ thay bằng đơn mới. */
    private HoaDon gioHang;

    private JTextField txtTimKiem;
    private JTable bangMenu;
    private JTable bangGio;
    private JLabel lblTongTien;
    private JLabel lblSoLuong;
    private JComboBox<LoaiPhucVu> cbPhucVu;
    private JSpinner spSoBan;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnXoaHet;
    private JButton btnThanhToan;

    public OrderFrame(Account taiKhoan) {
        this.taiKhoan = taiKhoan;
        this.gioHang = new HoaDon(taiKhoan.username);

        setTitle("Đặt hàng - " + taiKhoan.username);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        taoGiaoDien();

        menu = FileIO.readMenu();
        hienThiMenu(menu);
        veLaiGio();

        setSize(1000, 600);
        setLocationRelativeTo(null);
    }

    // ----- Dựng giao diện -----

    private void taoGiaoDien() {
        JSplitPane chia = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelMenu(), panelGio());
        chia.setResizeWeight(0.55);
        chia.setDividerLocation(540);
        add(chia, BorderLayout.CENTER);
    }

    private JPanel panelMenu() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 6));

        JPanel tren = new JPanel(new BorderLayout(6, 0));
        tren.add(new JLabel("Tìm món:"), BorderLayout.WEST);
        txtTimKiem = new JTextField();
        txtTimKiem.addActionListener(e -> timKiem());
        tren.add(txtTimKiem, BorderLayout.CENTER);
        JButton btnTim = new JButton("Tìm");
        btnTim.addActionListener(e -> timKiem());
        tren.add(btnTim, BorderLayout.EAST);
        p.add(tren, BorderLayout.NORTH);

        bangMenu = new JTable(new DefaultTableModel(
                new Object[] { "Mã", "Tên món", "Đơn giá", "Nhóm" }, 0) {
            @Override
            public boolean isCellEditable(int dong, int cot) {
                return false;
            }
        });
        bangMenu.setRowHeight(24);
        bangMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Nháy đúp vào một dòng là mở luôn hộp thoại tùy chỉnh, khỏi phải bấm nút
        bangMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    themVaoGio();
                }
            }
        });
        p.add(new JScrollPane(bangMenu), BorderLayout.CENTER);

        JPanel duoi = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton btnChiTiet = new JButton("Xem chi tiết");
        btnChiTiet.addActionListener(e -> ChiTietMonDialog.hien(this, monDangChon()));
        JButton btnThem = new JButton("Thêm vào giỏ");
        btnThem.addActionListener(e -> themVaoGio());
        duoi.add(btnChiTiet);
        duoi.add(btnThem);
        p.add(duoi, BorderLayout.SOUTH);

        return p;
    }

    private JPanel panelGio() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(10, 6, 10, 10));

        JPanel tren = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel tieuDe = new JLabel("GIỎ HÀNG");
        tieuDe.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tren.add(tieuDe);
        tren.add(new JLabel("     Phục vụ:"));
        cbPhucVu = new JComboBox<>(LoaiPhucVu.values());
        cbPhucVu.addActionListener(e -> capNhatOSoBan());
        tren.add(cbPhucVu);
        tren.add(new JLabel("Bàn:"));
        spSoBan = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spSoBan.setPreferredSize(new Dimension(56, 24));
        tren.add(spSoBan);
        p.add(tren, BorderLayout.NORTH);

        bangGio = new JTable(new DefaultTableModel(
                new Object[] { "Món", "SL", "Tùy chỉnh", "Thành tiền" }, 0) {
            @Override
            public boolean isCellEditable(int dong, int cot) {
                return false;
            }
        });
        bangGio.setRowHeight(24);
        bangGio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bangGio.getColumnModel().getColumn(0).setPreferredWidth(120);
        bangGio.getColumnModel().getColumn(1).setPreferredWidth(30);
        bangGio.getColumnModel().getColumn(2).setPreferredWidth(220);
        bangGio.getColumnModel().getColumn(3).setPreferredWidth(90);
        bangGio.getSelectionModel().addListSelectionListener(e -> capNhatNut());
        p.add(new JScrollPane(bangGio), BorderLayout.CENTER);

        JPanel duoi = new JPanel(new BorderLayout(0, 8));

        JPanel nutSua = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnSua = new JButton("Sửa");
        btnSua.addActionListener(e -> suaTrongGio());
        btnXoa = new JButton("Xóa món");
        btnXoa.addActionListener(e -> xoaKhoiGio());
        btnXoaHet = new JButton("Xóa hết");
        btnXoaHet.addActionListener(e -> xoaHet());
        nutSua.add(btnSua);
        nutSua.add(btnXoa);
        nutSua.add(btnXoaHet);
        duoi.add(nutSua, BorderLayout.NORTH);

        JPanel tong = new JPanel(new GridLayout(2, 1));
        lblSoLuong = new JLabel(" ");
        lblSoLuong.setForeground(Color.GRAY);
        lblSoLuong.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTongTien = new JLabel(" ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongTien.setHorizontalAlignment(SwingConstants.RIGHT);
        tong.add(lblSoLuong);
        tong.add(lblTongTien);
        duoi.add(tong, BorderLayout.CENTER);

        btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnThanhToan.setPreferredSize(new Dimension(160, 40));
        btnThanhToan.addActionListener(e -> thanhToan());
        JPanel pTt = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pTt.add(btnThanhToan);
        duoi.add(pTt, BorderLayout.SOUTH);

        p.add(duoi, BorderLayout.SOUTH);
        return p;
    }

    // ----- Menu bên trái -----

    private void hienThiMenu(List<Mon> list) {
        dangHienThi = list;
        DefaultTableModel m = (DefaultTableModel) bangMenu.getModel();
        m.setRowCount(0);
        for (Mon mon : list) {
            m.addRow(new Object[] { mon.ma, mon.ten, mon.giaDinhDang(), mon.nhom.nhan });
        }
    }

    private void timKiem() {
        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.isEmpty()) {
            hienThiMenu(menu);
            return;
        }
        List<Mon> ketQua = new ArrayList<>();
        for (Mon m : menu) {
            if (ChuoiViet.chua(m.ten, tuKhoa)
                    || ChuoiViet.chua(m.ma, tuKhoa)
                    || ChuoiViet.chua(m.nhom.nhan, tuKhoa)) {
                ketQua.add(m);
            }
        }
        hienThiMenu(ketQua);
    }

    private Mon monDangChon() {
        int dong = bangMenu.getSelectedRow();
        return (dong < 0 || dong >= dangHienThi.size()) ? null : dangHienThi.get(dong);
    }

    // ----- Giỏ hàng -----

    private void themVaoGio() {
        Mon mon = monDangChon();
        if (mon == null) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một món trong menu trước.",
                    "Chưa chọn món", JOptionPane.WARNING_MESSAGE);
            return;
        }
        TuyChinhDialog d = new TuyChinhDialog(this, mon, null);
        d.setVisible(true);
        ChiTietOrder ct = d.getKetQua();
        if (ct != null) {
            gioHang.them(ct);
            veLaiGio();
            bangGio.setRowSelectionInterval(gioHang.danhSach.size() - 1, gioHang.danhSach.size() - 1);
        }
    }

    private void suaTrongGio() {
        int dong = bangGio.getSelectedRow();
        if (dong < 0) {
            return;
        }
        ChiTietOrder cu = gioHang.danhSach.get(dong);
        TuyChinhDialog d = new TuyChinhDialog(this, cu.mon, cu);
        d.setVisible(true);
        ChiTietOrder moi = d.getKetQua();
        if (moi != null) {
            gioHang.danhSach.set(dong, moi);
            veLaiGio();
            bangGio.setRowSelectionInterval(dong, dong);
        }
    }

    private void xoaKhoiGio() {
        int dong = bangGio.getSelectedRow();
        if (dong < 0) {
            return;
        }
        gioHang.xoa(dong);
        veLaiGio();
    }

    private void xoaHet() {
        if (gioHang.rong()) {
            return;
        }
        int chon = JOptionPane.showConfirmDialog(this,
                "Xóa toàn bộ " + gioHang.danhSach.size() + " món trong giỏ?",
                "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (chon == JOptionPane.YES_OPTION) {
            gioHang.danhSach.clear();
            veLaiGio();
        }
    }

    /** Vẽ lại bảng giỏ hàng và cập nhật tổng tiền. */
    private void veLaiGio() {
        DefaultTableModel m = (DefaultTableModel) bangGio.getModel();
        m.setRowCount(0);
        for (ChiTietOrder ct : gioHang.danhSach) {
            String mo = ct.moTaTuyChinh();
            m.addRow(new Object[] {
                    ct.mon.ten,
                    ct.soLuong,
                    mo.isEmpty() ? "-" : mo,
                    TienTe.dinhDang(ct.thanhTien())
            });
        }
        lblSoLuong.setText(gioHang.danhSach.size() + " món  /  " + gioHang.tongSoLuong() + " phần");
        lblTongTien.setText("Tổng: " + gioHang.tongTienDinhDang());
        capNhatNut();
    }

    private void capNhatNut() {
        boolean coChon = bangGio.getSelectedRow() >= 0;
        btnSua.setEnabled(coChon);
        btnXoa.setEnabled(coChon);
        btnXoaHet.setEnabled(!gioHang.rong());
        btnThanhToan.setEnabled(!gioHang.rong());
    }

    private void capNhatOSoBan() {
        boolean taiQuan = cbPhucVu.getSelectedItem() == LoaiPhucVu.TAI_QUAN;
        spSoBan.setEnabled(taiQuan);
    }

    // ----- Thanh toán -----

       private void thanhToan() {
        // Không thanh toán khi giỏ trống
        if (gioHang.rong()) return;
    
        // Lưu thông tin phục vụ
        gioHang.loaiPhucVu = (LoaiPhucVu) cbPhucVu.getSelectedItem();
        gioHang.soBan = gioHang.loaiPhucVu == LoaiPhucVu.TAI_QUAN
                ? (int) spSoBan.getValue() : 0;
    
        String noiNhan = gioHang.soBan == 0 ? "Mang đi" : "Bàn " + gioHang.soBan;
        String thongBao = "Phục vụ: " + noiNhan
                + "\nTổng tiền: " + gioHang.tongTienDinhDang()
                + "\n\nXác nhận thanh toán?";
    
        if (JOptionPane.showConfirmDialog(this, thongBao, "Thanh toán",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
    
        // Ghi đơn hàng vào orders.txt
        if (!FileIO.writeOrder(gioHang)) {
            JOptionPane.showMessageDialog(this, "Không thể lưu đơn hàng!");
            return;
        }
    
        // Tạo giỏ hàng mới
        JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
        gioHang = new HoaDon(taiKhoan.username);
        veLaiGio();
       }
 }