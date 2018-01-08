package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;

public class MUAoutput extends BuiltInOperation {
    public MUAoutput() {
        name = "output";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        // System.out.println("Output");
        // System.out.println(expr.namespace.getName() + " " + expr.namespace + " -> "
        //     + expr.namespace.getParent().getName() + " " + expr.namespace.getParent());
        // System.out.println(expr);

        StackTrace.getInstance().push(name);
        expr.setEvalDone();
        StackTrace.getInstance().pop();
        MUAObject result = expr.objectList.get(1);
        // Eval
        if(result instanceof ExprListObject) {
            ((ExprListObject)result).evalExpr();
            return ((ExprListObject)result).getReturnVal();
        } else return result;
    }
}
