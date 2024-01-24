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
    private int positionIndex;

    public Player(String name, Game game, int positionIndex) {
        this.playerName = name;
        this.game = game;
        this.positionIndex = positionIndex;
        playerHand = new Hand(this, game.getDeck());

    }

    public void makeMove() {
        int current = game.getCurrent();
        skipTurn = false;

        System.out.println("Do you want to play a card?");
        boolean answer = readInputBoolean();

        if (answer){
            int index = getAnyCardChoice();
            // Check if any other player wants to play a NOPE card
            if(getGame().validateMove(getPlayerHand().getCardsInHand().get(index), this)){
                getPlayerHand().getCardsInHand().get(index).action(this);
                if (isSkipTurn()) {
                    if (game.getCurrent() == current) {
                        game.setCurrent((game.getCurrent() + 1) % game.getPlayers().size());
                    }
                } else {
                    makeMove();
                }
            }
            else{
                System.out.println("Your card has been cancelled by another player playing a NOPE card.");
                getPlayerHand().getCardsInHand().remove(index);
                makeMove();
            }
        }
        else{
            draw();
            game.setCurrent((game.getCurrent() + 1) % game.getPlayers().size());
        }
    }

    public void draw() {
        ArrayList<Card> pile = getGame().getDeck().getDrawPile();
        Card temp = pile.get(0);
        pile.remove(temp);
        getGame().getDeck().setDrawPile(pile);
        getPlayerHand().getCardsInHand().add(temp);

        if (temp.getCardType().equals(Card.CARD_TYPE.BOMB)) {
            dieOrDefuse(temp);
        }
    }

    public boolean askNope(Card card, Player player){
        boolean result = false;
        System.out.println(player.getPlayerName() + " is playing " + card.getCardName());
        System.out.println(printHand());
        System.out.println("Do you want to use your NOPE card?");
        boolean answer = readInputBoolean();
        if (answer) {
            result = true;
            int index = getCardChoice(Card.CARD_TYPE.NOPE);
            getPlayerHand().getCardsInHand().remove(index);
        }
        return result;
    }

    public void dieOrDefuse(Card bomb) {
        System.out.println("You have drawn an Exploding Kitten!");
        printHand();
        if (handContains(Card.CARD_TYPE.DEFUSE)) {
            System.out.println("Use a Defuse?");
            boolean answer = readInputBoolean();
            if (answer){
                printHand();
                System.out.println("Which card? (index)");
                int input2 = readInputInt();
                getPlayerHand().getCardsInHand().remove(input2);
                System.out.println("In which position would you like to put the Exploding Kitten? (index)");
                int input3 = readInputInt();
                getGame().getDeck().getDrawPile().add(input3, bomb);
                getPlayerHand().getCardsInHand().remove(bomb);
            }
            else{
                System.out.println("Better luck next time Champ!");
                die();
            }
        } else {
            System.out.println("Better luck next time Champ!");
            die();
        }
    }

    public int getCardChoice(Card.CARD_TYPE cardType){
        boolean isIndexValid = false;
        int input2 = 0;
        printHand();
        System.out.println("Which card would you like to play? (number between 0 and " + (getPlayerHand().getCardsInHand().size() - 1) + ")");
        while(!isIndexValid){
            input2 = readInputInt();
            if (input2 <= 0 || input2 > getPlayerHand().getCardsInHand().size()){
                System.out.println("Invalid index, choose a number between 0 and" + (getPlayerHand().getCardsInHand().size() - 1));
            }
            isIndexValid = true;
        }
        if (getPlayerHand().getCardsInHand().get(input2).getCardType().equals(cardType)){
            return input2;
        }
        else{
            System.out.println("Chosen card is not of type " + cardType);
            getCardChoice(cardType);
        }
        return 0;
    }

    public int getAnyCardChoice(){
        boolean isIndexValid = false;
        int input2 = 0;
        printHand();
        System.out.println("Which card would you like to play? (number between 0 and " + (getPlayerHand().getCardsInHand().size() - 1) + ")");
        while(!isIndexValid){
            input2 = readInputInt();
            if (input2 < 0 || input2 > getPlayerHand().getCardsInHand().size()){
                System.out.println("Invalid index, choose a number between 0 and" + (getPlayerHand().getCardsInHand().size() - 1));
            }
            isIndexValid = true;
        }
        if (getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.CARD_TYPE.DEFUSE)){
            System.out.println("Defuse card can only be played when a bomb has been drawn.");

            getAnyCardChoice();
        }
        else if(getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.CARD_TYPE.NOPE)){
            System.out.println("Nope card cannot be played at the moment.");
            getAnyCardChoice();
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

    public String readInputString() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();

        } catch (IOException e) {
            System.out.println("Error while reading String input. Try writing again");
            readInputString();
        }
        return null;
    }

    public int readInputInt() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line = br.readLine();
            return Integer.parseInt(line);
        } catch (Exception e) {
            System.out.println("Error while reading Int input. Try writing number again");
            readInputInt();
        }
        return 0;
    }

    public boolean readInputBoolean(){
        System.out.println("Write 'y' for yes, and write 'n' for no:");
        switch (readInputString()){
            case "y":
                return true;
            case "n":
                return false;
            default:
                System.out.println("Invalid input, try again:");
                readInputBoolean();
        }
        return false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String printHand() {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append(this.getPlayerName()).append("\n");
        for (int i = 0; i < getPlayerHand().getCardsInHand().size(); i++) {
            result.append(getPlayerHand().getCardsInHand().get(i).getCardName()).append(i).append(" | ");
        }
        result.append("\n");
        return result.toString();
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


}