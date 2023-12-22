package model;

public class FavorCard extends Card{


    public FavorCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {

    }
}
