package MUABackEnd.MUAObjects;

public abstract class BuiltInOperation extends OperationObject {
    public BuiltInOperation()       { }
    @Override int getArgc()         { return argc; }
    @Override boolean isBuiltIn()   { return true; }
    @Override String getName()      { return name; }
    @Override
    public MUAObject getResult(ExprListObject expr) throws MUAStackOverflow {
        StackTrace.getInstance().push(name);
        StackTrace.getInstance().pop();
        return null;
    }
}
