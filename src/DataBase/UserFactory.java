/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChxxxXL
 */
public class UserFactory {
    public Connection con ;
    public UserFactory(Connection con){
        this.con = con;
    }
    public UserInfo readUser(String name){
        UserInfo user = null;
        try {
            String sql = "SELECT ip, port, state FROM userinfo WHERE name = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                user = new UserInfo();
                user.userName = name;
                user.ip = rs.getString("ip");
                user.port = rs.getInt("port");
                user.state = rs.getInt("state");
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }
        
    public void writeUser(UserInfo user){
        try {
            String sql =  "UPDATE userinfo SET ip = ?, port = ?, tstate = 1, state = ? WHERE name = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, user.ip);
            stmt.setInt(2, user.port);
            stmt.setInt(3, user.state);
            stmt.setString(4, user.userName);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public UserPwd readUserPwd(String name){
        UserPwd user = null;
        String sql = "SELECT pwd FROM userinfo WHERE name = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                String pwd = rs.getString("pwd");
                user = new UserPwd(name, pwd);
            }
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }
    
    public void writeUser(UserPwd user) {
        try {
            String sql = "INSERT INTO userinfo (name, pwd) VALUES (?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, user.userName);
            stmt.setString(2, user.userPwd);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
}
