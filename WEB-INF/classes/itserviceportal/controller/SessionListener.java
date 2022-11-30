package itserviceportal.controller;

import itserviceportal.model.beans.*;
import itserviceportal.model.datalayer.*;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * SessionListener
 * This class is used to manage session tracking. It adds all the sessions of each user to a hashmap on log in and
 * removes them on log out or timeout. This allows us to modify sessions outside the scope of the session for greater
 * flexiblity.
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public final class SessionListener implements HttpSessionListener, HttpSessionAttributeListener  {

	// Map to store active users and their sessions (UserID, List of their sessions)
	private static HashMap<Integer, List<HttpSession>> activeUsers = new HashMap<Integer, List<HttpSession>>();

	// Utility method to print out all active user sessions
	private static void printActiveUsers() {
		for (Map.Entry<Integer, List<HttpSession>> user : activeUsers.entrySet()) {
			System.out.println("User: "+user.getKey());
			System.out.println("Sessions: ");
			for(HttpSession session : user.getValue()){
				System.out.println(session.getCreationTime());
			}
		}
	}

	/**
	 * Check if a user is active in a session.
	 * 
	 * @param userID
	 * @return true if active
	 */
	public static boolean isUserActive(int userID) {
		return activeUsers.containsKey(userID);
	}

	/**
	 * Return list of active user's sessions.
	 * 
	 * @param userID
	 * @return user's sessions
	 */
	public static List<HttpSession> getActiveUserSessions(int userID) {
		return activeUsers.get(userID);
	}

	/**
	 * Reload an active user's notifcations into their session(s).
	 * 
	 * @param userID
	 */
	public static void updateActiveUserNotifications(int userID) {
		try {
			// If the user is active than load in their notifications again
			if (isUserActive(userID)) {
				List<HttpSession> sessions = activeUsers.get(userID);

				// Retrieve current notifications from database
				NotificationDataAccess notificationDAL = new NotificationDataAccess();
				List<Notification> notifications = notificationDAL.getNotifications(userID);

				// Reset the notifications attirbute in each of the user's sessions
				ListIterator<HttpSession> it = sessions.listIterator();
				while (it.hasNext()) {
					HttpSession session = it.next();
					session.setAttribute("notifications", notifications);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set session's to timeout after 15 minutes.
	 * 
	 * @param event HttpSessionEvent
	 */
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		event.getSession().setMaxInactiveInterval(15 * 60);
	}

	/**
	 * Remove session from activeUser's session if destroyed session contained a user
	 * and remove user from active if they no longer have any sessions.
	 * 
	 * @param event HttpSessionBindingEvent
	 */
	@Override
	public final void sessionDestroyed(HttpSessionEvent event) {
		User user = (User) event.getSession().getAttribute("user");
		// If the destroyed session contained a user than remove that session from active map
		if (user != null) {
			long creationTime = event.getSession().getCreationTime();
			// Get list of that user's sessions
			List<HttpSession> sessions = activeUsers.get(user.getUserID());

			// Remove the session from the list of active user's session
			ListIterator<HttpSession> it = sessions.listIterator();
			while (it.hasNext()) {
				HttpSession session = it.next();
				// If they were created at the same time then we want to remove that session
				if (session.getCreationTime() == creationTime) {
					it.remove();
					break;
				}
			}
			// If user has no sessions then remove them from active users
			if (sessions.isEmpty()) {
				activeUsers.remove(user.getUserID());
			}
		}
	}

	/**
	 * Add userID and session to activeUsers map if user attribute added to a session.
	 *
	 * @param event HttpSessionBindingEvent
	 */
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		// User logs in
		if (event.getName().equals("user")) {
			User user = (User) event.getValue();
			List<HttpSession> sessions = activeUsers.get(user.getUserID());

			// Check if user had no previous active sessions
			if (sessions == null) {
				sessions = new ArrayList<HttpSession>();
			}
			// Add new session to list of active sessions
			sessions.add(event.getSession());
			activeUsers.put(user.getUserID(), sessions);
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
	}
}