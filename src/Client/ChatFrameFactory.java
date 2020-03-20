/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import MessageGroup.MessageBase;
import MessageGroup.MessageNoraml;
import MessageGroup.MessageRecord;
import Notifier.Observable;
import javax.swing.JFrame;

/**
 *
 * @author ChxxxXL
 */
public class ChatFrameFactory implements Observable {

    public ChatFrameFactory(ClientSendThread sender) {
        ChatJFrame.sender = sender;
    }

    @Override
    public void update(MessageBase msg) {
        if (msg instanceof MessageNoraml) {
            MessageNoraml msgNoraml = (MessageNoraml) msg;
            String fromName = msgNoraml.fromName;
            ChatJFrame cf = ChatJFrame.getChatFrame(fromName);
            cf.addMessage(msgNoraml);
            cf.setVisible(true);
        } else if (msg instanceof MessageRecord) {
            MessageRecord msgR = (MessageRecord) msg;
            new Thread(new ChatRecordJDialog(
                    new JFrame(), false, msgR.toName, msgR)).start();
        }
    }

}
