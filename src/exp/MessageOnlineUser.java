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
public class MessageOnlineUser extends MessageBase {
    public Vector<String> users;
    public MessageOnlineUser(Vector users) {
        super(MessageBase.USER_MESSAGE);
        this.users = users;
    }
    
}
