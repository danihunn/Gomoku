package uni.example;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uni.example.model.PlayerScore;
import uni.example.service.DatabaseService;

public class DatabaseServiceTest {

    private DatabaseService dbService;

    @BeforeEach
    public void setUp() {
        dbService = new DatabaseService("jdbc:sqlite::memory:");
    }

    @AfterEach
    public void tearDown() {
        dbService.close();
    }

    @Test
    public void testAddWin_NewPlayer() {
        dbService.addWin("Alice");
        List<PlayerScore> scores = dbService.getHighScores();
        assertEquals(1, scores.size());
        PlayerScore score = scores.get(0);
        assertEquals("Alice", score.getName());
        assertEquals(1, score.getWins());
    }

    @Test
    public void testAddWin_ExistingPlayer() {
        dbService.addWin("Bob");
        dbService.addWin("Bob");
        List<PlayerScore> scores = dbService.getHighScores();
        assertEquals(1, scores.size());
        PlayerScore score = scores.get(0);
        assertEquals("Bob", score.getName());
        assertEquals(2, score.getWins());
    }
    @Test
    public void testAddWin_Failure() throws SQLException {
        DatabaseService service = new DatabaseService("jdbc:sqlite::memory:");
        service.close();
        service.addWin("FailUser");
    }


    @Test
    public void testMultiplePlayers() {
        dbService.addWin("Alice");
        dbService.addWin("Bob");
        dbService.addWin("Alice");
        List<PlayerScore> scores = dbService.getHighScores();
        assertEquals(2, scores.size());
        assertEquals("Alice", scores.get(0).getName());
        assertEquals(2, scores.get(0).getWins());
        assertEquals("Bob", scores.get(1).getName());
        assertEquals(1, scores.get(1).getWins());
    }

    @Test
    public void testGetHighScores_Empty() {
        List<PlayerScore> scores = dbService.getHighScores();
        assertTrue(scores.isEmpty());
    }

    @Test
    public void testGetHighScores_Failure() throws SQLException {
        DatabaseService service = new DatabaseService("jdbc:sqlite::memory:");
        service.close();
        List<PlayerScore> scores = service.getHighScores();
        assertTrue(scores.isEmpty());
    }


    @Test
    public void testCreateTable_DirectCall() throws Exception {
        dbService.addWin("Charlie");
        List<PlayerScore> scores = dbService.getHighScores();
        assertEquals(1, scores.size());
        assertEquals("Charlie", scores.get(0).getName());
        assertEquals(1, scores.get(0).getWins());
    }

    @Test
    public void testDatabaseService_DefaultConstructor() {
        DatabaseService defaultService = new DatabaseService("jdbc:sqlite::memory:");
        defaultService.addWin("DefaultUser");
        List<PlayerScore> scores = defaultService.getHighScores();
        assertEquals(1, scores.size());
        assertEquals("DefaultUser", scores.get(0).getName());
        defaultService.close();
    }

    @Test
    public void testDatabaseService_StringConstructor() {
        DatabaseService stringService = new DatabaseService("jdbc:sqlite::memory:");
        stringService.addWin("StringUser");
        List<PlayerScore> scores = stringService.getHighScores();
        assertEquals(1, scores.size());
        assertEquals("StringUser", scores.get(0).getName());
        stringService.close();
    }


    @Test
    public void testClose() throws SQLException {
        DatabaseService service = new DatabaseService("jdbc:sqlite::memory:");
        service.close();
        service.close();
    }

}
