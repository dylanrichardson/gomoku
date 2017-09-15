package gomoku;

import java.util.Collection;

interface Algorithm {
    Move chooseMove(Collection<Move> possibleMoves, Board board);
}
