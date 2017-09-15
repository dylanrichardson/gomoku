package gomoku;

class Move {

    private final String playerName;
    private final String column;
    private final String row;

    Move(String playerName, String column, String row) {
        this.playerName = playerName;
        this.column = column;
        this.row = row;
    }

    Move(String playerName, Integer column, Integer row) {
        this.playerName = playerName;
        this.column = column.toString();
        this.row = row.toString();
    }

    public String toString() {
        return playerName + " " + column + " " + row;
    }
}
