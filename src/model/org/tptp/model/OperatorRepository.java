package org.tptp.model;

import java.util.*;

public abstract class OperatorRepository {
    private static OperatorRepository instance = null;
    public static OperatorRepository getInstance() {
        if ( null == instance) {
            return RepositoryFactory.getInstance().createOperatorRepository();
        }
        return instance;
    }
 
    
    /**
     * Returns a {@link Set} of Operators where the name is equal to the given name.
     */
    public abstract Set<Operator> getOperatorsByName(String name);
} 