package uni.example;

import java.util.Scanner;
import java.util.logging.Logger;

import uni.example.model.Board;
import uni.example.model.Player;
import uni.example.service.GameService;

public class GameApplication {

    private static final Logger LOGGER = Logger.getLogger(GameApplication.class.getName());

    public static void main(final String[] args) {
        final Board board = new Board(10, 10);

        try (Scanner scanner = new Scanner(System.in)) {
            LOGGER.info("Give me your name: ");
            final String playerName = scanner.nextLine().trim();
            final Player human = new Player(playerName, 'x');
            final Player computer = new Player("Gép", 'o');

            LOGGER.info("Load from file (name of file or leave empty): ");
            final String loadFile = scanner.nextLine().trim();
            if (!loadFile.isEmpty()) {
                board.loadFromFile(loadFile);
            }

            final GameService gameService = new GameService(board, human, computer, scanner);
            gameService.startGame();

            LOGGER.info("Save (name of file or leave empty): ");
            final String saveFile = scanner.nextLine().trim();
            if (!saveFile.isEmpty()) {
                board.saveToFile(saveFile);
            }

            LOGGER.info("Game END");
        }
    }
}
