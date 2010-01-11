package org.tptp.model;

import java.util.*;

public class Atp {
    private long id = -1;
    private String name;
    private String atpName, atpVersion;
    
    private Map<String,String> values = new HashMap<String,String>();
    
    /**
     * @deprecated
     */
    public Atp(String name, String atpName, String atpVersion) {
        this(-1, name, atpName, atpVersion);
    }
    
    /**
     * Creates a new unvalidated ATP object. To check the atpName and atpVersion for validity, see {@link AtpFactory}.
     * This should only be used by the repository implementations, to recreate a saved object.
     *
     * @see AtpFactory
     */
    public Atp(long id, String name, String atpName, String atpVersion) {
        this.id = id;
        this.setName(name);
        this.atpName = atpName;
        this.atpVersion = atpVersion;
    }
    
    public String toString() {
        return "Atp [id=" + id + ";name=" + name + ";atpName=" + atpName + "-" + atpVersion + "]";
    }
    
    public boolean equals(Object object) {
        if ( object instanceof Atp) {
            Atp atp = (Atp)object;
            
            if (!(id == atp.id &&
                    (name == null ? atp.name == null : name.equals(atp.name)) &&
                    (atpName == null ? atp.atpName == null  : atpName.equals(atp.atpName)) &&
                    (atpVersion == null ? atp.atpVersion == null  : atpVersion.equals(atp.atpVersion))))
            {
                return false;
            }
            for ( String key : values.keySet() ) {
                if ( !atp.hasOption(key) )
                {
                    return false;
                }
            }
            
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        return (int)this.id;
    }
    
    public void setOption(String key, String value) {
        this.values.put(key, value);
    }
    
    public boolean hasOption(String key) {
        return this.values.containsKey(key);
    }
    
    public String getOption(String key) {
        return this.values.get(key);
    }
    
    public Map<String,String> getOptions() {
        return Collections.unmodifiableMap(this.values);
    }
    
    public long getId() { return this.id; }
    public String getName() { return this.name; }
    public String getAtpName() { return this.atpName; }
    public String getAtpVersion() { return this.atpVersion; }
    
    public void setId(long id) { this.id = id; }
    public void setName(String name) {
        if ( name == null || "".equals(name.trim())) {
            throw new ModelException("An empty name is not allowed in ATP.");
        }
        this.name = name;
    }
    public void setAtpName(String atpName) { this.atpName = atpName; }
    public void setAtpVersion(String atpVersion) { this.atpVersion = atpVersion; }
}