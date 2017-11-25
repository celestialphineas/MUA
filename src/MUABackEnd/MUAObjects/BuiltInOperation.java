package MUABackEnd.MUAObjects;

public abstract class BuiltInOperation extends OperationObject {
    public BuiltInOperation()       { }
    @Override public int getArgc()         { return argc; }
    @Override public boolean isBuiltIn()   { return true; }
    @Override public String getName()      { return name; }
    @Override public String toString()     { return name; }
    @Override
    public MUAObject getResult(ExprListObject expr) throws MUAStackOverflowException {
        StackTrace.getInstance().push(name);
        StackTrace.getInstance().pop();
        return null;
    }
}
