package controller;

import model.Game;

/**
 * A class creating a game, and interacting with a player. Waiting for input, etc.
 * The overall controller of the game.
 * @author Ervinas Vilkaitis and Ugnius Tulaba.
 */
public class GameController {


    private Game game;

    /**
     * Method creates a <code>Game</code> class instance and starts it.
     * @param numberOfPlayers the number of players playing the game (exceeding computer player)
     */
    public GameController(int numberOfPlayers) {
        // For example if only one player is playing against a computer, this parameter numberOfPlayers should be set to 1.
        game = new Game(numberOfPlayers);
        game.start();
    }
}