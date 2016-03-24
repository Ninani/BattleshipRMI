import implementation.GameMode;
import implementation.RealPlayer;
import remote.GameService;

import java.rmi.Naming;
import java.util.Scanner;

public class Client {

    private static GameMode gameMode;
    private static String userNick;
    private static int roomNumber;

    public static void main(String[] args) {

        if (args.length != 2){
            System.out.println("type: java -cp ./classes Client <IP> <RMI Registry port>");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        String mode;

        do {
            System.out.println("Choose game mode:");
            System.out.println("single mode - type 's' \nmultiplayer - type 'm' ");
            mode = scanner.next();
        }while (!mode.equalsIgnoreCase("s") && !mode.equalsIgnoreCase("m"));

        if (mode.equalsIgnoreCase("m")) gameMode = GameMode.MULTIPLAYER;
        else gameMode = GameMode.SINGLEPLAYER;

        System.out.println("Type your nickname");
        userNick = scanner.next();

        roomNumber = 0;
        if (gameMode.equals(GameMode.MULTIPLAYER)){
            System.out.println("Type room number to join or 0 to create new one.");
            roomNumber = Integer.parseInt(scanner.next());
        }

        try {
            RealPlayer realPlayer = new RealPlayer(userNick);

            Object o = Naming.lookup(String.format("rmi://%s:%s/game", args[0], args[1]));
            System.out.println(o.getClass().getName());
            GameService gameService = (GameService) o;


            gameService.registerPlayer(realPlayer, gameMode, roomNumber);
            realPlayer.waitForFinish();

            gameService.unregisterPlayer(realPlayer);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
