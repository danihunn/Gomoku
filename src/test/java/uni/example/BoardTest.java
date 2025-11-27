package uni.example;

import org.junit.jupiter.api.*;

import uni.example.model.Board;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(5, 5);
    }

    @Test
    void testInitialBoardSetup() {
        assertEquals(5, board.getRowCount());
        assertEquals(5, board.getColCount());

        assertEquals('x', board.getCell(2, 2));

        for (int r = 0; r < board.getRowCount(); r++) {
            for (int c = 0; c < board.getColCount(); c++) {
                if (r != 2 || c != 2) {
                    assertTrue(board.isCellEmpty(r, c));
                }
            }
        }
    }

    @Test
    void testPlaceMoveAndIsCellEmpty() {
        assertTrue(board.isCellEmpty(0, 0));
        board.placeMove(0, 0, 'o');
        assertFalse(board.isCellEmpty(0, 0));
        assertEquals('o', board.getCell(0, 0));
    }

    @Test
    void testPrintBoardDoesNotThrow() {
        assertDoesNotThrow(() -> board.printBoard());
    }

    @Test
    void testSaveAndLoadFile() throws IOException {
        File tempFile = File.createTempFile("board_test", ".txt");
        tempFile.deleteOnExit();

        board.placeMove(0, 0, 'o');
        board.saveToFile(tempFile.getAbsolutePath());

        Board newBoard = new Board(5, 5);
        newBoard.loadFromFile(tempFile.getAbsolutePath());

        assertEquals('o', newBoard.getCell(0, 0));
        assertEquals('x', newBoard.getCell(2, 2));
    }

    @Test
    void testLoadNonexistentFileLogsWarning() {
        Board newBoard = new Board(5, 5);
        assertDoesNotThrow(() -> newBoard.loadFromFile("nonexistent_file.txt"));
    }
}
