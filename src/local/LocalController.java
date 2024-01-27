package local;

import exploding_kittens.Controller;
import exploding_kittens.model.*;
import exploding_kittens.view.PlayerTUI;
import local.view.LocalPlayerTUI;

import java.util.ArrayList;
import java.util.Arrays;

public class LocalController implements Controller {


    private Game game;
    private LocalPlayerTUI tui;

    public LocalController(Game game, LocalPlayerTUI tui){
        this.game = game;
        this.tui = tui;
        tui.setController(this);
        game.setController(this);
        game.setup();
        game.play();
    }

    public void doTurn(Player player){
        tui.showMessage("\nMOVE NUMBER: " + game.getTurnCounter() + "\n" + "Cards left in pile: " + game.getDeck().getDrawPile().size() + "\nBombs left: " + game.getDeck().getNumberOfActiveBombs());
        tui.showMessage("(In some occasions you can go back on your decision typing in 'b', when asked for input.\n");
        tui.printHand();
        for (int i = 0; i < game.getTurns(); i++){
            player.makeMove();
            game.setTurnCounter(game.getTurnCounter() + 1);
        }
        tui.showMessage("---------------------------------------------------------------------------------------------------------------------\n");
    }

    @Override
    public void declareWinner(Player player) {
        tui.showMessage("The winner is:\n" + player.getPlayerName() + "!");
        System.exit(0);
    }

    @Override
    public void playOrDraw(Player player) {
        boolean goBack = true;
        while (goBack) {
            goBack = false;
            boolean answer;
            tui.showMessage("Do you want to play a card?");
            try {
                answer = tui.readInputBoolean();
            } catch (BooleanReturnException e) {
                tui.showMessage("You cannot go back without making this decision.");
                goBack = true;
                continue;
            }
            if (answer) {
                int index = getAnyCardChoice();
                if (index == -10 || index == -1) {
                    goBack = true;
                    continue;
                }
                // Check if any other player wants to play a NOPE card
                if (validateMove(player.getPlayerHand().getCardsInHand().get(index), player)) {
                    player.getPlayerHand().getCardsInHand().get(index).action(player);
                    player.getPlayerHand().getCardsInHand().remove(index);
                    if (game.isSkipTurn()) {
                        if (game.getCurrent() == player.getPositionIndex()) {
                            if (player.getPositionIndex() == (game.getPlayers().size() - 1)) {
                                game.setCurrent(0);
                            } else {
                                game.setCurrent((game.getCurrent() + 1));
                            }
                        }
                    } else {
                        playOrDraw(player);
                    }
                } else {
                    tui.showMessage("Your card has been cancelled by another player playing a NOPE card.");
                    player.getPlayerHand().getCardsInHand().remove(index);
                    playOrDraw(player);
                }
            } else {
                player.draw();
                if (game.getTurns() == 1 && game.getCurrent() == player.getPositionIndex()) {
                    if (player.getPositionIndex() == (game.getPlayers().size() - 1)) {
                        game.setCurrent(0);
                    } else {
                        game.setCurrent((game.getCurrent() + 1));
                    }
                }
            }
        }
    }

    public void drawCard(Player player){
        Card drawn = player.draw();
        tui.showMessage("\nYou drew " + drawn.getCardName() + "!\n");
        if (drawn.getCardType().equals(Card.cardType.BOMB)) {
            bombDrawn(drawn);
        }
    }

    @Override
    public boolean validateByNope(Card card, Player player) {
        boolean result = false;
        tui.showMessage(player.getPlayerName() + " is playing " + card.getCardName());
        tui.printHand();
        boolean goBack = true;
        while(goBack){
            goBack = false;
            tui.showMessage("Do you want to use your NOPE card?");
            boolean answer = false;
            try {
                answer = tui.readInputBoolean();
            } catch (BooleanReturnException e) {
                tui.showMessage("You cannot go back without making this decision.");
                goBack = true;
                continue;
            }
            if (answer) {
                result = true;
                int index = -1;
                while (index == -1){
                    index = getCardChoice(Card.cardType.NOPE);
                    if (index == -10){
                        goBack = true;
                    }
                }
                if (index >= 0){
                    getCurrentPlayer().getPlayerHand().getCardsInHand().remove(index);
                }
            }
        }
        return result;
    }

