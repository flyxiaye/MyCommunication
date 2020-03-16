/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import DataBase.UserFactory;
import DataBase.UserInfo;
import MessageGroup.MessageBase;
import MessageGroup.MessageHeart;
import Server.ServerSendThread;
import Server.ShareOnlineStateThread;
import java.net.InetAddress;
import java.sql.Connection;

/**
 *
 * @author ChxxxXL
 */
public class DealerHeart extends DealerBase {


    public DealerHeart() {
    }
    @Override
    public void serverDealer(MessageBase msg, DealerToolkits toolkits) {
        MessageHeart msgHeart = (MessageHeart) msg;
        UserFactory userFactory = new UserFactory(toolkits.DataBaseStream.con);
        userFactory.writeUser(new UserInfo(msgHeart.fromName, toolkits.toIP.getHostAddress(), toolkits.toPort, 1));
        toolkits.serverSender.sendMessage(toolkits.shareOnlineStateThread.msg, toolkits.toIP, toolkits.toPort);
    }

    @Override
    public void clientDealer(MessageBase msg, DealerToolkits toolkits) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
