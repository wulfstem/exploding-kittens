package exploding_kittens.model;

public class NopeCard extends Card{


    public NopeCard(cardType type, String cardName, Deck deck) {
        super(type, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // Has no action
    }
}
