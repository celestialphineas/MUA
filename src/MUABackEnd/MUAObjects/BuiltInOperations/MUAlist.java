package MUABackEnd.MUAObjects.BuiltInOperations;
import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAlist extends BuiltInOperation {
    public MUAlist() {
        name = "list";
        argc = -1;      // Any
    }
    @Override
    public MUAObject getResult(ExprListObject expr) throws MUAStackOverflowException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr.objectList.get(1);
        MUAObject obj2 = expr.objectList.get(2);
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject)obj1).evalExpr();
            obj1 = ((ExprListObject)obj1).getReturnVal();
        }
        if(obj2 instanceof ExprListObject) {
            ((ExprListObject)obj2).evalExpr();
            obj2 = ((ExprListObject)obj2).getReturnVal();
        }
        if(obj1 instanceof NumObject && obj2 instanceof NumObject) {
            double result = ((NumObject)obj1).getVal() + ((NumObject)obj2).getVal();
            return new NumObject(result);
        }
        MUAErrorMessage.error(ErrorStringResource.operation_add,
                ErrorStringResource.incompatible_type,
                obj1.typeName() + ", " + obj2.typeName());
        StackTrace.getInstance().pop();
        return null;
    }
}
