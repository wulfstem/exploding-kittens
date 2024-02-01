package server;

import java.io.*;
import java.net.*;

public class ChatClientHandler {
    private Socket socket;
    private PrintWriter writer;

    public ChatClientHandler(Socket socket) {
        this.socket = socket;
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}