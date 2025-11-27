package uni.example;

import org.junit.jupiter.api.Test;

import uni.example.model.Player;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testConstructorAndGetters() {
        Player player = new Player("Dani", 'x');
        assertEquals("Dani", player.getName());
        assertEquals('x', player.getSymbol());
    }

    @Test
    void testToString() {
        Player player = new Player("Bob", 'o');
        assertEquals("Bob (o)", player.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        Player player1 = new Player("Charlie", 'x');
        Player player2 = new Player("Charlie", 'x');
        Player player3 = new Player("Charlie", 'o');
        Player player4 = new Player("Dave", 'x');

        assertEquals(player1, player2);
        assertEquals(player1.hashCode(), player2.hashCode());

        assertNotEquals(player1, player3);
        assertNotEquals(player1, player4);
        assertNotEquals(player3, player4);
    }

    @Test
    void testEqualsWithSameObject() {
        Player player = new Player("Eve", 'o');
        assertEquals(player, player);
    }

    @Test
    void testEqualsWithNull() {
        Player player = new Player("Frank", 'x');
        assertNotEquals(player, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        Player player = new Player("George", 'o');
        assertNotEquals(player, "some string");
    }
}
