package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAIO.MUAIO;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAsave extends BuiltInOperation {
    public MUAsave() {
        name = "save";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject filename = expr.objectList.get(1);
        // Eval
        if(filename instanceof ExprListObject) {
            ((ExprListObject) filename).evalExpr();
            filename = ((ExprListObject) filename).getReturnVal();
        }
        if(!(filename instanceof WordObject)) {
            String type1 = "null";
            if(filename != null) type1 = filename.typeName();
            MUAErrorMessage.error(ErrorStringResource.operation_save,
                ErrorStringResource.incompatible_type, type1);
            throw new MUARuntimeException();
        }
        // Save namespace
        MUAIO.getInstance().saveFile(expr.namespace.toMUAExprString(), ((WordObject)filename).getVal());
        StackTrace.getInstance().pop();
        return null;
    }
}
