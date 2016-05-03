package Solver;

import Expression.ExpressionType;
import org.exolab.jms.administration.AdminConnectionFactory;
import org.exolab.jms.administration.JmsAdminServerIfc;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Properties;


public class Solver{

    private static final String JNDI_CONTEXT_FACTORY_CLASS_NAME = "org.exolab.jms.jndi.InitialContextFactory";
    private static final String DEFAULT_JMS_PROVIDER_URL = "tcp://localhost:3035/";
    private static final String DEFAULT_INCOMING_MESSAGES_QUEUE_NAME = "queue1";


    // Application JNDI context
    private Context jndiContext;

    // JMS Administrative objects references
    private QueueConnectionFactory queueConnectionFactory;
    private Queue incomingMessagesQueue;

    // JMS Client objects
    private QueueConnection connection;
    private QueueSession session;
    private QueueReceiver receiver;

    // Business logic objects
    private TextListener listener;

    private HashMap<ExpressionType, String> topics = new HashMap<>();
    private HashMap<ExpressionType, Publisher> topicPublishers = new HashMap<>();


    /************** Initialization BEGIN ******************************/
    public Solver() throws NamingException, JMSException, MalformedURLException {
        this(DEFAULT_JMS_PROVIDER_URL, DEFAULT_INCOMING_MESSAGES_QUEUE_NAME);
    }

    public Solver(String providerUrl, String incomingMessagesQueueName) throws NamingException, JMSException, MalformedURLException {
        initializeJndiContext(providerUrl);
        initializeTopics();
        initializeAdministrativeObjects(incomingMessagesQueueName);
        initializeJmsClientObjects();
    }

    private void initializeJndiContext(String providerUrl) throws NamingException {
        // JNDI Context
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_CONTEXT_FACTORY_CLASS_NAME);
        props.put(Context.PROVIDER_URL, providerUrl);
        jndiContext = new InitialContext(props);
        System.out.println("JNDI context initialized!");
    }

    private void initializeAdministrativeObjects(String incomingMessagesQueueName) throws NamingException {
        // ConnectionFactory
        queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("ConnectionFactory");
        // Destination
        incomingMessagesQueue = (Queue) jndiContext.lookup(incomingMessagesQueueName);
        System.out.println("JMS administrative objects (ConnectionFactory, Destinations) initialized!");
    }

    private void initializeJmsClientObjects() throws JMSException {
        connection = queueConnectionFactory.createQueueConnection();
        session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE); // false - non-transactional, AUTO_ACKNOWLEDGE - messages acknowledged after receive() method returns
        receiver = session.createReceiver(incomingMessagesQueue);
        listener = new TextListener(topicPublishers);
        System.out.println("JMS client objects (Session, MessageConsumer) initialized!");
    }

    private void initializeTopics() throws NamingException, JMSException, MalformedURLException {

        addTopics();

        TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) jndiContext.lookup("JmsTopicConnectionFactory");

        for (ExpressionType expressionType: topics.keySet()) {
            Topic topic = (Topic) jndiContext.lookup(topics.get(expressionType));

            //topic connection
            TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
            TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            TopicPublisher topicPublisher = topicSession.createPublisher(topic);
            TextMessage message = topicSession.createTextMessage();

            Publisher publisher = new Publisher(topicConnection, topicPublisher, message);
            topicPublishers.put(expressionType, publisher);
        }
    }

    private void addTopics() throws JMSException, MalformedURLException {

        JmsAdminServerIfc admin = AdminConnectionFactory.create(DEFAULT_JMS_PROVIDER_URL);

        putTopics();

//        for (String topic: topics.values()) {
//            Boolean isQueue = Boolean.FALSE;
//            if (!admin.addDestination(topic, isQueue)) {
//                System.err.println("Failed to create topic " + topic);
//            }
//        }

    }

    private void putTopics(){
        topics.put(ExpressionType.ADDITION, "addTopic");
        topics.put(ExpressionType.SUBTRACTION, "subTopic");
        topics.put(ExpressionType.MULTIPLICATION, "mulTopic");
        topics.put(ExpressionType.DIVISION, "divTopic");
    }



    /************** Initialization END ******************************/




    /************** Business logic BEGIN ****************************/
    public void start() throws JMSException, IOException {
        connection.start();
        System.out.println("Connection started - receiving messages possible!");

        // Receive messages synchronously
        while (true) {
            Message m = receiver.receive(1);
            if (m!=null){
                listener.onMessage(m);
            }


        }
    }

    public void stop() {
        // close the connection
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException exception) {
                exception.printStackTrace();
            }
        }

        // close the context
        if (jndiContext != null) {
            try {
                jndiContext.close();
            } catch (NamingException exception) {
                exception.printStackTrace();
            }
        }

        for (Publisher publisher: topicPublishers.values()) {
            TopicConnection topicConnection = publisher.getTopicConnection();
            if (topicConnection != null){
                try {
                    topicConnection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    /************** Business logic END ****************************/
}
