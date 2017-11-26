package MUABackEnd.MUANamespace;

import MUABackEnd.MUAObjects.MUAObject;

import java.util.HashMap;
import java.util.Map;

public class Namespace implements MUAObject {
    protected String name;
    protected Namespace parent;
    protected Map<String, MUAObject> hashMap;

    public Namespace(String name_, Namespace parent_) {
        name = name_; parent = parent_;
        hashMap = new HashMap<>();
    }
    public Namespace clone(String name_) {
        Namespace result = new Namespace(name_, parent);
        result.hashMap = new HashMap<>(this.hashMap);
        return result;
    }
    protected Namespace() {}

    public void setName(String name_)           { name = name_; }
    public String getName()                     { return name; }
    public void setParent(Namespace parent_)    { parent = parent_; }
    public Namespace getParent()                { return parent; }

    public MUAObject find(String key) {
        MUAObject found = hashMap.get(key);
        if(found == null) {
            String[] splitted = key.split(".");
            if(splitted.length < 2) {
                return parent.find(key);
            }
            // Find the namespace
            MUAObject foundNamespace = GlobalNamespace.getInstance();
            for(int i = 0; i < splitted.length - 1; i++) {
                foundNamespace = ((Namespace)foundNamespace).hashMap.get(splitted[i]);
                if(foundNamespace == null || !(foundNamespace instanceof Namespace)) {
                    return GlobalNamespace.getInstance().find(key);
                }
            }
            found = ((Namespace)foundNamespace).hashMap.get(splitted[splitted.length - 1]);
            if(found == null) {
                return GlobalNamespace.getInstance().find(key);
            }
        }
        return found;
    }

    public void set(String key, MUAObject val) {
        if(key != null && val != null) {
            hashMap.put(key, val);
        }
    }

    @Override
    public boolean isAtomic()   { return false; }
    @Override
    public String typeName()    { return "namespace"; }
}
