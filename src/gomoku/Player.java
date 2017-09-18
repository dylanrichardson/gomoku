package gomoku;

import java.util.ArrayList;
import java.util.List;

import static gomoku.Stone.FRIENDLY;

class Player {

    private static final Integer WIN_LENGTH = 5;
    private static final Integer BOARD_SIDE = 15;

    private final GameCommunication gameCommunication;
    private final List<Move> moves;
    private final Integer winLength;

    // mutable
    private Board board;
    private Algorithm algorithm;

    private Player(GameCommunication gameCommunication, Board board, Integer winLength, Algorithm algorithm) {
        this.gameCommunication = gameCommunication;
        this.board = board;
        this.moves = new ArrayList<>();
        this.winLength = winLength;
        this.algorithm = algorithm;
    }

    Player(String playerName) {
        this(playerName, BOARD_SIDE, BOARD_SIDE, WIN_LENGTH, new MiniMax());
    }

    Player(String playerName, Integer width, Integer height, Integer winLength, Algorithm algorithm) {
        this(new GameCommunication(playerName), new Board(width, height), winLength, algorithm);
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
        Move move = algorithm.chooseMove(FRIENDLY, board, winLength);
        moves.add(move);
        board = board.withMove(move);
        gameCommunication.writeMove(move);
    }

    private Result getResult(Long duration) {
        return new Result(gameCommunication.getOutcome(), gameCommunication.getEndReason(), board, moves, duration);
    }
}
