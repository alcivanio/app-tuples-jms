package jms;

import java.io.*;
import java.util.*;
import javax.jms.*;
import javax.naming.*;

import org.exolab.jms.administration.AdminConnectionFactory;
import org.exolab.jms.administration.JmsAdminServerIfc;

import models.Channel;

public class SenderQueue{
    
	
	
	
	
    public static void sendMessage(Channel toChannel, String mes){

     try{

    	 	//creating the channel if necessary
	 	String url = "tcp://localhost:3035/";
 	    JmsAdminServerIfc admin = AdminConnectionFactory.create(url);
 	    
 	    //check for a destination, if there is no destinations, just create a new one
 	    String destinationId = "dest" + toChannel.id.intValue();
 	    if(!admin.destinationExists(destinationId)) {
 	    		if(!admin.addDestination(destinationId, Boolean.TRUE)) {
 	    			System.out.println("failed to create queue " + destinationId);
 	    		}
 	    }
    	 
    	 
    	 
		Hashtable properties = new Hashtable();
		properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.exolab.jms.jndi.InitialContextFactory");
		properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

		Context context = new InitialContext(properties);

		QueueConnectionFactory qfactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");

		QueueConnection qconnection = qfactory.createQueueConnection();
		QueueSession qsession = qconnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

		TextMessage message = qsession.createTextMessage();
		message.setText(mes);
      
		javax.jms.Queue dest = (javax.jms.Queue) context.lookup(destinationId);
		QueueSender sender = qsession.createSender(dest);
		sender.send(message);
        
		context.close();
		qconnection.close();

        
    }catch(Exception e){
      e.printStackTrace();
    }
 }
}
