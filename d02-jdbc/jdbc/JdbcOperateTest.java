package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcOperateTest {
	/** 指定访问数据库的驱动程序的名称 */
	public static String db_driver = "com.mysql.jdbc.Driver";
	/** 需要访问的数据库名称 */
	public static String db_url = "jdbc:mysql://localhost:3306/cart";
	/** 数据库账户 */
	public static String db_user = "root";
	/** 数据库密码 */
	public static String db_pass = "root";

	public static void main(String[] args) throws Exception {
		//simpleQuery();
		//updateProduct();
		
		// 获取数据
		List<Product> list = queryProduct2(null);
		// 打印出查询到的所有数据
		System.out.println("ID\tName\t\tPrice\t\tComment");
		System.out.println("-------------------------------------------------");
		for (Product p : list) {
			System.out.println(p.getId()+"\t"+p.getName()+"\t\t"+p.getPrice()+"\t\t"+p.getPrice());
		}
	}
	
	/**
	 * 简单的查询
	 */
	public static void simpleQuery() {
		// 要执行的查询SQL语句
		String querySql = "SELECT * FROM Product";
		// 声明一个Connection对象,用于和数据库建立连接
		Connection conn = null;
		// 声明一个Statement对象，用于对数据库执行SQL语句
		Statement stmt = null;
		try {
			// 装载驱动程序
			Class.forName(db_driver);
			// 获取和数据库的连接
			conn = DriverManager.getConnection(db_url, db_user, db_pass);
			// 创建一个Statement对象，用于对数据库执行SQL语句
			stmt = conn.createStatement();
			// 执行查询操作，得到的结果存放在ResultSet对象
			ResultSet rs = stmt.executeQuery(querySql);
			// 打印出查询到的所有数据
			System.out.println("ID\tName\t\tPrice\t\tComment");
			System.out.println("-------------------------------------------------");
			while (rs.next()) {
				String pId = rs.getString("ID");
				String name = rs.getString("Name");
				double price = rs.getDouble("Price");
				String comment = rs.getString("Comment");
				System.out.println(pId + "\t" + name + "\t\t" + price + "\t\t" + comment);
			}

		} catch (Exception e) {
			System.out.println("Error:" + e);
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
			}
		}
	}
	
	/**
	 * 简单的查询
	 */
	public static void updateProduct() {
		// 要执行的更新SQL语句
		String updateSql = "UPDATE Product SET Price=999 WHERE ID=?";
		Connection conn = null;
		// 预处理语句
		PreparedStatement ps = null;
		try {
			Class.forName(db_driver);
			conn = DriverManager.getConnection(db_url, db_user, db_pass);
			// 禁止自动递交，设置回滚点
			conn.setAutoCommit(false);
			// 预编译SQL语句
			ps = conn.prepareStatement(updateSql);
			// 设置参数
			ps.setInt(1, 7);
			// 更新数据库的记录，返回更新的记录数
			int count = ps.executeUpdate();
			System.out.println("更新记录：" + count + "条");
			// 事务递交
			conn.commit();
		} catch (Exception e) {
			System.out.println("Error:" + e);
			try {
				// 操作不成功则回滚
				conn.rollback();
			} catch (Exception ee) { }
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) { }
		}
	}
	
	/**
	 * Style Rule: 
	 * 1)把事务管理与数据库连接的管理分离开
	 * 2)把持久化操作从逻辑中分离处理。
	 */
	public static List<Product> queryProduct(Connection conn, String productId) throws SQLException {
		List<Product> list = new ArrayList<Product>();
		String sql = "select ID, Name, Price, Comment from Product ";
		if (productId != null && !"".equals(productId))
			sql = sql + " where ID = ? ";
		ResultSet rs = null;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			if (productId != null && !"".equals(productId))
				ps.setInt(1, Integer.parseInt(productId));
			rs = ps.executeQuery();
			
			while (rs.next()) {
				int pId = rs.getInt("ID");
				String name = rs.getString("Name");
				double price = rs.getDouble("Price");
				String comment = rs.getString("Comment");
				Product p = new Product();
				p.setId(pId);
				p.setName(name);
				p.setPrice(price);
				p.setComment(comment);
				list.add(p);
			}
			rs.close();
		} catch (SQLException ex) {
			System.err.println("queryProduct: " + ex.getMessage());
			return list;
		}
		return list;
	}

	public static List<Product> queryProduct2(String productId) throws SQLException {
		List<Product> list = new ArrayList<Product>();
		String sql = "select ID, Name, Price, Comment from Product ";
		if (productId != null && !"".equals(productId))
			sql = sql + " where ID = ? ";
		// 使用连接管理对象统一获取数据库连接
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = DBHelper.getConnection();
			if (conn == null) {
				throw new SQLException("ConnectionTest: 无法获得数据库连接!");
			}
			ps = conn.prepareStatement(sql);
			if (productId != null && !"".equals(productId))
				ps.setInt(1, Integer.parseInt(productId));
			rs = ps.executeQuery();
			
			while (rs.next()) {
				int pId = rs.getInt("ID");
				String name = rs.getString("Name");
				double price = rs.getDouble("Price");
				String comment = rs.getString("Comment");
				Product p = new Product();
				p.setId(pId);
				p.setName(name);
				p.setPrice(price);
				p.setComment(comment);
				list.add(p);
			}
			rs.close();
		} catch (SQLException ex) {
			System.err.println("queryProduct: " + ex.getMessage());
			return list;
		} finally {
			DBHelper.close(conn);
			DBHelper.close(ps, rs);
		}

		return list;
	}

}
