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

    public int matchUser(InetAddress host, int port) {
        //数据库匹配IP和端口号
        String sql = "SELECT id FROM userinfo WHERE ip = '"
                + host.getHostAddress() + "' AND port = " + Integer.toString(port);

        try {
            Statement stmt = this.con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt("id");
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(VisitDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public void changeState(int id, boolean state) {
        //根据id修改在线状态
        String sql = null;
        if (state) {
            sql = "UPDATE userinfo SET state = 1, tstate = 1 WHERE id = " + Integer.toString(id);
        } else {
            sql = "UPDATE userinfo SET state = 0, tstate = 0 WHERE id = " + Integer.toString(id);
        }
        Statement stmt;
        try {
            stmt = this.con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(VisitDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addInetAddress(int id, InetAddress host, int port) {
        String ip = host.getHostAddress();
        PreparedStatement stmt;
        String sql = "update userinfo set ip = ?, port = ? where id = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, ip);
            stmt.setInt(2, port);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(VisitDB.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void writeChatRecord(String fromName, MessageExp msg) {
        //写入聊天记录
//        String sql = "INSERT INTO chatrecord (fromname, toname, record) VALUES ('"
//                + fromName + "', '" + msg.getToName() + "', '" + msg.getData() + "');";
        String sql = "insert into chatrecord (fromname, toname, record) values (?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, fromName);
            stmt.setString(2, msg.getToName());
            stmt.setString(3, msg.getData());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(VisitDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean signUpUser(MessageExp msg) {
        //注册用户
        String userName = msg.getToName();
        String sql = "SELECT id FROM userinfo WHERE name = '" + userName + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {    //重复用户名
                return false;
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(VisitDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] s = msg.getData().split(" ");
        sql = "INSERT INTO userinfo (name, pwd, shortname) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, userName);
            stmt.setString(2, s[0]);
            stmt.setString(3, s[1]);
            stmt.executeUpdate();
            stmt.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VisitDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
