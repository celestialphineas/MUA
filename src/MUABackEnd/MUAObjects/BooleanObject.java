package MUABackEnd.MUAObjects;

public class BooleanObject implements MUAObject{
    private boolean val = false;
    private final double falseErr = 1e-5;
    public BooleanObject(double val_)   { val = val_ < falseErr && val_ > -falseErr; }
    public BooleanObject(int val_)      { val = val_ != 0; }
    public BooleanObject(boolean val_)  { val = val_; }
    public BooleanObject(String val_)   { val = val_.toLowerCase().equals("true"); }
    public boolean getVal() { return val; }
    @Override public boolean isAtomic() { return true; }
    @Override public String typeName()  { return "boolean"; }
    @Override public String toString()  { return val ? "true" : "false"; }
}
