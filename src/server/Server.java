package server;

import exploding_kittens.model.Card;

// methods for server side operations
public interface Server {
    enum Error {
        ALREADY_LOGGED_IN(1, "Client is already logged in"),
        INVALID_USERNAME(2, "The username is invalid"),
        INVALID_MOVE(3, "The move is not allowed"),
        DISCONNECTED_USER(4, "The user has been disconnected");

        private final int code;
        private final String description;

        /**
         * @param code: The error code identifier
         * @param description: The error description
         */
        Error(int code, String description){
            this.code = code;
            this.description = description;
        }
    }
    /**
     * Enum of server commands abbreviations with full command name and descriptions.
     */
    enum ServerAction{
        WE("WELCOME", "Sends welcome message to client"),
        GS("GAME_START", "Starts the game"),
        DC("DEAL_CARD", "Deals card to client"),
        DF("DIFFUSE_CHECK", "Checks if client has a diffuse card"),
        EC("ELIMINATE_CLIENT", "Eliminates a client from the game"),
        EG("END_GAME", "Ends the current game"),
        AW("ANNOUNCE_WINNER", "Announces the game winner");

        final String command;
        private final String description;

        ServerAction(String command, String description) {
            this.command = command;
            this.description = description;
        }
    }

    /**
     * Accepts handshake from the client
     * @param client: the client who initializes the handshake
     */
    void acceptHandshake(ClientHandler client);

    /**
     * Sends a welcome message to the client
     */
    void welcome(ClientHandler client);

    /**
     * Starts the game once there are enough players
     */
    void gameStart();

    /**
     * Deals the top card to the client
     */
    void drawCard(ClientHandler client);

    /**
     * Checks if the client has a diffuse card
     */
    void diffuseCheck(ClientHandler client);

    /**
     * Eliminates the client from the game
     */
    void explode(ClientHandler client);

    /**
     * Ends the game
     */
    void gameEnd();

    /**
     * Determines and announces the winner of the game
     */
    void gameWinner(ClientHandler client);

    /**
     * Informs an admin about an event
     * @param message: the message to send the admin
     */
    void informAdmin(String message);

    /**
     * Sends a global message to all clients
     */
    void broadcastMessage(String message);

    /**
     * Notifies that a new round has started
     */
    void roundStarted();

    /**
     * Notifies that a card has been played
     * @param card: the card that has been played
     */
    void cardPlayed(Card card);
}
