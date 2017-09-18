package gomoku;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static gomoku.GameCommunication.END_GAME;
import static gomoku.GameCommunication.MOVE_FILE;
import static org.junit.Assert.*;

public class GameTest {


    private String playerA = "A";
    private String playerB = "B";
    private Throwable exception;

    @Test
    public void play() {
        cleanUpGame();
        try {
            startGame(playerA);
            startGame(playerB);
            Process p = Runtime.getRuntime().exec("python test/referee.py " + playerA + " " + playerB + " 3 3 3");
            new StreamGobbler(p.getErrorStream()).start();
            new StreamGobbler(p.getInputStream()).start();
            p.waitFor();
            assertEquals(0, p.exitValue());
            assertNull(exception);
        } catch (IOException | InterruptedException e) {
            fail(e.getMessage());
        } finally {
            cleanUpGame();
        }
    }

    private void startGame(String playerName) {
        Thread thread = new Thread(() -> new Game(playerName, 3, 3, 3, new MiniMax()).play());
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