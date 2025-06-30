package jdbc;

import java.sql.*;

/**
 * 测试数据库JDBC操作
 * @author Administrator
 */
public class MySQLDemo {

	public static void main(String[] args) throws Exception {
		// 指定访问数据库的驱动程序的名称
		String driver = "com.mysql.jdbc.Driver";
		// 需要访问的数据库URL
		String url = "jdbc:mysql://localhost:3306/course";
		// 数据库访问的用户名
		String db_user = "root";
		// 数据库访问的密码
		String db_pwd = "root";

		// 要执行的查询SQL语句
		String querySql = "SELECT * FROM UserInfo";
		// 要执行的更新SQL语句
		String updateSql = "UPDATE UserInfo SET Cell='88229530' WHERE ID=?";
		
		// 声明一个Connection对象,用于和数据库建立连接
		Connection conn = null;
		// 声明一个Statement对象，用于对数据库执行SQL语句
		Statement stmt = null;
		// 预处理语句
		PreparedStatement ps = null;
		try {
			// 装载驱动程序
			Class.forName(driver);
			// 获取和数据库的连接
			conn = DriverManager.getConnection(url, db_user, db_pwd);
			// 禁止自动递交，设置回滚点
			conn.setAutoCommit(false);
			// 创建一个Statement对象，用于对数据库执行SQL语句
			stmt = conn.createStatement();
			// 执行查询操作，得到的结果存放在ResultSet对象
			ResultSet rs = stmt.executeQuery(querySql);
			// 打印出查询到的所有数据
			System.out.println("ID\tLoginName\tUserName\tUserPwd");
			System.out.println("-------------------------------------------------");
			while (rs.next()) {
				int id = rs.getInt("ID");
				String loginName = rs.getString("LoginName");
				String userName = rs.getString("UserName");
				String passWord = rs.getString("UserPwd");
				System.out.println(id+"\t"+loginName+"\t\t"+userName+"\t\t"+passWord);
			}
			
			// 预编译SQL语句
			ps = conn.prepareStatement(updateSql);
			// 设置参数
			ps.setInt(1,6);
			// 更新数据库的记录，返回更新的记录数
			int count = ps.executeUpdate();
			System.out.println("更新记录："+count+"条");
			// 事务递交
			conn.commit();
		} catch (Exception e) {
			System.out.println("Error:"+e);
			try { // 操作不成功则回滚
				conn.rollback();
			} catch (Exception ee) {}
		} finally {
			try {
				if (conn!=null) conn.close();
				if (stmt!=null) stmt.close();
			} catch (SQLException e) {}
		}
	}

}
