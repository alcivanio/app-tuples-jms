package models;
import java.rmi.RemoteException;

import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;

import net.jini.space.JavaSpace;

public class ChannelCounter implements Entry{
	private static final long serialVersionUID = 1L;
	
	public Integer count;	
	
	public ChannelCounter() {}
	
	
	public ChannelCounter(JavaSpace space) {}
	
	
	public static ChannelCounter get(JavaSpace jSpace) {
		ChannelCounter template = new ChannelCounter();
		
		try {
			ChannelCounter c = (ChannelCounter) jSpace.read(template, null, 60 * 10);
			if (c == null) {
				c = new ChannelCounter();
				c.count = 0;
			}
			
			return c;
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
		
		return null;
	}
	
	
	public void more(JavaSpace space) {
		ChannelCounter template = new ChannelCounter();
		
		try {
			ChannelCounter c = (ChannelCounter) space.take(template, null, 60 * 10);
			if (c == null) {
				c = new ChannelCounter();
				c.count = 0;
			}
			
			c.count++;
			space.write(c, null, 60 * 1000);
			count = c.count;
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
	}
	
	
	public static void clear(JavaSpace jSpace) {
		ChannelCounter template = new ChannelCounter();
		
		try {
			ChannelCounter c = (ChannelCounter) jSpace.takeIfExists(template, null, 60 * 10);
			while(c != null) {
				c = (ChannelCounter) jSpace.takeIfExists(template, null, 60 * 10);
			}
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }		
	}
	
	
	
}
