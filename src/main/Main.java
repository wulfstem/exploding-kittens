package main;

import controller.GameController;

/**
 * Main class for Exploding Kittens initialization.
 * Programming Project BIT MOD2.
 * @author Ervinas Vilkaitis and Ugnius Tulaba.
 */
public class Main {


    private static GameController gameController;

    /**
     * Main thread <code>main</code> starts the system.
     * @param args is an array list of the initial system arguments.
     */
    public static void main (String[] args){
        System.out.println( "Welcome to Exploding Kittens!" );
        // TEMPORARILY numberOfPlayers is assigned to 1
        // the aim is to figure out the number of players for the game that is being started
        int numberOfPlayers = 1;
        gameController = new GameController(numberOfPlayers);
    }
}
