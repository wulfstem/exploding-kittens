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

    public void showHand(Player player){
        tui.showMessage("\nMOVE NUMBER: " + game.getTurnCounter() + "\n" + "Cards left in pile: " + game.getDeck().getDrawPile().size() + "\nBombs left: " + game.getDeck().getNumberOfActiveBombs());
        tui.showMessage("(In some occasions you can go back on your decision typing in 'b', when asked for input.\n");
        tui.printHand(player);
    }

    public boolean isCardBeingPlayed(){
        boolean result = false;
        boolean goBack = true;
        while(goBack){
            goBack = false;
            tui.showMessage("Do you want to play a card?");
            try {
                result = tui.readInputBoolean();
            } catch (BooleanReturnException e) {
                tui.showMessage("You cannot go back without making this decision.");
                goBack = true;
                continue;
            }
        }
        return result;
    }

    public void moveCanceled(Player player){
        tui.showMessage("Your card has been cancelled by another player playing a NOPE card.");
    }

    @Override
    public void draw(Player player){
        ArrayList<Card> pile = game.getDeck().getDrawPile();
        Card temp = pile.get(0);
        pile.remove(temp);
        ArrayList<Card> pile2 = game.getDeck().getDiscardPile();
        pile2.add(temp);
        game.getDeck().setDrawPile(pile);
        game.getDeck().setDiscardPile(pile2);
        player.getPlayerHand().getCardsInHand().add(temp);
        tui.showMessage("You have drawn " + temp.getCardName());
        if (temp.getCardType().equals(Card.cardType.BOMB)) {
            bombDrawn(player, temp);
        }
    }

    public int whichCardIsPlayed(){
        int result = 0;
        boolean goBack = true;
        while(goBack){
            goBack = false;
            result = tui.getAnyCardChoice(getCurrentPlayer());
            if (result == -10 || result == -1) {
                goBack = true;
                continue;
            }
        }
        return result;
    }

    @Override
    public boolean validateByNope(Card card, Player player) {
        boolean result = false;
        result = tui.askNope(card, player);
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
                    index = tui.getCardChoice(player, Card.cardType.NOPE);
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
    public void bombDrawn(Player player, Card bomb) {
        tui.showMessage("You have drawn an Exploding Kitten!");
        tui.printHand(player);
        if (player.handContains(Card.cardType.DEFUSE)) {
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
                    int index = tui.getCardChoice(player, Card.cardType.DEFUSE);
                    if (index == -10 || index == -1){
                        goBack = true;
                        continue;
                    }
                    player.getPlayerHand().getCardsInHand().remove(index);
                    tui.showMessage("In which position would you like to put the Exploding Kitten? (1 between " + game.getDeck().getDrawPile().size() + ")");
                    int input3 = (tui.readInputInt())  - 1;
                    if (input3 == -10){
                        goBack = true;
                        continue;
                    }
                    game.getDeck().getDrawPile().add(input3, bomb);
                    player.getPlayerHand().getCardsInHand().remove(bomb);
                }
                else{
                    tui.showMessage("Better luck next time Champ!");
                    player.die();
                }
            }
        } else {
            System.out.println("Better luck next time Champ!");
            player.die();
        }
    }

    @Override
    public boolean validateMove(Card card, Player player) {
        for (Player otherPlayer : game.getPlayers()) {
            if (otherPlayer != player) { // Check other players, not the current player
                for (Card cardInHand : otherPlayer.getPlayerHand().getCardsInHand()) {
                    if (cardInHand.getCardType().equals(Card.cardType.NOPE)) {
                        if (validateByNope(card, player)) { // If a Nope card is used
                            return false; // Stop checking further
                        }
                    }
                }
            }
        }
        return true; // No Nope card was used, proceed with the move
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