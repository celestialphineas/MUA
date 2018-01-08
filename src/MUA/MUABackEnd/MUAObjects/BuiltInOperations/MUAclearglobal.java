package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUACore;
import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;

public class MUAclearglobal extends BuiltInOperation {
    public MUAclearglobal() {
        name = "clearglobal";
        argc = 0;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        StackTrace.getInstance().pop();
        MUACore.getInstance().MUAInit();
        return null;
    }
}
