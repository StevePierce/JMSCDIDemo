/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.websocket;

import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Message;
import javax.jms.MessageListener;
import web.websocket.qualifier.WSJMSMessage;

/**
 *
 * @author spierce
 */
@Named
@MessageDriven(mappedName = "jms/msgQueue")
public class WebSocketMDB implements MessageListener {
    
    @Inject
    @WSJMSMessage
    Event<Message> jmsEvent;
    
    @Override
    public void onMessage(Message msg) {
        jmsEvent.fire(msg);
    }
    
}
