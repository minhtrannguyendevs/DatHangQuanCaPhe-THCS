package models;

public class Mon {
    public String ma;
    public String ten;
    public double gia;
    public String loai;

    public Mon(String ma, String ten, double gia, String loai) {
        this.ma = ma;
        this.ten = ten;
        this.gia = gia;
        this.loai = loai;
    }

    public static Mon fromLine(String line) {
        String[] s = line.split(";");
        if (s.length >= 4) {
            return new Mon(s[0].trim(), s[1].trim(), Double.parseDouble(s[2].trim()), s[3].trim());
        }
        return null;
    }
}