import MUAFrontEnd.LexicalAnalyzer;
import java.util.Deque;
import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        Scanner scanner = new Scanner(System.in);
        Deque<String> stringList = null;

        while(scanner.hasNext()) {
            String string = scanner.nextLine();
            lexicalAnalyzer.sendLine(string);
            if(lexicalAnalyzer.isCompleteLine()) {
                stringList = lexicalAnalyzer.getStringList();
                lexicalAnalyzer.cleanUp();
                break;
            }
        }

        if(stringList != null) for(String s: stringList) {
            System.out.print("{" + s + "} ");
        }
        System.out.println();
    }
}
