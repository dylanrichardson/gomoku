package gomoku;

import java.util.OptionalDouble;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.DoubleStream;

import static gomoku.Stone.FRIENDLY;
import static gomoku.Stone.OPPONENT;


class MiniMax implements Algorithm {

    private final Integer winLength;

    MiniMax(Integer winLength) {
        this.winLength = winLength;
    }

    @Override
    public Double evaluateMove(Move move, Board board) {
        System.out.println(move);
        Board newBoard = board.withMove(move);
        if (move.getStone() == FRIENDLY) {
            return getMinValue(newBoard, "");
        } else {
            return getMaxValue(newBoard, "");
        }
    }

    private Double getMinValue(Board board, String tab) {
        return getExtremeValue(FRIENDLY, board, DoubleStream::min, this::getMaxValue, tab + "   ");
    }

    private Double getMaxValue(Board board, String tab) {
        return getExtremeValue(OPPONENT, board, DoubleStream::max, this::getMinValue, tab + "   ");
    }

    private Double getExtremeValue(
            Stone stone,
            Board board,
            Function<DoubleStream, OptionalDouble> getValue,
            BiFunction<Board, String, Double> getNextExtremeValue,
            String tab) {

        if (board.isTerminal(winLength)) {
            Stone winner = board.getWinner(winLength);
            if (winner == FRIENDLY)
                return WIN_VALUE;
            if (winner == OPPONENT)
                return LOSS_VALUE;
            return DRAW_VALUE;
        }

        // for debugging
        System.out.println(tab + stone.toString());

        DoubleStream moveValues = getPossibleMoves(stone.getOpponent(), board)
                .mapToDouble((move) -> {System.out.println(tab + move);return getNextExtremeValue.apply(board.withMove(move), tab);});
        OptionalDouble extremeValue = getValue.apply(moveValues);

        if (extremeValue.isPresent()) {
            System.out.println(tab + " - " + extremeValue.getAsDouble());
            return extremeValue.getAsDouble();
        } else {
            throw NO_MOVES_LEFT;
        }
    }
}
