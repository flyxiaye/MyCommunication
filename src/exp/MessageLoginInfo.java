/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exp;

/**
 *
 * @author ChxxxXL
 */
public class MessageLoginInfo extends MessageBase {
    
    public String passwd;

    public MessageLoginInfo(String fromName, String passwd) {
        super(MessageBase.LOGIN_MESSAGE, fromName);
        this.passwd = passwd;
    }
    
    public MessageLoginInfo(String fromName){
        super(MessageBase.LOGIN_MESSAGE, fromName);
    }
    
}
