package remote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameBoard extends Remote, Serializable {

    boolean putShip(int size, int xCoordinate, int yCoordinate) throws RemoteException;
    boolean shoot(int xCoordinate, int yCoordinate) throws RemoteException;
    void clean() throws RemoteException;
    void drawBoard(boolean forOpponent) throws RemoteException;
    boolean gameFinished() throws RemoteException;
}
