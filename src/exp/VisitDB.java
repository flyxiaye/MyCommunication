/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exp;

import Server.ServerJFrame;
import java.net.InetAddress;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChxxxXL
 */
public class VisitDB {

//     MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
//    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
//    final String DB_URL = "jdbc:mysql://localhost:3306/userinfo?useSSL=false&serverTimezone=UTC";
//
//    // 数据库的用户名与密码，需要根据自己的设置
//    final String USER = "root";
//    final String PASS = "cxl123";
    public Connection con;

    public VisitDB() throws SQLException, ClassNotFoundException {
        //连接打开数据库
//        Class.forName(JDBC_DRIVER);
//        con = DriverManager.getConnection(DB_URL, USER, PASS);
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        con = DriverManager.getConnection("jdbc:ucanaccess://UserInfo.accdb");
    }

    public void changetStateByHeartBeat(int userId) {
        //根据心跳包修改临时在线状态
        String sql = "UPDATE userinfo SET tstate = 1, state = 1 WHERE id = " + Integer.toString(userId);
        try {
            Statement stmt = this.con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(VisitDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changetStateByHeartBeat(String userName) {
        //根据心跳包修改临时在线状态
        String sql = "UPDATE userinfo SET tstate = 1, state = 1 WHERE name = '" + userName + "'";
        try {
            Statement stmt = this.con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(VisitDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dealHeartBeat() {
        try {
            //处理心跳包
            Statement stmt = con.createStatement();
            String sql = "SELECT id, tstate FROM userinfo WHERE state = 1";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int tstate = rs.getInt("tstate");
                int userId = rs.getInt("id");
                if (tstate == 0) {
                    sql = "UPDATE userinfo SET state = 0 WHERE id = " + Integer.toString(userId);
                } else {
                    sql = "UPDATE userinfo SET tstate = 0 WHERE id = " + Integer.toString(userId);
                }
                Statement st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(VisitDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
