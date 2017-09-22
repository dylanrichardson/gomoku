package gomoku;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static gomoku.Stone.FRIENDLY;

interface Algorithm {

    Double WIN_VALUE = 1.0;
    Double DRAW_VALUE = 0.0;
    Double LOSS_VALUE = -1.0;

    RuntimeException NO_MOVES_LEFT = new RuntimeException("No moves left to make");

    default Move chooseMove(Stone stone, Board board, Integer winLength, Double timeLimit) {
        Long startTime = System.nanoTime();
        Integer mod = (stone == FRIENDLY) ? 1 : -1;
        Move bestMove = null;
        Double bestScore = LOSS_VALUE;
        List<Move> moves = getPossibleMoves(stone, board).collect(Collectors.toList());

        for (int i = 0; i < moves.size(); i++) {
            Double newTimeLimit = getTimeLeft(startTime, timeLimit) / (moves.size() - i);

            Double score = mod * evaluateMove(moves.get(i), board, winLength, newTimeLimit);
            if (score > bestScore) {
                bestScore = score;
                bestMove = moves.get(i);
                if (score.equals(WIN_VALUE)) {
                    break;
                }
            }
        }

        if (bestMove == null)
            throw NO_MOVES_LEFT;
        return bestMove;
    }

    default Double getTimeLeft(Long startTime, Double timeLeft) {
        return timeLeft - (System.nanoTime() - startTime);
    }

    default Stream<Move> getPossibleMoves(Stone stone, Board board) {
        return board
                .getOpenCells()
                .stream()
                .map(cell -> new Move(stone, cell.fst, cell.snd));
    }

    Double evaluateMove(Move move, Board board, Integer winLength, Double timeLimit);
}
