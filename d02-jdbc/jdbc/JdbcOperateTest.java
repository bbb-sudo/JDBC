package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcOperateTest {
	/** ָ���������ݿ��������������� */
	public static String db_driver = "com.mysql.jdbc.Driver";
	/** ��Ҫ���ʵ����ݿ����� */
	public static String db_url = "jdbc:mysql://localhost:3306/cart";
	/** ���ݿ��˻� */
	public static String db_user = "root";
	/** ���ݿ����� */
	public static String db_pass = "root";

	public static void main(String[] args) throws Exception {
		//simpleQuery();
		//updateProduct();
		
		// ��ȡ����
		List<Product> list = queryProduct2(null);
		// ��ӡ����ѯ������������
		System.out.println("ID\tName\t\tPrice\t\tComment");
		System.out.println("-------------------------------------------------");
		for (Product p : list) {
			System.out.println(p.getId()+"\t"+p.getName()+"\t\t"+p.getPrice()+"\t\t"+p.getPrice());
		}
	}
	
	/**
	 * �򵥵Ĳ�ѯ
	 */
	public static void simpleQuery() {
		// Ҫִ�еĲ�ѯSQL���
		String querySql = "SELECT * FROM Product";
		// ����һ��Connection����,���ں����ݿ⽨������
		Connection conn = null;
		// ����һ��Statement�������ڶ����ݿ�ִ��SQL���
		Statement stmt = null;
		try {
			// װ����������
			Class.forName(db_driver);
			// ��ȡ�����ݿ������
			conn = DriverManager.getConnection(db_url, db_user, db_pass);
			// ����һ��Statement�������ڶ����ݿ�ִ��SQL���
			stmt = conn.createStatement();
			// ִ�в�ѯ�������õ��Ľ�������ResultSet����
			ResultSet rs = stmt.executeQuery(querySql);
			// ��ӡ����ѯ������������
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
	 * �򵥵Ĳ�ѯ
	 */
	public static void updateProduct() {
		// Ҫִ�еĸ���SQL���
		String updateSql = "UPDATE Product SET Price=999 WHERE ID=?";
		Connection conn = null;
		// Ԥ�������
		PreparedStatement ps = null;
		try {
			Class.forName(db_driver);
			conn = DriverManager.getConnection(db_url, db_user, db_pass);
			// ��ֹ�Զ��ݽ������ûع���
			conn.setAutoCommit(false);
			// Ԥ����SQL���
			ps = conn.prepareStatement(updateSql);
			// ���ò���
			ps.setInt(1, 7);
			// �������ݿ�ļ�¼�����ظ��µļ�¼��
			int count = ps.executeUpdate();
			System.out.println("���¼�¼��" + count + "��");
			// ����ݽ�
			conn.commit();
		} catch (Exception e) {
			System.out.println("Error:" + e);
			try {
				// �������ɹ���ع�
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
	 * 1)��������������ݿ����ӵĹ�����뿪
	 * 2)�ѳ־û��������߼��з��봦��
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
		// ʹ�����ӹ������ͳһ��ȡ���ݿ�����
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = DBHelper.getConnection();
			if (conn == null) {
				throw new SQLException("ConnectionTest: �޷�������ݿ�����!");
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
