package itserviceportal.controller;

import itserviceportal.model.beans.*;
import itserviceportal.model.datalayer.*;
import java.io.*;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * LogoutController
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class LogoutController extends HttpServlet{

	/**
	 * Log the user out and display the login page
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Check to see if the user is currently already inside the session
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// If the user is not null then perform the logout
		if (user != null)
		{
			// Destory and create new session with message to inform the user
			session.invalidate();
			request.getSession().setAttribute("successMessage", "You've been logged out!");
			// Redirect user to login page
			response.sendRedirect(request.getContextPath() + "/");
		}

		// The user is not logged in so display the login page
		else
		{
			session.setAttribute("errorMessage", "No user logged in!");
			response.sendRedirect(request.getContextPath() + "/");
		}	
	}

	/**
	 * There is no POST for this controller. Perform GET
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}