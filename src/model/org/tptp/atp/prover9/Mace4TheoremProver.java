package org.tptp.atp.prover9;

public class Mace4TheoremProver extends Prover9TheoremProver {
    public Mace4TheoremProver() {
        DEFAULT_BINARY = "mace4";
    }
    public String getName() { return "Mace4"; }
    public boolean isProver() { return false;}
    public String getDescription() {
        return "The interface for using the Mace4 prover.";
    }
}