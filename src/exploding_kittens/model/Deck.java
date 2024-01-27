package exploding_kittens.model;

import java.util.ArrayList;

public class Deck {
    private final String[] AVAILABLE_BOMB_NAMES = {"Cat hair in Warp Core", "Dynamite Kitten & Ship", "Grenade Kitten & House", "Walking on Nuclear Launch Keyboard"};
    public final int NUMBER_OF_DEFUSES = 6;
    private final String[] AVAILABLE_DEFUSE_NAMES = {"Belly Rubs", "Capnip Sandwiches", "Kitten Therapy", "Laser Pointer", "Kitten Yoga", "3AM Flatulence"};
    public final int NUMBER_OF_ATTACK2 = 4;
    private final String[] AVAILABLE_ATTACK2_NAMES = {"Bear-o-dactyl", "Catterwocky", "Crab-a-pult", "Thousand-year Back Hair"};
    public final int NUMBER_OF_FAVOR = 4;
    private final String[] AVAILABLE_FAVOR_NAMES = {"Back Hair Shampoo", "Beard-Sailing", "Enslaved by Party Squirrels", "Rub Peanut Butter on your Belly Button"};
    public final int NUMBER_OF_NOPE = 5;
    private final String[] AVAILABLE_NOPE_NAMES = {"Jackanope", "Nope Ninja", "Nope Sandwich", "Nopebell Peace Prize", "Nopestradamus"};
    public final int NUMBER_OF_SHUFFLE = 4;
    private final String[] AVAILABLE_SHUFFLE_NAMES = {"Abracrab Lincoln", "Bat farts", "Pomeranian Storm", "Transdimensional Litter Box"};
    public final int NUMBER_OF_SKIP = 4;
    private final String[] AVAILABLE_SKIP_NAMES = {"Bunnyraptor", "Cheetah Butt", "Crab Walk", "Hypergoat"};
    public final int NUMBER_OF_SEE3 = 5;
    private final String[] AVAILABLE_SEE3_NAMES = {"Goat Wizard", "Mantis Shrimp", "Pig-a-corn", "Special-ops Bunnies", "Unicorn Enchilada"};
    public final int NUMBER_OF_REGULAR = 5 * 4;
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

        /*
        ALTERNATIVE WAY OF NAMING CARDS
        for (int i = 0; i < NUMBER_OF_DEFUSES; i++){
            drawPile.add(new DefuseCard(Card.CARD_TYPE.DEFUSE, AVAILABLE_DEFUSE_NAMES[i], this));
        }
        for (int i = 0; i < numberOfPlayers - 1; i++){
            drawPile.add(new BombCard(Card.CARD_TYPE.BOMB, AVAILABLE_BOMB_NAMES[i], this));
        }
        for (int i = 0; i < NUMBER_OF_ATTACK2; i++){
            drawPile.add(new Attack2Card(Card.CARD_TYPE.ATTACK2, AVAILABLE_ATTACK2_NAMES[i],this));
        }
        for (int i = 0; i < NUMBER_OF_FAVOR; i++){
            drawPile.add(new FavorCard(Card.CARD_TYPE.FAVOR, AVAILABLE_FAVOR_NAMES[i],this));
        }
        for (int i = 0; i < NUMBER_OF_NOPE; i++){
            drawPile.add(new NopeCard(Card.CARD_TYPE.NOPE, AVAILABLE_NOPE_NAMES[i],this));
        }
        for (int i = 0; i < NUMBER_OF_SHUFFLE; i++){
            drawPile.add(new ShuffleCard(Card.CARD_TYPE.SHUFFLE, AVAILABLE_SHUFFLE_NAMES[i],this));
        }
        for (int i = 0; i < NUMBER_OF_SKIP; i++){
            drawPile.add(new SkipCard(Card.CARD_TYPE.SKIP, AVAILABLE_SKIP_NAMES[i],this));
        }
        for (int i = 0; i < NUMBER_OF_SEE3; i++){
            drawPile.add(new See3Card(Card.CARD_TYPE.SEE3, AVAILABLE_SEE3_NAMES[i],this));
        }
        for (int i = 0; i < NUMBER_OF_NOPE; i++){
            drawPile.add(new NopeCard(Card.CARD_TYPE.NOPE, AVAILABLE_NOPE_NAMES[i],this));
        }
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 4; j++){
                drawPile.add(new RegularCard(Card.CARD_TYPE.REGULAR, AVAILABLE_REGULAR_NAMES[i],this));
            }
        }
        */
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

    public int getNumberOfActiveBombs(){
        return numberOfActiveBombs;
    }
}
