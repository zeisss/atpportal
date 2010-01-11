package org.tptp.model;

import org.tptp.atp.*;
import java.util.*;

/**
 * This class provides some additional logic for creating a initially valid {@link Atp} object. Since an atp object
 * referes to a {@link TheoremProver} by its name and the version the prover might not always be available
 * during the lifetime of an atp object. Thus the atp object itself does not provide the logic for checking
 * if such a prover exists.
 * 
 * @see Atp
 * @see TheoremProver
 */
public class AtpFactory {
    /**
     * Creates a new {@link Atp} object and check if the references {@link TheoremProver} is
     * currently available.
     *
     * @throws ModelException if the TheoremProver is not available.
     */
    public static Atp createAtp(String name, String atpName, String atpVersion) {
        TheoremProver tp = TheoremProver.findByNameAndVersion(atpName, atpVersion);
        if ( tp == null ) {
            throw new ModelException("Referenced TheoremProver \"" + atpName + "\" - \"" + atpVersion + "\" is not available.");
        }
        
        return new Atp(name, atpName, atpVersion);
    }
    
    /**
     * This is the same as createAtp(String,String,String) PLUS the checking for all required options in the map.
     * The map is then stored in the {@link Atp} with the <code>setOption(String,String)</code> methods.
     *
     * @see #createAtp(String,String,String)
     */
    public static Atp createAtp(String name, String atpName, String atpVersion, Map<String,String> options) {
        Atp atp = createAtp(name, atpName, atpVersion);
        TheoremProver tp = TheoremProver.findByNameAndVersion(atpName, atpVersion);
        Map<String,String> required = tp.getOptionDescriptionMap();
        
        // Check if all required keys are set and non-empty
        for ( String key : required.keySet()) {
            if ( !options.containsKey(key) || "".equals(options.get(key))) {
                throw new ModelException("Options do not contain key " + key + " or its empty.");
            }
        }
        
        // We set the options in an extra loop, as they may contain additional keys, like the optionals
        for ( String key : options.keySet()) {
            atp.setOption(key, options.get(key));
        }
        
        return atp;
    }
}