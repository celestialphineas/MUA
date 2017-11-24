package MUABackEnd;

import MUABackEnd.MUAObjects.ExprListObject;
import MUABackEnd.MUAObjects.MUAObject;
import MUABackEnd.MUAObjects.NumObject;
import MUABackEnd.MUAObjects.WordObject;
import MUAFrontEnd.Token;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// The MUA core
public class MUACore {
    private static MUACore ourInstance = new MUACore();
    public static MUACore getInstance() {
        return ourInstance;
    }
    private int namespaceId = 1;

    private MUACore() {

    }
    public void MUACoreInit() {

    }
    static List<MUAObject> makeExprList(List<Token> tokenList) {
        List<MUAObject> result = new LinkedList<>();
        Deque<Token> tokens = new LinkedList<>(tokenList);
        while(!tokens.isEmpty()) {
            result.add(makeObject(tokens));
        }
    }

    static MUAObject makeObject(Deque<Token> tokens) {
        Token token = tokens.getFirst();
        if(token.type == Token.Type.WORD) {
            return new WordObject(token.val);
        } else if(token.type == Token.Type.NUMBER) {
            return new NumObject(token.val);
        } else if(token.type == Token.Type.OPERATION) {
            // TODO
        }
    }
}
