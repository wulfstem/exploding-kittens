import java.util.ArrayList;

public class Game {


    private ArrayList<Player> players;
    private int numberOfPlayers;
    private boolean isComputerPlaying = false;


    public Game(Player p1){
        this.numberOfPlayers = 1;
        players.add( p1 );
        this.isComputerPlaying = true;
        players.add(new Player());
    }

    public void start(){
        // To be implemented
    }
}
