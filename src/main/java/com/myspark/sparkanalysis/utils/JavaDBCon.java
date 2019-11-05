package com.myspark.sparkanalysis.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * @date: 2019/07/03
 * @author: 范林茂
 */
public class JavaDBCon {

	 private String url = "jdbc:mysql://localhost:3306/sparktest";
	 private String user = "root";
	 private String password = "admin";
	 private String driver = "com.mysql.jdbc.Driver";
	 
	 private Connection conn = null;
	 private PreparedStatement ps = null;
	 
	 private Connection getConnection() throws Exception {
		 Class.forName(driver);
		return DriverManager.getConnection(url, user, password);
	 }
	 /**
	  * 预编译查询
	  * @param sql
	  * @param params
	  * @return
	  * @throws Exception
	  */
	 public ResultSet doQuery(String sql, Object[] params) throws Exception {
		 ResultSet result = null;
		 
		 try {
			 conn = this.getConnection();
			 ps = conn.prepareStatement(sql);
			if(params != null) {
				for (int i = 0; i < params.length; i++) {
					ps.setObject(i+1, params[i]);
				}
			}
			result = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return result;
		 
	 }
	 /**
	  * 关闭资源
	  * @throws SQLException
	  */
	 public void close() throws SQLException {
		if(ps != null) {
			ps.close();
		}
		if(conn != null) {
			conn.close();
		}
			
	 }
	
	
	
}
