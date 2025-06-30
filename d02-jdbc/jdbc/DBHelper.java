package jdbc;

import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class DBHelper {
	/** ָ���������ݿ��������������� */
	public static String db_driver = "com.mysql.jdbc.Driver";
	/** ��Ҫ���ʵ����ݿ����� */
	public static String db_url = "jdbc:mysql://localhost:3306/cart";
	/** ���ݿ��˻� */
	public static String db_user = "root";
	/** ���ݿ����� */
	public static String db_pass = "root";

	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			// װ����������
			Class.forName(db_driver);
			// ��ȡ�����ݿ������
			conn = DriverManager.getConnection(db_url, db_user, db_pass);
			// ��ֹ�Զ��ݽ������ûع���
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
