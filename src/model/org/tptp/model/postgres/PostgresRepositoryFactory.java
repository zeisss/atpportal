package org.tptp.model.postgres;

import org.tptp.model.*;
import org.tptp.model.jdbc.ConnectionFactory;

/**
 * The RepositoryFactory implementation for the org.tptp.model.postgres package.
 *
 * @see RepositoryFactory
 * @see ConnectionFactory
 */
public class PostgresRepositoryFactory extends RepositoryFactory {
    private ConnectionFactory factory;
    
    /**
     * Creates a new PostgresRepositoryFactory with the given ConnectionFactory.
     * The ConnectionFactory is used to provide the repositories with a {java.sql.Connection}.
     *
     * @see ConnectionFactory
     */
    public PostgresRepositoryFactory (ConnectionFactory factory) {
        this.factory = factory;
    }
    
    public AccountRepository createAccountRepository() {
        return new PostgresAccountRepository(factory);
    }
    public AlgebraRepository createAlgebraRepository() {
        return new PostgresAlgebraRepository(factory);
    }
    public AtpRepository createAtpRepository() {
        return new PostgresAtpRepository(factory);
    }
    public FormulaRepository createFormulaRepository() {
        return new PostgresFormulaRepository(factory);
    }
    public ProofRepository createProofRepository() {
        return new PostgresProofRepository(factory);
    }
    public QueueJobRepository createQueueJobRepository() {
        return new PostgresQueueJobRepository(factory);
    }
    
    public OperatorRepository createOperatorRepository() {
        return new PostgresOperatorRepository(factory);
    }
    
    public Transaction createTransaction() {
        return this.factory;
    }
}