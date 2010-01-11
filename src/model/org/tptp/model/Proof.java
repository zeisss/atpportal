package org.tptp.model;

import java.util.*;

/**
 * A proof is the result of a {@link QueueJob} and links several formulas and other objects. A proof consists of
 * a list of {@link ProofStep}s showing the transformations used for this proof. All resulting formulas are linked
 * and can be fetched by {@link FormulaRepository#getFormulasProvedBy(long)}. For a list of all used formulas for the
 * transformations see {@link FormulaRepository#getFormulasUsedInProof(long)}. A table of Details is accessible with the
 * {@link #getDetail(String)} methods.
 *
 * @see ProofRepository
 * @see QueueJobRepository#getByProofId(long)
 */
public class Proof {
    private long id = -1;
    private Date time;
    
    private SortedSet<ProofStep> proofSteps = new TreeSet<ProofStep>(); // TODO: Add a comparator to sort by line?
    private Map<String,String> details = new HashMap<String,String>();
    
    public Proof () {
        this(new Date());
    }
    
    public Proof(Date d) {
        this(-1, d);
    }
    
    public Proof (long id, Date time) {
        this.id = id;
        this.time = time;
    }
    
    /**
     * Returns null if this proof is unsaved and no queuejob is associated with this object in the backend.
     *
     * @see QueueJobRepository#getByProofId(long)
     */
    public QueueJob getQueueJob() {
        return this.id == -1 ? null : QueueJobRepository.getInstance().getByProofId(this.id);
    }
    
    
    
    
    
    
    public Set<String> getDetailKeys() {
        return this.details.keySet();
    }
    
    public Map<String,String> getDetails() {
        return Collections.unmodifiableMap(this.details);
    }
    
    public void setDetail(String key, String value) {
        details.put(key, value);
    }
    
    public String getDetail(String key) {
        return details.get(key);
    }
    
    
    
    /**
     * Returns an unmodifiable version of the internal SortedSet containing the {@link ProofStep} objects.
     */
    public SortedSet<ProofStep> getProofSteps() {
        return Collections.unmodifiableSortedSet(this.proofSteps);
    }
    
    public void addProofStep(ProofStep step) {
        this.proofSteps.add(step);
    }
    
    
    
    
    
    public String toString() {
        return "Proof [id=" + id + ";time="+time +"]";
    }
    
    public int hashCode() {
        return (int) id;
    }
    
    public boolean equals(Object object) {
        if ( object instanceof Proof) {
            Proof p = (Proof)object;
            
            if (!( (id == p.id) &&
                 (time == null ? p.time == null : time.equals(p.time)) )) {
                return false;
            }
            for (String key : getDetailKeys()) {
                if ( !p.details.containsKey(key)) {
                    return false;
                }
            }
            for ( ProofStep ps : proofSteps) {
                if (!(p.proofSteps.contains(ps))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public long getId() { return this.id; }
    public Date getTime() { return this.time; }
    
    public void setId(long id) { this.id = id;}
    public void setTime(Date t) { this.time = t; }
}