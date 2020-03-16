/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import Client.ChatJFrame;
import DataBase.ChatRecord;
import DataBase.ChatRecordFactory;
import DataBase.UserFactory;
import DataBase.UserInfo;
import MessageGroup.MessageBase;
import MessageGroup.MessageNoraml;
import Server.ServerSendThread;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ChxxxXL
 */
public class DealerNormal extends DealerBase {

    @Override
    public void serverDealer(MessageBase msg, DealerToolkits toolkits) {
        MessageNoraml msgNormal = (MessageNoraml) msg;
        //将消息写入数据库
        ChatRecordFactory crFactory = new ChatRecordFactory(toolkits.DataBaseStream.con);
        crFactory.writeRecord(new ChatRecord(msgNormal));
        //根据要发送的用户名获取其IP 端口和在线状态
        UserFactory userFactory = new UserFactory(toolkits.DataBaseStream.con);
        UserInfo dstUser = userFactory.readUser(msgNormal.toName);
        if (dstUser.state == 1) {
            try {
                //对方在线
                toolkits.serverSender.sendMessage(msgNormal, InetAddress.getByName(dstUser.ip), dstUser.port);
            } catch (UnknownHostException ex) {
                Logger.getLogger(DealerNormal.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {//对方不在线
        }
    }

    @Override
    public void clientDealer(MessageBase msg, DealerToolkits toolkits) {
        MessageNoraml msgNoraml = (MessageNoraml) msg;
        String fromName = msg.fromName;
        if (toolkits.cfMap.containsKey(fromName)) {
            ChatJFrame tmpFrame = toolkits.cfMap.get(fromName);
            tmpFrame.addMessage(msgNoraml);
            tmpFrame.setVisible(true);
        } else {
            //弹出窗口
            ChatJFrame chatJFrame = new ChatJFrame(toolkits.clientSender, fromName);
            this.addChatFrame(toolkits.cfMap, fromName, chatJFrame);
            new Thread(chatJFrame).start();
            chatJFrame.addMessage(msgNoraml);
        }
    }
    private void addChatFrame(Map cfMap, String keyString, ChatJFrame chatJFrame) {
        if (cfMap.containsKey(keyString)) {
            cfMap.replace(keyString, chatJFrame);
        }
        cfMap.put(keyString, chatJFrame);
    }

    private void deleteChatFrame(Map cfMap, String keyString) {
        cfMap.remove(keyString);
    }

}
