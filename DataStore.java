import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class DataStore {
    private static final String DB_URL = "jdbc:sqlite:data.db";
    private static boolean sqliteAvailable = false;
    private static final File PROP_FILE = new File("data.properties");

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            sqliteAvailable = true;
            initDB();
        } catch (Throwable t) {
            sqliteAvailable = false;
        }
    }

    private static void initDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS user_progress (id TEXT PRIMARY KEY, xp INTEGER, level INTEGER, lastStudied TEXT, streak INTEGER, totalStudied INTEGER);");
            }
        } catch (Exception e) {
            sqliteAvailable = false;
        }
    }

    public static synchronized void saveXP(String id, int xp) {
        if (id == null) id = "default";
        if (sqliteAvailable) {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "INSERT INTO user_progress(id,xp) VALUES(?,?) ON CONFLICT(id) DO UPDATE SET xp=?;";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, id);
                    ps.setInt(2, xp);
                    ps.setInt(3, xp);
                    ps.executeUpdate();
                }
                return;
            } catch (Exception ignored) {}
        }
        try {
            Properties p = new Properties();
            if (PROP_FILE.exists()) {
                try (FileInputStream in = new FileInputStream(PROP_FILE)) { p.load(in); }
            }
            p.setProperty(id + ".xp", Integer.toString(xp));
            try (FileOutputStream out = new FileOutputStream(PROP_FILE)) { p.store(out, "user data fallback"); }
        } catch (Exception ignored) {}
    }

    public static synchronized int getXP(String id) {
        if (id == null) id = "default";
        if (sqliteAvailable) {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String q = "SELECT xp FROM user_progress WHERE id = ? LIMIT 1";
                try (PreparedStatement ps = conn.prepareStatement(q)) {
                    ps.setString(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) return rs.getInt("xp");
                    }
                }
            } catch (Exception ignored) {}
        }
        try {
            Properties p = new Properties();
            if (PROP_FILE.exists()) {
                try (FileInputStream in = new FileInputStream(PROP_FILE)) { p.load(in); }
                String v = p.getProperty(id + ".xp");
                if (v != null) return Integer.parseInt(v);
            }
        } catch (Exception ignored) {}
        return 0;
    }
}
