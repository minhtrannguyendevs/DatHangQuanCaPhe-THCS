package models;

/**
 * Mức độ tùy chỉnh, dùng chung cho lượng cà phê / đường / đá / sữa.
 * Muốn thêm bớt nấc thì sửa ngay ở đây, toàn bộ chương trình tự cập nhật theo.
 */
public enum MucDo {
    KHONG("Không", 0),
    IT("Ít", 30),
    VUA("Vừa", 50),
    NHIEU("Nhiều", 70),
    CUC_NHIEU("Cực nhiều", 100);

    /** Chữ hiển thị cho người dùng. */
    public final String nhan;
    /** Phần trăm tương ứng, dùng khi in lên hóa đơn. */
    public final int phanTram;

    MucDo(String nhan, int phanTram) {
        this.nhan = nhan;
        this.phanTram = phanTram;
    }

    /** JComboBox gọi hàm này để hiển thị, nên trả về nhãn tiếng Việt. */
    @Override
    public String toString() {
        return nhan;
    }

    /** Đọc lại từ file. Không khớp thì trả về VUA để không làm hỏng cả đơn hàng. */
    public static MucDo tuTen(String ten) {
        if (ten != null) {
            for (MucDo m : values()) {
                if (m.name().equalsIgnoreCase(ten.trim())) {
                    return m;
                }
            }
        }
        return VUA;
    }
}
