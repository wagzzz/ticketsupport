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
 * SuggestionController
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class SuggestionController extends HttpServlet {

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

		// Check if user is allowed to access Suggestion
		String referer = request.getHeader("referer");
		if (referer == null) {
			response.sendRedirect("ServicePortal");
			return;
		} else {
			// Get servlet name user requested suggestion from
			if (referer.endsWith("/")) {
				referer = referer.substring(0, referer.length()-1);
			}
			if (referer.indexOf('?') > -1) {
				referer = referer.substring(referer.lastIndexOf("/", referer.indexOf('?'))+1);
			} else {
				referer = referer.substring(referer.lastIndexOf("/")+1);
			}
			// Suggestions only accessable from report page
			if (!referer.equals("Report")) {
				response.sendRedirect("ServicePortal");
				return;
			}
		}

		// Get search term
		String term = request.getParameter("term");
		if (term != null && !term.isEmpty()) {
			// Get List of all suggested articles
			List<SupportTicket> suggestedArticles = getSuggestions(term);
			// Attach suggestions to the request to be forwarded to the jsp
			request.setAttribute("suggestedArticles", suggestedArticles);
		} else {
			response.sendRedirect("ServicePortal");
			return;
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Jsp.SUGGESTEDARTICLES.url());
		dispatcher.forward(request, response);
	}

	/**
	 * Call doGet
	 *
	 * @param request a http servlet request
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Get List of all similar Articles(Support Tickets) in the knowledge base
	 *
	 * @param term string
	 * @return ArrayList< SupportTicket> list of suggested articles
	 */ 
	public ArrayList<SupportTicket> getSuggestions(String term) {
		try {
			// Calling the Ticket Data Access to retrieve suggestions from the database
			TicketDataAccess ticketDAO = new TicketDataAccess();
			ArrayList<SupportTicket> suggestions = ticketDAO.getSuggestedArticles(term);
			return suggestions;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

