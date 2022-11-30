package itserviceportal.customtags;

import itserviceportal.model.beans.*;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.SkipPageException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class NotificationTag extends SimpleTagSupport {

	private Notification notification;

	public NotificationTag() {
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	@Override
	public void doTag() throws JspException, IOException {
		if (notification == null && (notification.getActionStr() == null || notification.getTicketID() == 0)) {
			return;
		}

		String notifStr = "";
		// Choose what the notification will say
		switch (notification.getActionStr()) {
			case "startWork" : notifStr += "Staff has started work on <strong>Support Ticket " + notification.getTicketID() + "</strong>."; break;
			case "submitSolution" : notifStr += "Solution submitted to <strong>Support Ticket " + notification.getTicketID() + "</strong>."; break;
			case "addKnowledge" : notifStr += "<strong>Support Ticket " + notification.getTicketID() + "</strong> has been added to the knowledge base."; break;
			case "removeKnowledge" : notifStr += "<strong>Support Ticket " + notification.getTicketID() + "</strong> has been removed to the knowledge base."; break;
			case "acceptSolution" : notifStr += "Solution for <strong>Support Ticket " + notification.getTicketID() + "</strong> has been accepted."; break;
			case "rejectSolution" : notifStr += "Solution for <strong>Support Ticket " + notification.getTicketID() + "</strong> has been rejected."; break;
			case "comment" : notifStr += "<strong>Support Ticket " + notification.getTicketID() + "</strong> has been commented on."; break;
			default : return;
		}

		// Output to jsp
		getJspContext().getOut().write(notifStr);
	}
}