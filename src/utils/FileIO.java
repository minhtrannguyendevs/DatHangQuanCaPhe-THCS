package utils;

import models.Account;
import models.HoaDon;
import models.Mon;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileIO {
    private static final String MON_FILE = "data/menu.txt";
    private static final String USER_FILE = "data/users.txt";
    private static final String ORDER_FILE = "data/orders.txt";

    /**
     * Tìm file kể cả khi chương trình chạy từ thư mục con.
     *
     * Đi ngược từ thư mục hiện tại lên thư mục cha cho tới khi thấy file.
     * Nhờ vậy chạy từ NetBeans (thư mục gốc) hay từ VS Code (thư mục src)
     * đều tìm ra. Trả về null nếu không có.
     */
    private static Path findFile(String relativePath) {
        Path directory = Paths.get("").toAbsolutePath();
        while (directory != null) {
            Path candidate = directory.resolve(relativePath);
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
            directory = directory.getParent();
        }
        return null;
    }

    /**
     * Như findFile nhưng dùng để ghi: chưa có file thì tạo mới.
     *
     * Ưu tiên đặt file vào thư mục "data" đã có sẵn, để đơn hàng không bị
     * ghi lung tung mỗi khi chạy từ một thư mục khác.
     */
    private static Path findOrCreateFile(String relativePath) throws IOException {
        Path daCo = findFile(relativePath);
        if (daCo != null) {
            return daCo;
        }

        Path thuMucCon = Paths.get(relativePath).getParent(); // thường là "data"
        Path tenFile = Paths.get(relativePath).getFileName();

        Path directory = Paths.get("").toAbsolutePath();
        while (directory != null) {
            Path candidate = (thuMucCon == null) ? directory : directory.resolve(thuMucCon);
            if (Files.isDirectory(candidate)) {
                return candidate.resolve(tenFile);
            }
            directory = directory.getParent();
        }

        // Không thấy thư mục data ở đâu cả thì tạo mới cạnh thư mục đang chạy
        Path moi = Paths.get("").toAbsolutePath().resolve(relativePath);
        Files.createDirectories(moi.getParent());
        return moi;
    }

    /** Đọc từng dòng của một file UTF-8, bỏ qua dòng trống. */
    private static List<String> docCacDong(Path file) throws IOException {
        List<String> ketQua = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    ketQua.add(line);
                }
            }
        }
        return ketQua;
    }

    // ----- Menu -----

    public static List<Mon> readMenu() {
        List<Mon> menu = new ArrayList<>();
        Path menuFile = findFile(MON_FILE);
        if (menuFile == null) {
            System.err.println("Khong tim thay file menu: " + MON_FILE);
            return menu;
        }
        try {
            for (String line : docCacDong(menuFile)) {
                try {
                    Mon mon = Mon.fromLine(line);
                    if (mon != null) {
                        menu.add(mon);
                    }
                } catch (RuntimeException e) {
                    System.err.println("Bo qua dong menu khong hop le: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Loi doc file menu: " + e.getMessage());
        }
        return menu;
    }

    // ----- Tài khoản -----

    public static List<Account> readUsers() {
        List<Account> users = new ArrayList<>();
        Path userFile = findFile(USER_FILE);
        if (userFile == null) {
            System.err.println("Khong tim thay file tai khoan: " + USER_FILE);
            return users;
        }
        try {
            for (String line : docCacDong(userFile)) {
                Account user = Account.fromString(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Loi doc file tai khoan: " + e.getMessage());
        }
        return users;
    }

    // ----- Đơn hàng -----

    /** Các mã hóa đơn đang có trong file, để kiểm tra trùng. */
    private static Set<String> docCacMaDaCo(Path orderFile) throws IOException {
        Set<String> ma = new HashSet<>();
        if (!Files.isRegularFile(orderFile)) {
            return ma;
        }
        for (String line : docCacDong(orderFile)) {
            int v = line.indexOf('|');
            ma.add(v < 0 ? line.trim() : line.substring(0, v).trim());
        }
        return ma;
    }

    /**
     * Ghi thêm một hóa đơn vào cuối file.
     *
     * Lưu ý: nếu mã hóa đơn đã tồn tại (hai đơn tạo trong cùng một giây thì
     * mã sinh ra giống hệt nhau), hàm này sẽ đổi order.maHD thành mã mới
     * chưa trùng. Vì vậy hãy in hóa đơn cho khách SAU khi gọi hàm này,
     * để mã trên giấy khớp với mã trong file.
     */
    public static boolean writeOrder(HoaDon order) {
        if (order == null) {
            return false;
        }
        try {
            Path orderFile = findOrCreateFile(ORDER_FILE);

            Set<String> daCo = docCacMaDaCo(orderFile);
            if (daCo.contains(order.maHD)) {
                String goc = order.maHD;
                int lan = 2;
                while (daCo.contains(goc + "-" + lan)) {
                    lan++;
                }
                order.maHD = goc + "-" + lan;
            }

            try (BufferedWriter bw = Files.newBufferedWriter(orderFile, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                bw.write(order.toLine());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Loi ghi file don hang: " + e.getMessage());
            return false;
        }
    }

    /** Đọc toàn bộ lịch sử đơn hàng. Tự đọc menu để tra mã món. */
    public static List<HoaDon> readOrders() {
        return readOrders(readMenu());
    }

    /**
     * Đọc lịch sử đơn hàng với menu đã có sẵn, khỏi đọc lại file menu.
     * Dòng hỏng thì bỏ qua và báo ra màn hình, không làm chết cả danh sách.
     */
    public static List<HoaDon> readOrders(List<Mon> menu) {
        List<HoaDon> orders = new ArrayList<>();
        Path orderFile = findFile(ORDER_FILE);
        if (orderFile == null) {
            return orders; // chua co don nao, khong phai loi
        }
        try {
            for (String line : docCacDong(orderFile)) {
                HoaDon hd = HoaDon.fromLine(line, menu);
                if (hd != null) {
                    orders.add(hd);
                } else {
                    System.err.println("Bo qua dong don hang khong hop le: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Loi doc file don hang: " + e.getMessage());
        }
        return orders;
    }
}
