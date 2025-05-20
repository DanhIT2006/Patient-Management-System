package CODE;

public class UserSession {
    public static String maBenhNhan;
    public static String hoTen;
    public static String gioiTinh;
    public static String ngaySinh;
    public static String sdt;
    public static String diaChi;

    // Hàm tiện ích: Xoá thông tin khi đăng xuất
    public static void clear() {
        maBenhNhan = null;
        hoTen = null;
        gioiTinh = null;
        ngaySinh = null;
        sdt = null;
        diaChi = null;
        System.out.println("UserSession cleared. maBenhNhan: " + maBenhNhan);
    }

    // Optional: Add a method to set user session data
    public static void setUserSession(String maBenhNhan, String hoTen, String gioiTinh, String ngaySinh, String sdt, String diaChi) {
        UserSession.maBenhNhan = maBenhNhan != null ? maBenhNhan.trim() : null;
        UserSession.hoTen = hoTen;
        UserSession.gioiTinh = gioiTinh;
        UserSession.ngaySinh = ngaySinh;
        UserSession.sdt = sdt;
        UserSession.diaChi = diaChi;
        System.out.println("UserSession set. maBenhNhan: " + maBenhNhan);
    }
}