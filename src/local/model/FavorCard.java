package local.model;

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
            thief.getTui().showMessage("Which player are we asking the card from?");
            index = thief.getTui().readInputInt();
            if (index == -10 || index == -1){
                return;
            }
        }
        Player victim = thief.getGame().getPlayers().get(index);
        victim.getTui().showMessage("Player " + victim.getPlayerName() + " choose a card to give as a favor:");
        victim.getTui().printHand();

        int index2 = -1;
        if (victim instanceof Computer){
            index2 = (int)(Math.random() * (victim.getPlayerHand().getCardsInHand().size() - 1));
        }
        else{
            boolean goBack = true;
            while(goBack){
                goBack = false;
                index2 = victim.getTui().readInputInt();
                if (index2 == -10 || index2 == -1){
                    victim.getTui().showMessage("You cannot back out of this.");
                    goBack = true;
                }
            }
        }
        Card temp = victim.getPlayerHand().getCardsInHand().get(index2);
        victim.getPlayerHand().getCardsInHand().remove(temp);
        thief.getPlayerHand().getCardsInHand().add(temp);
        if (!(thief instanceof Computer)){
            thief.getTui().showMessage("You got " + temp.getCardName());
        }
    }
}
