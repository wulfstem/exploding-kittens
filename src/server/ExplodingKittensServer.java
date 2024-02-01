package server;

import exploding_kittens.model.Card;
import exploding_kittens.model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ExplodingKittensServer implements Server {


    private int port;
    private int numberOfClients;
    private ArrayList<String> usernames;
    private ServerSocket serverSocket;
    private ServerController controller;

    public ExplodingKittensServer(int numberOfPlayers, int port, ServerController controller){
        this.port = port;
        this.numberOfClients = numberOfPlayers;
        this.controller = controller;
        this.usernames = new ArrayList<>();
    }

    public void establishConnections(){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, controller);
                controller.addClientHandler(clientHandler);

                new Thread(clientHandler).start();
                welcome(clientHandler);

                if (controller.getClientHandlers().size() == numberOfClients) {
                    gameStart();
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    @Override
    public void acceptHandshake(ClientHandler client) {
        // Functionality is done in ClientHandler
    }

    @Override
    public void welcome(ClientHandler client) {
        client.sendMessage(ServerAction.WE.command);
    }

    @Override
    public void gameStart() {
        // Wait for all usernames to be received
        while (usernames.size() < numberOfClients) {
            try {
                Thread.sleep(1000); // Sleep for a short period before checking again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        controller.createGame(new Game(numberOfClients + 1, usernames, controller.getComputerPlayer()));
        broadcastMessage(ServerAction.GS.command);
    }

    @Override
    public void drawCard(ClientHandler client) {

    }

    @Override
    public void diffuseCheck(ClientHandler client) {

    }

    @Override
    public void explode(ClientHandler client) {

    }

    @Override
    public void gameEnd() {
        //Need to close the server
    }

    @Override
    public void gameWinner(ClientHandler client) {

    }

    @Override
    public void informAdmin(String message) {

    }

    @Override
    public void broadcastMessage(String message) {
        for (ClientHandler client: controller.getClientHandlers()){
            client.sendMessage(message);
        }
    }

    @Override
    public void roundStarted() {

    }

    @Override
    public void cardPlayed(Card card) {

    }

    public void addUsername(String username){
        this.usernames.add(username);
    }
}
