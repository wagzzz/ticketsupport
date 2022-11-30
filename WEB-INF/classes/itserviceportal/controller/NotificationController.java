package itserviceportal.controller;

import itserviceportal.model.beans.*;
import itserviceportal.model.datalayer.*;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
import java.lang.NumberFormatException;
import java.sql.SQLException;

import javax.servlet.http.*;
import javax.servlet.*;

/**
 * NotificationController
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 22-05-2018
 */

public class NotificationController extends HttpServlet {

	/**
	 * Dismisses a notification for a user
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		// Get user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Check valid action
		String action = request.getParameter("action");
		if (action == null) {
			response.sendRedirect("ServicePortal");
			return;
		} else if (action.equals("dismiss")) {

			// Check valid notifcationID
			int notificationID = 0;
			try {
				notificationID = Integer.parseInt(request.getParameter("notificationID"));
			} catch (Exception e) {
				response.sendRedirect("ServicePortal");
				return;
			}

			try {
				dismissNotification(user, notificationID, session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Send user back to page they requested to dismiss notification from
		String referer = request.getHeader("referer");
		if (referer == null) {
			response.sendRedirect("ServicePortal");
			return;
		}

		if (referer.endsWith("/")) {
			referer = referer.substring(0, referer.length()-1);
		}
		// Get servlet url-mapping and query string only
		String sendUserBack = referer.substring(referer.lastIndexOf("/", referer.indexOf('?'))+1);
		if (sendUserBack.isEmpty()) {
			sendUserBack = referer.substring(referer.lastIndexOf("/")+1);
		}
		response.sendRedirect(sendUserBack);
	}

	/**
	 * This method handles when a user clicks to view a notification.
	 * It dismisses the notification then redirects them to the updated support ticket.
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		// Get user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Check valid notifcationID
		int notificationID = 0;
		try {
			notificationID = Integer.parseInt(request.getParameter("notificationID"));
		} catch (Exception e) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Check valid ticketID
		int ticketID = 0;
		try {
			ticketID = Integer.parseInt(request.getParameter("ticketID"));
		} catch (Exception e) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		}

		try {
			dismissNotification(user, notificationID, session);
			// Send to TicketController to display Support Ticket
			response.sendRedirect("Ticket?ticketID=" + ticketID);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.setAttribute("errorMessage", "Sorry! Request is invalid.");
		response.sendRedirect("ServicePortal");
	}

	/**
	 * Removes a notification from both the database and session
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void dismissNotification(User user, int notificationID, HttpSession session) {
		try {
			// Delete notificaton from database
			NotificationDataAccess notificationDAO = new NotificationDataAccess();
			notificationDAO.dismissNotification(user.getUserID(), notificationID);

			// Remove the notification from any active sessions the user has
			SessionListener.updateActiveUserNotifications(user.getUserID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

