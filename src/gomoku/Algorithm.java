package gomoku;

import java.util.Comparator;
import java.util.stream.Stream;

interface Algorithm {
    default Move chooseMove(String playerName, Board board) {

        return getPossibleMoves(playerName, board)
                .max(Comparator.comparingInt(move -> evaluate(playerName, move, board)))
                .orElseThrow(() -> new RuntimeException("No moves left to make"));
    }

    default Stream<Move> getPossibleMoves(String playerName, Board board) {
        return board
                .getOpenCells().stream()
                .map((cell) -> new Move(playerName, cell.fst, cell.snd));
    }

    default Integer evaluateBoard(Board board) {
        // TODO
        return 0;
    }

    Integer evaluate(String playerName, Move move, Board board);
}
