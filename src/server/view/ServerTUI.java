package server.view;

import exploding_kittens.Controller;
import exploding_kittens.model.BackInputException;
import exploding_kittens.model.BooleanReturnException;
import exploding_kittens.view.PlayerTUI;

public class ServerTUI implements PlayerTUI {


    public ServerTUI(){}

    @Override
    public void showMessage(String text) {

    }

    @Override
    public String readInputString() throws BackInputException {
        return null;
    }

    @Override
    public int readInputInt() {
        return 0;
    }

    @Override
    public boolean readInputBoolean() throws BooleanReturnException {
        return false;
    }

    @Override
    public void printHand() {

    }
}
