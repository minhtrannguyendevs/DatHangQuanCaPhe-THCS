package models;

import utils.TienTe;

public class Mon {
    public String ma;
    public String ten;
    /** Tiền Việt không có số lẻ nên dùng int, tránh hiển thị ra "32000.0". */
    public int gia;
    public String loai;
    /** Nhóm món, quyết định được tùy chỉnh những gì. */
    public NhomMon nhom;

    public Mon(String ma, String ten, int gia, String loai, NhomMon nhom) {
        this.ma = ma;
        this.ten = ten;
        this.gia = gia;
        this.loai = loai;
        this.nhom = nhom;
    }

    /**
     * Đọc một dòng menu: ma;ten;gia;loai;nhom
     * Cột nhóm là tùy chọn, thiếu thì tự đoán để file menu cũ vẫn dùng được.
     */
    public static Mon fromLine(String line) {
        String[] s = line.split(";");
        if (s.length < 4) {
            return null;
        }
        String ma = s[0].trim();
        String ten = s[1].trim();
        // parse qua double rồi ép về int để nhận được cả "32000" lẫn "32000.0"
        int gia = (int) Double.parseDouble(s[2].trim());
        String loai = s[3].trim();
        NhomMon nhom = (s.length >= 5) ? NhomMon.tuTen(s[4]) : NhomMon.suyLuanTu(loai);
        return new Mon(ma, ten, gia, loai, nhom);
    }

    /** Ghi ngược ra dòng menu, dùng khi quản lý sửa menu ở giai đoạn 6. */
    public String toLine() {
        return ma + ";" + ten + ";" + gia + ";" + loai + ";" + nhom.name();
    }

    public String giaDinhDang() {
        return TienTe.dinhDang(gia);
    }

    @Override
    public String toString() {
        return ten + " - " + giaDinhDang();
    }
}
