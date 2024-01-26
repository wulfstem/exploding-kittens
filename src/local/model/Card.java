package local.model;

public abstract class Card {


    public enum CARD_TYPE{DEFUSE, BOMB, ATTACK2, FAVOR, NOPE, SHUFFLE, SKIP, SEE3, REGULAR}
    private CARD_TYPE cardType;
    private String cardName;
    private Deck deck;

    public Card(CARD_TYPE cardType, String cardName, Deck deck){
        this.cardType = cardType;
        this.cardName = cardName;
        this.deck = deck;
    }

    public abstract void action(Player player);

    public CARD_TYPE getCardType() {
        return cardType;
    }

    public String getCardName() {
        return cardName;
    }

    public Deck getDeck() {
        return deck;
    }
}
