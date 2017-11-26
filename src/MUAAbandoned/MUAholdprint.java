package MUAAbandoned;

import MUABackEnd.MUAObjects.*;
import MUAIO.MUAIO;

public class MUAholdprint extends BuiltInOperation {
    public MUAholdprint() {
        name = "holdprint";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
            throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject result = expr.objectList.get(1);
        MUAIO.getInstance().out.println(result);
        StackTrace.getInstance().pop();
        return null;
    }
}
