package itserviceportal.model.beans;

import java.io.Serializable;
import java.util.*;

/**
 * IssueDetail bean
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class IssueDetail implements Serializable {

	private int issueDetailID;	//The ID of the issue detail
	private String question;	//The question being answered
	private String response;	//The response to the question

	public IssueDetail() {
	}
	

	/**
	 * Overloaded Constructor
	 *
	 * @param issueDetailID The id of the issueDetail
	 * @param question The question text.
	 * @param response the response text.
	 */
	public IssueDetail(int issueDetailID, String question, String response) {
		this.issueDetailID = issueDetailID;
		this.question = question;
		this.response = response;
	}
	

	/**
	 * issueDetailID GETTER and SETTER
	 */
	public int getIssueDetailID() { return issueDetailID; }
	public void setIssueDetailID(int issueDetailID) { this.issueDetailID = issueDetailID; }
	

	/**
	 * question GETTER and SETTER
	 */
	public String getQuestion () { return question; }
	public void setQuestion (String question) { this.question = question; }
	

	/**
	 * response GETTER and SETTER
	 */
	public String getResponse () { return response; }
	public void setResponse (String response) { this.response = response; }
}
	