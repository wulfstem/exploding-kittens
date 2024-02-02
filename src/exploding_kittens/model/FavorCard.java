package exploding_kittens.model;

public class FavorCard extends Card{


    public FavorCard(cardType type, String cardName, Deck deck) {
        super(type, cardName, deck);
    }

    @Override
    public void action(Player thief) {
        // Asks a different player to give up one of his cards
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
            index = thief.getController().getOtherPlayerChoice(thief);
        }
        Player victim = thief.getGame().getPlayers().get(index);
        int cardIndex = 0;

        if (victim instanceof Computer){
            cardIndex = (int)(Math.random() * (victim.getPlayerHand().getCardsInHand().size() - 1));
        }
        else{
            cardIndex = victim.getController().getCardForFavor(victim);
        }
        Card temp = victim.getPlayerHand().getCardsInHand().get(cardIndex);
        victim.getPlayerHand().remove(temp);
        thief.getPlayerHand().add(temp);

        if (!(thief instanceof Computer)){
            thief.getController().informStolenCard(thief, temp);
        }
    }
}
