package model;

public class Attack2Card extends Card{


    public Attack2Card(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // tap another player to take 2 turns and skip yours
        player.setSkipTurn(true);
        if (player.getGame().getTurns() == 1){
            player.getGame().setTurns(player.getGame().getTurns() + 1);
        }
        else{
            player.getGame().setTurns(player.getGame().getTurns() + 2);
        }
        System.out.println("Which player are you attacking? (index)");
        int index = player.readInputInt();
        player.getGame().setCurrent(index);

        //Manage data for NOPE card
        player.getGame().getData2().setCardUser(player);
        player.getGame().getData2().setCardTarget(player.getGame().getPlayers().get(index));
        player.getGame().getData2().setCardPlayed(this);
        player.getGame().getData2().setStolenCard(null);
        player.getGame().getData2().setComboPlayed(null, null);
        player.getGame().getData2().setDrawPileBeforeTurn(player.getGame().getDeck().getDrawPile());
    }
}
