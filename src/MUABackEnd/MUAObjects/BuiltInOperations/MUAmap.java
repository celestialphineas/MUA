package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.util.List;
import java.util.stream.Collectors;

public class MUAmap extends BuiltInOperation {
    public MUAmap() {
        name = "map";
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
        ExprListObject result = new ExprListObject(expr_);
        result.objectList.clear();
        OperationObject operationObject = null;
        // Test if the first argument is operationizable
        if(obj1 instanceof ExprListObject) {
            if(((ExprListObject) obj1).objectList.get(0).toString().equals("thing")) {
                ((ExprListObject) obj1).evalExpr();
                obj1 = ((ExprListObject) obj1).getReturnVal();
            }
            // Found operation object
            boolean foundOperationObject = false;
            if(obj1 instanceof OperationObject) {
                operationObject = (OperationObject) obj1;
                foundOperationObject = true;
            } else if(!CustomOperation.isOperationizable((ExprListObject) obj1)) {
                MUAErrorMessage.error(ErrorStringResource.operation_map,
                        ErrorStringResource.incompatible_type, "Cannot operationize the first argument");
                throw new MUARuntimeException();
            }
            if(!foundOperationObject) try { operationObject = new CustomOperation((ExprListObject) obj1); }
            catch (Exception e) {
                MUAErrorMessage.error(ErrorStringResource.operation_map,
                        ErrorStringResource.incompatible_type, "Cannot operationize the first argument");
                throw new MUARuntimeException();
            }
        } else {
            MUAErrorMessage.error(ErrorStringResource.operation_map,
                    ErrorStringResource.incompatible_type, "Cannot operationize the first argument");
            throw new MUARuntimeException();
        }
        if(operationObject.getArgc() != 1) {
            MUAErrorMessage.error(ErrorStringResource.operation_map,
                    ErrorStringResource.incompatible_type, "The mapping function must have exactly one argument");
            throw new MUARuntimeException();
        }

        if(obj2 instanceof ExprListObject && ((ExprListObject) obj2).objectList.size() >= 1) {
            result.objectList.add(((ExprListObject) obj2).objectList.get(0));
            if(((ExprListObject) obj2).objectList.size() == 1) return result;
            List<MUAObject> resultObjects;
            try {
                final OperationObject operation = operationObject;
                resultObjects = ((ExprListObject) obj2)
                        .objectList
                        .subList(1, ((ExprListObject) obj2).objectList.size())
                        .stream()
                        .map(x -> {
                            ExprListObject mappedExpr = new ExprListObject(expr_);
                            mappedExpr.objectList.clear();
                            mappedExpr.objectList.add(operation);
                            mappedExpr.objectList.add(x);
                            MUAObject lambdaResult = null;
                            try {
                                mappedExpr.evalExpr();
                                lambdaResult = mappedExpr.getReturnVal();
                            } catch (Exception e) {
                                MUAErrorMessage.error(ErrorStringResource.operation_meta,
                                        ErrorStringResource.too_few_operands, "Evaluation error, stopped");
                            }
                            return lambdaResult;
                        })
                        .collect(Collectors.toList());
            } catch (Exception e) {
                MUAErrorMessage.error(ErrorStringResource.operation_map,
                        ErrorStringResource.incompatible_type, "Evaluation error, stopped");
                throw e;
            }
            result.objectList.addAll(resultObjects);
        } else {
            MUAErrorMessage.error(ErrorStringResource.operation_map,
                    ErrorStringResource.incompatible_type, "Argument 2 is not a list");
            throw new MUARuntimeException();
        }

        return result;
    }
}
