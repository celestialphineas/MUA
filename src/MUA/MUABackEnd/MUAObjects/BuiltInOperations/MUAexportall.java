package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.util.Map;

public class MUAexportall extends BuiltInOperation {
    public MUAexportall() {
        name = "exportall";
        argc = 0;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        if(expr.namespace != expr.namespace.getParent()) {
            for(Map.Entry<String, MUAObject> entry : expr.namespace.getMap().entrySet()) {
                expr.namespace.getParent().set(entry.getKey(), entry.getValue());
            }
        }
        StackTrace.getInstance().pop();
        return null;
    }
}
