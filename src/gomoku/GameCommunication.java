package gomoku;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Collections.singleton;

class GameCommunication {

    static final String MOVE_FILE = "move_file";
    static final String END_GAME = "end_game";
    private static final Integer TIME_LIMIT = 10000;

    private final String playerName;

    GameCommunication(String playerName) {
        this.playerName = playerName;
    }

    Boolean isOver() {
        return Files.exists(Paths.get(END_GAME));
    }

    void waitForOpponentMove() {
        Integer totalDuration = 0;
        Integer sleepDuration = 100;
        while (!Files.exists(Paths.get(playerFile(playerName)))) {
            if (totalDuration > TIME_LIMIT)
                throw new RuntimeException("Player waited longer than " + TIME_LIMIT / 1000 + " seconds");
            try {
                Thread.sleep(sleepDuration);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            totalDuration += sleepDuration;
        }
    }

    void makeMove(Move move) {
        try {
            Files.write(Paths.get(MOVE_FILE), singleton(move.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    EndReason getEndReason() {
        // TODO
        return null;
    }

    Outcome getOutcome() {
        // TODO
        return null;
    }

    static String playerFile(String playerName) {
        return playerName + ".go";
    }
}
