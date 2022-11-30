package itserviceportal.model.datalayer;
import itserviceportal.model.beans.*;
import java.io.*;
import java.util.*;
import javax.sql.*;
import java.util.Date;
import java.sql.*;
import javax.naming.InitialContext;



/**
 * TicketDataAccess.java
 * The database access class which adds, updates, gets list and gets single support tickets inside the database
 * 
 * Inherits from: DataAccessLayer.java
 *
 * @author Brice Purton, Jonathan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */


public class TicketDataAccess extends DataAccessLayer{

	/**
	 * Class constructor, calling the Superclass DataAccessLayer to initalise the database =
	 * connection, prepared statement and result set objects.
	 */
	public TicketDataAccess() {
		super();
	}



	public TicketDataAccess(Connection connection) {
		super(connection);
	}




	/**
	* This method adds a new SupportTicket to the database
	* 
	* @param user The user object who is creating Support Ticket
	* @param category The category of the support ticket (Network, Email etc etc)
	* @param title The title of the support ticket
	* @param description The description of the support ticket
	* @param issueDetails The Map object of questions and response for the support ticket, 
	*           			 outlining the issues / problems which need fixing.
	* @throws SQLException
	*/
	public void newTicket(User user, String category, String title, String description, Map<String, String> issueDetails) throws SQLException {
		
		//String Query
		String query = "INSERT INTO tbl_SupportTicket (Title, Descrip, ReportedOn, CreatedByUserID, CategoryID) VALUES (?, ?, ?, ?, ?)";
		
		try 
		{
			if(connection == null)
				connection = getConnection();

			//Prepare Statement
			statement = connection.prepareStatement(query);
			
			//Set Statement
			statement.setString(1, title);
			statement.setString(2, description);
			statement.setTimestamp(3, new Timestamp(new Date().getTime()));
			statement.setInt(4, user.getUserID());
			statement.setInt(5, getCategoryID(category));
			
			//Execute Statement, adding ticket to DB
			statement.executeUpdate();
			statement.close();
			
			//Creating statement to retrieve ticketID
			Statement statement = connection.createStatement();
			query = "SELECT LAST_INSERT_ID()";
			results = statement.executeQuery(query);

			//Getting the ticketID from the result
			results.next();
			int ticketID = results.getInt("LAST_INSERT_ID()");
			
			//Add IssueDetails for ticket to DB using the ticketID retrieved by last insert
			//Calling the IssueDetailDataAccess to handle the processing of the insert.
			IssueDetailDataAccess issueDetailDAL = new IssueDetailDataAccess(connection);
			issueDetailDAL.newIssueDetails(ticketID, issueDetails, true);

			closeConnections();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("EXCEPTION CAUGHT: TicketDataAccess -- newTicket");
			closeConnections();
		}
			
	}





