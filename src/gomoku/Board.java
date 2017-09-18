package gomoku;

import com.sun.tools.javac.util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Board {

    private final Stone[][] cells;
    private final Integer width;
    private final Integer height;
    private Map<Integer, Stone> winners = new HashMap<>();

    Board() {
        this(15, 15);
    }

    private Board(Integer width, Integer height, Stone[][] cells) {
        this.cells = cells;
        this.width = width;
        this.height = height;
    }

    Board(Integer width, Integer height) {
        this(width, height, new Stone[width][height]);
    }

    Board withMove(Move move) {
        Stone[][] newCells = Arrays
                .stream(cells)
                .map(Stone[]::clone)
                .toArray(Stone[][]::new);
        newCells[move.getColumn()][move.getRow()] = move.getStone();
        return new Board(width, height, newCells);
    }

    Collection<Pair<Integer, Integer>> getOpenCells() {
        List<Pair<Integer, Integer>> openCells = new ArrayList<>();
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                if (cells[col][row] == null) {
                    openCells.add(new Pair<>(col, row));
                }
            }
        }
        return openCells;
    }

    Stone getStoneInCell(Integer column, Integer row) {
        return (column < width && row < height) ? cells[column][row] : null;
    }

    Boolean isTerminal(Integer winLength) {
        // TODO optimize
        Boolean isDraw = true;
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                Stone stone = cells[col][row];
                if (stone == null) {
                    isDraw = false;
                } else if (isTerminalCell(col, row, winLength)) {
                    winners.put(winLength, stone);
                    return true;
                }
            }
        }
        winners.put(winLength, null);
        return isDraw;
    }

    Stone getWinner(Integer winLength) {
        if (!winners.containsKey(winLength)) {
            isTerminal(winLength);
        }
        return winners.get(winLength);
    }

    private Boolean isTerminalCell(Integer col, Integer row, Integer winLength) {
        return isVerticalWin(col, row, winLength)
                || isHorizontalWin(col, row, winLength)
                || isDiagonalWin(col, row, winLength);
    }

    private Boolean isVerticalWin(Integer col, Integer row, Integer winLength) {
        return allSame(getVerticalStones(col, row, winLength));
    }

    private Boolean isHorizontalWin(Integer col, Integer row, Integer winLength) {
        return allSame(getHorizontalStones(col, row, winLength));
    }

    private Boolean isDiagonalWin(Integer col, Integer row, Integer winLength) {
        return allSame(getDiagonalStones(col, row, winLength));
    }

    private <T> Boolean allSame(Collection<T> cells) {
        Iterator<T> iter = cells.iterator();
        T first = iter.next();
        while (iter.hasNext()) {
            T next = iter.next();
            if (next == null || !next.equals(first)) {
                return false;
            }
        }
        return first != null;
    }

    private Collection<Stone> getVerticalStones(Integer col, Integer row, Integer length) {
        return getStones(length, r -> getStoneInCell(col, row + r));
    }

    private Collection<Stone> getHorizontalStones(Integer col, Integer row, Integer length) {
        return getStones(length, r -> getStoneInCell(col + r, row));
    }

    private Collection<Stone> getDiagonalStones(Integer col, Integer row, Integer length) {
        return getStones(length, r -> getStoneInCell(col + r, row + r));
    }

    private Collection<Stone> getStones(Integer length, Function<Integer, Stone> getStone) {
        return IntStream
                .range(0, length)
                .mapToObj(getStone::apply)
                .collect(Collectors.toList());
    }
}
