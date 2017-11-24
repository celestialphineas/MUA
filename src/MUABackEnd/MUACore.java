package MUABackEnd;

import MUABackEnd.MUAObjects.ExprListObject;
import MUAFrontEnd.Token;
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
    ExprListObject makeExprList(List<Token> tokenList) {

    }
}
