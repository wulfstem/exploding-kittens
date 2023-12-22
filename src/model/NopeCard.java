package model;

public class NopeCard extends Card{


    public NopeCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action() {
        // deny the last card played except it's a bomb or a defuse
    }
}
