package exploding_kittens.model;

import java.util.ArrayList;

public class Deck {
    public final int NUMBER_OF_DEFUSES = 6;
    public final int NUMBER_OF_ATTACK2 = 4;
    public final int NUMBER_OF_FAVOR = 4;
    public final int NUMBER_OF_NOPE = 5;
    public final int NUMBER_OF_SHUFFLE = 4;
    public final int NUMBER_OF_SKIP = 4;
    public final int NUMBER_OF_SEE3 = 5;
    private final String[] AVAILABLE_REGULAR_NAMES = {"Beard Cat", "Cattermelon", "Hairy Potato Cat", "Rainbow-Ralphing Cat", "Tacocat"};

    private int numberOfActiveBombs;

    private ArrayList<Card> drawPile;
    private ArrayList<Card> discardPile;

    public Deck(int numberOfPlayers){
        discardPile = new ArrayList<>();
        drawPile = new ArrayList<>();
        numberOfActiveBombs = 0;
        for (int i = 0; i < NUMBER_OF_DEFUSES; i++){
            drawPile.add(new DefuseCard(Card.cardType.DEFUSE, "DEFUSE", this));
        }
        for (int i = 0; i < numberOfPlayers - 1; i++){
            drawPile.add(new BombCard(Card.cardType.BOMB, "BOMB", this));
            numberOfActiveBombs++;
        }
        for (int i = 0; i < NUMBER_OF_ATTACK2; i++){
            drawPile.add(new AttackCard(Card.cardType.ATTACK2, "ATTACK",this));
        }
        for (int i = 0; i < NUMBER_OF_FAVOR; i++){
            drawPile.add(new FavorCard(Card.cardType.FAVOR, "FAVOR",this));
        }
        for (int i = 0; i < NUMBER_OF_NOPE; i++){
            drawPile.add(new NopeCard(Card.cardType.NOPE, "NOPE",this));
        }
        for (int i = 0; i < NUMBER_OF_SHUFFLE; i++){
            drawPile.add(new ShuffleCard(Card.cardType.SHUFFLE, "SHUFFLE",this));
        }
        for (int i = 0; i < NUMBER_OF_SKIP; i++){
            drawPile.add(new SkipCard(Card.cardType.SKIP, "SKIP",this));
        }
        for (int i = 0; i < NUMBER_OF_SEE3; i++){
            drawPile.add(new SeeCard(Card.cardType.SEE3, "SEE_AHEAD",this));
        }
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 4; j++){
                drawPile.add(new RegularCard(Card.cardType.REGULAR, AVAILABLE_REGULAR_NAMES[i],this));
            }
        }
    }

    public ArrayList<Card> getDrawPile() {
        return drawPile;
    }

    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }


    public void setDrawPile(ArrayList<Card> drawPile) {
        this.drawPile = drawPile;
    }

    public void setDiscardPile(ArrayList<Card> discardPile) {
        this.discardPile = discardPile;
    }

    public int getNumberOfActiveBombs(){
        return numberOfActiveBombs;
    }
}
