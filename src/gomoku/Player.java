package gomoku;

import java.util.ArrayList;
import java.util.List;
import static gomoku.Stone.FRIENDLY;

class Player {

    static final Double TIME_LIMIT = 9.0 * 1000000000; // 9 seconds

    private final GameCommunication gameCommunication;
    private final List<Move> moves;
    private final Integer winLength;
    private final Algorithm algorithm;
    private final Double timeLimit;

    // mutable
    private Board board;

    Player(GameCommunication gameCommunication, Board board, Integer winLength, Double timeLimit, Algorithm algorithm) {
        this.gameCommunication = gameCommunication;
        this.board = board;
        this.moves = new ArrayList<>();
        this.winLength = winLength;
        this.timeLimit = timeLimit;
        this.algorithm = algorithm;
    }

    Player(String playerName, Integer width, Integer height, Integer winLength) {
        this(playerName, width, height, winLength, TIME_LIMIT, new Algorithm());
    }

    private Player(String playerName, Integer width, Integer height, Integer winLength, Double timeLimit, Algorithm algorithm) {
        this(new GameCommunication(playerName), new Board(width, height), winLength, timeLimit, algorithm);
    }

    Result play() {
        // TODO handle special 2nd move of game
        Long startTime = System.currentTimeMillis();
        int round = 0;


        if (makeFirstMove())
            return getResult((System.currentTimeMillis() - startTime)  / 1000);

        while (true) {
            Debug.print("\n\nROUND - " + round++ + " " + (System.currentTimeMillis() - startTime) / 1000 + "s\n\n");
            Debug.print("Waiting...");
            gameCommunication.waitForTurn();
            readMove();
            if (gameCommunication.isOver()) {
                return getResult((System.currentTimeMillis() - startTime)  / 1000);
            }
            makeMove();
        }
    }

    // return true if game is over
    private Boolean makeFirstMove() {
        gameCommunication.waitForTurn();
        readMove();
        if (gameCommunication.isOver()) {
            return true;
        }
        if (moves.isEmpty()) {
            applyMove(new Move(FRIENDLY, board.getWidth() / 2, board.getHeight() / 2));
        } else {
            Move oppMove = moves.get(0);
            applyMove(new Move(FRIENDLY, oppMove.getColumn(), oppMove.getRow()));
        }
        return false;
    }

    private void readMove() {
        Move move = gameCommunication.readMove();
        if (board.isValidMove(move)) {
            moves.add(move);
            board = board.withMove(move);
        }
    }

    private void makeMove() {
        Debug.print(board);
        Debug.print("Choosing move...");
        Move move = algorithm.chooseMove(FRIENDLY, board, winLength, timeLimit);
        applyMove(move);
    }

    private void applyMove(Move move) {
        moves.add(move);
        board = board.withMove(move);
        gameCommunication.writeMove(move);
        Debug.print(board);
    }

    private Result getResult(Long duration) {
        return new Result(
                gameCommunication.getPlayerName(),
                gameCommunication.getOutcome(),
                gameCommunication.getEndReason(),
                board,
                moves,
                duration);
    }
}
