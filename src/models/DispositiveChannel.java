package models;
import java.rmi.RemoteException;

import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

public class DispositiveChannel implements Entry {
	private static final long serialVersionUID = 1L;
	
	public Integer deviceId;
	public Integer channelId;
	
	
	public DispositiveChannel() {}
	
	
	public DispositiveChannel(Integer deviceId, Integer channelId) {
		this.deviceId 	= deviceId;
		this.channelId 	= channelId;
	}
	
	public static void clear(JavaSpace jSpace) {
		DispositiveChannel template = new DispositiveChannel();
		
		try {
			DispositiveChannel c = (DispositiveChannel) jSpace.takeIfExists(template, null, 60 * 10);
			while(c != null) {
				c = (DispositiveChannel) jSpace.takeIfExists(template, null, 60 * 10);
			}
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }		
	}
	
	
}
