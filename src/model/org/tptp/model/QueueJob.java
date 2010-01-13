package org.tptp.model;

import java.util.*;

public class QueueJob {
    public static final int STATUS_NEW = 0;       // Its just new, ignore it
    public static final int STATUS_QUEUED = 1;    // Hey, work on this, please!
    public static final int STATUS_LOCKED = 2;    // A worker is currently looking at this job
    public static final int STATUS_PROCESSED = 3; // The worker is done (prove_id should be filled)
    public static final int STATUS_ERROR = 4;     // Ooops, something went wrong!
    
    private static final int MAX_STATUS = STATUS_ERROR;
    
    
    private long id;
    
    private long accountId; // Who created this?
    private long atpId; // Which atp?
    private long proofId; // Whats the result?
    
    private int status;
    private Date createdAt;
    private String goalFormula;
    
    private String message;
    
    /**
     * This is the input and the output as generated by the TheoremProver.
     * Intended for manual debuging by a human mind only.
     */
    private String inputText, outputText; 
    
    public QueueJob(String goalFormula, Atp atp) {
        this(goalFormula, atp, null);
    }
    public QueueJob(String goalFormula, Atp atp, Account owner) {
        this(
            -1,
            goalFormula,
            atp == null ? -1 : atp.getId(),
            owner == null ? -1 : owner.getId(),
            -1,
            STATUS_NEW,
            new Date(),
            null,
            null,
            null
        );
    }
    
    public QueueJob(long id, String goalFormula, long atpId, long accountId, long proofId,  int status, Date createdAt, String message, String inputText, String outputText) {
        this.id = id;
        this.goalFormula = goalFormula;
        this.atpId = atpId;
        this.accountId = accountId;
        this.proofId = proofId;
        this.createdAt = createdAt;
        this.status = status;
        this.message = message;
        this.inputText = inputText;
        this.outputText = outputText;
    }
    
    public void setStatus(int newStatus) {
        if ( newStatus < 0 || newStatus > MAX_STATUS ) return;
        status = newStatus;
    }
    
    public String toString() {
        return "QueueJob [id=" + id + ";goalFormula=" + goalFormula + ";atp-id=" + atpId + ";accountId=" + accountId + ";proofId=" + proofId + ";createdAt=" + createdAt + ";status=" + status + "]";
    }
    
    public int hashCode() {
        return (int) id;
    }
    
    public boolean equals(Object job) {
        if ( job instanceof QueueJob ) {
            QueueJob obj = (QueueJob) job;
            
            return  (obj.id == id) &&
                    (goalFormula == null ?  obj.goalFormula == null : goalFormula.equals(obj.goalFormula)) &&
                    (atpId == obj.atpId) &&
                    (accountId == obj.accountId) &&
                    (proofId == obj.proofId) &&
                    (createdAt == null ? obj.createdAt == null : createdAt.equals(obj.createdAt)) &&
                    (status == obj.status) &&
                    (message == null ? obj.message == null : message.equals(obj.message)) &&
                    (inputText == null ? obj.inputText == null : inputText.equals(obj.inputText)) &&
                    (outputText == null ? obj.outputText == null : outputText.equals(obj.outputText));
        }
        return false;
    }
    
    public Account getAccount() {
        if ( this.accountId == -1 ) {
            return null;
        }
        return AccountRepository.getInstance().get(this.accountId);
    }
    
    public void setId(long id) { this.id = id; }
    public void setGoalFormula(String s) { this.goalFormula = s; }
    public void setAtpId(long atpId) { this.atpId = atpId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }
    public void setProofId(long proofId) { this.proofId = proofId; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setMessage(String message) { this.message = message; }
    // vor setStatus() see above
    public void setInputText(String s) { this.inputText = s; }
    public void setOutputText(String s) { this.outputText = s; }
    
    public long getId() { return this.id; }
    public String getGoalFormula() { return this.goalFormula; }
    public long getAtpId() { return this.atpId; }
    public long getAccountId() { return this.accountId; }
    public long getProofId() { return this.proofId; }
    public Date getCreatedAt() { return this.createdAt; }
    public int getStatus() { return this.status; }
    public String getMessage() { return this.message; }
    
    
    public String getInputText() { return this.inputText; }
    public String getOutputText() { return this.outputText; }
}