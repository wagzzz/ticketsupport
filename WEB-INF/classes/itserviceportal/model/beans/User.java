package itserviceportal.model.beans;
import java.lang.*;

/**
 * User bean
 *
 * @author Brice Purton, Jonothan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public class User {
    private int userID;             //The ID of the user
    private String email;           //The University email of the user
    private String firstName;       //The first name of the user              
    private String lastName;        //The last name of the user
    private String contactNumber;   //The phone number of the user
    private Role role;              //The role of the user (USER or STAFF)


    public User() {
        userID = 0;
        email = "";
        firstName = "";
        lastName = "";
        contactNumber = "";
        role = Role.USER;
    }



    /**
	 * Overloaded Constructor
	 *
	 * @param id The ID of the user
	 * @param email The University email of the user
	 * @param fName The first name of the user 
	 * @param lName The last name of the user
     * @param num The phone number of the user
     * @param userRole The role of the user (USER or STAFF) as a String
	 */
    public User(int id, String email, String fName, String lName, String num, String userRole) {
        userID = id;
        this.email = email;
        firstName = fName;
        lastName = lName;
        contactNumber = num;
        setRole(userRole);
    }

    /**
     * userID GETTER and SETTER
     */
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }



    /**
     * userID GETTER and SETTER
     */
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }



    /**
     * firstName GETTER and SETTER
     */
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }



    /**
     * lastName GETTER and SETTER
     */
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }



    /**
     * contactNumber GETTER and SETTER
     */
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }



    /**
     * role GETTER and SETTERS
     */
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public void setRole(String role) {
        if(role.equalsIgnoreCase("staff"))
        {
            this.role = Role.STAFF;
        }
        else{
            this.role = Role.USER;
        }
    }
}