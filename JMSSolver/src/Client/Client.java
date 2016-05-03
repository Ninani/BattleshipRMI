package Client;

import Expression.ExpressionType;
import org.exolab.jms.administration.AdminConnectionFactory;
import org.exolab.jms.administration.JmsAdminServerIfc;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Properties;

public class Client {

    private static final String JNDI_CONTEXT_FACTORY_CLASS_NAME = "org.exolab.jms.jndi.InitialContextFactory";
    private static final String DEFAULT_JMS_PROVIDER_URL = "tcp://localhost:3035/";

    private HashMap<ExpressionType, String> topics = new HashMap<>();
    private HashMap<ExpressionType, TopicConnection> topicConnectionMap = new HashMap<>();

    private Context jndiContext = null;
    private ClientListener topicListener = null;

    public Client() throws NamingException, JMSException, MalformedURLException {
        addTopics();
        initializeJndiContext(DEFAULT_JMS_PROVIDER_URL);
    }

    private void initializeJndiContext(String providerUrl) throws NamingException {
        // JNDI Context
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_CONTEXT_FACTORY_CLASS_NAME);
        props.put(Context.PROVIDER_URL, providerUrl);
        jndiContext = new InitialContext(props);
        System.out.println("JNDI context initialized!");
    }

    private void addTopics() throws JMSException, MalformedURLException {

        JmsAdminServerIfc admin = AdminConnectionFactory.create(DEFAULT_JMS_PROVIDER_URL);

        topics.put(ExpressionType.ADDITION, "addTopic");
        topics.put(ExpressionType.SUBTRACTION, "subTopic");
        topics.put(ExpressionType.MULTIPLICATION, "mulTopic");
        topics.put(ExpressionType.DIVISION, "divTopic");
    }

    public void subscribe(ExpressionType expressionType) throws NamingException, JMSException {
        TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) jndiContext.lookup("JmsTopicConnectionFactory");
        Topic topic = (Topic) jndiContext.lookup(topics.get(expressionType));

        TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
        TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);

        topicListener = new ClientListener();
        topicSubscriber.setMessageListener(topicListener);
        topicConnectionMap.put(expressionType, topicConnection);
        topicConnection.start();
        System.out.println(expressionType.toString() +  " subscribed");

    }

    public void unsubscribe(ExpressionType expressionType){
        TopicConnection topicConnection = topicConnectionMap.get(expressionType);
        if (topicConnection != null){
            try {
                topicConnection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        topicConnectionMap.remove(expressionType, topicConnection);
    }

    public void stop(){

//        for (ExpressionType expressionType: topicConnectionMap.keySet()) {
//            unsubscribe(expressionType);
//        }

        if (jndiContext != null){
            try {
                jndiContext.close();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }

    }


}
