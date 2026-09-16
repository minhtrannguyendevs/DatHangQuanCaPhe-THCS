package utils;

/** Định dạng tiền Việt: 32000 -> "32.000 đ". */
public class TienTe {

    private TienTe() {
        // lớp tiện ích, không tạo đối tượng
    }

    public static String dinhDang(int soTien) {
        return String.format("%,d", soTien).replace(',', '.') + " đ";
    }
}
