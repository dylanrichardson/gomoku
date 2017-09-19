package gomoku;


import java.util.Arrays;

class Main {

    private static final Integer WIN_LENGTH = 5;
    private static final Integer BOARD_SIDE_LENGTH = 15;
    private static final String PLAYER_NAME = "bid";

    public static void main(String[] args) {
        String playerName = parseName(args);
        Integer boardWidth = parseWidth(args);
        Integer boardHeight = parseHeight(args);
        Integer winLength = parseLength(args);
        Debug.debug = parseDebug(args);

        Player player = new Player(playerName, boardWidth, boardHeight, winLength);
        try {
            Debug.print(player.play());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private static Boolean parseDebug(String[] args) {
        return Arrays.stream(args).anyMatch(arg -> arg.equals("-D") || arg.equals("--debug"));
    }

    private static String parseName(String[] args) {
        if (args.length > 0)
            return args[0];
        else
            return PLAYER_NAME;
    }

    private static Integer parseWidth(String[] args) {
        return parseIntWithDefault(args, 1, BOARD_SIDE_LENGTH);
    }

    private static Integer parseHeight(String[] args) {
        return parseIntWithDefault(args, 2, BOARD_SIDE_LENGTH);
    }

    private static Integer parseLength(String[] args) {
        return parseIntWithDefault(args, 3, WIN_LENGTH);
    }

    private static Integer parseIntWithDefault(String[] args, Integer index, Integer def) {
        try {
            if (args.length > index)
                return Integer.parseInt(args[index]);
            else
                return def;
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
