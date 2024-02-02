package exploding_kittens.model;

// Used to determine and handle if and when the player wants to go back on his decision
public class BackInputException extends Exception{


    public BackInputException(String msg){
        super(msg);
    }
}
