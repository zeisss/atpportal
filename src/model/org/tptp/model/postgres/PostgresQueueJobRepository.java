package org.tptp.model.postgres;

import java.util.*;
import java.sql.*;

import org.tptp.model.*;
import org.tptp.model.jdbc.ConnectionFactory;

public class PostgresQueueJobRepository extends QueueJobRepository {
    // The columns in the order as parsed by the buildFromResultSet() method
    private static final String DB_COLUMNS = "id, goal_formula, atp_id, account_id, proof_id, status, timestamp, message, input_text, output_text ";
    
    private ConnectionFactory factory;
    
    public PostgresQueueJobRepository(ConnectionFactory factory) {
        if ( factory == null ) {
            throw new IllegalArgumentException("Factory must not be null!");
        }
        this.factory = factory;
    }
    public void storeInputFormula(QueueJob job, Formula f) {
        if ( job == null || job.getId() == -1 ) {
            throw new IllegalArgumentException("QueueJob is null or not saved.");
        }
        if ( f== null || f.getId() == -1 ) {
            throw new IllegalArgumentException("Formula is null or not saved.");
        }
        
        String sql = "INSERT INTO queuejob_formulas (queuejob_id, formula_id) VALUES (?, ?)";
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setLong(1,job.getId());
            st.setLong(2, f.getId());
            st.executeUpdate();
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    private QueueJob buildFromResultSet(ResultSet rs) throws SQLException {
        QueueJob job = new QueueJob(
            rs.getLong(1),
            rs.getString(2),
            rs.getLong(3),
            rs.getLong(4),
            rs.getLong(5),
            rs.getInt(6), // Status
            new java.util.Date(rs.getTimestamp(7).getTime()),
            rs.getString(8), // message
            rs.getString(9), // input
            rs.getString(10) // output
        );
        
        job.setId(rs.getLong(1));
        if ( rs.wasNull() )
            job.setId(-1); // Just in case ;)
            
        job.setAtpId(rs.getLong(3));
        if ( rs.wasNull()) job.setAtpId(-1);
        
        job.setAccountId(rs.getLong(4));
        if ( rs.wasNull()) job.setAccountId(-1);
        
        job.setProofId(rs.getLong(5));
        if ( rs.wasNull()) job.setProofId(-1);
        
        job.setMessage(rs.getString(8));
        if ( rs.wasNull()) job.setMessage(null);
        
        job.setInputText(rs.getString(9));
        if ( rs.wasNull()) job.setInputText(null);
        
        job.setOutputText(rs.getString(10));
        if ( rs.wasNull()) job.setOutputText(null);
        
        return job;
    }
    
    public QueueJob getNextQueueJob()
    {
        String sql = "SELECT " + DB_COLUMNS + 
                     "FROM queuejob WHERE status = " + QueueJob.STATUS_QUEUED;
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if ( rs.next() )
            {
                QueueJob job = buildFromResultSet(rs);
                return job;
            } else {
                return null;
            }
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    public QueueJob getByProofId(long proofId)
    {
        String sql = "SELECT " + DB_COLUMNS + 
                     "FROM queuejob WHERE proof_id = " + proofId;
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if ( rs.next() )
            {
                QueueJob job = buildFromResultSet(rs);
                return job;
            } else {
                return null;
            }
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    public List<QueueJob> getRange(int begin, int length)
    {
        if ( begin < 0 ) {
            throw new IllegalArgumentException("Begin must be position or equal to zero."); 
        }
        
        if ( length < 0 ) {
            throw new IllegalArgumentException("Length must be position or equal to zero."); 
        }
        
        String sql = "SELECT " + DB_COLUMNS + 
                     "FROM queuejob " +
                     "ORDER BY timestamp DESC " + 
                     "LIMIT " + length + " OFFSET " + begin;
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            List<QueueJob> jobs = new ArrayList<QueueJob>(length);
            while ( rs.next() )
            {
                QueueJob job = buildFromResultSet(rs);
                jobs.add(job);
            }
            return jobs;
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    public Set<QueueJob> getAll() {
        String sql = "SELECT " + DB_COLUMNS + " FROM queuejob";
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            Set<QueueJob> jobs = new HashSet<QueueJob>();
            while ( rs.next() )
            {
                QueueJob job = buildFromResultSet(rs);
                jobs.add(job);
            }
            return jobs;
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    public QueueJob get(long id) {
        String sql = "SELECT " + DB_COLUMNS + 
                     "FROM queuejob WHERE id = " + id;
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if ( rs.next() )
            {
                QueueJob job = buildFromResultSet(rs);
                return job;
            } else {
                return null;
            }
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    public void save(QueueJob job) {
        if ( job == null || job.getId() != -1) {
            throw new IllegalArgumentException("Job is null or already created!");
        }
        
        // "id, goal_formula, atp_id, account_id, proof_id, status, timestamp, message, input_text, output_text ";
        String sql = "INSERT INTO queuejob (" + DB_COLUMNS + ") " +
                     "VALUES (nextval('\"SEQ_QUEUEJOB_ID\"'),?,?,?,?,?,?,?,?,?)";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(1, job.getGoalFormula());
            
            if ( job.getAtpId() == -1 ) {
                pst.setNull(2, Types.BIGINT);
            } else {
                pst.setLong(2, job.getAtpId());
            }
            
            if ( job.getAccountId() == -1) {
                pst.setNull(3, Types.BIGINT);
            } else {
                pst.setLong(3, job.getAccountId());    
            }
            
            if ( job.getProofId() == -1 ) {
                pst.setNull(4, Types.BIGINT);
            } else {
                pst.setLong(4, job.getProofId());    
            }
            
            pst.setInt(5, job.getStatus());
            
            if ( job.getCreatedAt() == null ) {
                pst.setNull(6, Types.TIMESTAMP);
            } else {
                pst.setTimestamp(6, new java.sql.Timestamp(job.getCreatedAt().getTime()));
            }
            
            if ( job.getMessage() == null ) {
                pst.setNull(7, Types.CHAR );
            } else {
                pst.setString(7, job.getMessage());
            }
            
            if ( job.getInputText() == null ) {
                pst.setNull(8, Types.CHAR );
            } else {
                pst.setString(8, job.getInputText());
            }
            
            if ( job.getOutputText() == null ) {
                pst.setNull(9, Types.CHAR );
            } else {
                pst.setString(9, job.getOutputText());
            }
            
            
            
            if ( 1 == pst.executeUpdate()) {
                ResultSet keys = pst.getGeneratedKeys();
                if ( keys.next()) {
                    job.setId(keys.getLong(1));
                } else {
                    // This should not happen, since we had an insert!
                }
            } else {
                // This should not happen (Docs: 1 for DML, 2 for DDL Statements)
                
            }
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( con != null ) {
                factory.close(con);
            }
        }
    }
    
    public void update(QueueJob job) {
        if ( job == null || job.getId() == -1) {
            throw new IllegalArgumentException("QueueJob is null or not yet created!");
        }
        String sql = "UPDATE queuejob SET atp_id = ?, goal_formula = ?, proof_id = ?, account_id = ?, status = ?, timestamp = ?, message = ?, input_text = ?, output_text = ? WHERE id = ?";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql);
            
            if ( job.getAtpId() == -1 ) {
                pst.setNull(1, Types.BIGINT);
            } else {
                pst.setLong(1, job.getAtpId());
            }
            pst.setString(2, job.getGoalFormula());
            
            System.out.println(job.getProofId());
            if ( job.getProofId() == -1 ) {
                pst.setNull(3, Types.BIGINT);
            } else {
                pst.setLong(3, job.getProofId());    
            }
            if ( job.getAccountId() == -1) {
                pst.setNull(4, Types.BIGINT);
            } else {
                pst.setLong(4, job.getAccountId());    
            }
            
            pst.setInt(5, job.getStatus());
            if ( job.getCreatedAt() == null ) {
                pst.setNull(6, Types.TIMESTAMP);
            } else {
                pst.setTimestamp(6, new java.sql.Timestamp(job.getCreatedAt().getTime()));
            }
            
            if ( job.getMessage() == null ) {
                pst.setNull(7, Types.CHAR );
            } else {
                pst.setString(7, job.getMessage());
            }
            
            if ( job.getInputText() == null ) {
                pst.setNull(8, Types.CHAR );
            } else {
                pst.setString(8, job.getInputText());
            }
            
            if ( job.getOutputText() == null ) {
                pst.setNull(9, Types.CHAR );
            } else {
                pst.setString(9, job.getOutputText());
            }
            
            
            pst.setLong(10, job.getId());
            
            
            if ( 1 == pst.executeUpdate()) {
                
            } else {
                // This should not happen (Docs: 1 for DML, 2 for DDL Statements)
                
            }
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( con != null ) {
                factory.close(con);
            }
        }
    }
    
    public void delete(QueueJob job) {
        if ( job == null ) {
            return; // Everything is fine ,)
        }
        
        if (job.getId() == -1) {
            throw new IllegalArgumentException("QueueJob is null or not created!");
        }
        String sql = "DELETE FROM queuejob WHERE id = ?";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, job.getId());
            if ( 1 == pst.executeUpdate()) {
                job.setId(-1);
            }
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( con != null ) {
                factory.close(con);
            }
        }
    }

}