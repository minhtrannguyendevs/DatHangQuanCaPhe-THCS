// import javax.swing.SwingUtilities;
// import models.Account;
// import views.HomeFrame; 

// public class Main {
//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             Account account = new Account("admin"," ", "QUANLY");
//             new HomeFrame().setVisible(true);
//         });
//     }
// }

import javax.swing.SwingUtilities;
import views.LoginFrame;
// Đăng nhập từ trang login (Ok thì sẽ vào home)
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}