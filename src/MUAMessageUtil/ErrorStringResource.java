package MUAMessageUtil;

public enum ErrorStringResource {
    lexical_analyzing("Lexical analyzing"),
    finding_variable("Finding variable"),
    finding_namespace("Finding namespace"),
    making_objects("Making objects"),
    mua_core("MUA core"),
    operation_add("Operation add"),
    operation_and("Operation and"),
    operation_declare("Operation declare"),
    operation_div("Operation div"),
    operation_erase("Operation erase"),
    operation_eq("Operation eq"),
    operation_export("Operation export"),
    operation_expose("Operation expose"),
    operation_gt("Operation gt"),
    operation_isname("Operation isname"),
    operation_lt("Operation lt"),
    operation_make("Operation make"),
    operation_mul("Operation mul"),
    operation_not("Operation not"),
    operation_or("Operation or"),
    operation_read("Operation read"),
    operation_repeat("Operation repeat"),
    operation_sub("Operation sub"),
    operation_test("Operation test"),
    operation_thing("Operation thing"),
    mua_custom_operation("Custom operation"),
    too_many_parentheses("You've got %% more right parentheses. "),
    too_many_brackets("You've got %% more right brackets. "),
    undefined_reference("Undefined reference to \"%%\". "),
    undefined_namespace("Undefined reference to namespace \"%%\". "),
    unexpected_token("Unexpected token \"%%\". "),
    unary_unsupported("MUA does not allow unary before non-atomic expression \"%%\". "),
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
