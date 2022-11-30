package itserviceportal.controller;

import itserviceportal.model.beans.*;
import itserviceportal.model.datalayer.*;
import java.util.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * The Authentication class is a web filter that manages user permissions.
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class Authentication implements Filter {

	private Map<String,String> permissions = new HashMap<String,String>();
	public final static String ALL = "ALL";
	public final static String USER = "USER";
	public final static String USERX = "USER_EXCLUSIVE";
	public final static String STAFF = "STAFF";
	public final static String NONE = "NO_ACCESS";

	/**
	 * This method initialises the filter specifying user page access
	 * by adding them to the permissions map.
	 *
	 * @param filterConfig
	 * @throws ServletException
	 */ 
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Servlets
		permissions.put("/",ALL);
		permissions.put("Login",ALL);
		permissions.put("Logout",USER);
		permissions.put("ServicePortal",USER);
		permissions.put("UserPortal",USERX);
		permissions.put("StaffPortal",STAFF);
		permissions.put("Report",USERX);
		permissions.put("TicketList",USER);
		permissions.put("Ticket",USER);
		permissions.put("KnowledgeBase",USER);
		permissions.put("Article",USER);
		permissions.put("Suggestion",USERX);
		permissions.put("Notification",USER);
		permissions.put("Timeout",USER);
		// JSPs
		permissions.put("login.jsp",ALL);
		permissions.put("userPortal.jsp",USERX);
		permissions.put("staffPortal.jsp",STAFF);
		permissions.put("reportIssue.jsp",USERX);
		permissions.put("ticketList.jsp",USER);
		permissions.put("userTicket.jsp",USERX);
		permissions.put("staffTicket.jsp",STAFF);
		permissions.put("knowledgeBase.jsp",USER);
		permissions.put("article.jsp",USER);
		permissions.put("suggestedArticle.jsp",USERX);
		permissions.put("403.jsp",ALL);
		permissions.put("404.jsp",ALL);
		permissions.put("500.jsp",ALL);
	}

	/**
	 * This method authenticates the user and redirects them according
	 * to their access permission and request uri.
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */ 
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
		HttpServletResponse httpServletResponse = ((HttpServletResponse)response);
		HttpSession session = httpServletRequest.getSession();
		RequestDispatcher requestDispatcher;

		// Get serlvet mapping the user is trying to request
		String uri = httpServletRequest.getRequestURI();
		if (uri.endsWith("/")) {
			uri = uri.substring(0, uri.length()-1);
		}
		String urlMapping = uri.substring(uri.lastIndexOf("/")+1);

		// Get permission of the page
		String permission = permissions.getOrDefault(urlMapping, ALL);

		// Authenticate user against permission of page
		switch (permission) {

			// All access allowed so proceeed with request
			case ALL:
				chain.doFilter(request, response);
				return;

			// No access allowed so redirect to login page
			case NONE:
				httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/");
				return;

			// Must be logged in to access otherwise redirect to login page
			case USER:
				if (isUserLoggedIn(session)) {
					chain.doFilter(request, response);
					return;
				} else {
					httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/");
					return;
				}

			// Must be logged in as a user to access otherwise redirect to login page
			case USERX:
				if (isUserLoggedIn(session) && !isStaffLoggedIn(session)) {
					chain.doFilter(request, response);
					return;
				} else {
					httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/");
					return;
				}

			// Must be logged in as a staff to access otherwise redirect to login page
			case STAFF:
				if (isStaffLoggedIn(session)) {
					chain.doFilter(request, response);
					return;
				} else {
					httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/");
					return;
				}
			default:
				chain.doFilter(request, response);
				return;
		}
	}

	@Override
	public void destroy() {
	}

	/**
	 * Check if a user is logged in
	 *
	 * @param session a http session object
	 * @return boolean true if user logged in
	 */ 
	public boolean isUserLoggedIn(HttpSession session) {
		return session.getAttribute("user") != null;
	}

	/**
	 * Check if a staff user is logged in
	 *
	 * @param session a http session object
	 * @return boolean true ifstaff logged in
	 */ 
	public boolean isStaffLoggedIn(HttpSession session) {
		User user = (User) session.getAttribute("user");
		return user != null && user.getRole() == Role.STAFF;
	}
}
