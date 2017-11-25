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
        // TODO
        StackTrace.getInstance().pop();
        return null;
    }
}
