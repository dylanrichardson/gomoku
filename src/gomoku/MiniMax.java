package gomoku;

import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;


class MiniMax implements Algorithm {

    @Override
    public Double evaluateMove(Move move, Board board, Integer winLength) {
        Debug.debug(move);
        Board newBoard = board.withMove(move);
        if (move.getStone() == FRIENDLY) {
            return getMinValue(newBoard, "", winLength);
        } else {
            return getMaxValue(newBoard, "", winLength);
        }
    }

    private Double getMinValue(Board board, String tab, Integer winLength) {
        return getExtremeValue(FRIENDLY, board, false, this::getMaxValue, tab + "   ", winLength);
    }

    private Double getMaxValue(Board board, String tab, Integer winLength) {
        return getExtremeValue(OPPONENT, board, true, this::getMinValue, tab + "   ", winLength);
    }

    private Double getExtremeValue(
            Stone stone,
            Board board,
            Boolean positive,
            TriFunction<Board, String, Integer, Double> getNextExtremeValue,
            String tab,
            Integer winLength) {

        if (board.isTerminal(winLength)) {
            Stone winner = board.getWinner(winLength);
            if (winner == FRIENDLY)
                return WIN_VALUE;
            if (winner == OPPONENT)
                return LOSS_VALUE;
            return DRAW_VALUE;
        }

        Debug.debug(tab + stone.toString());

        Integer mod = (positive) ? 1 : -1;
        Double extremeScore = LOSS_VALUE * mod;
        Move extremeMove = null;
        List<Move> moves = getPossibleMoves(stone.getOpponent(), board).collect(Collectors.toList());

        for (Move move : moves) {
            Debug.debug(tab + move);
            Double score = mod * getNextExtremeValue.apply(board.withMove(move), tab, winLength);
            if (score >= extremeScore * mod) {
                extremeScore = score;
                extremeMove = move;
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
    interface TriFunction<A,B,C,R> {
        R apply(A a, B b, C c);
    }
}
