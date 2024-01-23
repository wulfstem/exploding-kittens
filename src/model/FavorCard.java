package model;

public class FavorCard extends Card{


    public FavorCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player thief) {
        System.out.println("Which player are we asking the card from?");
        int input1 = thief.readInputInt();
        Player victim = thief.getGame().getPlayers().get(input1);

        System.out.println("Player " + victim.getPlayerName() + " choose a card to give as a favor:");
        victim.printHand();
        int input2 = victim.readInputInt();
        Card temp = victim.getPlayerHand().getCardsInHand().get(input2);
        victim.getPlayerHand().getCardsInHand().remove(temp);
        thief.getPlayerHand().getCardsInHand().add(temp);

        //Manage data for NOPE card
        thief.getGame().getData2().setCardUser(thief);
        thief.getGame().getData2().setCardTarget(victim);
        thief.getGame().getData2().setCardPlayed(this);
        thief.getGame().getData2().setStolenCard(temp);
        thief.getGame().getData2().setComboPlayed(null, null);
        thief.getGame().getData2().setDrawPileBeforeTurn(thief.getGame().getDeck().getDrawPile());
    }
}
