package MUABackEnd.MUAObjects;

public class DumbHeadObject implements MUAObject {
    private String val = new String();
    public DumbHeadObject(double val_)  { val = Double.toString(val_); }
    public DumbHeadObject(int val_)     { val = Integer.toString(val_); }
    public DumbHeadObject(boolean val_) { val = val_ ? "true" : "false"; }
    public DumbHeadObject(String val_)  { val = val_; }
    public String getVal() { return val; }
    @Override public boolean isAtomic() { return true; }
    @Override public String typeName()  { return "dumbhead"; }
    @Override public String toString()  { return val; }
    @Override public String toMUAExprString() { return toString(); }
}