	/**
	 * Gets all the tickets from the database matching the passed in filter parameters
	 *
	 * @param user the user getting the list of tickets
	 * @param categorySelect the category filter select (All, Network, Hardware etc)
	 * @param stateSelect The state filter selected (Completed, New etc)
	 * @param knowledgeBase value outlining to get all tickets or only knowledge base (true = knowledge base only, false = all tickets)
	 * @param orderBy the order by filter selected (newest or oldest)
	 * @throws SQLException
	 * @return ArrayList<SupportTicket> if execution successful
	 * @return null if exception is caught.
	 */
	public ArrayList<SupportTicket> getAllTicketsFromDB(User user, String categorySelect, String stateSelect, boolean isKnowledgeBase, String orderBy) throws SQLException {
		
		//The list of tickets that will be returned
		ArrayList<SupportTicket> ticketsList = new ArrayList<>();
		
		String query;

		//Getting all knowledge base articles, do not need to filter by userID or TicketState
		if(isKnowledgeBase)
		{
			if(orderBy.equals("newest"))
				query = "SELECT * FROM vw_SupportTickets WHERE CategoryName LIKE ? AND IsKnowledgeBase = 1 ORDER BY ReportedOn DESC;";
			else
				query = "SELECT * FROM vw_SupportTickets WHERE CategoryName LIKE ? AND IsKnowledgeBase = 1 ORDER BY ReportedOn ASC;";
		}

		//Otherwise, getting all the tickets by the userID
		else
		{
			if(orderBy.equals("newest"))
				query = "SELECT * FROM vw_SupportTickets WHERE CreatedByUserID LIKE ? AND CategoryName LIKE ? AND TicketState LIKE ? ORDER BY ReportedOn DESC";
			else
				query = "SELECT * FROM vw_SupportTickets WHERE CreatedByUserID LIKE ? AND CategoryName LIKE ? AND TicketState LIKE ? ORDER BY ReportedOn ASC";
		}



		try
		{
			if(connection == null)
				connection = getConnection();

			//Getting the DB connection, performing the query and getting the results
			statement = connection.prepareStatement(query);

			//If the user requesting is a staff member, a wildcard will be returned to allow the access of all tickets,
			//otherwise, the user will only be able to view tickets created by his id
			String[] filterValues = buildQueryStringValuesGetAllTickets(user, categorySelect, stateSelect, orderBy);

			//Prepare the query parameters
			//If not getting knowledge base articles, set the userID, will be wildcard if staff, or id if regular USER
			if(!isKnowledgeBase)
			{
				statement.setString(1, filterValues[0]);
				statement.setString(2, filterValues[1]);
				statement.setString(3, filterValues[2]);
			}

			//Otherwise, just setting the category filter
			else
			{
				statement.setString(1, filterValues[1]);
			}
			

			//Execute the query and get the results
			results = statement.executeQuery();

			//Loop through the results set and create Support Tickets using the ticket factory method and add to list
			while (results.next())
			{
				//Passing in false to the ticket factory so the ticket factory won't load comments or issue details
				//as this is only a list of support tickets, that information is not needed and speeds up execution
				SupportTicket ticket = supportTicketFactory(results, false);
				if(ticket != null)
					ticketsList.add(ticket);
			}
			closeConnections();
			return ticketsList;
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION CAUGHT: TicketDataAccess -- getAllTicketsFromDB()");
			closeConnections();
			return null;
		}
	}




	/**
	 * Gets a ticket from the database matching the specified ticketID
	 *
	 * @param id The SupportTicketID accessing
	 * @param user the user getting the ticket
	 * @param isKnowledgeBase value outlining if the ticket being accessed is a knowledge base article
	 * @throws SQLException
	 * @return SupportTicket object if successful, NULL if exception occured
	 */
	public SupportTicket getTicketByIDFromDB(int id, User user, boolean isKnowledgeBase) throws SQLException {
		
		SupportTicket ticket = null;
		String query = "";

		//If the request is for a knowledge base article, get the Ticket by ID and where is knowledgebase
		if(isKnowledgeBase)
		{
			query = "SELECT * FROM vw_SupportTickets WHERE TicketID = ? AND IsKnowledgeBase = 1;";
		}

		//Otherwise, a user is viewing a normal support ticket
		else
		{
			//If the user is a staff, they can view all tickets, otherwise, a user can only view their own tickets
			if(user.getRole() == Role.STAFF)
				query = "SELECT * FROM vw_SupportTickets WHERE TicketID = ?;";
			else
				query = "SELECT * FROM vw_SupportTickets WHERE TicketID = ? AND CreatedByUserID = ?;";
		}
		

		try
		{
			if(connection == null)
				connection = getConnection();

			//Getting the DB connection, performing the query and getting the results
			statement = connection.prepareStatement(query);

			//Prepare the query parameters
			//Setting the TicketID
			statement.setInt(1, id);

			//If the user getting the ticket by ID is a USER, set the extra parameter
			if(!isKnowledgeBase && user.getRole() == Role.USER)
				statement.setInt(2, user.getUserID());
				
			//Execute the query and get the results
			results = statement.executeQuery();

			//Get the row and create the ticket object with the factory method
			//Passing in true to the factory method to indicate that all comments and issue details
			//will be retrieved.
			if (results.next()) {
				ticket = supportTicketFactory(results, true);
			}
				
			closeConnections();
			return ticket;
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION CAUGHT: TicketDataAccess -- getTicketByIDFromDB()");
			closeConnections();
			return null;
		}
	}






	/**
	 * Helper method for the SupportTicketFactory.
	 * Creates a User object from the result, represents the user who created the support ticket
	 *
	 * @param results the result set obtained from the database
	 * @return User
	 */
	private User getCreatedByUserFromResults(ResultSet results) throws SQLException {
		int id = results.getInt("CreatedByUserID");
		String email = results.getString("CreatedByEmail");
		String fName = results.getString("CreatedByFName");
		String lName = results.getString("CreatedByLName");
		String contactNum = results.getString("CreatedByContactNum");
		String role = results.getString("CreatedByRole");
		User user = new User(id, email, fName, lName, contactNum, role);
		return user;
	}






