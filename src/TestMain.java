import MUAFrontEnd.LexicalAnalyzer;
import MUAFrontEnd.Token;
import MUAFrontEnd.Tokenizer;

import java.util.List;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        Scanner scanner = new Scanner(System.in);
        List<String> stringList = null;

        while(scanner.hasNext()) {
            String string = scanner.nextLine();
            lexicalAnalyzer.sendLine(string);
            if(lexicalAnalyzer.isCompleteLine()) {
                stringList = lexicalAnalyzer.getStringList();
                lexicalAnalyzer.cleanUp();
                break;
            }
        }

        List<Token> tokens = Tokenizer.tokenize(stringList);
        if(stringList != null) for(Token t: tokens) {
            System.out.print(t);
        }
        System.out.println();
    }
}
