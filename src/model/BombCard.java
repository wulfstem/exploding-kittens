package model;

public class BombCard extends Card{


    public BombCard(CARD_TYPE cardType, String cardName, Deck deck) {

        super(cardType, cardName, deck);
    }

    @Override
    public void action() {
        // either defused when drawn or kills the player
    }
}
