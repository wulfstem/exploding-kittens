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
        Card temp = null;
        if (thief instanceof Computer){
            Iterator<Card> cardIterator = thief.getPlayerHand().getCardsInHand().iterator();

            while (cardIterator.hasNext()) {
                Card card = cardIterator.next();

                if (card.getCardType().equals(this.getCardType()) && card.getCardName().equals(this.getCardName()) && !(card.equals(this))) {
                    int index = 0;
                    boolean valid = false;

                    while (!valid) {
                        index = (int) (Math.random() * thief.getGame().getPlayers().size() - 1);

                        if (index != thief.getPositionIndex()) {
                            valid = true;
                        }
                    }

                    victim = thief.getGame().getPlayers().get(index);

                    if (victim instanceof Computer) {
                        int index2 = (int) (Math.random() * (victim.getPlayerHand().getCardsInHand().size() - 1));
                        temp = victim.getPlayerHand().getCardsInHand().get(index2);
                    } else {
                        victim.getController().getTui().showMessage("Player " + victim.getPlayerName() + " choose a card to give as a favor:");
                        victim.getController().getTui().printHand(victim);
                        int input3 = victim.getController().getTui().readInputInt();
                        temp = victim.getPlayerHand().getCardsInHand().get(input3);
                    }
                    cardIterator.remove();
                    thief.getPlayerHand().getCardsInHand().add(temp);
                }
            }
        }
        else{
            thief.getController().getTui().showMessage("Choose a duplicate card in your hand:");
            int input1 = thief.getController().getTui().getCardChoice(thief, cardType.REGULAR);
            if (input1 == -10 || input1 == -1){
                return;
            }
            if (thief.getPlayerHand().getCardsInHand().get(input1).getCardType().equals(this.getCardType()) && thief.getPlayerHand().getCardsInHand().get(input1).getCardName().equals(this.getCardName()) && !(thief.getPlayerHand().getCardsInHand().get(input1).equals(this))){
                thief.getController().getTui().showMessage("Which player are you asking the card from?");
                int input2 = thief.getController().getTui().readInputInt();
                if (input2 == -10 || input2 == -1){
                    return;
                }
                victim = thief.getGame().getPlayers().get(input2);
                if (victim instanceof Computer){
                    int index2 = (int)(Math.random() * (victim.getPlayerHand().getCardsInHand().size() - 1));
                    temp = victim.getPlayerHand().getCardsInHand().get(index2);
                    victim.getPlayerHand().getCardsInHand().remove(temp);
                    thief.getPlayerHand().getCardsInHand().add(temp);
                }
                else{
                    boolean goBack = true;
                    while(goBack){
                        goBack = false;
                        victim.getController().getTui().showMessage("Player " + victim.getPlayerName() + " choose a card to give as a favor:");
                        victim.getController().getTui().printHand(victim);
                        int input3 = victim.getController().getTui().readInputInt();
                        if (input3 < 0 || input3 > (victim.getPlayerHand().getCardsInHand().size() - 1)){
                            goBack = true;
                            continue;
                        }
                        temp = victim.getPlayerHand().getCardsInHand().get(input3);
                        victim.getPlayerHand().getCardsInHand().remove(temp);
                        thief.getPlayerHand().getCardsInHand().add(temp);
                    }
                }
            }
            else{
                thief.getController().getTui().showMessage("Invalid input, choose a duplicate card but not the same one");
                action(thief);
            }
        }
    }
}
