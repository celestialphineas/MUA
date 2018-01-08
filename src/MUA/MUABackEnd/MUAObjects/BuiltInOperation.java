package MUABackEnd.MUAObjects;

public abstract class BuiltInOperation extends OperationObject {
    public BuiltInOperation()       { }
    @Override public int getArgc()          { return argc; }
    @Override public boolean isBuiltIn()    { return true; }
    // The built-in operations are atomic
    @Override public boolean isAtomic()     { return true; }
    @Override public String getName()       { return name; }
    @Override public String toString()      { return name; }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        StackTrace.getInstance().pop();
        return null;
    }
}
