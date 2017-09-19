package gomoku;

class Debug {

    static Boolean print = false;
    static Boolean debug = false;

    static void print(Object x) {
        if (print)
            System.out.println(x);
    }

    static void debug(Object x) {
        if (debug)
            print(x);
    }
}
