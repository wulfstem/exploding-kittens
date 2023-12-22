package model;

public class RegularCard extends Card{


    public RegularCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // can be used as a steal when using two matching regular cards
    }
}
