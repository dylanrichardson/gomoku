package gomoku;

import java.util.List;

class Result {

    private final Outcome outcome;
    private final EndReason endReason;
    private final Board board;
    private final List<Move> moves;
    private final Long duration;
    private final String playerName;

    Result(String playerName, Outcome outcome, EndReason endReason, Board board, List<Move> moves, Long duration) {
        this.playerName = playerName;
        this.outcome = outcome;
        this.endReason = endReason;
        this.board = board;
        this.moves = moves;
        this.duration = duration;
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

    Long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return  "Player: " + playerName + "\n" +
                "Outcome: " + outcome + "\n" +
                "End reason: " + endReason + "\n" +
                "Duration: " + duration + "\n" +
                "Board: \n" + board + "\n" +
                "Moves: " + moves + "\n";
    }
}