	/**
	 * Helper method for the SupportTicketFactory.
	 * Creates a User object from the result, represents the user who resolved the support ticket
	 *
	 * @param results the result set obtained from the database
	 * @return User
	 */
	private User getResolvedByUserFromResults(ResultSet results) throws SQLException {
		int id = results.getInt("ResolvedByUserID");
		String email = results.getString("ResolvedByEmail");
		String fName = results.getString("ResolvedByFName");
		String lName = results.getString("ResolvedByLName");
		String contactNum = results.getString("ResolvedByContactNum");
		String role = results.getString("ResolvedByRole");
		User user = new User(id, email, fName, lName, contactNum, role);
		return user;
	}





	/**
	 * Helper method for getAllTicketsFromDB
	 * Makes the query string values which will be passed into the 
	 * select statement in order to perform filtering.
	 *
	 * @param user the user getting the list of tickets
	 * @param categorySelect the category filter select (All, Network, Hardware etc)
	 * @param stateSelect The state filter selected (Completed, New etc)
	 * @param knowledgeBase value outlining to get all tickets or only knowledge base (true = knowledge base only, false = all tickets)
	 * @param orderBy the order by filter selected (newest or oldest)
	 * @return String[] values will be an SQL wildcard '%' if matching "ALL" criteria for the filter, otherwise, the value passed in will remain
	 */
	private String[] buildQueryStringValuesGetAllTickets(User user, String categorySelect, String stateSelect, String orderBy) {
		String[] queryValues = new String[4];

		//If the role is just a user, they can only view the tickets they have made
		if(user.getRole() == Role.USER)
			queryValues[0] = Integer.toString(user.getUserID());

		//Otherwise, the user is staff member so display everything using a wildcard
		else
			queryValues[0] = "%";


		//If the state selected is all, use a wildcard to find everything, otherwise, use the value passed in
		if(stateSelect.equals("all"))
			queryValues[1] = "%";
		else
			queryValues[1] = stateSelect;



		//If the categorySelected is "all" then use a wildcard to find everything
		if(categorySelect.equals("all"))
			queryValues[2] = "%";
		//Otherwise, use the valids selected
		else
			queryValues[2] = categorySelect;

		return queryValues;
	}





