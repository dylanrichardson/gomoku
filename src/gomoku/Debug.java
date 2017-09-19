package gomoku;

class Debug {

    static Boolean debug = false;

    static void print(Object x) {
        if (debug)
            System.out.println(x);
    }
}
