package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAthing extends BuiltInOperation {
    public MUAthing() {
        name = "thing";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        ExprListObject expr = new ExprListObject(expr_);
        // Test use:
        // System.out.println("Thing namespace: " + expr.namespace.getName());
        MUAObject obj1 = expr.objectList.get(1);
        // Eval
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject) obj1).evalExpr();
            obj1 = ((ExprListObject) obj1).getReturnVal();
        }
        if(!(obj1 instanceof WordObject)) {
            String typename = "null";
            if(obj1 != null) typename = obj1.typeName();
            MUAErrorMessage.error(ErrorStringResource.operation_thing,
                ErrorStringResource.incompatible_type, typename);
            throw new MUARuntimeException();
        }
        MUAObject obj = expr_.namespace.find(((WordObject)obj1).getVal());
        if(obj == null) {
            MUAErrorMessage.warn(ErrorStringResource.operation_thing,
                ErrorStringResource.undefined_reference,
                ((WordObject)obj1).getVal());
        }
        StackTrace.getInstance().pop();
        return obj;
    }
}
