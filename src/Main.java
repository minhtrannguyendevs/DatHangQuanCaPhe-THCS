import javax.swing.SwingUtilities;
import models.Account;
import views.HomeFrame; // Đổi từ views.home.HomeFrame thành views.HomeFrame

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Account account = new Account("admin", "", "", "QUANLY");
            new HomeFrame().setVisible(true);
        });
    }
}