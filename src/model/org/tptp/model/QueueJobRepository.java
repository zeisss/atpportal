package org.tptp.model;

import java.util.*;

/**
 * The QueueJobRepository abstracts the backend storage for the {@link QueueJob} model class. Use the <code>getInstance()</code>
 * for getting an instance. Before using, you must set the backend implementation in the {@link RepositoryFactory}.
 *
 * @see QueueJob
 * @see RepositoryFactory
 */
public abstract class QueueJobRepository {
    private static QueueJobRepository instance = null;
    
    /**
     * Returns the instance or constructs it by calling <code>RepositoryFactory.createQueueJobRepository()</code>
     * 
     * @see RepositoryFactory
     * @see RepositoryFactory#createQueueJobRepository()
     * @throws IllegalStateException when the {@link RepositoryFactory} has not been initialized.
     */
    public static QueueJobRepository getInstance() {
        if ( null == instance) {
            return RepositoryFactory.getInstance().createQueueJobRepository();
        }
        return instance;
    }
    
    /**
     * Stores the given Formula as an input formula for the given QueueJob. For this
     * to success the status of the job must be STATUS_NEW.
     */
    public abstract void storeInputFormula(QueueJob job, Formula f);
    
    /**
     * Returns the next job with a status = QUEUED.
     * 
     * @see QueueJob
     * @see QueueJob#STATUS_QUEUED
     */
    public abstract QueueJob getNextQueueJob();
    
    /***
     * Returns the QueueJob with the given Id.
     */
    public abstract QueueJob get(long id);
    
    /**
     * Returns the QueueJob with a set proofId equal to the given id.
     */
    public abstract QueueJob getByProofId(long proofId);
    
    /**
     * Loads all QueueJob objects from the backend storage and returns them.
     */
    public abstract Set<QueueJob> getAll();
    
    /**
     * Fetches <code>length</code> items from the backend, starting at the <code>begin</code>th item (sorted by the age).
     *
     * @param A positive integer describing how many rows should be skipped (can be 0 to start at the first row)
     * @param A positive integer describing how many rows to return (can be 0 to say RETURN THE REST)
     */
    public abstract List<QueueJob> getRange(int begin, int length);
    
    /**
     * Creates a new QueueJob in the backend storage.
     * The id of this job must be -1.
     */
    public abstract void save(QueueJob job);
    
    /**
     * Updates an existing QueueJob in the backend storage.
     */
    public abstract void update(QueueJob job);
    
    /**
     * Removes an existing QueueJob in the backend storage.
     */
    public abstract void delete(QueueJob job);
}