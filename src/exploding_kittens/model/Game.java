package exploding_kittens.model;

import exploding_kittens.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class creating the game environment, responsible for game logic, computations in-game and back-end processes.
 * @author Ervinas Vilkaitis and Ugnius Tulaba.
 */

public class Game {


    // Side attributes
    private int numberOfPlayers;
    private ArrayList<String> nicknames;
    private boolean computerPlayer;
    private int current;
    private int turns;
    private ArrayList<Player> players;
    private Deck deck;
    private int turnCounter;
    private Controller controller;
    private boolean isAttack;

    /**
     * Class constructor
     * @param numberOfPlayers the number of players playing a game, not including the computer player.
     */
    public Game(int numberOfPlayers, ArrayList<String> nicknames) {
        if (numberOfPlayers == 1) {
            this.numberOfPlayers = numberOfPlayers + 1;
            computerPlayer = true;
        } else {
            this.numberOfPlayers = numberOfPlayers;
            computerPlayer = false;
        }
        this.nicknames = nicknames;
        this.players = new ArrayList<>();
        turnCounter = 1;
        setAttack(false);
    }

    /**
     * Method creating an array <code>players</code> with instances of class <code>Player</code>
     * @param computerPlayer boolean set to true if computer is playing, set to false if not.
     * @param deck instance of Class <code>Deck</code>.
     *
     * @requires computerPlayer != null && deck != null;
     * @ensures players.length == getNumberOfPlayers();
     */
    public void createPlayers(boolean computerPlayer, Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null");
        }
        if (!computerPlayer) {
            for (int i = 0; i < getNumberOfPlayers(); i++) {
                String playerName = nicknames.get(i);
                players.add(new Player(playerName, this, i, controller));
            }
        } else {
            for (int i = 0; i < getNumberOfPlayers() - 1; i++) {
                String playerName = nicknames.get(i);
                players.add(new Player(playerName, this, i, controller));
            }
            players.add(new Computer(Computer.COMPUTER_NAME, this, (getNumberOfPlayers() - 1), controller));
        }
        updatePlayersPositions();
    }
    /**
     * method creates an instance of class <code>Deck</code>.
     *
     * @ensures number of Exploding Kittens in deck == numberOfPlayers - 1;
     */
    public void createDeck() {
        deck = new Deck(numberOfPlayers);
    }

    /**
     * method creates instances of class <code>Hand</code> and assigns every one of them to a different player.
     */
    public void createHands() {
        System.out.println("Number of players: " + players.size());
        for (Player p : players) {
            p.setPlayerHand(new Hand(p, deck));
        }
    }

    /**
     * method initiates other methods in this class, to prepare the game environment before it is started.
     */
    public void setup() {
        createDeck();
        createPlayers(isComputerPlayer(), getDeck());
        createHands();
        shuffle();
        setCurrent(selectRandomly(getPlayers().size()));
    }
    /**
     * Starts first turn by letting a randomly chosen player start and runs the whole game until there is a winner.
     */
    public void play(){
        turns = 1;
        turnCounter++;
        while(!hasWinner()){
            updatePlayersPositions();
            controller.doTurn(players.get(current), turns);
        }
        controller.declareWinner(getPlayers().get(0));
    }

    /**
     * method shuffles the cards in <code>drawPile</code>.
     */
    public void shuffle(){
        Collections.shuffle(getDeck().getDrawPile());
    }

    /**
     * method selects a random integer from given range starting with 0.
     * @param size the endpoint of the range.
     * @return integer randomly chosen number form the given range.
     */
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

    public void updatePlayersPositions(){
        for (int i = 0; i < getPlayers().size(); i++){
            getPlayers().get(i).setPositionIndex(i);
        }
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public int getTurnCounter(){
        return turnCounter;
    }

    public void setTurnCounter(int number){
        this.turnCounter = number;
    }

    public boolean isAttack() {
        return isAttack;
    }

    public void setAttack(boolean attack) {
        isAttack = attack;
    }
}