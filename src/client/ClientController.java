package client;

import client.view.ClientTUI;
import exploding_kittens.Controller;
import exploding_kittens.model.Card;
import exploding_kittens.model.Player;
import exploding_kittens.view.PlayerTUI;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import static jdk.nashorn.internal.objects.NativeString.substring;

public class ClientController implements Client{


    private String serverAddress;
    private int port;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String username;
    private ClientTUI tui;

    public ClientController(String serverAddress, int port){
        this.serverAddress = serverAddress;
        this.port = port;
        tui = new ClientTUI();
        askForUsername();
        handshake();
        new Thread(new ServerMessageHandler()).start();
    }

    public void askForUsername(){
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        boolean valid = false;
        while(!valid){
            tui.showMessage("What is your username?");
            try {
                String input = read.readLine();
                if (input.length() <= 20 && !input.isEmpty() && Character.isLetter(input.charAt(0))){
                    username = input;
                    valid = true;
                }
                else{
                    tui.showMessage("That is not a valid username.\nMust be a string with alphabetical letters and/or numbers. Spaces allowed.\nMaximum 20 characters and minimum 1 character.\nCannot start with a number or a space.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // client interface methods
    @Override
    public void handshake() {
        try {

            InetAddress address = InetAddress.getByName(serverAddress);

            // Establish a connection to the server
            socket = new Socket(address, port);
            tui.showMessage("Connected to server at " + serverAddress + ":" + port);

            // Set up input and output streams for the socket
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String serverActive = in.readLine();
            tui.showMessage("Received from server: " + serverActive);

            announces(username);
        } catch (IOException e) {
            tui.showMessage("Error connecting to server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleInput(String input){

    }

    public void handleServerMessage(String message){

        String[] inputs = message.split("\\|");

        switch(inputs[0]){
            case "GAME_START":
                requestGameState();
            case "DEAL_CARD":

            case "DIFFUSE_CHECK":

            case "ELIMINATE_CLIENT":

            case "END_GAME":

            case "ANNOUNCE_WINNER":

        }
    }

    /**
     * Helper method to send a message to the server.
     * @param message The message to be sent.
     */
    private void sendMessage(String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }

    @Override
    public void announces(String username) {
        try {
            sendMessage(Command.AN.command + "|" + username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doMovePlayCard(String type, String name) {

    }

    @Override
    public void doMoveEndTurn() {

    }

    @Override
    public void requestGameState() {
        try {
            sendMessage(Command.RS.command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class ServerMessageHandler implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    tui.showMessage("Received from server: " + message);
                    handleServerMessage(message);
                }
            } catch (IOException e) {
                tui.showMessage("Error reading from server: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        String serverAddress = "localhost"; // Replace with the actual server address
        int serverPort = 6744;
        new ClientController(serverAddress, serverPort);
    }
}
