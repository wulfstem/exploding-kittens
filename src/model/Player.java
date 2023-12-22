package model;

public class Player {


    private String playerName;
    private Hand playerHand;

    public Player(String name){
        this.playerName = name;
    }

    public void setPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }
}
