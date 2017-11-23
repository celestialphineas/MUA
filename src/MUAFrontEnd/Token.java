package MUAFrontEnd;

public class Token {
    public enum Type { WORD, OPERATION, NUMBER, EXPROP, LBRACKET, RBRACKET }
    public Type type;
    public String val;
    Token(Type type_, String val_) {
        type = type_; val = val_;
    }
    // Override toString() method for testing
    @Override
    public String toString() {
        return "<" + type.toString() + ": " + val + "> ";
    }
}