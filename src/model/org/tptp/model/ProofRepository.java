package org.tptp.model;

import java.util.*;

public abstract class ProofRepository {
    private static ProofRepository instance = null;
    public static ProofRepository getInstance() {
        if ( null == instance) {
            return RepositoryFactory.getInstance().createProofRepository();
        }
        return instance;
    }
    
    public ProofRepository() {}
    
    public void storeUsage(Proof p, Formula f) {
        if ( p == null ) {
            throw new IllegalArgumentException("Proof is null");
        }
        if ( f == null ) {
            throw new IllegalArgumentException("Formula is nulL");
        }
        storeUsage(p.getId(), f.getId());
    }
    
    public void storeProve(Proof p, Formula f) {
        if ( p == null ) {
            throw new IllegalArgumentException("Proof is null");
        }
        if ( f == null ) {
            throw new IllegalArgumentException("Formula is nulL");
        }
        storeProve(p.getId(), f.getId());
    }
    
    public abstract void storeUsage(long proofId, long formulaId);
    
    public abstract void storeProve(long proofId, long formulaId);
    
    public Set<Proof> getProofsForFormula(Formula f) {
        if ( f == null ) {
            throw new IllegalArgumentException("Forumla is null");
        }
        return getProofsForFormula(f.getId());
    }
    
    public abstract Set<Proof> getProofsForFormula(long formulaId);
    
    
    public Set<Proof> getProofsWhereFormulaUsed(Formula f) {
        if ( f == null ) {
            throw new IllegalArgumentException("Forumla is null");
        }
        return getProofsWhereFormulaUsed(f.getId());
    }
    public abstract Set<Proof> getProofsWhereFormulaUsed(long formulaId);
    
    public abstract Set<Proof> getAll();
    public abstract Proof get(long id);
    
    public abstract void save(Proof proof);
    public abstract void update(Proof proof);
    public abstract void delete(Proof proof);
    
}