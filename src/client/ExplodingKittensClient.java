package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ExplodingKittensClient implements Client{


    private ClientController controller;
    private String serverAddress;
    private int port;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ExplodingKittensClient(String serverAddress, int port, ClientController controller){
        this.controller = controller;
        this.serverAddress = serverAddress;
        this.port = port;

        controller.askForUsername();
        handshake();

        new Thread(new ServerMessageHandler()).start();
    }

    public synchronized void handleServerMessage(String message){

        String[] inputs = message.split("\\|");

        switch(inputs[0]){
            case "GAME_START":
                requestGameState();
                break;
            case "GAME_STATE":
                controller.handleGameState(message);
                break;
            case "DEAL_CARD":
                controller.drawCard(inputs[1]);
                break;
            case "DIFFUSE_CHECK":
                controller.checkForDefuse(inputs[1]);
                break;
            case "NOPE":
                controller.askNope(inputs[1], inputs[2]);
                break;
            case "ELIMINATE_CLIENT":

                break;
            case "CANCELLED":
                controller.cardCancelled();
                break;
            case "DEATH":
                controller.announceDeath(inputs[1]);
                break;
            case "VICTORY":
                controller.announceVictory(inputs[1]);
                break;
            case "INDICATE_PLAYER":
                controller.playerChoice();
                break;
            case "FAVOR":
                controller.cardToGiveUp(inputs[1]);
                break;
            case "MATCH":
                controller.matchCard();
                break;
            case "FUTURE":
                controller.showFuture(inputs[1]);
                break;
            case "SEND_DECISION":
                controller.makeDecision();
                break;
        }
    }

    public void closeClient() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Client closed.");
            }
        } catch (IOException e) {
            controller.getTUI().showMessage("Error while closing client: " + e.getMessage());
        }
    }

    @Override
    public void handshake() {
        try {

            InetAddress address = InetAddress.getByName(serverAddress);

            socket = new Socket(address, port);
            controller.getTUI().showMessage("Connected to server at " + serverAddress + ":" + port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String serverActive = in.readLine();
            controller.getTUI().showMessage("Received from server: " + serverActive);

            announces(controller.getUsername());
        } catch (IOException e) {
            controller.getTUI().showMessage("Error connecting to server: " + e.getMessage());
        }
    }

    @Override
    public void announces(String username) {
        sendMessage(Command.AN.command + "|" + username);
    }

    @Override
    public void doMovePlayCard(String type, String name) {

    }

    @Override
    public void doMoveEndTurn() {

    }

    @Override
    public void requestGameState() {
        sendMessage(Command.RS.command);
    }

    public synchronized void sendMessage(String message){
        try {
            System.out.println("sending " + message);
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            controller.getTUI().showMessage("Request could not be sent out.");
        }
    }

    private class ServerMessageHandler implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    controller.getTUI().showMessage("Received from server: " + message);
                    handleServerMessage(message);
                }
            } catch (IOException e) {
                controller.getTUI().showMessage("Error reading from server: " + e.getMessage());
            }
        }
    }
}
