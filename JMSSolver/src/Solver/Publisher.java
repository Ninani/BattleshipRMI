package Solver;

import javax.jms.TextMessage;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;

public class Publisher {

    TopicConnection topicConnection;
    TopicPublisher topicPublisher;
    TextMessage textMessage;

    public Publisher(TopicConnection topicConnection, TopicPublisher topicPublisher, TextMessage textMessage){
        this.topicConnection = topicConnection;
        this.topicPublisher = topicPublisher;
        this.textMessage = textMessage;
    }

    public TopicConnection getTopicConnection() {
        return topicConnection;
    }

    public TopicPublisher getTopicPublisher() {

        return topicPublisher;
    }

    public TextMessage getTextMessage() {
        return textMessage;
    }

}
