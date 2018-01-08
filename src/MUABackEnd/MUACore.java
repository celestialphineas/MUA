package MUABackEnd;

import MUABackEnd.MUANamespace.GlobalNamespace;
import MUABackEnd.MUANamespace.Namespace;
import MUABackEnd.MUAObjects.*;
import MUABackEnd.MUAObjects.BuiltInOperations.*;
import MUAFrontEnd.LexicalAnalyzer;
import MUAFrontEnd.Token;
import MUAIO.MUAIO;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

// The MUA core
public class MUACore {
    private static boolean showStackTrace = false;
    private static MUACore ourInstance = new MUACore();
    public static MUACore getInstance()         { return ourInstance; }
    public static void setShowStackTrace()      { showStackTrace = true; }
    public static void unsetShowStackTrace()    { showStackTrace = false; }
    private MUACore() { MUAInit(); }
    public void MUAInit() {
        Namespace global = GlobalNamespace.getInstance();
        // Clear global namespace
        global.clear();
        // Load core operations
        loadCoreOperations();
        // Load startup script
        loadStartupScript();
    }

    private void loadStartupScript() {
        String data
                = new Scanner(getClass().getClassLoader()
                .getResourceAsStream("startup.mua"), "UTF-8")
                .useDelimiter("\\A").next();
        LexicalAnalyzer internalLexicalAnalyzer = new LexicalAnalyzer();
        internalLexicalAnalyzer.sendLine(data);
        MUACore.evaluate(internalLexicalAnalyzer.getTokenList(), GlobalNamespace.getInstance());
        internalLexicalAnalyzer.cleanUp();
    }

    public static void exit() {
        System.exit(0);
    }

