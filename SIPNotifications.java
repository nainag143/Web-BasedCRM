package org.com.onepointone.requesthandler;



import java.net.DatagramSocket;

import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import org.apache.log4j.Logger;
import org.com.onepointone.dao.DBConnection;
import com.genesyslab.platform.commons.connection.tls.IPAddressUtil;


/**
 * This class will just create an UDP listener to receive the notifications from JVoIP
 */
public class SIPNotifications extends Thread
{
	final static Logger logger = Logger.getLogger(SIPNotifications.class);
   boolean terminated = false;
   byte[] buf = null;
   DatagramSocket socket = null;
   DatagramPacket packet = null;

   /**
   * ctor
   */
   public SIPNotifications()
   {

   }

   /**
   *Start listening on UDP 19421
   */
   public boolean Start()
   {
       try{
           //socket = new DatagramSocket(19421);  //51100
    	  // InetAddress ip = InetAddress.getLocalHost();
    	   System.out.println("Sip Class start called + sip notifications started");
    	   InetSocketAddress localaddress = new InetSocketAddress("172.18.16.218", 5060);
    	  
    	   socket = new DatagramSocket(localaddress);
           buf = new byte[3600];
           packet = new DatagramPacket(buf, buf.length);
           terminated = false;
           this.start();
           logger.info("sip notifications started");
           return true;
       }
       catch(SocketException e)
       {
    	   System.out.println("SocketException at SIPNotifications Start: "+e.getMessage()+"\r\n"+e.getStackTrace()); 
       }
       
       catch(Exception e)
       {
    	   System.out.println("Exception at SIPNotifications Start: "+e.getMessage()+"\r\n"+e.getStackTrace()); 
    	   
       }
       return false;
   }

   /**
   * signal terminate and close the socket
   */
   public void Stop()
   {
       try{
           terminated = true;
           if (socket != null)
               socket.close();
           //a socket close exception might be raised here which is safe to catch and hide
       }catch(Exception e) { }
   }

   /**
   * blocking read in this thread
   */
   public void run()
   {
        try{
            while (!terminated) {
                packet.setData(buf, 0, buf.length);
                packet.setLength(buf.length);
                socket.receive(packet);
                if (packet != null && packet.getLength() > 0) {
                    String str = new String(packet.getData(), 0,
                                            packet.getLength());
                    ProcessNotifications(str);
                }
            }
        }catch(Exception e)
        {
           if(!terminated)  logger.error("Exception at SIPNotifications run: "+e.getMessage()+"\r\n"+e.getStackTrace());
        }

    }


    /**
    * all messages from JVoIP will be routed to this function.
    * parse and process them after your needs regarding to your application logic
    */

    public void ProcessNotifications(String msg)
    {
    	
        try
        {
        	//msg.toString();
           System.out.println("\t REC FROM JVOIP: " + msg);
            boolean retval = msg.contains("EventEstablished");
            if(retval == true)
            {
            	System.out.println("\t REC FROM Event: " + msg);
            }

            //frame.jTextArea1.append(msg);
        }
        catch(Exception e) {
        	logger.error("Exception at SIPNotifications ProcessNotifications: "+e.getMessage()+"\r\n"+e.getStackTrace()); 
        	}
       //todo: process notifications here (change your user interface or business logic depending on the sipstack state / call state)
    }
}

