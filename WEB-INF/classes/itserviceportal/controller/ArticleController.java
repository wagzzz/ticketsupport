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
 * ArticleController
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class ArticleController extends HttpServlet {

	/**
	 * Display Article
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

		// Get the ticket ID passed in as a URL param
		int articleID = -1;
		try {
			articleID = Integer.parseInt(request.getParameter("articleID"));
		} catch (NumberFormatException e) {
			session.setAttribute("errorMessage", "Sorry! The article you've requested does not exist.");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Get the article by id
		SupportTicket article = getArticle(articleID, user);

		if (article == null) {
			session.setAttribute("errorMessage", "Sorry! The article you've requested does not exist.");
			response.sendRedirect("ServicePortal");
			return;
		// If no article display error message
		} else if (article.getTitle() == null) {
			session.setAttribute("errorMessage", "Sorry! We could not display that article");
		}

		request.setAttribute("article", article);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Jsp.ARTICLE.url());
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
	 * Get Article from database
	 *
	 * @param articleID integer
	 * @param user User
	 * @return SupportTicket or null if error
	 */ 
	public SupportTicket getArticle(int articleID, User user) {
		try {
			// Calling the Ticket Data Access to retrieve the ticket from the database
			TicketDataAccess ticketDAO = new TicketDataAccess();
			SupportTicket article = ticketDAO.getTicketByIDFromDB(articleID, user, true);
			return article;
		} catch (Exception e) {
			return null;
		}
	}
}

