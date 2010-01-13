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
    
    private String inputText, outputText;
    
    public Result(boolean successfull, Exception exception, TheoremProver prover, String inputText, String outputText) {
        if ( successfull ) {
            throw new IllegalStateException("Cannot create a sucessfull Result with an errormessage.");
        }
        if ( exception == null ) {
            throw new IllegalArgumentException("Exception must be provided.");
        }
        this.successfull = successfull;
        this.exception = exception;
        this.prover = prover;
        this.inputText = inputText;
        this.outputText = outputText;
    }
    
    /**
     * Creates a successfull Result Object.
     * 
     * @param successfull Must be true
     * @param used
     * @param proved
     * @param proofSteps
     * @param details
     * @param inputText A text helping the enduser to identify what was used when invoking the threoremprover.
     * @param outputText A text helping the enduser to identify what was the result of the invocation. Might be a log or the output.
     */
    public Result(boolean successfull, Set<Formula> used, Set<Formula> proved, Set<ProofStep> proofSteps, Map<String,String> details, TheoremProver prover, String inputText, String outputText) {
        if ( !successfull ) {
            throw new IllegalStateException("Cannot create an unsuccessfull Result without an errormessage.");
        }
        this.successfull = successfull;
        this.proofSteps = proofSteps;
        this.used = used;
        this.proved = proved;
        this.details = details;
        this.prover = prover;
        this.inputText = inputText;
        this.outputText = outputText;
    }
    
    public boolean isSuccessfull() { return successfull; }
    public Exception getException() { return exception; }
    
    public Set<Formula> getUsedFormulas() { return used; }
    public Set<Formula> getProvedFormulas() { return proved; }
    public Set<ProofStep> getProofSteps() { return proofSteps; }
    public Map<String,String> getDetails() { return details; }
    public TheoremProver getTheoremProver() { return prover; }
    
    public String getInputText() { return inputText; }
    public String getOutputText() { return outputText; }
}