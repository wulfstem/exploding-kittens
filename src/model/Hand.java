import java.util.ArrayList;

public class Hand {


    private ArrayList<Card> hand;
    private Player player;


    public Hand(ArrayList<Card> cards, Player player){
        this.hand = cards;
        this.player = player;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public Player getPlayer() {
        return player;
    }
}
