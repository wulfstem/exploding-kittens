package exploding_kittens.model;

import java.util.ArrayList;
import java.util.Random;

public class Hand {


    private final int STARTING_CARDS_IN_HAND = 8;
    private ArrayList<Card> cardsInHand;
    private Player player;


    public Hand(Player player, Deck deck){
        this.player = player;
        cardsInHand = new ArrayList<>();
        Random random = new Random();
        while(cardsInHand.size() < STARTING_CARDS_IN_HAND - 1){
            ArrayList<Card> pile = deck.getDrawPile();
            Card temp = pile.get(random.nextInt(pile.size()));
            if(!temp.getCardType().equals(Card.cardType.DEFUSE) && !temp.getCardType().equals(Card.cardType.BOMB)){
                cardsInHand.add(temp);
                pile.remove(temp);
                deck.setDrawPile(pile);
            }
        }
        for (int i = 0; i < deck.getDrawPile().size(); i++){
            ArrayList<Card> pile = deck.getDrawPile();
            Card temp = pile.get(i);
            if (temp.getCardType().equals(Card.cardType.DEFUSE)){
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

    public void setCardsInHand(ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public void remove(Card card){
        ArrayList<Card> temp = new ArrayList<>(getCardsInHand());
        temp.remove(card);
        setCardsInHand(temp);
    }

    public void add(Card card){
        ArrayList<Card> temp = new ArrayList<>(getCardsInHand());
        temp.add(card);
        setCardsInHand(temp);
    }

    public Player getPlayer() {
        return player;
    }
}
