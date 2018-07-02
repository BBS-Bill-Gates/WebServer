package Server;

import java.sql.*;

public class Mysql {
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/USER?useSSL=true";
	static String user = "root";
	static String password = "toor";
	public Mysql() {
		
	}
	public ResultSet verify(String ID, String pass) {
		ResultSet rs = null;
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, user, password);
			PreparedStatement ps = con.prepareStatement("select * from Manager where ID=? and PWD=?");
			ps.setObject(1, ID);
			ps.setObject(2, pass);
			rs = ps.executeQuery();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			
		}
		return rs;
	}
	public ResultSet searchGrade(String studentID) {
		ResultSet rs = null;
		try {
			Class.forName(JDBC_DRIVER);
			Connection con = DriverManager.getConnection(DB_URL, user, password);
			PreparedStatement ps = con.prepareStatement("select * from Student where SID=?");
			ps.setObject(1, studentID);
			rs = ps.executeQuery();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("update failure");
		}
		return rs;
	}
	
}
