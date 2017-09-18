package gomoku;

import java.util.ArrayList;
import java.util.List;

class Game {

    private static final Integer WIN_LENGTH = 5;
    private static final Integer BOARD_SIDE = 5;

    private final GameCommunication gameCommunication;
    private final List<Move> moves;
    private final Player player;

    // mutable
    private Board board;

    private Game(GameCommunication gameCommunication, Board board, Player player) {
        this.gameCommunication = gameCommunication;
        this.board = board;
        this.player = player;
        this.moves = new ArrayList<>();
    }

    Game(String playerName) {
        this(new GameCommunication(playerName), new Board(BOARD_SIDE, BOARD_SIDE), new Player(WIN_LENGTH));
    }

    Result play() {
        // TODO handle special 2nd move of game
        Long startTime = System.nanoTime();
        while (true) {
            readMove();
            if (gameCommunication.isOver()) {
                return getResult(System.nanoTime() - startTime);
            }
            makeMove();
        }
    }

    private void readMove() {
        Move move = gameCommunication.waitForOpponentMove();
        if (board.isValidMove(move)) {
            moves.add(move);
            board = board.withMove(move);
        }
    }

    private void makeMove() {
        Move move = player.getNextMoveOn(board);
        moves.add(move);
        board = board.withMove(move);
        gameCommunication.writeMove(move);
    }

    private Result getResult(Long duration) {
        return new Result(gameCommunication.getOutcome(), gameCommunication.getEndReason(), board, moves, duration);
    }
}
