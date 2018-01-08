package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAdeclare extends BuiltInOperation {
    public MUAdeclare() {
        name = "declare";
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
        if(!(obj1 instanceof WordObject)) {
            String type1 = "null", type2 = "null";
            if(obj1 != null) type1 = obj1.typeName();
            if(obj2 != null) type2 = obj2.typeName();
            MUAErrorMessage.error(ErrorStringResource.operation_declare,
                ErrorStringResource.incompatible_type,
                type1 + ", " + type2);
            throw new MUARuntimeException();
        }
        // Check obj2
        if(!(obj2 instanceof ExprListObject)) {
            String type1 = "null", type2 = "null";
            if(obj1 != null) type1 = obj1.typeName();
            if(obj2 != null) type2 = obj2.typeName();
            MUAErrorMessage.error(ErrorStringResource.operation_declare,
                    ErrorStringResource.incompatible_type,
                    type1 + ", " + type2);
            throw new MUARuntimeException();
        }

        MUAObject head2 = ((ExprListObject)obj2).objectList.get(0);
        if(!(head2 instanceof OperationObject)
        || !((OperationObject)head2).getName().equals("list")) {
            String type1 = "null", type2 = "null";
            if(obj1 != null) type1 = obj1.typeName();
            if(obj2 != null) type2 = obj2.typeName();
            MUAErrorMessage.error(ErrorStringResource.operation_declare,
                    ErrorStringResource.incompatible_type,
                    type1 + ", " + type2);
            throw new MUARuntimeException();
        }

        // Make a pseudo operationizable list
        ExprListObject makeExpr = new ExprListObject(expr_);
        makeExpr.objectList.clear();
        makeExpr.objectList.add(new DumbHeadObject("make"));
        makeExpr.objectList.add(obj1);
        ExprListObject newobj2 = new ExprListObject();
        newobj2.objectList.add(new MUAlist());
        newobj2.objectList.add(obj2);
        newobj2.objectList.add(obj2);
        makeExpr.objectList.add(newobj2);
        makeExpr.evalExpr();
        StackTrace.getInstance().pop();
        return null;
    }
}
