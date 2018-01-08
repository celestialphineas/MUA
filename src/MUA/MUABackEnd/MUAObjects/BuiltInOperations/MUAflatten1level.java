package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;

public class MUAflatten1level extends BuiltInOperation {
    public MUAflatten1level() {
        name = "flatten1level";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        ExprListObject expr = new ExprListObject(expr_);
        if(expr.objectList.size() <= 1) {
            StackTrace.getInstance().pop();
            return expr;
        }
        // Eval
        MUAObject first = expr.objectList.get(1);
        if(first instanceof ExprListObject) {
            ((ExprListObject)first).evalExpr();
            first = ((ExprListObject)first).getReturnVal();
        }
        // Result
        ExprListObject result = new ExprListObject(expr);
        result.objectList.clear();
        if(first instanceof ExprListObject) {
            result.objectList.add(((ExprListObject)first).objectList.get(0));
        } else {
            result.objectList.add(new MUAlist());
        }

        if(first instanceof ExprListObject) {
            for(MUAObject obj
            : ((ExprListObject)first).objectList.subList(1, ((ExprListObject)first).objectList.size())) {
                if(obj instanceof ExprListObject) {
                    result.objectList.addAll(((ExprListObject) obj).objectList.subList(1, ((ExprListObject) obj).objectList.size()));
                } else {
                    result.objectList.add(obj);
                }
            }
        } else {
            result.objectList.add(first);
        }

        StackTrace.getInstance().pop();
        return result;
    }
}
