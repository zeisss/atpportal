package atpportal.ui.listeners;

import javax.servlet.http.*;;
import javax.servlet.*;

import org.tptp.model.*;
import org.tptp.model.jdbc.DefaultConnectionFactory;
import org.tptp.model.postgres.*;
import org.tptp.atp.*;

import java.util.*;

/**
 * Application startup/shutdown listener, configured in the web.xml file.
 * It installs the @link{org.tptp.model.RepositoryFactory}, so the DataModel can
 * be used. On shutdown, it closes all database connection.
 *
 * @see org.tptp.model.RepositoryFactory
 * @see org.tptp.model.postgres.PostgresRepositoryFactory
 * @see org.tptp.model.postgres.DefaultConnectionFactory
 */
public class ModelInitializationListener implements ServletContextListener
{
    private DefaultConnectionFactory factory;
    
    /**
     * Application Startup Event
     */
    public void	contextInitialized(ServletContextEvent ce) {
        ServletContext sc = ce.getServletContext();
        
        Properties prop = new Properties();
	prop.setProperty("jdbc.string", sc.getInitParameter("org.tptp.model.postgres.string"));
	prop.setProperty("jdbc.class", sc.getInitParameter("org.tptp.model.postgres.class"));
	prop.setProperty("jdbc.username", sc.getInitParameter("org.tptp.model.postgres.username"));
	prop.setProperty("jdbc.password", sc.getInitParameter("org.tptp.model.postgres.password"));
	
        // TODO: Replace with a DataSource or sthg like that
        factory = new DefaultConnectionFactory(prop);
        
        // Initialize the model, so it finds the database backend
	RepositoryFactory.setInstance(
            new PostgresRepositoryFactory (factory)
        );
        
    }
    
    /**
     * Called by the webcontainer when the application is shutdown. It closes the database backend connection.
     */    
    public void	contextDestroyed(ServletContextEvent ce) {
	factory.shutdown();
    }
}