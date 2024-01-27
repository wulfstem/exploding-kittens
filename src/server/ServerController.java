package server;

import exploding_kittens.model.Card;
import exploding_kittens.model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerController implements Server {


    private int port;
    private int numberOfPlayers;
    private ArrayList<String> usernames;
    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers;
    private Game game;

    public ServerController(int numberOfPlayers, int port) {
        this.numberOfPlayers = numberOfPlayers;
        this.port = port;
        this.clientHandlers = new ArrayList<>();
        this.usernames = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            // Continuously listen for new clients
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // Create a new ClientHandler for the connected client
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clientHandlers.add(clientHandler);

                // Start the client handler in a new thread
                new Thread(clientHandler).start();
                welcome(clientHandler);
                acceptHandshake(clientHandler);

                // Check if the required number of players is connected to start the game
                if (clientHandlers.size() == numberOfPlayers) {
                    // Initialize and start the game
                    gameStart();
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Server's interface methods
    @Override
    public void acceptHandshake(ClientHandler client) {
        // not used (moved to clientHandler)
    }

    @Override
    public void welcome(ClientHandler client) {
        client.sendMessage(ServerAction.WE.command);
    }

    @Override
    public void gameStart() {
        // Wait for all usernames to be received
        while (usernames.size() < numberOfPlayers) {
            try {
                Thread.sleep(1000); // Sleep for a short period before checking again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        game = new Game(numberOfPlayers, usernames);
        //game.setController(this);
        game.setup();
        broadcastMessage(ServerAction.GS.command);
        game.play();
    }

    /*
    public String getGameState(){
        return game.provideGameState();
    }

     */


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

    }

    @Override
    public void gameWinner(ClientHandler client) {

    }

    @Override
    public void informAdmin(String message) {

    }

    @Override
    public void broadcastMessage(String message) {
        for (ClientHandler client: clientHandlers){
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
        usernames.add(username);
    }

    public static void main (String[] args){
        System.out.println( "\nWelcome to Exploding Kittens!" );

        int port = 6744;

        int numberOfPlayers = Integer.parseInt(args[0]);
        new ServerController(numberOfPlayers, port);
    }
}
