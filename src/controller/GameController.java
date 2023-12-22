package controller;

import model.Game;

public class GameController {


    private Game game;

    public GameController(int numberOfPlayers){

        game = new Game(numberOfPlayers);
    }
}
