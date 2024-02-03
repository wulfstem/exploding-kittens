package client.controller;

import client.Client;
import client.ExplodingKittensClient;
import client.view.ClientTUI;
import exploding_kittens.model.BackInputException;
import exploding_kittens.model.BooleanReturnException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    private int playerIndex;
    private int turns;
    private String playedRegular;

    public ClientController(String serverAddress, int port){
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

    public void showFuture(String cards){
        tui.showMessage("The next cards in the draw pile are:");
        String[] splitUp = cards.split(",");
        for (int i = 0; i < splitUp.length; i++){
            tui.showMessage(splitUp[i]);
        }
    }

    public void matchCard(){
        int result = 0;
        tui.showMessage("Choose a duplicate card in your hand:");
        boolean valid = false;
        while(!valid){
            valid = true;
            result = tui.getCardChoice(playedRegular, cardsInHand);
            if (result == -10){
                tui.showMessage("You cannot go back on this decision.");
                valid = false;
            }
        }
        client.sendMessage("MATCH|" + result);
        cardsInHand.remove(result);
        cardsInHand.remove(playedRegular);
    }

    public void announceVictory(String line){
        tui.showMessage(line);
        client.closeClient();
    }

    public void handleGameState(String gameState){
        String[] splitUp = gameState.split("\\|");
        this.playerIndex = Integer.parseInt(splitUp[1]);
        this.turns = Integer.parseInt(splitUp[2]);
        this.numberOfPlayers = Integer.parseInt(splitUp[3]);
        String[] info = splitUp[4].split(",");
        alivePlayers = new ArrayList<>();
        for (int i = 0; i < (numberOfPlayers + numberOfPlayers); i += 2){
            alivePlayers.add(info[i]);
            infoOtherPlayers.put(info[i], Integer.parseInt(info[i+1]));
        }
        this.current = splitUp[5];
        this.isCurrent = (current.equals(username));
        this.sizeOfDrawPile = Integer.parseInt(splitUp[6]);
        String[] cards = splitUp[7].split(",");
        this.cardsInHand = new ArrayList<>();
        this.cardsInHand.addAll(Arrays.asList(cards));
        StringBuilder result = new StringBuilder();
        result.append("\nAlive players: ");
        for (int i = 0; i < alivePlayers.size(); i++){
            if(i == alivePlayers.size() - 1){
                result.append("(index ").append(i).append(") ").append(alivePlayers.get(i)).append(" with ").append(infoOtherPlayers.get(alivePlayers.get(i))).append(" cards").append("\n");
            }
            else{
                result.append("(index ").append(i).append(") ").append(alivePlayers.get(i)).append(" with ").append(infoOtherPlayers.get(alivePlayers.get(i))).append(" cards").append(", ");
            }
        }
        tui.showMessage(String.valueOf(result));
        if (!isCurrent){
            tui.showMessage("Player " + current + " is making his/her turn.");
        }
    }

    public void drawCard(String card){
        tui.showMessage("You have drawn " + card);
        cardsInHand.add(card);
        endTurn();
    }

    public void playerChoice(){
        int index = 0;
        boolean goBack = true;
        while(goBack){
            goBack = false;
            tui.showMessage("Which player? (index of other player)");
            index = tui.readInputInt();
            if (index == -10 || index == -1) {
                tui.showMessage("You cannot go back on this decision.");
                goBack = true;
            }
            if (index == playerIndex){
                boolean valid = false;
                while(!valid){
                    tui.showMessage("You cannot pick yourself. Which player? (index of other player)");
                    index = tui.readInputInt();
                    if(index != playerIndex){
                        valid = true;
                    }
                }
            }
            if(index < 0 || index >= alivePlayers.size()){
                tui.showMessage("That is not a valid index. Choose an index between 0 and " + (alivePlayers.size() -1));
                goBack = true;
            }
        }
        client.sendMessage("TARGET|" + index);
    }

    public void cardToGiveUp(String thief){
        int result = 0;
        tui.printHand(cardsInHand, sizeOfDrawPile, numberOfPlayers - 1, turns);
        tui.showMessage("Choose a card to give as a favor to player " + thief + ":");
        boolean goBack = true;
        while(goBack){
            goBack = false;
            result = tui.readInputInt();
            if (result == -10 || result == -1){
                tui.showMessage("You cannot back out of this.");
                goBack = true;
            }
            if (result < 0 || result >= cardsInHand.size()){
                tui.showMessage("Invalid input. Chose a card to give as a favor:");
                goBack = true;
            }
        }
        client.sendMessage("FAVOR|" + result);
        cardsInHand.remove(result);
    }

    public void askNope(String player, String card){
        tui.showMessage(player + " is playing " + card);
        tui.printHand(cardsInHand, sizeOfDrawPile, numberOfPlayers - 1, turns);
        boolean answer;
        while(true){
            tui.showMessage("Do you want to use your NOPE card?");
            try {
                answer = tui.readInputBoolean();
                break;
            } catch (BooleanReturnException e) {
                tui.showMessage("You cannot go back without making this decision.");
            }
        }
        if(answer){
            client.sendMessage("NOPE|T");
            cardsInHand.remove("NOPE");
        }
        else{
            client.sendMessage("NOPE|N");
        }
    }

    public void cardCancelled(){
        tui.showMessage("Your card has been cancelled.");
    }

    public void checkForDefuse(String sym){
        if (sym.equals("T")) {
            tui.showMessage("You have drawn an Exploding Kitten!");
            cardsInHand.add("BOMB");
            tui.printHand(cardsInHand, sizeOfDrawPile - 1, numberOfPlayers - 2, turns);
            boolean invalid = true;
            boolean answer = false;
            while (invalid) {
                tui.showMessage("Use a Defuse?");
                try {
                    answer = tui.readInputBoolean();
                    invalid = false;
                } catch (BooleanReturnException e) {
                    tui.showMessage("You cannot go back without making this decision.");
                }
            }
            boolean goBack = true;
            while (goBack) {
                goBack = false;
                if (answer) {
                    int index = tui.getCardChoice("DEFUSE", cardsInHand);
                    cardsInHand.remove(index);
                    tui.showMessage("In which position would you like to put the Exploding Kitten? (1 between " + (sizeOfDrawPile - 1) + ")");
                    int input3 = (tui.readInputInt()) - 1;
                    if (input3 == -10) {
                        goBack = true;
                        continue;
                    }
                    client.sendMessage("DEFUSE|" + input3);
                    cardsInHand.remove("BOMB");
                } else {
                    client.sendMessage("DEFUSE|N");
                    tui.showMessage("Better luck next time Champ!");
                    //close client
                }
            }
        }
        else{
            tui.showMessage("You have drawn an Exploding Kitten!");
            System.out.println("Better luck next time Champ!");
            //close client
        }
        endTurn();
    }

    public void announceDeath(String name){
        tui.showMessage("Player " + name + " has exploded!");
        alivePlayers.remove(name);
    }

    public void endTurn(){
        tui.showMessage("Your turn is over.");
        tui.showMessage("---------------------------------------------------------------------------------------------------------------------\n");
    }

    public void makeDecision(){
        tui.printHand(cardsInHand, sizeOfDrawPile, numberOfPlayers-1, turns);
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
            if(cardsInHand.get(index).equals("Beard Cat") || cardsInHand.get(index).equals("Cattermelon") ||  cardsInHand.get(index).equals("Hairy Potato Cat") || cardsInHand.get(index).equals("Rainbow-Ralphing Cat") || cardsInHand.get(index).equals("Tacocat")){
                playedRegular = cardsInHand.get(index);
            }
            client.sendMessage(Client.Command.PC.command + "|" + cardsInHand.get(index) + index);
            if(!(cardsInHand.get(index).equals("Beard Cat") || cardsInHand.get(index).equals("Cattermelon") ||  cardsInHand.get(index).equals("Hairy Potato Cat") || cardsInHand.get(index).equals("Rainbow-Ralphing Cat") || cardsInHand.get(index).equals("Tacocat"))){
                cardsInHand.remove(index);
            }
        }
        else{
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
        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        new ClientController(serverAddress, serverPort);
    }
}
