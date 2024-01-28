package exploding_kittens.view;

import exploding_kittens.model.BackInputException;
import exploding_kittens.model.BooleanReturnException;
import exploding_kittens.model.Card;
import exploding_kittens.model.Player;

public interface PlayerTUI {

    public void showMessage(String text);
    public String readInputString() throws BackInputException;
    boolean askNope(Card card, Player player, Player otherPlayer);
    public int readInputInt();
    int getAnyCardChoice(Player player);
    int getCardChoice(Player player, Card.cardType type);
    public boolean readInputBoolean() throws BooleanReturnException;
    void printHand(Player player);
}
