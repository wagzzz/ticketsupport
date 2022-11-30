package itserviceportal.model.beans;

/**
 * ENUM class of types of SupportTicket Categoeries
 * There is 5 category types, Network, Software, Hardware, Account and email
 *
 * @author Brice Purton, Jonathan Williams, Wajdi Yournes
 * @version 1.0
 * @since 19-05-2018
 */

public enum Category {
    NETWORK("Network"),
    SOFTWARE("Software"),
    HARDWARE("Hardware"),
    ACCOUNT("Account"),
    EMAIL("Email");

	private String str;
	private Category(String str) { this.str = str; }
	public String getStr() { return str; }
}