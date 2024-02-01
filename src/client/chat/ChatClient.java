package client.chat;

import exploding_kittens.model.BackInputException;

import java.io.*;
import java.net.*;

public class ChatClient {

    private static String username;

    public ChatClient(){
        username = null;
        askForUsername();
    }

    public void askForUsername(){
        boolean valid = false;
        while(!valid){
            System.out.println("What is your username?");
            String input;
            try {
                input = readInputString();
            }catch (BackInputException e) {
                System.out.println("You cannot go back on this decision.");
                continue;
            }
            if (input.length() <= 20 && !input.isEmpty() && Character.isLetter(input.charAt(0))){
                username = input;
                valid = true;
            }
            else{
                System.out.println("That is not a valid username.\nMust be a string with alphabetical letters and/or numbers. Spaces allowed.\nMaximum 20 characters and minimum 1 character.\nCannot start with a number or a space.");
            }
        }
    }

    public String readInputString() throws BackInputException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            line = br.readLine();
        } catch (IOException e) {
            System.out.println("Error while reading String input");
            return null;
        }
        String CANCEL_MOVE = "b";
        if (line.equals(CANCEL_MOVE)){
            throw new BackInputException("Player has changed his mind.");
        }
        else{
            return line;
        }
    }

    public static void main(String[] args) {

        new ChatClient();

        try {
            Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter clientWriter = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = serverReader.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            String userInput;
            while ((userInput = userInputReader.readLine()) != null) {
                clientWriter.println(username + "> " + userInput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
