package CODE;

public class UserSession {
    public static String userId; // Có thể là mabenhnhan, maBacSi, hoặc mã khác
    public static String role;   // "benhnhan", "bacsi", hoặc "bophankhac"
    public static String hoTen;
    public static String gioiTinh;
    public static String ngaySinh;
    public static String sdt;
    public static String diaChi;

    public static void clear() {
        userId = null;
        role = null;
        hoTen = null;
        gioiTinh = null;
        ngaySinh = null;
        sdt = null;
        diaChi = null;
        System.out.println("UserSession cleared. userId: " + userId);
    }

    public static void setUserSession(String userId, String role, String hoTen, String gioiTinh, String ngaySinh, String sdt, String diaChi) {
        UserSession.userId = userId != null ? userId.trim() : null;
        UserSession.role = role;
        UserSession.hoTen = hoTen;
        UserSession.gioiTinh = gioiTinh;
        UserSession.ngaySinh = ngaySinh;
        UserSession.sdt = sdt;
        UserSession.diaChi = diaChi;
        System.out.println("UserSession set. userId: " + userId + ", role: " + role);
    }
}