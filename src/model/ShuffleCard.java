package model;

public class ShuffleCard extends Card{


    public ShuffleCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {

        //Manage data for NOPE card
        player.getGame().getData2().setCardUser(player);
        player.getGame().getData2().setCardTarget(null);
        player.getGame().getData2().setCardPlayed(this);
        player.getGame().getData2().setStolenCard(null);
        player.getGame().getData2().setComboPlayed(null, null);
        player.getGame().getData2().setDrawPileBeforeTurn(player.getGame().getDeck().getDrawPile());

        //shuffles the draw pile
        player.getGame().shuffle();
    }
}
