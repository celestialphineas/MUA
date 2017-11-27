package MUABackEnd;

import MUABackEnd.MUANamespace.GlobalNamespace;
import MUABackEnd.MUANamespace.Namespace;
import MUABackEnd.MUAObjects.*;
import MUABackEnd.MUAObjects.BuiltInOperations.*;
import MUAFrontEnd.Token;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// The MUA core
public class MUACore {
    private static MUACore ourInstance = new MUACore();
    public static MUACore getInstance() {
        return ourInstance;
    }

    private MUACore() { MUAInit(); }
    public void MUAInit() {
        Namespace global = GlobalNamespace.getInstance();
        // Clear global namespace
        global.clear();
        // Load core operations
        loadCoreOperations();
    }
    public void loadCoreOperations() {
        Namespace global = GlobalNamespace.getInstance();
        // Add built-in functions
        registerBuiltInOperation(MUAadd.class, global);
        registerBuiltInOperation(MUAand.class, global);
        registerBuiltInOperation(MUAdeclare.class, global);
        registerBuiltInOperation(MUAdiv.class, global);
        registerBuiltInOperation(MUAeq.class, global);
        registerBuiltInOperation(MUAerase.class, global);
        registerBuiltInOperation(MUAexport.class,global);
        registerBuiltInOperation(MUAexportnamespace.class, global);
        registerBuiltInOperation(MUAexpose.class, global);
        registerBuiltInOperation(MUAexposenamespace.class, global);
        registerBuiltInOperation(MUAeval.class, global);
        registerBuiltInOperation(MUAfalse.class, global);
        registerBuiltInOperation(MUAgt.class, global);
        registerBuiltInOperation(MUAhold.class, global);
        registerBuiltInOperation(MUAiff.class, global);
        registerBuiltInOperation(MUAift.class, global);
        registerBuiltInOperation(MUAisname.class, global);
        registerBuiltInOperation(MUAlist.class, global);
        registerBuiltInOperation(MUAlt.class, global);
        registerBuiltInOperation(MUAmake.class, global);
        registerBuiltInOperation(MUAmod.class, global);
        registerBuiltInOperation(MUAmul.class, global);
        registerBuiltInOperation(MUAnot.class, global);
        registerBuiltInOperation(MUAor.class, global);
        registerBuiltInOperation(MUAoutput.class, global);
        registerBuiltInOperation(MUAprint.class, global);
        registerBuiltInOperation(MUAread.class, global);
        registerBuiltInOperation(MUAreadlist.class, global);
        registerBuiltInOperation(MUArepeat.class, global);
        registerBuiltInOperation(MUAstop.class, global);
        registerBuiltInOperation(MUAsub.class, global);
        registerBuiltInOperation(MUAtest.class, global);
        registerBuiltInOperation(MUAthing.class, global);
        registerBuiltInOperation(MUAtrue.class, global);
    }

    private void registerBuiltInOperation(Class Op, Namespace namespace) {
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

    public static List<MUAObject> evaluate(List<MUAObject> exprList) {
        List<MUAObject> result = new LinkedList<>();
        for(MUAObject obj : exprList) {
            if(obj instanceof ExprListObject) {
                try {
                    ((ExprListObject) obj).evalExpr();
                    MUAObject returnVal = ((ExprListObject) obj).getReturnVal();
                    if(returnVal != null) {
                        result.add(returnVal);
                    }
                } catch (MUAStackOverflowException e) {
                    MUAErrorMessage.error(ErrorStringResource.mua_core,
                        ErrorStringResource.stack_overflow, obj.toString());
                } catch (MUARuntimeException e) {
                    MUAErrorMessage.error(ErrorStringResource.mua_core,
                        ErrorStringResource.runtime_error, obj.toString());
                }
            } else if(obj != null) {
                result.add(obj);
            }
        }
        return result;
    }

    public static List<MUAObject> makeExprList(List<Token> tokenList) {
        if(tokenList == null || tokenList.isEmpty()) {
            return null;
        }
        List<MUAObject> result = new LinkedList<>();
        Deque<Token> tokens = new LinkedList<>(tokenList);
        while(!tokens.isEmpty()) {
            try {
                result.add(makeObject(tokens, GlobalNamespace.getInstance()));
            } catch (MakeObjectFailureException e) {
                return null;
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
            namespace = list.allocateNamespace(namespace);
            // Test use:
            // System.out.println("List namespace: " + list.namespace.getName());
            MUAObject toAppend = makeObject(tokens, namespace);
            while(toAppend != null) {
                list.objectList.add(toAppend);
                toAppend = makeObject(tokens, namespace);
            }
            return list;
        } else { // RBRACKET
            namespace = namespace.getParent();
            return null;
        }
    }
}

class MakeObjectFailureException extends Exception {
    MakeObjectFailureException()                 { super(); }
    MakeObjectFailureException(String message)   { super(message); }
}
