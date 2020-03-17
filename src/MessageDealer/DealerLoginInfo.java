/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import DataBase.UserFactory;
import DataBase.UserInfo;
import DataBase.UserPwd;
import MessageGroup.MessageBase;
import MessageGroup.MessageLoginInfo;

/**
 *
 * @author ChxxxXL
 */
public class DealerLoginInfo extends DealerBase {

    @Override
    public void serverDealer(MessageBase msg, DealerToolkits toolkits) {
        MessageLoginInfo msgLoginInfo = (MessageLoginInfo) msg;
        String ack = "false";   //服务器应答信息
        UserFactory userFactory = new UserFactory(toolkits.DataBaseStream.con);
        UserInfo user = userFactory.readUser(msgLoginInfo.fromName);
        UserPwd userPwd = userFactory.readUserPwd(msgLoginInfo.fromName);
        if (user == null) {
            ack = "不存在该用户";
        } else if (1 == user.state) {
            ack = "您已经登陆！";
        } else if (userPwd.userPwd.equals(msgLoginInfo.passwd)) {
            ack = "true";
            //修改用户信息，写入ip和端口数据
            user.state = 1;
            user.ip = toolkits.toIP.getHostAddress();
            user.port = toolkits.toPort;
            userFactory.writeUser(user);
            toolkits.jTextArea1.append(msgLoginInfo.fromName + "已登陆\n");
            toolkits.jTextArea1.setCaretPosition(toolkits.jTextArea1.getDocument().getLength());
        }
        MessageBase message = new MessageLoginInfo(ack);
        toolkits.serverSender.sendMessage(message, toolkits.toIP, toolkits.toPort);
    }

    @Override
    public void clientDealer(MessageBase msg, DealerToolkits toolkits) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
