package model;

public class Computer extends Player{


    public static final String COMPUTER_NAME = "RoboTukas";

    public Computer(String playerName, Game game, int positionIndex) {
        super(playerName, game, positionIndex);
    }

    @Override
    public void makeMove(){
        int current = super.getGame().getCurrent();
        setSkipTurn(false);
        int choiceYN = (int)(Math.random() * 1);

        if (choiceYN == 0){
            int choiceCardToPlay = (int)(Math.random() * getPlayerHand().getCardsInHand().size()) + 1;

            if(getGame().validateMove(getPlayerHand().getCardsInHand().get(choiceCardToPlay), this)) {
                getGame().updateData();
                getPlayerHand().getCardsInHand().get(choiceCardToPlay).action(this);
                if (isSkipTurn()) {
                    if (getGame().getCurrent() == current) {
                        getGame().setCurrent((getGame().getCurrent() + 1) % getGame().getPlayers().size());
                    }
                }
                else{
                    makeMove();
                }
            }
            else {
                getPlayerHand().getCardsInHand().remove(choiceCardToPlay);
                makeMove();
            }
        }
        else {
            draw();
            getGame().setCurrent((getGame().getCurrent() + 1) % getGame().getPlayers().size());
        }
    }
}
