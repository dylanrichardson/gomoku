package gomoku;

import com.sun.tools.javac.util.Pair;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static gomoku.Stone.FRIENDLY;

interface Algorithm {

    Double WIN_VALUE = 1.0;
    Double DRAW_VALUE = 0.0;
    Double LOSS_VALUE = -1.0;

    RuntimeException NO_MOVES_LEFT = new RuntimeException("No moves left to make");

    default Move chooseMove(Stone stone, Board board, Integer winLength) {
        Integer mod = (stone == FRIENDLY) ? 1 : -1;
        Move bestMove = null;
        Double bestScore = LOSS_VALUE;
        List<Move> moves = getPossibleMoves(stone, board).collect(Collectors.toList());
        for (Move move : moves) {
            Double score = mod * evaluateMove(move, board, winLength);
            if (score > bestScore) {
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

    default Stream<Move> getPossibleMoves(Stone stone, Board board) {
        return board
                .getOpenCells()
                .stream()
                .map(cell -> new Move(stone, cell.fst, cell.snd));
    }

    Double evaluateMove(Move move, Board board, Integer winLength);
}
