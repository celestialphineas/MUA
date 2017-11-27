package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUANamespace.GlobalNamespace;
import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAexpose extends BuiltInOperation {
    public MUAexpose() {
        name = "expose";
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
        String objName = ((WordObject)namespaceName).getVal();
        MUAObject obj = expr.namespace.find(objName);
        GlobalNamespace.getInstance().set(objName, obj);
        StackTrace.getInstance().pop();
        return null;
    }
}
