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
        boolean allow = true;

        if (choiceYN == 0){
            int choiceCardToPlay = (int)(Math.random() * (getPlayerHand().getCardsInHand().size() - 1));
            if (getPlayerHand().getCardsInHand().get(choiceCardToPlay).getCardType().equals(Card.CARD_TYPE.DEFUSE)){
                makeMove();
                return;
            }
            else if(getPlayerHand().getCardsInHand().get(choiceCardToPlay).getCardType().equals(Card.CARD_TYPE.NOPE)){
                makeMove();
                return;
            }
            else if(getPlayerHand().getCardsInHand().get(choiceCardToPlay).getCardType().equals(Card.CARD_TYPE.REGULAR)){
                Card tempCard = getPlayerHand().getCardsInHand().get(choiceCardToPlay);
                for (Card card : this.getPlayerHand().getCardsInHand()){
                    allow = card.getCardType().equals(tempCard.getCardType()) && card.getCardName().equals(tempCard.getCardName()) && !(card.equals(tempCard));
                }
            }
            if (!allow){
                makeMove();
                return;
            }
            if(getGame().validateMove(getPlayerHand().getCardsInHand().get(choiceCardToPlay), this)) {
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

    @Override
    public boolean askNope(Card card, Player player){
        boolean result = false;
        boolean answer = ((int) (Math.random() * 1)) == 0;
        if (answer) {
            result = true;
            int index = getCardChoice(Card.CARD_TYPE.NOPE);
            getPlayerHand().getCardsInHand().remove(index);
        }
        return result;
    }

    @Override
    public void dieOrDefuse(Card bomb){
        if (handContains(Card.CARD_TYPE.DEFUSE)) {
            boolean answer = ((int) (Math.random() * 1)) == 0;
            if (answer){
                int index = getCardChoice(Card.CARD_TYPE.DEFUSE);
                getPlayerHand().getCardsInHand().remove(index);

                int answer2 = ((int) (Math.random() * (getGame().getDeck().getDrawPile().size())));
                getGame().getDeck().getDrawPile().add(answer2, bomb);
                getPlayerHand().getCardsInHand().remove(bomb);
            }
            else{
                die();
            }
        } else {
            die();
        }
    }

    @Override
    public int getCardChoice(Card.CARD_TYPE cardType){
        boolean isValid = false;
        int choice = 0;
        while(!isValid){
            choice = (int)(Math.random() * (getPlayerHand().getCardsInHand().size() - 1));
            if (getPlayerHand().getCardsInHand().get(choice).getCardType().equals(cardType)){
                isValid = true;
            }
        }
        return choice;
    }
}
