package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Deck has total of 56 cards. That includes:
 * (4) Exploding Kittens (depends on the number of players n - 1,
 * (6) Defuses (all the extra will be mixed in the deck),
 * At the beginning of the round each player receives 7 random cards + 1 Defuse,
 */

public class Game {


    // Side attributes
    private int numberOfPlayers;
    private boolean computerPlayer;
    private int current;
    private int turns;


    // Main attributes
    private ArrayList<Player> players;
    private Deck deck;


    public Game(int numberOfPlayers) {
        if (numberOfPlayers == 1) {
            this.numberOfPlayers = numberOfPlayers + 1;
            computerPlayer = true;
        } else {
            this.numberOfPlayers = numberOfPlayers;
            computerPlayer = false;
        }
        this.players = new ArrayList<>();
    }

    public void createPlayers(boolean computerPlayer, Deck deck) {
        if (!computerPlayer) {
            for (int i = 0; i < getNumberOfPlayers(); i++) {
                String playerName = "Default name";
                players.add(new Player(playerName, this));
            }
        } else {
            for (int i = 0; i < getNumberOfPlayers() - 1; i++) {
                String playerName = "Default name";
                players.add(new Player(playerName, this));
            }
            players.add(new Computer(Computer.COMPUTER_NAME, this));
        }
    }

    public void createDeck() {
        deck = new Deck(numberOfPlayers, this);
    }

    public void createHands() {
        for (Player p : getPlayers()) {
            p.setPlayerHand(new Hand(p, deck));
        }
    }

    public void setup() {
        createDeck();
        createPlayers(isComputerPlayer(), getDeck());
        createHands();
        shuffle(getDeck());
    }

    public void start(){
        setup();
        play();
    }

    public void play(){
        setTurns(1);
        setCurrent(selectRandomly(getPlayers().size()));

        while(!hasWinner()){
            System.out.println(getPlayers().get(getCurrent()).printHand());
            for (int i = 0; i < turns; i++){
                getPlayers().get(getCurrent()).makeMove();
            }
        }
    }

    public void shuffle(Deck deck){
        Collections.shuffle(deck.getDrawPile());
    }

    public int selectRandomly(int size){
        Random randomly = new Random();
        return randomly.nextInt(size);
    }

    public boolean hasWinner(){
        return getPlayers().size() == 1;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public boolean isComputerPlayer() {
        return computerPlayer;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current){
        this.current = current;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}