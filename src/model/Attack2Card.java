package model;

public class Attack2Card extends Card{


    public Attack2Card(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action() {
        // tap another player to take 2 turns and skip yours
    }

}
