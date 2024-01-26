package exploding_kittens.view;

import local.model.BackInputException;
import local.model.BooleanReturnException;

public interface PlayerTUI {

    public void showMessage(String text);
    public String readInputString() throws BackInputException;
    public int readInputInt();
    public boolean readInputBoolean() throws BooleanReturnException;
}
