package org.com.onepointone.requesthandler;
 
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.com.onepointone.bean.LoginBean;
import org.com.onepointone.dao.DBConnection;
import org.com.onepointone.dao.LoginDao;
import org.com.onepointone.dao.UtilDao;

/**
 * Servlet implementation class LoginController
 */
public class LoginServelet extends HttpServlet {
	LoginDao loginDao=new LoginDao();
	LoginBean loginBean=null;
	UtilDao dao=new UtilDao();
	 int agentid=0 ;
	 int  extension=0 ;
	 
	 public static ConcurrentHashMap<String, Integer> loginIdMap =new ConcurrentHashMap<String ,Integer>();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String un=request.getParameter("username");
		String pw=request.getParameter("password");
		 
		boolean biostatus=dao.getBioMatrixStatus(un);
		
		 if(biostatus){
			 System.out.println("bio satatus true");
			 loginBean = new LoginBean() ;
			 loginBean=dao.loginCheck(un);
			 System.out.println("FLAG : "+loginBean.toString());
				System.out.println("jayYyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"+request.getRemoteAddr());

		 if(loginBean.flag)
		 {
			 System.out.println("Login flag true");
			 
				System.out.println("jayYyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"+request.getRemoteAddr());

			String dn=dao.getExtInfo(request.getRemoteHost());
			
			HttpSession session=request.getSession();  
	        session.setAttribute("loginBean",loginBean); 
	        session.setAttribute("DN", dn);
	        agentid=Integer.parseInt( loginBean.getAgentid()) ;
	        extension=Integer.parseInt(dn) ;
	        StartTserverThread.extn=dn;
	        StartTserverThread.RegisterExtension(dn);
			if(StartTserverThread.AgentLogin(dn,loginBean.getAgentid()))
			{
			StartTserverThread.AgentReady(dn);
            // dao.agentLogin(agentname,PangeumLogin.opoid , "LOGIN", DN, String.valueOf(agentid), processname, PangeumLogin.hostname);
			dao.agentLogin(loginBean.getAgentname(),loginBean.getUsername() , "LOGIN", dn, String.valueOf(loginBean.getAgentid()), DBConnection.processname, request.getRemoteAddr());
				
			System.out.println("jayYyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy"+request.getRemoteAddr());
			int loginid=dao.createLoginLogoutLogLog(loginBean,dn,request.getRemoteAddr());			
			
			loginIdMap.put(dn,loginid);
StartTserverThread.agentNameMap.put(dn, loginBean.getAgentname());

			StartTserverThread.opoMap.put(dn,un);
			dispatch("/home.jsp",request,response);
			
			}

			else{
				 response.sendRedirect("/");
			}
			
			
		 }
		 else{}
		 
		 }
		
		 
		else{
		    response.sendRedirect("/");
		}
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

 
