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
    private String[] nicknames;
    private boolean computerPlayer;
    private int current;
    private int turns;
    private boolean skipTurn;
    private ArrayList<Player> players;
    private Deck deck;
    private Controller controller;

    /**
     * Class constructor
     * @param numberOfPlayers the number of players playing a game, not including the computer player.
     */
    public Game(int numberOfPlayers, String[] nicknames) {
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
                String playerName = nicknames[i];
                players.add(new Player(playerName, this, i));
            }
        } else {
            for (int i = 0; i < getNumberOfPlayers() - 1; i++) {
                String playerName = nicknames[i];
                players.add(new Player(playerName, this, i));
            }
            players.add(new Computer(Computer.COMPUTER_NAME, this, (getNumberOfPlayers() - 1)));
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
        int moveCounter = 1;
        setTurns(1);
        setCurrent(selectRandomly(getPlayers().size()));

        while(!hasWinner()){
            updatePlayersPositions();
            getPlayers().get(getCurrent()).getTui().showMessage("\nMOVE NUMBER: " + moveCounter + "\n" + "Cards left in pile: " + getDeck().getDrawPile().size() + "\nBombs left: " + getDeck().getNumberOfActiveBombs());
            getPlayers().get(getCurrent()).getTui().showMessage("(In some occasions you can go back on your decision typing in 'b', when asked for input.\n");
            if (!(getPlayers().get(getCurrent()) instanceof Computer)){
                getPlayers().get(getCurrent()).getTui().printHand();
            }
            for (int i = 0; i < turns; i++){
                controller.makeMove();
                moveCounter++;
            }
            getPlayers().get(getCurrent()).getTui().showMessage("------------------------------------------------------------------------------\n");
        }
        getPlayers().get(getCurrent()).getTui().showMessage("The winner is:\n" + getPlayers().get(0).getPlayerName() + "!");
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

    public boolean isSkipTurn(){
        return skipTurn;
    }

    public void setSkipTurn(boolean skipTurn){
        this.skipTurn = skipTurn;
    }

    public void updatePlayersPositions(){
        for (int i = 0; i < getPlayers().size(); i++){
            getPlayers().get(i).setPositionIndex(i);
        }
    }

    public void setController(Controller controller){
        this.controller = controller;
    }
}