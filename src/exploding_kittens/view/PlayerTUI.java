package exploding_kittens.view;

import exploding_kittens.model.BackInputException;
import exploding_kittens.model.BooleanReturnException;

public interface PlayerTUI {

    public void showMessage(String text);
    public String readInputString() throws BackInputException;
    public int readInputInt();
    public boolean readInputBoolean() throws BooleanReturnException;
    void printHand();
}
