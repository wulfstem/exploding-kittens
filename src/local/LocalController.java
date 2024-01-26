package local;

import exploding_kittens.Controller;
import exploding_kittens.model.BooleanReturnException;
import exploding_kittens.model.Card;
import exploding_kittens.model.Game;
import exploding_kittens.model.Player;

public class LocalController implements Controller {


    private Game game;

    public LocalController(Game game){
        this.game = game;
        game.start();
        game.setController(this);
    }

    @Override
    public void makeMove() {
        int current = game.getCurrent();
        game.setSkipTurn(false);

        boolean goBack = true;
        while(goBack){
            goBack = false;
            boolean answer;
            getCurrentPlayer().getTui().showMessage("Do you want to play a card?");
            try{
                answer = getCurrentPlayer().getTui().readInputBoolean();
            }catch (BooleanReturnException e){
                getCurrentPlayer().getTui().showMessage("You cannot go back without making this decision.");
                goBack = true;
                continue;
            }
            if (answer){
                int index = getAnyCardChoice();
                if (index == -10 || index == -1){
                    goBack = true;
                    continue;
                }
                // Check if any other player wants to play a NOPE card
                if(validateMove(getCurrentPlayer().getPlayerHand().getCardsInHand().get(index), getCurrentPlayer())){
                    getCurrentPlayer().getPlayerHand().getCardsInHand().get(index).action(getCurrentPlayer());
                    getCurrentPlayer().getPlayerHand().getCardsInHand().remove(index);
                    if (game.isSkipTurn()) {
                        if (game.getCurrent() == current) {
                            if (current == (game.getPlayers().size() - 1)){
                                game.setCurrent(0);
                            }
                            else{
                                game.setCurrent((game.getCurrent() + 1));
                            }
                        }
                    } else {
                        makeMove();
                    }
                }
                else{
                    getCurrentPlayer().getTui().showMessage("Your card has been cancelled by another player playing a NOPE card.");
                    getCurrentPlayer().getPlayerHand().getCardsInHand().remove(index);
                    makeMove();
                }
            }
            else{
                getCurrentPlayer().draw();
                if (game.getTurns() == 1 && game.getCurrent() == current){
                    if (current == (game.getPlayers().size() - 1)){
                        game.setCurrent(0);
                    }
                    else{
                        game.setCurrent((game.getCurrent() + 1));
                    }
                }
            }
        }
    }

    @Override
    public boolean validateByNope(Card card, Player player) {
        boolean result = false;
        getCurrentPlayer().getTui().showMessage(player.getPlayerName() + " is playing " + card.getCardName());
        getCurrentPlayer().getTui().printHand();
        boolean goBack = true;
        while(goBack){
            goBack = false;
            getCurrentPlayer().getTui().showMessage("Do you want to use your NOPE card?");
            boolean answer = false;
            try {
                answer = getCurrentPlayer().getTui().readInputBoolean();
            } catch (BooleanReturnException e) {
                getCurrentPlayer().getTui().showMessage("You cannot go back without making this decision.");
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
    public void dieOrDefuse(Card bomb) {
        getCurrentPlayer().getTui().showMessage("You have drawn an Exploding Kitten!");
        getCurrentPlayer().getTui().printHand();
        if (getCurrentPlayer().handContains(Card.cardType.DEFUSE)) {
            boolean invalid = true;
            boolean answer = false;
            while(invalid){
                getCurrentPlayer().getTui().showMessage("Use a Defuse?");
                try {
                    answer = getCurrentPlayer().getTui().readInputBoolean();
                    invalid = false;
                } catch (BooleanReturnException e) {
                    getCurrentPlayer().getTui().showMessage("You cannot go back without making this decision.");;
                }
            }
            boolean goBack = true;
            while(goBack){
                goBack = false;
                if (answer){
                    getCurrentPlayer().getTui().showMessage("Which card? (index)");
                    int index = getCardChoice(Card.cardType.DEFUSE);
                    if (index == -10 || index == -1){
                        goBack = true;
                        continue;
                    }
                    getCurrentPlayer().getPlayerHand().getCardsInHand().remove(index);
                    getCurrentPlayer().getTui().showMessage("In which position would you like to put the Exploding Kitten? (1 between " + game.getDeck().getDrawPile().size() + ")");
                    int input3 = getCurrentPlayer().getTui().readInputInt();
                    if (input3 == -10){
                        goBack = true;
                        continue;
                    }
                    game.getDeck().getDrawPile().add(input3, bomb);
                    getCurrentPlayer().getPlayerHand().getCardsInHand().remove(bomb);
                }
                else{
                    getCurrentPlayer().getTui().showMessage("Better luck next time Champ!");
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
        getCurrentPlayer().getTui().showMessage("Which card would you like to play? (number between 0 and " + (getCurrentPlayer().getPlayerHand().getCardsInHand().size() - 1) + ")");
        while(!isIndexValid){
            input2 = getCurrentPlayer().getTui().readInputInt();
            if (input2 == -10){
                return -10;
            }
            if (input2 <= 0 || input2 > getCurrentPlayer().getPlayerHand().getCardsInHand().size()){
                getCurrentPlayer().getTui().showMessage("Invalid index, choose a number between 0 and" + (getCurrentPlayer().getPlayerHand().getCardsInHand().size() - 1));
            }
            isIndexValid = true;
        }
        if (getCurrentPlayer().getPlayerHand().getCardsInHand().get(input2).getCardType().equals(type)){
            return input2;
        }
        else{
            getCurrentPlayer().getTui().showMessage("Chosen card is not of type " + type);
            return -1;
        }
    }

    @Override
    public int getAnyCardChoice() {
        boolean isIndexValid = false;
        int input2 = 0;
        getCurrentPlayer().getTui().showMessage("Which card would you like to play? (number between 0 and " + (getCurrentPlayer().getPlayerHand().getCardsInHand().size() - 1) + ")");
        while(!isIndexValid){
            input2 = getCurrentPlayer().getTui().readInputInt();
            if (input2 == -10){
                return -10;
            }
            if (input2 < 0 || input2 > getCurrentPlayer().getPlayerHand().getCardsInHand().size()){
                getCurrentPlayer().getTui().showMessage("Invalid index, choose a number between 0 and" + (getCurrentPlayer().getPlayerHand().getCardsInHand().size() - 1));
            }
            isIndexValid = true;
        }
        if (getCurrentPlayer().getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.cardType.DEFUSE)){
            getCurrentPlayer().getTui().showMessage("Defuse card can only be played when a bomb has been drawn.");
            return -1;
        }
        else if(getCurrentPlayer().getPlayerHand().getCardsInHand().get(input2).getCardType().equals(Card.cardType.NOPE)){
            getCurrentPlayer().getTui().showMessage("Nope card cannot be played at the moment.");
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







    public static void main (String[] args){
        System.out.println( "\nWelcome to Exploding Kittens!" );
        int numberOfPlayers = Integer.parseInt(args[0]);
        String[] nicknames = new String[numberOfPlayers];
        for (int i = 1; i <= numberOfPlayers; i++){
            nicknames[i-1] = args[i];
        }
        new LocalController(new Game(numberOfPlayers, nicknames));
    }
}