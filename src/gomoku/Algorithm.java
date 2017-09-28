package gomoku;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static gomoku.Stone.FRIENDLY;


class Algorithm {

    static final Double WIN_VALUE = 1.0;
    static final Double BLOCK_WIN_VALUE = 0.5;
    static final Double BLOCK_2ND_WIN_VALUE = 0.25;
    static final Double DRAW_VALUE = 0.0;

    private static final RuntimeException NO_MOVES_LEFT = new RuntimeException("No moves left to make");
    private static final Double MIN_TIME_LIMIT = 10000.0; // ns

    Move chooseMove(Stone stone, Board board, Integer winLength, Double timeLimit) {
        long startTime = System.nanoTime();
        Supplier<Double> timeLeft = () -> getTimeLeft(startTime, timeLimit);

        Integer mod = (stone == FRIENDLY) ? 1 : -1;
        Move bestMove = null;
        Double bestScore = -1 * WIN_VALUE;
        // TODO get terminal moves if possible
        List<Move> moves = getPossibleMoves(stone, board);

        for (int i = 0; i < moves.size(); i++) {
            Double newTimeLimit = timeLeft.get() / (moves.size() - i);
            Move move = moves.get(i);

            Double score = mod * evaluateMove(move, board, winLength, newTimeLimit);
            if (score >= bestScore) {
                bestScore = score;
                bestMove = move;
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
        long startTime = System.nanoTime();
        Debug.debug(move);

        int mod = move.getStone() == FRIENDLY ? 1 : -1;
        // check if win
        if (board.willBeWin(move, winLength)) {
            return WIN_VALUE * mod;
        }
        // check if block
        if (board.willBeBlock(move, winLength)) {
            return BLOCK_WIN_VALUE * mod;
        }
        // check if two before loss
        if (board.willBeBlockIn2Moves(move, winLength)) {
            return BLOCK_2ND_WIN_VALUE * mod;
        }

        Board newBoard = board.withMove(move);
        Double extremeValue = getExtremeValue(move.getStone().getOpponent(), newBoard, winLength, getTimeLeft(startTime, timeLimit));
        if (extremeValue > DRAW_VALUE) Debug.debug("\nboard: " + board + "move: " + move + "\nvalue: " + extremeValue + "");
        return extremeValue;
    }

    private Double getExtremeValue(Stone stone, Board board, Integer winLength, Double timeLimit) {
        long startTime = System.nanoTime();

        if (board.isDraw()) {
            return DRAW_VALUE;
        }

        Integer mod = (stone == FRIENDLY) ? 1 : -1;
        Double extremeScore = -1 * WIN_VALUE;
        Move extremeMove = null;
        List<Move> moves = getPossibleMoves(stone, board);

        for (int i = 0; i < moves.size(); i++) {
            Double newTimeLimit = getTimeLeft(startTime, timeLimit) / (moves.size() - i);
            // check if out of time
            if (newTimeLimit < MIN_TIME_LIMIT)
                return getHeuristic(board, extremeMove);

            Move move = moves.get(i);
            // recurse in minimax
            Double score = mod * evaluateMove(move, board, winLength, newTimeLimit);
            if (score >= extremeScore) {
                extremeScore = score;
                extremeMove = move;
                if (score.equals(WIN_VALUE)) {
                    break;
                }
            }
        }

        if (extremeMove == null)
            throw NO_MOVES_LEFT;
        return extremeScore * mod;
    }

    Double getHeuristic(Board board, Move move) {
        return DRAW_VALUE;
    }

    List<Move> getPossibleMoves(Stone stone, Board board) {
        // TODO order the moves
        return board
                .getOpenCells()
                .stream()
                .map(cell -> new Move(stone, cell.fst, cell.snd))
                .collect(Collectors.toList());
    }

    private Double getTimeLeft(Long startTime, Double timeLeft) {
        return timeLeft - (System.nanoTime() - startTime);
    }

    @FunctionalInterface
    private interface QuadFunction<A,B,C,D,R> {
        R apply(A a, B b, C c, D d);
    }
}
