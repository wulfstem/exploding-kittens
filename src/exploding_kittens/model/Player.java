package exploding_kittens.model;

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
    private final String CANCEL_MOVE = "b";

    public Player(String name, Game game, int positionIndex) {
        this.playerName = name;
        this.game = game;
        this.positionIndex = positionIndex;
        playerHand = new Hand(this, game.getDeck());

    }

    public void makeMove() {
        int current = getGame().getCurrent();
        skipTurn = false;

        boolean goBack = true;
        while(goBack){
            goBack = false;
            boolean answer;
            System.out.println("Do you want to play a card?");
            try{
                answer = readInputBoolean();
            }catch (BooleanReturnException e){
                System.out.println("You cannot go back without making this decision.");
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
                    System.out.println("Your card has been cancelled by another player playing a NOPE card.");
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
            System.out.println("\nYou drew " + temp.getCardName() + "!\n");
        }
        if (temp.getCardType().equals(Card.CARD_TYPE.BOMB)) {
            dieOrDefuse(temp);
        }
    }

    public boolean askNope(Card card, Player player){
        boolean result = false;
        System.out.println(player.getPlayerName() + " is playing " + card.getCardName());
        printHand();
        boolean goBack = true;
        while(goBack){
            goBack = false;
            System.out.println("Do you want to use your NOPE card?");
            boolean answer = false;
            try {
                answer = readInputBoolean();
            } catch (BooleanReturnException e) {
                System.out.println("You cannot go back without making this decision.");
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
        System.out.println("You have drawn an Exploding Kitten!");
        printHand();
        if (handContains(Card.CARD_TYPE.DEFUSE)) {
            boolean invalid = true;
            boolean answer = false;
            while(invalid){
                System.out.println("Use a Defuse?");
                try {
                    answer = readInputBoolean();
                    invalid = false;
                } catch (BooleanReturnException e) {
                    System.out.println("You cannot go back without making this decision.");;
                }
            }
            boolean goBack = true;
            while(goBack){
                goBack = false;
                if (answer){
                    System.out.println("Which card? (index)");
                    int index = getCardChoice(Card.CARD_TYPE.DEFUSE);
                    if (index == -10 || index == -1){
                        goBack = true;
                        continue;
                    }
                    getPlayerHand().getCardsInHand().remove(index);
                    System.out.println("In which position would you like to put the Exploding Kitten? (1 between " + getGame().getDeck().getDrawPile().size() + ")");
                    int input3 = readInputInt();
                    if (input3 == -10){
                        goBack = true;
                        continue;
                    }
                    getGame().getDeck().getDrawPile().add(input3, bomb);
                    getPlayerHand().getCardsInHand().remove(bomb);
                }
                else{
                    System.out.println("Better luck next time Champ!");
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
        System.out.println("Which card would you like to play? (number between 0 and " + (getPlayerHand().getCardsInHand().size() - 1) + ")");
        while(!isIndexValid){
            input2 = readInputInt();
            if (input2 == -10){
                return -10;
            }
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
            return -1;
        }
    }

    public int getAnyCardChoice(){
        boolean isIndexValid = false;
        int input2 = 0;
        System.out.println("Which card would you like to play? (number between 0 and " + (getPlayerHand().getCardsInHand().size() - 1) + ")");
        while(!isIndexValid){
            input2 = readInputInt();
            if (input2 == -10){
                return -10;
            }
            if (input2 < 0 || input2 > getPlayerHand().getCardsInHand().size()){
                System.out.println("Invalid index, choose a number between 0 and" + (getPlayerHand().getCardsInHand().size() - 1));
            }
            isIndexValid = true;
        }
        if (getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.CARD_TYPE.DEFUSE)){
            System.out.println("Defuse card can only be played when a bomb has been drawn.");
            return -1;
        }
        else if(getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.CARD_TYPE.NOPE)){
            System.out.println("Nope card cannot be played at the moment.");
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

    public String readInputString() throws BackInputException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            line = br.readLine();
        } catch (IOException e) {
            System.out.println("Error while reading String input");
            return null;
        }
        if (line.equals(CANCEL_MOVE)){
            throw new BackInputException("Player has changed his mind.");
        }
        else{
            return line;
        }
    }

    public int readInputInt() {
        String line;
        try {
            line = readInputString();
        }catch (BackInputException e){
            return -10;
        }
        try {
            return Integer.parseInt(line);
        } catch (Exception e) {
            System.out.println("Input is not a valid number.");
            return -1;
        }
    }

    public boolean readInputBoolean() throws BooleanReturnException{
        boolean invalid = true;
        while(invalid){
            invalid = false;
            System.out.println("Write 'y' for yes, and write 'n' for no:");
            String line;
            try{
                line = readInputString();
            }catch (BackInputException e){
                throw new BooleanReturnException("Player has changed his mind.");
            }
            switch (line){
                case "y":
                    return true;
                case "n":
                    return false;
            }
            System.out.println("Invalid input, decide ('y', 'n', or 'b'");
            invalid = true;
        }
        return false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void printHand() {
        System.out.println("Player " + getPositionIndex() + ": " + this.getPlayerName());
        cardsInHandAnimation(getPlayerHand().getCardsInHand().size());
    }

    public void cardsInHandAnimation(int times){
        for (int i = 0; i < times; i++){
            System.out.print("|--------------------|    ");
        }
        System.out.print("\n");
        for (int j = 0; j < 7; j++){
            for (int i = 0; i < times; i++){
                if (j == 2){
                    System.out.print("|");
                    String number = String.valueOf(i);
                    int totalWidth = 20;

                    int leftPadding = (totalWidth - number.length()) / 2;
                    int rightPadding = totalWidth - number.length() - leftPadding;

                    System.out.printf("%" + leftPadding + "s%s%" + rightPadding + "s", "", number, "");
                    System.out.print("|");
                    System.out.print("    ");
                }
                else if (j == 3){
                    String cardName = getPlayerHand().getCardsInHand().get(i).getCardName();
                    int totalWidth = 22;

                    int leftPadding = (totalWidth - cardName.length()) / 2;
                    int rightPadding = totalWidth - cardName.length() - leftPadding;

                    System.out.printf("%" + leftPadding + "s%s%" + rightPadding + "s", "", cardName, "");
                    System.out.print("    ");
                }
                else{
                    System.out.print("|                    |    ");
                }
            }
            System.out.print("\n");
        }
        for (int i = 0; i < times; i++){
            System.out.print("|--------------------|    ");
        }
        System.out.print("\n");
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