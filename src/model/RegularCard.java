package model;

public class RegularCard extends Card{


    public RegularCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // can be used as a steal when using two matching regular cards
        System.out.println("Choose a duplicate card in your hand:");
        int input1 = player.readInputInt();
        if (player.getPlayerHand().getCardsInHand().get(input1).getCardType().equals(this.getCardType()) && player.getPlayerHand().getCardsInHand().get(input1).getCardName().equals(this.getCardName())){
            System.out.println("Which player are you asking the card from?");
            int input2 = player.readInputInt();
            player.steal(player.getGame().getPlayers().get(input2), player);
            player.getPlayerHand().getCardsInHand().remove(input1);
            player.getPlayerHand().getCardsInHand().remove(this);
        }
    }
}
