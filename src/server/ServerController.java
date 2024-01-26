package server;

import exploding_kittens.Controller;
import exploding_kittens.model.Card;
import exploding_kittens.model.Game;
import exploding_kittens.model.Player;
import exploding_kittens.view.PlayerTUI;
import server.model.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerController implements Controller {

    private Game game;
    private ArrayList<String> nicknames;
    private ServerSocket serverSocket;
    private int numberOfClients;
    private int port;
    private List<ClientHandler> clientHandlers;
    private boolean isRunning;

    public ServerController(int numberOfClients, int port) {
        this.port = port;
        this.numberOfClients = numberOfClients;
        this.clientHandlers = new ArrayList<>();
        this.nicknames = new ArrayList<>();
        this.isRunning = true;
        startServer();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is running on port " + port);
            new Thread(this::listenForClients).start();
        } catch (IOException e) {
            System.err.println("Could not start server on port " + port);
            e.printStackTrace();
            return;
        }
    }

    private void listenForClients() {
        while (isRunning) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                if (!isRunning) {
                    break;
                }
                System.err.println("Error accepting client connection");
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing server socket");
            e.printStackTrace();
        }
    }

    @Override
    public void drawCard(Player player) {

    }

    @Override
    public boolean validateByNope(Card card, Player player) {
        return false;
    }

    @Override
    public boolean validateMove(Card card, Player player) {
        return false;
    }

    @Override
    public int getCardChoice(Card.cardType type) {
        return 0;
    }

    @Override
    public void bombDrawn(Card bomb) {

    }

    @Override
    public int getAnyCardChoice() {
        return 0;
    }

    @Override
    public void doTurn(Player player) {

    }

    @Override
    public void declareWinner(Player player) {

    }

    @Override
    public void playOrDraw(Player player) {

    }

    @Override
    public Player getCurrentPlayer() {
        return null;
    }

    @Override
    public PlayerTUI getTui() {
        return null;
    }

    public void addNickname(String nickname){
        nicknames.add(nickname);
    }

    public static void main (String[] args){
        System.out.println( "\nWelcome to Exploding Kittens!" );

        int port = 6744;

        int numberOfPlayers = Integer.parseInt(args[0]);
        new ServerController(numberOfPlayers, port);
    }
}
