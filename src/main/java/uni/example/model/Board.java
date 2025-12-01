package uni.example.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Board {

    private static final Logger LOGGER = Logger.getLogger(Board.class.getName());
    private static final char EMPTY_CELL = '.';
    private static final char HUMAN_CELL = 'x';
    private static final char COMPUTER_CELL = 'o';

    private final int rowCount;
    private final int colCount;
    private final char[][] grid;

    public Board(final int rowCount, final int colCount) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.grid = new char[rowCount][colCount];

        for (int i = 0; i < rowCount; i++) {
            Arrays.fill(this.grid[i], EMPTY_CELL);
        }

        final int midRow = rowCount / 2;
        final int midCol = colCount / 2;
        grid[midRow][midCol] = HUMAN_CELL;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public char getCell(final int row, final int col) {
        return grid[row][col];
    }

    public boolean isCellEmpty(final int row, final int col) {
        return grid[row][col] == EMPTY_CELL;
    }

    public void placeMove(final int row, final int col, final char symbol) {
        grid[row][col] = symbol;
    }

    public void printBoard() {
        if (LOGGER.isLoggable(Level.INFO)) {
            final StringBuilder boardOutput = new StringBuilder();
            boardOutput.append("\n  ");
            for (int c = 0; c < colCount; c++) {
                boardOutput.append((char) ('a' + c)).append(" ");
            }
            boardOutput.append(System.lineSeparator());

            for (int r = 0; r < rowCount; r++) {
                boardOutput.append(r + 1).append(" ");
                for (int c = 0; c < colCount; c++) {
                    boardOutput.append(grid[r][c]).append(" ");
                }
                boardOutput.append(System.lineSeparator());
            }

            LOGGER.info(boardOutput.toString());
        }
    }

    public void loadFromFile(final String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < rowCount) {
                for (int col = 0; col < Math.min(line.length(), colCount); col++) {
                    final char cellChar = line.charAt(col);
                    if (cellChar == HUMAN_CELL || cellChar == COMPUTER_CELL) {
                        grid[row][col] = cellChar;
                    }
                }
                row++;
            }
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Board loaded from file: {0}", filename);
            }
        } catch (IOException exception) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Can't load file, starting new game.", exception);
            }
        }
    }

    public void saveToFile(final String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int r = 0; r < rowCount; r++) {
                for (int c = 0; c < colCount; c++) {
                    writer.write(grid[r][c]);
                }
                writer.newLine();
            }
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Game saved to file: {0}", filename);
            }
        } catch (IOException exception) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while saving game to file: " + filename, exception);
            }
        }
    }
}
