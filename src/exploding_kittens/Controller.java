package exploding_kittens;

import exploding_kittens.model.Card;
import exploding_kittens.model.Player;

public interface Controller {


    public void makeMove();

    public boolean validateByNope(Card card, Player player);

    public void dieOrDefuse(Card bomb);

    public int getCardChoice(Card.cardType type);

    public int getAnyCardChoice();

    public boolean validateMove(Card card, Player player);
}
