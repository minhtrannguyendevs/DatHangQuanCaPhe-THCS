package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import utils.TienTe;

/**
 * Một đơn hàng hoàn chỉnh.
 *
 * Định dạng lưu file (mỗi hóa đơn một dòng):
 *   maHD|thoiGian|nhanVien|loaiPhucVu|soBan|tongTien|chiTiet#chiTiet#chiTiet
 *
 * tongTien được ghi ra file dù có thể tính lại, để sau này lỡ đổi giá menu
 * thì hóa đơn cũ vẫn giữ đúng số tiền đã thu.
 */
public class HoaDon {

    private static final DateTimeFormatter DANG_MA = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public String maHD;
    public LocalDateTime thoiGian;
    public String nhanVien;
    public LoaiPhucVu loaiPhucVu = LoaiPhucVu.TAI_QUAN;
    public int soBan;
    public List<ChiTietOrder> danhSach = new ArrayList<>();
    /** Số tiền đã thu, đọc lên từ file. Đơn mới thì bằng 0 và dùng tongTien(). */
    private int tongTienDaLuu = 0;

    public HoaDon(String nhanVien) {
        this.nhanVien = nhanVien;
        this.thoiGian = LocalDateTime.now();
        this.maHD = "HD" + thoiGian.format(DANG_MA);
    }

    private HoaDon() {
        // dùng cho fromLine
    }

    public void them(ChiTietOrder ct) {
        if (ct != null) {
            danhSach.add(ct);
        }
    }

    public void xoa(int viTri) {
        if (viTri >= 0 && viTri < danhSach.size()) {
            danhSach.remove(viTri);
        }
    }

    public boolean rong() {
        return danhSach.isEmpty();
    }

    public int tongSoLuong() {
        int tong = 0;
        for (ChiTietOrder ct : danhSach) {
            tong += ct.soLuong;
        }
        return tong;
    }

    public int tongTien() {
        if (danhSach.isEmpty() && tongTienDaLuu > 0) {
            return tongTienDaLuu;
        }
        int tong = 0;
        for (ChiTietOrder ct : danhSach) {
            tong += ct.thanhTien();
        }
        return tong;
    }

    public String tongTienDinhDang() {
        return TienTe.dinhDang(tongTien());
    }

    public String thoiGianDinhDang() {
        return thoiGian.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // ----- Đọc ghi file -----

    public String toLine() {
        StringBuilder ct = new StringBuilder();
        for (ChiTietOrder c : danhSach) {
            if (ct.length() > 0) {
                ct.append('#');
            }
            ct.append(c.toLine());
        }
        return String.join("|",
                maHD,
                thoiGian.toString(),
                ChiTietOrder.lamSach(nhanVien),
                loaiPhucVu.name(),
                String.valueOf(soBan),
                String.valueOf(tongTien()),
                ct.toString());
    }

    /** FileIO.writeOrder() đang gọi toString(), nên cho nó trỏ về toLine(). */
    @Override
    public String toString() {
        return toLine();
    }

    /** Đọc ngược lại. Cần menu để tra mã món. Dòng hỏng thì trả về null. */
    public static HoaDon fromLine(String line, List<Mon> menu) {
        String[] s = line.split("\\|", -1);
        if (s.length < 7) {
            return null;
        }
        try {
            HoaDon hd = new HoaDon();
            hd.maHD = s[0].trim();
            hd.thoiGian = LocalDateTime.parse(s[1].trim());
            hd.nhanVien = s[2].trim();
            hd.loaiPhucVu = LoaiPhucVu.tuTen(s[3]);
            hd.soBan = Integer.parseInt(s[4].trim());
            hd.tongTienDaLuu = Integer.parseInt(s[5].trim());

            if (!s[6].trim().isEmpty()) {
                for (String dong : s[6].split("#")) {
                    ChiTietOrder ct = ChiTietOrder.fromLine(dong, menu);
                    if (ct != null) {
                        hd.danhSach.add(ct);
                    }
                }
            }
            return hd;
        } catch (RuntimeException e) {
            return null;
        }
    }
}
