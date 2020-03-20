/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageDealer;

import DataBase.ChatRecordFactory;
import DataBase.VisitDB;
import MessageGroup.MessageBase;
import MessageGroup.MessageRecord;
import Server.ServerSendThread;
import java.net.InetAddress;
import java.util.Vector;

/**
 *
 * @author ChxxxXL
 */
public class DealerRecord extends DealerBase {

    @Override
    public void dealer(MessageBase msg, InetAddress ip, int port) {
        MessageRecord msgRecord = (MessageRecord) msg;
        ChatRecordFactory crFactory = new ChatRecordFactory(VisitDB.getConnection());
        Vector recordVector = crFactory.readChatRecords(
                msgRecord.fromName, msgRecord.toName);
        MessageRecord msgR = new MessageRecord(
                msgRecord.fromName, msgRecord.toName, recordVector);
        ServerSendThread.getSendThread().sendMessage(msgR, ip, port);
    }

}
