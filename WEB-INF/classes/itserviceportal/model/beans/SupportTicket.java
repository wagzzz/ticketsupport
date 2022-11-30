package itserviceportal.model.beans;
import java.io.Serializable;
import java.util.*;


/**
 * SupportTicket Bean
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class SupportTicket implements Serializable {

	private int ticketID;							//The ID of the ticket
	private Category category;						//The Ticket category (Network, Hardware etc)
	private State state;							//The current state of the ticket (NEW, IN PROGRESS etc)
	private String title;							//The title of the ticket
	private String description;						//Description of the issues
	private Date reportedOn;						//The date the ticket was reported on
	private User reportedBy;						//The user who made the support ticket
	private Date resolvedOn;						//The date a solution was provided
	private User resolvedBy;						//The staff member who solved the issue
	private boolean knowledgeBase;					//A value outlining if the support ticket is a knowledge base article
	private String resolutionDetails;				//The resolution details provided by the staff member
	private ArrayList<IssueDetail> issueDetails;	//A list of Questions and responses by the user explaining their issue
	private ArrayList<Comment> comments;			//The list of comments made by the user and staff on the ticket.

	public SupportTicket() {
	}




	/**
	 * Overloaded Constructor
	 *
	 * @param ticketID The ID of the ticket
	 * @param category The Ticket category (Network, Hardware etc) as a Category ENUM class
	 * @param state The current state of the ticket (NEW, IN PROGRESS etc) as a State ENUM class
	 * @param title The title of the ticket
	 * @param description Description of the issue
	 * @param reportedOn The date the ticket was reported on
	 * @param reportedBy The user who made the support ticket
	 * @param resolvedOn The date a solution was provided
	 * @param resolvedBy The staff member who solved the issue
	 * @param knowledgeBase A value outlining if the support ticket is a knowledge base article
	 * @param resolutionDetails The resolution details provided by the staff member
	 * @param issueDetails A list of Questions and responses by the user explaining their issue
	 * @param comments The list of comments made by the user and staff on the ticket.
	 */
	public SupportTicket(int ticketID, Category category, State state, String title, String description, Date reportedOn,
		User reportedBy, Date resolvedOn, User resolvedBy, boolean knowledgeBase, String resolutionDetails, ArrayList<IssueDetail> issueDetails, ArrayList<Comment> comments) {
		this.ticketID = ticketID;
		this.category = category;
		this.state = state;
		this.title = title;
		this.description = description;
		this.reportedOn = reportedOn;
		this.reportedBy = reportedBy;
		this.resolvedOn = resolvedOn;
		this.resolvedBy = resolvedBy;
		this.knowledgeBase = knowledgeBase;
		this.resolutionDetails = resolutionDetails;
		this.issueDetails = issueDetails;
		this.comments = comments;
	}




	/**
	 * Overloaded Constructor
	 *
	 * @param ticketID The ID of the ticket
	 * @param category The Ticket category (Network, Hardware etc) as a String
	 * @param state The current state of the ticket (NEW, IN PROGRESS etc) as a String
	 * @param title The title of the ticket
	 * @param description Description of the issue
	 * @param reportedOn The date the ticket was reported on
	 * @param reportedBy The user who made the support ticket
	 * @param resolvedOn The date a solution was provided
	 * @param resolvedBy The staff member who solved the issue
	 * @param knowledgeBase A value outlining if the support ticket is a knowledge base article
	 * @param resolutionDetails The resolution details provided by the staff member
	 * @param issueDetails A list of Questions and responses by the user explaining their issue
	 * @param comments The list of comments made by the user and staff on the ticket.
	 */
	public SupportTicket(int ticketID, String category, String state, String title, String description, Date reportedOn,
		User reportedBy, Date resolvedOn, User resolvedBy, boolean knowledgeBase, String resolutionDetails, ArrayList<IssueDetail> issueDetails, ArrayList<Comment> comments) {
		this.ticketID = ticketID;
		setCategory(category);
		setState(state);
		this.title = title;
		this.description = description;
		this.reportedOn = reportedOn;
		this.reportedBy = reportedBy;
		this.resolvedOn = resolvedOn;
		this.resolvedBy = resolvedBy;
		this.knowledgeBase = knowledgeBase;
		this.resolutionDetails = resolutionDetails;
		this.issueDetails = issueDetails;
		this.comments = comments;
	}


	/**
	 * ticketID GETTER and SETTER
	 */
	public int getTicketID() { return ticketID; }
	public void setTicketID(int ticketID) { this.ticketID = ticketID; }



	/**
	 * category GETTER and SETTERS
	 */
	public Category getCategory() { return category; }
	public void setCategory(Category category) { this.category = category; }
	public void setCategory(String category) {
		if(category.equalsIgnoreCase("network"))
			this.category = Category.NETWORK;
		else if(category.equalsIgnoreCase("software"))
			this.category = Category.SOFTWARE;
		else if(category.equalsIgnoreCase("hardware"))
			this.category = Category.HARDWARE;
		else if(category.equalsIgnoreCase("account"))
			this.category = Category.ACCOUNT;
		else
			this.category = Category.EMAIL;
	}



	/**
	 * state GETTER and SETTERS
	 */
	public State getState() { return state; }
	public void setState(State state) { this.state = state; }
	public void setState(String state) {
		if(state.equalsIgnoreCase("new"))
			this.state = State.NEW;

		else if(state.equalsIgnoreCase("in progress"))
			this.state = State.INPROGRESS;

		else if(state.equalsIgnoreCase("completed"))
			this.state = State.COMPLETED;

		else
			this.state = State.RESOLVED;
	}


	/**
	 * title GETTER and SETTER
	 */
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }



	/**
	 * description GETTER and SETTER
	 */
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }



	/**
	 * title reportedOn and SETTER
	 */
	public Date getReportedOn() { return reportedOn; }
	public void setReportedOn(Date reportedOn) { this.reportedOn = reportedOn; }



	/**
	 * reportedBy GETTER and SETTER
	 */
	public User getReportedBy() { return reportedBy; }
	public void setReportedBy(User reportedBy) { this.reportedBy = reportedBy; }



	/**
	 * resolvedOn GETTER and SETTER
	 */
	public Date getResolvedOn() { return resolvedOn; }
	public void setResolvedOn(Date resolvedOn) { this.resolvedOn = resolvedOn; }



	/**
	 * resolvedBy GETTER and SETTER
	 */
	public User getResolvedBy() { return resolvedBy; }
	public void setResolvedBy(User resolvedBy) { this.resolvedBy = resolvedBy; }



	/**
	 * knowledgeBase GETTER and SETTER
	 */
	public boolean isKnowledgeBase() { return knowledgeBase; }
	public void setKnowledgeBase(boolean knowledgeBase) { this.knowledgeBase = knowledgeBase; }



	/**
	 * resolutionDetails GETTER and SETTER
	 */
	public String getResolutionDetails() { return resolutionDetails; }
	public void setResolutionDetails(String resolutionDetails) { this.resolutionDetails = resolutionDetails; }



	/**
	 * issueDetails GETTER and SETTER
	 */
	public ArrayList<IssueDetail> getIssueDetails() { return issueDetails; }
	public void setIssueDetails(ArrayList<IssueDetail> issueDetails) { this.issueDetails = issueDetails; }



	/**
	 * comments GETTER and SETTER
	 */
	public ArrayList<Comment> getComments() { return comments; }
	public void setComments(ArrayList<Comment> comments) { this.comments = comments; }
}
