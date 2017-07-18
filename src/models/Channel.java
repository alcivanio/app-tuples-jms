package models;
import java.rmi.RemoteException;
import java.util.ArrayList;

import classes.Generals;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;
public class Channel implements Entry {
	private static final long serialVersionUID = 1L;

	public Integer  id;
	public String	name;
	
	public Channel() {}
	
	
	public Channel(int id, String name) {
		this.name 	= name;
		this.id 		= id;
	}
	
	public static void add(Channel c, JavaSpace space, ChannelCounter cc) {
		try {
			space.write(c, null, 60 * 1000);
			cc.more(space);
		} catch (RemoteException | TransactionException e) { e.printStackTrace(); }
	}
	
	
	public static void remove(Channel template, JavaSpace space) {
		try {
			Channel c = (Channel) space.take(template, null, 60 * 1000);
			System.out.println(c.name + " was removed");
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
	}
	
	
	/*public static ArrayList<Channel> getAll(JavaSpace space) {
		
		
		try {
		
			TransactionManager mgr = SpaceUtils.getManager();
			Transaction.Created transaction = TransactionFactory.create(mgr, 60*1000);
			Channel template = new Channel();
			ArrayList<Channel> channels = new ArrayList<>();
			Channel c;
			
			do {
				c = (Channel) space.take(template, (Transaction) transaction, 60 * 1000);
				if (c != null) {
					channels.add(c);
				}
			} while(c != null);
			
			return channels;
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException | LeaseDeniedException e) { e.printStackTrace(); }
		
		return null;
	}*/
	
	
	public static ArrayList<Channel> getAll(JavaSpace space) {
		
		try {
			ArrayList<Channel> channels = new ArrayList<>();
			Channel template = new Channel();
			Channel c = (Channel) space.take(template, null, 60 * 10);
			
			while(c != null) {
				channels.add(c);
				c = (Channel) space.take(template, null, 60 * 10);
			}
			
			for (Channel ch : channels) {
				space.write(ch, null, 60 * 1000);
			}
			
			return channels;
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
		
		return null;
	}
	
	
	public static void clear(JavaSpace space) {
		try {
			Channel template 	= new Channel();
			Channel c 			= (Channel) space.takeIfExists(template, null, 60 * 10);
			
			while(c != null) {
				c = (Channel) space.takeIfExists(template, null, 60 * 10);
			}

		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
	}
	
	
	
}
