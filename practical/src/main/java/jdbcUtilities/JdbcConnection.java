package jdbcUtilities;

import java.beans.Statement;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConnection {
	private static Connection con;

	public static Connection getConnection() {
		try {
			FileInputStream fis = new FileInputStream("C:\\Users\\prudh\\eclipse-workspace\\practical\\src\\main\\java\\jdbcUtilities\\db.properties");
			Properties p = new Properties();
			p.load(fis);
			Class.forName(p.getProperty("DRIVER"));
			con = DriverManager.getConnection(p.getProperty("URL"), p.getProperty("UNAME"), p.getProperty("PWD"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	public static void closeConnection(Connection con, ResultSet rs, Statement st) {
		try {
			if (con != null) {
				con.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
