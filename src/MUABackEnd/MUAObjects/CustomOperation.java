package MUABackEnd.MUAObjects;

public class CustomOperation extends OperationObject {
    // The operation serial changes every time a new custom operation is constructed.
    private static int operationSerial = 1;
    // The operation object's number
    private static int operationNumber;
    // You MUST check if the list is operationizable first before you construct the
    // custom operation.
    public CustomOperation(ExprListObject list) throws UnOperationizableListException {
        if(!isOperationizable(list)) throw new UnOperationizableListException();
        operationNumber = operationSerial++;
        // TODO
    }
    @Override public boolean isBuiltIn()    { return true; }
    @Override public String getName()       { return "function" + operationNumber; }
    @Override public String toString()      { return "function"; }
    public void setName(String name_)       { name = name_; }

    @Override
    MUAObject getResult(ExprListObject expr) throws MUAStackOverflowException {
        StackTrace.getInstance().push(name);
        // TODO
        StackTrace.getInstance().pop();
        return null;
    }

    // Test if a list is operationizable
    public static boolean isOperationizable(ExprListObject list) {
        // TODO
        return true;
    }
}