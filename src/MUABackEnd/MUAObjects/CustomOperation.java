package MUABackEnd.MUAObjects;

import MUABackEnd.MUAObjects.BuiltInOperations.MUAlist;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;
import java.util.List;

public class CustomOperation extends OperationObject {
    // The operation serial changes every time a new custom operation is constructed.
    private static int operationSerial = 1;
    // The operation object's number
    private static int operationNumber;
    // Argument declaration list
    private ExprListObject argDeclaration;
    // Operation sequence list
    private ExprListObject localEnvList;

    // You MUST check if the list is operationizable first before you construct the
    // custom operation.
    public CustomOperation(ExprListObject list) throws UnOperationizableListException {
        if(!isOperationizable(list)) throw new UnOperationizableListException();
        argc = ((ExprListObject)(list.objectList.get(1))).objectList.size() - 1;
        try {
            argDeclaration = new ExprListObject((ExprListObject)(list.objectList.get(1)));
            localEnvList = (ExprListObject)(list.objectList.get(2));
        } catch (Exception e) {
            MUAErrorMessage.error(ErrorStringResource.mua_custom_operation,
                    ErrorStringResource.operationization_internal, "Mysterious force");
            throw new UnOperationizableListException();
        }
        name = "function" + operationNumber;
        operationNumber = operationSerial++;
    }

    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        // Passing parameters
        // Set the variable in the namespace according to the first sublist
        for(int i = 1; i <= argc; i++) {
            WordObject word = (WordObject)argDeclaration.objectList.get(i);
            MUAObject obj = expr.objectList.get(i);
            if(word == null) continue;
            // Eval parameter
            if(obj instanceof ExprListObject) {
                ((ExprListObject) obj).evalExpr();
                obj = ((ExprListObject) obj).getReturnVal();
            }
            localEnvList.namespace.set(word.getVal(), obj);
            // Test use: 
            // System.out.println("Env list namespace: " + localEnvList.namespace.getName());
            // System.out.println("Env list namespace: " + word.getVal() + ", " + localEnvList.namespace.find(word.getVal()));
        }
        // Get result
        localEnvList.evalExpr();
        MUAObject result = localEnvList.getReturnVal();
        StackTrace.getInstance().pop();
        return result;
    }

    // Test if a list is operationizable
    public static boolean isOperationizable(ExprListObject list) {
        // A function is operationizable:
        //      1. The list has at least two sublists
        //      2. The first list must be a collection of words (argument/operand list)
        if(list == null) return false;
        if(list.objectList.size() < 3) return false;
        if(!(list.objectList.get(0) instanceof MUAlist)) return false;
        if(!(list.objectList.get(1) instanceof ExprListObject)) return false;
        if(!(((ExprListObject)(list.objectList.get(1))).objectList.get(0) instanceof MUAlist))
            return false;
        if(!(list.objectList.get(2) instanceof ExprListObject)) return false;
        if(!(((ExprListObject)(list.objectList.get(2))).objectList.get(0) instanceof MUAlist))
            return false;
        // Check the first
        List<MUAObject> listToCheck = ((ExprListObject)(list.objectList.get(1))).objectList;
        for(MUAObject i : listToCheck.subList(1, listToCheck.size())) {
            if(!(i instanceof WordObject)) return false;
        }
        return true;
    }

    @Override public boolean isBuiltIn()    { return true; }
    @Override public String getName()       { return name; }
    @Override public String toString()      { return name; }
    public void setName(String name_)       { name = name_; }
}