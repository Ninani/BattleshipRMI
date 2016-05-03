package remote;

import exceptions.UserAlreadyRegisteredException;
import implementation.GameMode;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameService extends Remote{

    void registerPlayer(Player player, GameMode gameMode, int roomNumber) throws RemoteException, UserAlreadyRegisteredException;
    void unregisterPlayer(Player player) throws RemoteException;
}
