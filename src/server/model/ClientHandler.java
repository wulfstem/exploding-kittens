package server.model;

import exploding_kittens.model.Player;
import server.ServerController;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Player player;
    private Socket clientSocket;
    private ServerController controller;
    private BufferedReader in;
    private BufferedWriter out;

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
                processClientMessage(line);
            }
        } catch (IOException e) {
            // Handle exception
        } finally {
            // Clean up resources
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

    private void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            // Log or handle the exception
        }
    }

    private void handleCommand(String command) {
        String[] parts = command.split("\\|");
        String keyword = parts[0];

        switch (keyword) {
            case "DO_MOVE":
                break;
            case "REQUEST_GAME_STATE":

        }
    }

    private void handleHello(String username) {
        System.out.println("Received ANNOUNCE from " + username);
        controller.addNickname(username);
        sendMessage("WELCOME");
    }

    public void sendMessage(String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
