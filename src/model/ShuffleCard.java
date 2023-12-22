package model;

public class ShuffleCard extends Card{


    public ShuffleCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        //shuffles the draw pile
    }
}
