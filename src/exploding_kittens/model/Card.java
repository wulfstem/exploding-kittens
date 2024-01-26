package exploding_kittens.model;

public abstract class Card {


    public enum cardType{DEFUSE, BOMB, ATTACK2, FAVOR, NOPE, SHUFFLE, SKIP, SEE3, REGULAR}
    private cardType type;
    private String cardName;
    private Deck deck;

    public Card(cardType cardType, String cardName, Deck deck){
        this.type = cardType;
        this.cardName = cardName;
        this.deck = deck;
    }

    public abstract void action(Player player);

    public cardType getCardType() {
        return type;
    }

    public String getCardName() {
        return cardName;
    }

    public Deck getDeck() {
        return deck;
    }
}
