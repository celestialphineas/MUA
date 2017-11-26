package MUAMessageUtil;

public enum ErrorStringResource {
    lexical_analyzing("Lexical analyzing"),
    finding_variable("Finding variable"),
    finding_namespace("Finding namespace"),
    making_objects("Making objects"),
    mua_core("MUA core"),
    operation_add("Operation add"),
    operation_make("Operation make"),
    operation_thing("Operation thing"),
    mua_custom_operation("Custom operation"),
    too_many_parentheses("You've got %% more right parentheses. "),
    too_many_brackets("You've got %% more right brackets. "),
    undefined_reference("Undefined reference to \"%%\". "),
    undefined_namespace("Undefined reference to namespace \"%%\". "),
    unexpected_token("Unexpected token \"%%\". "),
    not_operationizable("Object \"%%\" is not operationizable. "),
    number_format("Unrecognized number format \"%%\". "),
    too_few_operands("Too few operands for operation \"%%\". "),
    mua_core_init_error("MUA core initialization error: \"%%\". "),
    operationization_internal("Operationization internal error: \"%%\". "),
    stack_overflow("Call stack overflow: \"%%\". "),
    runtime_error("Runtime error: \"%%\". "),
    incompatible_type("Incompatible type %%. ");
    private String resourceVal;
    ErrorStringResource(String str) {
        resourceVal = str;
    }
    public String getResourceVal() { return resourceVal; }
}
