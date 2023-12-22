package main;

import controller.GameController;

import java.util.ArrayList;

public class Main {


    private static GameController gameController;

    public static void main (String[] args){
        System.out.println( "Welcome to Exploding Kittens!" );
        //figure out how many players are going to be playing the game.
        int numberOfPlayers = 1;
        gameController = new GameController(numberOfPlayers);
    }
}
