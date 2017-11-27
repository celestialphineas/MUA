package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.util.LinkedList;

public class MUArepeat extends BuiltInOperation {
    public MUArepeat() {
        name = "repeat";
        argc = 2;
    }
    @Override
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr_.objectList.get(1);
        MUAObject obj2 = expr_.objectList.get(2);
        // Eval the first slot
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject) obj1).evalExpr();
            obj1 = ((ExprListObject) obj1).getReturnVal();
        }
        if(!(obj1 instanceof NumObject)
        || !(obj2 instanceof ExprListObject)) {
            String type1 = "null", type2 = "null";
            if(obj1 != null) type1 = obj1.typeName();
            if(obj2 != null) type2 = obj2.typeName();
            MUAErrorMessage.error(ErrorStringResource.operation_repeat,
                ErrorStringResource.incompatible_type,
                type1 + ", " + type2);
            throw new MUARuntimeException();
        }
        // Round
        int n = (int)(((NumObject)obj1).getVal() + 0.5);
        MUAObject result = null;
        for(int i = 0; i < n; i++) {
            ExprListObject runExpr = new ExprListObject();
            runExpr.objectList = new LinkedList<>(((ExprListObject) obj2).objectList);
            runExpr.namespace = ((ExprListObject) obj2).namespace;
            runExpr.evalExpr();
            result = runExpr.getReturnVal();
            if(runExpr.isEvalDone()) break;
        }
        StackTrace.getInstance().pop();
        return result;
    }
}
