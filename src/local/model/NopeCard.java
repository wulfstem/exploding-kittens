package local.model;

public class NopeCard extends Card{


    public NopeCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // Has no action

    }
}
