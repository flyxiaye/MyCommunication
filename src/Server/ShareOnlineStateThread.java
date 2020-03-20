/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import DataBase.UserFactory;
import DataBase.UserInfo;
import MessageGroup.MessageOnlineUser;
import DataBase.VisitDB;
import MessageGroup.MessageBase;
import MessageGroup.MessageHeart;
import Notifier.ServerObservable;
import java.sql.*;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChxxxXL 发送在线用户列表
 */
public class ShareOnlineStateThread extends Thread implements ServerObservable {

    public MessageOnlineUser msg = null;
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
                Vector<String> onlineUser = new Vector<>();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    onlineUser.addElement(rs.getString("name"));
                }
                if (msg == null) {
                    msg = new MessageOnlineUser(onlineUser);
                }
                msg.users = onlineUser;
                sleep(1000);
            } catch (SQLException ex) {
                Logger.getLogger(ShareOnlineStateThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ShareOnlineStateThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update(MessageBase msg, InetAddress ip, int port) {
        if (msg instanceof MessageHeart) {
            MessageHeart msgHeart = (MessageHeart) msg;
            UserFactory userFactory = new UserFactory(VisitDB.getConnection());
            userFactory.writeUser(new UserInfo(msgHeart.fromName, ip.getHostAddress(), port, 1));
            ServerSendThread.getSendThread().sendMessage(this.msg, ip, port);
        }
    }
}
