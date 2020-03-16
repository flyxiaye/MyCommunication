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
public class MessageSignUpInfo extends MessageBase {
    
    public String passwd;

    public MessageSignUpInfo(String fromName, String passwd) {
        super(MessageBase.SINGUP_MESSAGE, fromName);
        this.passwd = passwd;
    }
    
     public MessageSignUpInfo(String fromName) {
        super(MessageBase.SINGUP_MESSAGE, fromName);
    }
    
    
}