    public static void loadCoreOperations() {
        Namespace global = GlobalNamespace.getInstance();
        // Add built-in functions
        Class[] builtInFunctionClasses = {
            // Arithmetic and numerical operations
                MUAadd.class, MUAsub.class, MUAmul.class, MUAdiv.class, MUAmod.class,
                MUAintervalrandom.class, MUAfloor.class,
                MUAsqrt.class, MUAsin.class,
            // Logical operations
                MUAand.class, MUAor.class, MUAnot.class,
            // Comparators
                MUAgt.class, MUAeq.class, MUAlt.class,
            // Boolean values
                MUAtrue.class, MUAfalse.class,
            // Character values
            // These built-in operations take no arguments and return a single MUA word containing exactly the character
                // chplus +         chminus -       chaster *       chslash /
                // chlbrac [        chrbrac ]       chlparenth (    chrparenth )
                // chtab \t         chspace <space> chendl \n
                MUAchplus.class, MUAchminus.class, MUAchaster.class, MUAchslash.class,
                MUAchlbrac.class, MUAchrbrac.class, MUAchlparenth.class, MUAchrparenth.class,
                MUAchtab.class, MUAchspace.class, MUAchendl.class,
            // List head
            // Head of the lists, take two arguments by default
                MUAlist.class,
            // List operations to do functional programming
                MUAflatten1level.class, MUAapply.class, MUAmap.class, MUApart.class, MUAlength.class,
            // Name binding and recalling operations
                // declare [formal_par1, formal_par2, ...]
                //      Declare a function for later use, and thus MUA can know the prototype of some function and thus
                //      be able to do the lexical analysis for its parameters. We need this because MUA does
                //      one-directional scanning. This operation can be useful to do lazy evaluation, nested function
                //      calls and recursion.
                // make "name val
                //      Make, is the operation to bind the value to a name in the local namespace
                // erase "name
                //      Erase the name in the local namespace
                // thing "name      :name
                //      Operation that returns the value bound to the name
                MUAdeclare.class, MUAmake.class, MUAerase.class, MUAthing.class,
            // Evaluation control operations
                // hold expr
                //      Hold the expression. Some built-in operations do evaluation to a certain slot in their arg list.
                //      The hold operation allows the held expression to be evaluated lazily
                // eval expr
                //      Evaluate the expression. (Forced eval twice)
                // silent expr
                //      Evaluate the expression but return nothing. Null in MUA is defined as nothing.
                //      In MUA, [ null ] you get []
                MUAhold.class, MUAeval.class, MUAsilent.class,
            // Control flow and high order expression replacement
            // These operations are full of side effects and are the evil side of MUA design.
            // You can use them to do imperative programming.
                // test expr
                //      The test operation implementation in MUA provides a possibility to do imperative programming.
                //      The test operation causes side effects. It marks its result in the local scope and return
                //      nothing.
                // ift expr
                //      ift, aka "if true", checks if the local scope has tested something. If the test result in local
                //      scope is true, the operation evaluates its first slot and returns the value of the expression.
                // iff expr
                //      iff, "if false" then expr
                // stop
                //      Stop the evaluation of current expression, stop returns nothing.
                // output expr
                //      Stop the evaluation of current expression, use the expr on the operation's first slot to replace
                //      the evaluating expression. Amazing, urh?
                // repeat times expr
                //      Evaluate the expression for times.
                // if booleantest trueval falseval
                //      Functional if
                MUAtest.class, MUAift.class, MUAiff.class, MUAstop.class, MUAoutput.class, MUArepeat.class,
                MUAif.class,
            // Namespace control
                // exportsymbol "name
                //      Export the local symbol with certain name to its upper level namespace
                // exportnamespace "name
                //      Export the namespace with certain name to its upper level namespace and thus make the inner
                //      namespace accessible to its outer level. The outer level may access its inner level with
                //      something like ":inner.name"
                // exportall
                //      Export all symbols defined in the local namespace to the upper level
                // exposesymbol "name
                //      Similar to exportsymbol, except that this expose the symbol to the global namespace
                // exposenamespace "name
                //      Similar to exportnamespace, except that this expose the namespace to the global namespace
                // exposeall
                //      Expose all symbols defined in the local namespace to the global namespace
                // clearlocal
                //      Clear all definitions in the local scope
                MUAexportsymbol.class, MUAexportnamespace.class, MUAexportall.class,
                MUAexposesymbol.class, MUAexposenamespace.class, MUAexposeall.class,
                MUAclearlocal.class, MUAnamespacelist.class, MUApparentnamespacelist.class,
            // MUA core utils
                // clearglobal
                //      Operation that clears all user-defined names in global namespace and load back the default
                //      definitions of built-in operations
                // exit
                //      Exit MUA core
                // reloadcore
                //      Reload MUA core, this operation reload the MUA core (i.e. load back the default operation
                //      definitions) without clearing user-defined names
                // setcalldepth depth
                //      This operation set up the maximum depth of call stack, usually 4096 is a good choice, since MUA
                //      does not implement itself's call stack, rather, MUA use JVM's call stack to make things live.
                MUAclearglobal.class, MUAexit.class, MUAreloadcore.class, MUAsetcalldepth.class, MUAloadmodule.class,
            // Predicates
                // isname "word
                //      Returns a boolean indicating if the word is an accessible expression in the current scope
                // isnumber, isword, islist, isbool, isempty
                //      These predicates all take one argument, and return a boolean object true or false accordingly.
                //      The predicates all evaluate its argument, and you will have to use "hold" for some desired
                //      effects.
                // isempty
                //      isempty evaluate the first argument and return a boolean to indicate if the expression is empty
                //      (with a head only) or the string is empty.
                MUAisname.class, MUAisnumber.class, MUAisword.class, MUAislist.class, MUAisbool.class, MUAisempty.class,
            // IO and system
                MUAprint.class, MUAprintinteger.class, MUAread.class, MUAreadlist.class,
                MUAwait.class, MUAsave.class, MUAload.class, MUAunicode.class, MUAfromunicode.class
        };
        for(Class classObj : builtInFunctionClasses) {
            registerBuiltInOperation(classObj, global);
        }
    }

    private static void registerBuiltInOperation(Class Op, Namespace namespace) {
        try {
            Object object = Op.newInstance();
            if(object instanceof OperationObject) {
                OperationObject op = (OperationObject)object;
                namespace.set(op.getName(), op);
            }
        } catch (Exception e) {
            MUAErrorMessage.error(ErrorStringResource.mua_core,
                ErrorStringResource.mua_core_init_error, Op.toString());
        }
    }

