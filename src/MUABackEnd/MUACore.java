package MUABackEnd;

import MUABackEnd.MUANamespace.GlobalNamespace;
import MUABackEnd.MUANamespace.Namespace;
import MUABackEnd.MUAObjects.*;
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

    private MUACore() {
        // Add built-in functions
        Namespace global = GlobalNamespace.getInstance();
        registerBuiltInOperation(MUAadd.class, global);
        registerBuiltInOperation(MUAlist.class, global);
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

    static MUAObject makeObject(Deque<Token> tokens, Namespace namespace)
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
            
            MUAObject found = exprList.namespace.find(token.val);
            MUAObject head;

            // If not found
            if(found == null) {
                MUAErrorMessage.error(ErrorStringResource.making_objects,
                        ErrorStringResource.unexpected_token, token.val);
                throw new MakeObjectFailureException();
            }
            // Found operation
            if(found instanceof BuiltInOperation) {
                head = found;
            } else if(found instanceof ExprListObject) {
                try {
                    head = new CustomOperation((ExprListObject)found);
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
            for(int i = 0; i < ((OperationObject)head).getArgc(); i++) {
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
