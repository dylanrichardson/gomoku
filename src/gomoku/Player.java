package gomoku;

import java.util.Collection;
import java.util.stream.Collectors;

class Player {

    private final String name;

    Player(String name) {
        this.name = name;
    }

    Move getNextMoveOn(Board board) {
        Collection<Move> possibleMoves = board
                .getOpenCells()
                .stream()
                .map((cell) -> new Move(name, cell.fst, cell.snd))
                .collect(Collectors.toList());
        Algorithm algorithm = new MiniMax();
        return algorithm.chooseMove(possibleMoves, board);
    }
}
