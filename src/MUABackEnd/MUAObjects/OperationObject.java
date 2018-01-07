package MUABackEnd.MUAObjects;

import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public abstract class OperationObject implements MUAObject {
    protected String name;
    protected int argc;
    public OperationObject()    { argc = 0; }
    public int getArgc()        { return argc; }
    public boolean isBuiltIn()  { return true; }
    public String getName()     { return name; }
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        if(expr.objectList.size() != getArgc()) {
            MUAErrorMessage.error(ErrorStringResource.operation_meta,
                    ErrorStringResource.incompatible_type, getName());
            throw new MUARuntimeException();
        }
        return null;
    }
    @Override public boolean isAtomic()  { return false; }
    @Override public String typeName()   { return "operation"; }
    @Override public String toMUAExprString() { return name; }
}

