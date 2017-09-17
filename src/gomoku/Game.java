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
        this(new GameCommunication(playerName), new Board(BOARD_SIDE, BOARD_SIDE), new Player(playerName, WIN_LENGTH));
    }

    Result play() {
        // TODO handle special 2nd move of game
        while (true) {
            gameCommunication.waitForOpponentMove();
            if (gameCommunication.isOver()) {
                return getResult();
            }
            makeMove();
        }
    }

    private void makeMove() {
        Move move = player.getNextMoveOn(board);
        moves.add(move);
        board = board.withMove(move);
        gameCommunication.makeMove(move);
    }

    private Result getResult() {
        return new Result(gameCommunication.getOutcome(), gameCommunication.getEndReason(), board, moves);
    }
}
