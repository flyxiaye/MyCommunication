/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import DataBase.UserFactory;
import DataBase.UserPwd;
import MessageGroup.MessageBase;
import MessageGroup.MessageSignUpInfo;

/**
 *
 * @author ChxxxXL
 */
public class DealerSignUpInfo extends DealerBase {

    @Override
    public void serverDealer(MessageBase msg, DealerToolkits toolkits) {
        MessageSignUpInfo msgSignUpInfo = (MessageSignUpInfo) msg;
        MessageSignUpInfo message1;
        UserFactory userFactory = new UserFactory(toolkits.DataBaseStream.con);
        if (userFactory.readUserPwd(msg.fromName) == null) {
            userFactory.writeUser(new UserPwd(msgSignUpInfo.fromName, msgSignUpInfo.passwd));
            message1 = new MessageSignUpInfo("true");
            toolkits.jTextArea1.append(msg.fromName + "已注册\n");
            toolkits.jTextArea1.setCaretPosition(toolkits.jTextArea1.getDocument().getLength());
        } else {
            message1 = new MessageSignUpInfo("false");
        }
        toolkits.serverSender.sendMessage(message1, toolkits.toIP, toolkits.toPort);
    }

    @Override
    public void clientDealer(MessageBase msg, DealerToolkits toolkits) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
