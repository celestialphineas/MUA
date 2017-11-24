import MUAAbandoned.ExprConversion;
import MUAFrontEnd.LexicalAnalyzer;
import MUAFrontEnd.Token;

import java.util.List;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        Scanner scanner = new Scanner(System.in);
        List<Token> tokenList = null;

        while(scanner.hasNext()) {
            String string = scanner.nextLine();
            lexicalAnalyzer.sendLine(string);
            if(lexicalAnalyzer.isCompleteLine()) {
                tokenList = lexicalAnalyzer.getTokenList();
                lexicalAnalyzer.cleanUp();
                break;
            }
        }
        tokenList = ExprConversion.fullParenthesize(tokenList);

        if(tokenList != null) for(Token t: tokenList) {
            System.out.print(t.val + " ");
        }
        System.out.println();
        if(tokenList != null) for(Token t: tokenList) {
            System.out.print(t);
        }
        System.out.println();
    }
}
