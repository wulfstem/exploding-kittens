package exploding_kittens.model;

public class Attack2Card extends Card{


    public Attack2Card(CARD_TYPE cardType, String cardName, Deck deck) {
        super(cardType, cardName, deck);
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
            boolean goBack = true;
            while(goBack){
                goBack = false;
                System.out.println("Which player are you attacking? (index)");
                index = player.readInputInt();
                if (index == -10 || index == -1){
                    System.out.println("You cannot go back on this decision.");
                    goBack = true;
                }
            }
            player.getGame().setCurrent(index);
        }
    }
}
