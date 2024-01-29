package exploding_kittens.model;

public class SkipCard extends Card{


    public SkipCard(cardType type, String cardName, Deck deck) {
        super(type, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // skip your turn and go to the next player
        player.setSkipTurn(true);
        if (player.getGame().getTurns() == 1 && player.getGame().getCurrent() == player.getPositionIndex()) {
            if (player.getPositionIndex() == (player.getGame().getPlayers().size() - 1)) {
                player.getGame().setCurrent(0);
            } else {
                player.getGame().setCurrent((player.getGame().getCurrent() + 1));
            }
        }
    }
}
