package MUABackEnd.MUAObjects;

import java.util.Stack;

public class StackTrace {
    private static StackTrace ourInstance = new StackTrace();
    public static StackTrace getInstance() {
        return ourInstance;
    }
    private static int maxSize = 1024;
    Stack<String> callStack = new Stack<>();

    private StackTrace() { }
    public void push(String str) throws MUAStackOverflowException
    {
        if(callStack.size() > maxSize) {
            throw new MUAStackOverflowException();
        }
        callStack.push(str);
    }
    public void pop()   { callStack.pop(); }
    public void clear() { if(!callStack.isEmpty()) callStack.clear(); }
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for(String str : callStack) {
            buffer.append("[[" + str + "]] ");
        }
        return buffer.toString();
    }
    static public int setMaxSize(int size) { return maxSize = size; }
    static public int getMaxSize() { return maxSize; }
}