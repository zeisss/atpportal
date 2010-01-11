package org.tptp.model.postgres;

import java.util.*;
import java.sql.*;
import java.io.*;

import org.tptp.model.*;

/**
 * This is a simple implementation of the ConnectionFactory providing a very simple
 * connection pooling (actually, it's not really a pooling). Its recommend to use
 * this only for testing.
 *
 * This factory creates a new connection upon calling {@link #open()}, if no connection has been
 * created previously by this {@link java.lang.Thread}. The connections are stored in a {@link ThreadLocal}.
 * The {@link close(Connection)} method is empty, because we don't close connections. The {@link #shutdown()} method
 * iterates over all created connections and closes them, but this method is not a part of the interface, so the
 * application developer has to call it manually on shutdown, if this class is used.
 * 
 * @see ConnectionFactory
 * @see PostgresRepositoryFactory
 */
public class DefaultConnectionFactory implements ConnectionFactory {
    private Properties prop;
    private ThreadLocal<Connection> connection = new ThreadLocal<Connection>();
    private Set<Connection> connections = new HashSet<Connection>();
    
    public DefaultConnectionFactory(Properties prop) {
        this.prop = prop;
    }
    
    /**
     * Returns a (cached) connection for the current thread. Close after usage with
     * {@link #close(Connection)}.
     *
     * @see #close(Connection)
     */
    public Connection open() {
        if (connection.get() == null)
        {
            try {
                
                String jdbcString = prop.getProperty("jdbc.string");
                String username = prop.getProperty("jdbc.username","");
                String password = prop.getProperty("jdbc.password", "");
                String className = prop.getProperty("jdbc.class");
                
                Class.forName(className);
                
                Connection con = DriverManager.getConnection(jdbcString, username, password);
                
                connection.set(con);
                connections.add(con);
                
                con.setAutoCommit(true);
            }
            catch (Exception exc) {
                throw new ModelException(exc);
            }
        }
        if ( connection.get() == null ) {
            throw new IllegalStateException("Connection still null!");
        }
        return connection.get();
    }
    
    public void close(Connection c) {
        // We do nothing
    }
    
    public void shutdown() {
        try {
            for ( Connection c : connections)
            {
                c.close();
            }
        } catch(Exception exc) {
            ; // TODO: Should we log/check this somehow?
            exc.printStackTrace();
        } finally {
            connections.clear();
            connection = new ThreadLocal<Connection>();
        }
    }
    
    
    
    
    
    public void beginTransaction() {
        try {
            open().setAutoCommit(false);
        } catch (SQLException exc) {
            throw new ModelException(exc);
        }
    }
    
    public void endTransaction() {
        try {
            open().setAutoCommit(true);
        } catch (SQLException exc) {
            throw new ModelException(exc);
        }
    }
    
    public void commit() {
        try {
            open().commit();
        } catch (Exception exc) {
            throw new ModelException(exc);
        }
    }
    
    public void rollback() {
        try {
            open().rollback();
        } catch (Exception exc) {
            throw new ModelException(exc);
        }
    }
}