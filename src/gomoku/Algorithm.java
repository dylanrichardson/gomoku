package gomoku;

import java.util.Comparator;
import java.util.stream.Stream;

import static gomoku.Stone.FRIENDLY;

interface Algorithm {

    Double WIN_VALUE = 1.0;
    Double DRAW_VALUE = 0.0;
    Double LOSS_VALUE = -1.0;

    RuntimeException NO_MOVES_LEFT = new RuntimeException("No moves left to make");

    default Move chooseMove(Stone stone, Board board) {
        Integer MOD = (stone == FRIENDLY) ? 1 : -1;
        return getPossibleMoves(stone, board)
                .max(Comparator.comparingDouble(move -> evaluateMove(move, board) * MOD))
                .orElseThrow(() -> NO_MOVES_LEFT);
    }

    default Stream<Move> getPossibleMoves(Stone stone, Board board) {
        return board
                .getOpenCells()
                .stream()
                .map(cell -> new Move(stone, cell.fst, cell.snd));
    }

    Double evaluateMove(Move move, Board board);
}
