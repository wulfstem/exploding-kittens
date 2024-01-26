package exploding_kittens;

import exploding_kittens.model.Card;
import exploding_kittens.model.Player;
import exploding_kittens.view.PlayerTUI;

public interface Controller {


    void drawCard(Player player);
    boolean validateByNope(Card card, Player player);
    boolean validateMove(Card card, Player player);
    int getCardChoice(Card.cardType type);
    void bombDrawn(Card bomb);

    int getAnyCardChoice();

    void doTurn(Player player);

    void declareWinner(Player player);

    void playOrDraw(Player player);

    Player getCurrentPlayer();
    PlayerTUI getTui();
}
