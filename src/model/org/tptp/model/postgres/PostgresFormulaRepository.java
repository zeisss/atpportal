package org.tptp.model.postgres;

import java.sql.*;
import org.tptp.model.*;
import java.util.*;

public class PostgresFormulaRepository extends FormulaRepository{
    private ConnectionFactory factory;
    public PostgresFormulaRepository(ConnectionFactory factory) {
        if ( factory == null) {
            throw new IllegalArgumentException("Factory must not be null!");
        }
        this.factory = factory;
    }
    
    public Set<Formula> getAll() {
        return fetchWithSql("");
    }
    
    public Set<Formula> getByFilter(long algebraId, String filter, boolean axioms, boolean theorems)
    {
        if ( filter == null ) {
            filter = "";
        }
        if ( !axioms && !theorems) {
            return new HashSet<Formula>();
        }
        
        // If no ID is given for the algebra, whe can not use the axiom / theorem -info
        if ( algebraId == -1 ) {
            return fetchWithSql(
                " WHERE name LIKE ?",
                new Object[]{"%" + filter + "%"}
            );
        } else {
            // Show all formulas linked with the given algebra
            if ( axioms && theorems ) {
                return fetchWithSql(
                    " , algebra_formula b WHERE b.algebra_id = ? AND a.id = b.formula_id AND a.name LIKE ?",
                    new Object[]{algebraId, "%" + filter + "%"}
                );   
            } else {
                // Show all formulas for the given theorem with axiom beeing true/false
                // NOTE: We handled both false/true above => axioms = !theorems
                return fetchWithSql(
                    " , algebra_formula b " +
                    "WHERE b.algebra_id = ? AND a.id = b.formula_id AND a.name LIKE ? AND axiom = " + (axioms ? "true" : "false"),
                    new Object[]{algebraId, "%" + filter + "%"}
                );    
            }
            
        }
    }
    
    public Set<Formula> getFormulasForQueueJob(long queueJobId)
    {
        if ( queueJobId == -1 ) {
            throw new IllegalArgumentException("QueueJob is not saved.");
        }
        return fetchWithSql(", queuejob_formulas b WHERE a.id = b.formula_id AND b.queuejob_id = " + queueJobId);
    }
    /**
     * Returns all Formulas which are used in the given proof.
     *
     * @see Proof
     */
    public Set<Formula> getUsedFormulasForProof(long proofId)
    {
        if ( proofId == -1 ) {
            throw new IllegalArgumentException("Formula is not saved.");
        }
        return fetchWithSql(", proof_formula_used b WHERE a.id = b.formula_id AND b.proof_id = " + proofId);
    }
    
    /**
     * Returns all Formulas that the given proof proves.
     */
    public Set<Formula> getProvedFormulasByProof(long proofId)
    {
        if ( proofId == -1 ) {
            throw new IllegalArgumentException("Formula is not saved.");
        }
        return fetchWithSql(", proof_formula_proves b WHERE a.id = b.formula_id AND b.proof_id = " + proofId);
    }
    
    public Set<Formula> getByAlgebra(Algebra algebra) {
        if ( algebra == null || algebra.getId() <= 0 ) {
            throw new IllegalArgumentException("Algebra is null or not saved");
        }
        return fetchWithSql(", algebra_formula b, algebra c WHERE a.id = b.formula_id AND c.id = b.algebra_id AND c.id = " + algebra.getId());
    }
    
    public Set<Formula> getAxiomsByAlgebra(Algebra algebra) { 
        if ( algebra == null || algebra.getId() <= 0 ) {
            throw new IllegalArgumentException("Algebra is null or not saved");
        }
        return fetchWithSql(
            ", algebra_formula b, algebra c" +
            " WHERE a.id = b.formula_id" +
            "   AND c.id = b.algebra_id" +
            "   AND c.id = " + algebra.getId() +
            "   AND axiom = true"
        );
    }
    
