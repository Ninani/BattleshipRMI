package implementation;

import remote.GameBoard;
import remote.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RealPlayer extends UnicastRemoteObject implements Player {

    private static String nickname;
    private static String opponentNick;
    private static boolean finished = false;
    private static Object synch;
    private GameBoard opponentBoard;
    Scanner scanner = new Scanner(System.in);

    public RealPlayer(String nickname) throws RemoteException {
        super();
        this.nickname = nickname;
        synch = new Object();
    }

    @Override
    public String getName() throws RemoteException {
        return nickname;
    }

    @Override
    public void onStartGame() throws RemoteException {
        System.out.println("Let's play!");
    }

    @Override
    public void sendText(String text) throws RemoteException {
        System.out.println(text);
    }

    @Override
    public GameBoard putShips(GameBoard playerBoard) throws RemoteException {
        int shipsToPut = 4;
        int xCoord = 0, yCoord = 0;
        System.out.println("Put 4 ships on board");

        while (shipsToPut > 0){
            System.out.print("y: ");
            xCoord = Integer.parseInt(scanner.next());
            System.out.print("x: ");
            yCoord = Integer.parseInt(scanner.next());
            if(playerBoard.putShip(3, xCoord, yCoord)) shipsToPut--;
            playerBoard.drawBoard(false);
        }
        return playerBoard;
    }

    @Override
    public void addOpponentBoard(GameBoard opponentBoard, String opponentNick) throws RemoteException {
        this.opponentBoard = opponentBoard;
        this.opponentNick = opponentNick;
    }

    @Override
    public boolean shoot() throws RemoteException{
        int xCoord, yCoord;
        System.out.println("Your turn");
        System.out.print("y: ");
        xCoord = Integer.parseInt(scanner.next());
        System.out.print("x: ");
        yCoord = Integer.parseInt(scanner.next());
        opponentBoard.shoot(xCoord, yCoord);
        printOpponentGameboard();

        if (opponentBoard.gameFinished()){
            System.out.println("You won!!!");
            return true;
        } else return false;
    }

    @Override
    public void printGameboard(GameBoard gameBoard) throws RemoteException {
        System.out.println("\nYOU");
        gameBoard.drawBoard(false);
    }

    @Override
    public GameBoard getOpponentBoard() throws RemoteException {
        return opponentBoard;
    }

    private void printOpponentGameboard() throws RemoteException {
        System.out.println("\n"+opponentNick.toUpperCase());
        opponentBoard.drawBoard(true);
    }

    public void waitForFinish() throws InterruptedException {
        synchronized (synch){
            while (!finished){
                synch.wait();
            }
        }

    }


}
