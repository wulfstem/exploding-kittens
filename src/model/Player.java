package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Player {


    private String playerName;
    private Game game;
    private Hand playerHand;
    private boolean skipTurn;
    private int current;

    public Player(String name, Game game){
        this.playerName = name;
        this.game = game;
        playerHand = new Hand(this, game.getDeck());
    }

    public void makeMove(){
        current = game.getCurrent();
        skipTurn = false;

        System.out.println("Do you want to play a card? ('y' or 'n')");
        String input1 = readInputString();
        switch(input1){
            case "y":
                System.out.println("Which card would you like to play? (provide index starting with 0)");
                int input2 = readInputInt();
                getPlayerHand().getCardsInHand().get(input2).action(this);
                if (isSkipTurn()){
                    // TO BE IMPLEMENTED
                    if (game.getCurrent() == current){
                        game.setCurrent((game.getCurrent() + 1) % game.getPlayers().size());
                    }
                }
                else{
                    makeMove();
                }
                break;
            case "n":
                draw();
                game.setCurrent((game.getCurrent() + 1) % game.getPlayers().size());
                break;
        }
    }

    public void steal(Player player, Player thief){
        System.out.println("Player " + player.getPlayerName() + " choose a card to give as a favor:");
        player.printHand();
        int input1 = readInputInt();
        Card temp = player.getPlayerHand().getCardsInHand().get(input1);
        player.getPlayerHand().getCardsInHand().remove(temp);
        thief.getPlayerHand().getCardsInHand().add(temp);
    }

    public void draw(){
        ArrayList<Card> pile = getGame().getDeck().getDrawPile();
        Card temp = pile.get(0);
        pile.remove(temp);
        getGame().getDeck().setDrawPile(pile);
        getPlayerHand().getCardsInHand().add(temp);

        if(temp.getCardType().equals(Card.CARD_TYPE.BOMB)){
            dieOrDefuse(temp);
        }
    }

    public void dieOrDefuse(Card bomb){
        System.out.println("You have drawn an Exploding Kitten!");
        printHand();
        if (handContains(Card.CARD_TYPE.DEFUSE)){
            System.out.println("Use a Defuse? ('y' or 'n')");
            String input = readInputString();
            switch(input){
                case "n":
                    System.out.println("Better luck next time Champ!");
                    die();
                    break;
                case "y":
                    printHand();
                    System.out.println("Which card? (index)");
                    int input2 = readInputInt();
                    getPlayerHand().getCardsInHand().remove(input2);
                    System.out.println("In which position would you like to put the Exploding Kitten? (index)");
                    int input3 = readInputInt();
                    getGame().getDeck().getDrawPile().add(input3, bomb);
                    getPlayerHand().getCardsInHand().remove(bomb);
                    break;
            }
        }
        else{
            System.out.println("Better luck next time Champ!");
            die();
        }
    }

    public void die(){
        ArrayList<Player> temp = getGame().getPlayers();
        temp.remove(this);
        getGame().setPlayers(temp);
    }

    public boolean handContains(Card.CARD_TYPE cardType){
        for (int i = 0; i < getPlayerHand().getCardsInHand().size(); i++){
            if (getPlayerHand().getCardsInHand().get(i).getCardType().equals(cardType)){
                return true;
            }
        }
        return false;
    }

    public String readInputString(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            return br.readLine();
        } catch(IOException e){
            System.out.println("Error while reading String input.");
            return null;
        }
    }

    public int readInputInt(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            String line = br.readLine();
            return Integer.parseInt(line);
        } catch(IOException e){
            System.out.println("Error while reading Int input.");
            return 0;
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public String printHand(){
        StringBuilder result = new StringBuilder();
        result.append(this.getPlayerName()).append("\n");
        for (int i = 0; i < getPlayerHand().getCardsInHand().size(); i++){
            result.append(getPlayerHand().getCardsInHand().get(i).getCardName()).append(" (").append(getPlayerHand().getCardsInHand().get(i).getCardType()).append(") | ");
        }
        return result.toString();
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
    }

    public Game getGame(){
        return game;
    }

    public boolean isSkipTurn() {
        return skipTurn;
    }

    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }
}
