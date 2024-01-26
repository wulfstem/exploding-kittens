package exploding_kittens.model;

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

    public void draw() {
        ArrayList<Card> pile = getGame().getDeck().getDrawPile();
        Card temp = pile.get(0);
        pile.remove(temp);
        getGame().getDeck().setDrawPile(pile);
        getPlayerHand().getCardsInHand().add(temp);
        if (!(this instanceof Computer)){
            tui.showMessage("\nYou drew " + temp.getCardName() + "!\n");
        }
        if (temp.getCardType().equals(Card.cardType.BOMB)) {
            dieOrDefuse(temp);
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

    public LocalPlayerTUI getTui(){
        return tui;
    }
}