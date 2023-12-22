package controller;

import model.Game;
import java.util.Random;

public class GameController {


    private Game game;

    public GameController(int numberOfPlayers) {
        game = new Game(numberOfPlayers);
        game.start();
    }
}