package model;

public class FavorCard extends Card{


    public FavorCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        System.out.println("Which player are we asking the card from?");
        int input1 = player.readInputInt();
        player.steal(player.getGame().getPlayers().get(input1), player);
    }

    @Override
    public void undo(Player player) {
        player.getPlayerHand().getCardsInHand().remove(player.getLastCardStolen());
        player.getLastVictim().getPlayerHand().getCardsInHand().add(player.getLastCardStolen());
    }
}
