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
    private final DatabaseService databaseService;

    public GameService(final Board board, final Player human, final Player computer,
                       final Scanner scanner, final DatabaseService databaseService) {
        this.board = board;
        this.human = human;
        this.computer = computer;
        this.scanner = scanner;
        this.databaseService = databaseService;
        this.random = new Random();
    }

    public GameService(final Board board, final Player human, final Player computer, final Scanner scanner) {
        this(board, human, computer, scanner, null);
    }

    public void startGame() {
        Player currentPlayer = human;
        boolean gameOver = false;

        while (!gameOver) {
            board.printBoard();

            if (currentPlayer.equals(human)) {
                humanMove();
            } else {
                computerMove();
            }

            final boolean hasWon = checkWin(currentPlayer.getSymbol());
            if (hasWon) {
                board.printBoard();
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "{0} won!", currentPlayer.getName());
                }
                if (databaseService != null) {
                    databaseService.addWin(currentPlayer.getName());
                }
                gameOver = true;
            } else {
                currentPlayer = currentPlayer.equals(human) ? computer : human;
            }
        }
    }

    public void humanMove() {
        boolean validMove = false;
        while (!validMove) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Your move (e.g., b3): ");
            }
            final String input = scanner.nextLine().trim();
            if (input.length() < 2) {
                continue;
            }

            final int column = input.charAt(0) - 'a';
            final int row;
            try {
                row = Integer.parseInt(input.substring(1)) - 1;
            } catch (NumberFormatException exception) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Invalid input, please enter a valid number.");
                }
                continue;
            }

            if (row >= 0 && row < board.getRowCount()
                    && column >= 0 && column < board.getColCount()
                    && board.isCellEmpty(row, column)) {
                board.placeMove(row, column, human.getSymbol());
                validMove = true;
            } else {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Invalid move! Cell is occupied or out of bounds.");
                }
            }
        }
    }

    public void computerMove() {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "AI's turn...");
        }
        int rowIndex;
        int columnIndex;

        do {
            rowIndex = random.nextInt(board.getRowCount());
            columnIndex = random.nextInt(board.getColCount());
        } while (!board.isCellEmpty(rowIndex, columnIndex));

        board.placeMove(rowIndex, columnIndex, computer.getSymbol());
    }

    public boolean checkWin(final char symbol) {
        boolean winFound = false;
        for (int rowIndex = 0; rowIndex < board.getRowCount(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < board.getColCount(); columnIndex++) {
                if (board.getCell(rowIndex, columnIndex) == symbol
                        && (checkDirection(rowIndex, columnIndex, 1, 0, symbol)
                            || checkDirection(rowIndex, columnIndex, 0, 1, symbol)
                            || checkDirection(rowIndex, columnIndex, 1, 1, symbol)
                            || checkDirection(rowIndex, columnIndex, 1, -1, symbol))) {
                    winFound = true;
                }
            }
        }
        return winFound;
    }

    private boolean checkDirection(final int startRow, final int startColumn,
                                   final int deltaRow, final int deltaColumn,
                                   final char symbol) {
        int count = 0;
        for (int i = 0; i < WIN_LENGTH; i++) {
            final int row = startRow + deltaRow * i;
            final int column = startColumn + deltaColumn * i;
            if (row >= 0 && row < board.getRowCount()
                    && column >= 0 && column < board.getColCount()
                    && board.getCell(row, column) == symbol) {
                count++;
            } else {
                break;
            }
        }
        return count == WIN_LENGTH;
    }
}
