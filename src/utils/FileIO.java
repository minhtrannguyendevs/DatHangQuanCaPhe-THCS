package utils;

import models.Mon;
import models.HoaDon;
import models.Account;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileIO {
    private static final String MON_FILE = "data/menu.txt";
    private static final String USER_FILE = "data/users.txt";
    private static final String ORDER_FILE = "data/orders.txt";

// Tìm file tự động kể cả khi chạy từ thư mục con
    private static Path findFile(String relativePath) {
        Path directory = Paths.get("").toAbsolutePath();
        while (directory != null) {
            Path candidate = directory.resolve(relativePath);
            if (candidate.toFile().isFile()) {
                return candidate;
            }
            directory = directory.getParent();
        }
        return null;
    }

// read menu from file
    public static List<Mon> readMenu(){
        
        List<Mon> menu = new ArrayList<>();
        Path menuFile = findFile(MON_FILE);

        if (menuFile == null) {
            System.err.println("Khong tim thay file menu: " + MON_FILE);
            return menu;
        }

        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(new FileInputStream(menuFile.toFile()), "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null){

                    if(line.trim().isEmpty()) continue;
                    try {
                        Mon mon = Mon.fromLine(line);
                        if(mon != null){
                            menu.add(mon);
                        }
                    } catch (RuntimeException e) {
                        System.err.println("Bo qua dong menu khong hop le: " + line);
                    }
                }

            }
            catch (IOException e) {
                System.err.println("Loi doc file: " + e.getMessage());
            }
            return menu;
    }


// read users from file
    public static List<Account> readUsers(){
        List<Account> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(new FileInputStream(USER_FILE), "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null){
                    if(line.trim().isEmpty()) continue;
                    Account user = Account.fromString(line);
                    if(user != null){
                        users.add(user);
                    }
                }

            }
            catch (IOException e) {
                System.err.println("Loi doc file: " + e.getMessage());
            }
            return users;
    }

// input order to file
    public static boolean writeOrder(HoaDon order){
        try (BufferedWriter bw = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(ORDER_FILE, true), "UTF-8"))) {
                bw.write(order.toString());
                bw.newLine();
                return true;
            }
            catch (IOException e) {
                System.err.println("Loi ghi file: " + e.getMessage());
                return false;
            }
    }

}