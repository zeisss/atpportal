package org.tptp.model.postgres;

import java.util.*;
import java.sql.*;

import org.tptp.model.*;
import org.tptp.model.jdbc.ConnectionFactory;

public class PostgresProofRepository extends ProofRepository {
    private ConnectionFactory factory;
    public PostgresProofRepository(ConnectionFactory factory) {
        if ( factory == null ) {
            throw new IllegalArgumentException("Factory must not be null!");
        }
        this.factory = factory;
    }
    
    
    private Proof buildFromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong(1);
        if ( rs.wasNull() ) id = -1;
        
        java.util.Date date = new java.util.Date(rs.getTimestamp(2).getTime());
        
        Proof proof = new Proof(id, date);
        return proof;
    }
    
    private ProofStep buildStepFromRS(ResultSet rs) throws SQLException {
        return new ProofStep(
            rs.getLong(2),
            rs.getString(3),
            rs.getString(4)
        );
    }
    
    private void storeProofDetails(Connection con, Proof proof) throws SQLException {
        String sql = "DELETE FROM proof_detail WHERE proof_id = " + proof.getId();
        Statement st = con.createStatement();
        st.executeUpdate(sql);
        
        
        sql = "INSERT INTO proof_detail (proof_id, detail_name, detail_value) VALUES (?,?,?)";
        PreparedStatement pst = con.prepareStatement(sql);
        
        for(String key : proof.getDetailKeys()) {
            pst.clearParameters();
            pst.setLong(1, proof.getId());
            pst.setString(2, key);
            pst.setString(3, proof.getDetail(key));
            
            if ( 1 == pst.executeUpdate() ) {
                // All ok
            }
        }
    }
    
    private void storeProofSteps(Connection con, Proof proof) throws SQLException {
        String sql = "DELETE FROM proof_step WHERE proof_id = " + proof.getId();
        Statement st = con.createStatement();
        st.executeUpdate(sql);
        
        sql = "INSERT INTO proof_step (proof_id, line, formula, reasoning) VALUES (?,?,?,?)";
        PreparedStatement pst = con.prepareStatement(sql);
        
        for(ProofStep step : proof.getProofSteps()) {
            pst.clearParameters();
            pst.setLong(1, proof.getId());
            pst.setLong(2, step.getLine());
            pst.setString(3, step.getFormula());
            pst.setString(4, step.getReasoning());
            
            if ( 1 == pst.executeUpdate() ) {
                // All ok
            }
        }
    }
    
    private Set<Proof> fetchWithSql(String sql) {
        String s = "SELECT a.id, a.timestamp FROM proof a " + sql;
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(s);
            
            Set<Proof> set = new HashSet<Proof>();
            while ( rs.next() )
            {
                set.add(buildFromResultSet(rs));
            }
            
            for(Proof proof : set) {
                sql = "SELECT proof_id, line, formula, reasoning FROM proof_step WHERE proof_id = " + proof.getId();
                rs = st.executeQuery(sql);
                while ( rs.next()) {
                    proof.addProofStep(buildStepFromRS(rs));
                }
            }
            for(Proof proof : set) {
                sql = "SELECT proof_id, detail_name, detail_value FROM proof_detail WHERE proof_id = " + proof.getId();
                rs = st.executeQuery(sql);
                while ( rs.next()) {
                    proof.setDetail(rs.getString(2), rs.getString(3));
                }
            }
            return set;
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    public Set<Proof> getProofsForFormula(long formulaId)
    {
        if ( formulaId == -1) {
            throw new IllegalArgumentException("Formula ID is negative.");   
        }
        return fetchWithSql(", proof_formula_proves b WHERE a.id = b.proof_id AND b.formula_id = " + formulaId);
    }
    
    public Set<Proof> getProofsWhereFormulaUsed(long formulaId)
    {
        if ( formulaId == -1) {
            throw new IllegalArgumentException("Formula ID is negative.");   
        }
        return fetchWithSql(", proof_formula_used b WHERE a.id = b.proof_id AND b.formula_id = " + formulaId);
    }
    
    public void storeUsage(long proofId, long formulaId)
    {
        if ( proofId == -1 || formulaId == -1) {
            throw new IllegalArgumentException("Proof or Formula ID is negative.");   
        } 
        String sql = "INSERT INTO proof_formula_used (proof_id, formula_id) VALUES (?,?)";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, proofId);
            pst.setLong(2, formulaId);
            if ( 1 == pst.executeUpdate() ) {
                
            }
            else
            {
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
    
    public void storeProve(long proofId, long formulaId)
    {
        if ( proofId == -1 || formulaId == -1) {
            throw new IllegalArgumentException("Proof or Formula ID is negative.");   
        }
        
        String sql = "INSERT INTO proof_formula_proves (proof_id, formula_id) VALUES (?,?)";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, proofId);
            pst.setLong(2, formulaId);
            if ( 1 == pst.executeUpdate() ) {
                
            }
            else
            {
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
    
    
    public Set<Proof> getAll() {
        String sql = "SELECT id, timestamp FROM proof";
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            Set<Proof> set = new HashSet<Proof>();
            while ( rs.next() )
            {
                set.add(buildFromResultSet(rs));
            }
            
            for(Proof proof : set) {
                sql = "SELECT proof_id, line, formula, reasoning FROM proof_step WHERE proof_id = " + proof.getId();
                rs = st.executeQuery(sql);
                while ( rs.next()) {
                    proof.addProofStep(buildStepFromRS(rs));
                }
            }
            for(Proof proof : set) {
                sql = "SELECT proof_id, detail_name, detail_value FROM proof_detail WHERE proof_id = " + proof.getId();
                rs = st.executeQuery(sql);
                while ( rs.next()) {
                    proof.setDetail(rs.getString(2), rs.getString(3));
                }
            }
            return set;
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    public Proof get(long id) {
        String sql = "SELECT id, timestamp " +
                     "FROM proof WHERE id = " + id;
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if ( rs.next() )
            {
                Proof proof = buildFromResultSet(rs);
            
                sql = "SELECT proof_id, line, formula, reasoning FROM proof_step WHERE proof_id = " + proof.getId();
                rs = st.executeQuery(sql);
                while ( rs.next()) {
                    proof.addProofStep(buildStepFromRS(rs));
                }
            
                sql = "SELECT proof_id, detail_name, detail_value FROM proof_detail WHERE proof_id = " + proof.getId();
                rs = st.executeQuery(sql);
                while ( rs.next()) {
                    proof.setDetail(rs.getString(2), rs.getString(3));
                }
            
                return proof;
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
    
    public void save(Proof proof) {
        if ( proof == null || proof.getId() != -1) {
            throw new IllegalArgumentException("Proof is null or already created!");
        }
        String sql = "INSERT INTO proof (id, timestamp) " +
                     "VALUES (nextval('\"SEQ_PROOF_ID\"'),?)";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setTimestamp(1, new java.sql.Timestamp(proof.getTime().getTime()));
            
            if ( 1 == pst.executeUpdate()) {
                ResultSet keys = pst.getGeneratedKeys();
                if ( keys.next()) {
                    proof.setId(keys.getLong(1));
                    
                    storeProofDetails(con, proof);
                    storeProofSteps(con, proof);
                    
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
    
    public void update(Proof proof) {
        if ( proof == null || proof.getId() == -1) {
            throw new IllegalArgumentException("Proof is null or not yet created!");
        }
        String sql = "UPDATE proof SET timestamp = ? WHERE id = ?";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setTimestamp(1, new Timestamp(proof.getTime().getTime()));
            pst.setLong(2, proof.getId());
            
            if ( 1 == pst.executeUpdate()) {
                storeProofDetails(con, proof);
                storeProofSteps(con, proof);
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
    
    public void delete(Proof proof) {
        if ( proof == null ) {
            return; // Everything is fine ,)
        }
        
        if (proof.getId() == -1) {
            throw new IllegalArgumentException("Proof is null or not created!");
        }
        String sql = "DELETE FROM proof WHERE id = ?";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, proof.getId());
            if ( 1 == pst.executeUpdate()) {
                proof.setId(-1);
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