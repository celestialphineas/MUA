package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;
import MUAIO.MUAIO;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAreadlist extends BuiltInOperation {
    public MUAreadlist() {
        name = "readlist";
        argc = 0;
    }
    @Override
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        String str = MUAIO.getInstance().in.nextLine();
        String[] strlst = str.split(" ");
        ExprListObject list = new ExprListObject();
        list.objectList.add(new MUAlist());
        for(String s : strlst) {
            char ch = s.charAt(0);
            if(ch == '.' || ch == '+' || ch == '-' || Character.isDigit(ch)) {
                try {
                    list.objectList.add(new NumObject(s));
                } catch (NumberFormatException e) {
                    MUAErrorMessage.error(ErrorStringResource.operation_read,
                            ErrorStringResource.number_format, s);
                    throw new MUARuntimeException();
                }
            } else {
                list.objectList.add(new WordObject(s));
            }
        }
        StackTrace.getInstance().pop();
        return list;
    }
}
