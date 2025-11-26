package data;

import java.sql.*;

public class ConnectionPool {

    private static ConnectionPool pool = null;

    // Constructor private để chặn việc tạo đối tượng từ bên ngoài
    private ConnectionPool() {
        try {
            // Load Driver PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    // Singleton: Đảm bảo chỉ có 1 ConnectionPool duy nhất chạy
    public static synchronized ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    // Hàm lấy kết nối
    public Connection getConnection() {
        try {
            // --- CẤU HÌNH TỰ ĐỘNG (LOCAL & RENDER) ---
            String dbUrl = System.getenv("DB_URL");
            String username = System.getenv("DB_USER");
            String password = System.getenv("DB_PASS");

            // Nếu không tìm thấy biến môi trường -> Chạy Localhost
            if (dbUrl == null) {
                // Sửa lại thông tin này cho đúng với máy bạn nếu cần
                dbUrl = "jdbc:postgresql://localhost:5432/murach";
                username = "postgres"; 
                password = "123"; // Hoặc pass "sesame" tùy bạn đặt
            }
            // -------------------------------------------

            return DriverManager.getConnection(dbUrl, username, password);
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    // Hàm giải phóng kết nối
    public void freeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}