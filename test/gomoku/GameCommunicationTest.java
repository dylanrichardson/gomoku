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

import static gomoku.GameCommunication.*;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class GameCommunicationTest {
    @Test
    public void isOverTrue() throws IOException {
        GameCommunication gameCommunication = new GameCommunication("test");
        addEndGame();
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
    public void makeMove() throws InterruptedException, IOException {
        String playerName = "test";
        Move move = new Move(playerName, 0, 0);
        GameCommunication gameCommunication = new GameCommunication(playerName);
        gameCommunication.makeMove(move);
        List<String> move_file = Files.readAllLines(Paths.get(MOVE_FILE));
        tryDelete(Paths.get(MOVE_FILE));

        assertEquals(singletonList(move.toString()), move_file);
    }

    @Test
    public void getEndReason() {
        // TODO
    }

    @Test
    public void getOutcome() {
        // TODO
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
        new GameCommunication(playerName).waitForOpponentMove();
        long endTime = System.nanoTime();
        tryDelete(path);

        assertEquals(delay / 100, (endTime - startTime) / 100000000);
    }

    private void addEndGame() throws IOException {
        Files.write(Paths.get(END_GAME), new ArrayList<>());
    }

    private void tryDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException ignored) {
        }
    }
}