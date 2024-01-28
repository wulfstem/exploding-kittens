package exploding_kittens.model;

public class AttackCard extends Card{


    public AttackCard(cardType type, String cardName, Deck deck) {
        super(type, cardName, deck);
    }

    @Override
    public void action(Player player) {
        // tap another player to take 2 turns and skip yours
        player.setSkipTurn(true);
        if (player.getGame().getTurns() == 1){
            player.getGame().setTurns(player.getGame().getTurns() + 1);
        }
        else{
            player.getGame().setTurns(player.getGame().getTurns() + 2);
        }
        int index = 0;
        if (player instanceof Computer){
            boolean valid = false;
            while(!valid){
                index = (int)(Math.random() * (player.getGame().getPlayers().size() - 1));
                if(index != player.getPositionIndex()){
                    valid = true;
                }
            }
            player.getGame().setCurrent(index);
        }
        else{
            index = player.getController().getOtherPlayerChoice(player);
            player.getGame().setCurrent(index);
        }
    }
}
