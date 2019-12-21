/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import exp.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.sql.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChxxxXL 发送在线用户列表
 */
public class ShareOnlineStateThread extends Thread {

    public MessageExp msg = null;
    private VisitDB DataBaseStream = null;

    public ShareOnlineStateThread(VisitDB DataBaseStream) {
        this.DataBaseStream = DataBaseStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Statement stmt = this.DataBaseStream.con.createStatement();
                String sql = "SELECT name FROM userinfo WHERE state = 1";
                //收集在线用户列表
                String onlineUser = "";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    onlineUser += rs.getString("name") + " ";
                }
                if (msg == null) {
                    msg = new MessageExp(MessageExp.USER_MESSAGE, onlineUser);
                } else {
                    msg.setData(onlineUser);
                }
                sleep(1000);
            } catch (SQLException ex) {
                Logger.getLogger(ShareOnlineStateThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ShareOnlineStateThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
