package exploding_kittens;

import exploding_kittens.model.Card;
import exploding_kittens.model.Player;
import exploding_kittens.view.PlayerTUI;

public interface Controller {


    boolean validateByNope(Card card, Player player);
    boolean validateMove(Card card, Player player);
    void bombDrawn(Player player, Card bomb);
    void showHand(Player player);
    void moveCanceled(Player player);
    void draw(Player player);
    boolean isCardBeingPlayed();
    public int whichCardIsPlayed();
    void doTurn(Player player);
    void declareWinner(Player player);
    Player getCurrentPlayer();
    PlayerTUI getTui();
}
