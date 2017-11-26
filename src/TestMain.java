import MUAAbandoned.ExprConversion;
import MUABackEnd.MUACore;
import MUABackEnd.MUAObjects.CustomOperation;
import MUABackEnd.MUAObjects.ExprListObject;
import MUABackEnd.MUAObjects.MUAObject;
import MUABackEnd.MUAObjects.NumObject;
import MUAFrontEnd.LexicalAnalyzer;
import MUAFrontEnd.Token;

import java.util.List;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        Scanner scanner = new Scanner(System.in);
        List<Token> tokenList = null;
        MUACore core = MUACore.getInstance();

        for(;;) {
            while(scanner.hasNext()) {
                String string = scanner.nextLine();
                lexicalAnalyzer.sendLine(string);
                if(lexicalAnalyzer.isCompleteLine()) {
                    tokenList = lexicalAnalyzer.getTokenList();
                    lexicalAnalyzer.cleanUp();
                    break;
                }
            }

            if(tokenList != null) for(Token t: tokenList) {
                System.out.print(t.val + " ");
            }
            System.out.println();
            if(tokenList != null) for(Token t: tokenList) {
                System.out.print(t);
            }
            System.out.println();

            // Lisp form
            List<MUAObject> muaObjects = MUACore.makeExprList(tokenList);
            if(muaObjects != null) System.out.println(muaObjects.toString());
            else continue;

            // Result
            List<MUAObject> result = MUACore.evaluate(muaObjects);
            if(result != null) System.out.println(result.toString());
            else continue;

            ExprListObject expr;
            if(muaObjects != null) {
                expr = (ExprListObject) (muaObjects.get(0));
                if(expr != null) {
                    System.out.println("Operationizable: "
                            + CustomOperation.isOperationizable(expr));
                }
            }
        }
    }
}
