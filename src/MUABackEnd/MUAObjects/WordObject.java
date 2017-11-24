package MUABackEnd.MUAObjects;

public class WordObject implements MUAObject {
    private String val = new String();
    public WordObject(double val_)  { val = Double.toString(val_); }
    public WordObject(int val_)     { val = Integer.toString(val_); }
    public WordObject(boolean val_) { val = val_ ? "true" : "false"; }
    public WordObject(String val_)  { val = val_; }
    public String getVal() { return val; }
    @Override
    public boolean isAtomic() { return true; }
    @Override
    public String typeName()  { return "word"; }
    @Override
    public String toString()  { return val; }
}
