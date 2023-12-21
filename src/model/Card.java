public abstract class Card {


    public enum CARD_TYPE{DEFUSE, BOMB, SKIP, SKIP_REVERSE, TAP, DOUBLE_TAP, SEE3, SHUFFLE, DRAW_BOTTOM, STEAL}

    private String name;
    private CARD_TYPE cardType;

    public Card(CARD_TYPE cardType, String name){
        this.name = name;
        this.cardType = cardType;
    }

    public void action(){}


    public String getName() {
        return name;
    }

    public CARD_TYPE getCardType() {
        return cardType;
    }
}
