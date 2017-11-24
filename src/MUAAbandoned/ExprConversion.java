package MUAAbandoned;

import MUAFrontEnd.Token;

import java.util.LinkedList;
import java.util.List;

public class ExprConversion {
    // The resulting formula is properly parenthesized, believe it or not.
    // -- Donald Knuth, the great type designer and typographer :)
    // This function is not used, for it does not meet the need.
    public static List<Token> fullParenthesize(List<Token> tokens) {
        if(tokens == null) return null;
        List<Token> result = new LinkedList<>();
        Token leftParenthesis = new Token(Token.Type.EXPROP, "(");
        Token rightParenthesis = new Token(Token.Type.EXPROP, ")");
        result.add(leftParenthesis);
        result.add(leftParenthesis);
        for(Token token : tokens) {
            if(token.type != Token.Type.EXPROP) {
                result.add(token);
            } else {
                if(token.val.equals("+") || token.val.equals("-")) {
                    result.add(rightParenthesis);
                    result.add(rightParenthesis);
                    result.add(token);
                    result.add(leftParenthesis);
                    result.add(leftParenthesis);
                } else if(token.val.equals("*") || token.val.equals("/") || token.val.equals("%")) {
                    result.add(rightParenthesis);
                    result.add(token);
                    result.add(leftParenthesis);
                } else if(token.val.equals("(") || token.val.equals(")")) {
                    result.add(token);
                    result.add(token);
                    result.add(token);
                }
            }
        }
        result.add(rightParenthesis);
        result.add(rightParenthesis);
        return result;
    }
}
