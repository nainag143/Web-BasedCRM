package org.com.onepointone.requesthandler;

import java.awt.Color;
import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.genesyslab.platform.commons.protocol.Endpoint;

import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.commons.protocol.RegistrationException;
import com.genesyslab.platform.voice.protocol.TServerProtocol;
import com.genesyslab.platform.voice.protocol.tserver.AddressType;
import com.genesyslab.platform.voice.protocol.tserver.AgentWorkMode;
import com.genesyslab.platform.voice.protocol.tserver.ControlMode;
import com.genesyslab.platform.voice.protocol.tserver.MakeCallType;
import com.genesyslab.platform.voice.protocol.tserver.RegisterMode;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAbandoned;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentLogin;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentLogout;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentNotReady;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAgentReady;
import com.genesyslab.platform.voice.protocol.tserver.events.EventAttachedDataChanged;
import com.genesyslab.platform.voice.protocol.tserver.events.EventDNBackInService;
import com.genesyslab.platform.voice.protocol.tserver.events.EventDNOutOfService;
import com.genesyslab.platform.voice.protocol.tserver.events.EventDestinationBusy;
import com.genesyslab.platform.voice.protocol.tserver.events.EventDialing;
import com.genesyslab.platform.voice.protocol.tserver.events.EventError;
import com.genesyslab.platform.voice.protocol.tserver.events.EventEstablished;
import com.genesyslab.platform.voice.protocol.tserver.events.EventHeld;
import com.genesyslab.platform.voice.protocol.tserver.events.EventNetworkReached;
import com.genesyslab.platform.voice.protocol.tserver.events.EventOffHook;
import com.genesyslab.platform.voice.protocol.tserver.events.EventOnHook;
import com.genesyslab.platform.voice.protocol.tserver.events.EventReleased;
import com.genesyslab.platform.voice.protocol.tserver.events.EventRetrieved;
import com.genesyslab.platform.voice.protocol.tserver.events.EventRinging;
import com.genesyslab.platform.voice.protocol.tserver.events.EventUserEvent;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentLogin;
import com.genesyslab.platform.voice.protocol.tserver.requests.agent.RequestAgentReady;
import com.genesyslab.platform.voice.protocol.tserver.requests.dn.RequestRegisterAddress;
import com.genesyslab.platform.voice.protocol.tserver.requests.party.RequestMakeCall;

import org.apache.log4j.Logger;
import org.com.onepointone.dao.DBConnection;

import webphone.*;
/**
 * Servlet implementation class JavaCtiRequestHandler
 */

public class JavaCtiRequestHandler extends HttpServlet {
	/**
	 * 
	 */
	final  static Logger logger = Logger.getLogger(JavaCtiRequestHandler.class);
	private static final long serialVersionUID = 3613508655455407893L;
	public static Message iMessage;
	public static String extn;
	Thread receiver;
	 public static TServerProtocol tServerProtocol = null;
	public static boolean isRunning;
	public static boolean isConnectionOpen;
	
	SIPNotifications sipnotifications=null;
	StartTserverThread obj=null;
	
	//Thread receiver;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JavaCtiRequestHandler() {
        super();
        // TODO Auto-generated constructor stub
        
    }

    
    public void init() throws ServletException {
      
    	setParameters();
    	
    	obj= new StartTserverThread();
    	
    	obj.CreateConnection("10.10.100.14",7000);

    	Thread t=new Thread(obj);
    	t.start();
    	System.out.println("After Thread Start");
    	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request,response);
	

	}
	protected void dispatch( String aURI, HttpServletRequest aoRequest, HttpServletResponse aoResponse )
	throws javax.servlet.ServletException, java.io.IOException
{
	RequestDispatcher rd = getServletContext().getRequestDispatcher( aURI );
	if ( rd == null )
	{
		rd = getServletContext().getRequestDispatcher("/errorPage.jsp");
	}
	rd.forward( aoRequest, aoResponse );
	return;

}
	
public static void setParameters()
	{
		try
		{
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			//System.out.println("DB Server Initialized Successfully....");
			DBConnection.initialize();
			//UtilDao.getHostName();
			//System.out.println("DB Server Initialized Successfully....");
			logger.info("DB Server Initialized Successfully....");
			System.out.println("DB Server Initialized Successfully....");
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

		}
		catch(Exception e)
		{
			System.out.println("["+new Date()+"] Caught Exception In Mizutech.setParameters()...\t"+e.getMessage());
			logger.error("["+new Date()+"] Caught Exception In Mizutech.setParameters()...\t"+e.getMessage());
		}
	}
}
