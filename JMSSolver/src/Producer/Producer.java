package Producer;


import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class Producer {

    private static final String JNDI_CONTEXT_FACTORY_CLASS_NAME = "org.exolab.jms.jndi.InitialContextFactory";
    private static final String DEFAULT_JMS_PROVIDER_URL = "tcp://localhost:3035/";
    private static final String DEFAULT_OUTGOING_MESSAGES_QUEUE_NAME = "queue1";


    // Application JNDI context
    private Context jndiContext;

    // JMS Administrative objects
    private QueueConnectionFactory queueConnectionFactory;
    private Queue outgoingMessagesQueue;

    // JMS Client objects
    private QueueConnection connection;
    private QueueSession session;
    private QueueSender sender;

    // Business Logic
    private String clientName;

    private TextMessage message = null;

    private ExpressionGenerator expressionGenerator;

    public Producer() throws NamingException, JMSException {
        this("Client " + new Random().nextInt());
    }

    public Producer(String clientName) throws NamingException, JMSException {
        this(clientName, DEFAULT_JMS_PROVIDER_URL, DEFAULT_OUTGOING_MESSAGES_QUEUE_NAME);
    }

    public Producer(String clientName, String providerUrl, String outgoingMessagesQueueName) throws NamingException, JMSException {
        this.clientName = clientName;
        initializeJndiContext(providerUrl);
        initializeAdministrativeObjects(outgoingMessagesQueueName);
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

    private void initializeAdministrativeObjects(String outgoingMessagesQueueName) throws NamingException {
        // ConnectionFactory
        queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("ConnectionFactory");
        // Destination
        outgoingMessagesQueue = (Queue) jndiContext.lookup(outgoingMessagesQueueName);
        System.out.println("JMS administrative objects (ConnectionFactory, Destinations) initialized!");
    }

    private void initializeJmsClientObjects() throws JMSException {
        connection = queueConnectionFactory.createQueueConnection();
        session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        sender = session.createSender(outgoingMessagesQueue);
        expressionGenerator = new ExpressionGenerator();
        System.out.println("JMS client objects (Session, MessageConsumer) initialized!");
    }

    public void start() throws JMSException, IOException {
        connection.start();

        System.out.println("Producer initialized");
        while (true) {

            // PUT YOUR CODE HERE
            message = session.createTextMessage();
            String expression = expressionGenerator.getExpression();
            message.setText(expression);
            System.out.println("Sending: " + expression);
            sender.send(message);

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }
    }

    public void stop() {
        // close the context
        if (jndiContext != null) {
            try {
                jndiContext.close();
            } catch (NamingException exception) {
                exception.printStackTrace();
            }
        }

        // close the connection
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException exception) {
                exception.printStackTrace();
            }
        }
    }

}
