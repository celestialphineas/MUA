package MUABackEnd.MUAObjects;

public class NumObject implements MUAObject {
    private double val = 0.;
    public NumObject(double val_)   { val = val_; }
    public NumObject(int val_)      { val = (double)val_; }
    public NumObject(boolean val_)  { val = val_ ? 1. : 0.; }
    public NumObject(String val_) throws NumberFormatException {
        val = Double.parseDouble(val_);
    }
    public double getVal() { return val; }
    @Override
    public boolean isAtomic() { return true; }
    @Override
    public String toString() { return Double.toString(val); }
    @Override
    public String typeName() { return "number"; }
}
