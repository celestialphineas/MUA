import MUABackEnd.MUACore;
import MUABackEnd.MUAObjects.ExprListObject;
import MUABackEnd.MUAObjects.MUAObject;
import MUABackEnd.MUAObjects.StackTrace;
import MUAFrontEnd.LexicalAnalyzer;
import MUAFrontEnd.Token;
import MUAIO.MUAIO;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

public class MUAInterpreter {
    public static void main(String[] args) {
        // Flags
        boolean flagInteractiveMode = true;
        boolean flagExpressionOut = false;
        boolean flagLispForm = false;
        boolean flagSilent = false;
        boolean flagShowPrompt = false;
        boolean flagPrintTokens = false;
        boolean flagStacktrace = false;
        FileReader scriptFile = null;

        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("--help")) {
                System.out.println("MUA (MakeUp lAnguage) Interpreter");
                System.out.println("Celestial Phineas (Yehang YIN) @ ZJU");
                System.out.println("usage: java MakeUpInterpreter [options] [filename]");
                System.out.println("Run in interactive mode by default.");
                System.out.println("Options:");
                System.out.println("\t--help  : Show this help message.");
                System.out.println("\t--out   : Output the value of the expressions.");
                System.out.println("\t--lisp  : Output the expressions in LISP form.");
                System.out.println("\t--silent: Hide all warnings and errors.");
                System.out.println("\t--prompt: Show prompt.");
                System.out.println("\t--tokens: Print tokens.");
                System.out.println("\t--trace : Print stack trace.");
                System.exit(0);
            }
            else if(args[i].equals("--out"))    { flagExpressionOut = true; }
            else if(args[i].equals("--lisp"))   { flagLispForm = true; }
            else if(args[i].equals("--silent")) { flagSilent = true; }
            else if(args[i].equals("--prompt")) { flagShowPrompt = true; }
            else if(args[i].equals("--tokens")) { flagPrintTokens = true; }
            else if(args[i].equals("--trace"))  { flagStacktrace = true; }
            else {
                flagInteractiveMode = false;
                try {
                    scriptFile = new FileReader(args[i]);
                } catch (Exception e) {
                    System.out.println("Cannot find file " + args[i]);
                    System.exit(1);
                }
                if(scriptFile == null) {
                    System.out.println("Cannot find file " + args[i]);
                    System.exit(1);
                }
            }
        }

        int inCount = 0, outCount = 0;
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        Scanner scanner = MUAIO.getInstance().in;
        List<Token> tokenList = null;
        MUACore core = MUACore.getInstance();

        if(flagLispForm)    ExprListObject.setLispForm();
        if(flagStacktrace)  MUACore.setShowStackTrace();

        if(flagInteractiveMode) while (true) {
            try {
                if (flagShowPrompt)
                    MUAIO.getInstance().out.print(String.format("%1$-10s", "[In" + inCount + "]\t:= "));
                inCount++;
                StringBuilder inputBuffer = new StringBuilder();
                while (scanner.hasNext()) {
                    String string = scanner.nextLine();
                    inputBuffer.append(string);
                    if(isCompleteInput(inputBuffer.toString())) {
                        lexicalAnalyzer.sendLine(inputBuffer.toString());
                        tokenList = lexicalAnalyzer.getTokenList();
                        lexicalAnalyzer.cleanUp();
                        break;
                    }
                }

                if (!flagSilent) {
                    if (flagPrintTokens) {
                        if (tokenList != null) {
                            for (Token t : tokenList) {
                                System.out.print(t);
                            }
                            System.out.println();
                        }
                    }

                    // Result
                    if (tokenList != null) {
                        List<MUAObject> result = MUACore.evaluate(tokenList);
                        if (flagExpressionOut) for (MUAObject obj : result) {
                            if (flagShowPrompt)
                                MUAIO.getInstance().out
                                        .print(String.format("%1$-10s", "[Out" + outCount + "]\t:= "));
                            outCount++;
                            MUAIO.getInstance().out.println(obj);
                        }
                        StackTrace.getInstance().clear();
                    }
                }
            } catch (Exception e) {
                MUAErrorMessage.error(ErrorStringResource.mua_main,
                        ErrorStringResource.unknow_internal_error, e.toString());
            }
        }
        // File
        else {
            BufferedReader buffer = new BufferedReader(scriptFile);
            String inputLine;
            while(true) {
                try {
                    inputLine = buffer.readLine();
                    if(inputLine == null) System.exit(0);
                    lexicalAnalyzer.sendLine(inputLine);
                    if (lexicalAnalyzer.isCompleteLine()) {
                        tokenList = lexicalAnalyzer.getTokenList();
                        lexicalAnalyzer.cleanUp();
                        continue;
                    }
                    MUACore.evaluate(tokenList);
                    StackTrace.getInstance().clear();
                } catch (Exception e) {
                    MUAErrorMessage.error(ErrorStringResource.mua_main, ErrorStringResource.unknow_internal_error,
                        e.toString());
                    System.exit(1);
                }
            }
        }
    }
    private static boolean isCompleteInput(String input) {
        int parenCount = 0, bracketCount = 0;
        for(int i = 0; i < input.length(); i++) {
            if(input.charAt(i) == '(') parenCount++;
            if(input.charAt(i) == ')') parenCount--;
            if(input.charAt(i) == '[') bracketCount++;
            if(input.charAt(i) == ']') bracketCount--;
        }
        if(parenCount <= 0 && bracketCount <= 0) return true;
        return false;
    }
}
