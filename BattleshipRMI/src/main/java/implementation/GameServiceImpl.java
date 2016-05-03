package implementation;

import exceptions.UserAlreadyRegisteredException;
import remote.GameController;
import remote.GameService;
import remote.Player;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;

// TODO: 22.03.16 remove list 'registeredPlayers'

public class GameServiceImpl implements GameService {

    private static final int THREADS_NO = 10;
    private final ExecutorService executor= Executors.newFixedThreadPool(THREADS_NO);
    private List<String> registeredPlayers = new LinkedList<>();
    private Map<Integer, Player> waitingForPair = new ConcurrentHashMap<>();


    @Override
    public void registerPlayer(Player player, GameMode gameMode, int roomNumber) throws RemoteException, UserAlreadyRegisteredException {

        if (!registeredPlayers.contains(player.getName())){
            registeredPlayers.add(player.getName());
        } else throw new UserAlreadyRegisteredException();

        if (gameMode.equals(GameMode.SINGLEPLAYER)){
            initializeSingle(player);
        } else {
            initializeMultiplayer(player, roomNumber);
        }

    }

    private void initializeSingle(Player player) throws RemoteException {
        Player computerOpponent = new ComputerPlayer();
        GameController gameController = new GameControllerImpl(player, computerOpponent);
        executor.execute(gameController);
    }

    private void initializeMultiplayer(Player player, int roomNumber) throws RemoteException {
        Player opponent;

        if (waitingForPair.containsKey(roomNumber)) {
            opponent = waitingForPair.get(roomNumber);
            waitingForPair.remove(roomNumber);
            GameController gameController = new GameControllerImpl(player, opponent);
            executor.execute(gameController);
        } else {
            int number = randomRoom();
            waitingForPair.put(number, player);
            player.sendText("You are waiting in the room " + number);
        }

    }

    private Integer randomRoom(){
        Random random = new Random();
        Integer roomNumber;
        do {
            roomNumber =  random.nextInt(THREADS_NO) + 1;
        } while (waitingForPair.containsKey(roomNumber));
        return roomNumber;
    }



    @Override
    public void unregisterPlayer(Player player) throws RemoteException {

        if (registeredPlayers.contains(player.getName())){
            registeredPlayers.remove(player.getName());
        }
    }

}
