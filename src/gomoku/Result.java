package gomoku;

import java.util.List;

class Result {

    private final Outcome outcome;
    private final EndReason endReason;
    private final Board board;
    private final List<Move> moves;

    Result(Outcome outcome, EndReason endReason, Board board, List<Move> moves) {
        this.outcome = outcome;
        this.endReason = endReason;
        this.board = board;
        this.moves = moves;
    }

    Outcome getOutcome() {
        return outcome;
    }

    EndReason getEndReason() { return endReason; }

    Board getBoard() {
        return board;
    }

    List<Move> getMoves() {
        return moves;
    }
}
