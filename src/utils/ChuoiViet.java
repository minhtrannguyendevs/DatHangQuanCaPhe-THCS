package utils;

import java.text.Normalizer;

/**
 * Xử lý chuỗi tiếng Việt cho việc tìm kiếm.
 *
 * Người dùng gõ "ca phe" thì phải tìm ra "Cà phê" — không ai chịu khó bật
 * bộ gõ tiếng Việt chỉ để tìm một món trong menu.
 */
public class ChuoiViet {

    private ChuoiViet() {
        // lớp tiện ích, không tạo đối tượng
    }

    /**
     * Bỏ dấu và chuyển về chữ thường: "Cà phê sữa" -> "ca phe sua".
     *
     * Normalizer.NFD tách nguyên âm ra khỏi dấu thanh, sau đó ta loại bỏ
     * các dấu đó đi. Riêng chữ đ/Đ không phải nguyên âm có dấu nên NFD
     * không tách được, phải thay tay.
     */
    public static String boDau(String s) {
        if (s == null) {
            return "";
        }
        String tach = Normalizer.normalize(s, Normalizer.Form.NFD);
        StringBuilder sb = new StringBuilder(tach.length());
        for (int i = 0; i < tach.length(); i++) {
            char c = tach.charAt(i);
            if (Character.getType(c) == Character.NON_SPACING_MARK) {
                continue; // đây là dấu thanh hoặc dấu mũ, bỏ đi
            }
            if (c == 'đ') {
                c = 'd';
            } else if (c == 'Đ') {
                c = 'D';
            }
            sb.append(c);
        }
        return sb.toString().toLowerCase();
    }

    /** Chuỗi gốc có chứa từ khóa không, bỏ qua dấu và phân biệt hoa thường. */
    public static boolean chua(String goc, String tuKhoa) {
        return boDau(goc).contains(boDau(tuKhoa));
    }
}
