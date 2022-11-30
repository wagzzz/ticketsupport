package itserviceportal.model.beans;

/**
 * ENUM class of SupportTicket State's
 * A support ticket can only be in the following states:
 * New, In Progress, Completed or Resolved.
 * 
 * A support ticket is set to NEW when the issue is reported by a user.
 * A support ticket is changed to IN PROGRESS when a staff member starts work on the issue, or when a user rejects a solution
 * A support ticket is changed to COMPLETED when a staff member provides a solution.
 * A support ticket is changed to RESOLVED when a user accepts the solution provided
 *
 * @author Brice Purton, Jonathan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public enum State {
    NEW("New"),
    INPROGRESS("In Progress"),
    COMPLETED("Completed"),
    RESOLVED("Resolved");

	private String str;
	private State(String str) { this.str = str; }
	public String getStr() { return str; }
}