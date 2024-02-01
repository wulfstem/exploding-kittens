package exploding_kittens;

import exploding_kittens.model.Card;
import exploding_kittens.model.Game;
import exploding_kittens.model.Player;
import server.ClientHandler;
import server.ExplodingKittensServer;

import java.util.ArrayList;

public interface Controller {


    boolean validateByNope(Card card, Player player, Player otherPlayer);
    boolean validateMove(Card card, Player player);
    void bombDrawn(Player player, Card bomb);
    void showHand(Player player);
    void moveCanceled(Player player);
    int getCardForFavor(Player player);
    void showFuture(Player player);
    void informStolenCard(Player player, Card card);
    int getMatchingCard(Player player, Card card);
    void draw(Player player);
    int getOtherPlayerChoice(Player player);
    boolean isCardBeingPlayed();
    public int whichCardIsPlayed();
    void doTurn(Player player, int turns);
    void declareWinner(Player player);
    Player getCurrentPlayer();
    void addClientHandler(ClientHandler clientHandler);
    ArrayList<ClientHandler> getClientHandlers();
    void createGame(Game game);
    ExplodingKittensServer getServer();
    void getGameState(ClientHandler client);
    void announceDeath(Player player);
    void startGame();
}
