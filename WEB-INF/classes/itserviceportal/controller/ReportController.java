package itserviceportal.controller;

import itserviceportal.model.beans.*;
import itserviceportal.model.datalayer.*;
import java.io.*;
import java.util.*;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * ReportController
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @lastModified: 14-05-2018
 */

public class ReportController extends HttpServlet{

	/**
	 * Display the Report Page
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(Jsp.REPORT.url());
		dispatcher.forward(request, response);
	}

	/**
	 * Create a new support ticket based on user input
	 *
	 * @param request a http servlet request 
	 * @param response a http servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Get user
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		// Get ticket title, category and description 
		String title = request.getParameter("title");
		String category = request.getParameter("category");
		String description = request.getParameter("description");
		
		// Check if input is invalid
		if (isInvalidInput(title, category, description)) {
			session.setAttribute("errorMessage", "Sorry, issue was submitted incorrectly. Please try again.");
			response.sendRedirect("Report");
			return;
		}

		// Map of questions and responses
		Map<String, String> issueDetails = new HashMap<String, String>();
		
		// Fill map with question and answers
		switch (category) {
			case "network": issueDetails = getNetworkDetails(request); break;
			case "software": issueDetails = getSoftwareDetails(request); break;
			case "hardware": issueDetails = getHardwareDetails(request); break;
			case "email": issueDetails = getEmailDetails(request); break;
			case "account": issueDetails = getAccountDetails(request); break;
			default:
				session.setAttribute("errorMessage", "Invalid Category. Please try again.");
				response.sendRedirect("Report");
				return;
		}
		
		// Create a new support ticket in database
		try {
			TicketDataAccess ticketDAO = new TicketDataAccess();
			ticketDAO.newTicket(user, category, title, description, issueDetails);
			// Show success message and redirect to service portal
			session.setAttribute("successMessage", "Your issue has been reported!");
			response.sendRedirect("ServicePortal");
			return;
		} catch (Exception e) {
			session.setAttribute("errorMessage", "Sorry, an error occured while trying to submit your ticket. Please try again.");
			response.sendRedirect("Report");
			return;
		}
	}

	/**
	 * Check if user input is invalid
	 *
	 * @param title string 
	 * @param category string
	 * @param description string
	 * @return boolean true if invalid
	 */
	public boolean isInvalidInput(String title, String category, String description) {
		if (title == null || title.length() < 3 || title.length() > 200) { return true; }
		if (category == null) { return true; }
		if (description == null || description.length() < 3 || description.length() > 20000) { return true; }
		return false;
	}

	/**
	 * Get Network Details 
	 *
	 * @param request a http servlet request 
	 * @return Map< String, String> question/answer pairs of any details the user submitted
	 */
	public Map<String, String> getNetworkDetails(HttpServletRequest request) {
		HashMap<String, String> details = new HashMap<String, String>();

		// Put valid question and answers into details map
		String device = request.getParameter("networkDevice");
		if (device != null && !device.isEmpty() && device.length() <= 20000) {
			details.put("Device", device);
		}
		String location = request.getParameter("networkLocation");
		if (location != null && !location.isEmpty() && location.length() <= 20000) {
			details.put("Location", location);
		}
		String browser = request.getParameter("networkBrowser");
		if (browser != null && !browser.isEmpty() && browser.length() <= 20000) {
			details.put("Browser", browser);
		}
		String website = request.getParameter("networkWebsite");
		if (website != null && !website.isEmpty() && website.length() <= 20000) {
			details.put("Website I'm trying to connect to?", website);
		}
		String access = request.getParameter("networkAccess");
		if (access != null && !access.isEmpty() && access.length() <= 20000) {
			details.put("I am able to access internal websites?", access);
		}
		String alternate = request.getParameter("networkAlternate");
		if (alternate != null && !alternate.isEmpty() && alternate.length() <= 20000) {
			details.put("I have tried using an alternate internet browser?", alternate);
		}
		String restart = request.getParameter("networkRestart");
		if (restart != null && !restart.isEmpty() && restart.length() <= 20000) {
			details.put("I have tried restarting my device?", restart);
		}
		String anotherDevice = request.getParameter("networkAnotherDevice");
		if (anotherDevice != null && !anotherDevice.isEmpty() && anotherDevice.length() <= 20000) {
			details.put("I can access the website on another device?", anotherDevice);
		}
		return details;
	}
	
