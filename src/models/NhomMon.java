package models;

/**
 * Nhóm món, quyết định món đó được tùy chỉnh những gì.
 *
 * Nhờ bảng cờ này mà OrderFrame chỉ cần hỏi "nhóm này có chỉnh đá không?"
 * thay vì viết if...else cho từng loại món. Thêm nhóm mới chỉ cần thêm
 * một dòng ở đây, giao diện tự chạy đúng.
 */
public enum NhomMon {
    // cà phê đường đá sữa size topping
    CAPHE("Cà phê", true, true, true, true, true, false),
    TRASUA("Trà sữa", false, true, true, true, true, true),
    TRA("Trà", false, true, true, false, true, true),
    SINHTO("Sinh tố", false, true, true, true, true, false),
    BANH("Bánh", false, false, false, false, false, false),
    ANVAT("Ăn vặt", false, false, false, false, false, false);

    public final String nhan;
    public final boolean chinhCaPhe;
    public final boolean chinhDuong;
    public final boolean chinhDa;
    public final boolean chinhSua;
    public final boolean chinhSize;
    public final boolean chinhTopping;

    NhomMon(String nhan, boolean chinhCaPhe, boolean chinhDuong, boolean chinhDa,
            boolean chinhSua, boolean chinhSize, boolean chinhTopping) {
        this.nhan = nhan;
        this.chinhCaPhe = chinhCaPhe;
        this.chinhDuong = chinhDuong;
        this.chinhDa = chinhDa;
        this.chinhSua = chinhSua;
        this.chinhSize = chinhSize;
        this.chinhTopping = chinhTopping;
    }

    /** Món có tùy chỉnh được gì không. Bánh và đồ ăn vặt thì không. */
    public boolean coTuyChinh() {
        return chinhCaPhe || chinhDuong || chinhDa || chinhSua || chinhSize || chinhTopping;
    }

    @Override
    public String toString() {
        return nhan;
    }

    public static NhomMon tuTen(String ten) {
        if (ten != null) {
            for (NhomMon n : values()) {
                if (n.name().equalsIgnoreCase(ten.trim())) {
                    return n;
                }
            }
        }
        return ANVAT;
    }

    /**
     * Dành cho dòng menu kiểu cũ chỉ có 4 cột, chưa ghi nhóm món.
     * Đoán tạm từ cột "loại" để file cũ vẫn đọc được, không làm vỡ chương trình.
     */
    public static NhomMon suyLuanTu(String loai) {
        if (loai != null && loai.trim().equalsIgnoreCase("Do an")) {
            return ANVAT;
        }
        return TRA;
    }
}