    @Override
    public void bombDrawn(Card bomb) {
        tui.showMessage("You have drawn an Exploding Kitten!");
        tui.printHand();
        if (getCurrentPlayer().handContains(Card.cardType.DEFUSE)) {
            boolean invalid = true;
            boolean answer = false;
            while(invalid){
                tui.showMessage("Use a Defuse?");
                try {
                    answer = tui.readInputBoolean();
                    invalid = false;
                } catch (BooleanReturnException e) {
                    tui.showMessage("You cannot go back without making this decision.");;
                }
            }
            boolean goBack = true;
            while(goBack){
                goBack = false;
                if (answer){
                    tui.showMessage("Which card? (index)");
                    int index = getCardChoice(Card.cardType.DEFUSE);
                    if (index == -10 || index == -1){
                        goBack = true;
                        continue;
                    }
                    getCurrentPlayer().getPlayerHand().getCardsInHand().remove(index);
                    tui.showMessage("In which position would you like to put the Exploding Kitten? (1 between " + game.getDeck().getDrawPile().size() + ")");
                    int input3 = (tui.readInputInt())  - 1;
                    if (input3 == -10){
                        goBack = true;
                        continue;
                    }
                    game.getDeck().getDrawPile().add(input3, bomb);
                    getCurrentPlayer().getPlayerHand().getCardsInHand().remove(bomb);
                }
                else{
                    tui.showMessage("Better luck next time Champ!");
                    getCurrentPlayer().die();
                }
            }
        } else {
            System.out.println("Better luck next time Champ!");
            getCurrentPlayer().die();
        }
    }

    @Override
    public int getCardChoice(Card.cardType type) {
        boolean isIndexValid = false;
        int input2 = 0;
        tui.showMessage("Which card would you like to play? (number between 0 and " + (getCurrentPlayer().getPlayerHand().getCardsInHand().size() - 1) + ")");
        while(!isIndexValid){
            input2 = tui.readInputInt();
            if (input2 == -10){
                return -10;
            }
            if (input2 <= 0 || input2 > getCurrentPlayer().getPlayerHand().getCardsInHand().size()){
                tui.showMessage("Invalid index, choose a number between 0 and" + (getCurrentPlayer().getPlayerHand().getCardsInHand().size() - 1));
            }
            isIndexValid = true;
        }
        if (getCurrentPlayer().getPlayerHand().getCardsInHand().get(input2).getCardType().equals(type)){
            return input2;
        }
        else{
            tui.showMessage("Chosen card is not of type " + type);
            return -1;
        }
    }

    @Override
    public int getAnyCardChoice() {
        boolean isIndexValid = false;
        int input2 = 0;
        tui.showMessage("Which card would you like to play? (number between 0 and " + (getCurrentPlayer().getPlayerHand().getCardsInHand().size() - 1) + ")");
        while(!isIndexValid){
            input2 = tui.readInputInt();
            if (input2 == -10){
                return -10;
            }
            if (input2 < 0 || input2 > getCurrentPlayer().getPlayerHand().getCardsInHand().size()){
                tui.showMessage("Invalid index, choose a number between 0 and" + (getCurrentPlayer().getPlayerHand().getCardsInHand().size() - 1));
            }
            isIndexValid = true;
        }
        if (getCurrentPlayer().getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.cardType.DEFUSE)){
            tui.showMessage("Defuse card can only be played when a bomb has been drawn.");
            return -1;
        }
        else if(getCurrentPlayer().getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.cardType.NOPE)){
            tui.showMessage("Nope card cannot be played at the moment.");
            return -1;
        }
        return input2;
    }

    @Override
    public boolean validateMove(Card card, Player player){
        boolean result = true;

        for (Player otherPlayer : game.getPlayers()) {
            if (otherPlayer != getCurrentPlayer()) {

                for (Card cardInHand : otherPlayer.getPlayerHand().getCardsInHand()) {
                    if (cardInHand.getCardType().equals(Card.cardType.NOPE)) {
                        if (validateByNope(card, player)) {
                            result = false;
                            return result;
                        }
                    }
                }
            }
        }
        return result;
    }

    public Player getCurrentPlayer(){
        return game.getPlayers().get(game.getCurrent());
    }

    @Override
    public PlayerTUI getTui() {
        return tui;
    }

    public static void main (String[] args){
        System.out.println( "\nWelcome to Exploding Kittens!" );
        int numberOfPlayers = Integer.parseInt(args[0]);
        ArrayList<String> nicknames = new ArrayList<>(Arrays.asList(args).subList(1, numberOfPlayers + 1));
        new LocalController(new Game(numberOfPlayers, nicknames), new LocalPlayerTUI());
    }
}