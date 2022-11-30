package itserviceportal.model.beans;
import java.io.Serializable;
import java.util.*;

/**
 * Notification bean
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 22-05-2018
 */

public class Notification implements Serializable {


	/**
	 * Enum class which outlines the possible actions which trigger a notification.
	 */
	public enum Action {
		STARTWORK, SUBMITSOLUTION, ACCEPTSOLUTION, REJECTSOLUTION, ADDKOWLEDGE, REMOVEKNOWLEDGE, COMMENT
	}

	private int notificationID;	//The ID of the notification
	private Action action;		//The Action which is triggering the notificiation
	private Date date;			//The date of the notification
	private int userID;			//The userID the notficiation is for
	private int ticketID;		//The ticketID the notification is for

	public Notification() {
	}



	/**
	 * Overloaded Constructor
	 *
	 * @param notificationID The id of the notification
	 * @param action The action triggering the notification as an Action ENUM
	 * @param date The date of the notification
	 * @param userID The user the notification is for
	 * @param ticketID The ticketID the notification is for
	 */
	public Notification(int notificationID, Action action, Date date, int userID, int ticketID) {
		this.notificationID = notificationID;
		this.action = action;
		this.date = date;
		this.userID = userID;
		this.ticketID = ticketID;
	}




	/**
	 * Overloaded Constructor
	 *
	 * @param notificationID The id of the notification
	 * @param action The action triggering the notification as an String
	 * @param date The date of the notification
	 * @param userID The user the notification is for
	 * @param ticketID The ticketID the notification is for
	 */
	public Notification(int notificationID, String action, Date date, int userID, int ticketID) {
		this.notificationID = notificationID;
		setAction(action);
		this.date = date;
		this.userID = userID;
		this.ticketID = ticketID;
	}



	/**
	 * notificationID GETTER and SETTER
	 */
	public int getNotificationID() { return notificationID; }
	public void setNotificationID(int notificationID) { this.notificationID = notificationID; }


	/**
	 * action GETTER and SETTER
	 */
	public Action getAction() { return action; }
	public void setAction(Action action) { this.action = action; }



	/**
	 * Method gets the the Action ENUM as a String value
	 * @return String - The string value of the action
	 */
	public String getActionStr() {
		if (action == null) {
			return "";
		} 
		switch (action) {
			case STARTWORK : return "startWork";
			case SUBMITSOLUTION : return "submitSolution";
			case ADDKOWLEDGE : return "addKnowledge";
			case ACCEPTSOLUTION : return "acceptSolution";
			case REJECTSOLUTION : return "rejectSolution";
			case REMOVEKNOWLEDGE : return "removeKnowledge";
			case COMMENT : return "comment";
			default : return "";
		}
	}



	/**
	 * Method sets the Action ENUM based on the String passed in
	 * @param action The action as a string
	 */
	public void setAction(String action) {
		if (action == null) {
			return;
		} 
		switch (action) {
			case "startWork" : this.action = Action.STARTWORK; break;
			case "submitSolution" : this.action = Action.SUBMITSOLUTION; break;
			case "addKnowledge" : this.action = Action.ADDKOWLEDGE; break;
			case "removeKnowledge" : this.action = Action.REMOVEKNOWLEDGE; break;
			case "acceptSolution" : this.action = Action.ACCEPTSOLUTION; break;
			case "rejectSolution" : this.action = Action.REJECTSOLUTION; break;
			case "comment" : this.action = Action.COMMENT; break;
			default : return;
		}
	}



	/**
	 * date GETTER and SETTER
	 */
	public Date getDate() { return date; }
	public void setDate(Date date) { this.date = date; }



	/**
	 * userID GETTER and SETTER
	 */
	public int getUserID() { return userID; }
	public void setuserID(int userID) { this.userID = userID; }



	/**
	 * ticketID GETTER and SETTER
	 */
	public int getTicketID() { return ticketID; }
	public void setTicketID(int ticketID) { this.ticketID = ticketID; }
}
