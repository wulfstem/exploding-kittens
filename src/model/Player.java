package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Player {


    private String playerName;
    private Game game;
    private Hand playerHand;


    public Player(String name, Game game){
        this.playerName = name;
        this.game = game;
        playerHand = new Hand(this, game.getDeck());
    }

    public void makeMove(){
        System.out.println("Do you want to play a card? ('y' or 'n')");
        String input = readInput();
    }

    public String readInput(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            String line = br.readLine();
            return line;
        } catch(IOException e){
            System.out.println("Error while reading input.");
            return null;
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public String printHand(){
        String result = "";
        for (int i = 0; i < getPlayerHand().getCardsInHand().size(); i++){
            result += getPlayerHand().getCardsInHand().get(i).getCardName() + " (" + getPlayerHand().getCardsInHand().get(i).getCardType() + ") | ";
        }
        return result;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
    }

    public Game getGame(){
        return game;
    }
}
