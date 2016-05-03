package remote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Player extends Remote, Serializable {

    String getName() throws RemoteException;
    void onStartGame() throws RemoteException;
    void sendText(String text) throws RemoteException;
    GameBoard putShips(GameBoard playerBoard) throws RemoteException;
    void addOpponentBoard(GameBoard opponentBoard, String opponentNick) throws RemoteException;
    boolean shoot() throws RemoteException;
    void printGameboard(GameBoard gameBoard) throws RemoteException;
    GameBoard getOpponentBoard() throws RemoteException;

}
