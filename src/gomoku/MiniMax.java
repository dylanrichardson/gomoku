package gomoku;

import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.DoubleStream;

import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;


class MiniMax implements Algorithm {

    @Override
    public Double evaluateMove(Move move, Board board, Integer winLength) {
//        Debug.print(move);
        Board newBoard = board.withMove(move);
        if (move.getStone() == FRIENDLY) {
            return getMinValue(newBoard, "", winLength);
        } else {
            return getMaxValue(newBoard, "", winLength);
        }
    }

    private Double getMinValue(Board board, String tab, Integer winLength) {
        return getExtremeValue(FRIENDLY, board, DoubleStream::min, this::getMaxValue, tab + "   ", winLength);
    }

    private Double getMaxValue(Board board, String tab, Integer winLength) {
        return getExtremeValue(OPPONENT, board, DoubleStream::max, this::getMinValue, tab + "   ", winLength);
    }

    private Double getExtremeValue(
            Stone stone,
            Board board,
            Function<DoubleStream, OptionalDouble> getValue,
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

//        Debug.print(tab + stone.toString());

        DoubleStream moveValues = getPossibleMoves(stone.getOpponent(), board).mapToDouble((move) -> {
//            Debug.print(tab + move);
            return getNextExtremeValue.apply(board.withMove(move), tab, winLength);
        });
        OptionalDouble extremeValue = getValue.apply(moveValues);

        if (extremeValue.isPresent()) {
//            Debug.print(tab + " - " + extremeValue.getAsDouble());
            return extremeValue.getAsDouble();
        } else {
            throw NO_MOVES_LEFT;
        }
    }

    @FunctionalInterface
    interface TriFunction<A,B,C,R> {
        R apply(A a, B b, C c);
    }
}
