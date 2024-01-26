package exploding_kittens.model;

public class DefuseCard extends Card{


    public DefuseCard(cardType type, String cardName, Deck deck) {
        super(type, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // defuse can be used after a bomb is drawn
    }
}
