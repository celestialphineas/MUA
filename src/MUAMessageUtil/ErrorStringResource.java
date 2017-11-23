package MUAMessageUtil;

public enum ErrorStringResource {
    lexical_analyzing("Lexical analyzing"),
    too_many_parentheses("You've got %% more right parentheses. "),
    too_many_brackets("You've got %% more right brackets. ");
    private String resourceVal;
    ErrorStringResource(String str) {
        resourceVal = str;
    }
    public String getResourceVal() { return resourceVal; }
}