    public Set<Formula> getTheoremsByAlgebra(Algebra algebra) {
        if ( algebra == null || algebra.getId() <= 0 ) {
            throw new IllegalArgumentException("Algebra is null or not saved");
        }
        return fetchWithSql(
            ", algebra_formula b, algebra c" +
            " WHERE a.id = b.formula_id" +
            "   AND c.id = b.algebra_id" +
            "   AND c.id = " + algebra.getId() +
            "   AND axiom = false"
        );    
    }
    
    private Set<Formula> fetchWithSql(String sqlWhere) {
        return fetchWithSql(sqlWhere, new Object[]{});
    }
    private Set<Formula> fetchWithSql(String sqlWhere, Object[] objects) {
        String sql = "SELECT a.id, a.name, a.comment, a.formula_text FROM formula a " + sqlWhere;
        
        
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            PreparedStatement pst = connection.prepareStatement(sql);
  
            int i = 1;
            for ( Object obj : objects ) {
                if ( obj instanceof Long ) {
                    pst.setLong(i, (Long) obj);
                } else if ( obj instanceof String ) {
                    pst.setString(i, (String) obj);
                } else if ( obj instanceof Boolean ) {
                    pst.setBoolean(i, (Boolean)obj);  
                }
                i++;
            }
            ResultSet rs = pst.executeQuery();
            
            
            Map<Long,Formula> map = new HashMap<Long, Formula>();
            while ( rs.next() ) {
                Formula f = new Formula();
                f.setId(rs.getLong(1));
                f.setName(rs.getString(2));
                f.setComment(rs.getString(3));
                f.setFormulaText(rs.getString(4));
                map.put(f.getId(), f);
            }
            rs.close();
            
            if ( map.size() == 0) {
                // Abort, if we haven't found a formula
                return new HashSet<Formula>();
            }
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT abbreviation, authors, title, year, formula_id FROM formula_reference " +
                          "WHERE formula_id = ANY(ARRAY[");
            for(Long id : map.keySet()) {
                builder.append(id).append(",");
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append("]) ORDER BY formula_id");
            Statement st = connection.createStatement();
            
            rs = st.executeQuery(builder.toString());
            while ( rs.next()) {
                FormulaReference ref = new FormulaReference(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4)
                );
                map.get(rs.getLong(5)).addReference(ref);
            }
            
