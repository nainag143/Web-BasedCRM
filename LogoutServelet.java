package org.com.onepointone.requesthandler;
 
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.com.onepointone.bean.LoginBean;
import org.com.onepointone.dao.UtilDao;

import com.genesyslab.ail.driver.voicesdk.TlibDriverVoiceSdk.Session;

import ws.server.WsServer;

/**
 * Servlet implementation class LoginController
 */
public class LogoutServelet extends HttpServlet {
	
	 
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
			
			 	HttpSession session=request.getSession();  
		        
			 	LoginBean bean=(LoginBean)session.getAttribute("loginBean");
			 	String dn=(String)session.getAttribute("DN");
			 	
			 	
		        
		        
		        StartTserverThread.logOut_Ext(bean,dn,request.getRemoteAddr().toString());
		       
		        WsServer.userMap.remove(bean.getExtension());
		    
		        
		        UtilDao dao=new UtilDao();
		        
		        dao.update_endtime_in_loginlogoutlog(LoginServelet.loginIdMap.get(dn));
		        
//		        WsServer.webSocketSession.close();
		        
		        /*
		         * remove extn from map
		         * web socket connection close
		         */
		        //session.invalidate();
		        //session.removeAttribute("extension");
		        response.sendRedirect("/OpoWeb");
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

}

 
