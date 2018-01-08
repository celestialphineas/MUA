package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAIO.MUAIO;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAread extends BuiltInOperation {
    public MUAread() {
        name = "read";
        argc = 0;
    }
    @Override
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        String str = MUAIO.getInstance().in.nextLine();
        char ch = str.charAt(0);
        if(ch == '.' || ch == '+' || ch == '-' || Character.isDigit(ch)) {
            try {
                StackTrace.getInstance().pop();
                return new NumObject(str);
            } catch (NumberFormatException e) {
                MUAErrorMessage.error(ErrorStringResource.operation_read,
                        ErrorStringResource.number_format, str);
                throw new MUARuntimeException();
            }
        } else {
            StackTrace.getInstance().pop();
            return new WordObject(str);
        }
    }
}
