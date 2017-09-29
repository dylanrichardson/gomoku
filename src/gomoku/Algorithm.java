package gomoku;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static gomoku.Stone.FRIENDLY;


class Algorithm {

    private static final Double WIN_VALUE = 1.0;
    private static final Double BLOCK_WIN_VALUE = 0.9;
    private static final Double COMBO_WIN_VALUE = 0.8;
    private static final Double BLOCK_COMBO_WIN_VALUE = 0.7;
    private static final Double TWO_AWAY_WIN_VALUE = 0.6;
    private static final Double BLOCK_TWO_AWAY_WIN_VALUE = 0.5;
    private static final Double HEURISTIC_FACTOR = 0.2;
    private static final Double DRAW_VALUE = 0.0;

    private static final RuntimeException NO_MOVES_LEFT = new RuntimeException("No moves left to make");
    private static final Double MIN_TIME_LIMIT = 50000.0; // ns

    Move chooseMove(Stone stone, Board board, Integer winLength,Double timeLimit) {
        long startTime = System.nanoTime();
        Supplier<Double> timeLeft = () -> getTimeLeft(startTime, timeLimit);

        Move bestMove = null;
        Double bestScore = -1 * WIN_VALUE;
        List<Move> moves = getPossibleMoves(stone, board);
        //reverse(moves);

        for (int i = 0; i < moves.size(); i++) {
            Double newTimeLimit = timeLeft.get() / (moves.size() - i);
            Move move = moves.get(i);
            Debug.debug(move);
            Double score = evaluateMove(move, board, winLength, -1 * WIN_VALUE, WIN_VALUE,newTimeLimit);

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

    Double evaluateMove(Move move, Board board, Integer winLength, Double alpha, Double beta, Double timeLimit) {
        long startTime = System.nanoTime();
        // heuristic strategy
        if (board.isWin(move, winLength)) {
            return WIN_VALUE;
        }
        if (board.isBlock(move, winLength)) {
            return BLOCK_WIN_VALUE;
        }
        if (board.isComboWin(move, winLength)) {
            return BLOCK_COMBO_WIN_VALUE;
        }
        if (board.isComboBlock(move, winLength)) {
            return BLOCK_COMBO_WIN_VALUE;
        }
        if (board.is2AwayWin(move, winLength)) {
            return TWO_AWAY_WIN_VALUE;
        }
        if (board.is2AwayBlock(move, winLength)) {
            return BLOCK_TWO_AWAY_WIN_VALUE;
        }
        // minimax
        Board newBoard = board.withMove(move);
        return HEURISTIC_FACTOR *  getExtremeValue(move.getStone().getOpponent(), newBoard, winLength, alpha, beta, getTimeLeft(startTime, timeLimit));
    }

    // minimax
    private Double getExtremeValue(Stone stone, Board board, Integer winLength, Double alpha, Double beta, Double timeLimit) {
        long startTime = System.nanoTime();

        if (board.isDraw()) {
            return DRAW_VALUE;
        }
        Boolean isFriendly = (stone == FRIENDLY);
        Integer mod = (isFriendly) ? 1 : -1;
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
            Double score = mod * evaluateMove(move, board, winLength, alpha, beta, newTimeLimit);

            if (score >= extremeScore) {
                extremeScore = score;
                extremeMove = move;
                if (score.equals(WIN_VALUE)) {
                    break;
                }
            }

            if(isFriendly) {
                if (score > alpha) {
                    alpha = score;
                }
            } else if(score < beta) {
                beta = score;
            }
            if (alpha > beta){
                break; // prune
            }
        }

        if (extremeMove == null)
            throw NO_MOVES_LEFT;
        return extremeScore * mod;
    }

    Double getHeuristic(Board board, Move move) {
        // TODO
        return DRAW_VALUE;
    }

    List<Move> getPossibleMoves(Stone stone, Board board) {
        // TODO order the moves
        return board
                .getOpenCells()
                .stream()
                .filter(cell -> nearbyStone(board, cell))
                .map(cell -> new Move(stone, cell))
                .collect(Collectors.toList());
    }

    // within 2 cells of stone
    private Boolean nearbyStone(Board board, Cell cell) {
        return Direction.all().anyMatch(dir ->
                board.isOccupiedCell(cell.translate(dir))
                || board.isOccupiedCell(cell.translate(dir, 2)));
    }

    private Double getTimeLeft(Long startTime, Double timeLeft) {
        return timeLeft - (System.nanoTime() - startTime);
    }

    @FunctionalInterface
    private interface QuadFunction<A,B,C,D,R> {
        R apply(A a, B b, C c, D d);
    }
}
