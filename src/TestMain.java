import MUAFrontEnd.LexicalAnalyzer;
import MUAFrontEnd.Token;

import java.util.List;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        Scanner scanner = new Scanner(System.in);
        List<String> lexemeList = null;

        while(scanner.hasNext()) {
            String string = scanner.nextLine();
            lexicalAnalyzer.sendLine(string);
            if(lexicalAnalyzer.isCompleteLine()) {
                lexemeList = lexicalAnalyzer.getStringList();
                lexicalAnalyzer.cleanUp();
                break;
            }
        }

        List<Token> tokens = LexicalAnalyzer.tokenize(lexemeList);
        if(lexemeList != null) for(Token t: tokens) {
            System.out.print(t);
        }
        System.out.println();
    }
}
