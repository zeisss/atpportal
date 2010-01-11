package org.tptp.model.postgres;

import java.sql.*;
import java.util.*;

import org.tptp.model.*;

/**
 * The ConnectionFactory is kind of a DataSource for the org.tptp.model.postgres package. The repositories
 * all have a ConnectionFactory instance, which they use to gain a Connection (and loosing it).
 *
 * @see PostgresRepositoryFactory
 */
public interface ConnectionFactory extends Transaction {
    /**
     * Returns a {java.sql.Connection} that can be used for accessing the database. After using return it with the close(Connection) method.
     */
    public Connection open();
    
    /**
     * Returns the Connection gained by {#open()} to this factory. Its up to the factory to close or reuse the connection.
     */
    public void close(Connection c);
}