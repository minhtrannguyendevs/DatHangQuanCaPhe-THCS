package models;

public class Account {
    public String username;
    public String password;
    public String role;

    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static Account fromString(String line) {
        String[] s = line.split(";");
        if (s.length >= 3) {
            return new Account(s[0].trim(), s[1].trim(), s[2].trim());
        }
        return null;
    }
}