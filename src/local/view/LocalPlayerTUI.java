package local.view;

import exploding_kittens.model.Card;
import exploding_kittens.view.PlayerTUI;
import exploding_kittens.model.BackInputException;
import exploding_kittens.model.BooleanReturnException;
import exploding_kittens.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LocalPlayerTUI implements PlayerTUI {


    public LocalPlayerTUI(){}

    @Override
    public void showMessage(String text) {
        System.out.println(text);
    }

    @Override
    public String readInputString() throws BackInputException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            line = br.readLine();
        } catch (IOException e) {
            System.out.println("Error while reading String input");
            return null;
        }
        String CANCEL_MOVE = "b";
        if (line.equals(CANCEL_MOVE)){
            throw new BackInputException("Player has changed his mind.");
        }
        else{
            return line;
        }
    }

    @Override
    public boolean askNope(Card card, Player player, Player other) {
        showMessage(player.getPlayerName() + " is playing " + card.getCardName());
        printHand(other);
        while(true){
            showMessage(other.getPlayerName() + "> Do you want to use your NOPE card?");
            try {
                return readInputBoolean();
            } catch (BooleanReturnException e) {
                showMessage(other.getPlayerName() + "> You cannot go back without making this decision.");
            }
        }
    }

    public int getCardChoice(Player player, Card.cardType type){
        boolean isIndexValid = false;
        int input2 = 0;
        showMessage(player.getPlayerName() + "> Which card? (number between 0 and " + (player.getPlayerHand().getCardsInHand().size() - 1) + ")");
        while(!isIndexValid){
            input2 = readInputInt();
            if (input2 == -10){
                return -10;
            }
            if (input2 == -1){
                continue;
            }
            if (input2 < 0 || input2 >= player.getPlayerHand().getCardsInHand().size()){
                showMessage(player.getPlayerName() + "> Invalid index, choose a number between 0 and" + (player.getPlayerHand().getCardsInHand().size() - 1));
                continue;
            }
            if (player.getPlayerHand().getCardsInHand().get(input2).getCardType().equals(type)){
                return input2;
            }
            else{
                showMessage(player.getPlayerName() + "> Chosen card is not of type " + type);
            }
        }
        return input2;
    }

    public int getAnyCardChoice(Player player) {
        boolean isIndexValid = false;
        int input2 = 0;
        while(!isIndexValid){
            showMessage(player.getPlayerName() + "> Which card would you like to play? (number between 0 and " + (player.getPlayerHand().getCardsInHand().size() - 1) + ")");
            input2 = readInputInt();
            if (input2 == -10){
                return -10;
            }
            if (input2 < 0 || input2 > player.getPlayerHand().getCardsInHand().size()){
                showMessage(player.getPlayerName() + "> Invalid index, choose a number between 0 and" + (player.getPlayerHand().getCardsInHand().size() - 1));
                continue;
            }
            isIndexValid = true;
        }
        if (player.getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.cardType.DEFUSE)){
            showMessage(player.getPlayerName() + "> Defuse card can only be played when a bomb has been drawn.");
            return -1;
        }
        else if(player.getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.cardType.NOPE)){
            showMessage(player.getPlayerName() + "> Nope card cannot be played at the moment.");
            return -1;
        }
        return input2;
    }

    @Override
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

    @Override
    public boolean readInputBoolean() throws BooleanReturnException {
        boolean invalid = true;
        while(invalid){
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

    public void cardsInHandAnimation(int times, Player player){
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
                    String cardName = player.getPlayerHand().getCardsInHand().get(i).getCardName();
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

    @Override
    public void printHand(Player player) {
        showMessage("Player " + player.getPositionIndex() + ": " + player.getPlayerName());
        cardsInHandAnimation(player.getPlayerHand().getCardsInHand().size(), player);
    }
}
