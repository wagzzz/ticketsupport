package itserviceportal.model.datalayer;
import itserviceportal.model.beans.*;
import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import javax.sql.*;
import java.sql.*;
import javax.naming.InitialContext;


/**
 * CommentDataAccess.java
 * The database access class which gets the comments for a support ticket from the MySQL database.
 * 
 * Inherits from: DataAccessLayer.java
 *
 * @author Brice Purton, Jonathan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */


public class CommentDataAccess extends DataAccessLayer{


	/**
   * Class constructor, calling the Superclass DataAccessLayer to initalise the database =
   * connection, prepared statement and result set objects.
   */
	public CommentDataAccess() {
		super();
	}



	public CommentDataAccess(Connection connection) {
		super(connection);
	}


  /**
   * This method gets all the comments for a support ticket from the database.
   * Creates Comment objects from the result set and adds them to an ArrayList.
   * 
   * @param ticketID The ticketID of the support ticket which you are getting comments for.
   * @return ArrayList<Comment> if execution successful.
   * @return null if exception occurs
   * @throws SQLException
   */
	public ArrayList<Comment> getAllCommentsForTicket(int ticketID, boolean doCloseConnection) throws SQLException {

		//The arraylist which will store all the comments for the support ticket
		ArrayList<Comment> comments = new ArrayList<>();

		//The query which will get all the comments from the support ticket with the specified ID
		String query = "SELECT * FROM vw_Comments WHERE TicketID = ?";

		try
		{
			if(connection == null)
				connection = getConnection();

			//Preparing the statement and executing the query to get the list of comments
			statement = connection.prepareStatement(query);
			statement.setString(1, Integer.toString(ticketID));
			results = statement.executeQuery();

			//Loop through the result set and create a comment object each iteration and add to the ArrayList
			while(results.next())
			{
				//Getting the column values from the view
				int id = results.getInt("CommentID");
				String text = results.getString("CommentText");
				Date date = new Date(results.getTimestamp("CommentDate").getTime());
				int userID = results.getInt("UserID");
				String fName = results.getString("FirstName");
				String lName = results.getString("LastName");
				String email = results.getString("Email");
				String num = results.getString("ContactNum");
				String role = results.getString("UserRole");

				//Create the user from the values
				User user = new User(userID, email, fName, lName, num, role);

				//Create the comment from the values
				Comment comment = new Comment(id, text, date, user);

				//Add the comment to the list of comments
				comments.add(comment);
			}

			//Processing is complete
			if(doCloseConnection)
				closeConnections();
			else
			{
				closeStatement();
				closeResults();
			}
			return comments;
		}

		//An exception was caught while trying to access the DB and process the data, print a error and close all open connections
		//Return null
		catch(Exception e)
		{
			System.out.println("EXCEPTION CAUGHT: CommentDataAccess -- getAllCommentsForTicket()");
			closeConnections();
			return null;
		}
	}




	/**
	 * Adds a comment to the Support Ticket
	 *
	 * @param ticketID the ticket the comment is being added to.
	 * @param commentText the comment text being added.
	 * @param userID the ID of the user who is adding comment.
	 * @throws SQLException
	 */
	public void addComment(int ticketID, String commentText, int userID) throws SQLException{

		//The insert statement
		String insert = "INSERT INTO tbl_Comment (CommentText, CommentDate, UserID, TicketID) VALUES (?, NOW(), ?, ?);";

		try
		{
			if(connection == null)
				connection = getConnection();

			//Getting the DB connection, performing the query and getting the results
			statement = connection.prepareStatement(insert);

			//Prepare the insert parameters
			statement.setString(1, commentText);
			statement.setInt(2, userID);
			statement.setInt(3, ticketID);


			//Execute the insert
			statement.execute();
				
			closeConnections();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION CAUGHT: TicketDataAccess -- addComment()");
			closeConnections();
		}
	}

}