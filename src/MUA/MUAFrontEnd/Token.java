package MUAFrontEnd;

public class Token {
    public enum Type { WORD, OPERATION, NUMBER, THING, EXPROP, LBRACKET, RBRACKET }
    public Type type;
    public String val;
    public Token(Type type_, String val_) {
        type = type_; val = val_;
    }
    public Token(Token token) {
        type = token.type; val = token.val;
    }
    // Override toString() method for testing
    @Override
    public String toString() {
        return "<" + type.toString() + ": " + val + "> ";
    }
}