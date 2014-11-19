/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import web.websocket.qualifier.WSJMSMessage;

/**
 *
 * @author spierce
 */
@Stateless
@ServerEndpoint("/websocket")
public class WebSocketEndpoint {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    private ConnectionFactory cf;

    @Resource(lookup = "jms/msgQueue")
    private Queue msgQueue;

    @OnOpen
    public void onOpen(final Session session) {
        System.out.println("session connected");
        sessions.add(session);
    }

    @OnClose
    public void onClose(final Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable exception) {
        System.err.println("Error! " + exception);
    }

    @OnMessage
    public void onMessage(final String message, final Session client) {
        try (JMSContext context = cf.createContext()) {
            context.createProducer().send((Destination) msgQueue, message);
        }
    }

    public static void onJMSMessage(@Observes @WSJMSMessage Message msg) {
        try {
            for (Session session : sessions) {
                session.getBasicRemote().sendText("message from JMS: " + msg.getBody(String.class));
            }
        }
        catch (IOException | JMSException ex) {
            System.err.println("Error! " + ex);
        }
    }
}
