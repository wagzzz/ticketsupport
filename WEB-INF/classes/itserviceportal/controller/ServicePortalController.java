package itserviceportal.controller;

import itserviceportal.model.beans.*;
import itserviceportal.model.datalayer.*;
import java.io.*;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * ServicePortalController
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class ServicePortalController extends HttpServlet{

	/**
	 * Display the Portal Page
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Get user from session
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// If staff role send to staff portal
		if(user.getRole() == Role.STAFF)
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Jsp.STAFFPORTAL.url());
			dispatcher.forward(request, response);
		}
		// Otherwise send to user portal
		else
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Jsp.USERPORTAL.url());
			dispatcher.forward(request, response);
		}
	}

    //No POST, perform GET if POST OCCURS
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}