package gomoku;

class Player {

    private final String name;
    private final Algorithm algorithm;

    private Player(String name, Algorithm algorithm) {
        this.name = name;
        this.algorithm = algorithm;
    }

    Player(String name, Integer winLength) {
        this(name, new MiniMax(winLength));
    }

    Move getNextMoveOn(Board board) {
        return algorithm.chooseMove(name, board);
    }
}
