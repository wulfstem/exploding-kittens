package client.view;

import exploding_kittens.model.BackInputException;
import exploding_kittens.model.BooleanReturnException;
import exploding_kittens.model.Card;
import exploding_kittens.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClientTUI{

    public synchronized void showMessage(String text) {
        System.out.println(text);
    }

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

    public boolean askNope(Card card, Player player, Player otherPlayer) {
        showMessage(player.getPlayerName() + " is playing " + card.getCardName());
        //printHand(otherPlayer);
        while(true){
            showMessage("Do you want to use your NOPE card?");
            try {
                return readInputBoolean();
            } catch (BooleanReturnException e) {
                showMessage("You cannot go back without making this decision.");
            }
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

    public int getAnyCardChoice(ArrayList<String> cardsInHand) {
        boolean isIndexValid = false;
        int input2 = 0;
        while(!isIndexValid){
            showMessage("Which card would you like to play? (number between 0 and " + (cardsInHand.size() - 1) + ")");
            input2 = readInputInt();
            if (input2 == -10){
                continue;
            }
            if (input2 < 0 || input2 >= cardsInHand.size()){
                showMessage("Invalid index, choose a number between 0 and" + (cardsInHand.size() - 1));
                continue;
            }
            if (cardsInHand.get(input2).equals("DEFUSE")){
                showMessage("Defuse card can only be played when a bomb has been drawn.");
                continue;
            }
            else if(cardsInHand.get(input2).equals("NOPE")){
                showMessage("Nope card cannot be played at the moment.");
                continue;
            }
            isIndexValid = true;
        }
        return input2;
    }

    public int getCardChoice(String type, ArrayList<String> cards) {
        boolean isIndexValid = false;
        int input2 = 0;
        showMessage("Which card? (number between 0 and " + (cards.size() - 1) + ")");
        while(!isIndexValid){
            input2 = readInputInt();
            if (input2 == -10){
                return -10;
            }
            if (input2 == -1){
                continue;
            }
            if (input2 < 0 || input2 >= cards.size()){
                showMessage("Invalid index, choose a number between 0 and" + (cards.size() - 1));
                continue;
            }
            if (cards.get(input2).equals(type)){
                return input2;
            }
            else{
                showMessage("Chosen card is not of type " + type);
            }
        }
        return input2;
    }

    public boolean readInputBoolean() throws BooleanReturnException {
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

    public void printHand(ArrayList<String> cards, int sizeOfDrawPile, int bombs, int turns) {
        showMessage("\nYour turn");
        showMessage("You have to make " + turns + " turns");
        showMessage("Cards left in pile: " + sizeOfDrawPile);
        showMessage("Exploding Kittens left: " + bombs + "\n");
        cardsInHandAnimation(cards);
        showMessage("");
    }

    public void cardsInHandAnimation(ArrayList<String> cards){
        for (int i = 0; i < cards.size(); i++){
            System.out.print("|--------------------|    ");
        }
        System.out.print("\n");
        for (int j = 0; j < 7; j++){
            for (int i = 0; i < cards.size(); i++){
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
                    String cardName = cards.get(i);
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
        for (int i = 0; i < cards.size(); i++){
            System.out.print("|--------------------|    ");
        }
        System.out.print("\n");
    }
}
