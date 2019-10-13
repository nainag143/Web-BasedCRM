package org.com.onepointone.requesthandler;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import com.genesyslab.ail.AilLoader;

/**
 * Servlet implementation class MainCtiRequestHandler
 */

public class MainCtiRequestHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static String agentid; 
	public static String extension;
	public static String phone;
	StartTserverThread obj;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainCtiRequestHandler() {
        super();
        // TODO Auto-generated constructor stub
        //obj= new StartTserverThread();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		dispatch("/index.jsp",request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
	
		
		
		doGet(request, response);
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
