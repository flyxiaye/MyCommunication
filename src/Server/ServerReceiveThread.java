/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import MessageGroup.MessageSignUpInfo;
import MessageGroup.MessageNoraml;
import MessageGroup.MessageLoginInfo;
import MessageGroup.MessageHeart;
import MessageGroup.MessageBase;
import DataBase.ChatRecord;
import DataBase.ChatRecordFactory;
import DataBase.UserFactory;
import DataBase.UserInfo;
import DataBase.UserPwd;
import com.mysql.cj.protocol.MessageReader;
import java.sql.*;
import MessageGroup.MessageRecord;
import DataBase.VisitDB;
import MessageDealer.DealerBase;
import MessageDealer.DealerToolkits;
import MessageDealer.MessageDealerFactory;
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
//        ChatRecordFactory crFactory = new ChatRecordFactory(DataBaseStream.con);
//        UserFactory userFactory = new UserFactory(DataBaseStream.con);
        DealerToolkits toolkits = new DealerToolkits();
        toolkits.setServerThread(sender);
        toolkits.setDataBase(DataBaseStream);
        toolkits.setJComponent(null, jTextArea1);
        toolkits.setShareOnlineThread(shareOnlineStateThread);
        MessageDealerFactory mdFactory = new MessageDealerFactory();
        while (true) {
            try {
                socket.receive(packet);
                toIP = packet.getAddress();
                toPort = packet.getPort();
                toolkits.setAddress(toIP, toPort);
                byte[] datas = Arrays.copyOf(packet.getData(), packet.getLength());
                MessageBase msg = (MessageBase) MessageBase.ByteToObject(datas);
                DealerBase dealer = mdFactory.createrDealer(msg);
                dealer.serverDealer(msg, toolkits);
//                if (msg instanceof MessageNoraml) {//普通消息
//                    MessageNoraml msgNormal = (MessageNoraml) msg;
//                    //将消息写入数据库
//                    crFactory.writeRecord(new ChatRecord(msgNormal));
//                    //根据要发送的用户名获取其IP 端口和在线状态
//                    UserInfo dstUser = userFactory.readUser(msgNormal.toName);
//                    if (dstUser.state == 1) {//对方在线
//                        sender.sendMessage(msgNormal, InetAddress.getByName(dstUser.ip), dstUser.port);
//                    } else {//对方不在线
//                    }
//                } else if (msg instanceof MessageHeart) {//心跳包
//                    MessageHeart msgHeart = (MessageHeart) msg;
//                    userFactory.writeUser(new UserInfo(msgHeart.fromName, toIP.getHostAddress(), toPort, 1));
//                    sender.sendMessage(shareOnlineStateThread.msg, toIP, toPort);
//                } else if (msg instanceof MessageLoginInfo) {//登陆信息
//                    MessageLoginInfo msgLoginInfo = (MessageLoginInfo) msg;
//                    String ack = "false";   //服务器应答信息
//                    UserInfo user = userFactory.readUser(msgLoginInfo.fromName);
//                    UserPwd userPwd = userFactory.readUserPwd(msgLoginInfo.fromName);
//                    if (user == null) {
//                        ack = "不存在该用户";
//                    } else if (1 == user.state) {
//                        ack = "您已经登陆！";
//                    } else if (userPwd.userPwd.equals(msgLoginInfo.passwd)) {
//                        ack = "true";
//                        //修改用户信息，写入ip和端口数据
//                        user.state = 1;
//                        user.ip = toIP.getHostAddress();
//                        user.port = toPort;
//                        userFactory.writeUser(user);
//                        jTextArea1.append(msgLoginInfo.fromName + "已登陆\n");
//                        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
//                    }
//                    MessageBase message = new MessageLoginInfo(ack);
//                    sender.sendMessage(message, toIP, toPort);
//                } else if (msg instanceof MessageSignUpInfo) { //注册信息
//                    MessageSignUpInfo msgSignUpInfo = (MessageSignUpInfo) msg;
//                    MessageSignUpInfo message1;
//                    if (userFactory.readUserPwd(msg.fromName) == null) {
//                        userFactory.writeUser(new UserPwd(msgSignUpInfo.fromName, msgSignUpInfo.passwd));
//                        message1 = new MessageSignUpInfo("true");
//                        jTextArea1.append(msg.fromName + "已注册\n");
//                        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
//                    } else {
//                        message1 = new MessageSignUpInfo("false");
//                    }
//                    sender.sendMessage(message1, toIP, toPort);
//                } else if (msg instanceof MessageRecord) {    //聊天消息记录
//                    MessageRecord msgRecord = (MessageRecord) msg;
//                    Vector recordVector = crFactory.readChatRecords(
//                            msgRecord.fromName, msgRecord.toName);
//                    MessageRecord msgR = new MessageRecord(
//                            msgRecord.fromName, msgRecord.toName, recordVector);
//                    sender.sendMessage(msgR, toIP, toPort);
//                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
