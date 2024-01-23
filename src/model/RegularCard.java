package model;

public class RegularCard extends Card{


    public RegularCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player thief) {
        // can be used as a steal when using two matching regular cards
        System.out.println("Choose a duplicate card in your hand:");
        int input1 = thief.readInputInt();
        if (thief.getPlayerHand().getCardsInHand().get(input1).getCardType().equals(this.getCardType()) && thief.getPlayerHand().getCardsInHand().get(input1).getCardName().equals(this.getCardName())){
            System.out.println("Which player are you asking the card from?");
            int input2 = thief.readInputInt();

            Player victim = thief.getGame().getPlayers().get(input2);
            System.out.println("Player " + victim.getPlayerName() + " choose a card to give as a favor:");
            victim.printHand();
            int input3 = victim.readInputInt();
            Card temp = victim.getPlayerHand().getCardsInHand().get(input3);
            victim.getPlayerHand().getCardsInHand().remove(temp);
            thief.getPlayerHand().getCardsInHand().add(temp);

            //Manage data for NOPE card
            thief.getGame().getData2().setCardUser(thief);
            thief.getGame().getData2().setCardTarget(victim);
            thief.getGame().getData2().setCardPlayed(this);
            thief.getGame().getData2().setStolenCard(temp);
            thief.getGame().getData2().setComboPlayed(thief.getPlayerHand().getCardsInHand().get(input1), this);
            thief.getGame().getData2().setDrawPileBeforeTurn(thief.getGame().getDeck().getDrawPile());

            thief.getPlayerHand().getCardsInHand().remove(input1);
            thief.getPlayerHand().getCardsInHand().remove(this);
        }
    }
}
