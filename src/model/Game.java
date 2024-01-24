package model;

import java.lang.reflect.Array;
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
    private boolean computerPlayer;
    private int current;
    private int turns;
    private ArrayList<Player> players;
    private Deck deck;

    /**
     * Class constructor
     * @param numberOfPlayers the number of players playing a game, not including the computer player.
     */
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
                String playerName = "Default name";
                players.add(new Player(playerName, this, i));
            }
        } else {
            for (int i = 0; i < getNumberOfPlayers() - 1; i++) {
                String playerName = "Default name";
                players.add(new Player(playerName, this, i));
            }
            players.add(new Computer(Computer.COMPUTER_NAME, this, (getNumberOfPlayers() - 1)));
        }
    }
    /**
     * method creates an instance of class <code>Deck</code>.
     *
     * @ensures number of Exploding Kittens in deck == numberOfPlayers - 1;
     */
    public void createDeck() {
        deck = new Deck(numberOfPlayers, this);
    }

    /**
     * method creates instances of class <code>Hand</code> and assigns every one of them to a different player.
     */
    public void createHands() {
        for (Player p : getPlayers()) {
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
    }

    /**
     * starts the game by first calling <code>setup()</code> to prepare the game environment.
     */
    public void start(){
        setup();
        play();
    }

    /**
     * Starts first turn by letting a randomly chosen player start and runs the whole game until there is a winner.
     */
    public void play(){
        setTurns(1);
        setCurrent(selectRandomly(getPlayers().size()));

        while(!hasWinner()){
            updatePlayersPositions();
            System.out.println(getPlayers().get(getCurrent()).printHand());
            for (int i = 0; i < turns; i++){
                getPlayers().get(getCurrent()).makeMove();
            }
        }
        System.out.println("The winner is:\n" + getPlayers().get(0).getPlayerName() + "!");
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

    public boolean validateMove(Card card, Player player){
        boolean result = true;
        for (int i = 0; i < getPlayers().size(); i++){
            if (i != getCurrent()){
                for (Card cardInHand : getPlayers().get(i).getPlayerHand().getCardsInHand()) {
                    if (cardInHand.getCardType().equals(Card.CARD_TYPE.NOPE)) {
                        if(getPlayers().get(i).askNope(card, player)){
                            result = false;
                        }
                    }
                }
            }
        }
        return result;
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
}