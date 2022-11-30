package itserviceportal.model.beans;

/**
 * ENUM class of user roles
 * Users can either be a USER or STAFF.
 * Only IT Staff are assigned the STAFF roles, so they have special privleges within the application.
 *
 * @author Brice Purton, Jonathan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public enum Role {
    USER, STAFF
}