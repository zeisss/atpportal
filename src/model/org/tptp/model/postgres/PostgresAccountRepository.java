package org.tptp.model.postgres;

import java.util.*;
import java.sql.*;

import org.tptp.model.*;
import org.tptp.model.jdbc.ConnectionFactory;

public class PostgresAccountRepository extends AccountRepository {
    private ConnectionFactory factory;
    public PostgresAccountRepository(ConnectionFactory factory) {
        if ( factory == null ) {
            throw new IllegalArgumentException("Factory must not be null!");
        }
        this.factory = factory;
    }
    
    
    public void storePassword(Account account, String password) {
        if ( account == null || account.getId() == -1 ) {
            throw new IllegalArgumentException("Account is null or not saved!");
        }
        
        String sql = "UPDATE account SET login_password = MD5(?) WHERE id = ?";
        Connection connection = null;
        try
        {
            connection = factory.open();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, password);
            st.setLong(2, account.getId());
            st.executeUpdate();
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    private Account buildFromResultSet(ResultSet rs) throws SQLException {
        return new Account(
            rs.getLong(1),
            rs.getString(2), // email,
            rs.getString(3), // login name
            rs.getString(4), // display name
            rs.getInt(5) // level
        );
    }
    
    private Account fetchBySql(String sqlWhere, Object[] objects) {
        String sql = "SELECT a.id, a.email, a.login_name, a.display_name, a.level FROM account a " + sqlWhere;
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            PreparedStatement st = connection.prepareStatement(sql);
            
            int i = 1;
            for (Object obj :objects ) {
                if ( obj instanceof Long ) {
                    st.setLong(i, (Long)obj);
                } else if ( obj instanceof String ) {
                    st.setString(i, obj.toString());
                } 
                i++;
            }
            
            ResultSet rs = st.executeQuery();
            if ( rs.next() ) {
                return buildFromResultSet(rs);
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
    public Account getByLoginNameAndPassword(String loginName, String password) {
        return fetchBySql("WHERE login_name = ? AND login_password = md5(?)", new Object[]{loginName, password});
    }
    public Account get(long id)
    {
        return fetchBySql("WHERE id = ?", new Object[]{new Long(id)});
    }
    public Account getByLoginName(String username) {
        return fetchBySql("WHERE login_name = ?", new Object[]{username});
    }
    
    public Set<Account> getAll()
    {
        String sql = "SELECT id, email, login_name, display_name, level FROM account";
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            Set<Account> accs = new HashSet<Account>();
            while ( rs.next() )
            {
                Account acc = buildFromResultSet(rs);
                accs.add(acc);
            }
            return accs;
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    
    
    public void save(Account account) {
        if ( account == null || account.getId() > 0 ) {
            throw new IllegalArgumentException("Account is null or already saved.");
        }
        
        String sql = "INSERT INTO account (id, email, login_name, display_name, level) VALUES (nextval('\"SEQ_ACCOUNT_ID\"'),?,?,?,?)";
        Connection con = null;
        
        try
        {
            con = factory.open();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, account.getEmail());
            st.setString(2, account.getLoginName());
            st.setString(3, account.getDisplayName());
            st.setInt(4, account.getLevel());
            if ( 1 == st.executeUpdate() ) {
                ResultSet keys = st.getGeneratedKeys();
                if ( keys.next()) {
                    account.setId(keys.getLong(1));
                }
            }
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( con != null ) {
                factory.close(con);
            }
        }
        
    }
    public void update(Account account) {
        if ( account == null || account.getId() == -1 ) {
            throw new IllegalArgumentException("Account is null or not saved.");
        }
        
        String sql = "UPDATE account SET email = ?, login_name = ?, display_name = ?, level = ? WHERE id = ?";
        Connection con = null;
        
        try
        {
            con = factory.open();
            PreparedStatement st = con.prepareStatement(sql);
            
            st.setString(1, account.getEmail());
            st.setString(2, account.getLoginName());
            st.setString(3, account.getDisplayName());
            st.setInt(4, account.getLevel());
            st.setLong(5, account.getId());
            st.executeUpdate();
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( con != null ) {
                factory.close(con);
            }
        }
    }
    public void delete(Account account) {
        if ( account == null || account.getId() <= 0) {
            throw new IllegalArgumentException("Account is null or not saved.");
        }
        
        String sql = "DELETE FROM account WHERE id = ?";
        Connection con = null;
        
        try
        {
            con = factory.open();
            PreparedStatement st = con.prepareStatement(sql);
            
            st.setLong(1, account.getId());
            st.executeUpdate();
            account.setId(-1);
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( con != null ) {
                factory.close(con);
            }
        }
    }
    
}