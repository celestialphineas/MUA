package MUABackEnd.MUAObjects;

public abstract class OperationObject implements MUAObject {
    protected String name;
    protected int argc;
    public OperationObject()    { argc = 0; }
    int getArgc()               { return argc; }
    boolean isBuiltIn()         { return true; }
    String getName()            { return name; }
    MUAObject getResult(ExprListObject expr) throws MUAStackOverflow {
        StackTrace.getInstance().push(name);
        StackTrace.getInstance().pop();
        return null;
    }
    @Override
    public boolean isAtomic()  { return false; }
    @Override
    public String typeName()   { return "operation"; }
}

