/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageGroup;

/**
 *
 * @author ChxxxXL
 */
public class MessageNoraml extends MessageBase {
    
    public String data;
    public String toName;

    public MessageNoraml(String fromName, String toName, String data) {
        super(fromName);
        this.toName = toName;
        this.data = data;
    }
    
}
