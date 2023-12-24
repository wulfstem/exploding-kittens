package model;

public class ShuffleCard extends Card{


    public ShuffleCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        //shuffles the draw pile
        player.getGame().shuffle();
    }

    @Override
    public void undo(Player player) {
        player.getGame().getDeck().setDrawPile(player.getGame().getDrawPileBeforeShuffle());
    }
}
