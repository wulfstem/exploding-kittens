package exploding_kittens.model;

import exploding_kittens.Controller;

public class Computer extends Player{


    public static final String COMPUTER_NAME = "RoboTukas";

    public Computer(String playerName, Game game, int positionIndex, Controller controller) {
        super(playerName, game, positionIndex, controller);
    }

    public void compete(){

    }

    public Card pickCardFromHand(){
        return null;
    }
}
