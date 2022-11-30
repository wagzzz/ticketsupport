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
 * TicketController
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class TicketController extends HttpServlet {

	/**
	 * Redirects get request to doPost method
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
		int ticketID = -1;
		try
		{
			ticketID = Integer.parseInt(request.getParameter("ticketID"));
		}
		// The URL did not contain a valid int value. Display error
		catch (NumberFormatException e)
		{
			session.setAttribute("errorMessage", "Sorry! The ticket you've requested does not exist.");
			response.sendRedirect("ServicePortal");
			return;
		}
		
		// Get the support ticket by id
		SupportTicket supportTicket = getTicket(ticketID, user);

		// If no ticket display error message
		if (supportTicket == null) {
			session.setAttribute("errorMessage", "Sorry! The ticket you've requested does not exist.");
			response.sendRedirect("ServicePortal");
			return;
		}
		request.setAttribute("supportTicket", supportTicket);

		// Send user to the correct jsp based on role
		if (user.getRole() == Role.USER) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Jsp.USERTICKET.url());
			dispatcher.forward(request, response);
		} else {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Jsp.STAFFTICKET.url());
			dispatcher.forward(request, response);
		}
	}

	/**
	 * Modify a support ticket based on user action and input
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		HttpSession session = request.getSession();
		// Get the action performed by user
		String action = request.getParameter("action");
		if (action == null) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Choose which method to execute based on user action
		switch (action) {
			case "startWork": StartWork(action, request, response); break;
			case "submitSolution": SubmitSolution(action, request, response); break;
			case "acceptSolution": AcceptSolution(action, request, response); break;
			case "rejectSolution": RejectSolution(action, request, response); break;
			case "addKnowledge": AddKnowledge(action, request, response); break;
			case "removeKnowledge": RemoveKnowledge(action, request, response); break;
			case "comment": Comment(action, request, response); break;
			default:
				session.setAttribute("errorMessage", "Sorry! Request is invalid");
				response.sendRedirect("ServicePortal");
				return;
		}
	}

	/**
	 * Retrieve a support ticket from the database
	 *
	 * @param ticketID integer
	 * @param user User
	 * @return SupportTicket or null if ticket doesn't exist
	 */ 
	public SupportTicket getTicket(int ticketID, User user) {
		try {
			// Calling the Ticket Data Access to retrieve the ticket from the database
			TicketDataAccess ticketDAO = new TicketDataAccess();
			SupportTicket supportTicket = ticketDAO.getTicketByIDFromDB(ticketID, user, false);
			return supportTicket;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Start Work Action limited to Staff role sets a ticket to in progress
	 *
	 * @param action string
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void StartWork(String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Check user role
		if (user.getRole() != Role.STAFF) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Get the ticket ID
		int ticketID = -1;
		try {
			ticketID = Integer.parseInt(request.getParameter("ticketID"));
		} catch (NumberFormatException e) {
			session.setAttribute("errorMessage", "Sorry! The ticket does not exist.");
			response.sendRedirect("ServicePortal");
			return;
		}

		SupportTicket supportTicket = getTicket(ticketID, user);
		// Support ticket will be null if user can't view that ticket
		if (supportTicket == null) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		// Only start work if ticket is new
		} else if (supportTicket.getState() != State.NEW) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		}

		try
		{
			// Set ticket state to in progress
			TicketDataAccess ticketDAO = new TicketDataAccess();
			ticketDAO.updateTicketStateToInProgress(ticketID);
			session.setAttribute("progressMessage", "You've started work on Support Ticket " + ticketID + ".");
 
			// Get the ticket ID
			int reportedUserID = -1;
			try {
				// Notify User
				reportedUserID = Integer.parseInt(request.getParameter("reportedBy"));
				NotificationDataAccess notificationDAO = new NotificationDataAccess();
				notificationDAO.setNotification(action, reportedUserID, ticketID);
				SessionListener.updateActiveUserNotifications(reportedUserID);
				session.setAttribute("progressMessage", "User has been notified that you've started work on Support Ticket " + ticketID + ".");
			} catch (NumberFormatException e) {
			}
		}
		catch (SQLException e)
		{
			session.setAttribute("errorMessage", "Sorry! An error occured while trying to start work on Support Ticket " + ticketID + ".");
		}
		
		// Display updated ticket
		doGet(request, response);
	}

	/**
	 * Submit Solution Action limited to Staff role adds a solution to a support ticket and sets it to completed
	 * 
	 * @param action string
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void SubmitSolution(String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Validate user role
		if (user.getRole() != Role.STAFF) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Get the ticket ID
		int ticketID = -1;
		try {
			ticketID = Integer.parseInt(request.getParameter("ticketID"));
		} catch (NumberFormatException e) {
			session.setAttribute("errorMessage", "Sorry! The ticket does not exist.");
			response.sendRedirect("ServicePortal");
			return;
		}

		SupportTicket supportTicket = getTicket(ticketID, user);
		// Support ticket will be null if user can't view that ticket
		if (supportTicket == null) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		// Only submit solution if ticket is in progress
		} else if (supportTicket.getState() != State.INPROGRESS) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Get submitted solution
		String resolutionDetails = request.getParameter("solution");
		if (resolutionDetails == null || resolutionDetails.length() < 3 || resolutionDetails.length() > 20000) {
			session.setAttribute("errorMessage", "Sorry! Solution must be between 3-20000 characters.");
			doGet(request, response);
			return;
		}

		// Update the ticket to completed status adding the resolution and staff user to the ticket
		try
		{
			TicketDataAccess ticketDAO = new TicketDataAccess();
			ticketDAO.updateTicketStateToComplete(ticketID, resolutionDetails, user.getUserID());

			session.setAttribute("primaryMessage", "Solution has been successfully submitted.");
 
			// Get the ticket ID
			int reportedUserID = -1;
			try {
				// Notify User
				reportedUserID = Integer.parseInt(request.getParameter("reportedBy"));
				NotificationDataAccess notificationDAO = new NotificationDataAccess();
				notificationDAO.setNotification(action, reportedUserID, ticketID);
				SessionListener.updateActiveUserNotifications(reportedUserID);
				session.setAttribute("primaryMessage", "Solution has been successfully submitted and user has been notified.");
			} catch (NumberFormatException e) {
			}
		}
		catch (SQLException e)
		{
			session.setAttribute("errorMessage", "Sorry! An error occured while trying to update the ticket state. Please try again");
		}
		
		// Display updated ticket
		doGet(request, response);
	}

	/**
	 * Accept Solution Action limited to User role accepts a solution to a support ticket and sets it to resolved
	 * 
	 * @param action string
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void AcceptSolution(String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Validate user role
		if (user.getRole() != Role.USER) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Get the ticket ID
		int ticketID = -1;
		try {
			ticketID = Integer.parseInt(request.getParameter("ticketID"));
		} catch (NumberFormatException e) {
			session.setAttribute("errorMessage", "Sorry! The ticket does not exist.");
			response.sendRedirect("ServicePortal");
			return;
		}

		SupportTicket supportTicket = getTicket(ticketID, user);
		// Support ticket will be null if user can't view that ticket
		if (supportTicket == null) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		// Only accept solution if ticket is completed
		} else if (supportTicket.getState() != State.COMPLETED) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Update the ticket to resolved status
		try
		{
			TicketDataAccess ticketDAO = new TicketDataAccess();
			ticketDAO.updateTicketStateToAccepted(ticketID);

			session.setAttribute("successMessage", "Support Ticket " + ticketID + " has been resolved and closed. We're glad we could help!");

			// Send notification to staff user
			int resolvedUserID = -1;
			try {
				resolvedUserID = Integer.parseInt(request.getParameter("resolvedBy"));
				NotificationDataAccess notificationDAO = new NotificationDataAccess();
				notificationDAO.setNotification(action, resolvedUserID, ticketID);
				SessionListener.updateActiveUserNotifications(resolvedUserID);
			} catch (NumberFormatException e) {
			}
		}
		catch (SQLException e)
		{
			session.setAttribute("errorMessage", "Sorry! An error occured while trying to update the ticket state. Please try again");
		}
		
		// Display updated ticket
		doGet(request, response);
	}

	/**
	 * Reject Solution Action limited to User role rejects a solution to a support ticket and sets it to in progress
	 * 
	 * @param action string
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void RejectSolution(String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Validate user role
		if (user.getRole() != Role.USER) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Get the ticket ID
		int ticketID = -1;
		try {
			ticketID = Integer.parseInt(request.getParameter("ticketID"));
		} catch (NumberFormatException e) {
			session.setAttribute("errorMessage", "Sorry! The ticket does not exist.");
			response.sendRedirect("ServicePortal");
			return;
		}

		SupportTicket supportTicket = getTicket(ticketID, user);
		// Support ticket will be null if user can't view that ticket
		if (supportTicket == null) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		// Only reject solution if ticket is completed
		} else if (supportTicket.getState() != State.COMPLETED) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Update the ticket to in progress status. When user rejects solution state is set to inprogress,
		// all resolution details such as ResolvedOn, ResolvedByUserID and ResolutionDetails are set back to NULL
		// and knowledgeBase is set to false
		try
		{
			TicketDataAccess ticketDAO = new TicketDataAccess();
			ticketDAO.updateTicketStateToRejected(ticketID);
			session.setAttribute("progressMessage", "Solution has been rejected.");

			// Send notification to staff user
			int resolvedUserID = -1;
			try {
				resolvedUserID = Integer.parseInt(request.getParameter("resolvedBy"));
				NotificationDataAccess notificationDAO = new NotificationDataAccess();
				notificationDAO.setNotification(action, resolvedUserID, ticketID);
				SessionListener.updateActiveUserNotifications(resolvedUserID);
				session.setAttribute("progressMessage", "Solution has been rejected and staff has been notified.");
			} catch (NumberFormatException e) {
			}
		}
		catch (SQLException e)
		{
			session.setAttribute("errorMessage", "Sorry! An error occured while trying to update the ticket state. Please try again");
		}
		
		// Display updated ticket
		doGet(request, response);
	}

	/**
	 * Add Knowledge Action limited to Staff role adds a support ticket to the knowledge base
	 * 
	 * @param action string
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void AddKnowledge(String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Validate user role
		if (user.getRole() != Role.STAFF) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Get the ticket ID
		int ticketID = -1;
		try {
			ticketID = Integer.parseInt(request.getParameter("ticketID"));
		} catch (NumberFormatException e) {
			session.setAttribute("errorMessage", "Sorry! The ticket does not exist.");
			response.sendRedirect("ServicePortal");
			return;
		}

		SupportTicket supportTicket = getTicket(ticketID, user);
		// Support ticket will be null if user can't view that ticket
		if (supportTicket == null) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		// Only add completed or resolved non-knowledge base tickets
		} else if (supportTicket.getState() == State.NEW || supportTicket.getState() == State.INPROGRESS || supportTicket.isKnowledgeBase()) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Add the ticket to the knowledge base
		try
		{
			TicketDataAccess ticketDAO = new TicketDataAccess();
			ticketDAO.AddOrRemoveFromKnowledgeBase(ticketID, true);

			session.setAttribute("successMessage", "Support Ticket has been added to Knowledge Base.");
 
			// Get the ticket ID
			int reportedUserID = -1;
			try {
				// Notify User
				reportedUserID = Integer.parseInt(request.getParameter("reportedBy"));
				NotificationDataAccess notificationDAO = new NotificationDataAccess();
				notificationDAO.setNotification(action, reportedUserID, ticketID);
				SessionListener.updateActiveUserNotifications(reportedUserID);
				session.setAttribute("successMessage", "Support Ticket has been added to Knowledge Base and user has been notified.");
			} catch (NumberFormatException e) {
			}
		}
		catch (SQLException e)
		{
			session.setAttribute("errorMessage", "Sorry! An error occured while trying to update the ticket state. Please try again");
		}
		
		// Display updated ticket
		doGet(request, response);
	}

	/**
	 * Remove Knowledge Action limited to Staff role removes a support ticket from the knowledge base
	 * 
	 * @param action string
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void RemoveKnowledge(String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Validate user role
		if (user.getRole() != Role.STAFF) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Get the support ticket
		TicketDataAccess ticketDAO = new TicketDataAccess();
		SupportTicket supportTicket = null;
		int ticketID = -1;
		try {
			ticketID = Integer.parseInt(request.getParameter("ticketID"));
			supportTicket = ticketDAO.getTicketByIDFromDB(ticketID, user, true);
		} catch (Exception e) {
			session.setAttribute("errorMessage", "Sorry! The ticket does not exist.");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Support ticket will be null if user can't view that ticket
		if (supportTicket == null) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		// Only remove knowledgeBase articles
		} else if (!supportTicket.isKnowledgeBase()) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid, not a knowledge base article.");
			response.sendRedirect("ServicePortal");
			return;
		}

		// Remove the ticket from the knowledge base
		try
		{
			String redirection = request.getParameter("redirection");
			ticketDAO = new TicketDataAccess();
			ticketDAO.AddOrRemoveFromKnowledgeBase(ticketID, false);

			session.setAttribute("successMessage", "Support Ticket has been removed from Knowledge Base.");
 
			// Get the ticket ID
			int reportedUserID = -1;
			try {
				// Notify User
				reportedUserID = Integer.parseInt(request.getParameter("reportedBy"));
				NotificationDataAccess notificationDAO = new NotificationDataAccess();
				notificationDAO.setNotification(action, reportedUserID, ticketID);
				SessionListener.updateActiveUserNotifications(reportedUserID);
				session.setAttribute("successMessage", "Support Ticket has been removed from Knowledge Base and user has been notified.");
			} catch (NumberFormatException e) {
			}

			// If redirection is not null, then we want to go back to the KnowledgeBase
			// because a staff member removed a knowledge base article from the article page
			if (redirection != null) {
				response.sendRedirect("KnowledgeBase");
				return;
			}
		}
		catch (SQLException e)
		{
			session.setAttribute("errorMessage", "Sorry! An error occured while trying to update the ticket state. Please try again");
		}
		
		// Display updated ticket
		doGet(request, response);
	}

	/**
	 * Comment Action adds a comment to a support ticket
	 * 
	 * @param action string
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	public void Comment(String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// Get the ticket ID
		int ticketID = -1;
		try {
			ticketID = Integer.parseInt(request.getParameter("ticketID"));
		} catch (NumberFormatException e) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		}

		SupportTicket supportTicket = getTicket(ticketID, user);
		// Support ticket will be null if user can't comment on ticket
		if (supportTicket == null) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		// Can't comment on resolved tickets
		} else if (supportTicket.getState() == State.RESOLVED) {
			session.setAttribute("errorMessage", "Sorry! Request is invalid.");
			response.sendRedirect("ServicePortal");
			return;
		}

		String commentText = request.getParameter("commentText");
		if (commentText == null || commentText.length() < 3 || commentText.length() > 20000) {
			session.setAttribute("errorMessage", "Sorry! Comment must be between 3-20000 characters.");
			doGet(request, response);
			return;
		}
			
		// Add the comment to the ticket
		try {
 			CommentDataAccess commentDAO = new CommentDataAccess();
 			commentDAO.addComment(ticketID, commentText, user.getUserID());
			session.setAttribute("successMessage", "Comment has been posted.");
 
			// If Staff action then notify user
 			if (user.getRole() == Role.STAFF) {
				// Get the ticket ID
				int reportedUserID = -1;
				try {
					reportedUserID = Integer.parseInt(request.getParameter("reportedBy"));
					NotificationDataAccess notificationDAO = new NotificationDataAccess();
					notificationDAO.setNotification(action, reportedUserID, ticketID);
					SessionListener.updateActiveUserNotifications(reportedUserID);
					session.setAttribute("successMessage", "Comment has been posted and user has been notified.");
				} catch (NumberFormatException e) {
				}
			}
		} catch (SQLException e) {
			session.setAttribute("errorMessage", "Sorry! an error occured while trying to add a comment, please try again.");
		}
		
		// Display updated ticket
		doGet(request, response);
	}
}

