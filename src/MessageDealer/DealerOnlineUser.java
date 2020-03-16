/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import Client.Info;
import MessageGroup.MessageBase;
import MessageGroup.MessageOnlineUser;
import javax.swing.DefaultListModel;

/**
 *
 * @author ChxxxXL
 */
public class DealerOnlineUser extends DealerBase {

    @Override
    public void serverDealer(MessageBase msg, DealerToolkits toolkits) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clientDealer(MessageBase msg, DealerToolkits toolkits) {
        MessageOnlineUser msgOnlineUser = (MessageOnlineUser) msg;
        DefaultListModel lm = (DefaultListModel) toolkits.jList1.getModel();
        lm.clear();
        for (String str : msgOnlineUser.users) {
            if (!str.equals(Info.userName)) {
                lm.addElement(str);
            }
        }
    }

}
