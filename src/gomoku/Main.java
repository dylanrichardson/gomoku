package gomoku;


class Main {

    private static final String PLAYER_NAME = "bid";

    public static void main(String[] args) {
        String playerName = parseArgs(args);
        Player player = new Player(playerName);
        try {
            Result result = player.play();
            System.out.println(result);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    private static String parseArgs(String[] args) {
        if (args.length > 0)
            return args[0];
        else
            return PLAYER_NAME;
    }
}
