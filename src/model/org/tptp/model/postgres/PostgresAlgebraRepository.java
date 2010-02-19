package org.tptp.model.postgres;

import org.tptp.model.*;

import javax.sql.*;
import java.sql.*;
import java.util.*;
import org.tptp.model.jdbc.ConnectionFactory;

public class PostgresAlgebraRepository extends AlgebraRepository
{
    private ConnectionFactory factory;
    public PostgresAlgebraRepository(ConnectionFactory factory) {
        if ( factory == null ) {
            throw new IllegalArgumentException("Factory must not be null!");
        }
        this.factory = factory;
    }
    
    public void link(Algebra algebra, Formula formula, boolean axiom)
    {
        if ( algebra == null || algebra.getId() <= 0 )
        {
            throw new IllegalArgumentException("Algebra is null or not saved.");
        }
        if ( formula == null || formula.getId() <= 0 ) {
            throw new IllegalArgumentException("Formula is null or not saved.");
        }
        unlink(algebra, formula);
        
        String sql = "INSERT INTO algebra_formula (algebra_id, formula_id, axiom) VALUES (?,?,?)";
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setLong(1, algebra.getId());
            st.setLong(2, formula.getId());
            st.setBoolean(3, axiom);
            
            st.executeUpdate();
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    public void unlink(Algebra algebra, Formula formula)
    {
        if ( algebra == null || algebra.getId() <= 0 )
        {
            throw new IllegalArgumentException("Algebra is null or not saved.");
        }
        if ( formula == null || formula.getId() <= 0 ) {
            throw new IllegalArgumentException("Formula is null or not saved.");
        }
        
        String sql = "DELETE FROM algebra_formula WHERE algebra_id = ? AND formula_id = ?";
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setLong(1, algebra.getId());
            st.setLong(2, formula.getId());
            st.executeUpdate();
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    private Algebra fromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong(1);
        if ( rs.wasNull()) id = -1;
        
        String name = rs.getString(2);
        if ( rs.wasNull()) name = "";
        
        String comment = rs.getString(3);
        if ( rs.wasNull()) comment = "";
        
        Algebra algebra = new Algebra(id, name, comment);
        return algebra;
    }
    
    public Algebra get(long id) {
        String sql = "SELECT id, name, comment FROM algebra WHERE id = " + id;
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if ( rs.next() ) {
                return fromResultSet(rs);
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
    
    public Set<Algebra> getAll() {
        String sql = "SELECT id, name, comment FROM algebra";
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            Set<Algebra> algebren = new HashSet<Algebra>();
            while ( rs.next() )
            {
                algebren.add(fromResultSet(rs));
            }
            return algebren;
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    private Set<Algebra> getAlgebren(long formulaId, boolean axiom) {
        String sql = "SELECT a.id, a.name, a.comment FROM algebra a, algebra_formula b WHERE a.id = b.algebra_id AND b.formula_id = ? AND b.axiom = ?";
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setLong(1, formulaId);
            st.setBoolean(2, axiom);
            ResultSet rs = st.executeQuery();
            
            Set<Algebra> algebren = new HashSet<Algebra>();
            while ( rs.next() )
            {
                algebren.add(fromResultSet(rs));
            }
            return algebren;
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    public Set<Algebra> getAlgebrenForAxiom(long formulaId) {
        return getAlgebren(formulaId, true);
    }
    
    public Set<Algebra> getAlgebrenForTheorem(long formulaId) {
        return getAlgebren(formulaId, false);
    }
    
    public void save(Algebra algebra) {
        if ( algebra == null || algebra.getId() != -1) {
            throw new IllegalArgumentException("Algebra is null or already created!");
        }
        String sql = "INSERT INTO algebra (id, name, comment) VALUES (nextval('\"SEQ_ALGEBRA_ID\"'), ?, ?)";
        
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = factory.open();
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, algebra.getName());
            if ( algebra.getComment() == null || algebra.getComment().equals("")) {
                pst.setNull(2, Types.VARCHAR);
            } else {
                pst.setString(2, algebra.getComment());    
            }
            
            
            if ( 1 == pst.executeUpdate()) {
                ResultSet keys = pst.getGeneratedKeys();
                if ( keys.next()) {
                    algebra.setId(keys.getLong(1));
                } else {
                    // This should not happen, since we had an insert!
                }
            } else {
                // This should not happen (Docs: 1 for DML, 2 for DDL Statements)
            }
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( pst != null ) {
                try {
                    pst.close();
                } catch (SQLException exc) {
                    throw new ModelException(exc);
                }
            }
            if ( con != null ) {
                factory.close(con);
            }
        }
    }
    public void update(Algebra algebra) {
        if ( algebra == null || algebra.getId() == -1) {
            throw new IllegalArgumentException("Algebra is null or not yet created!");
        }
        String sql = "UPDATE algebra SET name = ?, comment = ? WHERE id = ?";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, algebra.getName());
            if ( algebra.getComment() == null) {
                pst.setNull(2, Types.VARCHAR);
            } else {
                pst.setString(2, algebra.getComment());    
            }
            pst.setLong(3, algebra.getId());
            
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
    public void delete(Algebra algebra) {
        if ( algebra == null ) {
            return; // Everything is fine ,)
        }
        
        if (algebra.getId() == -1) {
            throw new IllegalArgumentException("Algebra is null or not created!");
        }
        String sql = "DELETE FROM algebra WHERE id = ?";
        
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = factory.open();
            pst = con.prepareStatement(sql);
            pst.setLong(1, algebra.getId());
            if ( 1 == pst.executeUpdate()) {
                algebra.setId(-1);
            }
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( pst != null ) {
                try {
                    pst.close();
                } catch(SQLException exc) {
                    throw new ModelException(exc);
                }
            }
            if ( con != null ) {
                factory.close(con);
            }
        }
        
    }
    
}