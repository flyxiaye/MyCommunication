/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exp;

import DataBase.ChatRecord;
import java.util.Vector;

/**
 *
 * @author ChxxxXL
 */
public class MessageRecord extends MessageBase {

    public int recordID = 0;
    public byte[] recordData;
    public String toName;
    public Vector vecRecord = new Vector();

    public MessageRecord() {
        super(MessageBase.RECORD_MESSAGE);
    }

    public MessageRecord(String fromName, String toName, Vector vec) {
        super(MessageBase.RECORD_MESSAGE, fromName);
        vecRecord = vec;
        this.toName = toName;
    }

    public void addRecord(ChatRecord cr) {
        vecRecord.addElement(cr);
    }

}
