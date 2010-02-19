package org.tptp.model.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import java.util.Properties;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import org.tptp.model.ModelException;
import org.tptp.model.WorkManager;

/**
 * This is a simple connection pool, as described in thesis. When requesting a connection,
 * it is first checked if the current thread has already a connection associated (via the ThreadLocal),
 * else its returned from the backing queue. if the queue is empty, a new connection is created.
 *
 * The prop parameter should contain the following keys:
 * <ul>
 *  <li>jdbc.string</li>
 *  <li>jdbc.class</li>
 *  <li>jdbc.username</li>
 *  <li>jdbc.password</li>
 * </ul>
 * The max_connection parameters, configures how many connection should be hold in the backing list at max.
 * 
 */
public class SimplePooledConnectionFactory implements ConnectionFactory {
    /**
     * Contains a list of connections that can be pooled for later reuse.
     */
    protected ArrayBlockingQueue<Connection> pooledConnections;
    
    /**
     * The list of connections currently in use through the open() method.
     */
    protected List<Connection> returnedConnections;
    
    /**
     * Contains a connection if the current thread has already requested a connection.
     */
    protected ThreadLocal<Connection> connection;
    
    /**
     * Set when the current connection is part of a transaction
     */
    protected ThreadLocal<Connection> transactionConnection;
    
    /**
     * The properties used to create a new connection.
     */
    protected Properties prop;
    
    protected int max_connections;
    
    /**
     * Creates a new factory with a max_connection value of 10.
     * 
     */
    public SimplePooledConnectionFactory(Properties prop) {
        this(prop, 10);
    }
    
    
    public SimplePooledConnectionFactory(Properties prop, int max_connections) {
        this.pooledConnections = new ArrayBlockingQueue<Connection>(max_connections);    
        this.returnedConnections = new CopyOnWriteArrayList<Connection>();
        
        this.connection = new ThreadLocal<Connection>();
        this.transactionConnection = new ThreadLocal<Connection>();
        
        this.prop = prop;
        this.max_connections = max_connections;
    }
    
    /**
     * Returns a brand new connection using the configured properties
     */
    protected Connection createConnection() throws ModelException {
        try {
            
            String jdbcString = prop.getProperty("jdbc.string");
            String username = prop.getProperty("jdbc.username","");
            String password = prop.getProperty("jdbc.password", "");
            String className = prop.getProperty("jdbc.class");
            
            Class.forName(className);
            
            Connection con = DriverManager.getConnection(jdbcString, username, password);
            con.setAutoCommit(true);
            return con;
        } catch (Exception exc) {
            throw new ModelException(exc);
        }
    }
    
    /**
     * Returns an unmodifiable List of the internal list of currently in use connections. FOR DEBUGGING ONLY
     */
    public List<Connection> getReturnedConnections() {
       return java.util.Collections.unmodifiableList(this.returnedConnections); 
    }
    
    /**
     * Returns a connection to use for the current thread. If a previous unreturned connection exists,
     * that one is returned. Otherwise a pooled or new connection is returned.
     */
    public Connection open() {
        // Return the previously given out connection.
        if ( this.connection.get() != null ) {
            return this.connection.get();
        }
        
        Connection con = this.pooledConnections.poll();
        if ( con == null ) {
            con = this.createConnection();
        }
        
        this.returnedConnections.add(con);
        this.connection.set(con);
        
        return con;
    }
    
    /**
     * This returns the connection to the pool (if not part of a transaction).
     *
     * @see WorkManager
     */
    public void close(Connection c) {
        // if we are in a connection, do NOT return a connection to the pool.
        if ( WorkManager.getCurrentTransaction() != null ) {
            return;
        }
        
        // Clean up our own business here
        this.returnedConnections.remove(c);
        
        if ( this.connection.get() == c ) {
            this.connection.remove();
        }
        
        // Clean the connection from any uncommited stuff
        try {
            c.rollback();
        }
        catch (Exception exc) {
            throw new ModelException("Error while cleaning up connection", exc);
        }
        
        // ok, we are clean, now store the connection
        if ( !this.pooledConnections.offer(c)) {
            // The pool don't want our connection, so close it
            try {
                c.close();
            } catch (Exception exc) {
                throw new ModelException(exc);
            }
        }
    }
    
    
    
    
    
    public void shutdown() {
        try {
            for ( Connection c : this.returnedConnections)
            {
                c.close();
            }
        } catch(Exception exc) {
            ; // TODO: Should we log/check this somehow?
            exc.printStackTrace();
        } finally {
            returnedConnections.clear();
            connection = new ThreadLocal<Connection>();
            transactionConnection = new ThreadLocal<Connection>();
        }
    }
        
        
        
        
        
    
    
    public void beginTransaction() {
        try {
            Connection c = open();
            c.setAutoCommit(false);
        } catch (SQLException exc) {
            throw new ModelException(exc);
        }
    }
    
    public void endTransaction() {
        try {
            Connection c = open();
            c.setAutoCommit(true);
            
            // WE close the connection here, since we opened it in beginTransaction()
            // and close() blocked all calls until the transaction is over.
            close(c); 
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