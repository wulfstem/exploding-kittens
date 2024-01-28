package exploding_kittens.model;

import java.util.Iterator;

public class RegularCard extends Card{


    public RegularCard(cardType type, String cardName, Deck deck) {
        super(type, cardName, deck);
    }

    @Override
    public void action(Player thief) {
        // can be used as a steal when using two matching regular cards
        Player victim = null;
        if (thief instanceof Computer){
            Iterator<Card> cardIterator = thief.getPlayerHand().getCardsInHand().iterator();

            while (cardIterator.hasNext()) {
                Card matchingCard = cardIterator.next();

                if (matchingCard.getCardType().equals(this.getCardType()) && matchingCard.getCardName().equals(this.getCardName()) && !(matchingCard.equals(this))) {
                    int index = 0;
                    boolean valid = false;

                    while (!valid) {
                        index = (int) (Math.random() * thief.getGame().getPlayers().size() - 1);

                        if (index != thief.getPositionIndex()) {
                            valid = true;
                        }
                    }

                    victim = thief.getGame().getPlayers().get(index);

                    int cardIndex = victim.getController().getCardForFavor(victim);
                    Card temp = victim.getPlayerHand().getCardsInHand().get(cardIndex);
                    thief.getPlayerHand().add(temp);
                    thief.getPlayerHand().remove(matchingCard);
                    cardIterator.remove();
                }
            }
        }
        else{
            int matchingCardIndex = thief.getController().getMatchingCard(thief, this);
            Card matchingCard = thief.getPlayerHand().getCardsInHand().get(matchingCardIndex);
            if (matchingCard.getCardType().equals(this.getCardType()) && matchingCard.getCardName().equals(this.getCardName()) && !(matchingCard.equals(this))){
                int victimIndex = thief.getController().getOtherPlayerChoice(thief);
                victim = thief.getGame().getPlayers().get(victimIndex);
                if (victim instanceof Computer){
                    int index2 = (int)(Math.random() * (victim.getPlayerHand().getCardsInHand().size() - 1));
                    Card temp = victim.getPlayerHand().getCardsInHand().get(index2);
                    victim.getPlayerHand().remove(temp);
                    thief.getPlayerHand().add(temp);
                    thief.getPlayerHand().remove(matchingCard);
                }
                else{
                    int chosenCardIndex = victim.getController().getCardForFavor(victim);
                    Card temp = victim.getPlayerHand().getCardsInHand().get(chosenCardIndex);
                    victim.getPlayerHand().remove(temp);
                    thief.getPlayerHand().add(temp);
                    thief.getPlayerHand().remove(matchingCard);
                }
            }
        }
    }
}
