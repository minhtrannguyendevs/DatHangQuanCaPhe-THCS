package views;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Account;
import models.Mon;
import utils.ChuoiViet;
import utils.FileIO;

public class HomeFrame extends javax.swing.JFrame {

        private final Account taiKhoan; // Tài khoản đang đăng nhập, dùng để phân quyền và ghi tên lên hóa đơn
        private List<Mon> menu = new ArrayList<>();
        private List<Mon> dangHienThi = new ArrayList<>();
        private OrderFrame orderFrame;

        public HomeFrame(Account taiKhoan) {
                this.taiKhoan = taiKhoan;
                initComponents();
                setTitle("Quán Cà Phê - " + taiKhoan.username + " (" + taiKhoan.role + ")"); // Title                       
                setLocationRelativeTo(null);
                loadData(); // Đọc dữ liệu từ file qua FileIO
        }
        private void loadData() { // Đọc menu và hiển thị tách biệt
                menu = FileIO.readMenu(); 
                hienThi(menu);
        }
        private void hienThi(List<Mon> list) {
                dangHienThi = list; // Lưu lại danh sách đang xuất hiện trên màn hình
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);
                for (Mon m : list) {
                        // Thay vì lấy số thô, gọi m.giaDinhDang() và lấy nhãn enum m.nhom.nhan
                        model.addRow(new Object[] { m.ma, m.ten, m.giaDinhDang(), m.nhom.nhan });
                }
        }
        // Hỗ trợ tìm kiếm bằng tiếng việt / Lọc menu theo ô tìm kiếm. So sánh sau khi bỏ dấu nên gõ "ca phe"/vẫn ra "Cà phê", khỏi phải bật bộ gõ tiếng Việt.
        private void timKiem() {
                String tuKhoa = jTextField1.getText().trim();
                if (tuKhoa.isEmpty()) {
                        hienThi(menu); 
                        return;
                }
                List<Mon> ketQua = new ArrayList<>();
                for (Mon m : menu) {
                        if (ChuoiViet.chua(m.ten, tuKhoa)
                                        || ChuoiViet.chua(m.ma, tuKhoa)
                                        || ChuoiViet.chua(m.nhom.nhan, tuKhoa)) {
                                ketQua.add(m);
                        }
                }
                hienThi(ketQua);
        }
        /** Món đang được chọn trên bảng, null nếu chưa chọn dòng nào. */
        private Mon monDangChon() {
                int dong = jTable1.getSelectedRow();
                return (dong < 0 || dong >= dangHienThi.size()) ? null : dangHienThi.get(dong);
        }
        private void initComponents() {

                jLabel1 = new javax.swing.JLabel();
                jLabel2 = new javax.swing.JLabel();
                jTextField1 = new javax.swing.JTextField();
                jButton2 = new javax.swing.JButton();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                jButton1 = new javax.swing.JButton();
                jButton3 = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setText("MENU QUAN CAFE");
                jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                jLabel2.setText("Tìm kiếm :");
                jTextField1.addActionListener(this::jTextField1ActionPerformed);
                jButton2.setText("OK");
                jButton2.addActionListener(this::jButton2ActionPerformed);
                //Cấu hình bảng không cho phép người dùng nhấp đúp vào ô để sửa chữ trực tiếp
                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {

                                },
                                new String[] {
                                                "Mã món", "Tên món", "Đơn giá (VNĐ)", "Loại"
                                }) {
                        boolean[] canEdit = new boolean[] {
                                        false, false, false, false
                        };

                        @Override
                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                        }
                });
                jScrollPane1.setViewportView(jTable1);

                jButton1.setText("Xem chi tiết món");
                jButton1.addActionListener(this::jButton1ActionPerformed); 
                jButton3.setText("Đặt hàng");
                jButton3.addActionListener(this::jButton3ActionPerformed);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addGap(57, 57, 57)
                                                                                                .addComponent(jLabel2)
                                                                                                .addGap(23, 23, 23)
                                                                                                .addGroup(layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                false)
                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                .addComponent(jTextField1,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                730,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                .addComponent(jButton2))
                                                                                                                .addComponent(jScrollPane1))
                                                                                                .addGroup(layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(jButton1))
                                                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                                                .addGap(26, 26, 26)
                                                                                                                                .addComponent(jButton3))))
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addGap(459, 459, 459)
                                                                                                .addComponent(jLabel1)))
                                                                .addGap(0, 34, Short.MAX_VALUE)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(55, 55, 55)
                                                                .addComponent(jLabel1)
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jButton2,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                26,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(layout.createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(jLabel2)
                                                                                                .addComponent(jTextField1,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jScrollPane1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                418,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(layout.createSequentialGroup()
                                                                                                .addComponent(jButton1)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(jButton3)))
                                                                .addContainerGap(15, Short.MAX_VALUE)));

                pack();
        }
        private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
                timKiem(); }
        private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
                timKiem(); }
        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {      
                ChiTietMonDialog.hien(this, monDangChon());     }
        private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
                new OrderFrame(taiKhoan).setVisible(true);      }

        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton2;
        private javax.swing.JButton jButton3;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTable jTable1;
        private javax.swing.JTextField jTextField1;
}