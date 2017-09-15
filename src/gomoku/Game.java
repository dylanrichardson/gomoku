package gomoku;

import java.util.ArrayList;
import java.util.List;

class Game {
    private final GameCommunication gameCommunication;
    private Board board;
    private List<Move> moves;
    private Player player;

    Game(GameCommunication gameCommunication, Board board, Player player) {
        this.gameCommunication = gameCommunication;
        this.board = board;
        this.player = player;
        this.moves = new ArrayList<>();
    }

    Game(String playerName) {
        this(new GameCommunication(playerName), new Board(), new Player(playerName));
    }

    Result play() {
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
        board.makeMove(move);
        gameCommunication.makeMove(move);
    }

    private Result getResult() {
        return new Result(gameCommunication.getOutcome(), gameCommunication.getEndReason(), board, moves);
    }
}
