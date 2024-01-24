package model;

public class FavorCard extends Card{


    public FavorCard(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
    }

    @Override
    public void action(Player thief) {
        int index = 0;
        if (thief instanceof Computer){
            boolean valid = false;
            while(!valid){
                index = (int)(Math.random() * thief.getGame().getPlayers().size() - 1);
                if(index != thief.getPositionIndex()){
                    valid = true;
                }
            }
        }
        else{
            System.out.println("Which player are we asking the card from?");
            index = thief.readInputInt();
        }
        Player victim = thief.getGame().getPlayers().get(index);
        System.out.println("Player " + victim.getPlayerName() + " choose a card to give as a favor:");
        System.out.println(victim.printHand());

        int index2;
        if (victim instanceof Computer){
            index2 = (int)(Math.random() * (victim.getPlayerHand().getCardsInHand().size() - 1));
        }
        else{
            index2 = victim.readInputInt();
        }
        Card temp = victim.getPlayerHand().getCardsInHand().get(index2);
        victim.getPlayerHand().getCardsInHand().remove(temp);
        thief.getPlayerHand().getCardsInHand().add(temp);
    }
}
