package local.model;

import local.view.LocalPlayerTUI;

import java.util.ArrayList;

public class Player {


    private String playerName;
    private Game game;
    private Hand playerHand;
    private boolean skipTurn;
    private int positionIndex;
    private final LocalPlayerTUI tui;

    public Player(String name, Game game, int positionIndex) {
        this.playerName = name;
        this.game = game;
        this.positionIndex = positionIndex;
        playerHand = new Hand(this, game.getDeck());
        tui = new LocalPlayerTUI(this);
    }

    public void makeMove() {
        int current = getGame().getCurrent();
        skipTurn = false;

        boolean goBack = true;
        while(goBack){
            goBack = false;
            boolean answer;
            tui.showMessage("Do you want to play a card?");
            try{
                answer = tui.readInputBoolean();
            }catch (BooleanReturnException e){
                tui.showMessage("You cannot go back without making this decision.");
                goBack = true;
                continue;
            }
            if (answer){
                int index = getAnyCardChoice();
                if (index == -10 || index == -1){
                    goBack = true;
                    continue;
                }
                // Check if any other player wants to play a NOPE card
                if(getGame().validateMove(getPlayerHand().getCardsInHand().get(index), this)){
                    getPlayerHand().getCardsInHand().get(index).action(this);
                    getPlayerHand().getCardsInHand().remove(index);
                    if (isSkipTurn()) {
                        if (getGame().getCurrent() == current) {
                            getGame().setCurrent((getGame().getCurrent() + 1) % getGame().getPlayers().size());
                        }
                    } else {
                        makeMove();
                    }
                }
                else{
                    tui.showMessage("Your card has been cancelled by another player playing a NOPE card.");
                    getPlayerHand().getCardsInHand().remove(index);
                    makeMove();
                }
            }
            else{
                draw();
                if (getGame().getTurns() == 1){
                    getGame().setCurrent((getGame().getCurrent() + 1) % getGame().getPlayers().size());
                }
            }
        }
    }

    public void draw() {
        ArrayList<Card> pile = getGame().getDeck().getDrawPile();
        Card temp = pile.get(0);
        pile.remove(temp);
        getGame().getDeck().setDrawPile(pile);
        getPlayerHand().getCardsInHand().add(temp);
        if (!(this instanceof Computer)){
            tui.showMessage("\nYou drew " + temp.getCardName() + "!\n");
        }
        if (temp.getCardType().equals(Card.CARD_TYPE.BOMB)) {
            dieOrDefuse(temp);
        }
    }

    public boolean askNope(Card card, Player player){
        boolean result = false;
        tui.showMessage(player.getPlayerName() + " is playing " + card.getCardName());
        tui.printHand();
        boolean goBack = true;
        while(goBack){
            goBack = false;
            tui.showMessage("Do you want to use your NOPE card?");
            boolean answer = false;
            try {
                answer = tui.readInputBoolean();
            } catch (BooleanReturnException e) {
                tui.showMessage("You cannot go back without making this decision.");
                goBack = true;
                continue;
            }
            if (answer) {
                result = true;
                int index = -1;
                while (index == -1){
                    index = getCardChoice(Card.CARD_TYPE.NOPE);
                    if (index == -10){
                        goBack = true;
                    }
                }
                if (index >= 0){
                    getPlayerHand().getCardsInHand().remove(index);
                }
            }
        }
        return result;
    }

    public void dieOrDefuse(Card bomb) {
        tui.showMessage("You have drawn an Exploding Kitten!");
        tui.printHand();
        if (handContains(Card.CARD_TYPE.DEFUSE)) {
            boolean invalid = true;
            boolean answer = false;
            while(invalid){
                tui.showMessage("Use a Defuse?");
                try {
                    answer = tui.readInputBoolean();
                    invalid = false;
                } catch (BooleanReturnException e) {
                    tui.showMessage("You cannot go back without making this decision.");;
                }
            }
            boolean goBack = true;
            while(goBack){
                goBack = false;
                if (answer){
                    tui.showMessage("Which card? (index)");
                    int index = getCardChoice(Card.CARD_TYPE.DEFUSE);
                    if (index == -10 || index == -1){
                        goBack = true;
                        continue;
                    }
                    getPlayerHand().getCardsInHand().remove(index);
                    tui.showMessage("In which position would you like to put the Exploding Kitten? (1 between " + getGame().getDeck().getDrawPile().size() + ")");
                    int input3 = tui.readInputInt();
                    if (input3 == -10){
                        goBack = true;
                        continue;
                    }
                    getGame().getDeck().getDrawPile().add(input3, bomb);
                    getPlayerHand().getCardsInHand().remove(bomb);
                }
                else{
                    tui.showMessage("Better luck next time Champ!");
                    die();
                }
            }
        } else {
            System.out.println("Better luck next time Champ!");
            die();
        }
    }

    public int getCardChoice(Card.CARD_TYPE cardType){
        boolean isIndexValid = false;
        int input2 = 0;
        tui.showMessage("Which card would you like to play? (number between 0 and " + (getPlayerHand().getCardsInHand().size() - 1) + ")");
        while(!isIndexValid){
            input2 = tui.readInputInt();
            if (input2 == -10){
                return -10;
            }
            if (input2 <= 0 || input2 > getPlayerHand().getCardsInHand().size()){
                tui.showMessage("Invalid index, choose a number between 0 and" + (getPlayerHand().getCardsInHand().size() - 1));
            }
            isIndexValid = true;
        }
        if (getPlayerHand().getCardsInHand().get(input2).getCardType().equals(cardType)){
            return input2;
        }
        else{
            tui.showMessage("Chosen card is not of type " + cardType);
            return -1;
        }
    }

    public int getAnyCardChoice(){
        boolean isIndexValid = false;
        int input2 = 0;
        tui.showMessage("Which card would you like to play? (number between 0 and " + (getPlayerHand().getCardsInHand().size() - 1) + ")");
        while(!isIndexValid){
            input2 = tui.readInputInt();
            if (input2 == -10){
                return -10;
            }
            if (input2 < 0 || input2 > getPlayerHand().getCardsInHand().size()){
                tui.showMessage("Invalid index, choose a number between 0 and" + (getPlayerHand().getCardsInHand().size() - 1));
            }
            isIndexValid = true;
        }
        if (getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.CARD_TYPE.DEFUSE)){
            tui.showMessage("Defuse card can only be played when a bomb has been drawn.");
            return -1;
        }
        else if(getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.CARD_TYPE.NOPE)){
            tui.showMessage("Nope card cannot be played at the moment.");
            return -1;
        }
        return input2;
    }

    public void die() {
        ArrayList<Player> temp = getGame().getPlayers();
        temp.remove(this);
        getGame().setPlayers(temp);

    }

    public boolean handContains(Card.CARD_TYPE cardType) {
        for (int i = 0; i < getPlayerHand().getCardsInHand().size(); i++) {
            if (getPlayerHand().getCardsInHand().get(i).getCardType().equals(cardType)) {
                return true;
            }
        }
        return false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
    }

    public Game getGame() {
        return game;
    }

    public boolean isSkipTurn() {
        return skipTurn;
    }

    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public LocalPlayerTUI getTui(){
        return tui;
    }
}