package models;
import java.rmi.RemoteException;
import java.util.ArrayList;

import jms.SenderQueue;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
public class Dispositive implements Entry {
	private static final long serialVersionUID = 1L;

	public Integer	id;
	public String 	name;
	
    public Dispositive(int id, String name) {
    		this.id 		= id;
    		this.name 	= name;
    }
    
    public Dispositive() {}
    
    
    public static void add(Dispositive c, Channel channel, JavaSpace space, DispositiveCounter dc) {
		try {
			DispositiveChannel dcr = new DispositiveChannel();
			dcr.channelId = channel.id;
			dcr.deviceId = dc.count;
			
			space.write(dcr, null, 60*1000);
			space.write(c, null, 60 * 1000);
			dc.more(space);
		} catch (RemoteException | TransactionException e) { e.printStackTrace(); }
	}
	
	
	public static void remove(Dispositive template, JavaSpace space) {
		try {
			//removing the reference of the dispositive / channel
			DispositiveChannel dcTemplate 	= new DispositiveChannel();
			dcTemplate.deviceId 				= template.id;
			DispositiveChannel rdc 			= (DispositiveChannel) space.take(dcTemplate, null, 60 * 1000);
			System.out.println("Referencia de canal: " + rdc.channelId + " ref de dispositivo: " + rdc.deviceId + " was removed");
			
			//removing the channel itself
			Dispositive c = (Dispositive) space.take(template, null, 60 * 1000);
			System.out.println(c.name + " was removed");
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
	}
	
	
	public static void move(Dispositive dispositive, Channel fromChannel, Channel toChannel, JavaSpace space) {
		try {
			DispositiveChannel dcTemplate 	= new DispositiveChannel();
			dcTemplate.deviceId 				= dispositive.id;
			DispositiveChannel rdc 			= (DispositiveChannel) space.takeIfExists(dcTemplate, null, 60 * 10);
			
			String outMessage = "OUT: " + dispositive.name;
			SenderQueue.sendMessage(fromChannel, outMessage);
			
			if(rdc == null) { rdc = new DispositiveChannel(); }
			
			rdc.channelId = toChannel.id;
			space.write(rdc, null, 60 * 1000);

			String inMessage = "IN: " + dispositive.name;
			SenderQueue.sendMessage(toChannel, inMessage);
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
	}
	
	
	public static ArrayList<Dispositive> getAll(Channel fromChannel, JavaSpace space) {
		try {
			ArrayList<Integer> idsForChannel = idsForChannel(fromChannel.id, space);
			ArrayList<Dispositive> dispositives = new ArrayList<>();
			Dispositive template = new Dispositive();
			
			for (int i=0; i<idsForChannel.size(); i++) {
				template.id = idsForChannel.get(i);
				Dispositive d = (Dispositive) space.takeIfExists(template, null, 60 * 10);
				if (d != null) {
					dispositives.add(d);
				}
			}
			
			for (Dispositive dsp : dispositives) {
				space.write(dsp, null, 60 * 1000);
			}
			
			return dispositives;
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
		
		return null;
	}
	
	
	private static ArrayList<Integer> idsForChannel(Integer channelId, JavaSpace space) {
		try {
			ArrayList<Integer> ids = new ArrayList<>();
			ArrayList<DispositiveChannel> auxList = new ArrayList<>();
			DispositiveChannel template = new DispositiveChannel();
			template.channelId = channelId;
			DispositiveChannel dc = (DispositiveChannel) space.takeIfExists(template, null, 60 * 10);
			
			while(dc != null) {
				ids.add(dc.deviceId);
				auxList.add(dc);
				dc = (DispositiveChannel) space.takeIfExists(template, null, 60 * 10);
			}
			
			for (DispositiveChannel dsc : auxList) {
				space.write(dsc, null, 60 * 1000);
			}
			
			return ids;
			
		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
		
		return null;
		
	}
	
	
	public static void clear(JavaSpace space) {
		try {
			Dispositive template 	= new Dispositive();
			Dispositive c 			= (Dispositive) space.takeIfExists(template, null, 60 * 10);
			
			while(c != null) {
				c = (Dispositive) space.takeIfExists(template, null, 60 * 10);
			}

		} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) { e.printStackTrace(); }
	}
	
}
