package atpportal.ui.listeners;

import javax.servlet.http.*;;
import javax.servlet.*;

import org.tptp.atp.*;
import org.tptp.model.ModelException;

import java.util.*;

/**
 * Application startup/shutdown listener, configured in the web.xml file.
 *
 * @see java.util.Timer
 * @see org.tptp.atp.QueueJobExecutorService
 */
public class ApplicationContextListener implements ServletContextListener
{
    private List<Timer> timers = new LinkedList<Timer>();
    
    /**
     * Application Startup Event
     */
    public void	contextInitialized(ServletContextEvent ce) {
	ServletContext sc = ce.getServletContext();
	
        // Parse the QueueWorker Parameter
	int queueworker = 2; // 2 has no special meaning, its just the fallback value if no value was provided
	String queueWorkerString = sc.getInitParameter("queue-worker");
	
	if ( queueWorkerString.equals("BY_PROCESSORS")) {
	    queueworker = Math.max(1, Runtime.getRuntime().availableProcessors());
	} else if ( null != queueWorkerString ) {
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
     * Called by the webcontainer when the application is shutdown. It stops the timers.
     */
    public void	contextDestroyed(ServletContextEvent ce) {
	for ( Timer t : timers) {
	    t.cancel();
	}
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