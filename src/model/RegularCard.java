package model;

import java.util.Iterator;

public class RegularCard extends Card{


    public RegularCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player thief) {
        // can be used as a steal when using two matching regular cards
        Player victim = null;
        Card temp = null;
        if (thief instanceof Computer){
            /*
            for (Card card: thief.getPlayerHand().getCardsInHand()){
                if (card.getCardType().equals(this.getCardType()) && card.getCardName().equals(this.getCardName()) && !(card.equals(this))){
                    int index = 0;
                    boolean valid = false;
                    while(!valid){
                        index = (int)(Math.random() * thief.getGame().getPlayers().size() - 1);
                        if(index != thief.getPositionIndex()){
                            valid = true;
                        }
                    }
                    victim = thief.getGame().getPlayers().get(index);
                    if (victim instanceof Computer){
                        int index2 = (int)(Math.random() * (victim.getPlayerHand().getCardsInHand().size() - 1));
                        temp = victim.getPlayerHand().getCardsInHand().get(index2);
                        victim.getPlayerHand().getCardsInHand().remove(temp);
                        thief.getPlayerHand().getCardsInHand().add(temp);
                    }
                    else{
                        System.out.println("Player " + victim.getPlayerName() + " choose a card to give as a favor:");
                        victim.printHand();
                        int input3 = victim.readInputInt();
                        temp = victim.getPlayerHand().getCardsInHand().get(input3);
                        victim.getPlayerHand().getCardsInHand().remove(temp);
                        thief.getPlayerHand().getCardsInHand().add(temp);
                    }
                }
            }
             */
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
                        System.out.println("Player " + victim.getPlayerName() + " choose a card to give as a favor:");
                        victim.printHand();
                        int input3 = victim.readInputInt();
                        temp = victim.getPlayerHand().getCardsInHand().get(input3);
                    }
                    cardIterator.remove();
                    thief.getPlayerHand().getCardsInHand().add(temp);
                }
            }
        }
        else{
            System.out.println("Choose a duplicate card in your hand:");
            int input1 = thief.readInputInt();
            if (thief.getPlayerHand().getCardsInHand().get(input1).getCardType().equals(this.getCardType()) && thief.getPlayerHand().getCardsInHand().get(input1).getCardName().equals(this.getCardName()) && !(thief.getPlayerHand().getCardsInHand().get(input1).equals(this))){
                System.out.println("Which player are you asking the card from?");
                int input2 = thief.readInputInt();

                victim = thief.getGame().getPlayers().get(input2);
                if (victim instanceof Computer){
                    int index2 = (int)(Math.random() * (victim.getPlayerHand().getCardsInHand().size() - 1));
                    temp = victim.getPlayerHand().getCardsInHand().get(index2);
                    victim.getPlayerHand().getCardsInHand().remove(temp);
                    thief.getPlayerHand().getCardsInHand().add(temp);
                }
                else{
                    System.out.println("Player " + victim.getPlayerName() + " choose a card to give as a favor:");
                    victim.printHand();
                    int input3 = victim.readInputInt();
                    temp = victim.getPlayerHand().getCardsInHand().get(input3);
                    victim.getPlayerHand().getCardsInHand().remove(temp);
                    thief.getPlayerHand().getCardsInHand().add(temp);
                }
            }
            else{
                System.out.println("Invalid input, choose a duplicate card but not the same one");
                action(thief);
            }
        }
    }
}
