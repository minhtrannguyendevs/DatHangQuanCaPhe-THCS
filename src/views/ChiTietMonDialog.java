package views;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import models.Mon;
import models.MucDo;
import models.Size;
import models.Topping;
import utils.TienTe;

/**
 * Hộp thoại xem thông tin một món.
 *
 * Ngoài giá và loại, hộp thoại còn liệt kê luôn những thứ khách được tùy chỉnh
 * để nhân viên biết trước mà tư vấn, khỏi phải mở màn hình đặt hàng ra xem.
 */
public class ChiTietMonDialog {

    private ChiTietMonDialog() {
        // lớp tiện ích, không tạo đối tượng
    }

    public static void hien(Component cha, Mon mon) {
        if (mon == null) {
            JOptionPane.showMessageDialog(cha,
                    "Hãy chọn một món trong bảng trước.",
                    "Chưa chọn món", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='width:320px; font-family:Segoe UI;'>");
        sb.append("<h2 style='margin:0 0 8px 0;'>").append(mon.ten).append("</h2>");
        sb.append("<table cellpadding='3'>");
        dong(sb, "Mã món", mon.ma);
        dong(sb, "Đơn giá", "<b>" + mon.giaDinhDang() + "</b>");
        dong(sb, "Loại", mon.loai);
        dong(sb, "Nhóm", mon.nhom.nhan);
        sb.append("</table>");

        if (mon.nhom.coTuyChinh()) {
            sb.append("<p style='margin:10px 0 4px 0;'><b>Khách có thể tùy chỉnh:</b></p><ul style='margin:0;'>");
            if (mon.nhom.chinhSize) {
                sb.append("<li>Cỡ ly: ").append(dsSize()).append("</li>");
            }
            if (mon.nhom.chinhCaPhe) {
                sb.append("<li>Lượng cà phê: ").append(dsMucDo()).append("</li>");
            }
            if (mon.nhom.chinhDuong) {
                sb.append("<li>Lượng đường: ").append(dsMucDo()).append("</li>");
            }
            if (mon.nhom.chinhDa) {
                sb.append("<li>Lượng đá: ").append(dsMucDo()).append("</li>");
            }
            if (mon.nhom.chinhSua) {
                sb.append("<li>Lượng sữa: ").append(dsMucDo()).append("</li>");
            }
            if (mon.nhom.chinhTopping) {
                sb.append("<li>Món thêm: ").append(dsTopping()).append("</li>");
            }
            sb.append("</ul>");
        } else {
            sb.append("<p style='margin:10px 0 0 0; color:#777;'><i>Món này không có tùy chỉnh.</i></p>");
        }
        sb.append("</body></html>");

        JOptionPane.showMessageDialog(cha, sb.toString(),
                "Chi tiết món", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void dong(StringBuilder sb, String nhan, String giaTri) {
        sb.append("<tr><td style='color:#666;'>").append(nhan)
                .append("</td><td>").append(giaTri).append("</td></tr>");
    }

    private static String dsSize() {
        List<String> ds = new ArrayList<>();
        for (Size s : Size.values()) {
            ds.add(s.phuThu == 0 ? s.nhan : s.nhan + " +" + TienTe.dinhDang(s.phuThu));
        }
        return String.join(", ", ds);
    }

    private static String dsMucDo() {
        List<String> ds = new ArrayList<>();
        for (MucDo m : MucDo.values()) {
            ds.add(m.nhan);
        }
        return String.join(", ", ds);
    }

    private static String dsTopping() {
        List<String> ds = new ArrayList<>();
        for (Topping t : Topping.values()) {
            ds.add(t.nhan + " +" + TienTe.dinhDang(t.gia));
        }
        return String.join(", ", ds);
    }
}
