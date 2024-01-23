package model;

public class See3Card extends Card{


    private final int VISION_OF_DRAW_PILE = 3;

    public See3Card(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // see the top 3 cards of the drawPile
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < VISION_OF_DRAW_PILE; i++){
            result.append(player.getGame().getDeck().getDrawPile().get(i).getCardName()).append(" (").append(player.getGame().getDeck().getDrawPile().get(i).getCardType()).append(") |");
        }
        System.out.println(result);

        //Manage data for NOPE card
        player.getGame().getData2().setCardUser(player);
        player.getGame().getData2().setCardTarget(null);
        player.getGame().getData2().setCardPlayed(this);
        player.getGame().getData2().setStolenCard(null);
        player.getGame().getData2().setComboPlayed(null, null);
        player.getGame().getData2().setDrawPileBeforeTurn(player.getGame().getDeck().getDrawPile());
    }
}
