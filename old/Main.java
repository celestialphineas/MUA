package MUAAbandoned;

import MUABackEnd.MUACore;
import MUABackEnd.MUAObjects.MUAObject;
import MUAFrontEnd.LexicalAnalyzer;
import MUAFrontEnd.Token;

import java.util.List;
import java.util.Scanner;

public class Main {
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

            // Result
            List<MUAObject> result = MUACore.evaluate(tokenList);
            if(result != null) System.out.println(result.toString());
        }
    }
}
