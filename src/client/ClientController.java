package client;

import client.view.ClientTUI;
import exploding_kittens.model.BackInputException;
import exploding_kittens.model.BooleanReturnException;
import exploding_kittens.view.PlayerTUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClientController{


    private ExplodingKittensClient client;
    private String username;
    private final ClientTUI tui;
    private ArrayList<String> cardsInHand;
    private int sizeOfDrawPile;
    private String current;
    private ArrayList<String> alivePlayers;
    private boolean isCurrent;
    private int numberOfPlayers;
    private HashMap<String, Integer> infoOtherPlayers;
    private boolean ready;

    public ClientController(String serverAddress, int port){
        ready = false;
        alivePlayers = new ArrayList<>();
        isCurrent = false;
        cardsInHand = new ArrayList<>();
        infoOtherPlayers = new HashMap<>();
        tui = new ClientTUI();
        client = new ExplodingKittensClient(serverAddress, port, this);
    }

    public void askForUsername(){
        boolean valid = false;
        while(!valid){
            tui.showMessage("What is your username?");
            String input;
            try {
                input = tui.readInputString();
            }catch (BackInputException e) {
                System.out.println("You cannot go back on this decision.");
                continue;
            }
            if (input.length() <= 20 && !input.isEmpty() && Character.isLetter(input.charAt(0))){
                username = input;
                valid = true;
            }
            else{
                tui.showMessage("That is not a valid username.\nMust be a string with alphabetical letters and/or numbers. Spaces allowed.\nMaximum 20 characters and minimum 1 character.\nCannot start with a number or a space.");
            }
        }
    }

    public void handleGameState(String gameState){
        String[] splitUp = gameState.split("\\|");
        this.numberOfPlayers = Integer.parseInt(splitUp[1]);
        String[] info = splitUp[2].split(",");
        if(!(alivePlayers.size() == numberOfPlayers)){
            alivePlayers = new ArrayList<>();
            for (int i = 0; i < (numberOfPlayers + numberOfPlayers); i += 2){
                alivePlayers.add(info[i]);
                infoOtherPlayers.put(info[i], Integer.parseInt(info[i+1]));
            }
        }
        this.current = splitUp[3];
        this.isCurrent = (current.equals(username));
        this.sizeOfDrawPile = Integer.parseInt(splitUp[4]);
        String[] cards = splitUp[5].split(",");
        this.cardsInHand.addAll(Arrays.asList(cards));
        StringBuilder result = new StringBuilder();
        result.append("\nAlive players: ");
        for (int i = 0; i < alivePlayers.size(); i++){
            if(i == alivePlayers.size() - 1){
                result.append(alivePlayers.get(i)).append("\n");
            }
            else{
                result.append(alivePlayers.get(i)).append(", ");
            }
        }
        tui.showMessage(String.valueOf(result));
        if (isCurrent){
            yourTurn();
        }
        else{
            tui.showMessage("Player " + current + " is making his/her turn.");
        }
    }

    public void drawCard(String card){
        tui.showMessage("You have drawn " + card);
        cardsInHand.add(card);
        endTurn();
    }

    public void yourTurn(){
        tui.printHand(cardsInHand, sizeOfDrawPile, (alivePlayers.size() - 1));
    }
    public void endTurn(){
        tui.showMessage("Your turn is over.");
        tui.showMessage("---------------------------------------------------------------------------------------------------------------------\n");
    }

    public void makeDecision(){
        boolean result = false;
        boolean goBack = true;
        while(goBack){
            goBack = false;
            tui.showMessage("Do you want to play a card?");
            try {
                result = tui.readInputBoolean();
            } catch (BooleanReturnException e) {
                tui.showMessage("You cannot go back without making this decision.");
                goBack = true;
            }
        }
        if (result){
            int index = whichCardIsBeingPlayed();
            System.out.println("Decision yes about to be sent");
            client.sendMessage(Client.Command.PC.command + "|" + cardsInHand.get(index) + index);
        }
        else{
            System.out.println("Decision no about to be sent");
            client.sendMessage(Client.Command.ET.command);
        }
        System.out.println("Decision sent");
    }

    public int whichCardIsBeingPlayed(){
        return tui.getAnyCardChoice(cardsInHand);
    }

    public ClientTUI getTUI(){
        return this.tui;
    }

    public String getUsername(){
        return username;
    }

    public static void main(String[] args) {
        String serverAddress = "localhost"; // Replace with the actual server address
        int serverPort = 6744;
        new ClientController(serverAddress, serverPort);
    }
}
