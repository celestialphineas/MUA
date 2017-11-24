package MUAMessageUtil;

public enum ErrorStringResource {
    lexical_analyzing("Lexical analyzing"),
    finding_variable("Finding variable"),
    finding_namespace("Finding namespace"),
    too_many_parentheses("You've got %% more right parentheses. "),
    too_many_brackets("You've got %% more right brackets. "),
    undefined_reference("Undefined reference to \"%%\". "),
    undefined_namespace("Undefined reference to namespace \"%%\". ");
    private String resourceVal;
    ErrorStringResource(String str) {
        resourceVal = str;
    }
    public String getResourceVal() { return resourceVal; }
}
