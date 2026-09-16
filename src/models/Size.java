package models;

import utils.TienTe;

/** Cỡ ly. Phụ thu nằm ở đây, muốn đổi giá chỉ sửa một chỗ. */
public enum Size {
    M("M", 0),
    L("L", 10000);

    public final String nhan;
    /** Số tiền cộng thêm khi chọn size này. */
    public final int phuThu;

    Size(String nhan, int phuThu) {
        this.nhan = nhan;
        this.phuThu = phuThu;
    }

    @Override
    public String toString() {
        return phuThu == 0 ? nhan : nhan + " (+" + TienTe.dinhDang(phuThu) + ")";
    }

    public static Size tuTen(String ten) {
        if (ten != null) {
            for (Size s : values()) {
                if (s.name().equalsIgnoreCase(ten.trim())) {
                    return s;
                }
            }
        }
        return M;
    }
}
