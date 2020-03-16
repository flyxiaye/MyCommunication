/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import Client.ChatRecordJDialog;
import DataBase.ChatRecordFactory;
import MessageGroup.MessageBase;
import MessageGroup.MessageRecord;
import java.util.Vector;
import javax.swing.JFrame;

/**
 *
 * @author ChxxxXL
 */
public class DealerRecord extends DealerBase {

    @Override
    public void serverDealer(MessageBase msg, DealerToolkits toolkits) {
        MessageRecord msgRecord = (MessageRecord) msg;
        ChatRecordFactory crFactory = new ChatRecordFactory(toolkits.DataBaseStream.con);
        Vector recordVector = crFactory.readChatRecords(
                msgRecord.fromName, msgRecord.toName);
        MessageRecord msgR = new MessageRecord(
                msgRecord.fromName, msgRecord.toName, recordVector);
        toolkits.serverSender.sendMessage(msgR, toolkits.toIP, toolkits.toPort);
    }

    @Override
    public void clientDealer(MessageBase msg, DealerToolkits toolkits) {
        MessageRecord msgR = (MessageRecord) msg;
        new Thread(new ChatRecordJDialog(
                new JFrame(), false, msgR.toName, msgR)).start();
    }

}
