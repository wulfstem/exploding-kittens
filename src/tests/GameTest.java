package tests;

import exploding_kittens.model.Game;
import exploding_kittens.model.Player;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * JUnit test class for the {@link exploding_kittens.model.Game} class.
 * It includes test cases for various methods in the Game class.
 *
 * @author Ervinas Vilkaitis and Ugnius Tulaba
 */
public class GameTest {

    private Game game;

    /**
     * Set up the initial game state before each test case.
     */
    @Before
    public void setUp() {
        // Create a game with 3 players (no computer player)
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Player1");
        nicknames.add("Player2");
        nicknames.add("Player3");

        game = new Game(3, nicknames, false);
    }

    /**
     * Test the creation of players in the game.
     */
    @Test
    public void testCreatePlayers() {
        game.setup();

        ArrayList<Player> players = game.getPlayers();

        assertNotNull(players);
        assertEquals(3, players.size());

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            assertEquals("Player" + (i + 1), player.getPlayerName());
            assertEquals(i, player.getPositionIndex());
        }
    }

    /**
     * Test the creation of the game deck.
     */
    @Test
    public void testCreateDeck() {
        game.setup();

        assertNotNull(game.getDeck());
        assertEquals(30, game.getDeck().getDrawPile().size()); // Assuming 3 players in the test
    }

    /**
     * Test the creation of hands for players.
     */
    @Test
    public void testCreateHands() {
        game.setup();

        ArrayList<Player> players = game.getPlayers();

        for (Player player : players) {
            assertNotNull(player.getPlayerHand());
            assertEquals(8, player.getPlayerHand().getCardsInHand().size()); // Assuming 4 cards per hand
        }
    }

    /**
     * Test selecting a random number within a specified range.
     */
    @Test
    public void testSelectRandomly() {
        int size = 5; // Test with a range from 0 to 4
        int randomValue = game.selectRandomly(size);

        assertTrue(randomValue >= 0);
        assertTrue(randomValue < size);
    }

    /**
     * Test checking for a game winner.
     */
    @Test
    public void testHasWinner() {
        game.setup();

        assertFalse(game.hasWinner());

        // Remove all players except one
        ArrayList<Player> players = game.getPlayers();
        players.remove(0);
        players.remove(0);
        game.setPlayers(players);

        assertTrue(game.hasWinner());
    }
}
