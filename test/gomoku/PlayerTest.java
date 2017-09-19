package gomoku;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static gomoku.GameCommunication.END_GAME;
import static gomoku.GameCommunication.MOVE_FILE;
import static gomoku.Outcome.DRAW;
import static org.junit.Assert.*;

public class PlayerTest {


    private final String playerA = "A";
    private Result resultA;
    private final String playerB = "B";
    private Result resultB;
    private Throwable exception;

    @Test
    public void play3x3MiniMax() {
        cleanUpGame();
        Thread threadA = startPlayer(playerA, new MiniMax());
        Thread threadB = startPlayer(playerB, new MiniMax());
        refGame(playerA, threadA, playerB, threadB);
        cleanUpGame();

        assertNull(exception);
        assertEquals(DRAW, resultA.getOutcome());
        assertEquals(DRAW, resultB.getOutcome());
    }

    private void refGame(String playerA, Thread threadA, String playerB, Thread threadB) {
        try {
            Process p = Runtime.getRuntime().exec("python test/referee.py " + playerA + " " + playerB + " 3 3 3");
            new StreamGobbler(p.getErrorStream()).start();
            new StreamGobbler(p.getInputStream()).start();
            p.waitFor();
            threadA.join();
            threadB.join();
            assertEquals(0, p.exitValue());
        } catch (IOException | InterruptedException e) {
            cleanUpGame();
            fail(e.getMessage());
        }
    }

    private Thread startPlayer(String playerName, Algorithm algorithm) {
        Thread thread = new Thread(() -> {
            Result result = new Player(playerName, 3, 3, 3, algorithm).play();
            if (playerName.equals(playerA))
                resultA = result;
            else
                resultB = result;
        });
        thread.setUncaughtExceptionHandler((th, e) -> {
            exception = e;
            e.printStackTrace();
        });
        thread.start();
        return thread;
    }

    private void cleanUpGame() {
        tryDelete(playerA + ".go");
        tryDelete(playerB + ".go");
        tryDelete(MOVE_FILE);
        tryDelete(END_GAME);
    }

    class StreamGobbler extends Thread {
        final InputStream is;

        // reads everything from is until empty.
        StreamGobbler(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ( (line = br.readLine()) != null)
                    Debug.print(line);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private void tryDelete(String path) {
        try {
            Files.delete(Paths.get(path));
        } catch (IOException ignored) {
        }
    }

}