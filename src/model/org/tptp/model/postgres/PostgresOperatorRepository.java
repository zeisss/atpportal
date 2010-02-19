package org.tptp.model.postgres;

import java.util.*;
import java.sql.*;

import org.tptp.model.*;
import org.tptp.model.jdbc.ConnectionFactory;

public class PostgresOperatorRepository extends OperatorRepository {
    private ConnectionFactory factory;
    public PostgresOperatorRepository(ConnectionFactory factory) {
        if ( factory == null ) {
            throw new IllegalArgumentException("Factory must not be null!");
        }
        this.factory = factory;
    }
    
    private Operator fromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong(1);
        if ( rs.wasNull()) id = -1;
        
        String name = rs.getString(2);
        if ( rs.wasNull()) name = "";
        
        int binding = rs.getInt(3);
        if ( rs.wasNull()) binding = -1;
        
        String notation = rs.getString(4);
        if ( rs.wasNull()) notation = "";

        String symbol = rs.getString(5);
        if ( rs.wasNull()) symbol = "";
        
        String symbol_intern = rs.getString(6);
        if ( rs.wasNull()) symbol_intern = "";
        
        int arity = rs.getInt(7);
        if ( rs.wasNull()) arity = -1;
        
        String description = rs.getString(8);
        if ( rs.wasNull()) description = "";

        String comment = rs.getString(9);
        if ( rs.wasNull()) comment = "";        
        Operator operator = new Operator(id, name, binding, notation, symbol, symbol_intern, arity, description, comment);
        return operator;
    }
    
    
    public Set<Operator> getOperatorsByName(String name) {
        String sql = "SELECT b.id, a.name, a.binding, a.notation, a.symbol, b.symbol_intern, b.arity, b.description, b.comment FROM operator_syntax_format a, operator b WHERE a.name = ? AND a.operator_id=b.id";      
        Connection connection = null;
        try
        {
            connection = factory.open();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            Set<Operator> operators = new HashSet<Operator>();
            while ( rs.next() )
            {
                operators.add(fromResultSet(rs));
            }
            return operators;
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    

} //end class