package gomoku;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static gomoku.Stone.OPPONENT;
import static java.util.Collections.singleton;

class GameCommunication {

    static final String MOVE_FILE = "move_file";
    static final String END_GAME = "end_game";
    private static final Integer TIME_LIMIT = 20000;//10000

    private final String playerName;

    GameCommunication(String playerName) {
        this.playerName = playerName;
    }

    Boolean isOver() {
        return Files.exists(Paths.get(END_GAME));
    }

    Move waitForOpponentMove() {
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
        return readMove();
    }

    private Move readMove() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(MOVE_FILE));
            return parseMove(lines.get(0));
        } catch (IOException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    private Move parseMove(String line) {
        String[] tokens = line.split(" ");
        return new Move(OPPONENT, tokens[1].charAt(0), Integer.parseInt(tokens[2]));
    }

    void writeMove(Move move) {
        try {
            System.out.println("\n\n" + playerName + " made move " + move + "\n\n");
            Files.write(Paths.get(MOVE_FILE), singleton(playerName + " " + move.getCell()));
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
