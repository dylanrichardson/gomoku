package gomoku;

import java.util.ArrayList;
import java.util.List;
import static gomoku.Stone.FRIENDLY;

class Player {

    private final GameCommunication gameCommunication;
    private final List<Move> moves;
    private final Integer winLength;

    // mutable
    private Board board;
    private final Algorithm algorithm;

    private Player(GameCommunication gameCommunication, Board board, Integer winLength, Algorithm algorithm) {
        this.gameCommunication = gameCommunication;
        this.board = board;
        this.moves = new ArrayList<>();
        this.winLength = winLength;
        this.algorithm = algorithm;
    }

    Player(String playerName, Integer width, Integer height, Integer winLength) {
        this(playerName, width, height, winLength, new MiniMax());
    }

    Player(String playerName, Integer width, Integer height, Integer winLength, Algorithm algorithm) {
        this(new GameCommunication(playerName), new Board(width, height), winLength, algorithm);
    }

    Result play() {
        // TODO handle special 2nd move of game
        Long startTime = System.currentTimeMillis();
        int round = 0;
        while (true) {
            Debug.print("\n\nROUND - " + round++ + (System.currentTimeMillis() - startTime) / 1000 + "s\n\n");
            Debug.print("Waiting...");
            gameCommunication.waitForTurn();
            readMove();
            if (gameCommunication.isOver()) {
                return getResult((System.currentTimeMillis() - startTime)  / 1000);
            }
            makeMove();
        }
    }

    private void readMove() {
        Move move = gameCommunication.readMove();
        if (!isRepeat(move) && board.isValidMove(move)) {
            moves.add(move);
            board = board.withMove(move);
        }
    }

    private Boolean isRepeat(Move move) {
        return moves.size() > 0 && moves.get(moves.size() - 1).getCell().equals(move.getCell());
    }

    private void makeMove() {
        Debug.print(board);
        Debug.print("Choosing move...");
        Move move = algorithm.chooseMove(FRIENDLY, board, winLength);
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
