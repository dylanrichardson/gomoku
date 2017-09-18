package gomoku;

import static gomoku.Stone.FRIENDLY;

class Player {

    private final Algorithm algorithm;

    Player(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    Player() {
        this(new MiniMax());
    }

    Move getNextMoveOn(Board board, Integer winLength) {
        return algorithm.chooseMove(FRIENDLY, board, winLength);
    }
}
