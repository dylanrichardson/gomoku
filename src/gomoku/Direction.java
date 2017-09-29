package gomoku;

import com.sun.tools.javac.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

enum Direction {
    North(0, -1),
    South(0, 1),
    East(1, 0),
    West(-1, 0),
    NorthEast(1, -1),
    NorthWest(-1, -1),
    SouthEast(1, 1),
    SouthWest(-1, 1);

    public Integer dCol;
    public Integer dRow;


    private static Map<Pair<Integer, Integer>, Direction> map = new HashMap<Pair<Integer, Integer>, Direction>();

    static {
        for (Direction dir : Direction.values()) {
            map.put(new Pair<>(dir.dCol, dir.dRow), dir);
        }
    }

    Direction(Integer dCol, Integer dRow) {
        this.dCol = dCol;
        this.dRow = dRow;
    }

    static Stream<Direction> all() {
        return Stream.concat(semiCircle(), semiCircle().map(Direction::opposite));
    }

    Direction opposite() {
        return map.get(new Pair<>(dCol * -1, dRow * -1));
    }

    static Stream<Direction> semiCircle() {
        return Stream.of(North, East, NorthEast, NorthWest);
    }
}
