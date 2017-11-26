package MUAIO;

import java.io.PrintStream;

public class MUAIO {
    public PrintStream out;
    private static MUAIO instance = new MUAIO();
    public static MUAIO getInstance() { return instance; }
    private MUAIO() { out = System.out; }
}
