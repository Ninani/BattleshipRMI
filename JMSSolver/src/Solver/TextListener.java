package Solver;

import Expression.ExpressionType;

import javax.jms.*;
import java.util.HashMap;


public class TextListener implements MessageListener {

    private SolvedExpression solvedExpression = null;
    private HashMap<ExpressionType, Publisher> topicPublishers = null;

    public TextListener(HashMap<ExpressionType, Publisher> topicPublishers){
        this.topicPublishers = topicPublishers;

    }

    @Override
    public void onMessage(Message message) {

        TextMessage msg = null;
        Publisher publisher = null;
        TopicPublisher topicPublisher = null;
        TextMessage msgToSend = null;

        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                System.out.println("Reading message: " +
                        msg.getText());
                solveExpression(msg.getText());
                publisher = topicPublishers.get(solvedExpression.getExpressionType());
                topicPublisher = publisher.getTopicPublisher();
                msgToSend = publisher.getTextMessage();

                String toSend = String.format("%s = %d", solvedExpression.getExpression().toString(), solvedExpression.getResult());
                msgToSend.setText(toSend);
                System.out.println("publishing message: " + msgToSend.getText());
                topicPublisher.publish(msgToSend);

            } else {
                System.out.println("Message of wrong type: " +
                        message.getClass().getName());
            }
        } catch (JMSException e) {
            System.out.println("JMSException in onMessage(): " +
                    e.toString());
        } catch (Throwable t) {
            System.out.println("Exception in onMessage():" +
                    t.getMessage());
        }

    }

    private void solveExpression(String expression){

        char[] charExpression = expression.toCharArray();
        int result = 0;
        ExpressionType type = null;

        int number1 = Character.getNumericValue(charExpression[0]);
        char operation = charExpression[1];
        int number2 = Character.getNumericValue(charExpression[2]);

        switch (operation){
            case '+':
                result = number1 + number2;
                type = ExpressionType.ADDITION;
                break;
            case '-':
                result = number1 - number2;
                type = ExpressionType.SUBTRACTION;
                break;
            case '*':
                result = number1 * number2;
                type = ExpressionType.MULTIPLICATION;
                break;
            case '/':
                result = number1 / number2;
                type = ExpressionType.DIVISION;
                break;
        }

        solvedExpression = new SolvedExpression(expression, result, type);

    }


}
