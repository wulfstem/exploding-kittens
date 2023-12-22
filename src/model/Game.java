package model;

import java.util.ArrayList;

/**
 * Deck has total of 56 cards. That includes:
 * (4) Exploding Kittens (depends on the number of players n - 1,
 * (6) Defuses (all the extra will be mixed in the deck),
 * At the beginning of the round each player receives 7 random cards + 1 Defuse,
 */

public class Game {


    private ArrayList<Player> players;
    private int numberOfPlayers;
    private boolean computerPlayer;

    private Deck deck;


    public Game(int numberOfPlayers){
        if (numberOfPlayers == 1){
            this.numberOfPlayers = numberOfPlayers + 1;
            computerPlayer = true;
        }
        else{
            this.numberOfPlayers = numberOfPlayers;
            computerPlayer = false;
        }
        this.players = new ArrayList<>();
        this.setup();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void createPlayers(){
        for (int i = 0; i < getNumberOfPlayers(); i++){
            // here get a name of a player one by one
            String playerName = "Default name";
            players.add(new Player(playerName));
        }
    }

    public void createDeck(){
        deck = new Deck(numberOfPlayers);
    }

    public void createHands(){
        for (Player p : getPlayers()){
            p.setPlayerHand(new Hand(p, deck));
        }
    }

    public void setup(){
        createPlayers();
        createDeck();
        createHands();
    }

    public void start(){
        // To be implemented
    }
}
