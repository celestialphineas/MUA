import MUABackEnd.MUACore;
import MUABackEnd.MUAObjects.ExprListObject;
import MUABackEnd.MUAObjects.MUAObject;
import MUABackEnd.StackTrace;
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
    public static boolean flagHomework = true;
    public static void main(String[] args) {
        // Flags
        boolean flagInteractiveMode = true;
        boolean flagExpressionOut = true;
        boolean flagLispForm = false;
        boolean flagSilent = false;
        boolean flagShowPrompt = true;
        boolean flagPrintTokens = false;
        boolean flagStacktrace = true;
        FileReader scriptFile = null;

        if(flagHomework) {
            flagInteractiveMode = true;
            flagExpressionOut = true;
            flagLispForm = false;
            flagSilent = false;
            flagShowPrompt = true;
            flagPrintTokens = false;
            flagStacktrace = true;
        }

        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("--help")) {
                System.out.println("MUA (MakeUp lAnguage) Interpreter");
                System.out.println("Celestial Phineas (Yehang YIN) @ ZJU");
                System.out.println("usage: java MakeUpInterpreter [options] [filename]");
                System.out.println("Run in interactive mode by default.");
                System.out.println("Options:");
                System.out.println("\t--help    : Show this help message.");
                System.out.println("\t--out     : Toggle output of the expressions values.");
                System.out.println("\t--lisp    : Toggle output the expressions in Lisp form.");
                System.out.println("\t--silent  : Toggle hiding of warnings and errors.");
                System.out.println("\t--noprompt: Toggle prompt.");
                System.out.println("\t--tokens  : Toggle printing tokens.");
                System.out.println("\t--trace   : Toggle print stack trace.");
                System.exit(0);
            }
            else if(args[i].equals("--out"))    { flagExpressionOut = !flagExpressionOut; }
            else if(args[i].equals("--lisp"))   { flagLispForm      = !flagLispForm; }
            else if(args[i].equals("--silent")) { flagSilent        = !flagSilent; }
            else if(args[i].equals("--prompt")) { flagShowPrompt    = !flagShowPrompt; }
            else if(args[i].equals("--tokens")) { flagPrintTokens   = !flagPrintTokens; }
            else if(args[i].equals("--trace"))  { flagStacktrace    = !flagStacktrace; }
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
        List<Token> tokenList = null;
        Scanner scanner = MUAIO.getInstance().in;
        MUACore core = MUACore.getInstance();

        if(flagLispForm)    ExprListObject.setLispForm();
        else                ExprListObject.unsetLispForm();
        if(flagStacktrace)  MUACore.setShowStackTrace();
        else                MUACore.unsetShowStackTrace();
        if(flagHomework)    LexicalAnalyzer.unsetStrict();
        else                LexicalAnalyzer.setStrict();

        if(flagInteractiveMode) while (true) {
            try {
                if (flagShowPrompt)
                    MUAIO.getInstance().out.print(String.format("%1$-10s", "[In" + inCount + "]\t:= "));
                inCount++;
                StringBuilder inputBuffer = new StringBuilder();
                while (scanner.hasNext()) {
                    String string = scanner.nextLine();
                    if(flagHomework) {
                        inputBuffer.append(string);
                        lexicalAnalyzer.sendLine(string);
                        if(isCompleteInput(inputBuffer.toString())) {
                            tokenList = lexicalAnalyzer.getTokenList();
                            lexicalAnalyzer.cleanUp();
                            break;
                        }
                    } else {
                        lexicalAnalyzer.sendLine(string);
                        if (lexicalAnalyzer.isCompleteLine()) {
                            tokenList = lexicalAnalyzer.getTokenList();
                            lexicalAnalyzer.cleanUp();
                            break;
                        }
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
                                        .print(String.format("%1$-11s", "[Out" + outCount + "]\t:= "));
                            outCount++;
                            MUAIO.getInstance().out.println(obj);
                        }
                        StackTrace.getInstance().clear();
                    }
                }
            } catch (Exception e) {
                MUAErrorMessage.error(ErrorStringResource.mua_main,
                        ErrorStringResource.unknow_internal_error, e.toString());
                e.printStackTrace();
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
