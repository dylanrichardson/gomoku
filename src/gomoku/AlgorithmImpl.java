package gomoku;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;


class AlgorithmImpl implements Algorithm {

    private ExecutorService threadPool = Executors.newCachedThreadPool();

    @Override
    public Double evaluateMove(Move move, Board board, Integer winLength, Double timeLimit) {
        Long startTime = System.nanoTime();
        Boolean isFriendly = move.getStone() == FRIENDLY;
        if (blocksWin(move, board)) {
            return isFriendly ? WIN_VALUE : LOSS_VALUE;
        }
        Debug.debug(move);
        Board newBoard = board.withMove(move);
        if (isFriendly) {
            return getMinValue(newBoard, "", winLength, getTimeLeft(startTime, timeLimit));
        } else {
            return getMaxValue(newBoard, "", winLength, getTimeLeft(startTime, timeLimit));
        }
    }

    private Boolean blocksWin(Move move, Board board) {
        // TODO
        return false;
    }

    private Double getMinValue(Board board, String tab, Integer winLength, Double timeLimit) {
        return getExtremeValue(FRIENDLY, board, false, this::getMaxValue, tab + "   ", winLength, timeLimit);
    }

    private Double getMaxValue(Board board, String tab, Integer winLength, Double timeLimit) {
        return getExtremeValue(OPPONENT, board, true, this::getMinValue, tab + "   ", winLength, timeLimit);
    }

    private Double getExtremeValue(
            Stone stone,
            Board board,
            Boolean positive,
            QuadFunction<Board, String, Integer, Double, Double> getNextExtremeValue,
            String tab,
            Integer winLength,
            Double timeLimit) {

        Long startTime = System.nanoTime();

        Future<Boolean> future = threadPool.submit(() -> board.isTerminal(winLength));
        try {
            Boolean isTerminal = future.get(getTimeLeft(startTime, timeLimit).longValue(), TimeUnit.NANOSECONDS);
            if (isTerminal) {
                Stone winner = board.getWinner(winLength);
                if (winner == FRIENDLY)
                    return WIN_VALUE;
                if (winner == OPPONENT)
                    return LOSS_VALUE;
                return DRAW_VALUE;
            }
        } catch (TimeoutException ex) {
            Debug.debug("Out of time");
            // TODO add heuristic
            return DRAW_VALUE;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return DRAW_VALUE;
        } finally {
            future.cancel(true);
        }

        Debug.debug(tab + stone.toString());

        Integer mod = (positive) ? 1 : -1;
        Double extremeScore = LOSS_VALUE * mod;
        Move extremeMove = null;
        List<Move> moves = getPossibleMoves(stone.getOpponent(), board).collect(Collectors.toList());

        for (int i = 0; i < moves.size(); i++) {
            Debug.debug(tab + moves.get(i));
            Double newTimeLimit = getTimeLeft(startTime, timeLimit) / (moves.size() - i);

            Double score = mod * getNextExtremeValue.apply(board.withMove(moves.get(i)), tab, winLength, newTimeLimit);
            if (score >= extremeScore * mod) {
                extremeScore = score;
                extremeMove = moves.get(i);
                if (score.equals(WIN_VALUE)) {
                    break;
                }
            }
        }

        if (extremeMove == null)
            throw NO_MOVES_LEFT;
        Debug.debug(tab + " score: " + extremeScore);
        return extremeScore * mod;
    }

    @FunctionalInterface
    interface QuadFunction<A,B,C,D,R> {
        R apply(A a, B b, C c, D d);
    }
}
