package org.com.onepointone.requesthandler;

import java.io.*;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import org.com.onepointone.bean.HomeBean;
import org.com.onepointone.dao.HomeDao;
 
public class HomeRequestHandler extends HttpServlet{
	 protected void doGet(HttpServletRequest req, HttpServletResponse res)
		        throws ServletException, IOException{
		 /*HomeBean homeBean = new HomeBean() ;
		        List list=homeBean.dataList();

		        req.setAttribute("data", list);
		        RequestDispatcher rd = req.getRequestDispatcher("/jsp/beandata.jsp");
		        rd.forward(req, res);
		  */  }
}