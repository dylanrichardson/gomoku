package gomoku;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static gomoku.EndReason.*;
import static gomoku.GameCommunication.*;
import static gomoku.Outcome.DRAW;
import static gomoku.Outcome.LOSS;
import static gomoku.Outcome.WIN;
import static gomoku.Stone.FRIENDLY;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class GameCommunicationTest {
    @Test
    public void isOverTrue() {
        GameCommunication gameCommunication = new GameCommunication("test");
        addEndGame("");
        Boolean isOver = gameCommunication.isOver();
        tryDelete(Paths.get(END_GAME));

        assertTrue(isOver);
    }

    @Test
    public void isOverFalse() {
        GameCommunication gameCommunication = new GameCommunication("test");
        tryDelete(Paths.get(END_GAME));

        assertFalse(gameCommunication.isOver());
    }

    @Test
    public void makeMove() throws IOException {
        String playerName = "test";
        Files.write(Paths.get(playerFile(playerName)), new ArrayList<>());
        Move move = new Move(FRIENDLY, 0, 0);
        GameCommunication gameCommunication = new GameCommunication(playerName);
        gameCommunication.writeMove(move);

        List<String> moveFile = Files.readAllLines(Paths.get(MOVE_FILE));
        tryDelete(Paths.get(MOVE_FILE));

        assertEquals(singletonList("test a 0"), moveFile);
    }

    @Test
    public void getEndReasonFiveInARow() {
        addEndGame("END: winner WINS! loser LOSES! Five in a row!");
        EndReason endReason = new GameCommunication("").getEndReason();
        assertEquals(FIVE_IN_ROW, endReason);
    }

    @Test
    public void getEndReasonOutOfTime() {
        addEndGame("END: winner WINS! loser LOSES! Time out!");
        EndReason endReason = new GameCommunication("").getEndReason();
        assertEquals(OUT_OF_TIME, endReason);
    }

    @Test
    public void getEndReasonOutOfOrder() {
        addEndGame("END: winner WINS! loser LOSES! Out-of-order move!");
        EndReason endReason = new GameCommunication("").getEndReason();
        assertEquals(OUT_OF_ORDER, endReason);
    }

    @Test
    public void getEndReasonInvalidMove() {
        addEndGame("END: winner WINS! loser LOSES! Invalid move!");
        EndReason endReason = new GameCommunication("").getEndReason();
        assertEquals(INVALID_MOVE, endReason);
    }

    @Test
    public void getOutcomeWin() {
        addEndGame("END: TEST WINS! loser LOSES! Invalid move!");
        Outcome outcome = new GameCommunication("TEST").getOutcome();
        assertEquals(WIN, outcome);
    }

    @Test
    public void getOutcomeLoss() {
        addEndGame("END: winner WINS! TEST LOSES! Invalid move!");
        Outcome outcome = new GameCommunication("TEST").getOutcome();
        assertEquals(LOSS, outcome);
    }

    @Test
    public void getOutcomeDraw() {
        addEndGame("END: TIE! board full!");
        Outcome outcome = new GameCommunication("TEST").getOutcome();
        assertEquals(DRAW, outcome);
    }

    @Test
    public void waitForOpponentMove() throws IOException {
        String playerName = "testPlayerName";
        Path path = Paths.get(playerFile(playerName));
        long delay = 1000;
        Timer timer = new Timer();
        tryDelete(path);
        long startTime = System.nanoTime();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Files.write(path, new ArrayList<>());
                } catch (IOException e) {
                    fail(e.getMessage());
                } finally {
                    timer.cancel();
                }
            }
        }, delay);
        new GameCommunication(playerName).waitForTurn();
        long endTime = System.nanoTime();
        tryDelete(path);

        assertEquals(delay / 100, (endTime - startTime) / 100000000);
    }

    private void addEndGame(String line) {
        try {
            Files.write(Paths.get(END_GAME), singletonList(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tryDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException ignored) {
        }
    }
}