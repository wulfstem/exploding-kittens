package model;

public class See3Card extends Card{


    public See3Card(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // see the top 3 cards of the drawPile
    }
}
