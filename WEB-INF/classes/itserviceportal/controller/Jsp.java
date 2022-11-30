package itserviceportal.controller;

/**
 * Enumeration of visitable JSP pages to store url constants for easy maintainability
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public enum Jsp {
	INDEX("/WEB-INF/view/index.jsp"),
	LOGIN("/WEB-INF/view/index.jsp"),
	USERPORTAL("/WEB-INF/view/userx/userPortal.jsp"),
	STAFFPORTAL("/WEB-INF/view/staff/staffPortal.jsp"),
	REPORT("/WEB-INF/view/userx/reportIssue.jsp"),
	TICKETLIST("/WEB-INF/view/user/ticketList.jsp"),
	USERTICKET("/WEB-INF/view/userx/userTicket.jsp"),
	STAFFTICKET("/WEB-INF/view/staff/staffTicket.jsp"),
	KNOWLEDGEBASE("/WEB-INF/view/user/knowledgeBase.jsp"),
	ARTICLE("/WEB-INF/view/user/article.jsp"),
	SUGGESTEDARTICLES("/WEB-INF/view/user/suggestedArticles.jsp"),
	FORBIDDEN("/WEB-INF/view/error403.jsp"),
	NOTFOUND("/WEB-INF/view/error404.jsp"),
	SERVERERROR("/WEB-INF/view/error500.jsp"),
	EXCEPTION("/WEB-INF/view/errorException.jsp");

	private String url;

	// Private constructor to prevent new instances.
	private Jsp(String url) { this.url = url; }

	public String url() { return url; }
}