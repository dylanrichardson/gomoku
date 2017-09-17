package gomoku;

import com.sun.tools.javac.util.Pair;

import java.util.*;

class Board {

    private final String[][] cells;
    private final Integer width;
    private final Integer height;

    Board() {
        this(15, 15);
    }

    private Board(Integer width, Integer height, String[][] cells) {
        this.cells = cells;
        this.width = width;
        this.height = height;
    }

    Board(Integer width, Integer height) {
        this(width, height, new String[width][height]);
    }

    Board withMove(Move move) {
        String[][] newCells = cells.clone();
        newCells[move.getColumn()][move.getRow()] = move.getPlayerName();
        return new Board(width, height, newCells);
    }

    Collection<Pair<Integer, Integer>> getOpenCells() {
        List<Pair<Integer, Integer>> openCells = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (cells[i][j] == null) {
                    openCells.add(new Pair<>(i, j));
                }
            }
        }
        return openCells;
    }

    String getPlayerInCell(Integer column, Integer row) {
        return cells[column][row];
    }

    Boolean isTerminal(Integer winLength) {
        // TODO
        return true;
    }
}
