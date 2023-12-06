public class ExplodingKittens {


    private Game game;


    public ExplodingKittens(Player p1){
        game = new Game( p1 );
        game.start();
    }


    public static void main (String[] args){
        System.out.println( "Welcome to Exploding Kittens!" );
    }
}
