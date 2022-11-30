package itserviceportal.customtags;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.SkipPageException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class DateFormatTag extends SimpleTagSupport {

	private Date date;

	public DateFormatTag() {
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public void doTag() throws JspException, IOException {
		if (date == null) {
			return;
		}

		Date now =	new Date();
		// Get time difference in minutes and hours
		long diff = now.getTime() - date.getTime();
		long diffMins = diff / (60 * 1000);
		long minutes = diff / (60 * 1000) % 60;
		long hours = diff / (60 * 60 * 1000) % 24;

		// Display time then date
		DateFormat df = new SimpleDateFormat("h:mma dd/MM/yyyy");
		String formattedDate = df.format(date).replace("AM", "am").replace("PM","pm");

		// If less than an hour ago then display as minutes ago
		if (diffMins < 2) {
			formattedDate = "1 minute ago";
		} else if (diffMins < 60) {
			formattedDate = minutes + " minutes ago";
		// If 6 or less hours ago then display as hours ago
		} else if (diffMins < 120) {
			formattedDate = "1 hour ago";
		} else if (diffMins <= 360) {
			formattedDate = hours + " hours ago";
		}

		// Output to jsp
		getJspContext().getOut().write(formattedDate);
	}
}