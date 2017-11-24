package MUABackEnd.MUAObjects;

public abstract class OperationObject implements MUAObject {
    protected String name;
    protected int argc;
    OperationObject()    { argc = 0; }
    int getArgc()       { return argc; }
    boolean isBuiltIn() { return true; }
    String getName()    { return name; }
    MUAObject getResult(ExprListObject expr) throws MUAStackOverflow {
        StackTrace.getInstance().push(name);
        StackTrace.getInstance().pop();
        return null;
    }
    @Override
    public boolean isAtomic()  { return false; }
}

abstract class BuiltInOperation extends OperationObject {
    BuiltInOperation()   { super(); }
    @Override int getArgc()       { return argc; }
    @Override boolean isBuiltIn() { return true; }
    @Override String getName()    { return name; }
    @Override
    MUAObject getResult(ExprListObject expr) throws MUAStackOverflow {
        StackTrace.getInstance().push(name);
        StackTrace.getInstance().pop();
        return null;
    }
}