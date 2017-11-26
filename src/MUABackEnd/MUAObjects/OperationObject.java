package MUABackEnd.MUAObjects;

public abstract class OperationObject implements MUAObject {
    protected String name;
    protected int argc;
    public OperationObject()    { argc = 0; }
    public int getArgc()        { return argc; }
    public boolean isBuiltIn()  { return true; }
    public String getName()     { return name; }
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        StackTrace.getInstance().pop();
        return null;
    }
    @Override
    public boolean isAtomic()  { return false; }
    @Override
    public String typeName()   { return "operation"; }
}

