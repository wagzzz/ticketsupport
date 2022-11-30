<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix ="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="customtags" uri="http://localhost:8080/c3180044_c3281849_c3237808_FinalProject/taglib/customtags" %>
<%@ page import="itserviceportal.model.beans.*" %>


<div class="dropdown">
	<c:choose>
		<%-- Display active notification button and dropdown menu if there are notificaitons --%>
		<c:when test="${not empty notifications}">
			<button class="btn btn-notif dropdown-toggle" type="button" id="notifications" data-toggle="dropdown">
				<span class="far fa-bell h5"></span>
				<span class="fas fa-circle notif-dot"></span>
			</button>
			<div id="menu-notif" class="dropdown-menu m-0 p-0 dropdown-menu-left" aria-labelledby="notifications">
				<%-- Iterate through notifications list --%>
				<c:forEach var="notification" items="${notifications}">
					<%-- Clicking on a notification sends a Post request to the Notification controller including the notification ID --%>
					<form class="form-notif form-inline" method="POST" action="Notification?notificationID=${notification.notificationID}">
						<%-- Hidden field for the notification's ticketID --%>
						<input name="ticketID" type="hidden" value="${notification.ticketID}">
						<div class="container-fluid btn-group nopadding">
							<button class="btn btn-block btn-light text-left word-wrap py-0 text-notif" type="submit">
								<span>
									<%-- Custom JSP tag to display notification --%>
									<customtags:notif notification="${notification}"/>
								</span>
								<br>
								<%-- Custom JSP tag to display notification date --%>
								<small class="text-muted"><customtags:date date="${notification.date}"/></small>
							</button>
							<%-- Link to send Get request to Notification controller including the notification ID --%>
							<a class="btn btn-dismiss py-0 d-flex align-items-center" href="Notification?action=dismiss&notificationID=${notification.notificationID}"><span class="fas fa-times align-middle"></span></a>
						</div>
					</form>
				</c:forEach>
			</div>
		</c:when>
		<%-- Display disabled notification button --%>
		<c:otherwise>
			<button class="btn btn-notif dropdown-toggle" type="button" id="notifications" data-toggle="dropdown" disabled>
				<span class="far fa-bell h5"></span>
			</button>
		</c:otherwise>
	</c:choose>
</div>