package exploding_kittens.model;

public class FavorCard extends Card{


    public FavorCard(cardType type, String cardName, Deck deck) {
        super(type, cardName, deck);
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
            boolean validp = false;
            while(!validp){
                thief.getController().getTui().showMessage("Which player are we asking the card from?");
                validp = true;
                index = thief.getController().getTui().readInputInt();
                if (index < 0 || index >= thief.getGame().getPlayers().size()){
                    validp = false;
                }
            }
            if (index == -10 || index == -1){
                return;
            }
        }
        Player victim = thief.getGame().getPlayers().get(index);
        victim.getController().getTui().showMessage("Player " + victim.getPlayerName() + " choose a card to give as a favor:");
        victim.getController().getTui().printHand(victim);

        int index2 = -1;
        if (victim instanceof Computer){
            index2 = (int)(Math.random() * (victim.getPlayerHand().getCardsInHand().size() - 1));
        }
        else{
            boolean goBack = true;
            while(goBack){
                goBack = false;
                index2 = victim.getController().getTui().readInputInt();
                if (index2 == -10 || index2 == -1){
                    victim.getController().getTui().showMessage("You cannot back out of this.");
                    goBack = true;
                }
            }
        }
        Card temp = victim.getPlayerHand().getCardsInHand().get(index2);
        victim.getPlayerHand().getCardsInHand().remove(temp);
        thief.getPlayerHand().getCardsInHand().add(temp);
        if (!(thief instanceof Computer)){
            thief.getController().getTui().showMessage("You got " + temp.getCardName());
        }
    }
}
