package client;

public interface Client {
    // methods for handling player input/output and communication with the server

    /**
     * Enum of client commands abbreviations with full command name and descriptions.
     */
    enum Command{
        AN("ANNOUNCE", "Announce presence to the server"),
        PC("DO_MOVE|PLAY|CARD", "Play a card from hand"),
        ET("DO_MOVE|END_TURN", "End the current turn"),
        RS("REQUEST_GAME_STATE", "Request the current game state");

        final String command;
        private final String description;

        Command(String command, String description) {
            this.command = command;
            this.description = description;
        }
    }

    /**
     * Handles the handshake with the server
     */
    void handshake();


    /**
     * Announces the presence of the client to the server
     * @param username: The name the client wishes to use
     */
    void announces(String username);

    /**
     * Plays a card from the client's hand
     * @param type: The type of the card
     * @param name: The name of the card
     */
    void doMovePlayCard(String type, String name);

    /**
     * Ends the client's turn
     */
    void doMoveEndTurn();

    /**
     * Requests the current game state from the server
     */
    void requestGameState();
}
