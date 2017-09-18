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


    private String playerA = "A";
    private Result resultA;
    private String playerB = "B";
    private Result resultB;
    private Throwable exception;

    @Test
    public void play() {
        cleanUpGame();
        startPlayer(playerA);
        startPlayer(playerB);
        refGame(playerA, playerB);
        cleanUpGame();

        assertNull(exception);
        assertEquals(DRAW, resultA.getOutcome());
        assertEquals(DRAW, resultB.getOutcome());
    }

    private void refGame(String playerA, String playerB) {
        try {
            Process p = Runtime.getRuntime().exec("python test/referee.py " + playerA + " " + playerB + " 3 3 3");
            new StreamGobbler(p.getErrorStream()).start();
            new StreamGobbler(p.getInputStream()).start();
            p.waitFor();
            assertEquals(0, p.exitValue());
        } catch (IOException | InterruptedException e) {
            cleanUpGame();
            fail(e.getMessage());
        }
    }

    private void startPlayer(String playerName) {
        Thread thread = new Thread(() -> {
            Result result = new Player(playerName, 3, 3, 3, new MiniMax()).play();
            if (playerName.equals(playerA))
                resultA = result;
            else
                resultB = result;
        });
        thread.setUncaughtExceptionHandler((th, ex) -> exception = ex);
        thread.start();
    }

    private void cleanUpGame() {
        tryDelete(playerA + ".go");
        tryDelete(playerB + ".go");
        tryDelete(MOVE_FILE);
        tryDelete(END_GAME);
    }

    class StreamGobbler extends Thread {
        InputStream is;

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
                    System.out.println(line);
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