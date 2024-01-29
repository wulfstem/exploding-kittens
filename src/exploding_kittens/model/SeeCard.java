package exploding_kittens.model;

public class SeeCard extends Card{


    public static final int VISION_OF_DRAW_PILE = 3;

    public SeeCard(cardType type, String cardName, Deck deck) {
        super(type, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // see the top 3 cards of the drawPile
        if (!(player instanceof Computer)){
            player.getController().showFuture(player);
        }
    }
}
