package uni.example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uni.example.model.Board;
import uni.example.model.Player;
import uni.example.model.PlayerScore;
import uni.example.service.DatabaseService;
import uni.example.service.GameService;

class GameServiceWithDatabaseTest {

    private Board board;
    private Player human;
    private Player computer;

    @BeforeEach
    void setup() {
        board = new Board(5, 5);
        human = new Player("Alice", 'x');
        computer = new Player("Bot", 'o');
    }

    @Test
    void testHumanMoveAndDatabaseUpdate() {
        String input = "a1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        DatabaseService dbService = new DatabaseService("jdbc:sqlite::memory:");
        GameService service = new GameService(board, human, computer, scanner, dbService);

        service.humanMove();

        assertEquals('x', board.getCell(0, 0));

        dbService.addWin(human.getName());
        List<PlayerScore> scores = dbService.getHighScores();
        assertEquals(1, scores.size());
        assertEquals("Alice", scores.get(0).getName());
        assertEquals(1, scores.get(0).getWins());
    }

    @Test
    void testComputerMove() {
        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]));
        DatabaseService dbService = new DatabaseService("jdbc:sqlite::memory:");
        GameService service = new GameService(board, human, computer, scanner, dbService);

        service.computerMove();

        boolean foundO = false;
        for (int r = 0; r < board.getRowCount(); r++) {
            for (int c = 0; c < board.getColCount(); c++) {
                if (board.getCell(r, c) == 'o') {
                    foundO = true;
                }
            }
        }
        assertTrue(foundO);
    }

    @Test
    void testFullGameHumanWins() {
        String input = "a1\nb1\nc1\nd1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        DatabaseService dbService = new DatabaseService("jdbc:sqlite::memory:");
        GameService service = new GameService(board, human, computer, scanner, dbService);

        service.startGame();

        assertTrue(service.checkWin('x'));

        List<PlayerScore> scores = dbService.getHighScores();
        assertEquals(1, scores.size());
        assertEquals("Alice", scores.get(0).getName());
        assertEquals(1, scores.get(0).getWins());
    }
}
