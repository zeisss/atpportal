package atpportal.ui.listeners;

import javax.servlet.http.*;;
import javax.servlet.*;

import org.tptp.model.*;
import org.tptp.model.postgres.*;
import org.tptp.atp.*;

import java.util.*;

/**
 * Application startup/shutdown listener, configured in the web.xml file.
 */
public class ApplicationContextListener implements ServletContextListener
{
    private DefaultConnectionFactory factory;
    private List<Timer> timers = new LinkedList<Timer>();
    
    /**
     * Application Startup Event
     */
    public void	contextInitialized(ServletContextEvent ce) {
        Properties prop = new Properties();
	ServletContext sc = ce.getServletContext();
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
	
	
	// Parse the QueueWorker Parameter
	int queueworker = 2;
	String queueWorkerString = sc.getInitParameter("queue-worker");
	
	if ( queueWorkerString.equals("BY_PROCESSORS")) {
	    queueworker = Math.max(1, Runtime.getRuntime().availableProcessors());
	} else {
	    try {
		queueworker = Integer.parseInt(queueWorkerString);
	    } catch (Exception exc) {
		sc.log("Unable to parse init-parameter 'queue-worker' (value: " + queueWorkerString +"); Using " + queueworker, exc);
	    }        
	}
	
	// Add n tasks executing the background jobs 
	for ( int i = 0; i < queueworker;i++) {
	    Timer t = new Timer("queuejob-executor-" + i, true);
	    
	    // TODO: This should be triggerable or sthg like that (A real QUEUE)
	    t.scheduleAtFixedRate(new QueueJobWorker(sc), i * 5000, 30*1000);
	    
	    timers.add(t);
	}
    }

    /**
     * Called by the webcontainer when the application is shutdown. It closes the database backend connection.
     */
    public void	contextDestroyed(ServletContextEvent ce) {
	for ( Timer t : timers) {
	    t.cancel();
	}
        factory.shutdown();
    }
    
    
    class QueueJobWorker extends TimerTask
    {
	private ServletContext sc;
	QueueJobWorker(ServletContext sc) {
	    this.sc = sc;
	}
	
	public void run() {
	    QueueJobExecutorService service = new QueueJobExecutorService();
	    // Execute this method as long as it returns true (if a proof has been executed, it returns true)
	    while (true) {
		try {
		    if (!service.executeNextJob())
			return;
		} catch(ModelException exc) {
		    this.sc.log("Error while executing next job", exc );
		}
	    }
	}
    
    }
}