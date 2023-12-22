package model;

import controller.GameController;

public class Computer extends Player{


    public static final String COMPUTER_NAME = "RoboTukas";

    public Computer(String playerName, Game game) {
        super(playerName, game);
    }
}
