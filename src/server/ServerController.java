package server;

import exploding_kittens.Controller;
import exploding_kittens.model.*;

import java.util.ArrayList;

public class ServerController implements Controller {


    private ExplodingKittensServer server;
    private ArrayList<ClientHandler> clientHandlers;
    private volatile int draw;
    private volatile int defuseIndex;
    private volatile int indexOfPlayerPlayingNope;
    private volatile int nopeAnswerCounter;
    private volatile int targetPlayer;
    private volatile int cardToBeStolen;
    private volatile int matchingCard;
    private  int expectedNopeAnswer;
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
        // Not used by network gameplay
        return false;
    }

    @Override
    public boolean validateMove(Card card, Player player) {
        ArrayList<String> playersWithNope = new ArrayList<>();
        for (Player otherPlayer : game.getPlayers()) {
            if (otherPlayer != player) { // Check other players, not the current player
                if (otherPlayer.handContains(Card.cardType.NOPE)) {
                    playersWithNope.add(otherPlayer.getPlayerName());
                }
            }
        }
        if (!playersWithNope.isEmpty()){
            indexOfPlayerPlayingNope = -1;
            expectedNopeAnswer = 0;
            nopeAnswerCounter = 0;
            for (ClientHandler clientHandler: clientHandlers){
                if (playersWithNope.contains(clientHandler.getPlayerName())){
                    clientHandler.sendMessage("NOPE|" + player.getPlayerName() + "|" + card.getCardName());
                    expectedNopeAnswer++;
                }
            }
            while(nopeAnswerCounter != expectedNopeAnswer){
                System.out.println(indexOfPlayerPlayingNope);
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    System.out.println("Error in wait");
                }
            }
            if(indexOfPlayerPlayingNope != -1){
                Player playerOfNope = game.getPlayers().get(indexOfPlayerPlayingNope);
                Card nopeCard = null;
                for (Card cardN: playerOfNope.getPlayerHand().getCardsInHand()){
                    if (cardN.getCardType().equals(Card.cardType.NOPE)){
                        nopeCard = cardN;
                        break;
                    }
                }
                System.out.println(playerOfNope.getPlayerName() + " is playing NOPE");
                playerOfNope.getPlayerHand().getCardsInHand().remove(nopeCard);
                // IMPLEMENT SO THAT EVERYONE KNOWS WHO CANCELLED AND ETC.
                if(validateMove(nopeCard, playerOfNope)){
                    return false;
                }
                else{
                    moveCanceled(playerOfNope);
                    return true;
                }
            }
            else{
                return true;
            }
        }
        else{
            return true;
        }
    }

    @Override
    public void draw(Player player) {
        ArrayList<Card> pile = game.getDeck().getDrawPile();
        Card temp = pile.get(0);
        pile.remove(temp);
        game.getDeck().setDrawPile(pile);
        player.getPlayerHand().add(temp);
        if (temp.getCardType().equals(Card.cardType.BOMB)) {
            bombDrawn(player, temp);
        }
        else{
            getCurrentClientHandler().sendMessage(Server.ServerAction.DC.command +  "|" + temp.getCardName());
        }
        if (game.getTurns() == 1 && game.getCurrent() == player.getPositionIndex()) {
            if (player.getPositionIndex() == (game.getPlayers().size() - 1)) {
                game.setCurrent(0);
            } else {
                game.setCurrent((game.getCurrent() + 1));
            }
        }
        if (game.getTurns() != 1){
            game.setTurns(game.getTurns() - 1);
        }
    }

    @Override
    public void bombDrawn(Player player, Card bomb) {
        defuseIndex = -1;
        if (player.handContains(Card.cardType.DEFUSE)){
            getCurrentClientHandler().sendMessage("DIFFUSE_CHECK|T");
            while(defuseIndex == -1){
                System.out.println(defuseIndex);
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                    System.out.println("Error in wait");
                }
            }
            System.out.println(defuseIndex);
            if (defuseIndex == -10){
                player.die();
                getCurrentClientHandler().closeResources();
            }
            else{
                player.getPlayerHand().remove(bomb);
                ArrayList<Card> pile = game.getDeck().getDrawPile();
                pile.add(defuseIndex, bomb);
                game.getDeck().setDrawPile(pile);
                for (Card card: player.getPlayerHand().getCardsInHand()){
                    if (card.getCardType().equals(Card.cardType.DEFUSE)){
                        player.getPlayerHand().remove(card);
                        return;
                    }
                }
            }
        }
        else{
            getCurrentClientHandler().sendMessage("DIFFUSE_CHECK|F");
            player.die();
            getCurrentClientHandler().closeResources();
        }
    }

    @Override
    public void showHand(Player player) {
        //Not needed in network gameplay. Taken care of this functionality by ClientController
    }

    @Override
    public void moveCanceled(Player player) {
        for (ClientHandler clientHandler: clientHandlers){
            if (clientHandler.getPlayerName().equals(player.getPlayerName())){
                clientHandler.sendMessage("CANCELLED");
            }
        }
    }

    @Override
    public int getCardForFavor(Player victim) {
        for(ClientHandler clientHandler: clientHandlers){
            if (clientHandler.getPlayerName().equals(victim.getPlayerName())){
                clientHandler.sendMessage("FAVOR|" + getCurrentPlayer().getPlayerName());
            }
        }
        cardToBeStolen = -1;
        while(cardToBeStolen == -1){
            try {
                wait(1000);
            } catch (InterruptedException e) {
                System.out.println("Error in wait");
            }
        }
        return cardToBeStolen;
    }

    @Override
    public void showFuture(Player player) {
        StringBuilder result = new StringBuilder();
        result.append("FUTURE|");
        if (player.getGame().getDeck().getDrawPile().size() >= 3){
            for (int i = 0; i < SeeCard.VISION_OF_DRAW_PILE; i++){
                if(i == 2){
                    result.append(player.getGame().getDeck().getDrawPile().get(i).getCardName());
                }
                else{
                    result.append(player.getGame().getDeck().getDrawPile().get(i).getCardName()).append(",");
                }
            }
        }
        else{
            for (int i = 0; i < player.getGame().getDeck().getDrawPile().size(); i++){
                if(i == player.getGame().getDeck().getDrawPile().size() - 1){
                    result.append(player.getGame().getDeck().getDrawPile().get(i).getCardName());
                }
                else{
                    result.append(player.getGame().getDeck().getDrawPile().get(i).getCardName()).append(",");
                }
            }
        }

        for(ClientHandler clientHandler: clientHandlers){
            if (clientHandler.getPlayerName().equals(player.getPlayerName())){
                clientHandler.sendMessage(String.valueOf(result));
            }
        }
    }

    @Override
    public void informStolenCard(Player player, Card card) {

    }

    @Override
    public int getMatchingCard(Player thief, Card card) {
        for(ClientHandler clientHandler: clientHandlers){
            if (clientHandler.getPlayerName().equals(thief.getPlayerName())){
                clientHandler.sendMessage("MATCH");
            }
        }
        matchingCard = -1;
        while(matchingCard == -1){
            try {
                wait(1000);
            } catch (InterruptedException e) {
                System.out.println("Error in wait");
            }
        }
        return matchingCard;
    }

    @Override
    public int getOtherPlayerChoice(Player player) {
        targetPlayer = -1;
        getCurrentClientHandler().sendMessage("INDICATE_PLAYER");

        while(targetPlayer == -1){
            System.out.println(targetPlayer);
            try {
                wait(1000);
            } catch (InterruptedException e) {
                System.out.println("Error in wait");
            }
        }
        return targetPlayer;
    }

    @Override
    public boolean isCardBeingPlayed() {
        draw = -1;
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
        System.out.println(card + " is being played");
        char lastChar = card.charAt(card.length() - 1);
        System.out.println(Integer.parseInt(String.valueOf(lastChar)));
        return (Integer.parseInt(String.valueOf(lastChar)));
    }

    @Override
    public void doTurn(Player player, int turns) {
        updateHandlersIndex();
        for (ClientHandler clientHandler: clientHandlers){
            getGameState(clientHandler);
        }
        System.out.println("Starting doTurn for player " + player.getPlayerName() + " with turns: " + turns);
        draw = -1;
        game.setAttack(false);
        for (int i = turns; i > 0; i--){
            System.out.println("Current player " + game.getPlayers().get(game.getCurrent()).getPlayerName());
            player.makeMove();
            game.setTurnCounter(game.getTurnCounter() + 1);
            if(turns > 1){
                for (ClientHandler clientHandler: clientHandlers){
                    getGameState(clientHandler);
                }
            }
            if (game.isAttack()){
                break;
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
        result.append(client.getPlayerIndex()).append("|");
        result.append(game.getTurns()).append("|");
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

    public void setDefuseIndex(int number){
        this.defuseIndex = number;
    }

    public synchronized void setIndexOfPlayerPlayingNope(int number){
        this.indexOfPlayerPlayingNope = number;
    }

    public synchronized int getIndexOfPlayerPlayingNope(){
        return indexOfPlayerPlayingNope;
    }

    public synchronized void increaseNopeAnswerCounter(){
        this.nopeAnswerCounter++;
    }

    public void setTargetPlayer(int number){
        this.targetPlayer = number;
    }
    public void setCardToBeStolen(int number){
        this.cardToBeStolen = number;
    }

    public void setMatchingCard(int number){
        this.matchingCard = number;
    }

    public ExplodingKittensServer getServer(){
        return this.server;
    }

    public void updateHandlersIndex(){
        for (ClientHandler clientHandler: clientHandlers){
            for (Player player: game.getPlayers()){
                if (player.getPlayerName().equals(clientHandler.getPlayerName())){
                    clientHandler.setPlayerIndex(game.getPlayers().indexOf(player));
                }
            }
        }
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
