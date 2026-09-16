package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Một dòng trong đơn hàng: món gì, mấy ly, và khách chỉnh những gì.
 *
 * Các tùy chỉnh luôn được lưu đầy đủ, nhưng chỉ tùy chỉnh nào mà nhóm món
 * cho phép mới được tính tiền và hiển thị. Nhờ vậy một cái Croissant vẫn
 * có trường "lượng đá" trong bộ nhớ nhưng không bao giờ lộ ra ngoài.
 */
public class ChiTietOrder {

    public Mon mon;
    public int soLuong = 1;
    public Size size = Size.M;
    public MucDo luongCaPhe = MucDo.VUA;
    public MucDo luongDuong = MucDo.VUA;
    public MucDo luongDa = MucDo.VUA;
    public MucDo luongSua = MucDo.VUA;
    public List<Topping> toppings = new ArrayList<>();
    public String ghiChu = "";

    public ChiTietOrder(Mon mon) {
        this.mon = mon;
    }

    public ChiTietOrder(Mon mon, int soLuong) {
        this.mon = mon;
        this.soLuong = soLuong;
    }

    // ----- Tính tiền -----

    public int phuThuTopping() {
        if (!mon.nhom.chinhTopping) {
            return 0;
        }
        int tong = 0;
        for (Topping t : toppings) {
            tong += t.gia;
        }
        return tong;
    }

    public int phuThuSize() {
        return mon.nhom.chinhSize ? size.phuThu : 0;
    }

    /** Giá một phần sau khi cộng hết phụ thu. */
    public int donGia() {
        return mon.gia + phuThuSize() + phuThuTopping();
    }

    public int thanhTien() {
        return donGia() * soLuong;
    }

    // ----- Hiển thị -----

    /** Mô tả tùy chỉnh để in lên hóa đơn, ví dụ: "Size L · Ít đường · Không đá". */
    public String moTaTuyChinh() {
        if (!mon.nhom.coTuyChinh()) {
            return "";
        }
        List<String> phan = new ArrayList<>();
        if (mon.nhom.chinhSize) {
            phan.add("Size " + size.nhan);
        }
        if (mon.nhom.chinhCaPhe) {
            phan.add(luongCaPhe.nhan + " cà phê");
        }
        if (mon.nhom.chinhDuong) {
            phan.add(luongDuong.nhan + " đường");
        }
        if (mon.nhom.chinhDa) {
            phan.add(luongDa.nhan + " đá");
        }
        if (mon.nhom.chinhSua) {
            phan.add(luongSua.nhan + " sữa");
        }
        if (mon.nhom.chinhTopping) {
            for (Topping t : toppings) {
                phan.add(t.nhan);
            }
        }
        if (!ghiChu.isBlank()) {
            phan.add("\"" + ghiChu + "\"");
        }
        return String.join(" · ", phan);
    }

    @Override
    public String toString() {
        String mo = moTaTuyChinh();
        return mon.ten + " x" + soLuong + (mo.isEmpty() ? "" : " (" + mo + ")");
    }

    // ----- Đọc ghi file -----

    /**
     * Bỏ các ký tự dùng làm dấu phân cách, nếu không một câu ghi chú
     * có dấu phẩy sẽ làm lệch cả dòng dữ liệu khi đọc lại.
     */
    public static String lamSach(String s) {
        if (s == null) {
            return "";
        }
        return s.replace('|', ' ')
                .replace('#', ' ')
                .replace(',', ' ')
                .replace('+', ' ')
                .replace(';', ' ')
                .trim();
    }

    /** Ghi thành: ma,soLuong,size,caPhe,duong,da,sua,topping1+topping2,ghiChu */
    public String toLine() {
        StringBuilder tp = new StringBuilder();
        for (Topping t : toppings) {
            if (tp.length() > 0) {
                tp.append('+');
            }
            tp.append(t.name());
        }
        return String.join(",",
                mon.ma,
                String.valueOf(soLuong),
                size.name(),
                luongCaPhe.name(),
                luongDuong.name(),
                luongDa.name(),
                luongSua.name(),
                tp.toString(),
                lamSach(ghiChu));
    }

    /**
     * Đọc ngược lại. Cần danh sách menu để tra mã món ra đối tượng Mon,
     * vì file đơn hàng chỉ lưu mã chứ không lưu tên và giá.
     * Trả về null nếu mã món không còn trong menu.
     */
    public static ChiTietOrder fromLine(String line, List<Mon> menu) {
        String[] s = line.split(",", -1);
        if (s.length < 8) {
            return null;
        }
        Mon mon = null;
        for (Mon m : menu) {
            if (m.ma.equalsIgnoreCase(s[0].trim())) {
                mon = m;
                break;
            }
        }
        if (mon == null) {
            return null;
        }

        ChiTietOrder ct = new ChiTietOrder(mon);
        try {
            ct.soLuong = Integer.parseInt(s[1].trim());
        } catch (NumberFormatException e) {
            ct.soLuong = 1;
        }
        ct.size = Size.tuTen(s[2]);
        ct.luongCaPhe = MucDo.tuTen(s[3]);
        ct.luongDuong = MucDo.tuTen(s[4]);
        ct.luongDa = MucDo.tuTen(s[5]);
        ct.luongSua = MucDo.tuTen(s[6]);

        if (!s[7].trim().isEmpty()) {
            for (String ten : s[7].split("\\+")) {
                Topping t = Topping.tuTen(ten);
                if (t != null) {
                    ct.toppings.add(t);
                }
            }
        }
        ct.ghiChu = (s.length >= 9) ? s[8].trim() : "";
        return ct;
    }
}
