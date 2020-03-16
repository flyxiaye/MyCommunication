/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import com.mysql.cj.protocol.MessageReader;
import MessageGroup.MessageNoraml;
import MessageGroup.MessageRecord;
import java.io.Serializable;

/**
 *
 * @author ChxxxXL
 */
public class ChatRecord implements Serializable {

    private static final long serialVersionUID = 16438942341264L;
    public String fromName;
    public String toName;
    public String chatData;
    public String date;

    public ChatRecord(String fromName, String toName, String data) {
        this.fromName = fromName;
        this.toName = toName;
        this.chatData = data;
    }

    public ChatRecord(MessageNoraml msg) {
        this(msg.fromName, msg.toName, msg.data);
    }
}
