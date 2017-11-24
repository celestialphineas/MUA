package MUABackEnd.MUAObjects;

public class CustomOperation extends OperationObject {
    // The operation serial changes every time a new custom operation is constructed.
    private static int operationSerial = 1;
    // The operation object's number
    private static int operationNumber;
    // You MUST check if the list is operationizable first before you construct the
    // custom operation.
    CustomOperation(ExprListObject list) throws UnOperationizableList {
        if(!isOperationizable()) throw new UnOperationizableList();
        operationNumber = operationSerial++;
        // TODO
    }
    @Override boolean isBuiltIn()   { return true; }
    @Override String getName()      { return "function" + operationNumber; }
    void setName(String name_)      { name = name_; }

    @Override
    MUAObject getResult(ExprListObject expr) throws MUAStackOverflow {
        StackTrace.getInstance().push(name);
        // TODO
        StackTrace.getInstance().pop();
        return null;
    }

    // Test if a list is operationizable
    static boolean isOperationizable(ExprListObject list) {
        // TODO
        return true;
    }
}

class UnOperationizableList extends Exception {
    UnOperationizableList()                 { super(); }
    UnOperationizableList(String message)   { super(message); }
}