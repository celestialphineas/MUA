package MUABackEnd.MUAObjects;

public class WordObject implements MUAObject {
    private String val = new String();
    public WordObject(double val_)  { val = Double.toString(val_); }
    public WordObject(int val_)     { val = Integer.toString(val_); }
    public WordObject(boolean val_) { val = val_ ? "true" : "false"; }
    public WordObject(String val_)  { val = val_; }
    public String getVal() { return val; }
    @Override public boolean isAtomic() { return true; }
    @Override public String typeName()  { return "word"; }
    @Override public String toString()  { return val; }
    @Override public String toMUAExprString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(\"");
        for(int i = 0; i < val.length() ; i++) {
            char ch = val.charAt(i);
            boolean validChar = false;
            switch (ch) {
                case '*': stringBuilder.append(" + chaster + \""); break;
                case '\n': stringBuilder.append(" + chendl + \""); break;
                case '[': stringBuilder.append(" + chlbrac + \""); break;
                case '(': stringBuilder.append(" + chlparenth + \""); break;
                case '-': stringBuilder.append(" + chminus + \""); break;
                case '+': stringBuilder.append(" + chplus + \""); break;
                case ']': stringBuilder.append(" + chrbrac + \""); break;
                case ')': stringBuilder.append(" + chrparenth + \""); break;
                case '/': stringBuilder.append(" + chslash + \""); break;
                case ' ': stringBuilder.append(" + chspace + \""); break;
                case '\t': stringBuilder.append(" + chtab + \""); break;
                default: stringBuilder.append(ch); break;
            }
        }
        stringBuilder.append(')');
        String result = stringBuilder.toString()
                .replace("\" +", "")
                .replace("+ \")", ")");
        return result;
    }
}
