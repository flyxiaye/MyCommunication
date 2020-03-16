/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import DataBase.ChatRecord;
import DataBase.ChatRecordFactory;
import DataBase.UserFactory;
import DataBase.UserInfo;
import DataBase.UserPwd;
import com.mysql.cj.protocol.MessageReader;
import java.sql.*;
import exp.*;
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

    public void setShareOnlineStateThread(ShareOnlineStateThread shareOnlineStateThread) {
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
                MessageBase msg = (MessageBase) MessageBase.ByteToObject(datas);
                switch (msg.id) {
                    case MessageBase.NORMAL_MESSAGE: {//普通消息
                        MessageNoraml msgNormal = (MessageNoraml) msg;
                        //将消息写入数据库
                        ChatRecordFactory crFactory = new ChatRecordFactory(DataBaseStream.con);
                        crFactory.writeRecord(new ChatRecord(msgNormal));
                        //根据要发送的用户名获取其IP 端口和在线状态
                        UserFactory userFactory = new UserFactory(DataBaseStream.con);
                        UserInfo dstUser = userFactory.readUser(msgNormal.toName);
                        if (dstUser.state == 1) {//对方在线
                            sender.sendMessage(msgNormal, InetAddress.getByName(dstUser.ip), dstUser.port);
                        } else {//对方不在线
                        }
                    }
                    break;
                    case MessageBase.HEART_MESSAGE: {//心跳包
                        MessageHeart msgHeart = (MessageHeart) msg;
                        UserFactory userFactory = new UserFactory(DataBaseStream.con);
                        userFactory.writeUser(new UserInfo(msgHeart.fromName, toIP.getHostAddress(), toPort, 1));
                        sender.sendMessage(shareOnlineStateThread.msg, toIP, toPort);
                    }
                    break;
                    case MessageBase.LOGIN_MESSAGE: {//登陆信息
                        MessageLoginInfo msgLoginInfo = (MessageLoginInfo) msg;
                        String ack = "false";   //服务器应答信息
                        UserFactory userFactory = new UserFactory(DataBaseStream.con);
                        UserInfo user = userFactory.readUser(msgLoginInfo.fromName);
                        UserPwd userPwd = userFactory.readUserPwd(msgLoginInfo.fromName);
                        if (1 == user.state) {
                            ack = "您已经登陆！";
                        } else if (userPwd.equals(msgLoginInfo.passwd)) {
                            ack = "true";
                            //修改用户信息，写入ip和端口数据
                            user.state = 1;
                            user.ip = toIP.getHostAddress();
                            user.port = toPort;
                            userFactory.writeUser(user);
                            jTextArea1.append(msgLoginInfo.fromName + "已登陆\n");
                            jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                        }
                        MessageBase message = new MessageLoginInfo(ack);
                        sender.sendMessage(message, toIP, toPort);
                    }
                    break;
                    case MessageBase.SINGUP_MESSAGE: { //注册信息
                        MessageSignUpInfo msgSignUpInfo = (MessageSignUpInfo) msg;
                        UserFactory userFactory = new UserFactory(DataBaseStream.con);
                        MessageSignUpInfo message1;
                        if (userFactory.readUserPwd(msg.fromName) == null) {
                            userFactory.writeUser(new UserPwd(msgSignUpInfo.fromName, msgSignUpInfo.passwd));
                            message1 = new MessageSignUpInfo("true");
                            jTextArea1.append(msg.fromName + "已注册\n");
                            jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                        } else {
                            message1 = new MessageSignUpInfo("false");
                        }
                        sender.sendMessage(message1, toIP, toPort);
                    }
                    break;
                    case MessageBase.RECORD_MESSAGE: {    //聊天消息记录
                        MessageRecord msgRecord = (MessageRecord) msg;
                        ChatRecordFactory crFactory = new ChatRecordFactory(DataBaseStream.con);
                        Vector recordVector = crFactory.readChatRecords(
                                msgRecord.fromName, msgRecord.toName);
                        MessageRecord msgR = new MessageRecord(
                                msgRecord.fromName, msgRecord.toName, recordVector);
                        sender.sendMessage(msgR, toIP, toPort);
                    }
                    break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
