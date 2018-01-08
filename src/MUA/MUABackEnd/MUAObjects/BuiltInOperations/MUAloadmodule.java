package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUACore;
import MUABackEnd.MUAObjects.MUANamespace.GlobalNamespace;
import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;
import MUAFrontEnd.LexicalAnalyzer;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.util.Scanner;

public class MUAloadmodule extends BuiltInOperation {
    public MUAloadmodule() {
        name = "loadmodule";
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
        // load module
        String moduleName = ((WordObject)filename).getVal();
        if(!moduleName.endsWith(".mua")) {
            moduleName = moduleName + ".mua";
        }

        try {
            Scanner scanner
                = new Scanner(getClass().getClassLoader().getResourceAsStream(moduleName), "UTF-8");
            LexicalAnalyzer internalLexicalAnalyzer = new LexicalAnalyzer();
            String line;
            while (scanner.hasNext()) {
                line = scanner.nextLine();
                internalLexicalAnalyzer.sendLine(line);
            }
            MUACore.evaluate(internalLexicalAnalyzer.getTokenList(), GlobalNamespace.getInstance());
            internalLexicalAnalyzer.cleanUp();
        } catch (Exception e) {
            MUAErrorMessage.error(ErrorStringResource.operation_loadmodule,
                    ErrorStringResource.file_reading_error, moduleName);
            throw new MUARuntimeException();
        }
        StackTrace.getInstance().pop();
        return null;
    }
}
