package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUACore;
import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;

public class MUAreloadcore extends BuiltInOperation {
    public MUAreloadcore() {
        name = "reloadcore";
        argc = 0;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        StackTrace.getInstance().pop();
        MUACore.loadCoreOperations();
        return null;
    }
}
