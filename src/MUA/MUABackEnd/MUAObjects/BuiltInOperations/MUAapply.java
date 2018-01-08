package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAapply extends BuiltInOperation {
    public MUAapply() {
        name = "apply";
        argc = 2;
    }
    @Override
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr_.objectList.get(1);
        MUAObject obj2 = expr_.objectList.get(2);
        // Eval the second argument
        if(obj2 instanceof ExprListObject) {
            ((ExprListObject)obj2).evalExpr();
            obj2 = ((ExprListObject)obj2).getReturnVal();
        }
        ExprListObject applied = new ExprListObject(expr_);
        applied.objectList.clear();
        // Test if the first argument is operationizable
        if(obj1 instanceof ExprListObject) {
            if(((ExprListObject) obj1).objectList.get(0).toString().equals("thing")) {
                ((ExprListObject) obj1).evalExpr();
                obj1 = ((ExprListObject) obj1).getReturnVal();
            }
            // Found operation object
            boolean foundOperationObject = false;
            if(obj1 instanceof OperationObject) {
                applied.objectList.add(obj1);
                foundOperationObject = true;
            } else if(!CustomOperation.isOperationizable((ExprListObject) obj1)) {
                MUAErrorMessage.error(ErrorStringResource.operation_apply,
                        ErrorStringResource.incompatible_type, "Cannot operationize the first argument");
                throw new MUARuntimeException();
            }
            if(!foundOperationObject) try {applied.objectList.add(new CustomOperation((ExprListObject) obj1));}
            catch (Exception e) {
                MUAErrorMessage.error(ErrorStringResource.operation_apply,
                        ErrorStringResource.incompatible_type, "Cannot operationize the first argument");
                throw new MUARuntimeException();
            }
        } else {
            MUAErrorMessage.error(ErrorStringResource.operation_apply,
                    ErrorStringResource.incompatible_type, "Cannot operationize the first argument");
            throw new MUARuntimeException();
        }
        if(obj2 instanceof ExprListObject) {
            try {
                if(((ExprListObject) obj2).objectList.size() != ((OperationObject)applied.objectList.get(0)).getArgc() + 1
                        || !(((ExprListObject) obj2).objectList.get(0).toString().equals("list"))) {
                    MUAErrorMessage.error(ErrorStringResource.operation_apply,
                            ErrorStringResource.incompatible_type, "Argument counts don't match");
                    throw new MUARuntimeException();
                }
            } catch (Exception e) {
                MUAErrorMessage.error(ErrorStringResource.operation_apply,
                        ErrorStringResource.incompatible_type, "Argument counts don't match");
                throw new MUARuntimeException();
            }
        }
        try {
            for(MUAObject obj : ((ExprListObject)obj2).objectList.subList(1, ((ExprListObject)obj2).objectList.size())) {
                applied.objectList.add(obj);
            }
            applied.evalExpr();
            MUAObject result = applied.getReturnVal();
            return result;
        } catch (Exception e) {
            MUAErrorMessage.error(ErrorStringResource.operation_apply,
                    ErrorStringResource.unknow_internal_error, e.getMessage());
            throw new MUARuntimeException();
        }
    }
}
