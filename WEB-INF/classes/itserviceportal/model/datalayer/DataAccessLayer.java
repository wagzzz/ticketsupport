package itserviceportal.model.datalayer;
import itserviceportal.model.beans.*;
import javafx.scene.chart.PieChart.Data;

import javax.sql.*;
import java.sql.*;
import javax.naming.InitialContext;

/**
 * DataAccessLayer.java
 * The database access class. Is a Super class which has the following sub classes:
 * CommentDataAccess.java, IssueDetailDataAccess.java, NotificationDataAccess.java, 
 * TicketDataAccess.java and UserDataAccess.java
 *
 * @author Brice Purton, Jonathan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class DataAccessLayer {

    protected Connection connection;      //The connection to the MySQL database
    protected PreparedStatement statement;  //A perpared statement object which is used to prepare querys/updates/insert
    protected ResultSet results;            //The result set object for processing data obtained from a SELECT query


  /**
   * Class constructor, calling a method to get the connection from the context.xml
   */
    public DataAccessLayer() {
        connection = null;
        statement = null;
        results = null;
    }


    /**
     * Overloaded consructor
     */
    public DataAccessLayer(Connection connection) {
        this.connection = connection;
        statement = null;
        results = null;
    }




  /**
   * This method gets the MySQL database connection from the resource found in the context.xml
   * 
   * @return Connection object if resource is found successfully
   * @return null if exception occurs
   */
    public Connection getConnection() {

        if(connection != null)
            return connection;

        else
        {
            try
            {
                //Lookup the resource in the context.xml by name and get the connection from the data source
                InitialContext context = new InitialContext();
                DataSource ds = (DataSource)context.lookup("java:/comp/env/ServicePortalDB");
                return ds.getConnection();
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }



  /**
   * This method closes all open connections to the MySQL database.
   * If the member varaibles are not null, close the connections
   * 
   * @throws SQLException
   */
    protected void closeConnections() throws SQLException {
        if(connection != null)
            connection.close();
        else if (statement != null)
            statement.close();
        else if(results != null)
            statement.close();
    }


    protected void closeConnection() throws SQLException {
        if(connection != null)
            connection.close();
    }

    protected void closeStatement() throws SQLException {
        if (statement != null)
            statement.close();
    }

    protected void closeResults() throws SQLException {
        if(results != null)
            results.close();
    }
}

