package exploding_kittens.model;

import java.util.ArrayList;
import java.util.Random;

public class Hand {


    private final int STARTING_CARDS_IN_HAND = 7 + 1;
    private ArrayList<Card> cardsInHand;
    private Player player;


    public Hand(Player player, Deck deck){
        this.player = player;
        cardsInHand = new ArrayList<>();
        Random random = new Random();
        while(cardsInHand.size() < STARTING_CARDS_IN_HAND - 1){
            ArrayList<Card> pile = deck.getDrawPile();
            Card temp = pile.get(random.nextInt(pile.size()));
            if(!temp.getCardType().equals(Card.CARD_TYPE.DEFUSE) && !temp.getCardType().equals(Card.CARD_TYPE.BOMB)){
                cardsInHand.add(temp);
                pile.remove(temp);
                deck.setDrawPile(pile);
            }
        }
        for (int i = 0; i < deck.getDrawPile().size(); i++){
            ArrayList<Card> pile = deck.getDrawPile();
            Card temp = pile.get(i);
            if (temp.getCardType().equals(Card.CARD_TYPE.DEFUSE)){
                cardsInHand.add(temp);
                pile.remove(temp);
                deck.setDrawPile(pile);
                break;
            }
        }
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public Player getPlayer() {
        return player;
    }
}
