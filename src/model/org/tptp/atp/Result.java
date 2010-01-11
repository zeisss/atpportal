package org.tptp.atp;

import org.tptp.model.*;
import java.util.*;

/**
 * This represents the result of a {@link TheoremProver} execution.
 *
 * @see TheoremProver.execute(java.util.Properties, org.tptp.model.Formula, java.util.Set)
 */
public class Result {
    private boolean successfull;
    
    private Set<Formula> used = null, proved = null;
    private Set<ProofStep> proofSteps = null;
    private Map<String,String> details = null;
    private Exception exception = null;
    private TheoremProver prover;
    
    public Result(boolean successfull, Exception exception, TheoremProver prover) {
        if ( successfull ) {
            throw new IllegalStateException("Cannot create a sucessfull Result with an errormessage.");
        }
        this.successfull = successfull;
        this.exception = exception;
        this.prover = prover;
    }
    public Result(boolean successfull, Set<Formula> used, Set<Formula> proved, Set<ProofStep> proofSteps, Map<String,String> details, TheoremProver prover) {
        if ( !successfull ) {
            throw new IllegalStateException("Cannot create an unsuccessfull Result without an errormessage.");
        }
        this.successfull = successfull;
        this.proofSteps = proofSteps;
        this.used = used;
        this.proved = proved;
        this.details = details;
        this.prover = prover;
    }
    
    public boolean isSuccessfull() { return successfull; }
    public Exception getException() { return exception; }
    
    public Set<Formula> getUsedFormulas() { return used; }
    public Set<Formula> getProvedFormulas() { return proved; }
    public Set<ProofStep> getProofSteps() { return proofSteps; }
    public Map<String,String> getDetails() { return details; }
    public TheoremProver getTheoremProver() { return prover; }
}