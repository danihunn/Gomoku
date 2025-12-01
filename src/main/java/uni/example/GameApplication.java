package uni.example;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import uni.example.model.Board;
import uni.example.model.Player;
import uni.example.service.DatabaseService;
import uni.example.service.GameService;

public class GameApplication {

    private static final Logger LOGGER = Logger.getLogger(GameApplication.class.getName());

    public static void main(final String[] args) {
        final Board board = new Board(10, 10);
        final DatabaseService databaseService = new DatabaseService();

        try (Scanner scanner = new Scanner(System.in)) {

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Please enter your name: ");
            }
            final String playerName = scanner.nextLine().trim();
            final Player humanPlayer = new Player(playerName, 'x');
            final Player computerPlayer = new Player("AI", 'o');

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Load from file (name of file or leave empty): ");
            }
            final String loadFileName = scanner.nextLine().trim();
            if (!loadFileName.isEmpty()) {
                try {
                    board.loadFromFile(loadFileName);
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO, "Board loaded successfully from {0}", loadFileName);
                    }
                } catch (final Exception exception) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "Failed to load board from file: " + loadFileName, exception);
                    }
                }
            }

            final GameService gameService = new GameService(board, humanPlayer, computerPlayer, scanner, databaseService);
            gameService.startGame();

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Show high score list? (y/n): ");
            }
            final String showHighScores = scanner.nextLine().trim();
            if ("y".equalsIgnoreCase(showHighScores)) {
                databaseService.getHighScores().forEach(score -> {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO, "{0} - {1} wins", new Object[]{score.getName(), score.getWins()});
                    }
                });
            }

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Save (name of file or leave empty): ");
            }
            final String saveFileName = scanner.nextLine().trim();
            if (!saveFileName.isEmpty()) {
                try {
                    board.saveToFile(saveFileName);
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO, "Board saved successfully to {0}", saveFileName);
                    }
                } catch (final Exception exception) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "Failed to save board to file: " + saveFileName, exception);
                    }
                }
            }

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("GAME END");
            }
        }
    }
}
