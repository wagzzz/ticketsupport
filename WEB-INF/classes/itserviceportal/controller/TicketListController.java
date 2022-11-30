package itserviceportal.controller;

import itserviceportal.model.beans.*;
import itserviceportal.model.datalayer.*;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
import java.lang.NumberFormatException;
import javax.servlet.http.*;
import javax.servlet.*;

/**
 * TicketListController
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class TicketListController extends HttpServlet {

	/**
	 * Display all tickets the user has access to.
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

		// Get List of all Support Tickets the user is allowed to view
		ArrayList<SupportTicket> tickets = getTickets(user, "all", "all", false, "newest");

		// If tickets is null send back to portal with error message
		if (tickets == null) {
			session.setAttribute("errorMessage", "Sorry! Support tickets are unavailable.");
			response.sendRedirect("ServicePortal");
			return;
		
		// If no tickets display error message
		} else if (tickets.isEmpty()) {
			session.setAttribute("errorMessage", "There are no tickets to display.");
		}

		// Attach tickets to the request to be forwarded to the jsp
		request.setAttribute("tickets", tickets);

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Jsp.TICKETLIST.url());
		dispatcher.forward(request, response);
	}

	/**
	 * Display all tickets the user has access to by sort criteria.
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

		// Get sort criteria
		String categorySelect = request.getParameter("categorySelect");
		String stateSelect = request.getParameter("stateSelect");
		String orderSelect = request.getParameter("orderSelect");

		// If no sort criteria send to portal with error
		if (categorySelect == null || stateSelect == null || orderSelect == null) {
			session.setAttribute("errorMessage", "Sorry! We could not sort tickets");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Get List of Support Tickets the user is allowed to view matching criteria
		List<SupportTicket> tickets = getTickets(user, categorySelect, stateSelect, false, orderSelect);

		// If tickets is null send back to portal with error message
		if (tickets == null) {
			session.setAttribute("errorMessage", "Sorry! Support tickets are unavailable.");
			response.sendRedirect("ServicePortal");
			return;
		
		// If no tickets display error message
		} else if (tickets.isEmpty()) {
			session.setAttribute("errorMessage", "There are no tickets to display.");
		}

		// Attach tickets to the request to be forwarded to the jsp
		request.setAttribute("tickets", tickets);

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Jsp.TICKETLIST.url());
		dispatcher.forward(request, response);
	}

	/**
	 * Get List of all Support Tickets the user is allowed to view
	 *
	 * @param user User 
	 * @param categorySelect String
	 * @param stateSelect String
	 * @param knowledgeBase boolean
	 * @param orderBy String
	 * @throws ServletException
	 * @throws IOException
	 * @return ArrayList< SupportTicket> or null if error retrieving tickets
	 */ 
	public ArrayList<SupportTicket> getTickets(User user, String categorySelect, String stateSelect, boolean knowledgeBase, String orderBy) {
		try {
			// Calling the Ticket Data Access to retrieve all the tickets from the database
			TicketDataAccess ticketDAL = new TicketDataAccess();
			ArrayList<SupportTicket> ticketList = ticketDAL.getAllTicketsFromDB(user, stateSelect, categorySelect, false, orderBy);
			return ticketList;
		} catch (Exception e) {
			return null;
		}
	}
}

