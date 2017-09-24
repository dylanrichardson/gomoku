package gomoku;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;


class Algorithm {

    static final Double WIN_VALUE = 1.0;
    static final Double DRAW_VALUE = 0.0;
    static final Double LOSS_VALUE = -1.0;

    private static final RuntimeException NO_MOVES_LEFT = new RuntimeException("No moves left to make");
    private static final Double MIN_TIME_LIMIT = 50000.0; // ns

    Move chooseMove(Stone stone, Board board, Integer winLength, Double timeLimit) {
        long startTime = System.nanoTime();
        Supplier<Double> timeLeft = () -> getTimeLeft(startTime, timeLimit);

        Integer mod = (stone == FRIENDLY) ? 1 : -1;
        Move bestMove = null;
        Double bestScore = LOSS_VALUE;
        List<Move> moves = getPossibleMoves(stone, board).collect(Collectors.toList());

        for (int i = 0; i < moves.size(); i++) {
            Double newTimeLimit = timeLeft.get() / (moves.size() - i);

            Double score = mod * evaluateMove(moves.get(i), board, winLength, newTimeLimit);
            if (score > bestScore) {
                bestScore = score;
                bestMove = moves.get(i);
                if (score.equals(WIN_VALUE)) {
                    break;
                }
            }
        }

        if (bestMove == null)
            throw NO_MOVES_LEFT;
        return bestMove;
    }

    Double evaluateMove(Move move, Board board, Integer winLength, Double timeLimit) {
        Debug.debug(move);
        long startTime = System.nanoTime();
        Supplier<Double> timeLeft = () -> getTimeLeft(startTime, timeLimit);

        Boolean isFriendly = move.getStone() == FRIENDLY;
        // check if blocking win
        if (board.isBlockingWin(move, winLength)) {
            return isFriendly ? WIN_VALUE : LOSS_VALUE;
        }

        Board newBoard = board.withMove(move);
        if (isFriendly) {
            return getMinValue(newBoard, "", winLength, timeLeft.get());
        } else {
            return getMaxValue(newBoard, "", winLength, timeLeft.get());
        }
    }

    Double getHeuristic(Board board) {
        return DRAW_VALUE;
    }

    private Double getTimeLeft(Long startTime, Double timeLeft) {
        return timeLeft - (System.nanoTime() - startTime);
    }

    private Stream<Move> getPossibleMoves(Stone stone, Board board) {
        return board
                .getOpenCells()
                .stream()
                .map(cell -> new Move(stone, cell.fst, cell.snd));
    }

    private Double getMinValue(Board board, String tab, Integer winLength, Double timeLimit) {
        return getExtremeValue(FRIENDLY, board, this::getMaxValue, tab + "   ", winLength, timeLimit);
    }

    private Double getMaxValue(Board board, String tab, Integer winLength, Double timeLimit) {
        return getExtremeValue(OPPONENT, board, this::getMinValue, tab + "   ", winLength, timeLimit);
    }

    private Double getExtremeValue(
            Stone stone,
            Board board,
            QuadFunction<Board, String, Integer, Double, Double> getNextExtremeValue,
            String tab,
            Integer winLength,
            Double timeLimit) {

        long startTime = System.nanoTime();
        Supplier<Double> timeLeft = () -> getTimeLeft(startTime, timeLimit);

        // check if terminal or out of time
        Double boardValue = board.getValue(winLength, timeLeft.get(), this::getHeuristic);
        if (boardValue != null) {
            return boardValue;
        }

        Debug.debug(tab + stone.toString());

        Integer mod = (stone == OPPONENT) ? 1 : -1;
        Double extremeScore = LOSS_VALUE * mod;
        Move extremeMove = null;
        List<Move> moves = getPossibleMoves(stone.getOpponent(), board).collect(Collectors.toList());

        for (int i = 0; i < moves.size(); i++) {
            Debug.debug(tab + moves.get(i));
            Double newTimeLimit = timeLeft.get() / (moves.size() - i);
            if (newTimeLimit < MIN_TIME_LIMIT)
                return getHeuristic(board);

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
