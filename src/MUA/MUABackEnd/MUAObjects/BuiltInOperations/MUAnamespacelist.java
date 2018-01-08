package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;

import java.util.Map;

public class MUAnamespacelist extends BuiltInOperation {
    public MUAnamespacelist() {
        name = "namespacelist";
        argc = 0;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        ExprListObject result = new ExprListObject(expr);
        result.objectList.clear();
        result.objectList.add(new MUAlist());
        for(Map.Entry<String, MUAObject> entry : expr.namespace.getMap().entrySet()) {
            if(!(entry.getValue() instanceof OperationObject)) {
                result.objectList.add(new WordObject(entry.getKey()));
            }
        }
        StackTrace.getInstance().pop();
        return result;
    }
}
