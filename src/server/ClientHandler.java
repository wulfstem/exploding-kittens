package server;

import server.controller.ServerController;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private ServerController controller;
    private BufferedReader in;
    private BufferedWriter out;
    private String playerName;
    private int playerIndex;
    private String cardBeingPlayed;

    public ClientHandler(Socket clientSocket, ServerController controller) {
        this.clientSocket = clientSocket;
        this.controller = controller;

        try {
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println(playerName + ">" + "Received from " + playerName + ": " + line);

                ExecutorService executor = Executors.newCachedThreadPool();
                String finalLine = line;
                executor.execute(() -> processClientMessage(finalLine));
            }
        } catch (IOException e) {
            controller.disconnection(playerName);
        } finally {
            closeResources();
        }
    }

    private void processClientMessage(String line) {
        if (line.startsWith("ANNOUNCE")) {
            handleHello(line.substring(9));
        } else {
            handleCommand(line);
        }
    }

    public void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.out.println("Resources could not be closed.");
        }
    }

    private void handleCommand(String command) {
        cardBeingPlayed = null;
        String[] parts = command.split("\\|");
        String keyword = parts[0];

        switch (keyword) {
            case "DEFUSE":
                if(parts[1].equals("N")){
                    controller.setDefuseIndex(-10);
                }
                else{
                    controller.setDefuseIndex(Integer.parseInt(parts[1]));
                }
                break;
            case "TARGET":
                controller.setTargetPlayer(Integer.parseInt(parts[1]));
                break;
            case "FAVOR":
                controller.setCardToBeStolen(Integer.parseInt(parts[1]));
                break;
            case "MATCH":
                controller.setMatchingCard(Integer.parseInt(parts[1]));
                break;
            case "DO_MOVE":
                switch(parts[1]){
                    case "PLAY":
                        cardBeingPlayed = parts[3];
                        controller.setDraw(0);
                        break;
                    case "END_TURN":
                        controller.setDraw(1);
                        break;
                }
                break;
            case "REQUEST_GAME_STATE":
                controller.getGameState(this);
                break;
            case "NOPE":
                if(parts[1].equals("T")){
                    if(controller.getIndexOfPlayerPlayingNope() == -1){
                        controller.setIndexOfPlayerPlayingNope(playerIndex);
                    }
                }
                controller.increaseNopeAnswerCounter();
                break;
        }
    }

    private void handleHello(String username) {
        System.out.println("Received ANNOUNCE from " + username);
        this.playerName = username;
        controller.getServer().addUsername(username);
    }

    public void sendMessage(String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            System.out.println("Message could not be sent.");
        }
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public void setPlayerIndex(int number){
        this.playerIndex = number;
    }

    public int getPlayerIndex(){
        return playerIndex;
    }

    public String getCardBeingPlayed() {
        return this.cardBeingPlayed;
    }
}
