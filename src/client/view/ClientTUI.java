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

    // Prints text to the console
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

    // Reads input integer and returns -1 or -10 if input is invalid to be handled with
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

    // Asks a player to choose a card of any type, except if it is DEFUSE or NOPE, which cannot be played by themselves
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

    // Asks a player to give index of the card of a specific required type
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

    // Reads input boolean and also includes handling input 'b' if the player wants to go back on his decision
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
            System.out.println("Invalid input, decide ('y' or 'n'");
            invalid = true;
        }
        return false;
    }

    // prints the hand of a player
    public void printHand(ArrayList<String> cards, int sizeOfDrawPile, int bombs, int turns) {
        showMessage("\nYour turn");
        showMessage("You have to make " + turns + " turns");
        showMessage("Cards left in pile: " + sizeOfDrawPile);
        showMessage("Exploding Kittens left: " + bombs + "\n");
        cardsInHandAnimation(cards);
        showMessage("");
    }

    // Custom animation that presents the cards nicely
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
