package local.view;

import exploding_kittens.view.PlayerTUI;
import local.model.BackInputException;
import local.model.BooleanReturnException;
import local.model.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class LocalPlayerTUI implements PlayerTUI {


    private Player player;
    private final String CANCEL_MOVE = "b";

    public LocalPlayerTUI(Player player){
        this.player = player;
    }

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
        if (line.equals(CANCEL_MOVE)){
            throw new BackInputException("Player has changed his mind.");
        }
        else{
            return line;
        }
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

    public void printHand() {
        showMessage("Player " + player.getPositionIndex() + ": " + player.getPlayerName());
        cardsInHandAnimation(player.getPlayerHand().getCardsInHand().size());
    }
}
