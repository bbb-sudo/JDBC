package jdbc;

import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class DBHelper {
	/** 指定访问数据库的驱动程序的名称 */
	public static String db_driver = "com.mysql.jdbc.Driver";
	/** 需要访问的数据库名称 */
	public static String db_url = "jdbc:mysql://localhost:3306/cart";
	/** 数据库账户 */
	public static String db_user = "root";
	/** 数据库密码 */
	public static String db_pass = "root";

	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			// 装载驱动程序
			Class.forName(db_driver);
			// 获取和数据库的连接
			conn = DriverManager.getConnection(db_url, db_user, db_pass);
			// 禁止自动递交，设置回滚点
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException ce) {
			ce.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return conn;
	}

	public static Connection getConnection2() throws SQLException {
		javax.sql.DataSource ds = null;
		try {
			Context ctx = new InitialContext();
			// jboss jndi name: java:/cart
			ds = (javax.sql.DataSource) ctx.lookup("java:comp/env/jdbc/cart");
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
		return ds.getConnection();
	}

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
			conn = null;
		}
	}

	public static void close(Statement stmt, ResultSet rs) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) { }
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) { }
		}
	}

}
