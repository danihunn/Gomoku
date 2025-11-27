package uni.example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uni.example.model.Board;
import uni.example.model.Player;
import uni.example.service.GameService;

class GameServiceTest {

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
    void testHumanMove() throws Exception {
        String input = "b2\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        GameService service = new GameService(board, human, computer, scanner);

        Method humanMove = GameService.class.getDeclaredMethod("humanMove");
        humanMove.setAccessible(true);
        humanMove.invoke(service);

        assertEquals('x', board.getCell(1, 1));
    }

    @Test
    void testComputerMove() throws Exception {
        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]));
        GameService service = new GameService(board, human, computer, scanner);

        Method computerMove = GameService.class.getDeclaredMethod("computerMove");
        computerMove.setAccessible(true);
        computerMove.invoke(service);

        boolean foundO = false;
        for (int r = 0; r < board.getRowCount(); r++) {
            for (int c = 0; c < board.getColCount(); c++) {
                if (board.getCell(r, c) == 'o') {
                    foundO = true;
                }
            }
        }
        assertTrue(foundO, "Computer should have placed 'o'");
    }

    @Test
    void testCheckWinHorizontal() throws Exception {
        board.placeMove(0, 0, 'x');
        board.placeMove(0, 1, 'x');
        board.placeMove(0, 2, 'x');
        board.placeMove(0, 3, 'x');

        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]));
        GameService service = new GameService(board, human, computer, scanner);

        Method checkWin = GameService.class.getDeclaredMethod("checkWin", char.class);
        checkWin.setAccessible(true);
        assertTrue((boolean) checkWin.invoke(service, 'x'));
    }

    @Test
    void testCheckWinVertical() throws Exception {
        board.placeMove(0, 0, 'x');
        board.placeMove(1, 0, 'x');
        board.placeMove(2, 0, 'x');
        board.placeMove(3, 0, 'x');

        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]));
        GameService service = new GameService(board, human, computer, scanner);

        Method checkWin = GameService.class.getDeclaredMethod("checkWin", char.class);
        checkWin.setAccessible(true);
        assertTrue((boolean) checkWin.invoke(service, 'x'));
    }

    @Test
    void testCheckWinDiagonal() throws Exception {
        board.placeMove(0, 0, 'x');
        board.placeMove(1, 1, 'x');
        board.placeMove(2, 2, 'x');
        board.placeMove(3, 3, 'x');

        Scanner scanner = new Scanner(new ByteArrayInputStream(new byte[0]));
        GameService service = new GameService(board, human, computer, scanner);

        Method checkWin = GameService.class.getDeclaredMethod("checkWin", char.class);
        checkWin.setAccessible(true);
        assertTrue((boolean) checkWin.invoke(service, 'x'));
    }

    @Test
    void testStartGameHumanWins() {
        // előkészítjük a táblát, hogy a humán nyerjen
        board.placeMove(0, 0, 'x');
        board.placeMove(0, 1, 'x');
        board.placeMove(0, 2, 'x');

        // humán bemenet: győztes lépés
        String input = "d1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        GameService service = new GameService(board, human, computer, scanner);

        service.startGame();

        assertEquals('x', board.getCell(0, 3), "Human should have placed 'x' to win");
    }
}