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
public class MessageSignUpInfo extends MessageBase {
    
    public String passwd;

    public MessageSignUpInfo(String fromName, String passwd) {
        super(fromName);
        this.passwd = passwd;
    }
    
     public MessageSignUpInfo(String fromName) {
        super(fromName);
    }
    
    
}
