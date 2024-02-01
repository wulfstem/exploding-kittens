package exploding_kittens.model;

import exploding_kittens.Controller;

import java.util.ArrayList;
import java.util.Random;

public class Computer extends Player{


    public static final String COMPUTER_NAME = "RoboTukas";
    private final int RANDOMLY_PICKED_TRUE = 1;

    public Computer(String playerName, Game game, int positionIndex, Controller controller) {
        super(playerName, game, positionIndex, controller);
    }

    @Override
    public void makeMove(){
        playOrDraw();
    }

    @Override
    public void playOrDraw(){
        setSkipTurn(false);
        Random random = new Random();
        int choice = random.nextInt(2);
        if (choice == RANDOMLY_PICKED_TRUE){
            boolean valid = false;
            int choiceCard = -1;
            while(!valid){
                valid = true;
                random = new Random();
                choiceCard = random.nextInt(getPlayerHand().getCardsInHand().size());
                if (getPlayerHand().getCardsInHand().get(choiceCard).getCardType().equals(Card.cardType.DEFUSE)){
                    valid = false;
                }
                if (getPlayerHand().getCardsInHand().get(choiceCard).getCardType().equals(Card.cardType.NOPE)){
                    valid = false;
                }
                if (getPlayerHand().getCardsInHand().get(choiceCard).getCardType().equals(Card.cardType.REGULAR)){
                    for (int i = 0; i < getPlayerHand().getCardsInHand().size(); i++) {
                        valid = getPlayerHand().getCardsInHand().get(i).getCardType().equals(Card.cardType.REGULAR) && choiceCard != i;
                    }
                }
            }
            if(getController().validateMove(getPlayerHand().getCardsInHand().get(choiceCard), this)){
                getPlayerHand().getCardsInHand().get(choiceCard).action(this);
                getPlayerHand().remove(getPlayerHand().getCardsInHand().get(choiceCard));
                if (!isSkipTurn()) {
                    playOrDraw();
                }
            }
            else{
                getPlayerHand().remove(getPlayerHand().getCardsInHand().get(choiceCard));
                playOrDraw();
            }
        }
        else{
            ArrayList<Card> pile = getGame().getDeck().getDrawPile();
            Card temp = pile.get(0);
            pile.remove(temp);
            getGame().getDeck().setDrawPile(pile);
            getPlayerHand().add(temp);
            if (temp.getCardType().equals(Card.cardType.BOMB)) {
                drewBomb(temp);
            }
            if (getGame().getTurns() == 1 && getGame().getCurrent() == getPositionIndex()) {
                if (getPositionIndex() == (getGame().getPlayers().size() - 1)) {
                    getGame().setCurrent(0);
                } else {
                    getGame().setCurrent((getGame().getCurrent() + 1));
                }
            }
            if (getGame().getTurns() != 1){
                getGame().setTurns(getGame().getTurns() - 1);
            }
        }
    }

    public void drewBomb(Card bomb){
        if (handContains(Card.cardType.DEFUSE)){
            for(Card card: getPlayerHand().getCardsInHand()){
                if(card.getCardType().equals(Card.cardType.DEFUSE)){
                    getPlayerHand().remove(card);
                    getPlayerHand().remove(bomb);
                }
            }
            ArrayList<Card> pile = getGame().getDeck().getDrawPile();
            pile.add(0, bomb);
            getGame().getDeck().setDrawPile(pile);
        }
        else{
            die();
        }
    }
}
