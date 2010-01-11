package org.tptp.model;

import java.util.*;

public abstract class AtpRepository {
    private static AtpRepository instance = null;
    public static AtpRepository getInstance() {
        if ( null == instance) {
            return RepositoryFactory.getInstance().createAtpRepository();
        }
        return instance;
    }
    
    
    public abstract Atp get(long id);
    
    /**
     * Returns a list containing all saved {@link Atp}s sorted by their ID. The newest first.
     */
    public abstract List<Atp> getAll();
    
    public abstract void save(Atp atp);
    public abstract void update(Atp atp);
    public abstract void delete(Atp atp);
}