package models;

import utils.TienTe;

/** Món thêm. Mỗi loại tự mang giá riêng nên sau này muốn phân biệt giá rất dễ. */
public enum Topping {
    TRAN_CHAU("Trân châu", 5000),
    THACH_DUA("Thạch dừa", 5000),
    PHO_MAI("Phô mai", 5000),
    KEM_CHEESE("Kem cheese", 5000),
    DAU_DO("Đậu đỏ", 5000);

    public final String nhan;
    public final int gia;

    Topping(String nhan, int gia) {
        this.nhan = nhan;
        this.gia = gia;
    }

    @Override
    public String toString() {
        return nhan + " (+" + TienTe.dinhDang(gia) + ")";
    }

    public static Topping tuTen(String ten) {
        if (ten != null) {
            for (Topping t : values()) {
                if (t.name().equalsIgnoreCase(ten.trim())) {
                    return t;
                }
            }
        }
        return null;
    }
}
