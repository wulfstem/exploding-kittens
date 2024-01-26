package exploding_kittens.model;

public class SkipCard extends Card{


    public SkipCard(cardType type, String cardName, Deck deck) {
        super(type, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // skip your turn and go to the next player
        player.setSkipTurn(true);
    }
}
