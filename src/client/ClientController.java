package client;

import exploding_kittens.model.Card;
import exploding_kittens.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {

    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private Scanner scanner;

    public ClientController(String serverAddress, int serverPort, String name) {
        try {

            socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to the server!");

            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            scanner = new Scanner(System.in);


            new Thread(this::handleServerMessages).start();


            sendMessage("ANNOUNCE|" + name);

            // Example: read user input and send it to the server
            while (true) {
                System.out.print("Enter a message to send to the server: ");
                String userInput = scanner.nextLine();
                sendMessage(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleServerMessages() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals("WELCOME")){
                    System.out.println("Server has responded with WELCOME!");
                }
                else{
                    System.out.println("Received message from server: " + line);
                    handleServerLines(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleServerLines(String line){

    }

    private void sendMessage(String message) {
        try {
            // Send messages to the server
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverAddress = "localhost"; // Replace with the actual server address
        int serverPort = 6744;
        String name = "Kokosas";
        new ClientController(serverAddress, serverPort, name);
    }
}
