package implementation;


import remote.GameBoard;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameBoardImpl implements GameBoard{

    private int[][] playerBoard;
    private int boardSize;
    private int toShoot = 0;

    public GameBoardImpl(int boardSize) throws RemoteException {
        this.boardSize = boardSize;
        playerBoard = new int[boardSize][boardSize];
    }

    @Override
    public boolean putShip(int shipSize, int xCoordinate, int yCoordinate) throws RemoteException {

        boolean toReturn = false;

            if (checkHorizontalAvailability(shipSize, xCoordinate, yCoordinate)){
                putHorizontally(shipSize, xCoordinate, yCoordinate);
                toReturn = true;
            }


        return toReturn;
    }

    private boolean checkHorizontalAvailability(int shipSize, int x, int y){

        if (y<0 || y>boardSize-shipSize) return false;

        for (int i=0; i<shipSize; i++){
            if (playerBoard[x][y+i] != 0) return false;
        }

        return true;

    }

    private boolean checkVerticalAvailability(int shipSize, int x, int y){

        if (y<0 || y>boardSize-shipSize) return false;

        for (int i=0; i<shipSize; i++){
            if (playerBoard[y+i][x] != 0) return false;
        }

        return true;

    }

    private void putHorizontally(int size, int x, int y){
        for (int i=y; i<y+size; i++){
            playerBoard[x][i] = 1;
        }
        toShoot += size;
    }

    private void putVertically(int size, int x, int y){

    }

    @Override
    public boolean shoot(int xCoordinate, int yCoordinate) throws RemoteException {

        if (playerBoard[xCoordinate][yCoordinate] == 1){
            playerBoard[xCoordinate][yCoordinate] = -1;
            toShoot--;
            return true;
        } else if (playerBoard[xCoordinate][yCoordinate] == -1){
            return false;
        } else {
            playerBoard[xCoordinate][yCoordinate] = -2;
            return false;
        }

    }

    @Override
    public void clean() throws RemoteException {

    }

    @Override
    public void drawBoard(boolean forOpponent) throws RemoteException {
        System.out.print("    ");
        for (int i=0; i< boardSize; i++){
            System.out.print(i+" ");
        }
        System.out.println("");
        System.out.print("    ");
        for (int i=0; i< boardSize; i++){
            System.out.print("- ");
        }
        System.out.println("");

        for (int i=0; i<boardSize; i++){
            System.out.print(i+" ");
            System.out.print("| ");
            for (int j=0; j<boardSize; j++){
                if (forOpponent) System.out.print(encodeCharForOpponent(playerBoard[i][j])+" ");
                else System.out.print(encodeChar(playerBoard[i][j])+" ");
            }
            System.out.println("|");
        }

        System.out.print("    ");
        for (int i=0; i< boardSize; i++){
            System.out.print("- ");
        }
        System.out.println("\n");
    }

    @Override
    public boolean gameFinished() throws RemoteException {
        if (toShoot > 0) return false;
        else return true;
    }

    private String encodeChar(int s){
        if (s == 1) return "*";
        else if (s == -1) return "@";
        else if (s == -2) return "-";
        else return " ";
    }

    private String encodeCharForOpponent(int s){
        if (s == -1) return "@";
        else if (s == -2) return "-";
        else return " ";
    }


}
