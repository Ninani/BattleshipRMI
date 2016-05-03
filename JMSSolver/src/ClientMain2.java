import Client.Client;
import Expression.ExpressionType;

public class ClientMain2 {

    public static void main(String[] args) {
        Client client = null;
        try {
            client = new Client();
            client.subscribe(ExpressionType.ADDITION);
            client.subscribe(ExpressionType.MULTIPLICATION);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.stop();
            }
        }

    }
}
