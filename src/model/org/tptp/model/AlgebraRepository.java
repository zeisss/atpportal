package org.tptp.model;

import java.util.*;

public abstract class AlgebraRepository {
    private static AlgebraRepository instance = null;
    public static AlgebraRepository getInstance() {
        if ( null == instance) {
            instance = RepositoryFactory.getInstance().createAlgebraRepository();
        }
        return instance;
    }
    
    /**
     * Creates a new link between the given algebra and the given formula.
     * If axiom is true, the formula is afterwards an axiom for the given algebra.
     *
     * If a link previously existed between the algebra and the formula it is overwritten.
     */
    public abstract void link(Algebra algebra, Formula formula, boolean axiom);
    
    public abstract void unlink(Algebra algebra, Formula formula);
    
    
    public abstract Algebra get(long i);
    public abstract Set<Algebra> getAll();
    
    /**
     * @see #getAlgebrenForAxiom(long)
     */
    public Set<Algebra> getAlgebrenForAxiom(Formula f) {
        if ( f == null || f.getId() < 0 ) {
            throw new IllegalArgumentException("Formula f is null or unsaved.");
        }
        return getAlgebrenForAxiom(f.getId());
    }
    
    /**
     * Returns a set of algebren where a formula identified by the given formula id
     * is an axiom.
     */
    public abstract Set<Algebra> getAlgebrenForAxiom(long formulaId);
    
    /**
     * @see #getAlgebrenForTheorem(long)
     */
    public Set<Algebra> getAlgebrenForTheorem(Formula f) {
        if ( f == null || f.getId() < 0 ) {
            throw new IllegalArgumentException("Formula f is null or unsaved.");
        }
        return getAlgebrenForTheorem(f.getId());
    }
    
    /**
     * Returns a set of algebren where a formula identified by the given formula id
     * is a theorem.
     */
    public abstract Set<Algebra> getAlgebrenForTheorem(long formulaId);
    
    public abstract void save(Algebra algebra);
    public abstract void update(Algebra algebra);
    public abstract void delete(Algebra algebra);
}