package implementation;

import remote.GameBoard;
import remote.GameController;
import remote.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class GameControllerImpl implements GameController {

    private static Player player1;
    private static Player player2;
//    private static List<Player> players = new LinkedList<>();
    private static boolean finished;
    private GameBoard player1Game = new GameBoardImpl(10);
    private GameBoard player2Game = new GameBoardImpl(10);

    public GameControllerImpl(Player player1, Player player2) throws RemoteException {
        this.player1 = player1;
        this.player2 = player2;
        finished = false;
    }

    @Override
    public void run() {

        try {
            player1Game = player1.putShips(player1Game);
            player2Game = player2.putShips(player2Game);
            player1.addOpponentBoard(player2Game, player2.getName());
            player2.addOpponentBoard(player1Game, player1.getName());

//            players.add(player1);
//            players.add(player2);

            player1.onStartGame();
            player2.onStartGame();

            while (!finished){

                player2.sendText(player1.getName().toUpperCase()+" is shooting...");
                finished = player1.shoot();
                player2.printGameboard(player1.getOpponentBoard());
                player1.sendText(player2.getName().toUpperCase()+" is shooting...");
                finished = player2.shoot();
                player1.printGameboard(player2.getOpponentBoard());

            }
            // TODO: 23.03.16 finishing game - unregister client



        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
