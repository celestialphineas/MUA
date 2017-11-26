package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;

public class MUAlist extends BuiltInOperation {
    public MUAlist() {
        name = "list";
        argc = -1;      // Any
    }
    @Override
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        ExprListObject expr = new ExprListObject(expr_);
        if(expr.objectList.size() <= 1) {
            return expr;
        }

        // Result
        ExprListObject result = new ExprListObject(expr);
        result.objectList.clear();
        result.objectList.add(expr.objectList.get(0));

        for(MUAObject obj : expr.objectList.subList(1, expr.objectList.size())) {
            if(obj instanceof ExprListObject) {
                ((ExprListObject) obj).evalExpr();
                MUAObject returnVal = ((ExprListObject) obj).getReturnVal();
                // Check if this should be stopped
                if(((ExprListObject) obj).isEvalDone()) {
                    if(returnVal != null) return returnVal;
                    else {
                        if(returnVal != null) result.objectList.add(returnVal);
                        return result;
                    }
                }
                if(returnVal == null) continue;
                result.objectList.add(returnVal);
            } else if(obj != null) {
                result.objectList.add(obj);
            }
        }

        StackTrace.getInstance().pop();
        return result;
    }
}
