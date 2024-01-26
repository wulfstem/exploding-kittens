package exploding_kittens.model;

public class BombCard extends Card{


    public BombCard(cardType type, String cardName, Deck deck) {

        super(type, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // does not have an action
    }
}
