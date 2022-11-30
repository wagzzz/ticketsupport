package itserviceportal.model.datalayer;
import itserviceportal.model.beans.*;
import java.io.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import javax.naming.InitialContext;


/**
 * IssueDetailDataAccess.java
 * The database access class which gets the issue details for a support ticket.
 * Issue details are the questions and the user's reponse to those question, so the user
 * can specify details about the issue they are having. Then, those questions and responses
 * can be displayed on the knowledge base etc.
 * 
 * Inherits from: DataAccessLayer.java
 *
 * @author Brice Purton, Jonathan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */


public class IssueDetailDataAccess extends DataAccessLayer{


  /**
   * Class constructor, calling the Superclass DataAccessLayer to initalise the database =
   * connection, prepared statement and result set objects.
   */
	public IssueDetailDataAccess() {
		super();
	}



	/**
	 * Overloaded constructor
	 */
	public IssueDetailDataAccess(Connection connection) {
		super(connection);
	}




  /**
   * This method inserts new issue details for a support ticket into the database.
   * 
   * @param id The ticketID of the support ticket which the issue details are for.
   * @param issues A Map object which contains key value pairs of (question, response to question)
   * @throws SQLException
   */
	public void newIssueDetails(int id, Map<String, String> issues, boolean doCloseConnection) throws SQLException {

		//Prepare Query
		String query = "INSERT INTO tbl_IssueDetails (QuestionText, ResponseText, TicketID) VALUES (?, ?, ?)";
		
		try 
		{
			if(connection == null)
				connection = getConnection();

			//Prepare Statement
			statement = connection.prepareStatement(query);

			//Cycle through hashmap, and add the question and question response to the database.
			for (Map.Entry<String, String> entry : issues.entrySet()) {
				statement.setString(1, entry.getKey());
				statement.setString(2, entry.getValue());
				statement.setInt(3, id);
				statement.executeUpdate();
			}

			if(doCloseConnection)
				closeConnections();
			else
				closeStatement();
		
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("EXCEPTION CAUGHT: IssueDetailDataAccess -- newIssueDetails");
			closeConnections();
		}
	}




	/**
   * This method gets all the issue details for a specific support ticket.
   * Creates an IssueDetails object from the results set and adds the object to an arrayList
   * 
   * @param ticketID The ticketID of the support ticket which you are getting the issue details for.
   * @throws SQLException
   */
	public ArrayList<IssueDetail> getAllIssueDetailsForTicket(int ticketID, boolean doCloseConnection) throws SQLException {

		//Creating the array list which the issue details will be stored
		ArrayList<IssueDetail> issueDetails = new ArrayList<>();

		//The SELECT query which will get all the issue details from the specific SupportTicket
		String query = "SELECT * FROM tbl_IssueDetails WHERE TicketID = ?";

		try 
		{
			if(connection == null)
				connection = getConnection();

			//Getting the DB connection, performing the query and getting the results
			statement = connection.prepareStatement(query);
			statement.setString(1, Integer.toString(ticketID));
			results = statement.executeQuery();

			//Loop through the result set, create IssueDetail objects and added to the list
			while (results.next()) {
				//Getting the column values from the table
				int id = results.getInt("IssueDetailsID");
				String question = results.getString("QuestionText");
				String response = results.getString("ResponseText");

				// Create the issueDetail from the values
				IssueDetail issueDetail = new IssueDetail(id, question, response);

				//Add the issueDetail to the list of issueDetails
				issueDetails.add(issueDetail);
			}

			//Data processing complete, return the list of IssueDetails
			if(doCloseConnection)
				closeConnections();
			else
			{
				closeStatement();
				closeResults();
			}
			return issueDetails;

		//An exception occures while accessing the DB or processing the data, print the error and close any open connections
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("EXCEPTION CAUGHT: IssueDetailDataAccess -- getAllIssueDetailsForTicket()");
			closeConnections();
			return null;
		}
	}

}