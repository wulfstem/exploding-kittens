package exploding_kittens.model;

import exploding_kittens.Controller;

import java.util.ArrayList;

public class Player {


    private String playerName;
    private Game game;
    private Hand playerHand;
    private boolean skipTurn;
    private int positionIndex;
    private final Controller controller;

    public Player(String name, Game game, int positionIndex, Controller controller) {
        this.playerName = name;
        this.game = game;
        this.positionIndex = positionIndex;
        this.controller = controller;
    }

    public void makeMove() {
        System.out.println("Current player " + game.getPlayers().get(game.getCurrent()).getPlayerName());
        System.out.println("makeMove called for player " + getPlayerName());
        playOrDraw();
    }

    public void playOrDraw() {
        System.out.println("Current player " + game.getPlayers().get(game.getCurrent()));
        System.out.println("Starting playOrDraw");
        setSkipTurn(false);
        controller.showHand(this);
        if (controller.isCardBeingPlayed()) {
            int index = controller.whichCardIsPlayed();
            if (index == -1){
                playOrDraw();
                return;
            }
            if(controller.validateMove(getPlayerHand().getCardsInHand().get(index), this)){
                getPlayerHand().getCardsInHand().get(index).action(this);
                getPlayerHand().remove(getPlayerHand().getCardsInHand().get(index));
                System.out.println("validate True");
                if (!isSkipTurn()) {
                    playOrDraw();
                }
            }
            else {
                System.out.println("validate False");
                controller.moveCanceled(this);
                getPlayerHand().remove(getPlayerHand().getCardsInHand().get(index));
                playOrDraw();
            }
        } else {
            controller.draw(this);
        }
    }

    public void die() {
        ArrayList<Player> temp = getGame().getPlayers();
        temp.remove(this);
        getGame().setPlayers(temp);

    }

    public boolean handContains(Card.cardType cardType) {
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

    public Controller getController(){
        return controller;
    }
}