            return new HashSet<Formula>(map.values());
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }    
    }
    
    
    public Formula getByFormulaText(String formulaText) {
        if ( formulaText == null || formulaText.equals("")) {
            throw new IllegalArgumentException("Illegal formulaText: " + formulaText);
        }
        
        String sql = "SELECT id, name, comment, formula_text FROM formula WHERE formula_text = ? ";
        String sql_refe = "SELECT abbreviation, authors, title, year FROM formula_reference WHERE formula_id = ";
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, formulaText);
            
            ResultSet rs = st.executeQuery();
            if ( rs.next() ) {
                Formula f = new Formula();
                f.setId(rs.getLong(1));
                f.setName(rs.getString(2));
                f.setComment(rs.getString(3));
                f.setFormulaText(rs.getString(4));
                
                Statement stt = connection.createStatement();
                rs = stt.executeQuery(sql_refe + f.getId());
                while ( rs.next()) {
                    FormulaReference ref = new FormulaReference(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4)
                    );
                    f.addReference(ref);
                }
                
                return f;
            } else {
                // No row found with the given id
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
    
    public Formula get(long id) {
        if ( id <= 0 ) {
            throw new IllegalArgumentException("Illegal formula_id: " + id);
        }
        
        String sql = "SELECT id, name, comment, formula_text FROM formula WHERE id = " + id;
        String sql_refe = "SELECT abbreviation, authors, title, year FROM formula_reference WHERE formula_id = " + id;
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if ( rs.next() ) {
                Formula f = new Formula();
                f.setId(rs.getLong(1));
                f.setName(rs.getString(2));
                f.setComment(rs.getString(3));
                f.setFormulaText(rs.getString(4));
                
                rs = st.executeQuery(sql_refe);
                while ( rs.next()) {
                    FormulaReference ref = new FormulaReference(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4)
                    );
                    f.addReference(ref);
                }
                
                return f;
            } else {
                // No row found with the given id
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
    
    public void save(Formula f) {
        if ( f == null || f.getId() > 0) {
            throw new IllegalArgumentException("Formula is null or already created!");
        }
        if ( f.getFormulaText() == null || f.getFormulaText().equals("")) {
            throw new IllegalArgumentException("Cannot store formula with empty formula-text");
        }
        
        String sql = "INSERT INTO formula (id, name, comment, formula_text) VALUES (nextval('\"SEQ_FORMULA_ID\"'),?,?,?)";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if ( f.getName() == null ) {
                pst.setNull(1, Types.VARCHAR);
            } else {
                pst.setString(1, f.getName());
            }
            
            if ( f.getComment() == null) {
                pst.setNull(2, Types.VARCHAR);
            } else {
                pst.setString(2, f.getComment());    
            }
            
            pst.setString(3, f.getFormulaText());
            
            if ( 1 == pst.executeUpdate()) {
                ResultSet keys = pst.getGeneratedKeys();
                if ( keys.next()) {
                    f.setId(keys.getLong(1));
                    
                    storeReferences(con, f);
                } else {
                    // This should not happen, since we had an insert!
                }
            } else {
                // This should not happen (Docs: Returns 1 for DML, 2 for DDL Statements)
            }
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( con != null ) {
                factory.close(con);
            }
        }
    }
    
    public void update(Formula f) {
        if ( f == null || f.getId() == -1 ) {
            throw new IllegalArgumentException("Formula is null or not yet saved!");
        }
        if ( f.getFormulaText() == null || f.getFormulaText().equals("")) {
            throw new IllegalArgumentException("Cannot store formula with empty formula-text");
        }
        
        String sql = "UPDATE formula SET name = ?, comment = ?, formula_text = ? WHERE id = ?";
        String sql_references = "DELETE FROM formula_reference WHERE formula_id = " + f.getId();    
        
        Connection con = null;
        try {
            con = factory.open();
            
            PreparedStatement pst = con.prepareStatement(sql);
            if ( f.getName() == null ) {
                pst.setNull(1, Types.VARCHAR);
            } else {
                pst.setString(1, f.getName());
            }
            
            if ( f.getComment() == null) {
                pst.setNull(2, Types.VARCHAR);
            } else {
                pst.setString(2, f.getComment());    
            }
            pst.setString(3, f.getFormulaText());
            pst.setLong(4, f.getId());
            
            if ( 1 == pst.executeUpdate()) {
                // Delete all references (they are only value objects!)
                Statement st = con.createStatement();
                st.executeUpdate(sql_references);
                
                // Recreate the references
                storeReferences(con, f);
            }
        }  catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( con != null ) {
                factory.close(con);
            }
        }
    }
    
    public void delete(Formula f) {
        if ( f == null ) {
            return; // Everything is fine ,)
        }
        
        if (f.getId() == -1) {
            throw new IllegalArgumentException("Formula is not created yet!");
        }
        
        // We only delete the formula, because the references are deleted through the
        // Postgres constraint
        String sql = "DELETE FROM formula WHERE id = ?";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, f.getId());
            if ( 1 == pst.executeUpdate()) {
                f.setId(-1);
            }
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( con != null ) {
                factory.close(con);
            }
        }
    }
    
    private void storeReferences(Connection con, Formula f) throws SQLException {
        String sql_reference = "INSERT INTO formula_reference (formula_id, abbreviation, authors, title, year) VALUES (?,?,?,?,?)";
        
        PreparedStatement pst = con.prepareStatement(sql_reference);
        for ( FormulaReference ref : f.getReferences() ) {
            pst.clearParameters();
            pst.setLong(1, f.getId());
            pst.setString(2, ref.getAbbreviation());
            pst.setString(3, ref.getAuthors());
            pst.setString(4, ref.getTitle());
            if ( ref.getYear() <= 0 )
                pst.setNull(5, Types.INTEGER);
            else 
                pst.setInt(5, ref.getYear());
            pst.executeUpdate();
        }
    }
}