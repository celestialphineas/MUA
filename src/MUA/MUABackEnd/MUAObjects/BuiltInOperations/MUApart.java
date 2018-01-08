package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.util.List;

public class MUApart extends BuiltInOperation {
    public MUApart() {
        name = "part";
        argc = 2;
    }
    @Override
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr_.objectList.get(1);
        MUAObject obj2 = expr_.objectList.get(2);
        // Eval the first argument
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject)obj1).evalExpr();
            obj1 = ((ExprListObject)obj1).getReturnVal();
        }
        // Eval the second argument
        if(obj2 instanceof ExprListObject) {
            ((ExprListObject)obj2).evalExpr();
            obj2 = ((ExprListObject)obj2).getReturnVal();
        }
        MUAObject result = new ExprListObject(expr_);
        ((ExprListObject)result).objectList.clear();
        // obj2 is not an expr list
        if(obj2 instanceof ExprListObject) {
            if(!(((ExprListObject) obj2).objectList.get(0).toString().equals("list"))) {
                MUAErrorMessage.warn(ErrorStringResource.operation_part,
                        ErrorStringResource.incompatible_type,
                        "Part of an expression with a head other than \"list\" may cause undefined behavior.");
            }
            // Add head to the list
            ((ExprListObject)result).objectList.add(((ExprListObject) obj2).objectList.get(0));
            // Handle the second expression list
            List<MUAObject> list2 = ((ExprListObject) obj2).objectList;
            if(obj1 instanceof NumObject) {
                if(((int)((NumObject) obj1).getVal()) <= 0 || ((int)((NumObject) obj1).getVal()) >= list2.size()) {
                    MUAErrorMessage.error(ErrorStringResource.operation_part,
                            ErrorStringResource.index_out_of_bound, "" + ((NumObject) obj1).getVal());
                    throw new MUARuntimeException();
                }
                return list2.get((int)(((NumObject) obj1).getVal()));
            } else if (obj1 instanceof ExprListObject) {
                int lower, upper;
                try {
                    lower = (int)((NumObject)((ExprListObject) obj1).objectList.get(1)).getVal();
                    upper = (int)((NumObject)((ExprListObject) obj1).objectList.get(2)).getVal();
                    if(lower > upper || lower <= 0 || upper >= list2.size()) {
                        MUAErrorMessage.error(ErrorStringResource.operation_part,
                                ErrorStringResource.index_out_of_bound, "" + lower + ", " + upper);
                        throw new MUARuntimeException();
                    }
                    ((ExprListObject)result).objectList.addAll(list2.subList(lower, upper + 1));
                } catch (MUARuntimeException ignored) {}
                catch (Exception e) {
                    MUAErrorMessage.error(ErrorStringResource.operation_part,
                            ErrorStringResource.incompatible_type, "Argument 1 expects a number or list [lower upper]");
                    throw new MUARuntimeException();
                }
            } else {
                MUAErrorMessage.error(ErrorStringResource.operation_part,
                        ErrorStringResource.incompatible_type, "Argument counts don't match");
                throw new MUARuntimeException();
            }
        } else if(obj2 instanceof WordObject) {
            // Handle the second string
            String str2 = ((WordObject) obj2).getVal();
            if(obj1 instanceof NumObject) {
                int index = ((int)((NumObject) obj1).getVal());
                if(index <= 0 || index > str2.length()) {
                    MUAErrorMessage.error(ErrorStringResource.operation_part,
                            ErrorStringResource.index_out_of_bound, "" + ((NumObject) obj1).getVal());
                    throw new MUARuntimeException();
                }
                result = new WordObject(Character.toString(str2.charAt(index - 1)));
            } else if (obj1 instanceof ExprListObject) {
                int lower, upper;
                try {
                    lower = (int)((NumObject)((ExprListObject) obj1).objectList.get(1)).getVal();
                    upper = (int)((NumObject)((ExprListObject) obj1).objectList.get(2)).getVal();
                    if(lower > upper || lower <= 0 || upper > str2.length()) {
                        MUAErrorMessage.error(ErrorStringResource.operation_part,
                                ErrorStringResource.index_out_of_bound, "" + lower + ", " + upper);
                        throw new MUARuntimeException();
                    }
                    result = new WordObject(str2.substring(lower - 1, upper));
                } catch (MUARuntimeException ignored) {}
                catch (Exception e) {
                    MUAErrorMessage.error(ErrorStringResource.operation_part,
                            ErrorStringResource.incompatible_type, "Argument 1 expects a number or list [lower upper]");
                    throw new MUARuntimeException();
                }
            } else {
                MUAErrorMessage.error(ErrorStringResource.operation_part,
                        ErrorStringResource.incompatible_type, "Argument counts don't match");
                throw new MUARuntimeException();
            }
        } else {
            MUAErrorMessage.error(ErrorStringResource.operation_part,
                    ErrorStringResource.incompatible_type, "Part expect the second argument to be an expression list.");
            throw new MUARuntimeException();
        }

        return result;
    }
}
