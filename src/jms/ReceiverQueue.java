package jms;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
 
import javax.jms.*;
import javax.naming.*;

import org.exolab.jms.administration.AdminConnectionFactory;
import org.exolab.jms.administration.JmsAdminServerIfc;

import models.Channel;

 
public class ReceiverQueue {
     
	//private Integer channelId;
	//public ArrayList<String> messages;
	
	public ReceiverQueue(Integer channelId) {
		//this.channelId	 = channelId;
		//messages			 = new ArrayList<String>();
		//startReading();
	}
	
	private void startReading() {
		Thread check = new Thread(new Runnable() {			
			@Override
			public void run() {
				//readMessage();
			}
		});
		
		
		check.start();
	}
	
	
	/*
     public void readMessage() {
    	 
    	 	try {

    	 		String url = "tcp://localhost:3035/";
        	    JmsAdminServerIfc admin = AdminConnectionFactory.create(url);
        	    
        	    //check for a destination, if there is no destinations, just create a new one
        	    String destinationId = "dest" + channelId;
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
	
			qconnection.start();
	
		    javax.jms.Queue dest 	= (javax.jms.Queue)context.lookup(destinationId);
	        	QueueReceiver qreceiver 	= qsession.createReceiver(dest);
	         
	                
			TextMessage textMessage = null;
	
			while (true) {
	               textMessage = (TextMessage) qreceiver.receive();
	               messages.add(textMessage.getText());
		           System.out.println("Mensagem Recebida: " + textMessage.getText());  
			}
			
			 
         }catch(Exception e){
             e.printStackTrace();
         }       
     }   */
     
     
     
     
     
     
     
     
     public static ArrayList<String> readMessages(Channel fromChannel) {
    	 
    	 	try {
    	 		//creating the channel if necessary
    	 		String url = "tcp://localhost:3035/";
        	    JmsAdminServerIfc admin = AdminConnectionFactory.create(url);
        	    
        	    //check for a destination, if there is no destinations, just create a new one
        	    String destinationId = "dest" + fromChannel.id.intValue();
        	    if(!admin.destinationExists(destinationId)) {
        	    		if(!admin.addDestination(destinationId, Boolean.TRUE)) {
        	    			System.out.println("failed to create queue " + destinationId);
        	    		}
        	    }
        	    
    	 		
    	 		Hashtable properties = new Hashtable();
			properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.exolab.jms.jndi.InitialContextFactory");
			properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
    	 		Context context = new InitialContext(properties);
    		
			
			ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
			javax.jms.Queue dest 	= (javax.jms.Queue)context.lookup(destinationId);	
			Connection connection = factory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			QueueBrowser browser = session.createBrowser(dest);
			
			connection.start();
			
			ArrayList<String>newMessages = new ArrayList<>();
			
			Enumeration messages = browser.getEnumeration();
			while(messages.hasMoreElements()) {
				Message message = (Message)messages.nextElement();
				if (message instanceof TextMessage) {
					TextMessage tm = (TextMessage) message;
					newMessages.add(tm.getText());
				}
			}
				
			connection.close();	
			
			return newMessages;
				
			} catch (NamingException | JMSException | MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
     }
     
     
     
     
     
     
     
     
     
     
     
     
 }
