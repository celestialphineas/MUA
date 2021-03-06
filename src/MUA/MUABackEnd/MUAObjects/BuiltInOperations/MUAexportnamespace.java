package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAexportnamespace extends BuiltInOperation {
    public MUAexportnamespace() {
        name = "exportnamespace";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject namespaceName = expr.objectList.get(1);
        // Eval
        if(namespaceName instanceof ExprListObject) {
            ((ExprListObject) namespaceName).evalExpr();
            namespaceName = ((ExprListObject) namespaceName).getReturnVal();
        }
        if(!(namespaceName instanceof WordObject)) {
            String type1 = "null";
            if(namespaceName != null) type1 = namespaceName.typeName();
            MUAErrorMessage.error(ErrorStringResource.operation_export,
                ErrorStringResource.incompatible_type, type1);
            throw new MUARuntimeException();
        }
        StackTrace.getInstance().pop();
        // Set namespace
        expr.namespace.getParent().set(
            ((WordObject)namespaceName).getVal(), expr.namespace);
        return null;
    }
}
