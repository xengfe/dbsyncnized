package com.yeecare.bz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.yeecare.bean.Entity;
import com.yeecare.util.Base;
import com.yeecare.util.DateStrUtl;
import com.yeecare.util.Log;



public class DbUtil extends Base {


	public static Connection connetion(String url, String username, String pwd) {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static List<Entity> select(String table,boolean first) {
		List<Entity> selectResult = new ArrayList<Entity>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = connetion(configInfo.url, configInfo.username, configInfo.password);
			stmt = conn.createStatement();
			String sql ="";
			if (first) {
				sql = "select * from " + table ;
			}else {
				sql = "select * from " + table + " where C_CreateTime > SUBDATE(CURRENT_TIMESTAMP,INTERVAL 2 HOUR)";
			}
			Log.i(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {

				String cid = rs.getString("C_ID");
				String uid = rs.getString("C_UID");
				String did = rs.getString("C_DID");
				long dsync = rs.getLong("C_DSYNC");
				Timestamp time = rs.getTimestamp("C_TIME");
				double glu = rs.getDouble("C_GLU");
				short flag = rs.getShort("C_FLAG");
				short res = rs.getShort("C_RES");
				short ctype = rs.getShort("C_CTYPE");
				short upload = rs.getShort("C_UPLOAD");
				Timestamp creattime = rs.getTimestamp("C_CreateTime");
				String ip = rs.getString("C_ClientIP");

				selectResult.add(new Entity(cid, uid,did, dsync, time, glu, flag,
						res, ctype, upload, creattime, ip));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return selectResult;

	}

	public static void insert(List<Entity> news) {
		if (news == null || news.size() <= 0) {
			return;
		}

		Connection conn = null;
		Statement stmt = null;
		int rs = 0;//返回值是更新的条数
		try {
			conn = connetion(configInfo.url, configInfo.username, configInfo.password);
			stmt = conn.createStatement();

			for (int i = 0; i < news.size(); i++) {
				String cid = news.get(i).C_ID;
				String uid = news.get(i).C_UID;
				String did = news.get(i).C_DID;//"13097937"
				long dsync = news.get(i).C_DSYNC;
				Timestamp time = news.get(i).C_TIME;
				double glu = news.get(i).C_GLU;
				short flag = news.get(i).C_FLAG;
				short res = news.get(i).C_RES;
				short ctype = news.get(i).C_CTYPE;
				short upload = news.get(i).C_UPLOAD;
				Timestamp creattime = news.get(i).C_CreateTime;
				String ip = news.get(i).C_ClientIP;

				String sql = "insert into  "
						+ dist_table
						+ " (C_ID,C_UID,C_DID,C_DSYNC,C_TIME,C_GLU,C_FLAG,C_RES,C_CTYPE,C_UPLOAD,C_CreateTime,C_ClientIP)"
						+ " values('" + cid + "','" + uid + "','" + did + "','" + dsync + "',"
						+ "STR_TO_DATE('" + DateStrUtl.getFormatTimestamp(time)
						+ "','%Y-%m-%d %H:%i:%s')" + ",'" + glu + "','" + flag
						+ "','" + res + "','" + ctype + "','" + upload + "',"
						+ "STR_TO_DATE('" + DateStrUtl.getFormatTimestamp(creattime)
						+ "','%Y-%m-%d %H:%i:%s')" + ",'" + ip + "');";
				System.out.println(sql);
				rs = stmt.executeUpdate(sql);
				System.out.println("save data successfully");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}
	
	public static void delete(){
		Connection conn = null;
		Statement stmt = null;
		int rs = 0;//返回值是更新的条数
		try {
			conn = connetion(configInfo.url, configInfo.username, configInfo.password);
			stmt = conn.createStatement();
			String sql = "delete from " + source_table ;
			rs = stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

}
