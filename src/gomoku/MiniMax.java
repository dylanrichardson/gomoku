package gomoku;


import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

class MiniMax implements Algorithm {

    private final Integer winLength;

    MiniMax(Integer winLength) {
        this.winLength = winLength;
    }

    @Override
    public Integer evaluate(String playerName, Move move, Board board) {
        return getMinValue(playerName, board.withMove(move));
    }

    private Integer getMinValue(String playerName, Board board) {
        return getExtremeValue(playerName, board, IntStream::min, this::getMaxValue);
    }

    private Integer getMaxValue(String playerName, Board board) {
        return getExtremeValue(playerName, board, IntStream::max, this::getMinValue);
    }

    private Integer getExtremeValue(
            String playerName,
            Board board,
            Function<IntStream, OptionalInt> getValue,
            BiFunction<String, Board, Integer> getNextExtremeValue) {

        if (board.isTerminal(winLength)) {
            return evaluateBoard(board);
        }

        IntStream moveValues = getPossibleMoves(playerName, board)
                .mapToInt((move) -> getNextExtremeValue.apply(playerName, board.withMove(move)));
        OptionalInt extremeValue = getValue.apply(moveValues);

        if (extremeValue.isPresent()) {
            return extremeValue.getAsInt();
        } else {
            throw new RuntimeException("No moves left to make");
        }
    }
}
