package itserviceportal.model.datalayer;
import itserviceportal.model.beans.*;
import java.io.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import javax.naming.InitialContext;


/**
 * UserDataAccess.java
 * The database access class which logs a user into the application
 * 
 * Inherits from: DataAccessLayer.java
 *
 * @author Brice Purton, Jonathan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */


public class UserDataAccess extends DataAccessLayer{


    /**
     * Class constructor, calling the Superclass DataAccessLayer to initalise the database =
     * connection, prepared statement and result set objects.
     */
    public UserDataAccess() {
        super();
    }



    public UserDataAccess(Connection connection) {
        super(connection);
    }



    /**
     * This method finds a user with the login credientals passed in and returns the user object if exists
     * to log the user into the application.
     * 
     * @param username The username of the user. Will be a University email (cxxxxxxx@uon.edu.au)
     * @param password The password for that username
     */
    public User loginUser(String username, String password) throws SQLException {

        String query = "SELECT * FROM tbl_User WHERE Email = ? AND UserPassword = ?";
        User user = null;
        try 
        {
            if (connection == null)
                connection = getConnection();

            // Getting the DB connection, performing the query and getting the results
            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            results = statement.executeQuery();

            // Loop through the result set
            while (results.next())
            {
                // Create a user from the results
                int id = results.getInt("UserID");
                String email = results.getString("Email");
                String firstName = results.getString("FirstName");
                String lastName = results.getString("LastName");
                String contactNumber = results.getString("ContactNum");
                String role = results.getString("UserRole");
                user = new User(id, email, firstName, lastName, contactNumber, role);
            
            }
            return user;
        }
        // If any errors occurred close all connections and return null 
        catch(Exception e)
        {
            System.out.println("EXCEPTION CAUGHT: UserDataAccess -- loginUser()");
            throw e;
        }
        finally 
        {
            closeConnections();
        }
    }
}