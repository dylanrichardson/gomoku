package gomoku;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static gomoku.Stone.FRIENDLY;
import static java.util.Collections.reverse;


class Algorithm {

    private static final Double WIN_VALUE = 1.0;
    private static final Double BLOCK_WIN_VALUE = 0.5;
    private static final Double BLOCK_2ND_WIN_VALUE = 0.25;
    private static final Double HEURISTIC_FACTOR = 0.2;
    private static final Double COMBO_BLOCK_VALUE = 0.1;
    private static final Double DRAW_VALUE = 0.0;

    private static final RuntimeException NO_MOVES_LEFT = new RuntimeException("No moves left to make");
    private static final Double MIN_TIME_LIMIT = 50000.0; // ns

    Move chooseMove(Stone stone, Board board, Integer winLength, Double timeLimit) {
        long startTime = System.nanoTime();
        Supplier<Double> timeLeft = () -> getTimeLeft(startTime, timeLimit);

        Move bestMove = null;
        Double bestScore = -1 * WIN_VALUE;
        List<Move> moves = getPossibleMoves(stone, board);
        reverse(moves);

        for (int i = 0; i < moves.size(); i++) {
            Double newTimeLimit = timeLeft.get() / (moves.size() - i);
            Move move = moves.get(i);

            Double score = evaluateMove(move, board, winLength, newTimeLimit);
            Debug.debug(move);
            Debug.debug(score);
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

        // check if win
        if (board.willBeWin(move, winLength)) {
            return WIN_VALUE;
        }
        // check if block
        if (board.willBeBlock(move, winLength)) {
            return BLOCK_WIN_VALUE;
        }
        // check if 2-away block
        if (board.willBeBlockIn2Moves(move, winLength)) {
            return BLOCK_2ND_WIN_VALUE;
        }
        // check combo of 2-away blocks
        if (board.willBeComboBlock(move, winLength)) {
            return COMBO_BLOCK_VALUE;
        }

        Board newBoard = board.withMove(move);
        return HEURISTIC_FACTOR *  getExtremeValue(move.getStone().getOpponent(), newBoard, winLength, getTimeLeft(startTime, timeLimit));
    }

    // minimax
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
            if (newTimeLimit < MIN_TIME_LIMIT) {
                return getHeuristic(board, extremeMove);
            }

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