	/**
	 * Get Software Details 
	 *
	 * @param request a http servlet request 
	 * @return Map< String, String> question/answer pairs of any details the user submitted
	 */
	public Map<String, String> getSoftwareDetails(HttpServletRequest request) {
		HashMap<String, String> details = new HashMap<String, String>();
		
		// Put valid question and answers into details map
		String device = request.getParameter("softwareDevice");
		if (device != null && !device.isEmpty() && device.length() <= 20000) {
			details.put("Device", device);
		}
		String software = request.getParameter("softwareSoftware");
		if (software != null && !software.isEmpty() && software.length() <= 20000) {
			details.put("Software", software);
		}
		String version = request.getParameter("softwareVersion");
		if (version != null && !version.isEmpty() && version.length() <= 20000) {
			details.put("Version", version);
		}
		String install = request.getParameter("softwareInstall");
		if (install != null && !install.isEmpty() && install.length() <= 20000) {
			details.put("I can install the software?", install);
		}
		String run = request.getParameter("softwareRun");
		if (run != null && !run.isEmpty() && run.length() <= 20000) {
			details.put("I can run the software?", run);
		}
		String anotherDevice = request.getParameter("softwareAnotherDevice");
		if (anotherDevice != null && !anotherDevice.isEmpty() && anotherDevice.length() <= 20000) {
			details.put("I have tried to run the software on another computer?", anotherDevice);
		}
		
		return details;
	}
	
	/**
	 * Get Hardware Details 
	 *
	 * @param request a http servlet request 
	 * @return Map< String, String> question/answer pairs of any details the user submitted
	 */
	public Map<String, String> getHardwareDetails(HttpServletRequest request) {
		HashMap<String, String> details = new HashMap<String, String>();
		
		// Put valid question and answers into details map
		String device = request.getParameter("hardwareDevice");
		if (device != null && !device.isEmpty() && device.length() <= 20000) {
			details.put("Device", device);
		}
		String location = request.getParameter("hardwareLocation");
		if (location != null && !location.isEmpty() && location.length() <= 20000) {
			details.put("Location", location);
		}
		String access = request.getParameter("hardwareAccess");
		if (access != null && !access.isEmpty() && access.length() <= 20000) {
			details.put("I can access the device with my account login?", access);
		}
		String damaged = request.getParameter("hardwareDamaged");
		if (damaged != null && !damaged.isEmpty() && damaged.length() <= 20000) {
			details.put("Device is damaged?", damaged);
		}
		String power = request.getParameter("hardwarePower");
		if (power != null && !power.isEmpty() && power.length() <= 20000) {
			details.put("Device powers on?", power);
		}
		String error = request.getParameter("hardwareError");
		if (error != null && !error.isEmpty() && error.length() <= 20000) {
			details.put("Error message?", error);
		}
		
		return details;
	}
	
	/**
	 * Get Email Details 
	 *
	 * @param request a http servlet request 
	 * @return Map< String, String> question/answer pairs of any details the user submitted
	 */
	public Map<String, String> getEmailDetails(HttpServletRequest request) {
		HashMap<String, String> details = new HashMap<String, String>();
		
		// Put valid question and answers into details map
		String setup = request.getParameter("emailSetup");
		if (setup != null && !setup.isEmpty() && setup.length() <= 20000) {
			details.put("I have setup my email?", setup);
		}
		String signIn = request.getParameter("emailSignIn");
		if (signIn != null && !signIn.isEmpty() && signIn.length() <= 20000) {
			details.put("I can sign in?", signIn);
		}
		String reset = request.getParameter("emailReset");
		if (reset != null && !reset.isEmpty() && reset.length() <= 20000) {
			details.put("I've tried resetting my password?", reset);
		}
		String sendReceive = request.getParameter("emailSendReceive");
		if (sendReceive != null && !sendReceive.isEmpty() && sendReceive.length() <= 20000) {
			details.put("I can send and receive emails?", sendReceive);
		}
		String internet = request.getParameter("emailInternet");
		if (internet != null && !internet.isEmpty() && internet.length() <= 20000) {
			details.put("Internet connection confirmed??", internet);
		}
		
		return details;
	}
	
	/**
	 * Get Account Details 
	 *
	 * @param request a http servlet request 
	 * @return Map< String, String> question/answer pairs of any details the user submitted
	 */
	public Map<String, String> getAccountDetails(HttpServletRequest request) {
		HashMap<String, String> details = new HashMap<String, String>();
		
		// Put valid question and answers into details map
		String activate = request.getParameter("accountActivate");
		if (activate != null && !activate.isEmpty() && activate.length() <= 20000) {
			details.put("I have activated my account?", activate);
		}
		String university = request.getParameter("accountUniversity");
		if (university != null && !university.isEmpty() && university.length() <= 20000) {
			details.put("I can log into a university computer?", university);
		}
		String system = request.getParameter("accountSystem");
		if (system != null && !system.isEmpty() && system.length() <= 20000) {
			details.put("University system im trying to access?", system);
		}

		String reset = request.getParameter("accountReset");
		if (reset != null && !reset.isEmpty() && reset.length() <= 20000) {
			details.put("I have tried resetting my password?", reset);
		}
		String error = request.getParameter("accountError");
		if (error != null && !error.isEmpty() && error.length() <= 20000) {
			details.put("Error message?", error);
		}
		return details;
	}
}