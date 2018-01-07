package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUACore;
import MUABackEnd.MUAObjects.*;
import MUAFrontEnd.LexicalAnalyzer;
import MUAIO.MUAIO;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAload extends BuiltInOperation {
    public MUAload() {
        name = "load";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject filename = expr.objectList.get(1);
        // Eval
        if(filename instanceof ExprListObject) {
            ((ExprListObject) filename).evalExpr();
            filename = ((ExprListObject) filename).getReturnVal();
        }
        if(!(filename instanceof WordObject)) {
            String type1 = "null";
            if(filename != null) type1 = filename.typeName();
            MUAErrorMessage.error(ErrorStringResource.operation_save,
                ErrorStringResource.incompatible_type, type1);
            throw new MUARuntimeException();
        }
        // Open input
        String fileInput = MUAIO.getInstance().getFile(((WordObject)filename).getVal());
        if(fileInput == null) {
            MUAErrorMessage.error(ErrorStringResource.operation_load,
                    ErrorStringResource.input_invalid, ((WordObject)filename).getVal());
            throw new MUARuntimeException();
        } else try {
            LexicalAnalyzer internalLexicalAnalyzer = new LexicalAnalyzer();
            internalLexicalAnalyzer.sendLine(fileInput);
            MUACore.evaluate(internalLexicalAnalyzer.getTokenList(), expr.namespace);
            internalLexicalAnalyzer.cleanUp();
        } catch (Exception e) {
            MUAErrorMessage.error(ErrorStringResource.mua_sub_interpreter,
                    ErrorStringResource.unknow_internal_error, e.toString());
            throw new MUARuntimeException();
        }
        StackTrace.getInstance().pop();
        return null;
    }
}
