package model;

public class SkipCard extends Card{


    public SkipCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action() {
        // skip your turn and go to the next player
    }
}
