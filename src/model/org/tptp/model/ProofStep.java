package org.tptp.model;

public class ProofStep implements Comparable {
    public long line;
    public String formula;
    private String reasoning;
    
    ProofStep() {
        this(-1, null, null);    
    }
    
    public ProofStep(long line, String formula, String reasoning) {
        this.line = line;
        this.formula = formula;
        this.reasoning = reasoning;
    }
    
    public int compareTo(Object obj) {
        if ( obj instanceof ProofStep ) {
            ProofStep ps = (ProofStep) obj;
            return (int)(line - ps.line);
        }
        return -1;
    }
    public String toString() {
        return "ProofStep [line=" + line + "; formula=" + formula + "; reasoning=" + reasoning + "]";
    }
    
    public boolean equals(Object obj) {
        if ( obj instanceof ProofStep) {
            ProofStep a = (ProofStep) obj;
            return (line == a.line) &&
                    (formula == null ? a.formula == null : formula.equals(a.formula)) &&
                    (reasoning == null ? a.reasoning == null : reasoning.equals(a.reasoning));
        }
        return false;
    }
    public int hashCode() {
        return ((int)line) + (formula == null ? -1 : formula.hashCode());
    }
    
    public long getLine() { return line; }
    public String getFormula() { return formula; }
    public String getReasoning() { return reasoning; }
    
    public void setLine(long line) { this.line = line; }
    public void setFormula(String formula) { this.formula = formula; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
}