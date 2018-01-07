package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;

import java.util.Map;

public class MUApparentnamespacelist extends BuiltInOperation {
    public MUApparentnamespacelist() {
        name = "pparentnamespacelist";
        argc = 0;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        ExprListObject result = new ExprListObject(expr);
        result.objectList.clear();
        result.objectList.add(new MUAlist());
        for(Map.Entry<String, MUAObject> entry : expr.namespace.getParent().getParent().getMap().entrySet()) {
            if(!(entry.getValue() instanceof OperationObject)) {
                result.objectList.add(new WordObject(entry.getKey()));
            }
        }
        StackTrace.getInstance().pop();
        return result;
    }
}
