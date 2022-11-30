package itserviceportal.controller;

import itserviceportal.model.beans.*;
import itserviceportal.model.datalayer.*;
import java.io.*;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * TimeoutController
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @lastModified: 18-05-2018
 */

public class TimeoutController extends HttpServlet{

	/**
	 * Send to doPost
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * Redirect a timed out user to login page with error message
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Check to see if the user is currently already inside the session
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// If the user is not null then the user is already logged in, direct to the portal
		if (user != null) {
			response.sendRedirect("ServicePortal");

		// Direct user to login page with errorMessage
		} else {
			String errorMessage = request.getParameter("errorMessage");
			if (errorMessage != null) {
				session.setAttribute("errorMessage", errorMessage);
			}
			response.sendRedirect(request.getContextPath() + "/");
		}
	}
}