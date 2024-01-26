package exploding_kittens.model;

public class ShuffleCard extends Card{


    public ShuffleCard(cardType type, String cardName, Deck deck) {
        super(type, cardName, deck);
    }

    @Override
    public void action(Player player) {
        //shuffles the draw pile
        player.getGame().shuffle();
    }
}
