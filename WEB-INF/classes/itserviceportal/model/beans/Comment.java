package itserviceportal.model.beans;

import java.io.Serializable;
import java.util.*;

/**
 * Comment bean
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class Comment implements Serializable {

	private int commentID;		//The ID of the comment
	private String commentText;	//The comment made by the user
	private Date createdOn;		//The date comment was made
	private User createdBy;		//Which user made the comment

	public Comment() {
	}


	/**
	 * Overloaded Constructor
	 *
	 * @param commentID The id of the comment
	 * @param commentText The comment being made
	 * @param createdOn The date the comment was made
	 * @param createdBy The user making the comment
	 */
	public Comment(int commentID, String commentText, Date createdOn, User createdBy) {
		this.commentID = commentID;
		this.commentText = commentText;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
	}


	/**
	 * commentID GETTER and SETTER
	 */
	public int getCommentID() { return commentID; }
	public void setCommentID(int commentID) { this.commentID = commentID; }


	/**
	 * commentText GETTER and SETTER
	 */
	public String getCommentText() { return commentText; }
	public void setCommentText(String text) { this.commentText = text; }


	/**
	 * createdOn GETTER and SETTER
	 */
	public Date getCreatedOn() { return createdOn; }
	public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }


	/**
	 * createdBy GETTER and SETTER
	 */
	public User getCreatedBy() { return createdBy; }
	public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}
