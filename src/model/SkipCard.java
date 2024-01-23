package model;

public class SkipCard extends Card{


    public SkipCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // skip your turn and go to the next player
        player.setSkipTurn(true);

        //Manage data for NOPE card
        player.getGame().getData2().setCardUser(player);
        player.getGame().getData2().setCardTarget(null);
        player.getGame().getData2().setCardPlayed(this);
        player.getGame().getData2().setStolenCard(null);
        player.getGame().getData2().setComboPlayed(null, null);
        player.getGame().getData2().setDrawPileBeforeTurn(player.getGame().getDeck().getDrawPile());
    }
}
