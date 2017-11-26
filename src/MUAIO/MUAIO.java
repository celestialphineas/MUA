package MUAIO;

import java.io.PrintStream;
import java.util.Scanner;

public class MUAIO {
    public Scanner in;
    public PrintStream out;
    public PrintStream err;
    private static MUAIO instance = new MUAIO();
    public static MUAIO getInstance() { return instance; }
    private MUAIO() {
        in  = new Scanner(System.in);
        out = System.out;
        err = System.err;
    }
}
