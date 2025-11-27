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
        this.grid[rowCount / 2][colCount / 2] = HUMAN_CELL;
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
        final StringBuilder stringBuild = new StringBuilder();
        stringBuild.append("\n");
        stringBuild.append("  ");
        for (int c = 0; c < colCount; c++) {
            stringBuild.append((char) ('a' + c)).append(" ");
        }
        stringBuild.append(System.lineSeparator());
        for (int r = 0; r < rowCount; r++) {
            stringBuild.append(r + 1).append(" ");
            for (int c = 0; c < colCount; c++) {
                stringBuild.append(grid[r][c]).append(" ");
            }
            stringBuild.append(System.lineSeparator());
        }
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(stringBuild.toString());
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
                    } else {
                        grid[row][col] = EMPTY_CELL;
                    }
                }
                row++;
            }
        } catch (IOException exception) {
            LOGGER.warning("Can't load file, new game starting");
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
                LOGGER.info("Game Saved: " + filename);
            }
        } catch (IOException exception) {
            LOGGER.severe("Error while saving");
        }
    }
}
