package gomoku;

import static java.lang.Character.toLowerCase;

class Move {

    private static final Character INDEX = 'a';

    private final String playerName;
    private final Integer column;
    private final Integer row;

    Move(String playerName, Character column, Integer row) {
        this(playerName, toLowerCase(column) - INDEX, row);
    }

    Move(String playerName, Integer column, Integer row) {
        this.playerName = playerName;
        this.column = column;
        this.row = row;
    }

    public String toString() {
        return playerName + " " + (char) (column + INDEX) + " " + row;
    }

    Integer getColumn() {
        return column;
    }

    String getPlayerName() {
        return playerName;
    }

    Integer getRow() {
        return row;
    }
}
