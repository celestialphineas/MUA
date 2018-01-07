package MUAFrontEnd;

// Input: character stream
// Output: a list of tokens

import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.util.*;

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
        } else if(readingComment) {
            // Do nothing
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
                if(stringList.size() > 0) stringList.removeLast();
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
                 if((ch == '+' || ch == '-') && minusBreak) {
                    stringBuffer.append(ch);
                } else if(!readingComment) {
                     stringList.addLast(Character.toString(ch));
                }
            }
        } else { // Ordinary character
            if(!readingComment) {
                if(stringBuffer.length() == 1) {
                    if(stringBuffer.charAt(0) == '+' || stringBuffer.charAt(0) == '-')
                        if(ch != '.' && !Character.isDigit(ch)) {
                            stringList.addLast(stringBuffer.toString());
                            stringBuffer.delete(0, stringBuffer.length());
                        }
                }
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
        return input.replaceAll("make\\s*\"(\\S*?)\\s*\\[\\s*\\[(.*?)\\]",
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
        // Infix conversion
        result = infixConvert(result);
        return result;
    }

    // Convert infix
    static public List<Token> infixConvert(List<Token> tokenList) {
        // First round: handle high priority operators
        List<Token> highEliminated = eliminateHighPrio(tokenList);
        // Second round: handle low priority operators
        List<Token> lowEliminated = eliminateLowPrio(highEliminated);
        // Third round: remove all expression operators and thing tokens
        List<Token> result = new LinkedList<>();
        for(Token token : lowEliminated) {
            if(token.type == Token.Type.THING) {
                result.add(new Token(Token.Type.OPERATION, "thing"));
                result.add(new Token(Token.Type.WORD, token.val));
            } else if(token.type == Token.Type.EXPROP) {
            } else {
                result.add(token);
            }
        }
        return result;
    }

    static private List<Token> eliminateHighPrio(List<Token> tokenList) {
        List<Token> result = new LinkedList<>();
        Deque<Token> tokens = new LinkedList<>(tokenList);
        while(!tokens.isEmpty()) {
            Token token = tokens.getFirst();
            List<Token> recResult = recursivelyElimHighPrio(tokens);
            if(recResult != null) result.addAll(recResult);
        }
        return result;
    }
    static private List<Token> eliminateLowPrio(List<Token> tokenList) {
        List<Token> result = new LinkedList<>();
        Deque<Token> tokens = new LinkedList<>(tokenList);
        while(!tokens.isEmpty()) {
            Token token = tokens.getFirst();
            List<Token> recResult = recursivelyElimLowPrio(tokens);
            if(recResult != null) result.addAll(recResult);
        }
        return result;
    }

    static private LinkedList<Token> recursivelyElimHighPrio(Deque<Token> tokens) {
        LinkedList<Token> result = new LinkedList<>();
        final LinkedList<Token> operand1 = new LinkedList<>();
        Token operator = null;

        while(!tokens.isEmpty()) {
            Token token = tokens.getFirst();
            tokens.removeFirst();
            if(isLeftParenthesisToken(token) || token.type == Token.Type.LBRACKET) {
                LinkedList<Token> subList = recursivelyElimHighPrio(tokens);
                subList.addFirst(token);
                if(operator != null) {
                    if(operator.val.equals("*"))
                        operand1.addFirst(new Token(Token.Type.OPERATION, "mul"));
                    else if(operator.val.equals("/"))
                        operand1.addFirst(new Token(Token.Type.OPERATION, "div"));
                    else
                        operand1.addFirst(new Token(Token.Type.OPERATION, "mod"));
                    operand1.addFirst(new Token(Token.Type.EXPROP, "("));
                    operand1.addAll(subList);
                    operand1.add(new Token(Token.Type.EXPROP, ")"));
                    operator = null;
                } else {
                    result.addAll(operand1);
                    operand1.clear();
                    operand1.addAll(subList);
                }
                continue;
            } else if(isRightParenthesisToken(token) || token.type == Token.Type.RBRACKET) {
                if(operator != null) {
                    MUAErrorMessage.warn(ErrorStringResource.lexical_analyzing,
                        ErrorStringResource.unexpected_token, operator.val);
                    result.clear();
                } else {
                    result.addAll(operand1);
                }
                result.add(token);
                return result;
            } else if(isHighPrioInfixOperatorToken(token)) {
                if(operand1.isEmpty()) {
                    // For plus and minus, here goes the unary handling
                    MUAErrorMessage.warn(ErrorStringResource.lexical_analyzing,
                        ErrorStringResource.unexpected_token, token.val);
                }
                operator = token;
                continue;
            }

            if(operator != null) {
                if(operator.val.equals("*"))
                    operand1.addFirst(new Token(Token.Type.OPERATION, "mul"));
                else if(operator.val.equals("/"))
                    operand1.addFirst(new Token(Token.Type.OPERATION, "div"));
                else
                    operand1.addFirst(new Token(Token.Type.OPERATION, "mod"));
                operand1.addFirst(new Token(Token.Type.EXPROP, "("));
                operand1.add(token);
                operand1.add(new Token(Token.Type.EXPROP, ")"));
                operator = null;
            } else {
                result.addAll(operand1);
                operand1.clear();
                operand1.add(token);
            }
        }
        result.addAll(operand1);
        return result;
    }
    static private LinkedList<Token> recursivelyElimLowPrio(Deque<Token> tokens) {
        LinkedList<Token> result = new LinkedList<>();
        final LinkedList<Token> operand1 = new LinkedList<>();
        Token operator = null;

        while(!tokens.isEmpty()) {
            Token token = tokens.getFirst();
            tokens.removeFirst();
            if(isLeftParenthesisToken(token) || token.type == Token.Type.LBRACKET) {
                LinkedList<Token> subList = recursivelyElimLowPrio(tokens);
                subList.addFirst(token);
                if(operator != null) {
                    if(operand1.isEmpty()) operand1.add(new Token(Token.Type.NUMBER, "0"));
                    if(operator.val.equals("+"))
                        operand1.addFirst(new Token(Token.Type.OPERATION, "add"));
                    else
                        operand1.addFirst(new Token(Token.Type.OPERATION, "sub"));
                    operand1.addFirst(new Token(Token.Type.EXPROP, "("));
                    operand1.addAll(subList);
                    operand1.add(new Token(Token.Type.EXPROP, ")"));
                    operator = null;
                } else {
                    result.addAll(operand1);
                    operand1.clear();
                    operand1.addAll(subList);
                }
                continue;
            } else if(isRightParenthesisToken(token) || token.type == Token.Type.RBRACKET) {
                if(operator != null) {
                    MUAErrorMessage.warn(ErrorStringResource.lexical_analyzing,
                            ErrorStringResource.unexpected_token, operator.val);
                    result.clear();
                } else {
                    result.addAll(operand1);
                }
                result.add(token);
                return result;
            } else if(isLowPrioInfixOperatorToken(token)) {
                if(operator != null) {
                    if(operator.val.equals(token.val)) operator.val = "+";
                    else operator.val = "-";
                }
                else operator = token;
                continue;
            }

            if(operator != null) {
                if(operand1.isEmpty()) operand1.add(new Token(Token.Type.NUMBER, "0"));
                if(operator.val.equals("+"))
                    operand1.addFirst(new Token(Token.Type.OPERATION, "add"));
                else
                    operand1.addFirst(new Token(Token.Type.OPERATION, "sub"));
                operand1.addFirst(new Token(Token.Type.EXPROP, "("));
                operand1.add(token);
                operand1.add(new Token(Token.Type.EXPROP, ")"));
                operator = null;
            } else {
                result.addAll(operand1);
                operand1.clear();
                operand1.add(token);
            }
        }
        result.addAll(operand1);
        return result;
    }

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
    // MUA token utilities
    static boolean isHighPrioInfixOperatorToken(Token token) {
        if(token.type != Token.Type.EXPROP) return false;
        if(token.val.equals("*") || token.val.equals("/") || token.val.equals("%")) return true;
        return false;
    }
    static boolean isLowPrioInfixOperatorToken(Token token) {
        if(token.type != Token.Type.EXPROP) return false;
        if(token.val.equals("+") || token.val.equals(("-"))) return true;
        return false;
    }
    static boolean isInfixOperatorToken(Token token) {
        return isHighPrioInfixOperatorToken(token) || isLowPrioInfixOperatorToken(token);
    }
    static boolean isLeftParenthesisToken(Token token) {
        return token.type == Token.Type.EXPROP && token.val.equals("(");
    }
    static boolean isRightParenthesisToken(Token token) {
        return token.type == Token.Type.EXPROP && token.val.equals(")");
    }
}
