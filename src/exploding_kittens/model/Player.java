package exploding_kittens.model;

import exploding_kittens.Controller;

import java.util.ArrayList;

/**
 * Class representing a player in the Exploding Kittens game.
 * Each player has a name, a hand of cards, and can take actions during their turn.
 * Players can play or draw cards, and their actions affect the game's state.
 *
 * @author Ervinas Vilkaitis and Ugnius Tulaba
 */
public class Player {

    private String playerName;
    private Game game;
    private Hand playerHand;
    private boolean skipTurn;
    private int positionIndex;
    private final Controller controller;

    /**
     * Constructor for creating a player instance.
     *
     * @param name           The name of the player.
     * @param game           The game instance the player is part of.
     * @param positionIndex  The position index of the player in the game.
     * @param controller     The controller for handling player actions and game flow.
     */
    public Player(String name, Game game, int positionIndex, Controller controller) {
        this.playerName = name;
        this.game = game;
        this.positionIndex = positionIndex;
        this.controller = controller;
    }

    /**
     * Initiates the player's turn by allowing them to play or draw cards.
     * The player's actions are determined by the controller.
     */
    public void makeMove() {
        playOrDraw();
    }

    /**
     * Handles the decision for the player to either play a card or draw a card from the draw pile.
     * The player's actions are determined by the controller.
     */
    public void playOrDraw() {
        setSkipTurn(false);
        controller.showHand(this);
        if (controller.isCardBeingPlayed()) {
            int index = controller.whichCardIsPlayed();
            if (index == -1){
                playOrDraw();
                return;
            }
            if(controller.validateMove(getPlayerHand().getCardsInHand().get(index), this)){
                controller.informOfPlayerAction(this, getPlayerHand().getCardsInHand().get(index));
                getPlayerHand().getCardsInHand().get(index).action(this);
                getPlayerHand().remove(getPlayerHand().getCardsInHand().get(index));
                if (!isSkipTurn()) {
                    playOrDraw();
                }
            }
            else {
                controller.moveCanceled(this);
                getPlayerHand().remove(getPlayerHand().getCardsInHand().get(index));
                playOrDraw();
            }
        } else {
            if (!controller.getDeathThisTurn()){
                controller.draw(this);
            }
        }
    }

    /**
     * Removes the player from the game and announces their elimination.
     */
    public void die() {
        game.setTurns(1);
        if(getPositionIndex() == (game.getPlayers().size() - 1)){
            game.setCurrent(0);
        }
        ArrayList<Player> temp = getGame().getPlayers();
        temp.remove(this);
        getGame().setPlayers(temp);
        controller.announceDeath(this);
    }

    /**
     * Checks if the player's hand contains a card of a specific type.
     *
     * @param cardType The type of card to check for.
     * @return True if the player's hand contains a card of the specified type, false otherwise.
     */
    public boolean handContains(Card.cardType cardType) {
        for (int i = 0; i < getPlayerHand().getCardsInHand().size(); i++) {
            if (getPlayerHand().getCardsInHand().get(i).getCardType().equals(cardType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the player's name.
     *
     * @return The name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Retrieves the player's hand of cards.
     *
     * @return The player's hand.
     */
    public Hand getPlayerHand() {
        return playerHand;
    }

    /**
     * Sets the player's hand of cards.
     *
     * @param playerHand The new hand of cards for the player.
     */
    public void setPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
    }

    /**
     * Retrieves the game instance the player is part of.
     *
     * @return The game instance.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Checks if the player's turn should be skipped.
     *
     * @return True if the player's turn should be skipped, false otherwise.
     */
    public boolean isSkipTurn() {
        return skipTurn;
    }

    /**
     * Sets whether the player's turn should be skipped.
     *
     * @param skipTurn True to skip the player's turn, false otherwise.
     */
    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }

    /**
     * Retrieves the position index of the player in the game.
     *
     * @return The position index of the player.
     */
    public int getPositionIndex() {
        return positionIndex;
    }

    /**
     * Sets the position index of the player in the game.
     *
     * @param positionIndex The new position index for the player.
     */
    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    /**
     * Retrieves the controller responsible for handling player actions and game flow.
     *
     * @return The controller instance.
     */
    public Controller getController(){
        return controller;
    }
}
