package itserviceportal.controller;

import itserviceportal.model.beans.*;
import itserviceportal.model.datalayer.*;
import java.io.*;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * LoginController
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class LoginController extends HttpServlet {

	/**
	 * Display Login Page
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
		
		// If user already logged in send to portal
		if(user != null) {
			response.sendRedirect("ServicePortal");
		} else {
			// Display the login page
			RequestDispatcher requestDispatcher; 
			requestDispatcher = request.getRequestDispatcher(Jsp.INDEX.url());
			requestDispatcher.forward(request, response);
		}	
	}

	/**
	 * Log the user into the IT Service Portal
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Get session
		HttpSession session = request.getSession();

		// Check if user already logged in
		User user = (User) session.getAttribute("user");
		if (user != null) {
			response.sendRedirect("ServicePortal");
			return;
		}

		// Get the data posted by the user
		String username = request.getParameter("email");
		String password = request.getParameter("password");

		// Calling the Data access layer to get the user from the database
		try
		{
			UserDataAccess userDAO = new UserDataAccess();
			user = userDAO.loginUser(username, password);
		
			// If the user is null then the user did not login correctly with a valid account
			if (user == null)
			{
				session.setAttribute("errorMessage", "Your username or password is incorrect. Please try again.");
				response.sendRedirect(request.getContextPath() + "/");
			}
			// Otherwise, the user successfully logged in
			else
			{
				// Add user into session
				session.setAttribute("user", user);
				// Add user notifications into the session
				SessionListener.updateActiveUserNotifications(user.getUserID());
				// Send logged in user to Service Portal
				response.sendRedirect("ServicePortal");
			}
		}

		// If any error occured display an error message to the user
		catch (Exception e)
		{
			session.setAttribute("errorMessage", "Sorry! Something went wrong while trying to log you in.");
			response.sendRedirect(request.getContextPath() + "/");
		} 
	}
}