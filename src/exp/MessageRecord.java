/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exp;

import java.util.Vector;

/**
 *
 * @author ChxxxXL
 */
public class MessageRecord extends MessageExp {

    public int recordID = 0;
    public byte[] recordData;
    public Vector<String[]> vecRecord = new Vector();

    public MessageRecord(int id) {
        super(id);
    }

    public MessageRecord(int id, String[] obj) {
        super(id);
        vecRecord.addElement(obj);
    }

    public MessageRecord(int id, String date, String fromName, String data) {
        super(id);
        vecRecord.addElement(new String[]{date, fromName, data});
    }

    public void addRecord(String date, String fromName, String data) {
        vecRecord.addElement(new String[]{date, fromName, data});
    }

    public void addRecord(String[] obj) {
        vecRecord.addElement(obj);
    }

}
