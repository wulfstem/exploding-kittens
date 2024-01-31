package server;

import exploding_kittens.Controller;
import exploding_kittens.model.Card;
import exploding_kittens.model.Game;
import exploding_kittens.model.Player;

import java.util.ArrayList;

public class ServerController implements Controller {


    private ExplodingKittensServer server;
    private ArrayList<ClientHandler> clientHandlers;
    private volatile int draw;
    private Game game;
    private int readyCounter;

    public ServerController(int numberOfPlayers, int port) {
        readyCounter = 0;
        this.clientHandlers = new ArrayList<>();
        this.server = new ExplodingKittensServer(numberOfPlayers, port, this);
        this.clientHandlers = new ArrayList<>();
        server.establishConnections();
    }

    @Override
    public boolean validateByNope(Card card, Player player, Player otherPlayer) {
        return false;
    }

    @Override
    public boolean validateMove(Card card, Player player) {
        return false;
    }

    @Override
    public void bombDrawn(Player player, Card bomb) {

    }

    @Override
    public void showHand(Player player) {
        //Not needed in network gameplay. Taken care of this functionality by ClientController
    }

    @Override
    public void moveCanceled(Player player) {

    }

    @Override
    public int getCardForFavor(Player player) {
        return 0;
    }

    @Override
    public void showFuture(Player player) {

    }

    @Override
    public void informStolenCard(Player player, Card card) {

    }

    @Override
    public int getMatchingCard(Player player, Card card) {
        return 0;
    }

    @Override
    public void draw(Player player) {
        ArrayList<Card> pile = game.getDeck().getDrawPile();
        Card temp = pile.get(0);
        pile.remove(temp);
        ArrayList<Card> pile2 = game.getDeck().getDiscardPile();
        pile2.add(temp);
        game.getDeck().setDrawPile(pile);
        game.getDeck().setDiscardPile(pile2);
        player.getPlayerHand().add(temp);
        getCurrentClientHandler().sendMessage(Server.ServerAction.DC.command +  "|" + temp.getCardName());
        if (temp.getCardType().equals(Card.cardType.BOMB)) {
            bombDrawn(player, temp);
        }
        if (game.getTurns() != 1){
            game.setTurns(game.getTurns() - 1);
        }
    }

    @Override
    public int getOtherPlayerChoice(Player player) {
        return 0;
    }

    @Override
    public boolean isCardBeingPlayed() {
        getCurrentClientHandler().sendMessage("SEND_DECISION");
        while(draw == -1){
            System.out.println(draw);
            try {
                wait(1000);
            } catch (InterruptedException e) {
                System.out.println("Error in wait");
            }
        }
        System.out.println(draw);
        return (draw == 0);
    }

    @Override
    public int whichCardIsPlayed() {
        String card = getCurrentClientHandler().getCardBeingPlayed();
        return (Integer.parseInt(card.substring(card.lastIndexOf(card))));
    }

    @Override
    public void doTurn(Player player, int turns) {
        System.out.println("Starting doTurn for player " + player.getPlayerName() + " with turns: " + turns);
        draw = -1;
        game.setAttack(false);
        for (int i = turns; i > 0; i--){
            player.makeMove();
            if (game.isAttack()){
                break;
            }
            game.setTurnCounter(game.getTurnCounter() + 1);
            for (ClientHandler clientHandler: clientHandlers){
                getGameState(clientHandler);
            }
        }
    }

    @Override
    public void declareWinner(Player player) {
        server.broadcastMessage("Player " + player.getPlayerName() + " has won the game!");
        for (ClientHandler clientHandler: clientHandlers){
            clientHandler.closeResources();
        }
        server.gameEnd();
    }

    @Override
    public Player getCurrentPlayer() {
        return game.getPlayers().get(game.getCurrent());
    }

    public synchronized void getGameState(ClientHandler client){
        StringBuilder result = new StringBuilder();
        result.append("GAME_STATE|");
        result.append(game.getPlayers().size()).append("|");
        for (int i = 0; i < game.getPlayers().size(); i++){
            if (i == game.getPlayers().size() - 1){
                result.append(game.getPlayers().get(i).getPlayerName()).append(",").append((game.getPlayers().get(i).getPlayerHand().getCardsInHand().size())).append("|");
            }
            else{
                result.append(game.getPlayers().get(i).getPlayerName()).append(",").append((game.getPlayers().get(i).getPlayerHand().getCardsInHand().size())).append(",");
            }
        }
        result.append(game.getPlayers().get(game.getCurrent()).getPlayerName()).append("|");
        /*
        for (int i = 0; i < game.getDeck().getDiscardPile().size(); i++){
            if (i == game.getDeck().getDiscardPile().size() - 1){
                result.append(game.getDeck().getDiscardPile().get(i).getCardName());
            }
            else{
                result.append(game.getDeck().getDiscardPile().get(i).getCardName()).append(",");
            }
        }
        result.append("|");
        for (int i = 0; i < game.getDeck().getDrawPile().size(); i++){
            if (i == game.getDeck().getDrawPile().size() - 1){
                result.append(game.getDeck().getDrawPile().get(i).getCardName());
            }
            else{
                result.append(game.getDeck().getDrawPile().get(i).getCardName()).append(",");
            }
        }
        result.append("|");
         */
        result.append(game.getDeck().getDrawPile().size()).append("|");
        for (Player player : game.getPlayers()){
            if (player.getPlayerName().equals(client.getPlayerName())){
                for (int i = 0; i < player.getPlayerHand().getCardsInHand().size(); i++){
                    if (i == player.getPlayerHand().getCardsInHand().size() - 1){
                        result.append(player.getPlayerHand().getCardsInHand().get(i).getCardName()).append("|");
                    }
                    else{
                        result.append(player.getPlayerHand().getCardsInHand().get(i).getCardName()).append(",");
                    }
                }
            }
        }
        client.sendMessage(String.valueOf(result));
        readyCounter++;
        if (readyCounter == game.getPlayers().size()){
            startGame();
        }
    }

    public void addClientHandler(ClientHandler clientHandler){
        this.clientHandlers.add(clientHandler);
    }

    public ArrayList<ClientHandler> getClientHandlers(){
        return this.clientHandlers;
    }

    public void createGame(Game game){
        this.game = game;
        this.game.setController(this);
        this.game.setup();
    }

    @Override
    public void startGame(){
        game.play();
    }

    public void setDraw(int number){
        this.draw = number;
    }

    public ExplodingKittensServer getServer(){
        return this.server;
    }

    public ClientHandler getCurrentClientHandler(){
        for (ClientHandler clientHandler: clientHandlers){
            if (clientHandler.getPlayerName().equals(getCurrentPlayer().getPlayerName())){
                return clientHandler;
            }
        }
        return null;
    }

    public static void main (String[] args){
        System.out.println( "\nWelcome to Exploding Kittens!" );

        int port = 6744;

        int numberOfPlayers = Integer.parseInt(args[0]);
        new ServerController(numberOfPlayers, port);
    }
}
