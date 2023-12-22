package model;

import java.util.ArrayList;
import java.util.Random;

public class Hand {


    private final int STARTING_CARDS_IN_HAND = 8;
    private ArrayList<Card> handCards;
    private Player player;


    public Hand(Player player, Deck deck){
        this.player = player;
        handCards = new ArrayList<>();
        Random random = new Random();
        while(handCards.size() < STARTING_CARDS_IN_HAND){
            ArrayList<Card> pile = deck.getDrawPile();
            Card temp = pile.get(random.nextInt(pile.size()));
            if(!temp.getCardType().equals(Card.CARD_TYPE.DEFUSE) && !temp.getCardType().equals(Card.CARD_TYPE.BOMB)){
                handCards.add(temp);
                pile.remove(temp);
                deck.setDrawPile(pile);
            }
        }
        for (int i = 0; i < deck.getDrawPile().size(); i++){
            ArrayList<Card> pile = deck.getDrawPile();
            Card temp = pile.get(random.nextInt(pile.size()));
            if (temp.getCardType().equals(Card.CARD_TYPE.DEFUSE)){
                handCards.add(temp);
                pile.remove(temp);
                deck.setDrawPile(pile);
                break;
            }
        }
    }

    public ArrayList<Card> getHandCards() {
        return handCards;
    }

    public Player getPlayer() {
        return player;
    }
}
