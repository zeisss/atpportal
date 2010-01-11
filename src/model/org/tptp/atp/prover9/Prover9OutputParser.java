package org.tptp.atp.prover9;

import java.util.*;
import org.tptp.model.*;

/**
 * This method parses the given output and creates Formula objects for the used and proves
 * formulas in the output. Theses formulas are then added to the given sets.
 * This method is called during the execute() steps.
 */
public class Prover9OutputParser {
    private final String BEGIN_PROOF = "============================== PROOF =================================";
    private final String END_PROOF = "============================== end of proof ==========================";
    
    private final String BEGIN_STATISTICS = "============================== STATISTICS ============================";
    private final String END_STATISTICS = "============================== end of statistics =====================";
    
    public Prover9OutputParser() {
        
    }
    
    /**
     * Parses the output of a prover9 call and fill the given variables with the result.
     */
    public void parse(String output, Set<Formula> used, Set<Formula> proved, Set<ProofStep> steps, Map<String,String> details) {
        int i = output.indexOf(BEGIN_PROOF) + BEGIN_PROOF.length();
        int i2 = output.indexOf(END_PROOF, i);
        if ( i != -1 && i2 != -1) {
            parseProof(output.substring(i, i2), used, proved, steps);    
        }
        
        i = output.indexOf(BEGIN_STATISTICS) + BEGIN_STATISTICS.length();
        i2 = output.indexOf(END_STATISTICS);
        if ( i != -1 && i2 != -1) {
            parseStatistics(output.substring(i, i2), details);   
        }
        
    }
    
    /**
     * Parses the Statistics block of the prover9 output
     */
    public void parseStatistics(String statisticsBlock, Map<String,String> details) {
        String [] lines = statisticsBlock.split("\n");
        for ( String line : lines ) {
            if ( line.trim().equals("") )
                continue;
            
            // Each line is a list of key=value pairs separated by a dot or a comma.
            // So we first split by dot and then by comma
            
            String [] byDot = line.split("\\.\\s");
            for ( String a : byDot) {
                
                // If there are dots at the end of the line, remove them
                if ( a.trim().endsWith("."))
                    a = a.trim().substring(0, a.trim().length()-1);
                    
                // Now split by comma
                String [] byComma = a.split(",");
                for ( String b : byComma ) {
                    // b now looks like key=value
                    
                    int i = b.indexOf("=");
                    String key = b.substring(0,i).trim();
                    String value = b.substring(i+1).trim();
                    
                    details.put(key, value);
                }
            }
            
        }
    }
    
    /**
     * This parses the proof block of a prover9 output.
     */
    public void parseProof(String proofBlock,Set<Formula> used, Set<Formula> proved, Set<ProofStep> steps) {
        String [] lines = proofBlock.split("\n");
        
        for (String line : lines) {
            // Skip the comments and empty lines
            if ( line.trim().equals("") || line.charAt(0) == '%')
                continue;
            int lineNumber;
            String formula, reasoning;
            
            // Split of the line number
            int i = line.indexOf(" ");
            lineNumber = Integer.valueOf(line.substring(0, i));
            line = line.substring(i+1);
            
            // The reasoning is separated from the formula with a dot
            i = line.indexOf(".");
            reasoning = line.substring(i+1).trim();
            if ( reasoning.endsWith(".")) {
                reasoning = reasoning.substring(0, reasoning.length()-1);
            } else {
                System.err.println("ERROR(Prover9OutputParser): Reasoning does not end with a dot. I'm totally wrong here.");
            }
            
            line = line.substring(0, i);
            
            // Cut of the name / labels
            i = line.indexOf("#");
            if ( i >= 0 ) {
                line = line.substring(0, i).trim();
            }
            formula = line;
            
            // Add the parsed line to the proof-steps
            ProofStep step = new ProofStep(lineNumber, formula, reasoning);
            steps.add(step);
            
            // Lines marked with assumption are our input lines, we already know they are proved
            if ( reasoning.equals("[assumption]"))
            {
                Formula f = new Formula("", formula);
                used.add(f);
            } else if ( reasoning.equals("[goal]"))
            {
                Formula f = new Formula("", formula);
                proved.add(f);
            }
            
        }
        
    }
}