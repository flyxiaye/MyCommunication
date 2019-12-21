/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import com.mysql.cj.protocol.MessageReader;
import java.sql.*;
import exp.MessageExp;
import exp.MessageRecord;
import exp.VisitDB;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author ChxxxXL
 */
public class ServerReceiveThread extends Thread {

    DatagramSocket socket = null;
    DatagramPacket packet = null;
    InetAddress toIP = null;
    int toPort = 0;
    byte[] dataBuf;
    VisitDB DataBaseStream = null;
    JTextArea jTextArea1;
    ServerSendThread sender = null;
    ShareOnlineStateThread shareOnlineStateThread = null;

    public ServerReceiveThread(int fromPort, VisitDB DataBaseStream, JTextArea jTextArea1, ServerSendThread sender) {
        try {
            socket = new DatagramSocket(fromPort);
            this.DataBaseStream = DataBaseStream;
            this.jTextArea1 = jTextArea1;
            this.sender = sender;
        } catch (SocketException ex) {
            Logger.getLogger(ServerReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ServerReceiveThread(DatagramSocket socket, VisitDB DataBaseStream, JTextArea jTextArea1, ServerSendThread sender) {
        this.socket = socket;
        this.DataBaseStream = DataBaseStream;
        this.jTextArea1 = jTextArea1;
        this.sender = sender;
    }
    
    public void setShareOnlineStateThread(ShareOnlineStateThread shareOnlineStateThread){
        this.shareOnlineStateThread = shareOnlineStateThread;
    }

    public void run() {
        jTextArea1.append("服务端启动！\n");
        dataBuf = new byte[20480];
        packet = new DatagramPacket(dataBuf, dataBuf.length);
        while (true) {
            try {
                socket.receive(packet);
                toIP = packet.getAddress();
                toPort = packet.getPort();
                byte[] datas = Arrays.copyOf(packet.getData(), packet.getLength());
                MessageExp msg = (MessageExp) MessageExp.ByteToObject(datas);
                switch (msg.getId()) {
                    case MessageExp.NORMAL_MESSAGE: {//普通消息
                        String toName = msg.getToName();
                        //根据发送过来的IP 端口获取其用户名
                        String sql = "SELECT name FROM userinfo WHERE ip = ? AND port = ?";
                        PreparedStatement stmt = DataBaseStream.con.prepareStatement(sql);
                        stmt.setString(1, toIP.getHostAddress());
                        stmt.setInt(2, toPort);
                        ResultSet rs = stmt.executeQuery();
                        String fromName = null;
                        if (rs.next()) {
                            fromName = rs.getString("name");
                        }
                        //将消息写入数据库
                        DataBaseStream.writeChatRecord(fromName, msg);
                        //根据要发送的用户名获取其IP 端口和在线状态
                        sql = "SELECT ip, port, state FROM userinfo WHERE name = ?";
                        stmt = DataBaseStream.con.prepareStatement(sql);
                        stmt.setString(1, toName);
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                            toIP = InetAddress.getByName(rs.getString("ip"));
                            toPort = rs.getInt("port");
                            int state = rs.getInt("state");
                            if (state == 1) {//对方在线
                                MessageExp clientMsg = new MessageExp(0, fromName, msg.getData());
                                sender.sendMessage(clientMsg, toIP, toPort);
                            } else {//对方不在线

                            }
                        }
                        stmt.close();
                    }
                    break;
                    case MessageExp.HEART_MESSAGE: {//心跳包
                        DataBaseStream.changetStateByHeartBeat(msg.getData().trim());
                        sender.sendMessage(shareOnlineStateThread.msg, toIP, toPort);
                    }
                    break;
                    case MessageExp.LOGIN_MESSAGE: {//登陆信息
                        String ack = "false";   //服务器应答信息
                        String[] s = msg.getData().split(" ");
                        Statement stmt = DataBaseStream.con.createStatement();
                        String sql = "SELECT id, pwd, state FROM userinfo WHERE name = '"
                                + s[0].trim() + "'";
                        ResultSet rs = stmt.executeQuery(sql);
                        if (rs.next()) {
                            String pwd = rs.getString("pwd");
                            int state = rs.getInt("state");
                            int userId = rs.getInt("id");
                            if (1 == state) {
                                ack = "您已经登陆！";
                            } else if (pwd.equals(s[1].trim())) {
                                ack = "true";
                                this.DataBaseStream.changeState(userId, true);
                                //根据id添加ip和端口至数据库
                                this.DataBaseStream.addInetAddress(userId, toIP, toPort);
                                jTextArea1.append(s[0] + "已登陆\n");
                                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                            }
                        }
                        MessageExp message = new MessageExp(MessageExp.LOGIN_MESSAGE, ack);
                        sender.sendMessage(message, toIP, toPort);
                        stmt.close();
                    }
                    break;
                    case MessageExp.SINGUP_MESSAGE: { //注册信息
                        boolean flag = DataBaseStream.signUpUser(msg);
                        MessageExp message1;
                        if (flag) {
                            message1 = new MessageExp(MessageExp.SINGUP_MESSAGE, "true");
                            jTextArea1.append(msg.getToName() + "已注册\n");
                            jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                        } else {
                            message1 = new MessageExp(MessageExp.SINGUP_MESSAGE, "false");
                        }
                        sender.sendMessage(message1, toIP, toPort);
                    }
                    break;
                    case MessageExp.RECORD_MESSAGE: {    //聊天消息记录
                        String toName = msg.getToName();
                        //根据发送过来的IP 端口获取其用户名
                        Statement stmt = DataBaseStream.con.createStatement();
                        String sql = "SELECT name FROM userinfo WHERE ip = '" + toIP.getHostAddress()
                                + "' AND port = " + Integer.toString(toPort);
                        ResultSet rs = stmt.executeQuery(sql);
                        String fromName = null;
                        if (rs.next()) {
                            fromName = rs.getString("name");
                        }
                        //根据双方用户名获取聊天记录
                        sql = "SELECT date, fromname, record FROM chatrecord WHERE fromname = ? AND toname = ? "
                                + "OR fromname = ? AND toname = ?";
                        PreparedStatement pst = DataBaseStream.con.prepareStatement(sql);
                        pst.setString(1, fromName);
                        pst.setString(2, toName);
                        pst.setString(3, toName);
                        pst.setString(4, fromName);
                        ResultSet rsRecord = pst.executeQuery();
                        MessageRecord msgR = new MessageRecord(MessageExp.RECORD_MESSAGE);
                        msgR.setToName(toName);
                        while (rsRecord.next()) {
                            String date = rsRecord.getString("date");
                            String from = rsRecord.getString("fromname");
                            String record = rsRecord.getString("record");
                            msgR.addRecord(date, from, record);
                        }
                        sender.sendMessage(msgR, toIP, toPort);
                        pst.close();
                        stmt.close();
                    }
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ServerReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
