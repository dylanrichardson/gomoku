package gomoku;

import static gomoku.Stone.FRIENDLY;

class Player {

    private final Algorithm algorithm;

    private Player(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    Player(Integer winLength) {
        this(new MiniMax(winLength));
    }

    Move getNextMoveOn(Board board) {
        return algorithm.chooseMove(FRIENDLY, board);
    }
}
