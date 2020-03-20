/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import DataBase.ChatRecord;
import DataBase.ChatRecordFactory;
import DataBase.UserFactory;
import DataBase.UserInfo;
import DataBase.VisitDB;
import MessageGroup.MessageBase;
import MessageGroup.MessageNoraml;
import Server.ServerSendThread;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author ChxxxXL
 */
public class DealerNormal extends DealerBase {
    
    @Override
    public void dealer(MessageBase msg, InetAddress ip, int port) {
        MessageNoraml msgNormal = (MessageNoraml) msg;
        //将消息写入数据库
        ChatRecordFactory crFactory = new ChatRecordFactory(VisitDB.getConnection());
        crFactory.writeRecord(new ChatRecord(msgNormal));
        //根据要发送的用户名获取其IP 端口和在线状态
        UserFactory userFactory = new UserFactory(VisitDB.getConnection());
        UserInfo dstUser = userFactory.readUser(msgNormal.toName);
        if (dstUser.state == 1) {
            try {
                //对方在线
                ServerSendThread.getSendThread().sendMessage(msgNormal, InetAddress.getByName(dstUser.ip), dstUser.port);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
        } else {
            //对方不在线
        }
    }
    
}