	/**
	 * Helper Factory Method for getAllTicketsFromDB() and getTicketByIDFromDB()
	 * Creates a SupportTicket object from the result set passed in.
	 *
	 * @param results the results set obtained from the database which contains the Support Ticket information
	 * @param getCommentsAndIssueDetails a value outlining if the factory method should create the SupportTicket object
	 * 									 with all comments and issue details, or just load support ticket information 
	 * 									 to speed up processing time. 
	 * @return SupportTicket if created successfully, NULL if exception occured
	 */
	private SupportTicket supportTicketFactory(ResultSet results, boolean getCommentsAndIssueDetails) {
		try
		{
			//Create the support ticket and get the values from the results
			int id = results.getInt("TicketID");
			String title = results.getString("Title");
			String desc = results.getString("Descrip");
			String state = results.getString("TicketState");
			Date reportedOn = null;
			Timestamp ts = results.getTimestamp("ReportedOn");
			if (ts != null) {
				reportedOn = new Date(ts.getTime());
			}
			Date resolvedOn = null;
			ts = results.getTimestamp("ResolvedOn");
			if (ts != null) {
				resolvedOn = new Date(ts.getTime());
			}
			Boolean isKnowledgeBase = results.getBoolean("IsKnowledgeBase");
			String resolutionDetails = results.getString("ResolutionDetails");
			int catID = results.getInt("CategoryID");
			String catName = results.getString("CategoryName");

			//Getting the created by user values from the query and creating the user object
			User createdByUser = getCreatedByUserFromResults(results);

			//Getting the resolved by user values from the query
			User resolvedByUser = null;

			//If the resolvedOn date is not null then there is a resolved by user information
			if(resolvedOn != null)
				resolvedByUser = getResolvedByUserFromResults(results);


			//If we are viewing a ticket individually get all the comments and issue details, otherwise, just get the ticket information
			ArrayList<IssueDetail> issueDetails = null;
			ArrayList<Comment> comments = null;
			if(getCommentsAndIssueDetails)
			{
				//Calling the comments data access to get all the comments for this ticket
				IssueDetailDataAccess issueDetailDAL = new IssueDetailDataAccess(connection);
				issueDetails = issueDetailDAL.getAllIssueDetailsForTicket(id, false);

				//Calling the comments data access to get all the comments for this ticket
				CommentDataAccess commentDAL = new CommentDataAccess(connection);
				comments = commentDAL.getAllCommentsForTicket(id, false);
			}

			//Creating the support ticket from the values retrieved from the query
			SupportTicket ticket = new SupportTicket(id, catName, state, title, desc, reportedOn, createdByUser, resolvedOn, resolvedByUser, isKnowledgeBase, resolutionDetails, issueDetails, comments);

			return ticket;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	



	/**
	 * Method which gets the category ID
	 *
	 * @param category the category searching for. 
	 * @return int The ID of the category in the database
	 */
	public int getCategoryID(String category) {
		switch (category) { 
			case "network": return 1;
			case "software": return 2;
			case "hardware": return 3;
			case "email": return 4;
			case "account": return 5;
			default: return 0;
		}
	}





	/**
	 * Updates the ticket state to inprogress.
	 * When the Staff click "Start Work", the ticket state is updated to in progress
	 *
	 * @param ticketID the ticket which is being updated
	 * @throws SQLException
	 */
	public void updateTicketStateToInProgress(int ticketID) throws SQLException {

		//Setting the update statement
		String update = "UPDATE tbl_SupportTicket SET TicketState = 'in progress' WHERE TicketID = ?;";

		try
		{
			if(connection == null)
				connection = getConnection();

			//Getting the DB connection, and perparing the update statement
			statement = connection.prepareStatement(update);

			//Prepare the update parameter
			statement.setInt(1, ticketID);

			//Execute the update and close all open connections
			statement.execute();
			closeConnections();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION CAUGHT: TicketDataAccess -- updateTicketStateToInProgress()");
			closeConnections();
		}
	}




	/**
	 * Updates the ticket state to completed.
	 * When the Staff click "Submit Solution", the ticket state is updated to completed
	 *
	 * @param ticketID the ticket which is being updated
	 * @param resolutionDetail The solution text submitted by the Staff member.
	 * @param resolvedByUserID The ID of the staff member who submitted the solution
	 * @throws SQLException
	 */
	public void updateTicketStateToComplete(int ticketID, String resolutionDetails, int resolvedByUserID) throws SQLException {

		//Setting the update statement
		//Inserting resolution details, when the solution was provided and who provided the solution
		String update = "UPDATE tbl_SupportTicket SET TicketState = 'completed', ResolutionDetails = ?, ResolvedOn = NOW(), ResolvedByUserID = ? WHERE TicketID = ? AND TicketState = 'in progress';";

		try
		{
			if(connection == null)
				connection = getConnection();

			//Getting the DB connection, and perparing the insert statement
			statement = connection.prepareStatement(update);

			//Prepare the update parameter
			statement.setString(1, resolutionDetails);
			statement.setInt(2, resolvedByUserID);
			statement.setInt(3, ticketID);
			


			//Execute the insert
			statement.execute();
				
			closeConnections();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION CAUGHT: TicketDataAccess -- updateTicketStateToComplete()");
			closeConnections();
		}
	}




	/**
	 * Updates the ticket state to accepted.
	 * When the user clicks "Accept Solution", the ticket state is updated to resolved
	 *
	 * @param ticketID the ticket which is being updated
	 * @throws SQLException
	 */
	public void updateTicketStateToAccepted(int ticketID) throws SQLException {

		//Setting the update statement
		String update = "UPDATE tbl_SupportTicket SET TicketState = 'resolved' WHERE TicketID = ? AND TicketState = 'completed';";

		try
		{
			if(connection == null)
				connection = getConnection();

			//Getting the DB connection, and perparing the update statement
			statement = connection.prepareStatement(update);

			//Prepare the update parameter
			statement.setInt(1, ticketID);

			//Execute the insert
			statement.execute();
				
			closeConnections();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION CAUGHT: TicketDataAccess -- updateTicketStateToAccepted()");
			closeConnections();
		}
	}





	/**
	 * Updates the ticket state to rejected.
	 * When the user clicks "Reject Solution", the ticket state is updated back to inprogress
	 * and resolution details, resolvedOn, resolvedByID and knowledgebase are cleared from the ticket.
	 *
	 * @param ticketID the ticket which is being updated
	 * @throws SQLException
	 */
	public void updateTicketStateToRejected(int ticketID) throws SQLException {

		//Setting the update statement
		//Removing from knowledge base because only tickets which are in state completed or resolved can be in the knowledge base
		String update = "UPDATE tbl_SupportTicket SET TicketState = 'in progress', ResolvedOn = NULL, ResolvedByUserID = NULL, ResolutionDetails = NULL, IsKnowledgeBase = 0 WHERE TicketID = ? AND TicketState = 'completed';";

		try
		{
			if(connection == null)
				connection = getConnection();

			//Getting the DB connection, and perparing the insert statement
			statement = connection.prepareStatement(update);

			//Prepare the update parameter
			statement.setInt(1, ticketID);

			//Execute the insert
			statement.execute();
				
			closeConnections();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION CAUGHT: TicketDataAccess -- updateTicketStateToRejected()");
			closeConnections();
		}
	}





	/**
	 * Adds or removes a ticket from the knowledge base.
	 *
	 * @param ticketID the ticket which is being updated
	 * @param doAddToKnowledgeBase a value outlining if adding to knowledge base or removing from knowledge base
	 * 							   (true = add to knowledge base, false = remove from knowledge base)
	 * @throws SQLException
	 */
	public void AddOrRemoveFromKnowledgeBase(int ticketID, boolean doAddToKnowledgeBase) throws SQLException {

		//Setting the update statement
		String update = "UPDATE tbl_SupportTicket SET IsKnowledgeBase = ? WHERE TicketID = ? AND (TicketState = 'completed' OR TicketState = 'resolved');";

		try
		{
			if(connection == null)
				connection = getConnection();
			
			//Getting the DB connection, and perparing the insert statement
			statement = connection.prepareStatement(update);

			//Prepare the update parameter
			statement.setBoolean(1, doAddToKnowledgeBase);
			statement.setInt(2, ticketID);

			//Execute the update and close all open connections
			statement.execute();
			closeConnections();
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION CAUGHT: TicketDataAccess -- AddOrRemoveFromKnowledgeBase()");
			closeConnections();
		}
	}



	/**
	 * Method to retrieve knowledge base articles from the database that contain
	 * words in their titles or descriptions matching words in term.
	 *
	 * @param term string containing words to match against
	 * @throws SQLException
	 * @return Arraylist of SupportTickets which are articles similar to term
	 */
	public ArrayList<SupportTicket> getSuggestedArticles(String term) throws SQLException {

		ArrayList<SupportTicket> ticketsList = new ArrayList<>();

		String queryStart = "SELECT * FROM vw_SupportTickets WHERE (";
		String queryEnd = ") AND IsKnowledgeBase = 1 ORDER BY ResolvedOn DESC;";
		String querySearch = "";

		// Split term into words
		ArrayList<String> terms = new ArrayList<String>(Arrays.asList(term.split(" ")));

		// Remove duplicates
		Set<String> removeDuplicates = new HashSet<>();
		removeDuplicates.addAll(terms);
		terms.clear();
		terms.addAll(removeDuplicates);

		// Format words and add insertion positions to query statement
		boolean isFirst = true;
		ListIterator<String> it = terms.listIterator();
		while (it.hasNext()) {
			String word = it.next();
			// Check if word is worth searching for
			if (word.length() >= 3) {
				it.set("%" + word +"%");
				if (isFirst) {
					querySearch += " Title LIKE ? OR Descrip LIKE ?";
					isFirst ^= true;
				} else {
					querySearch += "  OR Title LIKE ? OR Descrip LIKE ?";
				}
			} else {
				it.remove();
			}
		}

		if (terms.isEmpty()) {
			// No words worth serching for so don't try query
			return null;
		}

		String query = queryStart + querySearch + queryEnd;

		try {
			if(connection == null)
				connection = getConnection();
			
			statement = connection.prepareStatement(query);
			// Insert search terms into statement
			for (int i=0, j=1; i < terms.size(); i++, j+=2) {
				statement.setString(j, terms.get(i));
				statement.setString(j+1, terms.get(i));
			}
			results = statement.executeQuery();

			while (results.next()) {
				SupportTicket ticket = supportTicketFactory(results, false);
				if (ticket != null) {
					ticketsList.add(ticket);
				}
			}
			return ticketsList;
		} catch(Exception e) {
			throw e;
		} finally {
			closeConnections();
		}
	}

}