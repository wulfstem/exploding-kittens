package model;

public class DefuseCard extends Card{


    public DefuseCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // defuse can be used after a bomb is drawn
        System.out.println("This card does not have an action.");
    }

    @Override
    public void undo(Player player) {
        // cannot be undone
    }
}
