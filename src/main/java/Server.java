import implementation.GameServiceImpl;
import remote.GameService;

import java.net.InetAddress;
import java.rmi.Naming;
import java.net.UnknownHostException;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    private static GameService gameServiceImpl;
    private static int boardSize = 10;

    public static void main(String[] args) {

        if (args.length != 2){
            System.out.println("type: java -cp ./classes Server <IP> <RMI Registry port>");
            return;
        }

        try {
            String hostname = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Server IP " + hostname);
            System.setProperty("java.rmi.server.hostname", hostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            gameServiceImpl = new GameServiceImpl();
            GameService gameService = (GameService) UnicastRemoteObject.exportObject(gameServiceImpl, 0);

            Naming.rebind(String.format("rmi://%s:%s/game", args[0], args[1]), gameService);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
