package models;

/** Uống tại quán hay mang đi. Giao diện chọn sẽ làm ở giai đoạn 5. */
public enum LoaiPhucVu {
    TAI_QUAN("Tại quán"),
    MANG_DI("Mang đi");

    public final String nhan;

    LoaiPhucVu(String nhan) {
        this.nhan = nhan;
    }

    @Override
    public String toString() {
        return nhan;
    }

    public static LoaiPhucVu tuTen(String ten) {
        if (ten != null) {
            for (LoaiPhucVu l : values()) {
                if (l.name().equalsIgnoreCase(ten.trim())) {
                    return l;
                }
            }
        }
        return TAI_QUAN;
    }
}