    public static List<MUAObject> evaluate(List<Token> tokenList) {
        return evaluate(tokenList, GlobalNamespace.getInstance());
    }
    public static List<MUAObject> evaluate(List<Token> tokenList, Namespace baseNamespace) {
        List<MUAObject> result = new LinkedList<>();
        if(tokenList == null || tokenList.isEmpty()) {
            return result;
        }
        Deque<Token> tokens = new LinkedList<>(tokenList);
        while (!tokens.isEmpty()) {
            try {
                MUAObject obj = makeObject(tokens, baseNamespace);
                try {
                    if(obj instanceof ExprListObject) {
                        ((ExprListObject) obj).evalExpr();
                        MUAObject returnVal = ((ExprListObject) obj).getReturnVal();
                        if(returnVal != null) {
                            result.add(returnVal);
                        }
                    }
                    else result.add(obj);
                } catch (MUAStackOverflowException e) {
                    if(showStackTrace)
                        MUAIO.getInstance().err.println("Stack trace: " + StackTrace.getInstance().toString());
                    MUAErrorMessage.error(ErrorStringResource.mua_core,
                        ErrorStringResource.stack_overflow, obj.toString());
                } catch (MUARuntimeException e) {
                    if(showStackTrace)
                        MUAIO.getInstance().err.println("Stack trace: " + StackTrace.getInstance().toString());
                    MUAErrorMessage.error(ErrorStringResource.mua_core,
                            ErrorStringResource.runtime_error, obj.toString());
                } catch (Exception e) {
                    if(showStackTrace)
                        MUAIO.getInstance().err.println("Stack trace: " + StackTrace.getInstance().toString());
                    MUAErrorMessage.error(ErrorStringResource.mua_core,
                            ErrorStringResource.unknow_internal_error, e.toString());
                }
            } catch (MakeObjectFailureException e) {
                return result;
            }
        }
        return result;
    }

    private static MUAObject makeObject(Deque<Token> tokens, Namespace namespace)
    throws MakeObjectFailureException {
        if(tokens == null || tokens.isEmpty()) return null;
        Token token = tokens.getFirst();
        tokens.removeFirst();
        if(token.type == Token.Type.WORD) {
            return new WordObject(token.val);
        } else if(token.type == Token.Type.NUMBER) {
            try {
                return new NumObject(token.val);
            } catch (NumberFormatException e) {
                MUAErrorMessage.error(ErrorStringResource.making_objects,
                    ErrorStringResource.number_format, token.val);
                throw new MakeObjectFailureException();
            }
        } else if(token.type == Token.Type.OPERATION) {
            ExprListObject exprList = new ExprListObject();
            exprList.namespace = namespace;
            MUAObject found = exprList.namespace.find(token.val);

            // If not found
            if(found == null) {
                MUAErrorMessage.error(ErrorStringResource.making_objects,
                        ErrorStringResource.unexpected_token, token.val);
                throw new MakeObjectFailureException();
            }

            // Found operation
            // This will add a dumb head to the exprList
            // The binding will be delayed to evaluation
            MUAObject head = new DumbHeadObject(token.val);
            // Ensure that "found" is always an operation object
            if(found instanceof BuiltInOperation) {
            } else if(found instanceof ExprListObject) {
                try {
                    found = new CustomOperation((ExprListObject)found);
                } catch (UnOperationizableListException e) {
                    MUAErrorMessage.error(ErrorStringResource.making_objects,
                            ErrorStringResource.not_operationizable, token.val);
                    throw new MakeObjectFailureException();
                }
            } else {
                MUAErrorMessage.error(ErrorStringResource.making_objects,
                        ErrorStringResource.not_operationizable, token.val);
                throw new MakeObjectFailureException();
            }

            exprList.objectList.add(head);
            for(int i = 0; i < ((OperationObject)found).getArgc(); i++) {
                MUAObject toAppend = makeObject(tokens, namespace);
                if(toAppend == null) {
                    MUAErrorMessage.error(ErrorStringResource.making_objects,
                        ErrorStringResource.too_few_operands, token.val);
                    throw new MakeObjectFailureException();
                }
                exprList.objectList.add(toAppend);
            }

            return exprList;

        } else if(token.type == Token.Type.EXPROP) {
            // Core does not handle the EXPROP objects
            MUAErrorMessage.error(ErrorStringResource.making_objects,
                    ErrorStringResource.unexpected_token, token.val);
            throw new MakeObjectFailureException();
        } else if(token.type == Token.Type.LBRACKET) {
            ExprListObject list = new ExprListObject();
            // Add list head
            list.objectList.add(new MUAlist());
            // Allocate namespace for list
            Namespace newNamespace = list.allocateNamespace(namespace);
            // Test use:
            // System.out.println("List namespace: " + list.namespace.getName());
            MUAObject toAppend = makeObject(tokens, newNamespace);
            while(toAppend != null) {
                list.objectList.add(toAppend);
                toAppend = makeObject(tokens, newNamespace);
            }
            return list;
        } else { // RBRACKET
            // namespace = namespace.getParent();
            return null;
        }
    }
}

class MakeObjectFailureException extends Exception {
    MakeObjectFailureException()                 { super(); }
    MakeObjectFailureException(String message)   { super(message); }
}
