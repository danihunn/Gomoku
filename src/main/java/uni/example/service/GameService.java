package uni.example.service;

import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import uni.example.model.Board;
import uni.example.model.Player;

public class GameService {

    private static final Logger LOGGER = Logger.getLogger(GameService.class.getName());
    private static final int WIN_LENGTH = 4;

    private final Board board;
    private final Player human;
    private final Player computer;
    private final Scanner scanner;
    private final Random random;

    public GameService(final Board board, final Player human, final Player computer, final Scanner scanner) {
        this.board = board;
        this.human = human;
        this.computer = computer;
        this.scanner = scanner;
        this.random = new Random();
    }

    public void startGame() {
        Player current = human;
        boolean gameOver = false;

        while (!gameOver) {
            board.printBoard();

            if (current.equals(human)) {
                humanMove();
            } else {
                computerMove();
            }

            if (checkWin(current.getSymbol())) {
                board.printBoard();
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info(current.getName() + " won!");
                }
                gameOver = true;
            } else {
                current = current.equals(human) ? computer : human;
            }
        }
    }

    public void humanMove() {
        boolean valid = false;
        while (!valid) {
            LOGGER.info("Your move: (ex. b3): ");
            final String input = scanner.nextLine().trim();
            if (input.length() < 2) {
                continue;
            }
            final int col = input.charAt(0) - 'a';
            int row;
            try {
                row = Integer.parseInt(input.substring(1)) - 1;
            } catch (NumberFormatException exception) {
                continue;
            }
            final boolean insideBoard = row >= 0 && row < board.getRowCount()
                    && col >= 0 && col < board.getColCount();
            if (insideBoard && board.isCellEmpty(row, col)) {
                board.placeMove(row, col, human.getSymbol());
                valid = true;
            } else {
                LOGGER.warning("Invalid move!");
            }
        }
    }

    public void computerMove() {
        LOGGER.info("AIs turn...");
        int row;
        int col;
        do {
            row = random.nextInt(board.getRowCount());
            col = random.nextInt(board.getColCount());
        } while (!board.isCellEmpty(row, col));
        board.placeMove(row, col, computer.getSymbol());
    }

    public boolean checkWin(final char symbol) {
        boolean result = false;
        for (int row = 0; row < board.getRowCount(); row++) {
            for (int col = 0; col < board.getColCount(); col++) {
                if (board.getCell(row, col) == symbol) {
                    if (checkDirection(row, col, 1, 0, symbol)
                            || checkDirection(row, col, 0, 1, symbol)
                            || checkDirection(row, col, 1, 1, symbol)
                            || checkDirection(row, col, 1, -1, symbol)) {
                        result = true;
                        break;
                    }
                }
            }
            if (result) {
                break;
            }
        }
        return result;
    }

    private boolean checkDirection(final int startRow, final int startCol,
                                   final int deltaRow, final int deltaCol,
                                   final char symbol) {
        int count = 0;
        for (int i = 0; i < WIN_LENGTH; i++) {
            final int row = startRow + deltaRow * i;
            final int col = startCol + deltaCol * i;
            if (row >= 0 && row < board.getRowCount()
                    && col >= 0 && col < board.getColCount()
                    && board.getCell(row, col) == symbol) {
                count++;
            } else {
                break;
            }
        }
        return count == WIN_LENGTH;
    }
}
