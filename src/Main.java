import javax.swing.SwingUtilities;
import views.LoginFrame;

/**
 * Điểm khởi chạy duy nhất của chương trình.
 * Mở màn hình đăng nhập, đăng nhập thành công thì LoginFrame tự mở HomeFrame.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
