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
 * KnowledgeBaseController
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class KnowledgeBaseController extends HttpServlet {

	/**
	 * Display Knowledge Base
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

		// Get List of all knowledge base articles
		List<SupportTicket> knowledgeBase = getKnowledgeBase(user, "all", "newest");

		// If knowledgeBase is null send back to portal with error message
		if (knowledgeBase == null) {
			session.setAttribute("errorMessage", "Sorry! Knowledge Base is unavailable.");
			session.setAttribute("errorMessage", "Invalid Request");
			response.sendRedirect("ServicePortal");
			return;
		
		// If knowledgeBase empty display error message
		} else if (knowledgeBase.isEmpty()) {
			session.setAttribute("errorMessage", "Knowledge Base is empty");
		}

		// Attach knowledgeBase to the request to be forwarded to the jsp
		request.setAttribute("knowledgeBase", knowledgeBase);

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Jsp.KNOWLEDGEBASE.url());
		dispatcher.forward(request, response);
	}

	/**
	 * Display Knowledge Base by sort criteria
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
		String orderSelect = request.getParameter("orderSelect");

		// If no sort criteria send to portal with error
		if (categorySelect == null || orderSelect == null) {
			session.setAttribute("errorMessage", "Sorry! We could not sort articles.");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Get List of all knowledge base articles matching criteria
		List<SupportTicket> knowledgeBase = getKnowledgeBase(user, categorySelect, orderSelect);

		// If tickets is null send back to portal with error message
		if (knowledgeBase == null) {
			session.setAttribute("errorMessage", "Sorry! Knowledge Base are unavailable.");
			response.sendRedirect("ServicePortal");
			return;
		
		// If no tickets display error message
		} else if (knowledgeBase.isEmpty()) {
			session.setAttribute("errorMessage", "There are no articles to display.");
		}

		// Attach tickets to the request to be forwarded to the jsp
		request.setAttribute("knowledgeBase", knowledgeBase);

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Jsp.KNOWLEDGEBASE.url());
		dispatcher.forward(request, response);
	}

	/**
	 * Get List of all Articles(Support Tickets) in the knowledge base
	 *
	 * @param user User
	 * @param categorySelect String
	 * @param orderBy String
	 * @return ArrayList< SupportTicket> or null if error retrieving knowledge base
	 */ 
	public ArrayList<SupportTicket> getKnowledgeBase(User user, String categorySelect, String orderBy) {
		try {
			//Calling the Ticket Data Access to retrieve all articles from the database
			TicketDataAccess ticketDAO = new TicketDataAccess();
			ArrayList<SupportTicket> knowledgeBase = ticketDAO.getAllTicketsFromDB(user, "all", categorySelect, true, orderBy);
			return knowledgeBase;
		} catch (Exception e) {
			return null;
		}
	}
}

