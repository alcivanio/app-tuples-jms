package models;
import java.rmi.RemoteException;

import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

public class DispositiveCounter implements Entry{
	private static final long serialVersionUID = 1L;
	
	public Integer count;
	
	
	public DispositiveCounter() {}
	
	
	public static DispositiveCounter get(JavaSpace jSpace) {
		DispositiveCounter template = new DispositiveCounter();
		
		try {
			DispositiveCounter dc = (DispositiveCounter) jSpace.read(template, null, 60 * 10);
			if (dc == null) {
				dc = new DispositiveCounter();
				dc.count = 0;
			}
			
			return dc;
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
		
		return null;
	}
	
	
	public void more(JavaSpace space) {
		DispositiveCounter template = new DispositiveCounter();
		
		try {
			DispositiveCounter dc = (DispositiveCounter) space.take(template, null, 60 * 10);
			if (dc == null) {
				dc = new DispositiveCounter();
				dc.count = 0;
			}
			
			dc.count++;
			space.write(dc, null, 60 * 1000);
			count = dc.count;
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
	}
	
	
	public static void clear(JavaSpace jSpace) {
		DispositiveCounter template = new DispositiveCounter();
		
		try {
			DispositiveCounter dc = (DispositiveCounter) jSpace.takeIfExists(template, null, 60 * 10);
			while(dc != null) {
				dc = (DispositiveCounter) jSpace.takeIfExists(template, null, 60 * 10);
			}
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }		
	}
	
	
	
	
	
	
	
	
}
