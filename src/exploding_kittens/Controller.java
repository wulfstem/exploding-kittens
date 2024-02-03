package exploding_kittens;

import exploding_kittens.model.Card;
import exploding_kittens.model.Game;
import exploding_kittens.model.Player;
import server.ClientHandler;
import server.ExplodingKittensServer;

import java.util.ArrayList;

/**
 * Interface defining the contract for controlling gameplay in the Exploding Kittens game.
 * Implementations of this interface handle various aspects of game logic and interactions.
 *
 * @author Ervinas Vilkaitis and Ugnius Tulaba
 */
public interface Controller {

    /**
     * Validates whether a "Nope" card can be played to cancel another player's move.
     *
     * @param card        The "Nope" card being played.
     * @param player      The player attempting to play the "Nope" card.
     * @param otherPlayer The player whose move is being canceled.
     * @return True if the "Nope" card can be played, false otherwise.
     */
    boolean validateByNope(Card card, Player player, Player otherPlayer);

    /**
     * Validates whether a card can be played by a player.
     *
     * @param card   The card being played.
     * @param player The player attempting to play the card.
     * @return True if the card can be played, false otherwise.
     */
    boolean validateMove(Card card, Player player);

    /**
     * Handles the scenario when an Exploding Kitten card is drawn by a player.
     *
     * @param player The player who drew the Exploding Kitten.
     * @param bomb   The Exploding Kitten card.
     */
    void bombDrawn(Player player, Card bomb);

    /**
     * Displays the player's hand and game information to the user interface.
     *
     * @param player The current player.
     */
    void showHand(Player player);

    /**
     * Notifies the player that their move has been canceled by another player.
     *
     * @param player The player whose move was canceled.
     */
    void moveCanceled(Player player);

    /**
     * Retrieves the card chosen by a player to give as a favor.
     *
     * @param player The player giving a card as a favor.
     * @return The index of the chosen card in the player's hand.
     */
    int getCardForFavor(Player player);

    /**
     * Displays information about the top cards in the draw pile (the future).
     *
     * @param player The current player.
     */
    void showFuture(Player player);

    /**
     * Notifies a player that a card has been stolen from them by another player.
     *
     * @param player The player whose card was stolen.
     * @param card   The stolen card.
     */
    void informStolenCard(Player player, Card card);

    /**
     * Retrieves the index of a matching card in a player's hand for a specific card.
     *
     * @param player The player making the choice.
     * @param card   The card to match.
     * @return The index of the matching card in the player's hand.
     */
    int getMatchingCard(Player player, Card card);

    /**
     * Handles the process of a player drawing a card from the draw pile.
     *
     * @param player The player drawing the card.
     */
    void draw(Player player);

    /**
     * Retrieves the index of another player chosen by the current player.
     *
     * @param player The current player making the choice.
     * @return The index of the chosen other player.
     */
    int getOtherPlayerChoice(Player player);

    /**
     * Checks if a card is being played by a player.
     *
     * @return True if a card is being played, false otherwise.
     */
    boolean isCardBeingPlayed();

    /**
     * Retrieves the index of the card being played by the current player.
     *
     * @return The index of the card being played.
     */
    int whichCardIsPlayed();

    /**
     * Executes a player's turn in the game.
     *
     * @param player The current player.
     * @param turns  The number of turns the player has in their current move.
     */
    void doTurn(Player player, int turns);

    /**
     * Declares the winner of the game.
     *
     * @param player The winning player.
     */
    void declareWinner(Player player);

    /**
     * Retrieves the current player in the game.
     *
     * @return The current player.
     */
    Player getCurrentPlayer();

    /**
     * Adds a client handler for network gameplay (not used in local gameplay).
     *
     * @param clientHandler The client handler to add.
     */
    void addClientHandler(ClientHandler clientHandler);

    /**
     * Retrieves a list of client handlers for network gameplay (not used in local gameplay).
     *
     * @return The list of client handlers.
     */
    ArrayList<ClientHandler> getClientHandlers();

    /**
     * Creates a game instance (not used in local gameplay).
     *
     * @param game The game instance to create.
     */
    void createGame(Game game);

    /**
     * Retrieves the server instance for network gameplay (not used in local gameplay).
     *
     * @return The server instance.
     */
    ExplodingKittensServer getServer();

    /**
     * Retrieves the game state and sends to the clientHandler. (not used in local gameplay).
     *
     * @param client The client handler to get the game state from.
     */
    void getGameState(ClientHandler client);

    /**
     * Returns boolean value which is set to true if the current player has disconnected, false otherwise. (not used in local gameplay).
     */
    boolean getDeathThisTurn();

    /**
     * Informs all other clients about the move that current player is making. (not used in local gameplay).
     *
     * @param player The player that is making the move.
     * @param card The card that is being played.
     */
    void informOfPlayerAction(Player player, Card card);

    /**
     * Announces the death of a player (not used in local gameplay).
     *
     * @param player The player who has been eliminated.
     */
    void announceDeath(Player player);

    /**
     * Starts the game (not used in local gameplay).
     */
    void startGame();
}
