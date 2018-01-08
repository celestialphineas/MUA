package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUANamespace.Namespace;
import MUABackEnd.MUAObjects.*;

public class MUAlist extends BuiltInOperation {
    public MUAlist() {
        name = "list";
        argc = 2;      // Any
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        if(expr.objectList.size() <= 1) {
            StackTrace.getInstance().pop();
            return expr;
        }

        // Result
        MUAObject result = new ExprListObject(expr);
        ((ExprListObject)result).objectList.clear();
        ((ExprListObject)result).objectList.add(expr.objectList.get(0));

        // For test use
        // System.out.println(expr.namespace.getName() + " " + expr.namespace + " -> "
        //     + expr.namespace.getParent().getName() + " " + expr.namespace.getParent());
        // System.out.println(expr);
        
        for(MUAObject obj : expr.objectList.subList(1, expr.objectList.size())) {
            if(obj instanceof ExprListObject) {
                ((ExprListObject) obj).evalExpr();
                MUAObject returnVal = ((ExprListObject) obj).getReturnVal();
                // Check if this should be stopped
                if(((ExprListObject) obj).isEvalDone()) {
                    if(returnVal != null) {
                        result = returnVal;
                        break;
                    } else {
                        result = null;
                        break;
                    }
                } else if(returnVal == null) {
                    continue;
                } else {
                    ((ExprListObject)result).objectList.add(returnVal);
                }
            } else if(obj != null) {
                ((ExprListObject)result).objectList.add(obj);
            }
        }

        StackTrace.getInstance().pop();
        return result;
    }
}
