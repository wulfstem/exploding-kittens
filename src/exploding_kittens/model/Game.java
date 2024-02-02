package exploding_kittens.model;

import exploding_kittens.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class creating the game environment, responsible for game logic, computations in-game and back-end processes.
 * This class represents the core of the Exploding Kittens game.
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
     * Class constructor.
     *
     * @param numberOfPlayers the number of players playing a game, including the computer player.
     * @param nicknames an ArrayList containing names that will be given to each created player accordingly.
     * @param computerPlayer is true if one of the players is a computer player, is false if game should have no computer players.
     */
    public Game(int numberOfPlayers, ArrayList<String> nicknames, boolean computerPlayer) {
        this.numberOfPlayers = numberOfPlayers;
        this.computerPlayer = computerPlayer;
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
        turns = 1;
    }

    /**
     * Starts first turn by letting a randomly chosen player start and runs the whole game until there is a winner.
     */
    public void play(){
        turns = 1;
        turnCounter++;
        while(!hasWinner()){
            updatePlayersPositions();
            reCheckBombs();
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

    /**
     * Checks if there is a winner in the game.
     *
     * @return true if there is only one player remaining, false otherwise.
     */
    public boolean hasWinner(){
        return getPlayers().size() == 1;
    }

    /**
     * Returns the list of player instances.
     *
     * @return ArrayList containing player instances.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the number of players in the game.
     *
     * @return the number of players.
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Checks if a computer player is present in the game.
     *
     * @return true if a computer player is present, false otherwise.
     */
    public boolean isComputerPlayer() {
        return computerPlayer;
    }

    /**
     * Returns the game's deck instance.
     *
     * @return the deck instance.
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Returns the current player's index.
     *
     * @return the current player's index.
     */
    public int getCurrent() {
        return current;
    }

    /**
     * Sets the current player's index.
     *
     * @param current the current player's index to set.
     */
    public void setCurrent(int current){
        this.current = current;
    }

    /**
     * Returns the current turn number.
     *
     * @return the current turn number.
     */
    public int getTurns() {
        return turns;
    }

    /**
     * Sets the current turn number.
     *
     * @param turns the current turn number to set.
     */
    public void setTurns(int turns) {
        this.turns = turns;
    }

    /**
     * Sets the list of player instances.
     *
     * @param players ArrayList containing player instances.
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Updates the positions of players in the game.
     */
    public void updatePlayersPositions(){
        for (int i = 0; i < getPlayers().size(); i++){
            getPlayers().get(i).setPositionIndex(i);
        }
    }

    /**
     * Sets the controller for the game.
     *
     * @param controller the controller to set.
     */
    public void setController(Controller controller){
        this.controller = controller;
    }

    /**
     * Returns the current turn counter.
     *
     * @return the current turn counter.
     */
    public int getTurnCounter(){
        return turnCounter;
    }

    /**
     * Sets the current turn counter.
     *
     * @param number the current turn counter to set.
     */
    public void setTurnCounter(int number){
        this.turnCounter = number;
    }

    /**
     * Checks if an attack is currently ongoing.
     *
     * @return true if there is an attack in progress, false otherwise.
     */
    public boolean isAttack() {
        return isAttack;
    }

    /**
     * Sets the attack status.
     *
     * @param attack true to indicate an ongoing attack, false otherwise.
     */
    public void setAttack(boolean attack) {
        isAttack = attack;
    }

    public void reCheckBombs(){
        if (numberOfPlayers != deck.getNumberOfActiveBombs() - 1){
            for (Card card: deck.getDrawPile()){
                if (card.getCardType().equals(Card.cardType.BOMB)){
                    deck.getDrawPile().remove(card);
                    return;
                }
            }
        }
    }
}