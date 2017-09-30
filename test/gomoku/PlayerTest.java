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
import static gomoku.Player.TIME_LIMIT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class PlayerTest {


    private final String playerA = "A";
    private Result resultA;
    private final String playerB = "B";
    private Result resultB;
    private Throwable exception;

//    @Test
//    public void play3x3() {
//        play(3, 3, 3);
//
//        assertNull(exception);
//        assertEquals(DRAW, resultA.getOutcome());
//        assertEquals(DRAW, resultB.getOutcome());
//        assertEquals(9, resultA.getMoves().size());
//        assertEquals(9, resultB.getMoves().size());
//    }
//
//
    @Test
    public void play15x15() {
        play(15, 15, 5);

        assertNull(exception);
        assertEquals(DRAW, resultA.getOutcome());
        assertEquals(DRAW, resultB.getOutcome());
        assertEquals(15*15, resultA.getMoves().size());
        assertEquals(15*15, resultB.getMoves().size());
    }

    private void play(Integer width, Integer height, Integer winLength) {
        cleanUpGame();
        refGame(width, height, winLength);
        cleanUpGame();
    }

    private void refGame(Integer width, Integer height, Integer winLength) {
        try {
            Thread threadA = startPlayer(playerA, width, height, winLength, TIME_LIMIT);
            Thread threadB = startPlayer(playerB, width, height, winLength, TIME_LIMIT);

            Process p = Runtime.getRuntime().exec("python test/referee.py " + playerA + " " + playerB + " "
                    + width + " " + height + " " + winLength);

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

    private Thread startPlayer(String playerName, Integer width, Integer height, Integer winLength, Double timeout) {
        Thread thread = new Thread(() -> {
            Result result = new Player(new GameCommunication(playerName), new Board(width, height), winLength, timeout, new Algorithm()).play();
            Debug.print(result);
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
        tryDelete("history_file");
    }

    private void tryDelete(String path) {
        try {
            Files.delete(Paths.get(path));
        } catch (IOException ignored) {
        }
    }

    class StreamGobbler extends Thread {
        final InputStream is;

        StreamGobbler(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null)
                    System.out.println(line);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

}