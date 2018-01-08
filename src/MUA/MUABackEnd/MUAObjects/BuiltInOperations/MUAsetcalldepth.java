package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAsetcalldepth extends BuiltInOperation {
    public MUAsetcalldepth() {
        name = "setcalldepth";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject result = expr.objectList.get(1);
        // Eval
        if(result instanceof ExprListObject) {
            ((ExprListObject)result).evalExpr();
            result = ((ExprListObject)result).getReturnVal();
        }
        if(!(result instanceof NumObject)) {
            String type1 = "null";
            if(result != null) type1 = result.typeName();
            MUAErrorMessage.error(ErrorStringResource.operation_setcalldepth,
                    ErrorStringResource.incompatible_type, type1);
            throw new MUARuntimeException();
        }
        if(((NumObject) result).getVal() > 1) {
            StackTrace.setMaxSize((int)((NumObject) result).getVal());
        }
        StackTrace.getInstance().pop();
        return result;
    }
}
