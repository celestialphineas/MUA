package MUAFrontEnd;

// Input: character stream
// Output: a list of tokens

import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// The lexical analyzer is a state machine
public class LexicalAnalyzer {
    final private StringBuilder stringBuffer = new StringBuilder();
    final private Deque<String> stringList = new LinkedList<>();
    private boolean completeLine = true;
    private boolean readingComment = false;
    private int slashCount = 0;
    private int parenthesisCount = 0;
    private int squareBracketCount = 0;

    // Lexical analyzer constructor
    public LexicalAnalyzer() {}
    // Send characters to the analyzer
    public void sendChar(char ch) {
        // If meet a blank char
        if(isBlankChar(ch)) {
            if(ch == '\n') {
                slashCount = 0;
                readingComment = false;
            }
            // String segmentation
            if(stringBuffer.length() != 0) {
                stringList.add(stringBuffer.toString());
                // Clear the string buffer
                stringBuffer.delete(0, stringBuffer.length());
            }
        } else if(isBreakableSymbol(ch)) {
            boolean minusBreak = true;
            if(stringBuffer.length() > 0 && !readingComment) {
                char lastCh = stringBuffer.charAt(stringBuffer.length() - 1);
                if(lastCh != '-' && lastCh != '+') {
                    minusBreak = false;
                }
                stringList.add(stringBuffer.toString());
                // Clear the string buffer
                stringBuffer.delete(0, stringBuffer.length());
            }
            if(ch == '/') {
                slashCount++;
            } else {
                slashCount = 0;
            }
            if(slashCount == 2) {
                stringList.removeLast();
                readingComment = true;
                slashCount = 0;
            } else {
                if(ch == '(') parenthesisCount++;
                if(ch == ')') parenthesisCount--;
                if(ch == '[') squareBracketCount++;
                if(ch == ']') squareBracketCount--;
                // IMPORTANT NOTE: The original design of MUA is ambiguous
                // There are two possible interpretation of the expression
                // [1 -1]
                // It can be interpreted as:
                // [mul 1 1]
                // or:
                // [1 (-1)]
                // In my design approach, [1 -1] will be regarded as the latter
                // [1-1]    -> [mul 1 1]
                // [1- 1]   -> [mul 1 1]
                // [1 -1]   -> [1 -1]
                if((ch == '-' || ch == '+') && minusBreak) {
                    stringBuffer.append(ch);
                } else if(!readingComment) {
                    stringList.addLast(Character.toString(ch));
                }
            }
        } else { // Ordinary character
            if(!readingComment) {
                stringBuffer.append(ch);
            }
        }
        completeLine = checkLineCompleteness();
    }
    public void sendLine(String input) {
        input = preprocess(input);
        for(int i = 0; i < input.length(); i++) {
            sendChar(input.charAt(i));
        }
        sendChar('\n');
        completeLine = checkLineCompleteness();
    }
    // Pre-process strings
    // Like, declare before definition
    private static String preprocess(String input) {
        if(input == null) return null;
        return input.replaceAll("make\\s*\"(.*?)\\s*\\[\\s*\\[(.*?)\\]",
                "declare \"$1 [$2] make \"$1 [[$2]");
    }
    public void sendStringBuffer(StringBuilder buffer) {
        for(int i = 0; i < buffer.length(); i++) {
            sendChar(buffer.charAt(i));
        }
        sendChar('\n');
        completeLine = checkLineCompleteness();
    }
    boolean checkLineCompleteness() {
        return parenthesisCount <= 0 && squareBracketCount <= 0;
    }

    // Cleaning the buffer and the string list
    public void clearBuffer() {
        stringBuffer.delete(0, stringBuffer.length());
    }
    public void clearStringList() {
        stringList.clear();
    }
    public void cleanUp() {
        stringBuffer.delete(0, stringBuffer.length());
        stringList.clear();
        completeLine = false;
        readingComment = false;
        slashCount = 0;
        parenthesisCount = 0;
        squareBracketCount = 0;
    }

    // Tokenize
    static public List<Token> tokenize(List<String> lexemeList) {
        if(lexemeList == null) return null;
        List<Token> result = new LinkedList<>();
        boolean meetColon = false;
        for(String lexeme : lexemeList) {
            if(lexeme.equals(":")) {
                meetColon = true;
                continue;
            }
            if(meetColon) {
                meetColon = false;
                // If there is no need to support infix, use the statements below
//                result.add(new Token(Token.Type.OPERATION, "thing"));
//                result.add(new Token(Token.Type.WORD, lexeme));
                result.add(new Token(Token.Type.THING, lexeme));
            } else {
                char ch = lexeme.charAt(0);
                if(ch == '\"') {
                    result.add(new Token(Token.Type.WORD, lexeme.substring(1)));
                } else if(ch == '.'
                        || ((ch == '-' || ch == '+') && lexeme.length() > 1)
                        || Character.isDigit(ch)) {
                    result.add(new Token(Token.Type.NUMBER, lexeme));
                } else if(isExpressionSymbol(ch)) {
                    result.add(new Token(Token.Type.EXPROP, lexeme));
                } else if(ch == '[') {
                    result.add(new Token(Token.Type.LBRACKET, lexeme));
                } else if(ch == ']') {
                    result.add(new Token(Token.Type.RBRACKET, lexeme));
                } else {
                    result.add(new Token(Token.Type.OPERATION, lexeme));
                }
            }
        }
        return result;
    }

    // Convert infix


    // Get the results of lexical analysis
    public boolean isCompleteLine() { return completeLine; }
    public List<String> getStringList() {
        if(!completeLine) return null;
        if(stringList.isEmpty()) return null;
        return new LinkedList<>(stringList);
    }
    public List<Token> getTokenList() {
        if(parenthesisCount < 0) {
            MUAErrorMessage.error(ErrorStringResource.lexical_analyzing,
                    ErrorStringResource.too_many_parentheses,
                    String.valueOf(-parenthesisCount));
            cleanUp();
            return null;
        }
        if(squareBracketCount < 0) {
            MUAErrorMessage.error(ErrorStringResource.lexical_analyzing,
                    ErrorStringResource.too_many_brackets,
                    String.valueOf(-squareBracketCount));
            cleanUp();
            return null;
        }
        return LexicalAnalyzer.tokenize(getStringList());
    }

    // MUA character property utilities
    static boolean isBlankChar(char ch) {
        return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t';
    }
    static boolean isBreakableSymbol(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' || ch == ':'
                || ch == '(' || ch == ')' || ch == '[' || ch == ']';
    }
    static boolean isExpressionSymbol(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' || ch == '(' || ch == ')';
    }
}
