package org.tptp.model;

import java.util.*;

public abstract class FormulaRepository {
    private static FormulaRepository instance = null;
    public static FormulaRepository getInstance() {
        if ( null == instance) {
            return RepositoryFactory.getInstance().createFormulaRepository();
        }
        return instance;
    }
    
    public abstract Set<Formula> getByFilter(long algebraId, String filter, boolean axioms, boolean theorems);
    
    public abstract Set<Formula> getAll();
    
    /**
     * Returns all formula entries (theorems and axioms) for the given algebra.
     */
    public abstract Set<Formula> getByAlgebra(Algebra algebra);
    
    public abstract Set<Formula> getAxiomsByAlgebra(Algebra algebra);
    
    public abstract Set<Formula> getTheoremsByAlgebra(Algebra algebra);
    
    public Set<Formula> getUsedFormulasForProof(Proof proof) {
        if ( proof == null ) {
            throw new IllegalArgumentException("Proof is null");
        }
        return getUsedFormulasForProof(proof.getId());
    }
    
    /**
     * Returns all Formulas which are used in the given proof.
     *
     * @see Proof
     */
    public abstract Set<Formula> getUsedFormulasForProof(long proofId);
    
    public Set<Formula> getProvedFormulasByProof(Proof proof) {
        if ( proof == null ) {
            throw new IllegalArgumentException("Proof is null");
        }
        return getProvedFormulasByProof(proof.getId());
    }
    
    /**
     * Returns all Formulas that the given proof proves.
     *
     * @see Proof
     */
    public abstract Set<Formula> getProvedFormulasByProof(long proofId);
    
    /**
     * Returns all Formulas that were added to the given {@link QueueJob}.
     *
     * @see QueueJob
     */
    public abstract Set<Formula> getFormulasForQueueJob(long queueJobId);
    
    
    /**
     * Loads the first found formula with the same formulaText as the given text.
     */
    public abstract Formula getByFormulaText(String formulaText);
    
    /**
     * Loads the formula entry from the backend for the given id.
     *
     * id == get(id).getId()
     */
    public abstract Formula get(long id);
    
    /**
     * Creates a new entry for the given formula in the backend.
     * The internal id of the formula might get updated.
     *
     * before: f.getId() == -1
     * save(f)
     * after: f.getId() > 0
     */
    public abstract void save(Formula f);
    
    /**
     * Updates an entry in the backend.
     * before: f.getId() > 0
     */
    public abstract void update(Formula f);
    
    /**
     * Deletes the given entry in the backend.
     *
     * before: f.getId() > 0
     * after: f.getId() == -1
     */
    public abstract void delete(Formula f);
}