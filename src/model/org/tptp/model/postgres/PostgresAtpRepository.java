package org.tptp.model.postgres;

import java.util.*;
import java.sql.*;

import org.tptp.model.*;
import org.tptp.model.jdbc.ConnectionFactory;

public class PostgresAtpRepository extends AtpRepository {
    private ConnectionFactory factory;
    public PostgresAtpRepository(ConnectionFactory factory) {
        if ( factory == null) {
            throw new IllegalArgumentException("Factory must not be null!");
        }
        this.factory = factory;
    }
    
    public List<Atp> getAll() {
        String sql = "SELECT id, name, atp_name, atp_version FROM atp ORDER BY id DESC";
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            List<Atp> resultList = new ArrayList<Atp>();
            SortedMap<Long,Atp> map = new TreeMap<Long,Atp>();
            while ( rs.next() )
            {
                Atp atp = new Atp(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
                );
                
                resultList.add(atp);
                map.put(atp.getId(), atp);
            }
            
            // Abort, if there are no results
            if ( map.size() == 0 )
            {
                return Collections.EMPTY_LIST;
            }
            
            // Fetch the options for the atp objects:            
            sql = "SELECT atp_id, option_name, option_value FROM atp_option WHERE atp_id = ANY(ARRAY[";
            for ( long id : map.keySet())
            {
                sql += id + ",";
            }
            sql = sql.substring(0, sql.length()-1) + "]) ORDER BY atp_id";
            
            rs = st.executeQuery(sql);
            while ( rs.next()) {
                map.get(rs.getLong(1)).setOption(rs.getString(2), rs.getString(3));
            }
            
            // Return the previously created list
            return resultList;
        
        } catch (SQLException exc) {
            throw new ModelException(exc);
        } finally {
            if ( connection != null ) {
                factory.close(connection);
            }
        }
    }
    
    public Atp get(long id) {
        String sql = "SELECT id, name, atp_name, atp_version " +
                     "FROM atp WHERE id = " + id;
        
        Connection connection = null;
        try
        {
            connection = factory.open();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if ( rs.next() )
            {
                Atp atp = new Atp(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
                );
                
                sql = "SELECT option_name, option_value FROM atp_option WHERE atp_id = " + atp.getId();
                rs = st.executeQuery(sql);
                while ( rs.next()) {
                    atp.setOption(rs.getString(1), rs.getString(2));
                }
                return atp;
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
    
    public void save(Atp atp) {
        if ( atp == null || atp.getId() != -1) {
            throw new IllegalArgumentException("Atp is null or already created!");
        }
        String sql = "INSERT INTO atp (id, name, atp_name, atp_version) " +
                     "VALUES (nextval('\"SEQ_QUEUEJOB_ID\"'),?,?,?)";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, atp.getName());
            pst.setString(2, atp.getAtpName());
            pst.setString(3, atp.getAtpVersion());
            
            if ( 1 == pst.executeUpdate()) {
                ResultSet keys = pst.getGeneratedKeys();
                if ( keys.next()) {
                    atp.setId(keys.getLong(1));
                    
                    storeDetails(con, atp);
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
    
    public void update(Atp atp) {
        if ( atp == null || atp.getId() == -1) {
            throw new IllegalArgumentException("Atp is null or not yet created!");
        }
        String sql = "UPDATE atp SET name = ?, atp_name = ?, atp_version = ? WHERE id = ?";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setString(1, atp.getName());
            pst.setString(2, atp.getAtpName());
            pst.setString(3, atp.getAtpVersion());
            pst.setLong(4, atp.getId());
            
            if ( 1 == pst.executeUpdate()) {
                
                storeDetails(con, atp);
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
    
    private void storeDetails(Connection con, Atp atp) throws SQLException {
        String sql = "DELETE FROM atp_option WHERE atp_id = " + atp.getId();
        Statement st = con.createStatement();
        st.executeUpdate(sql);
        st.close();
        
        PreparedStatement pst = con.prepareStatement(
            "INSERT INTO atp_option (atp_id, option_name, option_value) VALUES (?,?,?)"
        );
        for ( String key : atp.getOptions().keySet()) {
            pst.setLong(1, atp.getId());
            pst.setString(2, key);
            pst.setString(3, atp.getOption(key));
            pst.executeUpdate();
        }
    }
    
    public void delete(Atp atp) {
        if ( atp == null ) {
            return; // Everything is fine ,)
        }
        
        if (atp.getId() == -1) {
            throw new IllegalArgumentException("Atp is null or not created!");
        }
        String sql = "DELETE FROM atp WHERE id = ?";
        
        Connection con = null;
        try {
            con = factory.open();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setLong(1, atp.getId());
            if ( 1 == pst.executeUpdate()) {
                atp.setId(-1);
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