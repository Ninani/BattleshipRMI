package implementation;

import remote.GameBoard;
import remote.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class ComputerPlayer extends UnicastRemoteObject implements Player {

    private GameBoard opponentBoard;
    private static String opponentNick;

    public ComputerPlayer() throws RemoteException {
    }

    @Override
    public String getName() throws RemoteException {
        return "computer";
    }

    @Override
    public void onStartGame() throws RemoteException {

    }

    @Override
    public void sendText(String text) throws RemoteException {

    }

    @Override
    public GameBoard putShips(GameBoard playerBoard) throws RemoteException {
        int shipsToPut = 4;
        int xCoord = 0, yCoord = 0;

        while (shipsToPut > 0){
            xCoord = randomCoordinate();
            yCoord = randomCoordinate();
            if(playerBoard.putShip(3, xCoord, yCoord)) shipsToPut--;
        }

        return playerBoard;

    }

    @Override
    public void addOpponentBoard(GameBoard opponentBoard, String opponentNick) throws RemoteException {
        this.opponentBoard = opponentBoard;
        this.opponentNick = opponentNick;

    }

    @Override
    public boolean shoot() throws RemoteException {
        int xCoord, yCoord;
        xCoord = randomCoordinate();
        yCoord = randomCoordinate();
        opponentBoard.shoot(xCoord, yCoord);

        if (opponentBoard.gameFinished()){
            return true;
        } else return false;

    }


    @Override
    public void printGameboard(GameBoard gameBoard) throws RemoteException {
    }

    @Override
    public GameBoard getOpponentBoard() throws RemoteException {
        return opponentBoard;
    }


    private int randomCoordinate(){
        Random random = new Random();
        return random.nextInt(10);
    }


}
