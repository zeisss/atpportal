package org.tptp.model;

/**
 * The RepositoryFactory is the link for the repositories to the backend. Each Repository is a singeltone
 * asking this class (also a singleton) for an implementation when the repository is not yet initalized.
 *
 * This way, the data storage of the model can be abstracted from the model logic. Currently, there is only
 * a postgres backend implementation of the model available. An example how to use it:
 * <pre><code>
 *      Properties prop = new Properties();
 *      prop.setProperty("jdbc.string", "jdbc:....");
 *      prop.setProperty("jdbc.class", "org.postgres.Driver");
 *      prop.setProperty("jdbc.username", "postgres");
 *      prop.setProperty("jdbc.password", "postgres");
 *      
 *      ConnectionFactory factory = new DefaultConnectionFactory(prop);
 *      RepositoryFactory.setInstance(new PostgresRepositoryFactory(factory));
 *
 *      // Now we can use the repositories
 *      AccountRepository repo = AccountRepository.getInstance();
 *
 * </code></pre>
 * <p>
 *   NOTE: After the initialization this class shouldn't be used any longer by the frontend developer/code.
 * </p>
 * <p>
 *   NOTE: The postgres package uses a {@link ConnectionFactory} to further abstracts away the connection
 *   pooling. A J2EE based application could implement the interface to provide access a JNDI DataSource.
 * </p>
 * 
 * An example how to use this model can be found in the unit tests.
 *
 */
public abstract class RepositoryFactory {
    private static RepositoryFactory instance;
    
    /**
     * Returns the current instance of the RepositoryFactory. Must have been previously set with the setter method.
     * If not done so, this throws an exception.
     */
    public static RepositoryFactory getInstance() {
        synchronized(RepositoryFactory.class) {
            if ( instance == null ) {
                throw new IllegalStateException("RepositoryFactory not initalized.");
            }
            return instance;
        }
    }
    public static void setInstance(RepositoryFactory repo) {
        synchronized(RepositoryFactory.class) {
            instance = repo;
        }
    }
    
    /**
     * Returns an instance of the {@link AccountRepository}. 
     */
    public abstract AccountRepository createAccountRepository();
    public abstract AlgebraRepository createAlgebraRepository();
    public abstract AtpRepository createAtpRepository();
    public abstract FormulaRepository createFormulaRepository();
    public abstract ProofRepository createProofRepository();
    public abstract QueueJobRepository createQueueJobRepository();
    public abstract OperatorRepository createOperatorRepository();
    
    /**
     * Returns a transaction object.
     */
    public abstract Transaction createTransaction();
}