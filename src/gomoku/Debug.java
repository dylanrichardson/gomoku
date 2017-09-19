package gomoku;

class Debug {

    static Boolean debug = true;

    static void print(Object x) {
        if (debug)
            System.out.println(x);
    }
}
