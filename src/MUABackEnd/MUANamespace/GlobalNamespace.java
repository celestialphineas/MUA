package MUABackEnd.MUANamespace;

import MUABackEnd.MUAObjects.MUAObject;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.util.HashMap;

public class GlobalNamespace extends Namespace {
    private static GlobalNamespace ourInstance = new GlobalNamespace();
    public static GlobalNamespace getInstance() {
        return ourInstance;
    }

    private GlobalNamespace() {
        name = "Global";
        hashMap = new HashMap<>();
    }

    @Override
    public void setName(String name_) { } // Do nothing
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setParent(Namespace parent_) { } // Do nothing
    @Override
    public Namespace getParent() { return ourInstance; }
    @Override
    public MUAObject find(String key) {
        MUAObject found = hashMap.get(key);
        if(found == null) {
            String[] splitted = key.split(".");
            if(splitted.length < 2) {
                MUAErrorMessage.error(ErrorStringResource.finding_variable,
                        ErrorStringResource.undefined_reference, key);
                return null;
            }
            // Find the namespace
            MUAObject foundNamespace = GlobalNamespace.getInstance();
            for(int i = 0; i < splitted.length - 1; i++) {
                foundNamespace = ((Namespace)foundNamespace).hashMap.get(splitted[i]);
                if(foundNamespace == null || !(foundNamespace instanceof Namespace)) {
                    MUAErrorMessage.error(ErrorStringResource.finding_namespace,
                            ErrorStringResource.undefined_namespace, splitted[0]);
                    return null;
                }
            }
            found = ((Namespace)foundNamespace).hashMap.get(splitted[splitted.length - 1]);
            if(found == null) {
                MUAErrorMessage.error(ErrorStringResource.finding_variable,
                        ErrorStringResource.undefined_reference, splitted[splitted.length - 1]);
                return null;
            }
        }
        return found;
    }

    @Override
    public void set(String key, MUAObject val) {
        if(val != null) {
            hashMap.put(key, val);
        }
    }
}
