package MUAFrontEnd;

// Input: a list of lexemes
// Output: a list of tokens

import java.util.LinkedList;
import java.util.List;

public class Tokenizer {
    static public List<Token> tokenize(List<String> lexemeList) {
        if(lexemeList == null) return null;
        List<Token> result = new LinkedList<>();
        boolean meetColon = false;
        for(String lexeme : lexemeList) {
            if(lexeme == ":") {
                meetColon = true;
                continue;
            }
            if(meetColon) {
                meetColon = false;
                result.add(new Token(Token.Type.OPERATION, "thing"));
                result.add(new Token(Token.Type.WORD, lexeme));
            } else {
                char ch = lexeme.charAt(0);
                if(ch == '\"') {
                    result.add(new Token(Token.Type.WORD, lexeme.substring(1)));
                } else if(ch == '.' || (ch == '-' && lexeme.length() > 1) || Character.isDigit(ch)) {
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

    static boolean isExpressionSymbol(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' || ch == '(' || ch == ')';
    }
}