package model;

public class DefuseCard extends Card{


    public DefuseCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action() {
        // defuse can be used after a bomb is drawn
    }
}
