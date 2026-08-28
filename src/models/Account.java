package models;

public class Account {
    public String username;
    public String password;
    public String fullName;
    public String role;

    public Account(String username, String password, String fullName, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public static Account fromString(String line) {
        String[] s = line.split(";");
        if (s.length >= 4) {
            return new Account(s[0].trim(), s[1].trim(), s[2].trim(), s[3].trim());
        }
        return null;
    }
}